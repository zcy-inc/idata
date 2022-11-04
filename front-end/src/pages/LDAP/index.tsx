import React from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import Scheduling from '@/pages/Integrated/components/Scheduling'
export default () => (
  <PageContainer
    fixedHeader
    header={{
      title: 'LDAP配置',
      breadcrumb: {
        // routes: [
        //   {
        //     path: '',
        //     breadcrumbName: '系统配置',
        //   },
        //   {
        //     path: '',
        //     breadcrumbName: 'LDAP配置',
        //   }
        // ],
      },
    }}
  >
    <div className="zcy-content"  >
    <Scheduling type="LDAP"/>
    </div>
  </PageContainer>
);
