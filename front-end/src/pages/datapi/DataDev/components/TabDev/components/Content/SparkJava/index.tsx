import React, { forwardRef, useEffect, useImperativeHandle, useState } from 'react';
import { Button, Form, FormInstance, Input, Upload } from 'antd';
import type { ForwardRefRenderFunction } from 'react';
import Title from '@/components/Title';
import ParamList from '../../ParamList';
import { uploadJar } from '@/services/datadev';
import { UploadFile } from 'antd/lib/upload/interface';
interface SparkJavaProps {
  data: any;
  form: FormInstance<any>;
  jobId: number;
}

const { Item } = Form;
const width = 200;

const SparkJava: ForwardRefRenderFunction<unknown, SparkJavaProps> = (
  { data, jobId, form },
  ref,
) => {
  const [fileList, setFileList] = useState<UploadFile[]>([]);

  useImperativeHandle(ref, () => ({
    form: form,
  }));

  useEffect(() => {
    if (data?.resourceHdfsPath) {
      const resourceHdfsPaths = data.resourceHdfsPath?.split('/') || [];
      setFileList([
        {
          uid: '1',
          name: resourceHdfsPaths[resourceHdfsPaths.length - 1],
          status: 'done',
        },
      ]);
      form.setFieldsValue({
        // upload: data.resourceHdfsPath,
        resourceHdfsPath: data.resourceHdfsPath,
        appArguments: data.appArguments,
        mainClass: data.mainClass,
      });
    }
  }, [data]);

  return (
    <>
      <Title style={{ marginTop: 16 }}>应用配置</Title>
      <Form form={form} colon={false}>
        <Item name="upload" label="执行文件" rules={[{ required: true }]}>
          <Upload
            style={{ marginTop: 16 }}
            accept="*"
            maxCount={1}
            fileList={fileList}
            customRequest={({ file, onSuccess, onError }) => {
              const formData = new FormData();
              formData.append('file', file);
              uploadJar({ jobId }, formData)
                .then((res) => {
                  if (res.success) {
                    onSuccess?.(res, (file as unknown) as XMLHttpRequest);
                    form.setFieldsValue({ resourceHdfsPath: res.data });
                    const resourceHdfsPaths = res.data?.split('/') || [];
                    setFileList([
                      {
                        uid: '1',
                        name: resourceHdfsPaths[resourceHdfsPaths.length - 1],
                        status: 'done',
                      },
                    ]);
                  }
                })
                .catch((err) => {
                  onError?.(err, (file as unknown) as XMLHttpRequest);
                });
            }}
          >
            <Button>上传文件</Button>
          </Upload>
        </Item>
        <Item name="resourceHdfsPath" style={{ position: 'absolute', visibility: 'hidden' }}>
          <Input />
        </Item>
        <Item label="参数">
          <ParamList formName={['appArguments', 'argumentValue', 'argumentRemark']} />
        </Item>
        <Item name="mainClass" label="执行类" rules={[{ required: true, message: '请输入' }]}>
          <Input placeholder="请输入" size="large" style={{ width }} />
        </Item>
      </Form>
    </>
  );
};

export default forwardRef(SparkJava);
