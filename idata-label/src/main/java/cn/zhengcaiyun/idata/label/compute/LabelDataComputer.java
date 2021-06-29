package cn.zhengcaiyun.idata.label.compute;

import cn.zhengcaiyun.idata.label.compute.query.Query;
import cn.zhengcaiyun.idata.label.compute.query.QueryService;
import cn.zhengcaiyun.idata.label.compute.query.dto.ConnectionDto;
import cn.zhengcaiyun.idata.label.compute.query.dto.WideTableDataDto;
import cn.zhengcaiyun.idata.label.compute.query.exception.ExecuteSqlException;
import cn.zhengcaiyun.idata.label.compute.sql.transform.DimensionTranslator;
import cn.zhengcaiyun.idata.label.compute.sql.transform.IndicatorTranslator;
import cn.zhengcaiyun.idata.label.compute.sql.transform.SqlTranslator;
import cn.zhengcaiyun.idata.label.dto.LabelQueryDataDto;
import cn.zhengcaiyun.idata.label.dto.label.rule.LabelRuleDto;
import cn.zhengcaiyun.idata.system.dal.dao.SysConfigDao;
import cn.zhengcaiyun.idata.system.dal.model.SysConfig;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Objects;

import static cn.zhengcaiyun.idata.system.dal.dao.SysConfigDynamicSqlSupport.sysConfig;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 14:36
 **/
@Component
public class LabelDataComputer {
    private static final Logger logger = LoggerFactory.getLogger(LabelDataComputer.class);

    private final Query query;
    private final SqlTranslator sqlTranslator;
    private final SysConfigDao sysConfigDao;

    @Autowired
    public LabelDataComputer(Query query, SqlTranslator sqlTranslator, SysConfigDao sysConfigDao) {
        this.query = query;
        this.sqlTranslator = sqlTranslator;
        this.sysConfigDao = sysConfigDao;
    }

    public LabelQueryDataDto compute(LabelRuleDto ruleDto, String objectType, Long limit, Long offset) {
        ConnectionDto connectionDto = getConnectionInfo();
        checkNotNull(connectionDto != null, "数据源连接信息不正确");
        String sql = sqlTranslator.translate(ruleDto, objectType, limit, offset);
        WideTableDataDto tableDataDto;
        try {
            tableDataDto = query.query(connectionDto, sql);
        } catch (SQLException ex) {
            logger.debug("query from trino failed. sql: {}.", sql, Throwables.getStackTraceAsString(ex));
            throw new ExecuteSqlException("执行错误");
        }

        if (Objects.isNull(tableDataDto)) {
            return null;
        }
        LabelQueryDataDto dataDto = new LabelQueryDataDto();
        dataDto.setColumns(tableDataDto.getMeta());
        dataDto.setData(tableDataDto.getData());
        return dataDto;
    }

    private ConnectionDto getConnectionInfo() {
        SysConfig trinoConfig = sysConfigDao.selectOne(dsl -> dsl.where(sysConfig.keyOne, isEqualTo("trino-info")))
                .orElse(null);
        if (trinoConfig != null) {
            return JSON.parseObject(trinoConfig.getValueOne(), ConnectionDto.class);
        }
        return null;
    }

    private ConnectionDto newConnectionInfo() {
        ConnectionDto connectionDto = new ConnectionDto();
        connectionDto.setHost("172.29.108.184");
        connectionDto.setPort(18080);
        connectionDto.setDbCatalog("hive");
        connectionDto.setUsername("presto");
        return connectionDto;
    }

    public LabelQueryDataDto testCompute() {
        ConnectionDto connectionDto = newConnectionInfo();

        String sql = sqlTranslator.mockSQL();
        WideTableDataDto tableDataDto;
        try {
            tableDataDto = query.query(connectionDto, sql);
        } catch (SQLException ex) {
            logger.debug("query from trino failed. sql: {}.", sql, Throwables.getStackTraceAsString(ex));
            throw new ExecuteSqlException("执行错误");
        }

        if (Objects.isNull(tableDataDto)) {
            return null;
        }
        LabelQueryDataDto dataDto = new LabelQueryDataDto();
        dataDto.setColumns(tableDataDto.getMeta());
        dataDto.setData(tableDataDto.getData());
        return dataDto;
    }

    public static void main(String[] args) {
        SqlTranslator sqlTranslator = new SqlTranslator(new DimensionTranslator(), new IndicatorTranslator());
        LabelDataComputer computer = new LabelDataComputer(new QueryService(), sqlTranslator, null);
        computer.testCompute();
    }
}
