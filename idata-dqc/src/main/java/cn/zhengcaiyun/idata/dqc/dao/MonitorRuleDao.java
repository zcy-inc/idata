package cn.zhengcaiyun.idata.dqc.dao;

import cn.zhengcaiyun.idata.dqc.model.entity.MonitorRule;
import cn.zhengcaiyun.idata.dqc.model.query.MonitorRuleQuery;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorRuleVO;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorTableVO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface MonitorRuleDao {
    int insert(@Param("rule") MonitorRule record);

    MonitorRule getById(Long id);

    List<MonitorRuleVO> getByPage(@Param("query") MonitorRuleQuery query);

    Integer getCount(@Param("query") MonitorRuleQuery query);

    @MapKey("tableName")
    HashMap<String, MonitorTableVO> getRuleCountByTableName(@Param("tables") List<String> tables,@Param("baselineId")Long baselineId);

    @MapKey("baselineId")
    HashMap<Long, MonitorTableVO> getRuleCountByBaselineId(@Param("baselineIdList") List<Long> baselineIdList);

    int updateNotNull(@Param("rule") MonitorRule record);

    boolean del(@Param("baselineId") Long baselineId, @Param("tableName") String tableName, @Param("editor") String editor);

    int update(@Param("rule") MonitorRule record);
    int updateAccessTime(@Param("ruleId")Long ruleId,@Param("accessTime") String accessTime);

    int updateByTemplateId(@Param("rule") MonitorRule rule);

    List<MonitorRuleVO> getScheduleRuleList(@Param("typeList") List<String> typeList, @Param("startIndex") Integer startIndex);

    List<MonitorRuleVO> getBaselineScheduleRuleList(@Param("typeList") List<String> typeList, @Param("startIndex") Integer startIndex);

    List<MonitorRuleVO> getRulesByTable(@Param("tableName") String tableName);

    List<MonitorRuleVO> getBaselineRulesByTableName(@Param("tableName") String tableName);
}