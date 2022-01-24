export default {
  '/api/p1/uc/roles': {
    success: true,
    data: {
      content: [
        {
          id: 1,
          name: 'mock角色1',
          editor: '百里',
        },
      ],
      total: 21,
    },
  },
  '/api/p1/uac/users': {
    success: true,
    data: {
      content: [
        {
          id: 1,
          username: 'yonghu1',
          nickname: '百里',
          editor: '百里',
          mobile: '18765432454',
          realName: '王小帅',
          department: '大数据',
          roleCodes: [1],
          roleNames: ['管理员'],
          authType: 'LDAP',
        },
        {
          id: 2,
          username: 'yonghu2',
          nickname: '千里',
          editor: '百里',
          realName: '韩信',
          mobile: '18765432458',
          department: '大数据',
          roleCodes: [1],
          roleNames: ['管理员'],
          authType: 'REGISTER',
        },
      ],
      total: 21,
    },
  },
  '/api/p1/uac/roleFolderTree': {
    success: true,
    data: [
      {
        name: '数仓研发',
        type: 'MENU',
        featureCode: 1,
        children: [
          {
            name: '数仓设计',
            type: 'MENU',
            featureCode: 2,
            children: [
              {
                name: '数仓设计1',
                type: 'FOLDER',
                folderId: 1,
                filePermission: '111',
                children: [
                  {
                    name: '数仓设计1-1',
                    type: 'FOLDER',
                    folderId: 4,
                    filePermission: '000',
                  },
                ],
              },
              {
                name: 'ADS',
                type: 'FOLDER',
                folderId: 5,
                filePermission: '010',
              },
            ],
          },
        ],
      },
    ],
  },
  '/api/p1/uac/roleFeatureTree': {
    success: true,
    data: [
      {
        featureCode: 1,
        featureName: '数据研发',
        children: [
          {
            featureCode: 2,
            featureName: '商家标签组',
            children: [
              {
                featureCode: 3,
                featureName: '商家活跃度1',
                children: [
                  {
                    featureCode: 52,
                    featureName: '商家活跃度3',
                  },
                  {
                    featureCode: 4,
                    featureName: '商家活跃度4',
                  },
                ],
              },
              {
                featureCode: 31,
                featureName: '商家活跃度2',
                children: [
                  {
                    featureCode: 51,
                    featureName: '商家活跃度5',
                  },
                  {
                    featureCode: 441,
                    featureName: '商家活跃度6',
                  },
                ],
              },
            ],
          },
        ],
      },
      {
        featureCode: 100,
        featureName: '数据研发2',
      },
    ],
  },
  '/api/p0/sys/systemFolderTree': {
    success: true,
    data: [
      {
        name: '数仓研发',
        type: 'MENU',
        featureCode: 1,
        children: [
          {
            name: '数仓设计',
            type: 'MENU',
            featureCode: 2,
            children: [
              {
                name: '数仓设计1',
                type: 'FOLDER',
                folderId: 1,
                filePermission: '111',
                children: [
                  {
                    name: '数仓设计1-1',
                    type: 'FOLDER',
                    folderId: 4,
                    filePermission: '000',
                  },
                ],
              },
              {
                name: 'ADS',
                type: 'FOLDER',
                folderId: 5,
                filePermission: '010',
              },
            ],
          },
        ],
      },
    ],
  },
  '/api/p0/sys/systemFeatureTree': {
    success: true,
    data: [
      {
        featureCode: 1,
        featureName: '数据研发',
        children: [
          {
            featureCode: 2,
            featureName: '商家标签组',
            children: [
              {
                featureCode: 3,
                featureName: '商家活跃度1',
                children: [
                  {
                    featureCode: 52,
                    featureName: '商家活跃度3',
                  },
                  {
                    featureCode: 4,
                    featureName: '商家活跃度4',
                  },
                ],
              },
              {
                featureCode: 31,
                featureName: '商家活跃度2',
                children: [
                  {
                    featureCode: 51,
                    featureName: '商家活跃度5',
                  },
                  {
                    featureCode: 441,
                    featureName: '商家活跃度6',
                  },
                ],
              },
            ],
          },
        ],
      },
      {
        featureCode: 100,
        featureName: '数据研发2',
      },
    ],
  },
};
