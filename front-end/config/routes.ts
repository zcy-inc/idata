export default [
  {
    path: '/login',
    layout: false,
    component: './Login',
  },
  { path: '/', redirect: '/datapi' },
  // { name: '组件', path: '/components', component: './Components' },
  {
    path: '/datapi',
    name: '数据研发',
    iconActive: 'https://sitecdn.zcycdn.com/f2e-assets/6c565c17-cb18-4678-8049-0d7d6f387877.svg',
    iconDefault: 'https://sitecdn.zcycdn.com/f2e-assets/c5e01666-c8e9-48dd-89a0-42d950117528.svg',
    routes: [
      { path: '/datapi', redirect: '/datapi/tablemanage' },
      { path: '/datapi/tablemanage', name: '数仓设计', component: './datapi/TableManage' },
      { path: '/datapi/measure', name: '指标库', component: './datapi/Measure' },
    ],
  },
  {
    path: '/objectLabel',
    name: '数据标签',
    component: './ObjectLabel',
    iconActive: 'https://sitecdn.zcycdn.com/f2e-assets/96f6bf29-38e9-42a9-af58-c5f173bd07c6.svg',
    iconDefault: 'https://sitecdn.zcycdn.com/f2e-assets/c00131f7-0482-4b79-abcd-0aa1952acd3c.svg',
  },
  {
    path: '/authority',
    name: '权限管理',
    iconActive: 'https://sitecdn.zcycdn.com/f2e-assets/c6ab0611-57cd-4e14-a68a-4384c198ed65.svg',
    iconDefault: 'https://sitecdn.zcycdn.com/f2e-assets/ccfa9e9b-0fa7-4f19-8e2b-e81b207b53b9.svg',
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
