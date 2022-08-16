package cn.zhengcaiyun.idata.develop.dal.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface MonitorRuleDao {

    Integer getRulesByTable(@Param("tableName") String tableName);

    Integer getBaselineRulesByTableName(@Param("tableName") String tableName);
}