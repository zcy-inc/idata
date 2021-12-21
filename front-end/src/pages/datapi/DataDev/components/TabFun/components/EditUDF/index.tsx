import React, { useEffect, useState } from 'react';
import { Button, Form, Input, message, Select, Upload } from 'antd';
import type { FC } from 'react';
import type { FormInstance } from 'antd';
import styles from './index.less';

import { Folder, UDF } from '@/types/datadev';
import { FolderBelong } from '@/constants/datadev';
import { getFolders, uploadUDFCodeFile } from '@/services/datadev';

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
  const [folders, setFolders] = useState<Folder[]>([]);

  useEffect(() => {
    getFolders({ belong: FolderBelong.DEVFUN })
      .then((res) => setFolders(res.data))
      .catch((err) => {});
  }, []);

  useEffect(() => {
    if (data) {
      const values = {
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
    }
  }, [data]);

  return (
    <div className={styles['edit-dag']}>
      <Form form={form} layout="horizontal" colon={false}>
        <Item name="udfName" label="函数名称" rules={ruleText}>
          <Input placeholder="请输入" size="large" style={{ width }} />
        </Item>
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
        <Item name="upload" label="函数代码" rules={[{ required: true }]}>
          <Upload
            style={{ marginTop: 16 }}
            accept="*"
            maxCount={1}
            customRequest={({ file, onSuccess, onError }) => {
              const formData = new FormData();
              formData.append('uploadFile', file);
              uploadUDFCodeFile(formData)
                .then((res) => {
                  if (res.success) {
                    onSuccess?.(res, file as unknown as XMLHttpRequest);
                    form.setFieldsValue({
                      fileName: res.data.fileName,
                      hdfsPath: res.data.relativePath,
                    });
                  } else {
                    message.error(`上传失败：${res.msg}`);
                  }
                })
                .catch((err) => {
                  onError?.(err, file as unknown as XMLHttpRequest);
                });
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
        <Item name="folderId" label="目标文件夹" rules={ruleSlct}>
          <Select
            placeholder="请选择"
            size="large"
            style={{ width }}
            options={folders.map((_) => ({ label: _.name, value: _.id }))}
            showSearch
            filterOption={(input: string, option: any) => option.label.indexOf(input) >= 0}
          />
        </Item>
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
