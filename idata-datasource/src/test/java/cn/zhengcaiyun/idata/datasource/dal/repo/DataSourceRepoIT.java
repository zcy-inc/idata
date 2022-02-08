///*
// * Licensed to the Apache Software Foundation (ASF) under one or more
// * contributor license agreements.  See the NOTICE file distributed with
// * this work for additional information regarding copyright ownership.
// * The ASF licenses this file to You under the Apache License, Version 2.0
// * (the "License"); you may not use this file except in compliance with
// * the License.  You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package cn.zhengcaiyun.idata.datasource.dal.repo;
//
//import cn.zhengcaiyun.idata.commons.context.Operator;
//import cn.zhengcaiyun.idata.commons.enums.DeleteEnum;
//import cn.zhengcaiyun.idata.datasource.Util.OperatorGenerator;
//import cn.zhengcaiyun.idata.datasource.dal.dao.DataSourceDao;
//import cn.zhengcaiyun.idata.datasource.dal.model.DataSource;
//import org.hamcrest.Matcher;
//import org.hamcrest.Matchers;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.testcontainers.containers.MySQLContainer;
//import org.testcontainers.utility.DockerImageName;
//
//import java.util.Date;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//
///**
// * @description:
// * @author: yangjianhua
// * @create: 2021-12-20 16:59
// **/
////@ExtendWith(SpringExtension.class)
////@MybatisTest
//@DisplayName("DataSourceRepo Integration Test")
////@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class DataSourceRepoIT extends AbstractRepositoryBaseIT{
//
//    @Autowired
//    private DataSourceDao dataSourceDao;
//    @Autowired
//    private DataSourceRepo dataSourceRepo;
//
//    @Test
//    public void createDataSource_shouldSuccessfully(){
//        // given
//        Operator operator = OperatorGenerator.genOne();
//        DataSource dataSource = buildDataSource(null,"test_data_source_1",operator);
//
//        // when
//        Long actualId = dataSourceRepo.createDataSource(dataSource);
//        // then
//        assertThat(actualId, Matchers.is(Matchers.notNullValue()));
//    }
//
//    private DataSource buildDataSource(Long id, String name, Operator operator){
//        DataSource dataSource = new DataSource();
//        dataSource.setId(id);
//        dataSource.setName(name);
//        dataSource.setType("mysql");
//        dataSource.setEnvironments("stag,prod");
//        dataSource.setRemark("test data source repo");
//        dataSource.setDbConfigs("[{},{}]");
//        dataSource.setDel(DeleteEnum.DEL_NO.val);
//        dataSource.setCreateTime(new Date());
//        dataSource.setCreator(operator.getNickname());
//        dataSource.setEditTime(new Date());
//        dataSource.setEditor(operator.getNickname());
//        return dataSource;
//    }
//}
