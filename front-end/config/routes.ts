export default [
  {
    path: '/login',
    layout: false,
    component: './Login',
  },
  { path: '/', redirect: '/datapi' },
  {
    path: '/datapi',
    name: '数据研发',
    iconActive: 'https://sitecdn.zcycdn.com/f2e-assets/9843593b-a480-44ac-a100-0619ae98abfc.svg',
    iconDefault: 'https://sitecdn.zcycdn.com/f2e-assets/86e1ee76-1dd4-4777-b365-7dee1da134be.svg',
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
    iconActive: 'https://sitecdn.zcycdn.com/f2e-assets/cc456476-c985-46b6-ab4f-7deddba7c7c6.svg',
    iconDefault: 'https://sitecdn.zcycdn.com/f2e-assets/5e17814c-4576-4836-af93-281f6c13973e.svg',
  },
  {
    path: '/authority',
    name: '权限管理',
    iconActive: 'https://sitecdn.zcycdn.com/f2e-assets/fee8a9b7-4a2b-4494-ab96-45ee46829469.svg',
    iconDefault: 'https://sitecdn.zcycdn.com/f2e-assets/a1d2a516-d00e-4201-99ec-a963d0d14c31.svg',
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
