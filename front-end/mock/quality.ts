// import { Request, Response } from 'express';


// // 代码中会兼容本地 service mock 以及部署站点的静态数据
// export default {
//   // 支持值为 Object 和 Array
//   'POST /api/dqc/monitorTable/getByPage': (req: Request, res: Response) => {
//     res.send({
//       "success":true,
//       "code":"200",
//       "msg":"",
//       "data":{
//         "data":[
//           {
//             "id":1,
//             "tableId":116,
//             "latestAlarmLevel": 2,
//             "comment":"业务实例维表",
//             "tableName":"dim_instance",
//             "ruleCount":1,
//             "accessTime":"2021-12-24 14:21:26"
//           },
//           {
//             "id":2,
//             "tableId":28,
//             "latestAlarmLevel": 1,
//             "comment":"采购单位维表",
//             "tableName":"dim_purchaser_org",
//             "ruleCount":13,
//             "accessTime":"2021-12-24 11:29:19"
//           },
//           {
//             "id":3,
//             "tableId":5,
//             "latestAlarmLevel": 3,
//             "comment":"财政部报表商品明细表",
//             "tableName":"ads_finance_rpt_contract_item",
//             "ruleCount":1,
//             "accessTime":"2021-06-02 09:40:22"
//           }
//         ],
//         "totalElements":3798,
//         "pageNum":1,
//         "pageSize":10,
//         "pages":380
//       }
//     });
//   },
//   'POST /api/dqc/monitorTable/add': (req: Request, res: Response) => { 
//     res.send({
//       "success":true,
//       "data":{
//           "id":3,
//           "creator":"元宿",
//           "editor":"元宿",
//           "baselineId":-1,
//           "tableName":"dwd.dw_search_query_item_click_di",
//           "partitionExpr":"pt=#{yyyyMMdd}",
//           "ruleCount":0,
//           "accessTime":""
//       },
//       "message":"错误信息"
//     })
//   },
//   'GET /api/dqc/monitorTable/del/:id/:isBaseline': (req: Request, res: Response) => { 
//     res.send({
//       "success":true,
//     })
//   },
//   'GET /api/dqc/getTables': (req: Request, res: Response) => { 
//     res.send({
//       "success":true,
//       "code":"200",
//       "msg":"",
//       "data":[
//         {
//             "tableName":"ods.ods_db_paas_user_paas_purchaser",
//             comment:'中文表名',
//             id: 1,
//             "partitioned": '{YYYY}-{MM}'   //true是分区，false为非分区
//         } 
//     ]
//     })
//   },
//   'GET /api/dqc/monitorTable/get/:id': (req: Request, res: Response) => { 
//     res.send({
//       "success":true,
//       "code":"200",
//       "msg":"",
//       "data":{
//         "id": 1,
//         "baselineId": -1,
//         "tableName": "dwd.dw_search_query_item_click_di",
//         "partitionExpr": "pt=#{yyyyMMdd}",
//         "accessTime": ""
//       }
//     })
//   },
//   'POST /api/dqc/monitorTable/update': (req: Request, res: Response) => { 
//     res.send({
//       "success":true,
//       "code":"200",
//       "msg":"",
//     })
//   },
//   'POST /api/dqc/monitorRule/getByPage': (req: Request, res: Response) => { 
//     res.send({
//       "success":true,
//       "code":"200",
//       "msg":"",
//       "data": {
//         "data":[
//           {
//             "id":1,
//             "tableName":"dwd.dw_search_query_item_click_di",
//             "comment":"中文表名",
//             "name":"表产出时间",
//             "ruleType":"system",
//             "monitorObj":"table",
//             "alarmLevel":1,
//             "alarmReceivers":"元宿",
//             "status":1,
//             "editor":"元宿",
//             "editTime":"2022-07-01 15:28:11.000"
//           }
//         ],
//         "totalElements":0,
//         "pageSize":5,
//         "curPage":1,
//         "totalPages":0
//       }
//     })
//   },
//   'POST /api/dqc/monitorRule/add': (req: Request, res: Response) => { 
//     res.send({
//       "success":true,
//       "code":"200",
//       "msg":"",
//       "data":{
//         "id":8,
//         "type":0,
//         "tableName":"dwd.dw_search_query_item_click_di",
//         "name":"自定义sql上升范围",
//         "ruleType":"system",
//         "templateId":-1,
//         "monitorObj":"field",
//         "alarmLevel":1,
//         "alarmReceivers":"元宿",
//         "content":"select 1",
//         "rangeStart":5,
//         "rangeEnd":10
//       }
//     })
//   },
//   'POST /api/dqc/monitorRule/update': (req: Request, res: Response) => { 
//     res.send({
//       "success":true,
//       "code":"200",
//       "msg":"",
//       "data":{
//         "id":8,
//         "type":0,
//         "tableName":"dwd.dw_search_query_item_click_di",
//         "name":"自定义sql上升范围",
//         "ruleType":"system",
//         "templateId":-1,
//         "monitorObj":"field",
//         "alarmLevel":1,
//         "alarmReceivers":"元宿",
//         "content":"select 1",
//         "rangeStart":5,
//         "rangeEnd":10
//       }
//     })
//   },
//   'GET /api/dqc/monitorRule/setStatus/:id/:status': (req: Request, res: Response) => { 
//     res.send({
//       "success":true,
//       "code":"200",
//       "msg":"",
//       "data":{
//         "id":8,
//         "type":0,
//         "tableName":"dwd.dw_search_query_item_click_di",
//         "name":"自定义sql上升范围",
//         "ruleType":"system",
//         "templateId":-1,
//         "monitorObj":"field",
//         "alarmLevel":1,
//         "alarmReceivers":"元宿",
//         "content":"select 1",
//         "rangeStart":5,
//         "rangeEnd":10
//       }
//     })
//   },
//   'GET /api/dqc/monitorRule/del/:id': (req: Request, res: Response) => { 
//     res.send({
//       "success":true,
//       "code":"200",
//       "msg":"",
//       "data":{
//         "id":8,
//         "type":0,
//         "tableName":"dwd.dw_search_query_item_click_di",
//         "name":"自定义sql上升范围",
//         "ruleType":"system",
//         "templateId":-1,
//         "monitorObj":"field",
//         "alarmLevel":1,
//         "alarmReceivers":"元宿",
//         "content":"select 1",
//         "rangeStart":5,
//         "rangeEnd":10
//       }
//     })
//   },
//   'GET /api/dqc/monitorRule/tryRun/:id': (req: Request, res: Response) => { 
//     res.send({
//       "success":true,
//       "code":"200",
//       "msg":"",
//       "data":[{
//         "tableName": "tableName",
//         "comment":"zhongwen",
//         "ruleName":"规则名称",
//         "ruleType": 1,
//         "fixValue": 1,//规则固定值
//         "rangeStart": 1, //规则开始值
//         "rangeEnd": 2, //规则结束值
//         "alarm":1,//是否告警1告警0未告警
//         "alarmLevel": 1,//是否告警1告警0未告警
//         "createTime": "2022-07-15 13:57:29",
//         "dataValue":2 //告警结果值
//       }]
//     })
//   },
//   'GET /api/dqc/table/getOwners': (req: Request, res: Response) => { 
//     res.send({
//       "success":true,
//       "code":"200",
//       "msg":"",
//       "data": ["张三"]
//     })
//   },
//   'GET /api/dqc/hive/table/getColumns': (req: Request, res: Response) => { 
//     res.send({
//       "success":true,
//       "code":"200",
//       "msg":"",
//       "data":[{
//             "name":"query"
//         }]
//     })
//   },
//   'GET /api/dqc/monitorHistory/get': (req: Request, res: Response) => { 
//     res.send({
//       "success":true,
//       "code":"200",
//       "msg":"",
//       "data":[{
//         "tableName": "tableName",
//         "comment":"zhongwen",
//         "ruleName":"规则名称",
//         "ruleType": 1,
//         "fixValue": 1,//规则固定值
//         "rangeStart": 1, //规则开始值
//         "rangeEnd": 2, //规则结束值
//         "alarm":1,//是否告警1告警0未告警
//         "alarmLevel": 1,//是否告警1告警0未告警
//         "createTime": "2022-07-15 13:57:29",
//         "dataValue":2 //告警结果值
//       }]
//     })
//   },
//   'GET /api/dqc/monitorRule/get/:id': (req: Request, res: Response) => { 
//     res.send({
//       "success":true,
//       "code":"200",
//       "msg":"",
//       "data":{
//         "id":8,
//         "type":0,
//         "tableName":"dwd.dw_search_query_item_click_di",
//         "name":"自定义sql上升范围",
//         "ruleType":"system",
//         "templateId":-1,
//         "monitorObj":"field",
//         "alarmLevel":1,
//         "alarmReceivers":"元宿",
//         "content":"select 1",
//         "rangeStart":5,
//         "rangeEnd":10
//       }
//     })
//   },
//   'POST /api/dqc/monitorTemplate/getByPage': (req: Request, res: Response) => { 
//     res.send({
//       "success":true,
//       "code":"200",
//       "msg":"",
//       "data":	{
//         "data": [
//         {
//           "id": 1,
//           "del": 0,
//           "creator": "系统管理员",
//           "createTime": "2022-06-30 20:19:17.000",
//           "editor": "系统管理员",
//           "editTime": "2022-07-04 10:28:24.000",
//           "type": "system",
//           "monitorObj": "table",
//           "status": 1,
//           "name": "表行数",
//           "content": "table_row",
//           "category": "integrity",
//           "outputType": 1
//       },
//       {
//         "id": 2,
//         "del": 0,
//         "creator": "系统管理员2",
//         "createTime": "2022-06-10 20:19:17.000",
//         "editor": "系统管理员2",
//         "editTime": "2022-09-04 10:28:24.000",
//         "type": "template",
//         "monitorObj": "field",
//         "status": 1,
//         "name": "表产出时间",
//         "content": "table_output_time",
//         "category": "integrity",
//         "outputType": 1
//       },
//       {
//         "id": 3,
//         "del": 0,
//         "creator": "系统管理员2",
//         "createTime": "2022-06-10 20:19:17.000",
//         "editor": "系统管理员2",
//         "editTime": "2022-09-04 10:28:24.000",
//         "type": "template",
//         "monitorObj": "field",
//         "status": 1,
//         "name": "值唯一",
//         "content": "field_unique",
//         "category": "integrity",
//         "outputType": 1
//       },
//       {
//         "id": 4,
//         "del": 0,
//         "creator": "系统管理员2",
//         "createTime": "2022-06-10 20:19:17.000",
//         "editor": "系统管理员2",
//         "editTime": "2022-09-04 10:28:24.000",
//         "type": "template",
//         "monitorObj": "field",
//         "status": 1,
//         "content": "field_enum_content",
//         "name": "字段枚举内容",
//         "category": "integrity",
//         "outputType": 1
//       },
//       {
//         "id": 5,
//         "del": 0,
//         "creator": "系统管理员2",
//         "createTime": "2022-06-10 20:19:17.000",
//         "editor": "系统管理员2",
//         "editTime": "2022-09-04 10:28:24.000",
//         "type": "template",
//         "monitorObj": "field",
//         "status": 1,
//         "name": "字段枚举数量",
//         "content": "field_enum_count",
//         "category": "integrity",
//         "outputType": 1
//       },
//       {
//         "id": 6,
//         "del": 0,
//         "creator": "系统管理员2",
//         "createTime": "2022-06-10 20:19:17.000",
//         "editor": "系统管理员2",
//         "editTime": "2022-09-04 10:28:24.000",
//         "type": "template",
//         "monitorObj": "field",
//         "status": 1,
//         "name": "字段数值范围",
//         "content": "field_data_range",
//         "category": "integrity",
//         "outputType": 1
//       },
//       {
//         "id": 7,
//         "del": 0,
//         "creator": "系统管理员2",
//         "createTime": "2022-06-10 20:19:17.000",
//         "editor": "系统管理员2",
//         "editTime": "2022-09-04 10:28:24.000",
//         "type": "template",
//         "monitorObj": "field",
//         "status": 1,
//         "name": "字段值不为空",
//         "content": "field_not_null",
//         "category": "integrity",
//         "outputType": 1
//       }
//   ],
//         "totalElements": 0,
//         "pageSize": 5,
//         "curPage": 1,
//         "totalPages": 0
//       }
//     })
//   },
//   'POST /api/dqc/monitorTemplate/add': (req: Request, res: Response) => { 
//     res.send({
//       "success":true,
//       "code":"200",
//       "msg":"",
//       "data": {
//         "id": 9,
//         "type": "custom",
//         "monitorObj": "table",
//         "content": "20:00",
//         "name": "表产出时间",
//         "category": "accuracy",
//         "outputType": 1
//         }
//     })
//   },
//   'POST /api/dqc/monitorTemplate/update': (req: Request, res: Response) => { 
//     res.send({
//       "success":true,
//       "code":"200",
//       "msg":"",
//       "data": {
//         "id": 9,
//         "type": "custom",
//         "monitorObj": "table",
//         "content": "20:00",
//         "name": "表产出时间",
//         "category": "accuracy",
//         "outputType": 1
//         }
//     })
//   },
//   'GET /api/dqc/monitorTemplate/get/:id': (req: Request, res: Response) => { 
//     res.send({
//       "success":true,
//       "code":"200",
//       "msg":"",
//       "data": {
//         "id": 9,
//         "del": 0,
//         "creator": "元宿",
//         "createTime": "2022-07-05 15:56:34.000",
//         "editor": "元宿",
//         "editTime": "2022-07-05 16:03:43.000",
//         "type": "template",
//         "monitorObj": "table",
//         "content": "20:00",
//         "status": 0,
//         "name": "表产出时间",
//         "category": "accuracy",
//         "outputType": 1
//       }
//     })
//   },
//   'GET /api/dqc/monitorTemplate/del/:id': (req: Request, res: Response) => { 
//     res.send({
//       "success":true,
//       "code":"200",
//       "msg":""
//     })
//   },
//   'GET /api/dqc/monitorTemplate/setStatus/:id/:status': (req: Request, res: Response) => { 
//     res.send({
//       "success":true,
//       "code":"200",
//       "msg":""
//     })
//   },
//   'POST /api/dqc/monitorBaseline/getByPage': (req: Request, res: Response) => { 
//     res.send({
//       "success":true,
//       "code":"200",
//       "msg":"",
//       "data":	{
//         "data": [
//         {
//         "id": 1,
//         "name": "基线名称",
//         "status": 1,
//         "del": 0,
//         "creator": "元宿",
//         "createTime": "2022-07-06 15:45:15.000",
//         "editor": "元宿",
//         "editTime": "2022-07-06 17:49:35.000",
//         "ruleCount": 0,
//         "tableCount": 0
//         }
//         ],
//         "totalElements": 0,
//         "pageSize": 5,
//         "curPage": 1,
//         "totalPages": 0
//       }
//     })
//   },
//   'POST /api/dqc/monitorBaseline/add': (req: Request, res: Response) => { 
//     res.send({
//       "success":true,
//       "code":"200",
//       "msg":"",
//       "data": {
//         "id": 9,
//         "type": "custom",
//         "monitorObj": "table",
//         "content": "20:00",
//         "name": "表产出时间",
//         "category": "accuracy",
//         "outputType": 1
//         }
//     })
//   },
//   'POST /api/dqc/monitorBaseline/update': (req: Request, res: Response) => { 
//     res.send({
//       "success":true,
//       "code":"200",
//       "msg":"",
//       "data": {
//         "id": 9,
//         "type": "custom",
//         "monitorObj": "table",
//         "content": "20:00",
//         "name": "表产出时间",
//         "category": "accuracy",
//         "outputType": 1
//         }
//     })
//   },
//   'GET /api/dqc/monitorBaseline/get/:id': (req: Request, res: Response) => { 
//     res.send({
//       "success":true,
//       "code":"200",
//       "msg":"",
//       "data": {
//         "id": 1,
//         "name": "基线名称",
//         "status": 1,
//         "del": 0,
//         "creator": "元宿",
//         "createTime": "2022-07-06 15:45:15",
//         "editor": "元宿",
//         "editTime": "2022-07-06 15:46:38"
//       }
//     })
//   },
//   'GET /api/dqc/monitorBaseline/del/:id': (req: Request, res: Response) => { 
//     res.send({
//       "success":true,
//       "code":"200",
//       "msg":""
//     })
//   },
//   'GET /api/dqc/monitorBaseline/setStatus/:id/:status': (req: Request, res: Response) => { 
//     res.send({
//       "success":true,
//       "code":"200",
//       "msg":""
//     })
//   },
//   'GET /api/dqc/user/getList': (req: Request, res: Response) => { 
//     res.send({
//       "success":true,
//       "code":"200",
//       "data": [
//         {
//         "nickname": "张三"
//         },
//         {
//           "nickname": "李四"
//           }
//         ],
//       "msg":""
//     })
//   },
//   'GET /api/dqc/tables/get/:id': (req: Request, res: Response) => { 
//     res.send({
//       "success":true,
//       "code":"200",
//       "data": [
//         {
//           "tableName": "yingwen",
//           "comment": "中文",
//           "partitionExpr": "“分区表达式”",
//           id: 1
//         }],
//       "msg":""
//     })
//   },
// };
