# zcy-idata-pro-front

> 政采云数据中台前端。typescript + react hooks + ant design pro v5

## 环境准备

IDE: 因为我们针对 vscode 定制了一些代码片段，所以统一使用 vscode

安装依赖:

```bash
npm install
```

添加本地代理配置: 根目录 config 文件夹下新建 user.local.json 文件，添加内容,填入对应的域账号和密码，该文件不会提交到 git 仓库，所以不必担心自己的密码泄露

```json
{
  "user": {
    "username": "",
    "password": ""
  },
  "url": "http://idata-staging.cai-inc.com/api/v1/idata/auth/login"
}
```

## 脚本命令

### 启动本地服务

```bash
npm start # 使用mock数据
npm run dev # 不使用mock数据
```

### 打包项目

```bash
npm run build
```

### 检查代码风格

```bash
npm run lint
```

```bash
npm run lint:fix
```

```bash
npm run lint:style # 修复样式
```

## 目录结构

. ├── Dockerfile // 用于 zpaas 构建 ├── README.md ├── config │   ├── config.ts // umi 配置 │   ├── defaultSettings.ts │   ├── proxy.ts // 代理配置 │   ├── routes.ts // 路由配置 │   └── user.local.json // 用户信息 ├── mock // 本地 mock 数据 ├── package-lock.json ├── package.json ├── public ├── src │   ├── access.ts │   ├── app.tsx │   ├── components // 通用组件 │   ├── constants // 常量 │   ├── global.less // 全局样式 │   ├── global.tsx │   ├── hooks // 通用 hooks │   ├── interfaces // 接口 │   ├── layouts │   │   └── CustomLayout // 自定义 layout │   ├── manifest.json │   ├── pages │   │   ├── 404.tsx // 404 页面 │   │   ├── Admin.tsx │   │   ├── DataQuality // 数据质量 │   │   └── document.ejs // html 模板 │   ├── service-worker.js │   ├── services │   ├── styles │   │   └── reset.less │   ├── types // 全局类型声明 │   │   └── typings.d.ts │   └── utils // 工具方法 └── tsconfig.json // ts 配置文件

## 开发示例

### 列表页

假设一级菜单宠物管理模块下有一个子目录狗管理，宠物管理->狗管理，我们现在要添加一个狗管理列表页

1. 路由设置：路由设置统一在项目根目录下 config/routes.ts 文件中,如果宠物管理模块已存在，则在宠物管理模块路由下添加新的路由规则。现在我们假设宠物管理是一个新的模块，我们再配置中新增如下配置

   ```typescript
   {
       path: '/pet',
       name: '宠物管理',
       icon: 'icon-shujuzhiliangtubiao', // icon必须已“icon-”开头，需要新增时在iconfont库中增加后修改config/defaultSettings.ts文件中的iconfontUrl项
       routes: [
         { path: '/pet', redirect: '/pet/dog/list' }, // 重定向的配置必须要添加，否则会导致点击面包屑不能达到预期效果
         {
           path: '/pet/dog',
           name: '狗管理',
           routes: [
             { path: '/pet/dog', redirect: '/pet/dog/list' }, // 重定向的配置必须要添加
             {
               path: '/pet/dog/list',
               name: '狗列表',
               hideInMenu: true, // 在列表中隐藏
               component: './Pet/Dog/List',
             },
           ],
         },
       ],
     },
   ```

1. 写好该列表接口返回数据对应的 interface。在 interfaces 文件夹下创建一个 pet.d.ts

   ```typescript
   // interfaces/pet.d.ts
   export interface Tdog {
     name: string;
     age: number;
     id: number;
   }
   ```

1. 编写对应的请求，在 services 目录下新建 pet.ts

   ```typescript
   import { request } from 'umi';
   import type { Tdog } from '@/interfaces/pet'; // 我们的eslint规则要求我们如果引入的内容在该模块中仅在类型上下文中使用，要使用import type 来引入

   export async function getDogs(
     params?: PaginatedParams<{
       // PaginatedParams 是约定的分页接口的参数格式，该类型声明在全局，所以不需要我们手动来引入
       name?: string;
       age?: number;
     }>,
   ) {
     return request<{ data: PaginatedData<Tdog> }>('/api/v1/idata/dogs', {
       // PaginatedData是约定好的分页接口的返回数据格式，和上面的PaginatedParams一样，不需要引入就能使用
       params,
     });
   }
   ```

1. 编写列表页面, pages 目录下新建 Pet/Dog/List.tsx 文件

   ```typescript
   import React, { useState } from 'react';
   import { Card, Input, Button } from 'antd';
   import { SearchPanel, TiledTable, Operation } from '@/components';
   import { operationTypeEnum } from '@/components/Operation';
   import { usePaginated } from '@/hooks';
   import type { ColumnsType } from 'antd/es/table'; // Table组件columns属性的类型
   import { getDogs, deleteDog } from '@/services/pet';
   import type { Tdog } from '@/interfaces/pet';
   import { getDeleteFn } from '@/utils/utils';

   const DogList: React.FC = () => {
     const [extraParams, setExtraParams] = useState<Record<string, unknown> | undefined>({});
     /**
      * tableProps 直接传递给antd的Table组件
      * refresh 可用来刷新列表
      */
     const { tableProps, refresh } = usePaginated(
       (params) => getDogs({ ...params, ...extraParams }),
       { refreshDeps: [extraParams] }, // refreshDeps: 当该数组中的变量发生变化时会重新请求
     );

     const onReset = () => setExtraParams({});
     const onAdd = () => {};
     const onExport = () => {};
     const onEidt = (id: number) => {
       console.log(id);
     };
     const onDelete = getDeleteFn(deleteDog, refresh); // 删除统一使用getDeleteFn工具方法

     // 定义Table组件的columns属性
     const columns: ColumnsType<Tdog> = [
       {
         title: '名字',
         dataIndex: 'name',
       },
       {
         title: '年龄',
         dataIndex: 'age',
       },
       {
         title: '操作',
         render: (_, row) => (
           <Operation.Group>
             {' '}
             // 表格中的操作，统一使用Operation组件
             <Operation label="编辑" onClick={() => onEidt(row.id)} />
             <Operation label="查看" to={`/profile/${row.id}`} />
             <Operation
               label="删除"
               type={operationTypeEnum.DANGER}
               onClick={() => onDelete(row.id)}
             />
           </Operation.Group>
         ),
       },
     ];
     return (
       <Card bordered={false}>
         <SearchPanel // 普通场景下直接传递options即可满足需求，当搜索组件存在联动时可使用jsx风格的使用方法
           options={[
             { label: '名称', name: 'name', component: <Input /> },
             { label: '年龄', name: 'age', component: <Input /> },
           ]}
           templateColumns="280px 280px"
           onSearch={setExtraParams}
           onReset={onReset}
         />
         <TiledTable
           rowKey="id"
           leftBtns={<Button onClick={onAdd}>添加</Button>}
           rightBtns={<Button onClick={onExport}>导出</Button>}
           columns={columns}
           {...tableProps}
         />
       </Card>
     );
   };

   export default DogList;
   ```

### 详情页

1. 详情页需要特别注意的是，我们预定了一个详情页的路由格式，即统一将该条记录的 id 左右 param 放在路径末尾

   ```typescript
     {
       path: '/pet/dog/profile/:id',
       name: '狗详情',
       hideInMenu: true,
       component: './Pet/Dog/Profile',
     },
   ```

2. 编写详情页代码

   ```typescript
   import React from 'react';
   import { Card, Descriptions } from 'antd';
   import { useProfile } from '@/hooks';
   import { getDogById } from '@/services/pet';

   const DogProfile = () => {
     const { data: record } = useProfile(getDogById);
     return (
       <Card bordered={false}>
         <Descriptions title="基本信息">
           <Descriptions.Item label="狗名">{record?.name}</Descriptions.Item>
           <Descriptions.Item label="狗龄">{record?.age}</Descriptions.Item>
         </Descriptions>
       </Card>
     );
   };

   export default DogProfile;
   ```

### 模态框

1. 这里只给出一个简单使用模态框的样例，更多用法看 antd 官方文档。以下为添加了模态框的列表页面

   ```typescript
   import React, { useState } from 'react';
   import { Card, Input, Button, Modal, Form } from 'antd';
   import { SearchPanel, TiledTable, Operation } from '@/components';
   import { operationTypeEnum } from '@/components/Operation';
   import { usePaginated } from '@/hooks';
   import type { ColumnsType } from 'antd/es/table'; // Table组件columns属性的类型
   import { getDogs, deleteDog, saveDog } from '@/services/pet';
   import type { Tdog } from '@/interfaces/pet';
   import { getDeleteFn, saveFn } from '@/utils/utils';

   const DogList: React.FC = () => {
     const [extraParams, setExtraParams] = useState<Record<string, unknown> | undefined>({});
     const [modalVisible, setModalVisible] = useState(false);
     const [form] = Form.useForm();
     /**
      * tableProps 直接传递给antd的Table组件
      * refresh 可用来刷新列表
      */
     const { tableProps, refresh } = usePaginated(
       (params) => getDogs({ ...params, ...extraParams }),
       { refreshDeps: [extraParams] }, // refreshDeps: 当该数组中的变量发生变化时会重新请求
     );

     const onReset = () => setExtraParams({});
     const onAdd = () => setModalVisible(true);
     const onSave = async () => {
       const values = form.getFieldsValue();
       await saveFn(() => saveDog(values));
       refresh(); // 成功保存后刷新列表
     };
     const onExport = () => {};
     const onEidt = (id: number) => {
       console.log(id);
     };
     const onDelete = getDeleteFn(deleteDog, refresh); // 删除统一使用getDeleteFn工具方法

     // 定义Table组件的columns属性
     const columns: ColumnsType<Tdog> = [
       {
         title: '名字',
         dataIndex: 'name',
       },
       {
         title: '年龄',
         dataIndex: 'age',
       },
       {
         title: '操作',
         render: (_, row) => (
           <Operation.Group>
             // 表格中的操作，统一使用Operation组件
             <Operation label="编辑" onClick={() => onEidt(row.id)} />
             <Operation label="查看" to={`/profile/${row.id}`} />
             <Operation
               label="删除"
               type={operationTypeEnum.DANGER}
               onClick={() => onDelete(row.id)}
             />
           </Operation.Group>
         ),
       },
     ];
     return (
       <Card bordered={false}>
         <SearchPanel // 普通场景下直接传递options即可满足需求，当搜索组件存在联动时可使用jsx风格的使用方法
           options={[
             { label: '名称', name: 'name', component: <Input /> },
             { label: '年龄', name: 'age', component: <Input /> },
           ]}
           templateColumns="280px 280px"
           onSearch={setExtraParams}
           onReset={onReset}
         />
         <TiledTable
           rowKey="id"
           leftBtns={<Button onClick={onAdd}>添加</Button>}
           rightBtns={<Button onClick={onExport}>导出</Button>}
           columns={columns}
           {...tableProps}
         />
         <Modal
           title="添加狗子"
           visible={modalVisible}
           onCancel={() => setModalVisible(false)}
           onOk={onSave}
           width={500}
           bodyStyle={{ paddingTop: 48 }}
           destroyOnClose
         >
           <Form form={form} preserve={false} labelCol={{ span: 6 }} wrapperCol={{ span: 18 }}>
             <Form.Item label="狗名" name="name" rules={[{ required: true }]}>
               <Input placeholder="请输入（必填）" style={{ width: 300 }} />
             </Form.Item>
             <Form.Item label="狗龄" name="age" rules={[{ required: true }]}>
               <Input placeholder="请输入（必填）" style={{ width: 300 }} />
             </Form.Item>
           </Form>
         </Modal>
       </Card>
     );
   };

   export default DogList;
   ```

## 代码片段

1. 目前有列表页、详情的代码片段
2. 使用方式: 新建一个文件然后输入 clp(create-list-page)或 cpp(create-profile-page)

## TODO

1. 文档搭建
