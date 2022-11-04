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

package cn.zhengcaiyun.idata.develop.manager;

import cn.zhengcaiyun.idata.develop.dal.model.job.DIStreamJobContent;
import cn.zhengcaiyun.idata.develop.dal.model.job.DIStreamJobTable;
import cn.zhengcaiyun.idata.develop.dal.repo.job.DIStreamJobContentRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.DIStreamJobTableRepo;
import cn.zhengcaiyun.idata.develop.dto.job.di.DIStreamJobContentDto;
import cn.zhengcaiyun.idata.develop.dto.job.di.DIStreamJobTableDto;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-08-17 16:25
 **/
@Component
public class DIStreamJobContentManager {

    private final DIStreamJobContentRepo streamJobContentRepo;
    private final DIStreamJobTableRepo streamJobTableRepo;

    @Autowired
    public DIStreamJobContentManager(DIStreamJobContentRepo streamJobContentRepo,
                                     DIStreamJobTableRepo streamJobTableRepo) {
        this.streamJobContentRepo = streamJobContentRepo;
        this.streamJobTableRepo = streamJobTableRepo;
    }

    @Transactional
    public void save(DIStreamJobContentDto contentDto) {
        Long id = contentDto.getId();
        final DIStreamJobContent content = contentDto.toModel();
        if (Objects.isNull(id)) {
            id = streamJobContentRepo.save(content);
            checkState(Objects.nonNull(id), "保存失败，请稍后重试");
        } else {
            streamJobContentRepo.update(content);
        }

        final Long contentId = id;
        List<DIStreamJobTableDto> tableDtoList = contentDto.getTableDtoList();
        List<DIStreamJobTable> tableList = tableDtoList.stream()
                .map(tableDto -> {
                    tableDto.setJobId(content.getJobId());
                    tableDto.setJobContentId(contentId);
                    tableDto.setJobContentVersion(content.getVersion());
                    tableDto.setSharding(content.getEnableSharding());
                    return tableDto.toModel();
                }).collect(Collectors.toList());
        streamJobTableRepo.delete(content.getJobId(), content.getVersion());
        streamJobTableRepo.save(tableList);
    }

    public DIStreamJobContentDto getJobContentDto(Long jobId, Integer version) {
        Optional<DIStreamJobContent> contentOptional = streamJobContentRepo.query(jobId, version);
        checkArgument(contentOptional.isPresent(), "作业版本不存在或已删除");

        List<DIStreamJobTable> tableList = streamJobTableRepo.query(jobId, version);
        DIStreamJobContentDto contentDto = DIStreamJobContentDto.from(contentOptional.get());
        if (CollectionUtils.isNotEmpty(tableList)) {
            List<DIStreamJobTableDto> tableDtoList = tableList.stream()
                    .map(DIStreamJobTableDto::from)
                    .collect(Collectors.toList());
            contentDto.setTableDtoList(tableDtoList);
        }
        return contentDto;
    }
}
