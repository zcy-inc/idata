import React, { useEffect, useState } from 'react';
import { Button, Form, Input, Select, Upload } from 'antd';
import type { FC } from 'react';
import type { FormInstance } from 'antd';
import { UDF } from '@/types/datadev';
import { uploadUDFCodeFile } from '@/services/datadev';
import { DEVFUNFolderFormItem } from '../../../../../components/FolderFormItem';
import styles from './index.less';

interface EditUDFProps {
  form: FormInstance;
  data?: UDF;
}

const width = 400;
const ruleText = [{ required: true, message: '请输入' }];
const ruleSlct = [{ required: true, message: '请选择' }];
const { Item } = Form;
const { TextArea } = Input;

const EditUDF: FC<EditUDFProps> = ({ data, form }) => {
  const [hdfsPath, setHdfsPath] = useState('');

  useEffect(() => {
    if (data) {
      const values = {
        ...data,
        udfName: data.udfName,
        udfType: data.udfType,
        upload: data.fileName,
        fileName: data.fileName,
        hdfsPath: data.hdfsPath,
        returnType: data.returnType,
        returnSample: data.returnSample,
        folderId: data.folderId,
        description: data.description,
        commandFormat: data.commandFormat,
        udfSample: data.udfSample,
      };
      form.setFieldsValue(values);
      setHdfsPath(data.hdfsPath);
    }
  }, [data]);

  const udfType = Form.useWatch('udfType', form);

  return (
    <div className={styles['edit-dag']}>
      <Form form={form} layout="horizontal" colon={false}>
        <Item name="udfName" label="函数名称" rules={ruleText}>
          <Input placeholder="请输入" size="large" style={{ width }} />
        </Item>
        {udfType === 'JavaFunction' && (
          <Item name="sourceName" label="执行类" rules={ruleText}>
            <Input placeholder="请输入" style={{ width }} />
          </Item>
        )}
        <Item name="udfType" label="函数类型" rules={ruleSlct}>
          <Select
            placeholder="请选择"
            size="large"
            style={{ width }}
            options={[
              { label: 'PythonFunction', value: 'PythonFunction' },
              { label: 'JavaFunction', value: 'JavaFunction' },
              { label: 'JavaUDAF', value: 'JavaUDAF' },
            ]}
          />
        </Item>
        <Item
          name="upload"
          label="函数代码"
          rules={[{ required: true }]}
          extra={`hdfs路径: ${hdfsPath || '-'}`}
        >
          <Upload
            style={{ marginTop: 16 }}
            accept="*"
            maxCount={1}
            showUploadList={{
              showRemoveIcon: false,
              showPreviewIcon: false
            }}
            customRequest={({ file, onSuccess, onError }) => {
              const formData = new FormData();
              formData.append('uploadFile', file);
              uploadUDFCodeFile(formData)
                .then((res) => {
                  if (res.success) {
                    onSuccess?.(res, (file as unknown) as XMLHttpRequest);
                    form.setFieldsValue({
                      fileName: res.data.fileName,
                      hdfsPath: res.data.relativePath,
                    });
                    setHdfsPath(res.data.relativePath);
                  }
                })
                .catch((err) => {
                  onError?.(err, (file as unknown) as XMLHttpRequest);
                });
            }}
            onRemove={async () => {
              form.resetFields(['fileName', 'hdfsPath']);
            }}
          >
            <Button>上传文件</Button>
          </Upload>
        </Item>
        <Item name="fileName" style={{ position: 'absolute', visibility: 'hidden' }}>
          <Input />
        </Item>
        <Item name="hdfsPath" style={{ position: 'absolute', visibility: 'hidden' }}>
          <Input />
        </Item>
        <Item name="returnType" label="返回类型" rules={ruleSlct}>
          <Select
            placeholder="请选择"
            size="large"
            style={{ width }}
            options={[
              { label: 'String', value: 'String' },
              { label: 'Boolean', value: 'Boolean' },
              { label: 'Date', value: 'Date' },
              { label: 'Timestamp', value: 'Timestamp' },
              { label: 'Decimal', value: 'Decimal' },
              { label: 'Double', value: 'Double' },
              { label: 'Float', value: 'Float' },
              { label: 'Integer', value: 'Integer' },
              { label: 'Long', value: 'Long' },
            ]}
          />
        </Item>
        <Item name="returnSample" label="返回值" rules={ruleText}>
          <Input placeholder="请输入" size="large" style={{ width }} />
        </Item>
        <DEVFUNFolderFormItem style={{ width }} />
        <Item name="description" label="描述" rules={ruleText}>
          <TextArea placeholder="请输入" style={{ width }} />
        </Item>
        <Item name="commandFormat" label="命令格式" rules={ruleText}>
          <Input placeholder="请输入" size="large" style={{ width }} />
        </Item>
        <Item name="udfSample" label="示例" rules={ruleText}>
          <Input placeholder="请输入" size="large" style={{ width }} />
        </Item>
      </Form>
    </div>
  );
};

export default EditUDF;
