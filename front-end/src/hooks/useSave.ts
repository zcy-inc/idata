import { useState } from 'react';
import { message } from 'antd';

export default <R>(
  service: () => Promise<Tresponse<R>>,
  cb?: (result?: Tresponse<R>) => void,
) => {
  const [loading, setLoadinng] = useState(false);
  const run = async () => {
    setLoadinng(true);
    const result = await service();
    setLoadinng(false);
    message.success('保存成功');
    cb?.(result);
    return result;
  };

  return {
    run,
    loading,
    btnProps: { onClick: run, loading },
  };
};
