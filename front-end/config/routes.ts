export default [
  {
    path: '/login',
    layout: false,
    component: './Login',
  },
  {
    path: '/',
    redirect: '/operations/dashboard',
  },
  {
    path: '/noAuthority',
    component: './NoAuthority',
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
        path: '/datapi/datasource',
        name: '数据源管理',
        component: './datapi/DataSource',
        featureCode: 'F_MENU_DATASOURCE_CENTER',
      },
      {
        path: '/datapi/tasks',
        name: '作业审批',
        component: './datapi/Tasks',
        featureCode: 'F_MENU_JOB_LIST',
      },
    ],
  },
  {
    path: '/quality',
    name: '数据质量',
    iconActive: 'https://sitecdn.zcycdn.com/f2e-assets/92612579-f054-42c6-89aa-89dd2e92999d.png',
    iconDefault: 'https://sitecdn.zcycdn.com/f2e-assets/0d46004d-ad86-4e57-b4b4-9721f881aaf4.png',
    featureCode: 'F_MENU_DATA_QUALITY',
    routes: [
      { path: '/quality', redirect: '/quality/monitor/list' },
      {
        path: '/quality/monitor',
        name: '监控管理',
        featureCode: 'F_MENU_DATA_QUALITY_MONITOR',
        routes: [
          { path: '/quality/monitor', redirect: '/quality/monitor/list' },
          {
            path: '/quality/monitor/list',
            component: './quality/Monitor',
            name: '监控管理',
            hideInMenu: true
          },
          {
            path: '/quality/monitor/edit/:id/:tableName',
            name: '监控编辑',
            component: './quality/Monitor/components/EditMonitor',
            hideInMenu: true
          },
        ]
      },
      {
        path: '/quality/template',
        name: '模版管理',
        featureCode: 'F_MENU_DATA_QUALITY_TEMPLATE',
        component: './quality/Template',
      },
      {
        path: '/quality/baseline',
        featureCode: 'F_MENU_DATA_QUALITY_BASE_LINE',
        name: '基线管理',
        routes: [
          { path: '/quality/baseline', redirect: '/quality/baseline/list' },
          {
            path: '/quality/baseline/list',
            component: './quality/Baseline',
            name: '基线管理',
            hideInMenu: true
          },
          {
            path: '/quality/baseline/edit/:id',
            name: '基线编辑',
            component: './quality/Baseline/components/EditBaseline',
            hideInMenu: true
          },
        ]
      },
    ],
  },
  {
    path: '/objectLabel',
    name: '数据标签',
    component: './ObjectLabel',
    featureCode: 'F_MENU_LABEL_MANAGE',
    iconActive: 'https://sitecdn.zcycdn.com/f2e-assets/96f6bf29-38e9-42a9-af58-c5f173bd07c6.svg',
    iconDefault: 'https://sitecdn.zcycdn.com/f2e-assets/c00131f7-0482-4b79-abcd-0aa1952acd3c.svg',
  },
  {
    path: '/configuration',
    name: '系统配置',
    iconActive: 'https://sitecdn.zcycdn.com/f2e-assets/0545456d-5849-4a1a-885c-a72dce22fdfb.png',
    iconDefault: 'https://sitecdn.zcycdn.com/f2e-assets/6af809b7-e099-49be-8b2d-05539e7d07ae.png',
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
          {
            path: '/configuration/authority/group',
            hideInMenu: true,
            routes: [
              {
                path: '/configuration/authority/group',
                redirect: '/configuration/authority/group/list',
              },
              {
                path: '/configuration/authority/group/list',
                name: '用户组管理',
                hideInMenu: true,
                component: './authority/group/List',
              },
            ],
          },
        ],
      },
      {
        path: '/configuration/apps',
        name: '应用管理',
        component: './Apps',
        featureCode: 'F_MENU_APP_MANAGE',
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
    iconActive:
      'https://sitecdn.zcycdn.com/f2e-assets/71ccc72f-6ceb-42d8-8442-146878701297.png?x-oss-process=image/quality,Q_75/format,jpg',
    iconDefault:
      'https://sitecdn.zcycdn.com/f2e-assets/937bbf3b-be59-47ef-92a2-ea7467077cbd.png?x-oss-process=image/quality,Q_75/format,jpg',
    featureCode: 'F_MENU_OPS_CENTER',
    routes: [
      { path: '/operations', redirect: '/operations/dashboard' },
      {
        path: '/operations/dashboard',
        name: '运维看板',
        component: './operations/Dashboard',
        featureCode: 'F_MENU_OPS_DASHBOARD',
      },
      {
        path: '/operations/stream',
        name: '实时作业',
        component: './operations/Stream',
        featureCode: 'F_MENU_STREAM_JOB_OPT',
      },
      {
        path: '/operations/taskHistory',
        name: '作业历史',
        component: './operations/TaskHistory',
        featureCode: 'F_MENU_OPS_DASHBOARD',
      },
      {
        path: '/operations/clusterMonitor',
        name: '集群监控',
        component: './operations/ClusterMonitor',
        featureCode: 'F_MENU_CLUSTER_MONITORING',
      },
      {
        path: '/operations/taskMonitor',
        name: '任务监控',
        component: './operations/TaskMonitor',
        featureCode: 'F_MENU_JOB_MONITORING',
      },
    ],
  },
  {
    path: '/measure',
    name: '数据指标',
    iconActive: 'https://sitecdn.zcycdn.com/f2e-assets/6c565c17-cb18-4678-8049-0d7d6f387877.svg',
    iconDefault: 'https://sitecdn.zcycdn.com/f2e-assets/c5e01666-c8e9-48dd-89a0-42d950117528.svg',
    featureCode: 'F_MENU_MEASURE_MANAGE',
    routes: [
      {
        path: '/measure/list',
        name: '指标',
        component: './Measure/List',
        featureCode: 'F_MENU_MEASURE_METRIC',
      },
      {
        path: '/measure/edit/:id?',
        name: '编辑指标详情',
        component: './Measure/Edit/EditMetric',
        hideInMenu: true
      },
      {
        path: '/measure/view/:id',
        name: '查看指标详情',
        component: './Measure/Edit/ViewMetric',
        hideInMenu: true
      },
      {
        path: '/measure/modifier',
        name: '修饰词',
        component: './Measure/Modifier',
        featureCode: 'F_MENU_MEASURE_MODIFIER',
      },
      {
        path: '/measure/approval',
        name: '指标审批',
        component: './Measure/Approval',
        featureCode: 'F_MENU_MEASURE_APPROVAL',
      },
    ]
  },
  {
    path:'/outLink',
    name:"数据地图",
    outLink: '/',
    iconActive: 'https://sitecdn.zcycdn.com/f2e-assets/afdd0ff9-65b8-4602-b8b7-5a7ec45e124a.png',
    iconDefault: 'https://sitecdn.zcycdn.com/f2e-assets/aa2f4114-78dd-47cc-b20d-c6a374a114e2.png',
    featureCode: 'F_MENU_DATA_MAP',
  },
  {
    path:'/outLink',
    name:"小采BI",
    outLink: '/itable/#/itable/list',
    iconActive: 'https://sitecdn.zcycdn.com/f2e-assets/896839e6-18ee-4c86-896f-7201835a6175.png',
    iconDefault: 'https://sitecdn.zcycdn.com/f2e-assets/896839e6-18ee-4c86-896f-7201835a6175.png',
    featureCode: 'F_MENU_XIAOCAI_BI',
  },
 
  {
    path:'/outLink',
    name:"DataPi",
    outLink: '/idata/#/datapi/apiDevelop',
    iconActive: 'https://sitecdn.zcycdn.com/f2e-assets/acb20dc4-957e-40ff-8fdb-8379b5620e13.png',
    iconDefault: 'https://sitecdn.zcycdn.com/f2e-assets/9d13ea38-5034-4aee-a5c6-7379f5a52111.png',
    featureCode: 'F_MENU_DATAPI',
  },
  {
    path:'/outLink',
    name:"浑仪",
    outLink: '/behavior-analysis/rulesManage/list',
    iconActive: 'https://sitecdn.zcycdn.com/f2e-assets/533d7a4e-debf-46d6-9299-d547d325b86e.png',
    iconDefault: 'https://sitecdn.zcycdn.com/f2e-assets/0a4656b2-ccd0-43e3-b366-01d572f3e707.png',
    featureCode: 'F_MENU_HUNYI',
  },
  {
    path:'/outLink',
    name:"小采DA",
    outLink: '/da',
    iconActive: 'https://sitecdn.zcycdn.com/f2e-assets/b1da873c-9f66-4e6d-9f5e-6510a7863ad0.png',
    iconDefault: 'https://sitecdn.zcycdn.com/f2e-assets/8a0a4715-6220-446b-864a-5eade021d85f.png',
    featureCode: 'F_MENU_IDAS',
  },
  {
    component: './404',
  },
];
