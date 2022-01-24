// import { Request, Response } from 'express';

// export default {
//   'GET /api/p0/dev/tableRelations': (req: Request, res: Response) => {
//     res.send({
//       success: true,
//       code: '',
//       msg: '',
//       data: {
//         tables: [
//           {
//             id: '105',
//             dbName: 'ads',
//             tblName: 'test_shiguang2',
//             tblComment: 'test',
//             columnInfos: [{ colComment: 'TEST', colName: 'TEST', colType: 'STRING' }],
//           },
//           {
//             id: '69',
//             dbName: 'hive',
//             tblName: 'dwd.dwd_district_open_application_detail_full_d',
//             tblComment: '区划开通政采云应用事实表',
//             columnInfos: [
//               { colComment: '区划编码', colName: 'district_code', colType: 'STRING' },
//               { colComment: '区划名称', colName: 'district_name', colType: 'STRING' },
//               { colComment: '应用名称', colName: 'application_name', colType: 'STRING' },
//               { colComment: '应用id', colName: 'application_id', colType: 'BIGINT' },
//               { colComment: '应用编码', colName: 'application_code', colType: 'STRING' },
//               { colComment: '应用层级', colName: 'application_level', colType: 'BIGINT' },
//               { colComment: '创建时间', colName: 'gmt_created_time', colType: 'TIMESTAMP' },
//             ],
//           },
//         ],
//         edges: [
//           {
//             from: 'ads.test_shiguang2',
//             fromPort: 'TEST',
//             text: '1',
//             to: 'hive.dwd.dwd_district_open_application_detail_full_d',
//             toPort: 'application_id',
//             toText: 'N',
//           },
//         ],
//       },
//     });
//   },
// };
