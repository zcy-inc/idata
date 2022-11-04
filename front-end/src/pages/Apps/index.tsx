import React, { useEffect, useState } from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import { Button, message, Table } from 'antd';
import { findApps, add, update, IApp } from '@/services/apps';
import { AddModal } from './components/addModal';

export default () => {
  const [curPage, setCurPage] = useState(1);
  const [total, setTotal] = useState<number>(0);
  const [data, setData] = useState<Object[]>([]);
  const [showAddModal, setShowAddModal] = useState(false);
  const [curApp, setCurApp] = useState<IApp>();
  const [isPreview, setIsPreview] = useState<boolean>(false);

  useEffect(() => {
    getData();
  }, [curPage]);

  const getData = async () => {
    const { success, data: {
      content,
      total,
    } } = await findApps({
      limit: 10,
      offset: 10 * (curPage - 1),
    });
    if (success) {
      setData(content);
      setTotal(total);
    }
  };

  const columns = [
    { title: '应用名称', key: 'appName', dataIndex: 'appName' },
    { title: '最后编辑人', key: 'editor', dataIndex: 'editor' },
    {
      title: '接入功能',
      key: 'appFeatures',
      dataIndex: 'appFeatures',
      render: (appFeatures: any[]) => {
        return appFeatures.map((feature) => feature.featureName).join('，')
      }
    },
    {
      title: '操作',
      key: 'appName',
      dataIndex: 'appName',
      render: (_: any, row: any) => {
        return (
          <><Button onClick={() => onEdit(row)}>编辑</Button><Button onClick={() => onPreview(row)} style={{marginLeft: '6px'}}>查看</Button></>
        );
      }
    },
  ];

  const onSubmit = async (values, isEdit: boolean, id: string) => {
    const { appName, description, appFeatures: featureCodes } = values;
    const api = isEdit ? update : add;
    const { success, msg } = await api({
      id,
      appName,
      description,
      featureCodes: featureCodes.join(','),
    });
    if (success) {
      message.success(isEdit ?'编辑成功' : '新增成功');
      getData();
    } else {
      message.error(msg);
    }

  }

  const onEdit = (row: React.SetStateAction<IApp | undefined>) => {
    setCurApp(row);
    setShowAddModal(true);
    setIsPreview(false);
  };

  const onPreview = (row: React.SetStateAction<IApp | undefined>) => {
    setCurApp(row);
    setShowAddModal(true);
    setIsPreview(true);
  };

  return (
    <PageContainer
      fixedHeader
      header={{
        title: '应用配置',
        breadcrumb: {
          routes: [
            {
              path: '',
              breadcrumbName: '系统配置',
            },
            {
              path: '',
              breadcrumbName: '应用配置',
            }
          ],
        },
      }}
    >
      <div className="zcy-content" >
        <div>
          <Button onClick={() => onEdit(undefined)}>新增应用</Button>
        </div>
        <Table
          rowKey="id"
          columns={columns}
          dataSource={data}
          scroll={{ x: 'max-content' }}
          style={{ marginTop: 16 }}
          pagination={{
            total,
            current: curPage,
            showSizeChanger: false,
            showTotal: (t) => `共${t}条`,
            onChange: (page) => {setCurPage(page);},
          }}
        />
      </div>
      {
        showAddModal && (
          <AddModal
            visible={showAddModal}
            onCancel={() => setShowAddModal(false)}
            onSubmit={onSubmit}
            app={curApp}
            isPreview={isPreview}
          />
        )
      }
    </PageContainer>
  )
};
