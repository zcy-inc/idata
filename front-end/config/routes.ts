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
        redirect: '/configuration/authority',
      },
      {
        path: '/configuration/authority',
        name: '权限管理',
        component: './authority',
        routes: [
          {
            path: '/configuration/authority',
            redirect: '/configuration/authority/role',
          },
          {
            path: '/configuration/authority/role',
            //name: '角色管理',
            hideInMenu: true,
            routes: [
              {
                path: '/configuration/authority/role',
                redirect: '/configuration/authority/role/list',
              },
              {
                name: '角色管理',
                hideInMenu: true,
                path: '/configuration/authority/role/list',
                component: './authority/role/List',
              },
              {
                path: '/configuration/authority/role/create',
                name: '角色新增',
                hideInMenu: true,
                component: './authority/role/Create',
              },
              {
                path: '/configuration/authority/role/edit/:id/:name',
                name: '角色编辑',
                hideInMenu: true,
                component: './authority/role/Edit',
              },
            ],
          },
          {
            path: '/configuration/authority/user',
            //name: '用户管理',
            hideInMenu: true,
            routes: [
              {
                path: '/configuration/authority/user',
                redirect: '/configuration/authority/user/list',
              },
              {
                path: '/configuration/authority/user/list',
                name: '用户管理',
                hideInMenu: true,
                component: './authority/user/List',
              },
            ],
          },
        ],
      },
      {
        path: '/configuration/integrated',
        name: '集成配置',
        component: './Integrated',
      },
      {
        path: '/configuration/labelController',
        name: '元数据属性配置',
        component: './LabelController',
      },
    ],
  },
  {
    name: '高级详情页',
    icon: 'smile',
    path: '/profileadvanced',
    component: './ProfileAdvanced',
  },
  {
    component: './404',
  },
];
