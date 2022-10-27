import React, { useEffect, useState } from 'react';
import { ProFormSelect, ProFormText, ProFormDatePicker, QueryFilter } from '@ant-design/pro-form';
import { Form, Space, Table, Button, Modal, Input, message } from 'antd';
import { ExclamationCircleOutlined } from '@ant-design/icons';
import type { FC } from 'react';
import { history, Link } from 'umi';
import type { ColumnsType } from 'antd/lib/table/Table';
import type { MetricFloderItem, MetricListItem } from '@/types/measure';
import showDialog from '@/utils/showDialog';
import styles from './index.less';
import { getMeasures, indexPublish, indexRetreat } from '@/services/measure';
import { getEnumValues } from '@/services/datadev';
import CreateMeasure from './components/CreateMeasure';
import { LabelTag } from '@/constants/datapi';
import moment from 'moment';

const { confirm } = Modal;

const MetricTypeOps = [
  { label: '原子指标', value: 'ATOMIC_METRIC_LABEL' },
  { label: '派生指标', value: 'DERIVE_METRIC_LABEL' },
];
const DataSource: FC<{ currentNode: MetricFloderItem }> = ({ currentNode }) => {
  const [data, setData] = useState<MetricListItem[]>([]);
  const [total, setTotal] = useState(0);
  const [current, setCurrent] = useState(1);
  const [loading, setLoading] = useState(false);
  const [bizProcessEnum, setBizProcessEnum] = useState<{ label: string; value: string }[]>([]);
  const [dataSetOptions, setDataSetOptions] = useState<{ label: string; value: string }[]>([]);
  const [isModalOpen, setIsModalOpen] = useState(false); // 确认发布弹窗
  const [currentIndex, setCurrentIndex] = useState({}); // 当前要发布/撤销的指标数据
  const [form] = Form.useForm();
  const [publishForm] = Form.useForm();

  useEffect(() => {
    getEnumValues({ enumCode: 'bizProcessEnum:ENUM' })
      .then((res) => {
        const options = res.data?.map((enumValue) => ({
          label: enumValue.enumValue,
          value: enumValue.valueCode,
        }));
        setBizProcessEnum(options);
      })
      .catch((err) => { });

    getEnumValues({ enumCode: 'domainIdEnum:ENUM' }).then(res => {
      const dataSetOptions = res.data?.map((enumValue: { enumValue: string; valueCode: string }) => ({
        label: enumValue.enumValue,
        value: enumValue.valueCode,
      }));
      setDataSetOptions(dataSetOptions);
    })
  }, []);

  useEffect(() => {
    if (currentNode.folderId) {
      getTableData(10 * (current - 1));
    }
  }, [currentNode, current])

  const TagMap = (labelTag: LabelTag) => {
    switch (labelTag) {
      case LabelTag.ATOMIC_METRIC_LABEL:
      case LabelTag.ATOMIC_METRIC_LABEL_DISABLE:
      case LabelTag.ATOMIC_METRIC_LABEL_DRAFT:
      case LabelTag.ATOMIC_METRIC_LABEL_APPROVE:
        return '原子指标';
      case LabelTag.DERIVE_METRIC_LABEL:
      case LabelTag.DERIVE_METRIC_LABEL_DISABLE:
      case LabelTag.DERIVE_METRIC_LABEL_DRAFT:
      case LabelTag.DERIVE_METRIC_LABEL_APPROVE:
        return '派生指标';
      default:
        return '';
    }
  };

  const getTableData = (offset: number) => {
    const { measureDeadline, ...params } = form.getFieldsValue();
    if (params.enable === 0) {
      params.enable = false;
    } else if (params.enable === 1) {
      params.enable = true;
    }
    setLoading(true);
    getMeasures({
      ...params,
      measureDeadline: measureDeadline ? moment(measureDeadline).format('YYYY-MM-DD') : undefined,
      limit: 10,
      offset,
      folderId: currentNode.folderId,
      measureType: 'METRIC_LABEL'
    })
      .then((res) => {
        setTotal(res.data.total);
        setData(res.data.content);
      })
      .catch((err) => { })
      .finally(() => setLoading(false));
  };

  const onFinish = async (values: any) => {
    setCurrent(1)
    getTableData(0);
  }

  const onReset = () => {
    onFinish({});
  }

  const addTreeItem = (node: { folderId: string } = { folderId: '' }) => {
    const isEdit = !!node.folderId;
    showDialog(`${isEdit ? '编辑指标' : '新建指标'}`, {
      modalProps: {
        width: 540
      },
      formProps: {
        node: {
          folderId: currentNode.pos
        }
      },
      beforeConfirm: (dialog, form, done) => {
        form.handleSubmit().then((values: any) => {
          done();
          history.push({
            pathname: '/measure/edit/',
            state: values
          });
        })
      },
    }, CreateMeasure)
  }

  // 渲染指标状态
  const renderLabelTag = (labelTag: string) => {
    switch (true) {
      case labelTag.endsWith('DISABLE'):
        return <span className={styles.disabled}>停用</span>;
      case labelTag.endsWith('DRAFT'):
        return <span className={styles.waited}>草稿</span>;
      case labelTag.endsWith('APPROVE'):
        return <span className={styles.waited}>待审批</span>;
      default:
        return <span className={styles.enabled}>启用</span>;
    }
  }

  // 发布
  const handlePublish = (record: any) => {
    setIsModalOpen(true);
    setCurrentIndex(record)
  }

  // 确认发布 metricCode = labelCode
  const handlePublishConfirm = () => {
    const params = {
      metricCode: currentIndex?.labelCode,
      remark: publishForm.getFieldsValue()?.remark
    }
    indexPublish(params).then(res => {
      if (+res.code) {
        getTableData(0);
        setIsModalOpen(false);
        publishForm.resetFields();
        message.success("发布成功")
      } else {
        message.success(res.msg)
      }
    }).catch(err => { })
  };

  // 取消发布
  const handleCancel = () => {
    setIsModalOpen(false);
    publishForm.resetFields();
  };

  // 撤销 metricCode = labelCode
  const handleRevoke = (record: any) => {
    const params = {
      metricCode: record?.labelCode
    }
    confirm({
      title: '提示',
      icon: <ExclamationCircleOutlined />,
      content: '你确定要撤销审批吗？',
      onOk() {
        indexRetreat(params).then(res => {
          if (+res.code) {
            getTableData(0);
            message.success("撤销成功")
          } else {
            message.success(res.msg)
          }
        }).catch(err => { })
      },
      onCancel() {
        console.log('取消');
      },
    });
  }

  // 渲染操作按钮
  const renderOps = (record: any) => {
    const { labelTag } = record
    switch (true) {
      case labelTag.endsWith('DISABLE'):
        // 禁用
        return (
          <Space size={16}>
            <Link to={`/measure/view/${record.labelCode}`}>查看</Link>
            <Link to={`/measure/edit/${record.labelCode}`}>编辑</Link>
          </Space>
        )
      case labelTag.endsWith('DRAFT'):
        // 草稿
        return (
          <Space size={16}>
            <Link to={`/measure/view/${record.labelCode}`}>查看</Link>
            <Link to={`/measure/edit/${record.labelCode}`}>编辑</Link>
            <Button type="link" onClick={() => handlePublish(record)}>发布</Button>
          </Space>
        )
      case labelTag.endsWith('APPROVE'):
        // 待审批
        return (
          <Space size={16}>
            <Link to={`/measure/view/${record.labelCode}`}>查看</Link>
            <Button type="link" onClick={() => handleRevoke(record)}>撤销</Button>
          </Space>
        )
      default:
        // 启用
        return (
          <Space size={16}>
            <Link to={`/measure/view/${record.labelCode}`}>查看</Link>
            <Link to={`/measure/edit/${record.labelCode}`}>编辑</Link>
          </Space>
        )
    }
  }

  const columns: ColumnsType<MetricListItem> = [
    { title: '指标ID', key: 'metricId', dataIndex: 'metricId' },
    { title: '指标名称', key: 'labelName', dataIndex: 'labelName' },
    {
      title: '指标类型',
      key: 'labelTag',
      dataIndex: 'labelTag',
      render: (t) => TagMap(t)
    },
    { title: '主题域', key: 'domain', dataIndex: 'domain' },
    { title: '业务过程', key: 'bizProcessValue', dataIndex: 'bizProcessValue' },
    {
      title: '指标状态',
      key: 'labelTag',
      dataIndex: 'labelTag',
      render: (t) => renderLabelTag(t)
    },
    { title: '截止生效日期', key: 'metricDeadline', dataIndex: 'metricDeadline' },
    { title: '创建人', key: 'creator', dataIndex: 'creator' },
    { title: '更新人', key: 'editor', dataIndex: 'editor' },
    { title: '最近更新时间', key: 'editTime', dataIndex: 'editTime' },
    { title: '所属文件夹', key: 'folderName', dataIndex: 'folderName' },
    {
      title: '操作',
      key: 'ops',
      dataIndex: 'ops',
      fixed: 'right',
      // render: (value, record) => (
      //   <Space size={16}>
      //     <Link to={`/measure/view/${record.labelCode}`}>查看</Link>
      //     <Link to={`/measure/edit/${record.labelCode}`}>编辑</Link>
      //   </Space>
      // ),
      render: (value, record) => renderOps(record),
    },
  ];

  const layout = {
    labelCol: { span: 4 },
    wrapperCol: { span: 16 },
  };

  return (
    <div className={styles.container}>
      <QueryFilter
        form={form}
        className='meaure-list-wrap'
        layout="horizontal"
        onFinish={onFinish}
        onReset={onReset}
        labelCol={{ span: 6 }}
        labelWidth={96}
      >
        <ProFormText
          name="measureId"
          label="指标ID"
          placeholder="请输入"
        />
        <ProFormText
          name="measureName"
          label="指标名称"
          placeholder="请输入"
        />
        <ProFormSelect
          name="bizProcessCode"
          label="业务过程"
          placeholder="请选择"
          options={bizProcessEnum}
        />
        <ProFormSelect
          name="metricType"
          label="指标类型"
          placeholder="请选择"
          options={MetricTypeOps}
        />
        <ProFormSelect
          name="enable"
          label="指标状态"
          placeholder="请选择"
          options={[{ label: '停用', value: 0 }, { label: '启用', value: 1 }]}
        />
        <ProFormText
          name="creator"
          label="创建人"
          placeholder="请输入"
        />
        <ProFormDatePicker
          label="截止生效日期"
          name="measureDeadline"
        />
        <ProFormSelect
          name="domain"
          label="主题域"
          placeholder="请选择"
          options={dataSetOptions}
        />
      </QueryFilter>
      <div style={{ textAlign: 'right' }}>
        <Button key="edit" onClick={() => addTreeItem()}>
          新增
        </Button>
      </div>
      <Table<MetricListItem>
        rowKey="id"
        columns={columns}
        dataSource={data}
        scroll={{ x: 'max-content' }}
        style={{ marginTop: 16 }}
        loading={loading}
        pagination={{
          total,
          current,
          showTotal: (t) => `共${t}条`,
          onChange: (page) => { setCurrent(page); },
        }}
      />

      <Modal
        title="提交发布"
        open={isModalOpen}
        onOk={handlePublishConfirm}
        onCancel={handleCancel}>
        <Form
          {...layout}
          form={publishForm}
          name="control-hooks"
        >
          <Form.Item name="remark" label="提交说明">
            <Input placeholder="请输入说明" />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default DataSource;
