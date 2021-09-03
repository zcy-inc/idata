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
      { path: '/datapi/tablemanage', name: '数仓设计', component: './datapi/tablemanage' },
      { path: '/datapi/measure', name: '指标库', component: './datapi/measure' },
    ],
  },
  {
    path: '/objectLabel',
    name: '数据标签',
    component: './ObjectLabel',
    iconActive: 'https://sitecdn.zcycdn.com/f2e-assets/f43bf9ae-4be9-49e3-ab11-dfcc77c790be.svg',
    iconDefault: 'https://sitecdn.zcycdn.com/f2e-assets/c3c8a043-e135-4abd-9137-8e16cd8b1097.svg',
  },
  {
    path: '/authority',
    name: '权限管理',
    iconActive: 'https://sitecdn.zcycdn.com/f2e-assets/00a9bf27-b003-42c5-903a-4913fe8d28d7.svg',
    iconDefault: 'https://sitecdn.zcycdn.com/f2e-assets/fd305122-648f-4d7d-a77e-9868b302f761.svg',
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
