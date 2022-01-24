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
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.testcontainers.containers.MySQLContainer;
//import org.testcontainers.utility.DockerImageName;
//
///**
// * @description:
// * @author: yangjianhua
// * @create: 2021-12-20 17:36
// **/
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ActiveProfiles("test")
//public abstract class AbstractRepositoryBaseIT {
//    static final MySQLContainer MY_SQL_CONTAINER;
//
//    static {
//        MY_SQL_CONTAINER = new MySQLContainer(DockerImageName.parse("mysql")).withDatabaseName("db_idata");
//        MY_SQL_CONTAINER.start();
//    }
//
//    @DynamicPropertySource
//    public static void mysqlProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
//        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
//        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
//    }
//}
