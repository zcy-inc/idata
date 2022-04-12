# IData 开源版使用手册
## 1. 什么是IData
IData是一个现代化的大数据开发管理平台，集成了各种大数据基础设施能力、算法的开发应用与部署能力、数据监控治理能力以及数据分析能力，IData提供完整的大数据开发流程，例如数据集成、数据开发、数据测试、数据发布、任务运维等，关注并聚焦于数据的全链路生命周期。能够非常方便高效的满足您对大数据开发的需求。
![](https://sitecdn.zcycdn.com/f2e-assets/458ca9e3-fdaf-4c16-8c4e-817de5b1d794.png?x-oss-process=image/quality,Q_75/format,jpg)
目标用户
IData的目标是提高数据研发的效率，降低数据研发的门槛，让数据能够在一个设备流水线上快速地完成加工。甚至anyone用户可以通过SQL、脚本方式像处理普通数据一样处理大数据。这里，anyone意味着用户不需要有如Spark、任务调度等技术背景。

## 2. 架构介绍
![](https://sitecdn.zcycdn.com/f2e-assets/69ba2cd8-195a-4304-8559-c303093155c2.png?x-oss-process=image/quality,Q_75/format,jpg)
![](https://sitecdn.zcycdn.com/f2e-assets/d654c062-f453-425e-b104-dd9471961f17.png?x-oss-process=image/quality,Q_75/format,jpg)
## 3. 搭建部署
前端启动脚本：/bin/frontend_build.sh

// step1:打包成镜像
apple@localhost bin % ./frontend_build.sh


// step2:推送镜像
apple@localhost bin % docker push yourDockerImage.repo.com


后端启动脚本：/bin/backend_build.sh

// step1:打包成镜像
apple@localhost bin % ./backend_build.sh


// step2:推送镜像
apple@localhost bin % docker push yourDockerImage.repo.com



## 4. 系统配置
### 4.1 权限管理
#### 4.1.1 角色管理
角色管理模块，用户能够通过对IData各个菜单和功能权限的控制，根据日常使用需求对平台用户进行角色划分，从而进行权限控制。
##### 新增角色操作步骤
路径：系统配置—权限管理—角色管理—新增角色
![](https://sitecdn.zcycdn.com/f2e-assets/06824eef-9b7a-4495-b44b-1b3aaf801c4e.png?x-oss-process=image/quality,Q_75/format,jpg)

1. 在系统配置菜单下，选择权限管理，点击【新增角色】按钮，进入新增页面；
   ![](https://sitecdn.zcycdn.com/f2e-assets/f4d3c7d2-3112-489b-80f3-c5fd8d9a30fb.png?x-oss-process=image/quality,Q_75/format,jpg)
2. 在“角色新增”页内，选择“功能”标签页，选中需要配置的菜单功能，在右侧进行功能的开启和关闭；
3. 在“角色新增”页内，选择“资源”标签页，选中需要配置的资源，在右侧进行资源的选中和取消；
   ![](https://sitecdn.zcycdn.com/f2e-assets/1c9a6e76-0e4d-49c0-a053-fbe0ef1373b9.png?x-oss-process=image/quality,Q_75/format,jpg)
### 4.2 集成配置
集成配置模块，用户能够通过参数值的输入将IData与调度系统、Hadoop、抽数等工具平台进行连接。
调度系统
![](https://sitecdn.zcycdn.com/f2e-assets/a6adaebd-38f3-46a1-b177-ac7fc8fc1a1f.png?x-oss-process=image/quality,Q_75/format,jpg)字段说明：
url：DolphinScheduler restful接口地址
token：DolphinScheduler 接口访问token，可以在DolphinScheduler 中创建一个用户，然后生成该用户的token
DStenantCode：DolphinScheduler 租户code
DSWorkGroup：DolphinScheduler 工作组
prodDSProjectCode：DolphinScheduler 项目的编号，创建一个项目后获取，对应IData中的真线环境
stagDSProjectCode：DolphinScheduler 项目的编号，创建一个项目后获取，对应IData中的预发环境
dagTimeout：DolphinScheduler 工作流默认的超时时间




##### Hive MetaStore
url字段为Hive Metastore元数据库地址。
username和password为元数据库用户名和密码。
![](https://sitecdn.zcycdn.com/f2e-assets/581b9f54-1deb-4fa9-aac7-bfcc46714910.png?x-oss-process=image/quality,Q_75/format,jpg)

##### Hadoop
core-site、hdfs-site和yarn-site即Hadoop官方core-site.xml、hdfs-site.xml和yarn-site.xml文件的解析内容，支持XML文件上传自动解析。
![](https://sitecdn.zcycdn.com/f2e-assets/5f4cb952-f12f-48be-988f-3e6d760c44bf.png?x-oss-process=image/quality,Q_75/format,jpg)

##### 抽数配置

yarn.addr：yarn RM地址+端口
kylin.auth：预置的kylin API调用HTTP header中Authorization
cluster：集群名称
idata.insert.erase.url：idata内置方法，http://${host}:${port}/api/p0/dev/spark/insertErase
idata.sql.rewrite.url：idata内置方法，http://${host}:${port}/api/p0/dev/spark/addDatabaseEnv
idata.job.detail.url：idata内置方法，http://${host}:${port}/api/p0/job/execute-detail
idata.monitor.url：idata内置方法，http://${host}:${port}/api/p0/quality/monitorHistory
hdfs.addr：namenode 地址+端口
kylin.api.url：kylin服务地址+端口


![](https://sitecdn.zcycdn.com/f2e-assets/962071c5-e206-4810-a062-6fdfd4631291.png?x-oss-process=image/quality,Q_75/format,jpg)


##### Livy
url为Livy的url路径
livy.sessionMax为Livy可同时存在最大session数量
![](https://sitecdn.zcycdn.com/f2e-assets/c9021e83-d9da-4f1d-a8a7-d575a39c164c.png?x-oss-process=image/quality,Q_75/format,jpg)

### 4.3 LDAP配置
用户能够通过LDAP配置中配置的LDAP相关信息，方便对接您当前正在使用的LDAP实现用户登录权限功能。
### 4.4 元数据属性配置
元数据属性配置主要用于多场景下表单页面的配置，您可以在元数据属性配置页面查看和编辑多模块的表单页面。
##### 配置适用场景表单

1. 点击系统配置**>**元数据属性配置页面。
2. 进入元数据属性配置页面，选择上方tab为表单基本信息或表结构设计，其中表单基本信息是面向数仓设计中表信息，表结构设计面向数仓设计中表结构设计信息。
3. 单击页面末端的新增属性。
4. 在新增属性对话框中，配置各项参数。
5. 在新增数据源对话框中，选择数据源类型为**mysql**。

**![](https://sitecdn.zcycdn.com/f2e-assets/210297c4-b2a2-43f4-9f44-88a386b185f5.png?x-oss-process=image/quality,Q_75/format,jpg)**

| 参数 | 描述 |
| --- | --- |
| 属性名称 | 输入需在表单页面展示的元件名称。 |
| 属性类型 | 选择所需表单元件类型，现仅支持输入框，选择器和布尔单选器。1. 选择选择器后，需点击添加一行数据增加下拉选项。|
| 是否必填 | 在表单页面是否为必填项，在保存时进行校验。 |

1. 点击确定后在页面处展示。
   ![](https://sitecdn.zcycdn.com/f2e-assets/5f404f9b-1463-4d84-b353-38e18871d7c1.png?x-oss-process=image/quality,Q_75/format,jpg)点击编辑已创建元件标签，点击删除已创建元件标签。
1. 新建完成的元数据属性标签能够在数仓设计中体现。

## 5.数据研发

IData的数据研发模块为您提供了界面化、智能高效的大数据数据开发与测试体验，本文将基于支持开发的任务类型、开发过程中的资源管控与使用说明、开发过程中的成员权限控制（资源与功能）来说明数据开发的功能使用。

### 5.1 数据源管理
数据源主要用于数据集成过程中读取和写入的对象，您可以在数据源管理页面查看、新增、编辑和删除数据源。


进入IData页面后，单击左侧导航栏中的数据源管理，即可进入数据源管理页面。
您可以在数据源管理页面，根据数据源类型、数据源名称等条件筛选需要查看的数据源。
单击页面右上角的新增数据源，即可新增相应的数据源，详情请参见数据源配置。


目前支持数据源类型：mysql, postgresql, hive, presto, kylin, phoenix, elasticsearch, csv。


#### 5.1.1 数据型数据源配置
##### 配置mysql数据源操作步骤

1. 进入数据源管理页面。
2. 在数据源管理页面，单击右上角的新增数据源。
3. 在新增数据源对话框中，选择数据源类型为**mysql**。
4. 在新增**mysql**数据源对话框中，配置各项参数。
5. 选择该数据源所需开发环境。
   ![](https://sitecdn.zcycdn.com/f2e-assets/a28fa6c3-16a3-49c7-930c-4689db8ff8ff.png?x-oss-process=image/quality,Q_75/format,jpg)配置数据源的基本信息。




| 参数 | 描述 |
| --- | --- |
| 数据源类型 | 当前选择的数据源类型为mysql。 |
| 数据源名称 | 数据源名称必须以字母、数字、下划线组合，且不能以数字和下划线开头。 |
| 环境 | 可以选择stag或prod环境。 |
| 备注说明 | 对数据源进行简单描述，不得超过80个字符。 |
| 数据库名称 | 此处配置的是该数据源对应的默认数据库名称。 |
| 数据库账号 | 登录数据库的用户名称。 |
| 数据库密码 | 登录数据库的密码。 |
| 服务器地址 | 地址包含host和port两部分。 |
| 目录 | 此处配置的是该数据源对应的数据库schema名称。 |

6. 单击测试连通性进行连通性测试。
7. 测试连通性通过后，单击确定。
#### 5.1.2 文件型数据源配置
##### 配置csv数据源操作步骤

1. 进入数据源管理页面。
2. 在数据源管理页面，单击右上角的新增数据源。
3. 在新增数据源对话框中，选择数据源类型为**csv**。
4. 在新增**mysql**数据源对话框中，配置各项参数。
5. 选择该数据源所需开发环境。
6. 配置数据源的基本信息。

![](https://sitecdn.zcycdn.com/f2e-assets/daff608a-ef4a-4518-afe5-137653139348.png?x-oss-process=image/quality,Q_75/format,jpg)

| 参数 | 描述 |
| --- | --- |
| 数据源类型 | 当前选择的数据源类型为mysql。 |
| 数据源名称 | 数据源名称必须以字母、数字、下划线组合，且不能以数字和下划线开头。 |
| 环境 | 可以选择stag或prod环境。 |
| 备注说明 | 对数据源进行简单描述，不得超过80个字符。 |

7. 点击上传文件后，将数据在下方进行预览展示。

### 5.2 数仓设计
数仓设计是针对元数据表的相关定义以及表结构设计。
IData认为一个完整的数据开发流程，应当首先完成元数据表结构的设计，数仓设计承担的就是这一部分工作。同时，在数仓设计中也能够对表的非元数据信息进行定义，赋予这张表更详实的信息，而为了保证高扩展性，通过系统配置中的元数据属性配置实现动态表单功能满足用户的定制化属性配置需求。
在表结构设计中，可能存在例如宽表等字段非常多的情况，为提升用户体验，支持通过DDL解析自动生成表结构，同时也支持根据当前已配置的表和字段信息生成DDL，极大解放数据开发人员。
数仓设计具体流程：

1. 点击数据研发>数据开发>数仓设计页面。
2. 在左侧目录列，点击右上角的新增>新建表。
3. 在新建表界面中，配置各项参数。
   i.![](https://sitecdn.zcycdn.com/f2e-assets/861e02fe-9301-4d55-8f2e-04fe62c07b40.png?x-oss-process=image/quality,Q_75/format,jpg)

   ​		ii.界面中基本信息和表结构设计中属性前有*表示该属性为必填。用户也可在元数据标签配置中自定义必填的属性标签。点击基本信息旁按钮，可选择性显示非必填属性。
   ![](https://sitecdn.zcycdn.com/f2e-assets/662dab97-025b-43f4-b704-c09f5526c3f4.png?x-oss-process=image/quality,Q_75/format,jpg)


基本信息

| 参数 | 描述 |
| --- | --- |
| 表名称 | 元数据表的名称。 |
| 数据库名称 | 元数据表所属的schema名。 |
| 是否分区表 | 是否为分区表，是或否选择。 |
| 数仓分层 | 元数据表在数仓中所属分层，当前默认分层有ODS、DWD、DWS、DIM和ADS. |
| 表中文名称 | 元数据表的中文名称。 |
| 数仓所属人 | 设计该表的数据开发所属人，来源于IData登录用户。 |
| 业务所属人 | 提出需求需要将该表落入到大数据的业务方所属人。 |
| 数据域 | 元数据表所属的数据域。 |
| 位置 | 元数据表在数仓设计的文件夹位置。 |

4. 完成表基本信息填写后，可直接进行表结构设计和关系表配置，配置各项参数（若要使用DDL模式生成表结构，需先直接点击右下角保存，在查看界面使用）。
   ​			i.点击下方的添加字段。![](https://sitecdn.zcycdn.com/f2e-assets/ae2ded7d-2546-4ff6-bda2-a488b0586e0f.png?x-oss-process=image/quality,Q_75/format,jpg)
   ​			ii.界面中表结构设计中属性前有*表示该属性为必填。用户也可在元数据标签配置中自定义必填的属性标签。点击表结构设计旁按钮，可选择性显示非必填属性。![](https://sitecdn.zcycdn.com/f2e-assets/777664a5-b95a-4f63-8b89-4805e69d19a6.png?x-oss-process=image/quality,Q_75/format,jpg)

| 参数 | 描述 |
| --- | --- |
| 字段英文名 | 元数据表的名称。 |
| 是否主键 | 是否为分区表，是或否选择。 |
| 字段类型 | 元数据表在数仓中所属分层，当前默认分层有ODS、DWD、DWS、DIM和ADS. |
| 是否分区字段 | 元数据表的中文名称。 |

5. 点击右下角保存可完成数据设计的新建。
6. 在查看页面点击DDL模式，可查看当前表结构设计的DDL语句，并支持将DDL语句复制至输入框中，点击弹窗右下角的生成表结构按钮生成表结构。

![](https://sitecdn.zcycdn.com/f2e-assets/840f6f04-8f57-4031-b851-06a70ea825c8.png?x-oss-process=image/quality,Q_75/format,jpg)


### 5.3 DAG管理
DAG是有向无环图的意思，调度系统中工作流就是以DAG的方式组织任务的依赖关系。您可以在DAG管理页面查看、新增、编辑和删除DAG，并在数据集成和数据开发模块内进行引用。
##### DAG配置

1. 点击数据研发**>**数据开发**>DAG**页面。
2. 在左侧目录列，单击右上角的新增**>**新建**DAG**。
3. 在新增**DAG**标签页中，配置各项参数。
4. 配置DAG的基本信息。

![](https://sitecdn.zcycdn.com/f2e-assets/c7cbd0da-b29a-49de-83d7-ef2ccb044fff.png?x-oss-process=image/quality,Q_75/format,jpg)

| 参数 | 描述 |
| --- | --- |
| DAG名称 | DAG名称必须以字母、数字、下划线（_）组合，且不能以数字和下划线（_）开头。 |
| 数仓分层 | 选择该DAG所属数仓分层，可选择ODS，DIM，DWD，DWS和ADS。 |
| 目标文件夹 | 选择目录归属文件夹。 |
| 始止时间 | DAG调度生效和时效的时间范围。 |
| 调度周期 | DAG调度周期，可选择年，月，周，日，小时和分钟。年调度即调度任务在每年的特定几天，在特定时间点自动运行一次；月调度即调度任务在每月的特定几天，在特定时间点自动运行一次；周调度即调度任务每周的特定几天，在特定时间点自动运行一次；日调度即调度节点每天在指定的定时时间运行一次。新建周期任务时，日调度默认的时间周期为每天0点运行一次。您可以根据需要自行指定运行时间点，例如，指定每天13点运行一次；小时调度即每天指定的时间段内，调度任务按N*1小时的时间间隔运行一次。例如，每天00:00~03:00的时间段内，每1小时运行一次分钟调度即每天指定的时间段内，调度任务按N*指定分钟的时间间隔运行一次。 |

### 5.3 数据集成
数据集成是稳定高效、弹性伸缩的数据同步平台，致力于提供复杂网络环境下、丰富的异构数据源之间高速稳定的数据移动及同步能力。目前仅支持离线同步。
离线同步简介
数据集成主要用于离线数据同步。离线数据通道通过定义数据来源和去向的数据源和数据集，提供一套抽象化的数据抽取插件（Reader）、数据写入插件（Writer），并基于此框架设计一套简化版的中间数据传输格式，从而实现任意结构化、半结构化数据源之间数据传输。
##### 离线数据同步
开发流程：

1. 点击数据研发**>**数据开发页面。
2. 在左侧目录列，单击右上角的新增**>**新建作业。
3. 在新建**DI**任务弹窗中，选择任务类型，数据分层和目标文件夹，输入任务名称。
   ![](https://sitecdn.zcycdn.com/f2e-assets/7cd6c326-a00f-475a-994f-8adb4cbb74b5.png?x-oss-process=image/quality,Q_75/format,jpg)配置任务的基本信息。




| 参数 | 描述 |
| --- | --- |
| 任务类型 | 选择需要创建的任务类型。目前仅支持离线同步。 |
| 任务名称 | 任务名称必须以字母、数字、下划线（_）组合，且不能以数字和下划线（_）开头。 |
| 数仓分层 | 选择该DAG所属数仓分层，可选择ODS，DIM，DWD，DWS和ADS。 |
| 目标文件夹 | 选择目录归属文件夹。 |
| 备注说明 | 对任务进行简单描述，不得超过80个字符。 |



4. 点击确定。
   ![](https://sitecdn.zcycdn.com/f2e-assets/017d6733-27d1-4edb-bf6f-c0e20aa703f8.png?x-oss-process=image/quality,Q_75/format,jpg)在新建任务标签中，首先需要配置离线同步节点的读取端数据源，以及需要同步的表等信息。




| 参数 | 描述 |
| --- | --- |
| 数据源 | 通常输入您配置的数据源名称。 |
| 表 | 所选取的需要同步的表。 |
| 数据过滤 | 您将要同步数据的筛选条件，暂时不支持limit关键字过滤。SQL语法与选择的数据源一致。 |
| 切分键 | 您可以将源数据表中某一列作为切分键，建议使用主键或有索引的列作为切分键，仅支持类型为整型的字段。 |
| 读取模式 | 选择数据读取模式。 |



![](https://sitecdn.zcycdn.com/f2e-assets/0353dac8-b979-4d0e-a2cc-2b8342b5e22d.png?x-oss-process=image/quality,Q_75/format,jpg)在新建任务标签中，首先需要选择数据来源，配置离线同步节点的读取端数据源，以及需要同步的表等信息。




| 参数 | 描述 |
| --- | --- |
| 数据源类型 | 通常选择您配置的数据源类型。 |
| 数据源名称 | 通常输入您配置的数据源名称。 |
| 表 | 所选取的需要同步的表。 |
| 数据过滤 | 您将要同步数据的筛选条件，暂时不支持limit关键字过滤。SQL语法与选择的数据源一致。 |
| 切分键 | 您可以将源数据表中某一列作为切分键，建议使用主键或有索引的列作为切分键，仅支持类型为整型的字段。 |
| 读取模式 | 选择数据读取模式。 |



![](https://sitecdn.zcycdn.com/f2e-assets/9a6d236e-6344-42d3-b355-119b0fd154c1.png?x-oss-process=image/quality,Q_75/format,jpg)选择数据去向，完成读取端数据源的配置后，您可以配置右侧的写入端数据源，以及需要写入的表信息等。




| 参数 | 描述 |
| --- | --- |
| 数据源类型 | 通常选择您配置的数据源类型。 |
| 数据源名称 | 通常输入您配置的数据源名称。 |
| 表 | 所选取的需要写入的表。 |
| 导入前准备语句 | 输入执行数据同步任务之前率先执行的SQL语句。 |
| 导入后准备语句 | 输入执行数据同步任务之后执行的SQL语句。 |
| 写入模式 | 选择数据写入模式类型。读取模式为增量时无需选择写入模式类型。 |



![](https://sitecdn.zcycdn.com/f2e-assets/81d2ce91-08c9-4918-8de8-298e320fe8e5.png?x-oss-process=image/quality,Q_75/format,jpg)配置字段的映射关系，选择数据来源和数据去向后，需指定读取端和写入端的映射关系。




| 参数 | 描述 |
| --- | --- |
| 同名映射 | 可以根据名称建立相应的映射关系，请注意匹配数据类型。 |
| 取消映射 | 可以取消建立的映射关系。 |
| 复制来源表 | 写入表未创建时，需点击复制来源表复制表结构。 |
| 手动映射 | 手动编辑读取端和写入端字段的映射关系，不创建关联的字段会被忽略。 |



![](https://sitecdn.zcycdn.com/f2e-assets/598865bb-06ef-49ba-9d19-6e06a3b3df83.png?x-oss-process=image/quality,Q_75/format,jpg)配置调度属性。
| 参数 | 描述 |
| --- | --- |
| 空跑调度 | 勾选后任务默认成功。 |
| DAG | 选择任务所属DAG。 |
| 队列 |  |
| 超时时间 | 当任务的运行时长超过超时时间时,任务自动终止运行。 |
| 重跑属性 | 当前任务的重跑规则。 |
| 报警等级 |  |
| 任务期望最大并发数 | 数据同步任务内，可以从源并行读取或并行写入数据存储端的最大线程数。 |

### 5.4 数据开发
数据开发模块提供多类型任务开发调试和函数管理能力。
##### 创建SQL任务
开发流程：

1. 在左侧目录列，单击右上角的新增**>**新建**DI**。
2. 在新建**DI**任务弹窗中，选择任务类型，数据分层和目标文件夹，输入任务名称。
   ![](https://sitecdn.zcycdn.com/f2e-assets/b31ade74-cd5c-460b-9509-ec1d35ec5c17.png?x-oss-process=image/quality,Q_75/format,jpg)配置任务的基本信息。




| 参数 | 描述 |
| --- | --- |
| 任务类型 | 选择需要创建的任务类型。目前仅支持script/kylin/spark/SQLr。 |
| 任务名称 | 任务名称必须以字母、数字、下划线（_）组合，且不能以数字和下划线（_）开头。 |
| 数仓分层 | 选择该DAG所属数仓分层，可选择ODS，DIM，DWD，DWS和ADS。 |
| 目标文件夹 | 选择目录归属文件夹。 |
| 备注说明 | 对任务进行简单描述，不得超过80个字符。 |



3. 点击确定。
4. 在新建任务标签页面中，在节点的编辑页面，编辑符合语法的SQL代码并运行。

调试：代码运行调试
测试：调度运行真线数据生成临时表
说明 使用不包含limit限制条件的SELECT语法查询数据时，默认只显示200条查询结果。如果您需要显示更多数据，则可以在SELECT语法后添加limit限制。最多支持显示10000条查询结果。

![](https://sitecdn.zcycdn.com/f2e-assets/0684e80d-2309-4c33-a207-c62ae695d72d.png?x-oss-process=image/quality,Q_75/format,jpg)配置属性。单击节点编辑区域上方操作栏的配置，配置节点的调度属性和依赖关系。
| 参数 | 描述 |
| --- | --- |
| 空跑调度 | 勾选后任务默认成功。 |
| DAG | 选择任务所属DAG。 |
| 队列 |  |
| 超时时间 | 当任务的运行时长超过超时时间时,任务自动终止运行。 |
| 超时策略 | 超时告警|
| 超时失败 | |
| 重跑属性 | 当前任务的重跑规则。 |
| 报警等级 |  |
| 驱动器内存 | 选择当前任务运行时需要的驱动器内存，默认4G。 |
| 执行器内存 | 选择当前任务运行时需要的执行器内存，默认4G。 |
| 任务期望最大并发数 | 数据同步任务内，可以从源并行读取或并行写入数据存储端的最大线程数。 |
| 优先等级 | 选择任务调度时的优先级。 |

![](https://sitecdn.zcycdn.com/f2e-assets/3b3b3925-b50e-4450-b2bf-765e9b5abab2.png?x-oss-process=image/quality,Q_75/format,jpg)

| 参数 | 描述 |
| --- | --- |
| 依赖的上游任务 | 选择任务所依赖的上游任务。仅能选中已创建的任务。|
| 数据写入模式 | 选择任务的数据写入模式，可选择overwrite和upsert。 |
| 本任务的输出 | 选择任务输出的数据源类型，并输入输出表的表名。 |



5. 点击工具栏中的保存图表，保存任务配置。需要设置节点的依赖关系才可以提交节点。
   ![](https://sitecdn.zcycdn.com/f2e-assets/d47e8f22-b955-4e99-a055-765fbeb0a569.png?x-oss-process=image/quality,Q_75/format,jpg)提交并发布任务。单击工具栏中的提交图标。在提交新版本对话框中，选择提交的环境并输入变更说明。




| 参数 | 描述 |
| --- | --- |
| 提交环境 | 选择任务的发布环境。 |
| 变更说明 | 对任务进行简单描述，不得超过80个字符。 |

6. 单击确认。点击数据开发**>**任务列表进行任务的发布，详情请参见任务列表。
### 5.5 任务列表
提交的任务会默认添加至待发布列表，该页面为您展示已提交的新增、更新任务等操作。
## 6 运维管理
### 6.1 运维看板
数据概览展示作业调度（Dolphin Scheduler）调度情况，资源调度（YARN）调度情况


![](https://sitecdn.zcycdn.com/f2e-assets/2c40653a-2172-4cd6-b307-602fe0075172.png?x-oss-process=image/quality,Q_75/format,jpg)
![](https://sitecdn.zcycdn.com/f2e-assets/e7d0776c-49f5-426a-8c91-bf775dc3b21d.png?x-oss-process=image/quality,Q_75/format,jpg)
![](https://sitecdn.zcycdn.com/f2e-assets/269a41d0-b73f-4bfe-800e-e1944221a973.png?x-oss-process=image/quality,Q_75/format,jpg)


### 6.2 任务监控
![](https://sitecdn.zcycdn.com/f2e-assets/84088c19-8c63-44b9-9ca2-bfae3a206371.png?x-oss-process=image/quality,Q_75/format,jpg)
该模块目前主要用来查询悬垂作业。
### 6.3 作业历史
作业历史列表查询：
![](https://sitecdn.zcycdn.com/f2e-assets/bc6f5b57-4f4a-4533-accf-c59ddf957df3.png?x-oss-process=image/quality,Q_75/format,jpg)


相比列表，通过甘特图能更清晰的了解作业执行情况：
![](https://sitecdn.zcycdn.com/f2e-assets/9d8ef471-83b6-499a-8e37-fad8dd721dbd.png?x-oss-process=image/quality,Q_75/format,jpg)
### 6.4 集群管理


![](https://sitecdn.zcycdn.com/f2e-assets/f745fd0b-2ecb-4c80-bb1e-2d439fa8429a.png?x-oss-process=image/quality,Q_75/format,jpg)
该模块提供实时查询集群中作业运行情况的能力。
