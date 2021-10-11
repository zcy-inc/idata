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

package cn.zhengcaiyun.idata.develop.service.dag.impl;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.commons.enums.UsingStatusEnum;
import cn.zhengcaiyun.idata.develop.condition.dag.DAGInfoCondition;
import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGInfo;
import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGSchedule;
import cn.zhengcaiyun.idata.develop.dal.repo.dag.DAGRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobExecuteConfigRepo;
import cn.zhengcaiyun.idata.develop.dto.dag.DAGDto;
import cn.zhengcaiyun.idata.develop.dto.dag.DAGInfoDto;
import cn.zhengcaiyun.idata.develop.dto.dag.DAGScheduleDto;
import cn.zhengcaiyun.idata.develop.service.dag.DAGService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-26 13:42
 **/
@Service
public class DAGServiceImpl implements DAGService {

    private final DAGRepo dagRepo;
    private final JobExecuteConfigRepo jobExecuteConfigRepo;

    @Autowired
    public DAGServiceImpl(DAGRepo dagRepo,
                          JobExecuteConfigRepo jobExecuteConfigRepo) {
        this.dagRepo = dagRepo;
        this.jobExecuteConfigRepo = jobExecuteConfigRepo;
    }

    @Override
    public Long addDAG(DAGDto dto, Operator operator) {
        DAGInfoDto dagInfoDto = dto.getDagInfoDto();
        DAGScheduleDto dagScheduleDto = dto.getDagScheduleDto();
        checkDag(dagInfoDto, dagScheduleDto);
        List<DAGInfo> dupNameDag = dagRepo.queryDAGInfo(dagInfoDto.getName());
        checkArgument(ObjectUtils.isEmpty(dupNameDag), "DAG名称已存在");

        dagInfoDto.setStatus(UsingStatusEnum.ENABLE.val);
        dagInfoDto.setOperator(operator);
        dagScheduleDto.setOperator(operator);

        DAGInfo info = dagInfoDto.toModel();
        DAGSchedule schedule = dagScheduleDto.toModel();
        return dagRepo.saveDAG(info, schedule);
    }

    @Override
    public Boolean editDAG(DAGDto dto, Operator operator) {
        DAGInfoDto dagInfoDto = dto.getDagInfoDto();
        DAGScheduleDto dagScheduleDto = dto.getDagScheduleDto();
        checkDag(dagInfoDto, dagScheduleDto);
        checkArgument(Objects.nonNull(dagInfoDto.getId()) && Objects.nonNull(dagScheduleDto.getId()), "DAG编号为空");
        tryFetchDAGInfo(dagInfoDto.getId());

        List<DAGInfo> dupNameDag = dagRepo.queryDAGInfo(dagInfoDto.getName());
        if (ObjectUtils.isNotEmpty(dupNameDag)) {
            DAGInfo dupNameInfo = dupNameDag.get(0);
            checkArgument(Objects.equals(dagInfoDto.getId(), dupNameInfo.getId()), "DAG名称已存在");
        }

        dagInfoDto.setStatus(null);
        dagInfoDto.resetEditor(operator);
        DAGInfo info = dagInfoDto.toModel();

        dagScheduleDto.setDagId(info.getId());
        dagScheduleDto.resetEditor(operator);
        DAGSchedule schedule = dagScheduleDto.toModel();

        //todo 更新后通知 ds
        return dagRepo.updateDAG(info, schedule);
    }

    @Override
    public DAGDto getDag(Long id) {
        DAGInfo dagInfo = tryFetchDAGInfo(id);
        Optional<DAGSchedule> optionalDAGSchedule = dagRepo.queryDAGSchedule(id);
        checkArgument(optionalDAGSchedule.isPresent(), "DAG不存在或已删除");

        DAGDto dto = new DAGDto();
        dto.setDagInfoDto(DAGInfoDto.from(dagInfo));
        dto.setDagScheduleDto(DAGScheduleDto.from(optionalDAGSchedule.get()));
        return dto;
    }

    @Override
    public Boolean removeDag(Long id, Operator operator) {
        tryFetchDAGInfo(id);
        // 删除时确定是否有作业依赖，有则不能删除
        long jobCount = jobExecuteConfigRepo.countDagJob(id);
        checkState(jobCount > 0, "该DAG存在作业依赖，不能删除");
        return dagRepo.deleteDAG(id, operator.getNickname());
    }

    @Override
    public Boolean enableDag(Long id, Operator operator) {
        tryFetchDAGInfo(id);

        DAGInfo info = new DAGInfo();
        info.setId(id);
        info.setStatus(UsingStatusEnum.ENABLE.val);
        info.setEditor(operator.getNickname());
        return dagRepo.updateDAGInfo(info);
    }

    /**
     * DAG停用：不影响已有作业，新建、修改作业时不可选择。
     *
     * @param id
     * @param operator
     * @return
     */
    @Override
    public Boolean disableDag(Long id, Operator operator) {
        tryFetchDAGInfo(id);

        DAGInfo info = new DAGInfo();
        info.setId(id);
        info.setStatus(UsingStatusEnum.DISABLE.val);
        info.setEditor(operator.getNickname());
        return dagRepo.updateDAGInfo(info);
    }

    @Override
    public List<DAGInfoDto> getDAGInfoList(DAGInfoCondition condition) {
        List<DAGInfo> dagInfoList = dagRepo.queryDAGInfo(condition);
        if (ObjectUtils.isEmpty(dagInfoList)) return Lists.newArrayList();

        return dagInfoList.stream()
                .map(DAGInfoDto::from)
                .collect(Collectors.toList());
    }

    private DAGInfo tryFetchDAGInfo(Long id) {
        checkArgument(Objects.nonNull(id), "DAG编号为空");
        Optional<DAGInfo> optionalDAGInfo = dagRepo.queryDAGInfo(id);
        checkArgument(optionalDAGInfo.isPresent(), "DAG不存在或已删除");
        return optionalDAGInfo.get();
    }

    private void checkDag(DAGInfoDto dagInfoDto, DAGScheduleDto dagScheduleDto) {
        checkArgument(Objects.nonNull(dagInfoDto), "DAG信息为空");
        checkArgument(StringUtils.isNotBlank(dagInfoDto.getName()), "DAG名称为空");
        checkArgument(StringUtils.isNotBlank(dagInfoDto.getDwLayerCode()), "DAG所属数仓分层为空");
        checkArgument(Objects.nonNull(dagInfoDto.getFolderId()), "DAG所属文件夹为空");

        checkArgument(Objects.nonNull(dagScheduleDto), "DAG调度信息为空");
        checkArgument(Objects.nonNull(dagScheduleDto.getBeginTime()) && Objects.nonNull(dagScheduleDto.getEndTime()), "DAG调度起止时间为空");
        checkArgument(StringUtils.isNotBlank(dagScheduleDto.getPeriodRange()), "DAG调度周期类型为空");
        checkArgument(StringUtils.isNotBlank(dagScheduleDto.getTriggerMode()), "DAG调度触发方式为空");
        checkArgument(StringUtils.isNotBlank(dagScheduleDto.getCronExpression()), "DAG调度时间为空");
    }
}
