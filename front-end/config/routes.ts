export default [
  {
    path: '/login',
    layout: false,
    component: './Login',
  },
  { path: '/', redirect: '/authority' },
  {
    path: '/datapi',
    name: '数据研发',
    icon: 'icon-quanxianguanli',
    routes: [
      { path: '/datapi', redirect: '/datapi/tablemanage' },
      { path: '/datapi/tablemanage', name: '数仓设计', component: './datapi/tablemanage' },
      { path: '/datapi/kpisystem', name: '指标库', component: './datapi/kpisystem' },
    ],
  },
  {
    path: '/authority',
    name: '权限管理',
    icon: 'icon-quanxianguanli',
    routes: [
      { path: '/authority', redirect: '/authority/role' },
      {
        path: '/authority/role',
        name: '角色管理',
        routes: [
          { path: '/authority/role', redirect: '/authority/role/list' },
          {
            path: '/authority/role/list',
            name: '角色列表',
            hideInMenu: true,
            component: './authority/role/List',
          },
          {
            path: '/authority/role/create',
            name: '新增角色',
            hideInMenu: true,
            component: './authority/role/Create',
          },
          {
            path: '/authority/role/edit/:id/:name',
            name: '编辑角色',
            hideInMenu: true,
            component: './authority/role/Edit',
          },
        ],
      },
      {
        path: '/authority/user',
        name: '用户管理',
        routes: [
          { path: '/authority/user', redirect: '/authority/user/list' },
          {
            path: '/authority/user/list',
            name: '用户列表',
            hideInMenu: true,
            component: './authority/user/List',
          },
        ],
      },
    ],
  },
  {
    component: './404',
  },
];
