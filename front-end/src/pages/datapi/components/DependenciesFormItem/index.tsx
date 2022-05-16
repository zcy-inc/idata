import React, { FC, useState } from 'react';
import { Form, Select, Table, Button, message, FormItemProps } from 'antd';
import { IconFont } from '@/components';
import { ColumnsType } from 'antd/es/table';
import { useRequest } from 'ahooks';
import { getConfiguredTaskList, getRecommendJob } from '@/services/datadev';
import { ProForm } from '@ant-design/pro-form';
import type { DependenciesJob } from '@/types/datadev';

type DependenciesValue = DependenciesJob & {
  isNew?: boolean;
};

type DependenciesInputProps = Parameters<typeof getRecommendJob>[0] & {
  value?: DependenciesValue[];
  onChange?: (v?: DependenciesValue[]) => void;
};

export const DependenciesInput: FC<DependenciesInputProps> = ({
  environment,
  jobId,
  // version,
  onChange,
  value,
}) => {
  const { data: optionData } = useRequest(() => getConfiguredTaskList({ environment }), {
    refreshDeps: [environment],
  });
  const jobOptions: DependenciesValue[] | undefined = optionData?.data?.map((item) => ({
    ...item,
    prevJobDagId: item.dagId,
    prevJobDagName: item.dagName,
    prevJobId: item.jobId,
    prevJobName: item.jobName,
    environment,
  }));
  const [selectDependency, setSelectDependency] = useState<DependenciesValue>();

  const handleDelete = (index: number) => {
    if (Array.isArray(value)) {
      const newVal = [...value];
      newVal.splice(index, 1);
      onChange?.(newVal);
    }
  };

  const handleManualAdd = () => {
    if (!selectDependency) {
      message.info('请先选择依赖的上游作业');
      return;
    }
    const valueItem = {
      ...selectDependency,
      isNew: true,
    };

    if (Array.isArray(value)) {
      const newVal = [...value];
      const prevJobIds = newVal.map((item) => item.prevJobId);
      if (!prevJobIds.includes(valueItem.prevJobId)) {
        newVal.push(valueItem);
        onChange?.(newVal);
      } else {
        message.info('作业已存在，勿重复添加');
      }
    } else {
      onChange?.([valueItem]);
    }
  };

  const handleAutoAdd = async () => {
    const { data } = await getRecommendJob({ jobId, environment, version: null });
    const newValueItems = data.map((item) => ({ ...item, isNew: true }));

    if (Array.isArray(value)) {
      const newVal = [...value];
      const prevJobIds = newVal.map((item) => item.prevJobId);
      newVal.push(...newValueItems.filter(({ prevJobId }) => !prevJobIds.includes(prevJobId)));
      onChange?.(newVal);
    } else {
      onChange?.(newValueItems);
    }
  };

  const columns: ColumnsType<DependenciesValue> = [
    {
      title: '父节点输出作业名称',
      dataIndex: 'prevJobName',
      key: 'prevJobName',
      width: '36%',
      align: 'center',
      render: (name, r) => {
        return r.isNew ? (
          <>
            <IconFont type="icon-xin" style={{ marginRight: 8 }} />
            {name}
          </>
        ) : (
          name
        );
      },
    },
    { title: '所属DAG', dataIndex: 'prevJobDagName', key: 'prevJobDagName', width: '45%' },
    {
      title: '操作',
      key: 'option',
      width: 80,
      render: (v, r, i) => <a onClick={() => handleDelete(i)}>删除</a>,
    },
  ];
  return (
    <>
      <ProForm.Item
        label="依赖的上游作业"
        addonAfter={
          <div style={{ display: 'flex' }}>
            <Button onClick={handleManualAdd}>添加</Button>
            <Button onClick={handleAutoAdd} style={{ marginLeft: 8 }}>
              自动推荐
            </Button>
          </div>
        }
        style={{ width: 400 }}
      >
        <Select
          options={jobOptions}
          showSearch
          filterOption={(input: string, option?: DependenciesValue) =>
            !!(option && option.prevJobName.indexOf(input) >= 0)
          }
          fieldNames={{ label: 'prevJobName', value: 'prevJobId' }}
          style={{ width: 300 }}
          onChange={(_, option) => setSelectDependency(option as DependenciesValue)}
        />
      </ProForm.Item>
      <Table
        rowKey="jobName"
        columns={columns}
        dataSource={value}
        pagination={false}
        size="small"
        style={{ minHeight: 150 }}
      />
    </>
  );
};

export function DependenciesFormItem<T>(
  props: { fieldProps: DependenciesInputProps } & FormItemProps<T>,
) {
  return (
    <Form.Item noStyle {...props}>
      <DependenciesInput {...props.fieldProps} />
    </Form.Item>
  );
}
