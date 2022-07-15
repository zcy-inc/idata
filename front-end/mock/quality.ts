import { Request, Response } from 'express';


// 代码中会兼容本地 service mock 以及部署站点的静态数据
export default {
  // 支持值为 Object 和 Array
  'POST /api/monitorTable/getByPage': (req: Request, res: Response) => {
    res.send({
      "success":true,
      "code":"200",
      "msg":"",
      "data":{
        "data":[
          {
            "id":1,
            "tableId":116,
            "latestAlarmLevel": 2,
            "comment":"业务实例维表",
            "tableName":"dim_instance",
            "ruleCount":1,
            "accessTime":"2021-12-24 14:21:26"
          },
          {
            "id":2,
            "tableId":28,
            "latestAlarmLevel": 1,
            "comment":"采购单位维表",
            "tableName":"dim_purchaser_org",
            "ruleCount":13,
            "accessTime":"2021-12-24 11:29:19"
          },
          {
            "id":3,
            "tableId":5,
            "latestAlarmLevel": 3,
            "comment":"财政部报表商品明细表",
            "tableName":"ads_finance_rpt_contract_item",
            "ruleCount":1,
            "accessTime":"2021-06-02 09:40:22"
          }
        ],
        "totalElements":3798,
        "pageNum":1,
        "pageSize":10,
        "pages":380
      }
    });
  },
  'POST /api/monitorTable/add': (req: Request, res: Response) => { 
    res.send({
      "success":true,
      "data":{
          "id":3,
          "creator":"元宿",
          "editor":"元宿",
          "baselineId":-1,
          "tableName":"dwd.dw_search_query_item_click_di",
          "partitionExpr":"pt=#{yyyyMMdd}",
          "ruleCount":0,
          "accessTime":""
      },
      "message":"错误信息"
    })
  },
  'GET /api/monitorTable/del/:id/:isBaseline': (req: Request, res: Response) => { 
    res.send({
      "success":true,
    })
  },
  'GET /api/getTables': (req: Request, res: Response) => { 
    res.send({
      "success":true,
      "code":"200",
      "msg":"",
      "data":[
        {
            "tableName":"ods.ods_db_paas_user_paas_purchaser",
            "partitioned":false   //true是分区，false为非分区
        } 
    ]
    })
  },
  'GET /api/monitorTable/get/:id': (req: Request, res: Response) => { 
    res.send({
      "success":true,
      "code":"200",
      "msg":"",
      "data":{
        "id": 1,
        "baselineId": -1,
        "tableName": "dwd.dw_search_query_item_click_di",
        "partitionExpr": "pt=#{yyyyMMdd}",
        "accessTime": ""
      }
    })
  },
  'POST /api/monitorTable/update': (req: Request, res: Response) => { 
    res.send({
      "success":true,
      "code":"200",
      "msg":"",
    })
  },
  'POST /api/monitorRule/getByPage': (req: Request, res: Response) => { 
    res.send({
      "success":true,
      "code":"200",
      "msg":"",
      "data": {
        "data":[
          {
            "id":1,
            "tableName":"dwd.dw_search_query_item_click_di",
            "comment":"中文表名",
            "name":"表产出时间",
            "ruleType":"system",
            "monitorObj":"table",
            "alarmLevel":1,
            "alarmReceivers":"元宿",
            "status":1,
            "editor":"元宿",
            "editTime":"2022-07-01 15:28:11.000"
          }
        ],
        "totalElements":0,
        "pageSize":5,
        "curPage":1,
        "totalPages":0
      }
    })
  }
};
