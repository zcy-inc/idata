package cn.zhengcaiyun.idata.develop.dto.dag;

import cn.zhengcaiyun.idata.commons.dto.BaseDto;
import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGSchedule;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @description: dag 调度配置
 * @author: yangjianhua
 * @create: 2021-09-15 16:07
 **/
public class DAGScheduleDto extends BaseDto {
    /**
     * 主键
     */
    private Long id;

    /**
     * dag id
     */
    private Long dagId;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 周期范围，year, month, week, day, hour, minute
     */
    private String periodRange;

    /**
     * 触发方式，interval: 时间间隔，point: 指定时间
     */
    private String triggerMode;

    /**
     * cron表达式
     */
    private String cronExpression;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDagId() {
        return dagId;
    }

    public void setDagId(Long dagId) {
        this.dagId = dagId;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getPeriodRange() {
        return periodRange;
    }

    public void setPeriodRange(String periodRange) {
        this.periodRange = periodRange;
    }

    public String getTriggerMode() {
        return triggerMode;
    }

    public void setTriggerMode(String triggerMode) {
        this.triggerMode = triggerMode;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public static DAGScheduleDto from(DAGSchedule schedule) {
        DAGScheduleDto dto = new DAGScheduleDto();
        BeanUtils.copyProperties(schedule, dto);
        return dto;
    }

    public DAGSchedule toModel() {
        DAGSchedule schedule = new DAGSchedule();
        BeanUtils.copyProperties(this, schedule);
        return schedule;
    }
}