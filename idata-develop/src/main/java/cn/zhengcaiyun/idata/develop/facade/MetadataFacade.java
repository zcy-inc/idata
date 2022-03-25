package cn.zhengcaiyun.idata.develop.facade;

import cn.hutool.core.util.StrUtil;
import cn.zhengcaiyun.idata.commons.enums.WhetherEnum;
import cn.zhengcaiyun.idata.commons.exception.GeneralException;
import cn.zhengcaiyun.idata.commons.pojo.PojoUtil;
import cn.zhengcaiyun.idata.connector.bean.dto.ColumnInfoDto;
import cn.zhengcaiyun.idata.connector.clients.hive.model.MetadataInfo;
import cn.zhengcaiyun.idata.connector.clients.hive.util.JiveUtil;
import cn.zhengcaiyun.idata.connector.spi.hive.HiveService;
import cn.zhengcaiyun.idata.connector.spi.hive.dto.CompareInfoDTO;
import cn.zhengcaiyun.idata.connector.spi.hive.dto.SyncHiveDTO;
import cn.zhengcaiyun.idata.develop.dal.model.DevColumnInfo;
import cn.zhengcaiyun.idata.develop.dal.model.DevLabel;
import cn.zhengcaiyun.idata.develop.dal.model.DevTableInfo;
import cn.zhengcaiyun.idata.develop.dto.label.SysLabelCodeEnum;
import cn.zhengcaiyun.idata.develop.dto.table.TableInfoDto;
import cn.zhengcaiyun.idata.develop.service.label.EnumService;
import cn.zhengcaiyun.idata.develop.service.label.LabelService;
import cn.zhengcaiyun.idata.develop.service.table.ColumnInfoService;
import cn.zhengcaiyun.idata.develop.service.table.TableInfoService;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.commons.enums.DeleteEnum.DEL_NO;


@Service
public class MetadataFacade {
    private static final Logger logger = LoggerFactory.getLogger(MetadataFacade.class);

    private final String COLUMN_TYPE_ENUM = "hiveColTypeEnum:ENUM";

    @Autowired
    private TableInfoService tableInfoService;

    @Autowired
    private HiveService hiveService;

    @Autowired
    private LabelService labelService;

    @Autowired
    private ColumnInfoService columnInfoService;

    @Autowired
    private EnumService enumService;

    @Transactional(rollbackFor = Throwable.class)
    public SyncHiveDTO syncMetadataToHive(Long tableId, String operator) {
        DevTableInfo tableInfo = tableInfoService.getSimpleById(tableId);
        String hiveTableName = tableInfo.getHiveTableName();
        String dbName = labelService.getDBName(tableId);

        checkDatabase(hiveTableName, dbName);

        String tableName = tableInfo.getTableName();

        Map<String, String> typeMapping = enumService.getCodeMapByEnumValue(COLUMN_TYPE_ENUM);
        Map<String, String> lowerCaseMapping = columnInfoService.getColumnNames(tableId).stream().collect(Collectors.toMap(e -> e.toLowerCase(), e -> e));
        // 1. 没有同步hive记录直接新建
        if (StringUtils.isBlank(hiveTableName)) {
            boolean existHiveTable = hiveService.existHiveTable(dbName, tableName);
            if (existHiveTable) {
                throw new GeneralException("远端hive表已存在，无法新建");
            }
            boolean success = hiveService.create(tableInfoService.getTableDDL(tableId));
            if (success) {
                MetadataInfo metadataInfo = hiveService.getMetadataInfo(dbName, tableName);
                List<DevLabel> labelList = Lists.newArrayList();
                Map<String, Long> nameOfIdMap = columnInfoService.getColumnInfo(tableId).stream().collect(Collectors.toMap(DevColumnInfo::getColumnName, DevColumnInfo::getId));
                metadataInfo.getColumnList().forEach(e -> {
                    ColumnInfoDto columnInfoDto = new ColumnInfoDto();
                    BeanUtils.copyProperties(e, columnInfoDto);
                    columnInfoDto.setPartition(false);
                    columnInfoDto.setColumnId(nameOfIdMap.get(columnInfoDto.getColumnName()));
                    List<DevLabel> subList = assembleDevLabelList(lowerCaseMapping, typeMapping, operator, tableId, columnInfoDto);
                    labelList.addAll(subList);
                });
                metadataInfo.getPartitionColumnList().forEach(e -> {
                    ColumnInfoDto columnInfoDto = new ColumnInfoDto();
                    BeanUtils.copyProperties(e, columnInfoDto);
                    columnInfoDto.setPartition(true);
                    columnInfoDto.setColumnId(nameOfIdMap.get(columnInfoDto.getColumnName()));
                    List<DevLabel> subList = assembleDevLabelList(lowerCaseMapping, typeMapping, operator, tableId, columnInfoDto);
                    labelList.addAll(subList);
                });
                labelService.batchUpsert(labelList);
                tableInfoService.updateHiveTableName(tableId, dbName + "." + tableName, operator);
            }
            return new SyncHiveDTO(true);
        }

        hiveTableName = hiveTableName.split("\\.")[1];
        SyncHiveDTO syncHiveDTO = new SyncHiveDTO(false);
        // 2. 重命名
        if (!StringUtils.equals(tableName, hiveTableName)) {

            boolean existSource = hiveService.existHiveTable(dbName, hiveTableName);
            if (!existSource) {
                throw new GeneralException(String.format("该表已存在同步至hive记录（表名：%s），但该表不存在，无法重命名", hiveTableName));
            }
            boolean existTarget = hiveService.existHiveTable(dbName, tableName);
            if (existTarget) {
                throw new GeneralException(String.format("修改的目标表名远端hive库中已存在（表名：%s），无法重命名", tableName));
            }
            boolean success = hiveService.rename(dbName, hiveTableName, tableName);
            if (!success) {
                throw new GeneralException(String.format("重命名失败（%s --> %s）", hiveTableName, tableName));
            }
            tableInfoService.updateHiveTableName(tableId, dbName + "." + tableName, operator);
            syncHiveDTO.getWarningList().add(String.format("重命名表：`%s`.%s ---> `%s`.%s", dbName, hiveTableName, dbName, tableName));
        }

        CompareInfoDTO compareInfoDTO = compareHive(tableId);
        // 3. 单向同步列信息（local --> hive库）
        // 3.1 新增列
        List<CompareInfoDTO.BasicColumnInfo> moreList = compareInfoDTO.getMoreList();
        if (CollectionUtils.isNotEmpty(moreList)) {
            Set<ColumnInfoDto> columns = moreList.stream()
                    .filter(e -> !e.isPartition())
                    .map(e -> {
                        ColumnInfoDto columnInfoDto = new ColumnInfoDto();
                        BeanUtils.copyProperties(e, columnInfoDto);
                        return columnInfoDto;
                    })
                    .collect(Collectors.toSet());
            boolean success = hiveService.addColumns(dbName, tableName, columns);
            if (success) {
                List<DevLabel> labelList = Lists.newArrayList();
                columns.forEach(e -> {
                    List<DevLabel> subList = assembleDevLabelList(lowerCaseMapping, typeMapping, operator, tableId, e);
                    labelList.addAll(subList);
                });
                labelService.batchUpsert(labelList);
            }
        }
        // 3.2 修改列
        List<CompareInfoDTO.ChangeColumnInfo> differentList = compareInfoDTO.getDifferentList()
                .stream()
                .filter(e -> !e.isHivePartition())
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(differentList)) {
            for (CompareInfoDTO.ChangeColumnInfo columnInfo : differentList) {
                String columnName = columnInfo.getColumnName();
                String columnType = columnInfo.getColumnType();
                String columnComment = columnInfo.getColumnComment();
                String hiveColumnName = columnInfo.getHiveColumnName();
                boolean success = hiveService.changeColumn(dbName, tableName, hiveColumnName,
                        columnName, columnType, columnComment);
                if (success) {
                    //删除被改名的列相关信息 column_id+历史column_name
                    labelService.deleteDeprecatedHiveColumn(columnInfo.getColumnId(), hiveColumnName);
                    ColumnInfoDto columnInfoDto = new ColumnInfoDto();
                    BeanUtils.copyProperties(columnInfo, columnInfoDto);
                    List<DevLabel> labelList = assembleDevLabelList(lowerCaseMapping, typeMapping, operator, tableId, columnInfoDto);
                    labelService.batchUpsert(labelList);
                }
            }
        }

        syncHiveDTO.setCompareInfoDTO(compareInfoDTO);
        return syncHiveDTO;
    }

    /**
     * 封装label
     * @param lowerCaseMapping
     * @param operator
     * @param tableId
     * @param columnInfoDto
     */
    private List<DevLabel> assembleDevLabelList(Map<String, String> lowerCaseMapping, Map<String, String> typeMapping,
                                                String operator, Long tableId, ColumnInfoDto columnInfoDto) {
        // 由于hive大小写不敏感，取出的建标DDL都是小写，此处做了映射
        String columnName = lowerCaseMapping.getOrDefault(columnInfoDto.getColumnName().toLowerCase(), columnInfoDto.getColumnName());
        String columnType = columnInfoDto.getColumnType();
        String columnComment = columnInfoDto.getColumnComment();
        Boolean partition = columnInfoDto.getPartition();
        Long columnId = columnInfoDto.getColumnId();

        Date now = new Date();
        List<DevLabel> list = new ArrayList<>();
        DevLabel devLabelName = new DevLabel();
        devLabelName.setDel(DEL_NO.val);
        devLabelName.setEditor(operator);
        devLabelName.setCreator(operator);
        devLabelName.setEditTime(now);
        devLabelName.setCreateTime(now);
        devLabelName.setTableId(tableId);
        devLabelName.setColumnId(columnId);
        devLabelName.setColumnName(columnName);
        devLabelName.setLabelCode(SysLabelCodeEnum.HIVE_COLUMN_NAME_LABEL.labelCode);
        devLabelName.setLabelParamValue(columnName);
        devLabelName.setHidden(WhetherEnum.YES.val);
        list.add(devLabelName);

        DevLabel devLabelType = new DevLabel();
        devLabelType.setDel(DEL_NO.val);
        devLabelType.setEditor(operator);
        devLabelType.setCreator(operator);
        devLabelType.setEditTime(now);
        devLabelType.setCreateTime(now);
        devLabelType.setTableId(tableId);
        devLabelType.setColumnId(columnId);
        devLabelType.setColumnName(columnName);
        devLabelType.setLabelCode(SysLabelCodeEnum.HIVE_COLUMN_TYPE_LABEL.labelCode);
        devLabelType.setLabelParamValue(typeMapping.getOrDefault(StringUtils.upperCase(columnType), columnType));
        devLabelType.setHidden(WhetherEnum.YES.val);
        list.add(devLabelType);

        DevLabel devLabelComment = new DevLabel();
        devLabelComment.setDel(DEL_NO.val);
        devLabelComment.setEditor(operator);
        devLabelComment.setCreator(operator);
        devLabelComment.setEditTime(now);
        devLabelComment.setCreateTime(now);
        devLabelComment.setTableId(tableId);
        devLabelComment.setColumnId(columnId);
        devLabelComment.setColumnName(columnName);
        devLabelComment.setLabelCode(SysLabelCodeEnum.HIVE_COLUMN_COMMENT_LABEL.labelCode);
        devLabelComment.setLabelParamValue(StrUtil.nullToEmpty(columnComment));
        devLabelComment.setHidden(WhetherEnum.YES.val);
        list.add(devLabelComment);

        DevLabel devLabelPartition = new DevLabel();
        devLabelPartition.setDel(DEL_NO.val);
        devLabelPartition.setEditor(operator);
        devLabelPartition.setCreator(operator);
        devLabelPartition.setEditTime(now);
        devLabelPartition.setCreateTime(now);
        devLabelPartition.setTableId(tableId);
        devLabelPartition.setColumnId(columnId);
        devLabelPartition.setColumnName(columnName);
        devLabelPartition.setLabelCode(SysLabelCodeEnum.HIVE_PARTITION_COL_LABEL.labelCode);
        devLabelPartition.setLabelParamValue(partition + "");
        devLabelPartition.setHidden(WhetherEnum.YES.val);
        list.add(devLabelPartition);
        return list;
    }

    /**
     * 比较表数据信息
     * @param tableId
     * @return
     */
    public CompareInfoDTO compareHive(Long tableId) {
//        List<String> columnList = columnInfoService.getColumnNames(tableId);
        List<DevColumnInfo> columnInfoList = columnInfoService.getColumnInfo(tableId);
        List<Long> columnIdList = new ArrayList<>();
        Map<Long, String> nameOfId = new HashMap<>();
        columnInfoList.forEach(e -> {
            columnIdList.add(e.getId());
            nameOfId.put(e.getId(), e.getColumnName());
        });

        List<DevLabel> labelList = labelService.findByTableId(tableId);

        List<String> filterList = Lists.newArrayList(
                SysLabelCodeEnum.COLUMN_COMMENT_LABEL.labelCode,
                SysLabelCodeEnum.COLUMN_TYPE_LABEL.labelCode,
                SysLabelCodeEnum.HIVE_COLUMN_COMMENT_LABEL.labelCode,
                SysLabelCodeEnum.HIVE_COLUMN_NAME_LABEL.labelCode,
                SysLabelCodeEnum.HIVE_COLUMN_TYPE_LABEL.labelCode,
                SysLabelCodeEnum.PARTITION_COL_LABEL.labelCode,
                SysLabelCodeEnum.HIVE_PARTITION_COL_LABEL.labelCode);

        //<column, code, value> 结构COLUMN_COMMENT_LABEL
        Table<Long, String, String> columnCodeValueTable = HashBasedTable.create();
        labelList.stream()
                .filter(e -> e.getColumnId() != null && StringUtils.isNotEmpty(e.getColumnName()))
                .filter(e -> filterList.contains(e.getLabelCode()))
                .forEach(e -> columnCodeValueTable.put(e.getColumnId(), e.getLabelCode(), e.getLabelParamValue()));

        Map<String, String> typeMapping = enumService.getEnumValueMapByCode(COLUMN_TYPE_ENUM);

        CompareInfoDTO compareInfoDTO = new CompareInfoDTO();
        for (Long columnId : columnIdList) {
            String columnName = nameOfId.get(columnId);
            String hiveColumnName = columnCodeValueTable.get(columnId, SysLabelCodeEnum.HIVE_COLUMN_NAME_LABEL.labelCode);
            String columnType = columnCodeValueTable.get(columnId, SysLabelCodeEnum.COLUMN_TYPE_LABEL.labelCode);
            String hiveColumnType = columnCodeValueTable.get(columnId, SysLabelCodeEnum.HIVE_COLUMN_TYPE_LABEL.labelCode);
            String columnComment = columnCodeValueTable.get(columnId, SysLabelCodeEnum.COLUMN_COMMENT_LABEL.labelCode);
            String hiveColumnComment = columnCodeValueTable.get(columnId, SysLabelCodeEnum.HIVE_COLUMN_COMMENT_LABEL.labelCode);
            String columnPartition = columnCodeValueTable.get(columnId, SysLabelCodeEnum.PARTITION_COL_LABEL.labelCode);
            String hiveColumnPartition = columnCodeValueTable.get(columnId, SysLabelCodeEnum.HIVE_PARTITION_COL_LABEL.labelCode);

            if (StringUtils.isEmpty(hiveColumnName)) {
                // local比hive多的列
                CompareInfoDTO.BasicColumnInfo basicColumnInfo = new CompareInfoDTO.BasicColumnInfo(columnName, typeMapping.get(columnType), columnComment);
                basicColumnInfo.setPartition(StringUtils.equals(columnPartition, "true"));
                basicColumnInfo.setColumnId(columnId);
                compareInfoDTO.getMoreList().add(basicColumnInfo);
            } else if (!StringUtils.equals(columnName, hiveColumnName)
                    || !StringUtils.equals(columnType, hiveColumnType)
                    || !JiveUtil.commentEquals(columnComment, hiveColumnComment)
                    || !StringUtils.equals(columnPartition, hiveColumnPartition)) {
                CompareInfoDTO.ChangeColumnInfo columnInfo = new CompareInfoDTO.ChangeColumnInfo();
                columnInfo.setColumnId(columnId);
                columnInfo.setColumnName(columnName);
                columnInfo.setHiveColumnName(hiveColumnName);
                columnInfo.setColumnType(typeMapping.get(columnType));
                columnInfo.setHiveColumnType(typeMapping.get(hiveColumnType));
                columnInfo.setColumnComment(columnComment);
                columnInfo.setHiveColumnComment(hiveColumnComment);
                columnInfo.setPartition(StringUtils.equals(columnPartition, "true"));
                columnInfo.setHivePartition(StringUtils.equals(hiveColumnPartition, "true"));

                compareInfoDTO.getDifferentList().add(columnInfo);
            }
        }

        //TODO 暂时不需要相比hive少的字段 less不需要

        return compareInfoDTO;
    }

    /**
     * 校验databse
     * @param hiveTableName
     * @param dbName
     */
    private void checkDatabase(String hiveTableName, String dbName) {
        if (StringUtils.isEmpty(dbName)) {
            throw new GeneralException("未查询到dbName");
        }

        if (StringUtils.isBlank(hiveTableName)) {
            return;
        }

        String[] tableMetaInfo = hiveTableName.split("\\.");
        if (tableMetaInfo.length < 2) {
            throw new IllegalArgumentException(String.format("同步的hive table信息未带库名%s", hiveTableName));
        }
        if (!StringUtils.equalsIgnoreCase(dbName, tableMetaInfo[0])) {
            throw new IllegalArgumentException("已同步过到hive的表不能更改database");
        }

    }

    public void markDiff(TableInfoDto tableInfo) {
        Long tableId = tableInfo.getId();
        if (tableInfo.getHiveTableName() != null) {
            // 同步过hive的表才进行比较
            // 将多的字段、少的字段、不同的字段都获取到
            List<cn.zhengcaiyun.idata.develop.dto.table.ColumnInfoDto> localColumnInfos = tableInfo.getColumnInfos();
            Set<String> diffColumnNameList = new HashSet<>();
            CompareInfoDTO compareInfoDTO = compareHive(tableId);
            diffColumnNameList.addAll(compareInfoDTO.getLessList().stream().map(CompareInfoDTO.BasicColumnInfo::getColumnName).collect(Collectors.toSet()));
            diffColumnNameList.addAll(compareInfoDTO.getMoreList().stream().map(CompareInfoDTO.BasicColumnInfo::getColumnName).collect(Collectors.toSet()));
            diffColumnNameList.addAll(compareInfoDTO.getDifferentList().stream().map(CompareInfoDTO.ChangeColumnInfo::getColumnName).collect(Collectors.toSet()));
            localColumnInfos.stream().forEach(e -> {
                e.setEnableCompare(true);
                e.setHiveDiff(diffColumnNameList.contains(e.getColumnName()));
            });
        }
    }

    /**
     * 同步hive远端表元数据信息到数据库
     * @param tableIdList
     */
    public void pullHiveInfo(List<Long> tableIdList) {
        Map<String, String> typeMapping = enumService.getCodeMapByEnumValue(COLUMN_TYPE_ENUM);
        String operator = "系统管理员";
        for (Long tableId : tableIdList) {
            Map<String, String> lowerCaseMapping = columnInfoService.getColumnNames(tableId).stream().collect(Collectors.toMap(e -> e.toLowerCase(), e -> e));
            DevTableInfo tableInfo = tableInfoService.getSimpleById(tableId);
            String hiveTableName = tableInfo.getHiveTableName();
            String dbName = labelService.getDBName(tableId);
            String tableName = tableInfo.getTableName();

            // 存在同步记录的表不做处理
            if (StringUtils.isNotEmpty(hiveTableName)) {
                continue;
            }
            boolean existHiveTable = hiveService.existHiveTable(dbName, tableName);
            // 不存在hive表不做同步
            if (!existHiveTable) {
                continue;
            }
            MetadataInfo metadataInfo = hiveService.getMetadataInfo(dbName, tableName);
            List<DevLabel> labelList = Lists.newArrayList();
            Map<String, Long> nameOfIdMap = columnInfoService.getColumnInfo(tableId).stream().collect(Collectors.toMap(DevColumnInfo::getColumnName, DevColumnInfo::getId));
            metadataInfo.getColumnList().forEach(e -> {
                ColumnInfoDto columnInfoDto = new ColumnInfoDto();
                BeanUtils.copyProperties(e, columnInfoDto);
                columnInfoDto.setPartition(false);
                columnInfoDto.setColumnId(nameOfIdMap.get(columnInfoDto.getColumnName()));
                List<DevLabel> subList = assembleDevLabelList(lowerCaseMapping, typeMapping, operator, tableId, columnInfoDto);
                labelList.addAll(subList);
            });
            metadataInfo.getPartitionColumnList().forEach(e -> {
                ColumnInfoDto columnInfoDto = new ColumnInfoDto();
                BeanUtils.copyProperties(e, columnInfoDto);
                columnInfoDto.setPartition(true);
                columnInfoDto.setColumnId(nameOfIdMap.get(columnInfoDto.getColumnName()));
                List<DevLabel> subList = assembleDevLabelList(lowerCaseMapping, typeMapping, operator, tableId, columnInfoDto);
                labelList.addAll(subList);
            });
            labelService.batchUpsert(labelList);
            tableInfoService.updateHiveTableName(tableId, dbName + "." + tableName, operator);

            logger.info("[script]:pull hive-info done " + tableId + ":" + tableName);
        }

    }

}
