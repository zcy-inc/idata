import React, { forwardRef, useEffect, useImperativeHandle, useState } from 'react';
import type { ForwardRefRenderFunction } from 'react';
import { Form, FormInstance, Input, Modal, Button, message } from 'antd';
import DataSourceSelect from '@/components/DSSelect';
import { JoinSelect } from '@/components';
import { getUDFList, genFlinkTemplate } from '@/services/datadev';
import type { UDF, Task } from '@/types/datadev';
import { TaskTypes } from '@/constants/datadev';
import SqlEditor from '@/components/SqlEditor';
import { EditorPanel, EditorPanelProps } from '../../../../../../components/EditorPanel';

const { confirm } = Modal;
const { Item } = Form;

interface SparkSqlProps {
  panelProps: EditorPanelProps;
  form: FormInstance<any>;
  editorRef: any;
  data: {
    task: Task;
    content: any;
  };
  visible: boolean;
  onCancel: () => void;
}

const SqlContent: ForwardRefRenderFunction<unknown, SparkSqlProps> = (
  { data: { content, task }, visible, onCancel, panelProps, form, editorRef },
  ref,
) => {
  const [UDFList, setUDFList] = useState<UDF[]>([]);

  const [loading, setLoading] = useState(false);

  useImperativeHandle(ref, () => ({
    form: form,
  }));

  useEffect(() => {
    getUDFListWrapped();
  }, []);

  useEffect(() => {
    if (content) {
      form.setFieldsValue(content);
    }
  }, [content]);

  const getUDFListWrapped = () => {
    setLoading(true);
    getUDFList()
      .then((res) => setUDFList(res.data))
      .catch((err) => {})
      .finally(() => setLoading(false));
  };

  const fetchSrcTemplate = (value: unknown[]) => genFlinkTemplate({ flinkSourceConfigs: value });
  const fetchDestTemplate = (value: unknown[]) => genFlinkTemplate({ flinkSinkConfigs: value });
  const genEditorTemplate = () => {
    const flinkExtConfig = form.getFieldValue(['extConfig', 'flinkExtConfig']);
    if (flinkExtConfig) {
      confirm({
        title: '提示',
        content: '生成模板会覆盖原有编辑内容',
        onOk: async () => {
          const temp = await genFlinkTemplate(flinkExtConfig);
          form.setFieldsValue({ sourceSql: temp });
        },
      });
    } else {
      message.warning('请先选择数据源');
    }
  };
  const footer =
    task.jobType === TaskTypes.SQL_FLINK ? (
      <Button type="primary" onClick={genEditorTemplate}>
        生成模板
      </Button>
    ) : null;

  return (
    <Form
      layout="horizontal"
      form={form}
      colon={false}
      labelCol={{ span: 6 }}
      wrapperCol={{ span: 16 }}
    >
      <EditorPanel {...panelProps}>
        <Item name="sourceSql" noStyle>
          <SqlEditor
            formRef={editorRef}
            height="100%"
            width="100%"
            language="sql"
            options={{ automaticLayout: true, quickSuggestions: false, fontSize: 28 }}
          />
        </Item>
      </EditorPanel>
      <Modal title="作业配置" visible={visible} onCancel={onCancel} footer={footer} forceRender>
        <Item name="externalTables" label="外部表">
          <Input placeholder="请输入" />
        </Item>
        <Item name="udfIds" label="自定义函数">
          <JoinSelect
            loading={loading}
            placeholder="请选择"
            options={UDFList.map(({ udfName, id }) => ({
              label: udfName,
              value: `${id}`,
            }))}
          />
        </Item>
        {task.jobType === TaskTypes.SQL_FLINK && (
          <>
            <Item label="来源数据源" name={['extConfig', 'flinkExtConfig', 'flinkSourceConfigs']}>
              <DataSourceSelect quantityCustom fetchTemplate={fetchSrcTemplate} />
            </Item>
            <Item label="目标数据源" name={['extConfig', 'flinkExtConfig', 'flinkSinkConfigs']}>
              <DataSourceSelect fetchTemplate={fetchDestTemplate} />
            </Item>
          </>
        )}
      </Modal>
    </Form>
  );
};

export default forwardRef(SqlContent);
