export default [
  {
    path: '/login',
    layout: false,
    component: './Login',
  },
  {
    path: '/',
    redirect: '/configuration',
  },
  {
    path: '/datapi',
    name: '数据研发',
    iconActive: 'https://sitecdn.zcycdn.com/f2e-assets/6c565c17-cb18-4678-8049-0d7d6f387877.svg',
    iconDefault: 'https://sitecdn.zcycdn.com/f2e-assets/c5e01666-c8e9-48dd-89a0-42d950117528.svg',
    featureCode: 'F_MENU_BIGDATA_RD',
    routes: [
      { path: '/datapi', redirect: '/datapi/datadev' },
      {
        path: '/datapi/datadev',
        name: '数据开发',
        component: './datapi/DataDev',
        featureCode: 'F_MENU_DATA_DEVELOP',
      },
      {
        path: '/datapi/measure',
        name: '指标库',
        component: './datapi/Measures',
        featureCode: 'F_MENU_MEASURE_MANAGE',
      },
      {
        path: '/datapi/datasource',
        name: '数据源管理',
        component: './datapi/DataSource',
        featureCode: 'F_MENU_DATASOURCE_CENTER',
      },
      {
        path: '/datapi/tasks',
        name: '任务列表',
        component: './datapi/Tasks',
        featureCode: 'F_MENU_JOB_LIST',
      },
    ],
  },
  {
    path: '/configuration',
    name: '系统配置',
    iconActive: 'https://sitecdn.zcycdn.com/f2e-assets/c6ab0611-57cd-4e14-a68a-4384c198ed65.svg',
    iconDefault: 'https://sitecdn.zcycdn.com/f2e-assets/ccfa9e9b-0fa7-4f19-8e2b-e81b207b53b9.svg',
    featureCode: 'F_MENU_SYSTEM_CONFIG',
    routes: [
      { path: '/configuration', redirect: '/configuration/authority' },
      {
        path: '/configuration/authority',
        name: '权限管理',
        component: './authority',
        featureCode: 'F_MENU_USER_FEATURE',
        routes: [
          { path: '/configuration/authority', redirect: '/configuration/authority/role' },
          {
            path: '/configuration/authority/role',
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
        featureCode: 'F_MENU_CONFIG_CENTER',
      },
      {
        path: '/configuration/LDAP',
        name: 'LDAP',
        component: './LDAP',
        featureCode: 'F_MENU_LDAP_CONFIG',
      },
      {
        path: '/configuration/labelController',
        name: '元数据属性配置',
        component: './LabelController',
        featureCode: 'F_MENU_METADATA_CONFIG',
      },
    ],
  },
  {
    path: '/operations',
    name: '运维中心',
    iconActive: 'https://sitecdn.zcycdn.com/f2e-assets/96f6bf29-38e9-42a9-af58-c5f173bd07c6.svg',
    iconDefault: 'https://sitecdn.zcycdn.com/f2e-assets/c00131f7-0482-4b79-abcd-0aa1952acd3c.svg',
    featureCode: 'TEST',
    routes: [
      { path: '/operations', redirect: '/operations/dashboard' },
      { path: '/operations/dashboard', name: '运维看板', component: './operations/Dashboard' },
      { path: '/operations/taskHistory', name: '作业历史', component: './operations/TaskHistory' },
      {
        path: '/operations/clusterMonitor',
        name: '集群监控',
        component: './operations/ClusterMonitor',
      },
      { path: '/operations/taskMonitor', name: '任务监控', component: './operations/TaskMonitor' },
    ],
  },
  {
    component: './404',
  },
];
