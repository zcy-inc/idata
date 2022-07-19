package cn.zhengcaiyun.idata.develop.facade;

import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.connector.clients.hive.ConnectInfo;
import cn.zhengcaiyun.idata.connector.clients.hive.Jive;
import cn.zhengcaiyun.idata.connector.clients.hive.pool.HivePool;
import cn.zhengcaiyun.idata.connector.clients.hive.util.JiveUtil;
import cn.zhengcaiyun.idata.connector.spi.hive.dto.CompareInfoNewDTO;
import cn.zhengcaiyun.idata.develop.dto.label.LabelDto;
import cn.zhengcaiyun.idata.develop.dto.table.ColumnInfoDto;
import cn.zhengcaiyun.idata.develop.dto.table.TableInfoDto;
import cn.zhengcaiyun.idata.develop.service.label.EnumService;
import cn.zhengcaiyun.idata.develop.service.table.ColumnInfoService;
import cn.zhengcaiyun.idata.develop.service.table.ForeignKeyService;
import cn.zhengcaiyun.idata.system.api.SystemConfigApi;
import cn.zhengcaiyun.idata.system.dto.ConfigDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhanqian
 * @date 2022/6/21 11:39 AM
 * @description
 */
@Service
public class ColumnFacade {

    private final String COLUMN_TYPE_ENUM = "hiveColTypeEnum:ENUM";

    @Autowired
    private SystemConfigApi systemConfigApi;

    @Autowired
    private EnumService enumService;

    @Autowired
    private ColumnInfoService columnInfoService;

    @Autowired
    private ForeignKeyService foreignKeyService;

    public CompareInfoNewDTO compare(String dbName, String tableName, List<ColumnInfoDto> columnInfoDtoList) {
        Map<String, String> hiveTypeMapping = enumService.getCodeMapByEnumValue(COLUMN_TYPE_ENUM);
        Map<String, String> localTypeMapping = new HashMap<>();
        hiveTypeMapping.forEach((k, v) -> localTypeMapping.put(v, k));


        Map<String, ColumnInfoDto> localMap = new HashMap<>();
        columnInfoDtoList.forEach(e -> localMap.put(e.getColumnName(), e));

        Map<String, cn.zhengcaiyun.idata.connector.bean.dto.ColumnInfoDto> hiveMap = new HashMap<>();
        List<cn.zhengcaiyun.idata.connector.bean.dto.ColumnInfoDto> metaColumnInfoList = getColumnMetaInfoIncludePartition(dbName, tableName);
        metaColumnInfoList.forEach(e -> hiveMap.put(e.getColumnName(), e));

        Set<String> columnNameSet = new HashSet<>();
        columnNameSet.addAll(localMap.keySet());
        columnNameSet.addAll(hiveMap.keySet());

        CompareInfoNewDTO compareInfoNewDTO = new CompareInfoNewDTO();
        for (String columnName : columnNameSet) {
            ColumnInfoDto localColumn = localMap.get(columnName);
            cn.zhengcaiyun.idata.connector.bean.dto.ColumnInfoDto hiveColumn = hiveMap.get(columnName);

            if (localColumn == null) {
                CompareInfoNewDTO.LessColumnInfo lessColumnInfo = new CompareInfoNewDTO.LessColumnInfo();
                lessColumnInfo.setHiveColumnComment(hiveColumn.getColumnComment());
                lessColumnInfo.setHiveColumnIndex(hiveColumn.getColumnIndex());
                lessColumnInfo.setHivePartition(hiveColumn.getPartition());
                lessColumnInfo.setHiveColumnName(columnName);
                lessColumnInfo.setHiveColumnComment(hiveColumn.getColumnType());
                compareInfoNewDTO.getLessList().add(lessColumnInfo);
            } else if (hiveColumn == null) {
                CompareInfoNewDTO.MoreColumnInfo moreColumnInfo = new CompareInfoNewDTO.MoreColumnInfo();
                moreColumnInfo.setColumnName(columnName);
                compareInfoNewDTO.getMoreList().add(moreColumnInfo);
            } else {
                String hiveColumnComment = hiveColumn.getColumnComment();
                String hiveColumnType = hiveColumn.getColumnType();
                Boolean hiveColumnPartition = hiveColumn.getPartition();
                Long localColumnId = localColumn.getId();

                List<LabelDto> columnLabels = localColumn.getColumnLabels();
                String localColumnTypeDesc = null;
                String localColumnComment = null;
                Boolean localColumnPartition = false;

                for (LabelDto labelDto : columnLabels) {
                    if (StringUtils.equalsIgnoreCase(labelDto.getLabelCode(), "columnType:LABEL")) {
                        localColumnTypeDesc = labelDto.getLabelParamValue();
                    }
                    if (StringUtils.equalsIgnoreCase(labelDto.getLabelCode(), "columnComment:LABEL")) {
                        localColumnComment = labelDto.getLabelParamValue();
                    }
                    if (StringUtils.equalsIgnoreCase(labelDto.getLabelCode(), "partitionedCol:LABEL")) {
                        localColumnPartition = StringUtils.equalsIgnoreCase(labelDto.getLabelParamValue(), "true");
                    }
                }
                String localColumnType = localTypeMapping.get(localColumnTypeDesc);

                if (!StringUtils.equalsIgnoreCase(localColumnType, hiveColumnType)
                        || !JiveUtil.commentEquals(localColumnComment, hiveColumnComment)
                        || (localColumnPartition != hiveColumnPartition)) {
                    CompareInfoNewDTO.ChangeColumnInfo changeColumnInfo = new CompareInfoNewDTO.ChangeColumnInfo();
                    changeColumnInfo.setColumnId(localColumnId);
                    changeColumnInfo.setColumnName(columnName);
                    changeColumnInfo.setHiveColumnName(columnName);
                    changeColumnInfo.setColumnType(localColumnType);
                    changeColumnInfo.setHiveColumnType(hiveColumnType);
                    changeColumnInfo.setColumnComment(localColumnComment);
                    changeColumnInfo.setHiveColumnComment(hiveColumnComment);
                    changeColumnInfo.setPartition(localColumnPartition);
                    changeColumnInfo.setHivePartition(hiveColumnPartition);

                    compareInfoNewDTO.getDifferentList().add(changeColumnInfo);
                }
            }
        }
        return compareInfoNewDTO;
    }

    public List<ColumnInfoDto> overwriteList(TableInfoDto tableInfo, CompareInfoNewDTO compareInfoNewDTO) {
        System.out.println();
        List<ColumnInfoDto> localColumnInfoList = tableInfo.getColumnInfos();
        String dbName = tableInfo.getDbName();
        String tableName = tableInfo.getTableName();
        Long tableId = tableInfo.getId();

        Map<String, String> hiveTypeMapping = enumService.getCodeMapByEnumValue(COLUMN_TYPE_ENUM);
        Map<String, String> localTypeMapping = new HashMap<>();
        hiveTypeMapping.forEach((k, v) -> localTypeMapping.put(v, k));

        Map<String, ColumnInfoDto> localMap = new HashMap<>();
        localColumnInfoList.forEach(e -> localMap.put(e.getColumnName(), e));

        List<cn.zhengcaiyun.idata.connector.bean.dto.ColumnInfoDto> metaColumnInfoList = getColumnMetaInfoIncludePartition(dbName, tableName);

        Map<String, CompareInfoNewDTO.ChangeColumnInfo> changeColumnInfoMap = new HashMap<>();
        compareInfoNewDTO.getDifferentList().forEach(e -> changeColumnInfoMap.put(e.getColumnName(), e));

        List<ColumnInfoDto> list = new ArrayList<>();
        for (cn.zhengcaiyun.idata.connector.bean.dto.ColumnInfoDto hiveColumn : metaColumnInfoList) {
            String columnName = hiveColumn.getColumnName();
            ColumnInfoDto column = localMap.get(columnName);
            if (column == null) {
                column = new ColumnInfoDto();
                column.setColumnName(columnName);
                column.setColumnType(hiveColumn.getColumnType());
                column.setColumnComment(hiveColumn.getColumnComment());

                List<LabelDto> labelDtoList = new ArrayList<>();
                LabelDto labelDto1 = new LabelDto();
                labelDto1.setLabelCode("pk:LABEL");
                labelDto1.setLabelParamValue("false");
                labelDto1.setColumnName(columnName);
                labelDtoList.add(labelDto1);

                LabelDto labelDto2 = new LabelDto();
                labelDto2.setLabelCode("columnType:LABEL");
                labelDto2.setLabelParamValue(hiveTypeMapping.getOrDefault(hiveColumn.getColumnType().toUpperCase(Locale.ROOT), "HIVE_COL_TYPE_STRING:ENUM_VALUE"));
                labelDto2.setColumnName(columnName);
                labelDtoList.add(labelDto2);

                LabelDto labelDto3 = new LabelDto();
                labelDto3.setLabelCode("partitionedCol:LABEL");
                labelDto3.setLabelParamValue(hiveColumn.getPartition() ? "true" : "false");
                labelDto3.setColumnName(columnName);
                labelDtoList.add(labelDto3);

                if (StringUtils.isNotEmpty(hiveColumn.getColumnComment())) {
                    LabelDto labelDto4 = new LabelDto();
                    labelDto4.setLabelCode("columnComment:LABEL");
                    labelDto4.setLabelParamValue(hiveColumn.getColumnComment());
                    labelDto4.setColumnName(columnName);
                    labelDtoList.add(labelDto4);
                }
                column.setColumnLabels(labelDtoList);
            }
            column.setColumnIndex(hiveColumn.getColumnIndex());

            CompareInfoNewDTO.ChangeColumnInfo changeColumnInfo = changeColumnInfoMap.get(columnName);
            if (changeColumnInfo != null) {
                Map<String, LabelDto> labelDtoMap = new HashMap<>();
                column.getColumnLabels().forEach(e -> labelDtoMap.put(e.getLabelCode(), e));
                if (changeColumnInfo.isHivePartition() != changeColumnInfo.isPartition()) {
                    LabelDto labelDto = labelDtoMap.get("partitionedCol:LABEL");
                    labelDto.setLabelParamValue(changeColumnInfo.isHivePartition() + "");
                }
                String hiveColumnComment = changeColumnInfo.getHiveColumnComment();
                if (!StringUtils.equalsIgnoreCase(changeColumnInfo.getColumnComment(), hiveColumnComment)) {
                    LabelDto labelDto = labelDtoMap.computeIfAbsent("columnComment:LABEL", e -> {
                        LabelDto elem = new LabelDto();
                        elem.setColumnName(columnName);
                        elem.setLabelCode("columnComment:LABEL");
                        return elem;
                    });
                    if (hiveColumnComment == null) {
                        hiveColumnComment = "";
                    }
                    labelDto.setLabelParamValue(hiveColumnComment);
                }
                if (!StringUtils.equalsIgnoreCase(changeColumnInfo.getColumnType(), changeColumnInfo.getHiveColumnType())) {
                    LabelDto labelDto = labelDtoMap.get("columnType:LABEL");
                    labelDto.setLabelParamValue(hiveTypeMapping.get(changeColumnInfo.getHiveColumnType().toUpperCase(Locale.ROOT)));
                }
                column.setColumnLabels(new ArrayList<>(labelDtoMap.values()));
            }
            list.add(column);
        }

        return list;
    }

    private List<cn.zhengcaiyun.idata.connector.bean.dto.ColumnInfoDto> getColumnMetaInfoIncludePartition(String dbName, String tableName) {
        ConfigDto systemConfigByKey = systemConfigApi.getSystemConfigByKey("hive-info");
        String jdbcUrl = systemConfigByKey.getValueOne().get("hive-info").getConfigValue();

        Jive jive = null;
        HivePool hivePool = null;
        try {
            ConnectInfo connectInfo = new ConnectInfo();
            connectInfo.setJdbc(jdbcUrl);
            GenericObjectPoolConfig config = new GenericObjectPoolConfig();
            config.setTestOnBorrow(true);
            hivePool = new HivePool(config, connectInfo);
            jive = hivePool.getResource();
            List<cn.zhengcaiyun.idata.connector.bean.dto.ColumnInfoDto> columnMetaInfo = jive.getColumnMetaInfoIncludePartition(dbName, tableName);
            return columnMetaInfo;
        } finally {
            jive.close();
            hivePool.close();
        }
    }
}
