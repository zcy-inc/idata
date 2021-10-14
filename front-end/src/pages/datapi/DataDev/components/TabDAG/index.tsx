import React, { Fragment, useEffect, useState } from 'react';
import { Button, Form, message, Modal, Space } from 'antd';
import { useModel } from 'umi';
import { get } from 'lodash';
import type { FC } from 'react';

import { createDAG, deleteDAG, disableDAG, editDAG, enableDAG, getDAG } from '@/services/datadev';
import { PeriodRange } from '@/constants/datadev';
import { IPane } from '@/models/datadev';
import { DAG } from '@/types/datadev';

import EditDAG from './components/EditDAG';
import ViewDAG from './components/ViewDAG';

export interface TabDAGProps {
  pane: IPane;
}

const { confirm } = Modal;

const TabDAG: FC<TabDAGProps> = ({ pane }) => {
  const [mode, setMode] = useState<'view' | 'edit'>(pane.mode);
  const [data, setData] = useState<DAG>();
  const [loading, setLoading] = useState<boolean>(false);
  const [form] = Form.useForm();

  const { getTreeWrapped, onRemovePane, replaceTab } = useModel('datadev', (_) => ({
    replaceTab: _.replaceTab,
    onRemovePane: _.onRemovePane,
    getTreeWrapped: _.getTreeWrapped,
  }));

  useEffect(() => {
    pane.id !== -1 && getDAGWrapped();
  }, [pane]);

  const getDAGWrapped = () => getDAG({ id: pane.id }).then((res) => setData(res.data));

  const onSubmit = () => {
    setLoading(true);
    form.validateFields().then(() => {
      const values = form.getFieldsValue();
      const time: string = values.time.format('HH:mm');
      const hour = time.split(':')[0];
      const minute = time.split(':')[1];
      const day: string[] = get(values, 'dayofmonth', []);
      const month: string[] = get(values, 'month', []);
      const triggerMode = values.triggerMode || 'specified';

      let cronExpression = ''; // 秒 / 分 / 时 / 日(月) / 月 / 日(星期) / 年，可参阅https://www.cnblogs.com/linjiqin/p/3178452.html

      switch (values.periodRange) {
        case PeriodRange.YEAR:
          cronExpression = `0 ${minute} ${hour} ${day.join(',')} ${month.join(',')} ? *`;
          break;
        case PeriodRange.MONTH:
          cronExpression = `0 ${minute} ${hour} ${day.join(',')} * ? *`;
          break;
        case PeriodRange.WEEK:
          const week: string[] = get(values, 'dayofweek', []);
          cronExpression = `0 ${minute} ${hour} ? * ${week.join(',')} *`;
          break;
        case PeriodRange.DAY:
          cronExpression = `0 ${minute} ${hour} * * ? *`;
          break;
        case PeriodRange.HOUR:
          if (triggerMode === 'interval') {
            const start: string = values.start.format('HH:mm');
            const startHour = start.split(':')[0];
            const startMinute = start.split(':')[1];
            const interval: string = get(values, 'interval', 0);
            const endHour = values.start.format('HH');
            cronExpression = `0 ${startMinute} ${startHour}-${endHour}/${interval} * * ? *`;
          }
          if (triggerMode === 'specified') {
            const hours: string[] = get(values, 'hours', []);
            cronExpression = `0 0 ${hours.join(',')} * * ? *`;
          }
          break;
        case PeriodRange.MINUTE:
          const startHour = values.start.format('HH');
          const interval: string = get(values, 'interval', 0);
          const endHour = values.end.format('HH');
          cronExpression = `0 0/${interval} ${startHour}-${endHour} * * ? *`;
          break;

        default:
          break;
      }
      const params = {
        dagInfoDto: {
          name: values.name,
          dwLayerCode: values.dwLayerCode,
          folderId: values.folderId,
        },
        dagScheduleDto: {
          beginTime: values.range[0].valueOf(),
          endTime: values.range[1].valueOf(),
          periodRange: values.periodRange,
          triggerMode,
          cronExpression,
        },
      };
      if (pane.id === -1) {
        createDAG(params)
          .then((res) => {
            if (res.success) {
              message.success('创建DAG成功');
              getTreeWrapped();
              replaceTab({
                oldKey: 'newDAG',
                newKey: `${pane.type}_${pane.belong}_${res.data.dagInfoDto.id}`,
                title: res.data.dagInfoDto.name,
                pane,
              });
            } else {
              message.error('创建DAG失败');
            }
          })
          .catch((err) => {})
          .finally(() => setLoading(false));
      } else {
        Object.assign(params.dagInfoDto, { id: data?.dagInfoDto.id });
        Object.assign(params.dagScheduleDto, { id: data?.dagScheduleDto.id });
        editDAG(params)
          .then((res) => {
            if (res.success) {
              message.success('编辑DAG成功');
              replaceTab({
                oldKey: pane.cid,
                newKey: pane.cid,
                title: res.data.dagInfoDto.name,
                pane,
              });
              getDAGWrapped().then(() => setMode('view'));
              getTreeWrapped();
            } else {
              message.error('编辑DAG失败');
            }
          })
          .catch((err) => {})
          .finally(() => setLoading(false));
      }
    });
  };

  const onDelete = () =>
    confirm({
      title: '删除DAG',
      content: '您确认要删除该DAG吗？',
      autoFocusButton: null,
      onOk: () =>
        deleteDAG({ id: pane.id })
          .then((res) => {
            if (res.success) {
              message.success('删除DAG成功');
              onRemovePane(pane.cid);
              getTreeWrapped();
            } else {
              message.error(`删除DAG失败：${res.msg}`);
            }
          })
          .catch((err) => {}),
    });

  const onEnable = () =>
    confirm({
      title: '启用DAG',
      content: '您确认要启用该DAG吗？',
      autoFocusButton: null,
      onOk: () =>
        enableDAG({ id: pane.id }).then((res) => {
          if (res.success) {
            message.success('启用DAG成功');
            getDAGWrapped();
          } else {
            message.error(`启用DAG失败：${res.msg}`);
          }
        }),
    });

  const onDisable = () =>
    confirm({
      title: '停用DAG',
      content: '您确认要停用该DAG吗？',
      autoFocusButton: null,
      onOk: () =>
        disableDAG({ id: pane.id }).then((res) => {
          if (res.success) {
            message.success('停用DAG成功');
            getDAGWrapped();
          } else {
            message.error(`停用DAG失败：${res.msg}`);
          }
        }),
    });

  const onCancel = () => {
    if (pane.id === -1) {
      onRemovePane('newTable');
    } else {
      getDAGWrapped().then(() => setMode('view'));
    }
  };

  return (
    <Fragment>
      {mode === 'view' && <ViewDAG data={data} />}
      {mode === 'edit' && <EditDAG data={data} form={form} />}
      <div className="workbench-submit">
        {mode === 'view' && (
          <Space>
            <Button key="edit" size="large" type="primary" onClick={() => setMode('edit')}>
              编辑
            </Button>
            {data?.dagInfoDto.status === 0 && (
              <Button key="enable" size="large" onClick={onEnable}>
                启用
              </Button>
            )}
            {data?.dagInfoDto.status === 1 && (
              <Button key="disable" size="large" onClick={onDisable}>
                停用
              </Button>
            )}
            <Button key="del" size="large" onClick={onDelete}>
              删除
            </Button>
          </Space>
        )}
        {mode === 'edit' && (
          <Space>
            <Button key="save" size="large" type="primary" onClick={onSubmit} loading={loading}>
              保存
            </Button>
            <Button key="cancel" size="large" onClick={onCancel}>
              取消
            </Button>
          </Space>
        )}
      </div>
    </Fragment>
  );
};

export default TabDAG;