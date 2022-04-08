package cn.zhengcaiyun.idata.develop.helper.rule;

import cn.hutool.core.util.ReUtil;
import cn.zhengcaiyun.idata.commons.enums.DataSourceTypeEnum;
import cn.zhengcaiyun.idata.commons.enums.DriverTypeEnum;
import cn.zhengcaiyun.idata.commons.exception.GeneralException;
import cn.zhengcaiyun.idata.develop.constant.enums.DiConfigModeEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.EngineTypeEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.SrcReadModeEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.DIJobContent;
import cn.zhengcaiyun.idata.develop.dto.job.di.DIJobContentContentDto;
import cn.zhengcaiyun.idata.develop.dto.job.di.MappingColumnDto;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum.BACK_FLOW;
import static cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum.DI_BATCH;
import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author zhanqian
 * @date 2022/3/16 10:23 AM
 * @description 数据集成规则校验
 */
public class DIRuleHelper {

    public static void checkJobContent(DIJobContentContentDto contentDto) {
        checkArgument(StringUtils.isNotBlank(contentDto.getSrcDataSourceType()), "来源数据源类型为空");
        checkArgument(Objects.nonNull(contentDto.getSrcDataSourceId()), "来源数据源编号为空");
        JobTypeEnum jobType = contentDto.getJobType();

        if (jobType == DI_BATCH || jobType == JobTypeEnum.DI_STREAM) {
            checkArgument(StringUtils.isNotBlank(contentDto.getSrcReadMode()), "读取模式为空");
        }
        String destDataSourceType = contentDto.getDestDataSourceType();
        checkArgument(StringUtils.isNotBlank(destDataSourceType), "目标数据源类型为空");
        checkArgument(Objects.nonNull(contentDto.getDestDataSourceId()), "目标数据源编号为空");
        if (!StringUtils.equalsIgnoreCase(destDataSourceType, DriverTypeEnum.Kafka.name())) {
            checkArgument(StringUtils.isNotBlank(contentDto.getDestTable()), "目标数据表为空");
        }
        checkArgument(StringUtils.isNotBlank(contentDto.getDestWriteMode()), "写入模式为空");
        checkArgument(StringUtils.isNotBlank(contentDto.getSrcTables()) || !Objects.isNull(contentDto.getSrcTableConfig()), "来源数据表为空");
//todo        checkArgument(ObjectUtils.isNotEmpty(contentDto.getSrcCols()), "来源数据表字段为空");   数据迁移完后需要取消注释
//todo        checkArgument(ObjectUtils.isNotEmpty(contentDto.getDestCols()), "目标数据表字段为空");  数据迁移完后需要取消注释

        List<MappingColumnDto> mappingColumnDtoList = contentDto.getSrcCols().stream()
                .filter(columnDto -> Objects.nonNull(columnDto.getMappedColumn()))
                .collect(Collectors.toList());
//todo        checkArgument(ObjectUtils.isNotEmpty(mappingColumnDtoList), "映射字段为空");  数据迁移完后需要取消注释


        DataSourceTypeEnum dataSourceTypeEnum = DataSourceTypeEnum.valueOf(contentDto.getSrcDataSourceType());
        // 回流 + 目标源为doris 不构建sql
        if (BACK_FLOW == jobType && DataSourceTypeEnum.doris == dataSourceTypeEnum) {
            if (StringUtils.isNotEmpty(contentDto.getSrcReadFilter())) {
                throw new GeneralException("回流到doris，不支持过滤条件");
            }
            if (contentDto.getSrcTables().split("\\.").length < 2) {
                throw new GeneralException("回流到doris，源表必须带库名");
            }
            if (!StringUtils.equals(contentDto.getSrcReadMode(), SrcReadModeEnum.ALL.value)) {
                throw new GeneralException("回流到doris，只支持全量回流");
            }
        }
    }

    /**
     * 是否构建mergeSql
     *
     * @param jobType @see cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum
     * @param srcReadMode 读取模式
     * @return
     */
    public static boolean buildMergeSql(String jobType, String srcReadMode) {
        return StringUtils.containsIgnoreCase(jobType, "DI") && StringUtils.equalsIgnoreCase(srcReadMode, SrcReadModeEnum.INC.value);
    }

    /**
     * 是否构建query sql
     *
     * @param jobType @see cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum
     * @param configMode 配置模式 可视化/脚本
     * @param srcReadFilter 读取过滤条件
     * @param srcCols 可视化列
     * @return
     */
    public static boolean buildQuery(String jobType, Integer configMode, String srcReadFilter, List<MappingColumnDto> srcCols, DataSourceTypeEnum dataSourceTypeEnum) {
        // 回流 + 目标源为doris 不构建sql
        if (StringUtils.containsIgnoreCase(jobType, "BACK_FLOW") && DataSourceTypeEnum.doris == dataSourceTypeEnum) {
            return false;
        }

        return true;
    }

    /**
     * 生成mergeSql
     * @param columnList select的列
     * @param keyColumns 主见keys，逗号分隔
     * @param sourceTable 源数据表，可能是多张，按正则解析
     * @param destTable 目标表（带库名）
     * @param typeEnum 驱动类型 @see cn.zhengcaiyun.idata.commons.enums.DriverTypeEnum
     * @param days 天数，近几天
     * @return
     * @throws IllegalArgumentException
     */
    public static String generateMergeSql(List<String> columnList, String keyColumns, String sourceTable, String destTable, DataSourceTypeEnum typeEnum, int days) throws IllegalArgumentException {
        checkArgument(destTable.split("\\.").length == 2, "生成mergeSql的destTable必须带上库名: " + destTable);
        String tmpTableParam = "src." + destTable.split("\\.")[1] + "_pt";
        // 匹配规则：例如 "tableName[1-2]"
        String regex1 = "(\\w+)\\[(\\d+-\\d+)\\]";
        // 匹配规则：例如 "tableName2,tableName2"
        String regex2 = "(\\d+(\\w+)[,]{0,1})+";

        // 是否涉及多张表（分区表）
        boolean isMulPartitionParam = false;
        if (typeEnum == DataSourceTypeEnum.mysql && (ReUtil.isMatch(regex1, sourceTable) || ReUtil.isMatch(regex2, sourceTable))) {
            isMulPartitionParam = true;
        }

        // 筛选的列名
        String columnsParam = StringUtils.join(columnList, ", ");
        // 筛选的带函数的列名
        String alisColumns = StringUtils.join(columnList.stream().map(e -> "t1." + e).collect(Collectors.toList()), "\n,");
        //生成keyCondition，key连接表，例如"t1.id=t2.id"
        List<String> keyColumnList = Arrays.asList(keyColumns.split(",")).stream().map(e -> "t1." + e + "=t2." + e).collect(Collectors.toList());
        String keyConditionParam = StringUtils.join(keyColumnList, " and ");
        //生成keyCondition，key连接表，例如"t2.id is null"
        List<String> whereKeyConditionList = Arrays.asList(keyColumns.split(",")).stream().map(e -> "t2." + e + " is null").collect(Collectors.toList());
        String whereKeyConditionParam = StringUtils.join(whereKeyConditionList, " and ");

        String mergeSqlTemplate = "";
        try (InputStream inputStream = new DIRuleHelper().getClass().getClassLoader().getResourceAsStream("template/merge_sql_template2.sql");) {
            byte[] buff = new byte[1024];
            int btr = 0;
            while ((btr = inputStream.read(buff)) != -1) {
                mergeSqlTemplate += new String(buff, 0, btr, "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new GeneralException("模版文件解析失败");
        }

        // 通过SpEL解析
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("isMulPartition", isMulPartitionParam);
        context.setVariable("destTable", destTable);
        context.setVariable("columns", columnsParam);
        context.setVariable("tmpTable", tmpTableParam);
        context.setVariable("alisColumns", alisColumns);
        context.setVariable("keyCondition", keyConditionParam);
        context.setVariable("whereKeyConditionParam", whereKeyConditionParam);
        context.setVariable("days", days);
        context.setVariable("br", "\n");

        Expression expression = parser.parseExpression(mergeSqlTemplate, new TemplateParserContext());
        return expression.getValue(context, String.class);
    }

    /**
     * 生成脚本query
     * @param selectColumns 列明逗号分隔
     * @param srcReadFilter 过滤条件
     * @param srcTables 源表
     * @return
     */
    public static String generateScriptQuery(String selectColumns, String srcReadFilter, String srcTables) {
        String scriptQuery = String.format("select %s from %s ", selectColumns, srcTables);
        if (StringUtils.isNotEmpty(srcReadFilter)) {
            scriptQuery += (" where " + srcReadFilter);
        }
        return scriptQuery;
    }

    /**
     * 生成可视化query
     * @param mappingColumnList 封装的列
     * @param srcReadFilter 过滤条件
     * @param srcTables 源表
     * @return
     */
    public static String generateSrcQuery(List<MappingColumnDto> mappingColumnList, String srcReadFilter, String srcTables) {
        List<String> columns = mappingColumnList
                .stream()
                .filter(e -> e.getMappedColumn() != null)
                .map(e -> {
                    String name = e.getName();
                    String mappingSql = e.getMappingSql();
                    return StringUtils.isNotEmpty(mappingSql) ? mappingSql : name;
                }).collect(Collectors.toList());

        String selectColumns = StringUtils.join(columns, ",");
        String srcQuery = String.format("select %s from %s ", selectColumns, srcTables);
        if (StringUtils.isNotEmpty(srcReadFilter)) {
            srcQuery += (" where " + srcReadFilter);
        }
        return srcQuery;
    }

    /**
     * 作业类型和执行引擎组合是否支持query sql方式
     * @return
     */
    public static boolean supportQuerySQL(SupportQuerySqlParam param) {
        JobTypeEnum jobTypeEnum = param.getJobTypeEnum();
        EngineTypeEnum engineTypeEnum = param.getEngineTypeEnum();

        DataSourceTypeEnum destDataSourceTypeEnum = null;
        Integer configMode = null;
        String srcColumns = null;
        String scriptSelectColumns = null;
        DIJobContent diJobContent = param.getDiJobContent();
        if (diJobContent != null) {
            destDataSourceTypeEnum = DataSourceTypeEnum.valueOf(diJobContent.getDestDataSourceType());
            configMode = diJobContent.getConfigMode();
            srcColumns = diJobContent.getSrcColumns();
            scriptSelectColumns = diJobContent.getScriptSelectColumns();
        }

        switch (jobTypeEnum) {
            case BACK_FLOW:
                if (EngineTypeEnum.SQOOP == engineTypeEnum) {
                    return false;
                }
                if (destDataSourceTypeEnum == DataSourceTypeEnum.doris) {
                    return false;
                }
                break;
            case DI_BATCH:
            case DI_STREAM:
                if (EngineTypeEnum.SQOOP == engineTypeEnum) {
                    switch (DiConfigModeEnum.getByValue(configMode)) {
                        case SCRIPT:
                            if (StringUtils.isNotEmpty(scriptSelectColumns)) {
                                // 是否带函数，用括号判断
                                long aliseCount =  Arrays.stream(scriptSelectColumns.split(",")).filter(e -> StringUtils.isNotEmpty(e) && e.contains("(") && e.contains(")")).count();
                                return aliseCount > 0;
                            }
                        case VISIBLE:
                            if (StringUtils.isNotEmpty(srcColumns)) {
                                // 是否设置了mappingSql
                                List<MappingColumnDto> srcCols = JSON.parseArray(srcColumns, MappingColumnDto.class);
                                long aliasMappingCount = srcCols.stream().filter(e -> StringUtils.isNotEmpty(e.getMappingSql())).count();
                                return aliasMappingCount > 0;
                            }
                    }
                    return false;
                }
            default:
                break;
        }
        return true;
    }

    /**
     * 作业类型和执行引擎组合是否必须带有query
     * @param jobTypeEnum 任务类型
     * @param engineTypeEnum 执行引擎
     * @return
     */
    public static boolean mustQuerySQL(JobTypeEnum jobTypeEnum, EngineTypeEnum engineTypeEnum) {
        switch (jobTypeEnum) {
            case DI_BATCH:
            case DI_STREAM:
                if (EngineTypeEnum.SPARK == engineTypeEnum) {
                    return true;
                }
                break;
        }
        return false;
    }

    /**
     * 作业类型和执行引擎组合是否支持columns 方式同步(即非SQL方式)
     * @param jobTypeEnum 任务类型
     * @param engineTypeEnum 执行引擎
     * @return
     */
    public static boolean supportColumns(JobTypeEnum jobTypeEnum, EngineTypeEnum engineTypeEnum) {
        switch (jobTypeEnum) {
            case BACK_FLOW:
                if (EngineTypeEnum.SPARK == engineTypeEnum) {
                    return false;
                }
                return true;
        }
        return true;
    }

    public static class SupportQuerySqlParam {
        // 任务类型
        private JobTypeEnum jobTypeEnum;
        // 执行引擎
        private EngineTypeEnum engineTypeEnum;
        // 集成/回流实体数据
        DIJobContent diJobContent;

        public JobTypeEnum getJobTypeEnum() {
            return jobTypeEnum;
        }

        public void setJobTypeEnum(JobTypeEnum jobTypeEnum) {
            this.jobTypeEnum = jobTypeEnum;
        }

        public EngineTypeEnum getEngineTypeEnum() {
            return engineTypeEnum;
        }

        public void setEngineTypeEnum(EngineTypeEnum engineTypeEnum) {
            this.engineTypeEnum = engineTypeEnum;
        }

        public DIJobContent getDiJobContent() {
            return diJobContent;
        }

        public void setDiJobContent(DIJobContent diJobContent) {
            this.diJobContent = diJobContent;
        }
    }
}
