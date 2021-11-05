export default [
  {
    path: '/login',
    layout: false,
    component: './Login',
  },
  {
    path: '/',
    redirect: '/datapi',
  },
  {
    path: '/configuration',
    name: '系统配置',
    iconActive: 'https://sitecdn.zcycdn.com/f2e-assets/c6ab0611-57cd-4e14-a68a-4384c198ed65.svg',
    iconDefault: 'https://sitecdn.zcycdn.com/f2e-assets/ccfa9e9b-0fa7-4f19-8e2b-e81b207b53b9.svg',
    routes: [
      {
        path: '/configuration',
        redirect: '/configuration/role',
      },
      {
        path: '/configuration/role',
        name: '角色管理',
        routes: [
          {
            path: '/configuration/role',
            redirect: '/configuration/role/list',
          },
          {
            path: '/configuration/role/list',
            name: '角色列表',
            hideInMenu: true,
            component: './authority/role/List',
          },
          {
            path: '/configuration/role/create',
            name: '新增角色',
            hideInMenu: true,
            component: './authority/role/Create',
          },
          {
            path: '/configuration/role/edit/:id/:name',
            name: '编辑角色',
            hideInMenu: true,
            component: './authority/role/Edit',
          },
        ],
      },
      {
        path: '/configuration/user',
        name: '用户管理',
        routes: [
          {
            path: '/configuration/user',
            redirect: '/authority/user/list',
          },
          {
            path: '/configuration/user/list',
            name: '用户列表',
            hideInMenu: true,
            component: './authority/user/List',
          },
        ],
      },
      {
        path: '/configuration/integrated',
        name: '集成配置',
        component: './Integrated',
      },
    ],
  },
  {
    component: './404',
  },
];
