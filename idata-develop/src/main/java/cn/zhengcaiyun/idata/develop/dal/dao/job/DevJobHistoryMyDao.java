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
package cn.zhengcaiyun.idata.develop.dal.dao.job;

import cn.hutool.core.date.DateTime;
import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobHistory;
import cn.zhengcaiyun.idata.develop.dto.job.JobHistoryDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import javax.annotation.Generated;
import java.util.Date;
import java.util.List;

/**
 * @author caizhedong
 * @date 2021-07-26 17:23
 */

@Mapper
public interface DevJobHistoryMyDao {

    @Insert("<script>" +
            "insert into dev_job_history(create_time, job_id, start_time, finish_time, duration, final_status, " +
            "avg_vcores, avg_memory, application_id) " +
            "values " +
            "<foreach collection='list' item='item' index='index' separator=','> " +
            "(#{item.createTime}, #{item.jobId}, #{item.startTime}, #{item.finishTime}, #{item.duration}, #{item.finalStatus}, " +
            "#{item.avgVcores}, #{item.avgMemory}, #{item.applicationId})" +
            "</foreach> " +
            "ON DUPLICATE KEY UPDATE final_status = VALUES(final_status)" +
            "</script>")
    void batchUpsert(List<DevJobHistory> list);

    //"SET SESSION sql_mode=(SELECT REPLACE(@@sql_mode, 'ONLY_FULL_GROUP_BY',''));" + //设置当前会话group by可显示其他字段内容
    @Select("<script>" +
            "SET SESSION sql_mode=(SELECT REPLACE(@@sql_mode, 'ONLY_FULL_GROUP_BY',''));" + //设置当前会话group by可显示其他字段内容
            "select *, AVG(duration) as avg_duration " +
            "from (select * from dev_job_history where <![CDATA[start_time >= #{startDate}]]> and <![CDATA[finish_time <= #{endDate}]]> order by duration desc limit #{top}) as t " +
            "group by t.job_id" +
            "</script>")
    List<JobHistoryDto> topDurationGroupByJobId(String startDate, String endDate, int top);

    @Select("<script>" +
            "SET SESSION sql_mode=(SELECT REPLACE(@@sql_mode, 'ONLY_FULL_GROUP_BY',''));" + //设置当前会话group by可显示其他字段内容
            "select * " +
            "from (select * from dev_job_history where <![CDATA[start_time >= #{startDate}]]> and <![CDATA[finish_time <= #{endDate}]]> order by avg_memory/4096, avg_vcores desc limit #{top}) as t " +
            "group by t.job_id" +
            "</script>")
    List<JobHistoryDto> topResourceGroupByJobId(String startDate, String endDate, int top);

    @Select("<script>" +
            "select t1.*, t2.name as jobName " +
            "from ( " +
            "      select * from dev_job_history where del = 0 and" +
            "       <if test = 'startDateBegin != null'>" +
            "           AND start_time <!CDATA[ >= ]]> #{startDateBegin} " +
            "       </if>" +
            "       <if test = 'startDateEnd != null'>" +
            "           AND start_time <!CDATA[ <= ]]> #{startDateEnd} " +
            "       </if>" +
            "       <if test = 'finishDateBegin != null'>" +
            "           AND finish_time <!CDATA[ >= ]]> #{finishDateBegin} " +
            "       </if>" +
            "       <if test = 'finishDateEnd != null'>" +
            "           AND finish_time <!CDATA[ <= ]]> #{finishDateEnd} " +
            "       </if>" +
            "       <if test = 'jobStatus != null'>" +
            "           AND final_status = #{jobStatus} " +
            "       </if>" +
            ") as t1 " +
            "join (select * from dev_job_info where 1 = 1 " +
            "       <if test = 'jobName != null'>" +
            "           AND name like concat('%', #{jobName}, '%') " +
            "       </if>" +
            ") as t2 " +
            "on t1.job_id = t2.id " +
            "</script>")
    List<JobHistoryDto> selectList(String startDateBegin, String startDateEnd, String finishDateBegin, String finishDateEnd, String jobName, String jobStatus);
}


