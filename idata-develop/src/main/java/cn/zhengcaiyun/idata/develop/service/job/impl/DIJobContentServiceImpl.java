/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.zhengcaiyun.idata.develop.service.job.impl;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.commons.enums.DriverTypeEnum;
import cn.zhengcaiyun.idata.datasource.service.DataSourceService;
import cn.zhengcaiyun.idata.develop.constant.enums.DestWriteModeEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.EditableEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.DIJobContent;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.DIJobContentRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobInfoRepo;
import cn.zhengcaiyun.idata.develop.dto.job.di.DIJobContentContentDto;
import cn.zhengcaiyun.idata.develop.dto.job.di.MappingColumnDto;
import cn.zhengcaiyun.idata.develop.service.job.DIJobContentService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-28 14:07
 **/
@Service
public class DIJobContentServiceImpl implements DIJobContentService {

    private final DIJobContentRepo diJobContentRepo;
    private final JobInfoRepo jobInfoRepo;

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    public DIJobContentServiceImpl(DIJobContentRepo diJobContentRepo,
                                   JobInfoRepo jobInfoRepo) {
        this.diJobContentRepo = diJobContentRepo;
        this.jobInfoRepo = jobInfoRepo;
    }

    @Override
    public DIJobContentContentDto save(Long jobId, DIJobContentContentDto contentDto, Operator operator) {
        checkArgument(Objects.nonNull(jobId), "作业编号为空");
        checkJobContent(contentDto);
        Optional<JobInfo> jobInfoOptional = jobInfoRepo.queryJobInfo(jobId);
        checkArgument(jobInfoOptional.isPresent(), "作业不存在或已删除");
        // 判断目标表是否在其他job中已经存在
        checkArgument(unUsedDestTable(contentDto.getDestTable(), jobId), "目标表已经被其他作业使用");

        Integer version = contentDto.getVersion();
        boolean startNewVersion = true;
        if (Objects.nonNull(version)) {
            Optional<DIJobContent> jobContentOptional = diJobContentRepo.query(jobId, version);
            checkArgument(jobContentOptional.isPresent(), "作业版本不存在或已删除");
            DIJobContent existJobContent = jobContentOptional.get();
            if (existJobContent.getEditable().equals(EditableEnum.YES.val)) {
                // 版本可以编辑，直接覆盖
                startNewVersion = false;
                contentDto.setId(existJobContent.getId());
                contentDto.setJobId(jobId);
                contentDto.resetEditor(operator);
                DIJobContent updateContent = contentDto.toModel();
                updateContent.setEditable(EditableEnum.YES.val);
                diJobContentRepo.update(updateContent);
            }
        }

        if (startNewVersion) {
            // 版本为空或不可编辑，新增版本
            version = diJobContentRepo.newVersion(jobId);
            contentDto.setId(null);
            contentDto.setJobId(jobId);
            contentDto.setVersion(version);
            contentDto.setOperator(operator);
            DIJobContent newVersionContent = contentDto.toModel();
            newVersionContent.setEditable(EditableEnum.YES.val);
            diJobContentRepo.save(newVersionContent);
        }

        return get(jobId, version);
    }

    @Override
    public DIJobContentContentDto get(Long jobId, Integer version) {
        checkArgument(Objects.nonNull(jobId), "作业编号为空");
        checkArgument(Objects.nonNull(version), "作业版本号为空");

        Optional<DIJobContent> jobContentOptional = diJobContentRepo.query(jobId, version);
        checkArgument(jobContentOptional.isPresent(), "作业版本不存在");
        return DIJobContentContentDto.from(jobContentOptional.get());
    }

    @Override
    public String generateMergeSql(Long dataSourceId, String table, String hiveTableName, DestWriteModeEnum diMode, DriverTypeEnum typeEnum) throws IllegalArgumentException {
        String srcHiveTable = "src." + hiveTableName;

        Map<Integer, String> sourceTableMap = getSourceTableMap(table);
        boolean isMulPartition = false;
        if (typeEnum == DriverTypeEnum.MySQL && sourceTableMap.size() > 1) {
            isMulPartition = true;
            table = sourceTableMap.values().stream().findFirst().get();
        }
        String[] columns = dataSourceService.getDBTableColumns(dataSourceId, table);
        StringBuilder sb = new StringBuilder();
        if (diMode == DestWriteModeEnum.append) {
            if (isMulPartition) {
                sb.append("set hive.exec.dynamic.partition=true;\n");
                sb.append("set hive.exec.dynamic.partition.mode=nonstrict;\n");
                sb.append("set hive.exec.max.dynamic.partitions.pernode=1000;\n");
            }
            sb.append("alter table AAABBBAAABBB_pt drop if exists partition(pt<'${day-3d}');\n");
            sb.append("insert overwrite table AAABBBAAABBB_pt partition(pt='${day}'");
            String columnStr = columns[0];
            if (isMulPartition) {
                sb.append(",num");
                columnStr = columns[0] + ",num";
            }
            sb.append(") \n");
            String []cols = columnStr.split(",");
            String linesColumn = columnStr.replace(",", "\n,");
            StringBuilder condition = new StringBuilder();
            for (String col : cols) {
                condition.append("\n,coalesce(t1.").append(col).append(",t2.").append(col).append(") ").append(col);
            }
            sb.append("select ").append(condition.substring(2, condition.length())).append("\n");
            sb.append("from \n(select ").append(linesColumn).append("\nfrom ").append(hiveTable).append(") t1 \n");
            sb.append("full join \n(select ").append(linesColumn).append("\nfrom AAABBBAAABBB_pt where pt='${day-1d}') t2 \n");
            String columnKey = StringUtils.defaultIfBlank(columns[1], "id");
            sb.append("on t1.").append(columnKey).append("=t2.").append(columnKey);
            if (isMulPartition) {
                sb.append(" and t1.num=t2.num");
            }
            sb.append(";\n");
            sb.append("insert overwrite table ").append(hiveTable);
            if (isMulPartition) {
                sb.append(" partition(num)");
            }
            sb.append("\n select ").append(linesColumn)
                    .append("\nfrom AAABBBAAABBB_pt where pt='${day}';");
        }
        return sb.toString().replace("AAABBBAAABBB", srcHiveTable);
    }

    private static Map<Integer, String> getSourceTableMap(String sourceTable) {
        Map<Integer, String> sourceTableMap = new HashMap<>();
        // 2623tableName1,2333tableName2
        if (sourceTable.contains(",")) {
            String[] tableNames = sourceTable.split(",");
            for (String tableName : tableNames) {
                int len = tableName.length();
                while (StringUtils.isNumeric(tableName.substring(--len)));
                sourceTableMap.put(NumberUtils.toInt(tableName.substring(len + 1)), tableName);
            }
            return sourceTableMap;
        }
        // zcy_backlog_handler_ref_t_[0-63]
        if (sourceTable.contains("[")) {
            int leftBracket = sourceTable.indexOf("[");
            int idx = sourceTable.indexOf("-");
            int rightBracket = sourceTable.indexOf("]");
            String baseSourceTable = sourceTable.substring(0, leftBracket);
            String leftNum = sourceTable.substring(leftBracket + 1, idx);
            String rightNum = sourceTable.substring(idx + 1, rightBracket);
            int start = NumberUtils.toInt(leftNum);
            int end = NumberUtils.toInt(rightNum);
            for (;start <= end; start++) {
                sourceTableMap.put(start, baseSourceTable + start);
            }
            return sourceTableMap;
        }
        sourceTableMap.put(0, sourceTable);
        return sourceTableMap;
    }

//    @Override
//    public String generateMergeSql(Long dataSourceId, String table, String hiveTable, DestWriteModeEnum diMode, DriverTypeEnum typeEnum) throws IllegalArgumentException {
//        String[] hiveTableSplit = hiveTable.split("\\.");
//        String[] hiveDb = hiveTableSplit[0].split("_");
//        String srcHiveTable = (hiveDb.length > 1 ? (hiveDb[0] + "_") : "") + "src." + hiveTableSplit[1];
//        Map<Integer, String> sourceTableMap = getSourceTableMap(table);
//        boolean isMulPartition = typeEnum == DriverTypeEnum.MySQL && sourceTableMap.size() > 1;
//        if (isMulPartition) {
//            table = sourceTableMap.values().stream().findFirst().get();
//        }
//        String[] columns = dataSourceService.getDBTableColumns(dataSourceId, table);
//        StringBuilder sb = new StringBuilder();
//        if (diMode == DestWriteModeEnum.append) {
//            if (isMulPartition) {
//                sb.append("set hive.exec.dynamic.partition=true;\n");
//                sb.append("set hive.exec.dynamic.partition.mode=nonstrict;\n");
//                sb.append("set hive.exec.max.dynamic.partitions.pernode=1000;\n");
//            }
//            sb.append("alter table AAABBBAAABBB_pt drop if exists partition(pt<'${day-3d}');\n");
//            sb.append("insert overwrite table AAABBBAAABBB_pt partition(pt='${day}'");
//            String columnStr = columns[0];
//            if (isMulPartition) {
//                sb.append(",num");
//                columnStr = columns[0] + ",num";
//            }
//            sb.append(") \n");
//            String []cols = columnStr.split(",");
//            String linesColumn = columnStr.replace(",", "\n,");
//            StringBuilder condition = new StringBuilder();
//            for (String col : cols) {
//                condition.append("\n,coalesce(t1.").append(col).append(",t2.").append(col).append(") ").append(col);
//            }
//            sb.append("select ").append(condition.substring(2, condition.length())).append("\n");
//            sb.append("from \n(select ").append(linesColumn).append("\nfrom ").append(hiveTable).append(") t1 \n");
//            sb.append("full join \n(select ").append(linesColumn).append("\nfrom AAABBBAAABBB_pt where pt='${day-1d}') t2 \n");
//            String columnKey = StringUtils.defaultIfBlank(columns[1], "id");
//            sb.append("on t1.").append(columnKey).append("=t2.").append(columnKey);
//            if (isMulPartition) {
//                sb.append(" and t1.num=t2.num");
//            }
//            sb.append(";\n");
//            sb.append("insert overwrite table ").append(hiveTable);
//            if (isMulPartition) {
//                sb.append(" partition(num)");
//            }
//            sb.append("\n select ").append(linesColumn)
//                    .append("\nfrom AAABBBAAABBB_pt where pt='${day}';");
//        }
//        return sb.toString().replace("AAABBBAAABBB", srcHiveTable);
//    }
//
//    private static Map<Integer, String> getSourceTableMap(String sourceTable) {
//        Map<Integer, String> sourceTableMap = new HashMap<>();
//        // 2623tableName1,2333tableName2
//        if (sourceTable.contains(",")) {
//            String[] tableNames = sourceTable.split(",");
//            for (String tableName : tableNames) {
//                int len = tableName.length();
//                while (StringUtils.isNumeric(tableName.substring(--len)));
//                sourceTableMap.put(NumberUtils.toInt(tableName.substring(len + 1)), tableName);
//            }
//            return sourceTableMap;
//        }
//        // zcy_backlog_handler_ref_t_[0-63]
//        if (sourceTable.contains("[")) {
//            int leftBracket = sourceTable.indexOf("[");
//            int idx = sourceTable.indexOf("-");
//            int rightBracket = sourceTable.indexOf("]");
//            String baseSourceTable = sourceTable.substring(0, leftBracket);
//            String leftNum = sourceTable.substring(leftBracket + 1, idx);
//            String rightNum = sourceTable.substring(idx + 1, rightBracket);
//            int start = NumberUtils.toInt(leftNum);
//            int end = NumberUtils.toInt(rightNum);
//            for (;start <= end; start++) {
//                sourceTableMap.put(start, baseSourceTable + start);
//            }
//            return sourceTableMap;
//        }
//        sourceTableMap.put(0, sourceTable);
//        return sourceTableMap;
//    }

    private void checkJobContent(DIJobContentContentDto contentDto) {
        checkArgument(StringUtils.isNotBlank(contentDto.getSrcDataSourceType()), "来源数据源类型为空");
        checkArgument(Objects.nonNull(contentDto.getSrcDataSourceId()), "来源数据源编号为空");
        checkArgument(StringUtils.isNotBlank(contentDto.getSrcReadMode()), "读取模式为空");
        checkArgument(StringUtils.isNotBlank(contentDto.getDestDataSourceType()), "目标数据源类型为空");
        checkArgument(Objects.nonNull(contentDto.getDestDataSourceId()), "目标数据源编号为空");
        checkArgument(StringUtils.isNotBlank(contentDto.getDestTable()), "目标数据表为空");
        checkArgument(StringUtils.isNotBlank(contentDto.getDestWriteMode()), "写入模式为空");
        checkArgument(StringUtils.isNotBlank(contentDto.getSrcTables()), "来源数据表为空");
//todo        checkArgument(ObjectUtils.isNotEmpty(contentDto.getSrcCols()), "来源数据表字段为空");   数据迁移完后需要取消注释
//todo        checkArgument(ObjectUtils.isNotEmpty(contentDto.getDestCols()), "目标数据表字段为空");  数据迁移完后需要取消注释

        List<MappingColumnDto> mappingColumnDtoList = contentDto.getSrcCols().stream()
                .filter(columnDto -> Objects.nonNull(columnDto.getMappedColumn()))
                .collect(Collectors.toList());
//todo        checkArgument(ObjectUtils.isNotEmpty(mappingColumnDtoList), "映射字段为空");  数据迁移完后需要取消注释
    }

    private boolean unUsedDestTable(String destTable, Long excludeJobId) {
        List<DIJobContent> diJobContents = diJobContentRepo.queryList(destTable);
        if (ObjectUtils.isEmpty(diJobContents)) return true;

        Set<Long> jobIdSet = diJobContents.stream()
                .map(DIJobContent::getJobId)
                .distinct()
                .filter(job_id -> !job_id.equals(excludeJobId))
                .collect(Collectors.toSet());
        return ObjectUtils.isEmpty(jobIdSet);
    }

}
