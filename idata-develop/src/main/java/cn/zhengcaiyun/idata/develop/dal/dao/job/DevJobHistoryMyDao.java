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

import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobHistory;
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
}
