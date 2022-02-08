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

package cn.zhengcaiyun.idata.datasource.service;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.commons.enums.DataSourceTypeEnum;
import cn.zhengcaiyun.idata.commons.enums.DeleteEnum;
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.datasource.Util.OperatorGenerator;
import cn.zhengcaiyun.idata.datasource.bean.dto.DataSourceDto;
import cn.zhengcaiyun.idata.datasource.bean.dto.DbConfigDto;
import cn.zhengcaiyun.idata.datasource.dal.model.DataSource;
import cn.zhengcaiyun.idata.datasource.dal.repo.DataSourceRepo;
import cn.zhengcaiyun.idata.datasource.manager.DataSourceManager;
import cn.zhengcaiyun.idata.datasource.service.impl.DataSourceServiceImpl;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-12-20 10:40
 **/
@DisplayName("DataSourceService Unit Test")
@ExtendWith(MockitoExtension.class)
public class DataSourceServiceTest {

    private DataSourceRepo dataSourceRepo;
    @Mock
    private DataSourceManager dataSourceManager;

    private DataSourceService dataSourceService;

    @BeforeAll
    @DisplayName("setup before all unit test of DataSourceService")
    public static void setupGlobal() {

    }

    @BeforeEach
    @DisplayName("setup before each unit test of DataSourceService")
    public void setup() {
        dataSourceRepo = mock(DataSourceRepo.class);
        dataSourceService = new DataSourceServiceImpl(dataSourceRepo, dataSourceManager);
    }

    @AfterEach
    @DisplayName("clean before each unit test of DataSourceService")
    public void clean() {

    }

    @AfterAll
    @DisplayName("clean before all unit test of DataSourceService")
    public static void cleanGlobal() {

    }

    @Test
    public void addDataSource_shouldSuccessfully() {
        // given
        Operator operator = OperatorGenerator.genOne();
        DataSourceDto dataSourceDto = buildDataSourceDto("test_db_1");
        DataSource dataSource = dataSourceDto.toModel();

        Long expectId = 1L;
        given(dataSourceRepo.queryDataSource(dataSourceDto.getName())).willReturn(Lists.newArrayList());
        given(dataSourceRepo.createDataSource(any(DataSource.class))).willReturn(expectId);

        // when
        Long actualId = dataSourceService.addDataSource(dataSourceDto, operator);

        // then
        assertThat(actualId, is(equalTo(expectId)));
    }

    @Test
    public void addDataSource_shouldThrowException_whenExistSameName() {
        // given
        Operator operator = OperatorGenerator.genOne();
        DataSourceDto dataSourceDto = buildDataSourceDto("test_db_1");

        Long expectId = 1L;
        DataSource sameNameDS = new DataSource();
        sameNameDS.setName(dataSourceDto.getName());
        given(dataSourceRepo.queryDataSource(dataSourceDto.getName())).willReturn(Lists.newArrayList(sameNameDS));

        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> dataSourceService.addDataSource(dataSourceDto, operator));

        // then
        assertThat("数据源名称已存在", is(equalTo(exception.getMessage())));
    }

    @Test
    public void editDataSource_shouldSuccessfully_whenNotChangeName() {
        // given
        Operator operator = OperatorGenerator.genOne();
        DataSourceDto editedDataSourceDto = buildDataSourceDto(1L, "test_db_1");
        editedDataSourceDto.setRemark(editedDataSourceDto.getRemark() + " changed");

        DataSource existDS = buildDataSourceDto(1L, "test_db_1").toModel();
        given(dataSourceRepo.queryDataSource(editedDataSourceDto.getId())).willReturn(Optional.of(existDS));
        given(dataSourceRepo.updateDataSource(any(DataSource.class))).willReturn(true);

        // when
        Boolean actualRet = dataSourceService.editDataSource(editedDataSourceDto, operator);

        // then
        assertThat(actualRet, is(true));
    }

    @Test
    public void editDataSource_shouldSuccessfully_whenChangeName() {
        // given
        Operator operator = OperatorGenerator.genOne();
        DataSourceDto editedDataSourceDto = buildDataSourceDto(1L, "test_db_1_c");
        editedDataSourceDto.setRemark(editedDataSourceDto.getRemark() + " changed");

        DataSource existDS = buildDataSourceDto(1L, "test_db_1").toModel();
        given(dataSourceRepo.queryDataSource(editedDataSourceDto.getId())).willReturn(Optional.of(existDS));
        given(dataSourceRepo.queryDataSource(editedDataSourceDto.getName())).willReturn(Lists.newArrayList());
        given(dataSourceRepo.updateDataSource(any(DataSource.class))).willReturn(true);

        // when
        Boolean actualRet = dataSourceService.editDataSource(editedDataSourceDto, operator);

        // then
        assertThat(actualRet, is(true));
    }

    @Test
    public void editDataSource_shouldThrowException_whenIdIsIllegal() {
        // given
        Operator operator = OperatorGenerator.genOne();
        DataSourceDto editedDataSourceDto = buildDataSourceDto(1L, "test_db_1_c");
        editedDataSourceDto.setRemark(editedDataSourceDto.getRemark() + " changed");

        given(dataSourceRepo.queryDataSource(editedDataSourceDto.getId())).willReturn(Optional.empty());

        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> dataSourceService.editDataSource(editedDataSourceDto, operator));

        // then
        assertThat("数据源不存在", is(equalTo(exception.getMessage())));
    }

    @Test
    public void editDataSource_shouldThrowException_whenIsDeleted() {
        // given
        Operator operator = OperatorGenerator.genOne();
        DataSourceDto editedDataSourceDto = buildDataSourceDto(1L, "test_db_1_c");
        editedDataSourceDto.setRemark(editedDataSourceDto.getRemark() + " changed");

        DataSource existDS = buildDataSourceDto(1L, "test_db_1").toModel();
        existDS.setDel(DeleteEnum.DEL_YES.val);
        given(dataSourceRepo.queryDataSource(editedDataSourceDto.getId())).willReturn(Optional.of(existDS));

        // when
        Exception exception = assertThrows(IllegalStateException.class, () -> dataSourceService.editDataSource(editedDataSourceDto, operator));

        // then
        assertThat("数据源已删除", is(equalTo(exception.getMessage())));
    }

    @Test
    public void editDataSource_shouldThrowException_whenExistSameName() {
        // given
        Operator operator = OperatorGenerator.genOne();
        DataSourceDto editedDataSourceDto = buildDataSourceDto(1L, "test_db_1_c");
        editedDataSourceDto.setRemark(editedDataSourceDto.getRemark() + " changed");

        DataSource existDS = buildDataSourceDto(1L, "test_db_1").toModel();
        DataSource sameNameDS = buildDataSourceDto(2L, "test_db_1_c").toModel();
        given(dataSourceRepo.queryDataSource(editedDataSourceDto.getId())).willReturn(Optional.of(existDS));
        given(dataSourceRepo.queryDataSource(editedDataSourceDto.getName())).willReturn(Lists.newArrayList(sameNameDS));

        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> dataSourceService.editDataSource(editedDataSourceDto, operator));

        // then
        assertThat("数据源名称已存在", is(equalTo(exception.getMessage())));
    }

    @Test
    public void removeDataSource_shouldSuccessfully() {
        //given
        Operator operator = OperatorGenerator.genOne();
        Long deletedId = 1L;

        DataSourceDto dataSourceDto = buildDataSourceDto(deletedId, "test_db_1");
        DataSource existDS = dataSourceDto.toModel();
        given(dataSourceRepo.queryDataSource(deletedId)).willReturn(Optional.of(existDS));
        given(dataSourceManager.checkInUsing(dataSourceDto.getType(), deletedId)).willReturn(false);
        given(dataSourceRepo.deleteDataSource(deletedId, operator.getNickname())).willReturn(true);

        // when
        Boolean actualRet = dataSourceService.removeDataSource(deletedId, operator);

        //then
        assertThat(actualRet, is(true));
    }

    @Test
    public void removeDataSource_shouldSuccessfully_whenHasDeleted() {
        //given
        Operator operator = OperatorGenerator.genOne();
        Long deletedId = 1L;

        DataSourceDto dataSourceDto = buildDataSourceDto(deletedId, "test_db_1");
        DataSource existDS = dataSourceDto.toModel();
        existDS.setDel(DeleteEnum.DEL_YES.val);
        given(dataSourceRepo.queryDataSource(deletedId)).willReturn(Optional.of(existDS));

        // when
        Boolean actualRet = dataSourceService.removeDataSource(deletedId, operator);

        //then
        assertThat(actualRet, is(true));
    }

    @Test
    public void removeDataSource_shouldThrowException_whenIdIsIllegal() {
        //given
        Operator operator = OperatorGenerator.genOne();
        Long deletedId = 1L;

        given(dataSourceRepo.queryDataSource(deletedId)).willReturn(Optional.empty());

        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> dataSourceService.removeDataSource(deletedId, operator));

        // then
        assertThat("数据源不存在", is(equalTo(exception.getMessage())));
    }

    @Test
    public void removeDataSource_shouldThrowException_whenInUsing() {
        //given
        Operator operator = OperatorGenerator.genOne();
        Long deletedId = 1L;

        DataSourceDto dataSourceDto = buildDataSourceDto(deletedId, "test_db_1");
        DataSource existDS = dataSourceDto.toModel();
        given(dataSourceRepo.queryDataSource(deletedId)).willReturn(Optional.of(existDS));
        given(dataSourceManager.checkInUsing(dataSourceDto.getType(), deletedId)).willReturn(true);

        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> dataSourceService.removeDataSource(deletedId, operator));

        // then
        assertThat("数据源正在被使用，不能删除", is(equalTo(exception.getMessage())));
    }

    private DataSourceDto buildDataSourceDto(Long id, String name) {
        DataSourceDto dto = new DataSourceDto();
        dto.setId(id);
        dto.setType(DataSourceTypeEnum.mysql);
        dto.setName(name);
        List<EnvEnum> envList = Arrays.asList(EnvEnum.stag, EnvEnum.prod);
        dto.setEnvList(envList);
        dto.setRemark("a test data source");
        dto.setDel(DeleteEnum.DEL_NO.val);

        DbConfigDto stagDb = build(EnvEnum.stag, "db_test_idata_stag", "testName_stag", "testPwd", "127.0.0.1", 3306, "");
        DbConfigDto prodDb = build(EnvEnum.prod, "db_test_idata_prod", "testName_prod", "testPwd", "127.0.0.1", 3306, "");
        List<DbConfigDto> dbConfigList = Arrays.asList(stagDb, prodDb);
        dto.setDbConfigList(dbConfigList);
        return dto;
    }

    private DataSourceDto buildDataSourceDto(String name) {
        return this.buildDataSourceDto(null, name);
    }

    private DbConfigDto build(EnvEnum env, String dbName, String username, String password, String host, Integer port, String schema) {
        DbConfigDto dbConfigDto = new DbConfigDto();
        dbConfigDto.setEnv(env);
        dbConfigDto.setDbName(dbName);
        dbConfigDto.setHost(host);
        dbConfigDto.setPort(port);
        dbConfigDto.setUsername(username);
        dbConfigDto.setPassword(password);
        dbConfigDto.setSchema(schema);
        return dbConfigDto;
    }

}
