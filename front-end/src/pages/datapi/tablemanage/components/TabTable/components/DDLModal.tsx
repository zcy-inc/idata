import React, { useState } from 'react';
import { Modal } from 'antd';
import { Controlled as CodeMirror } from 'react-codemirror2';
import type { FC } from 'react';
import 'codemirror/mode/sql/sql';
import 'codemirror/lib/codemirror.css';
import 'codemirror/theme/rubyblue.css';

interface DDLModalProps {
  visible: boolean;
  onCancel: () => void;
}

const DDLModal: FC<DDLModalProps> = ({ visible, onCancel }) => {
  const [v, setV] = useState(
    "create external table `ads`.`ads_tax_efficiency_options`(`data_type` STRING comment '数据类型',`code` STRING comment '选项对应的枚举值',`value` STRING comment '选项对应的名称') comment '国税效能报表下拉框选项数据表' stored as orc location 'hdfs://nameservice1/hive/ads.db/ads_tax_efficiency_options' ",
  );

  return (
    <Modal title="DDL模式" visible={visible} onCancel={onCancel} width={800}>
      <CodeMirror
        value={v}
        options={{
          tabSize: 4,
          mode: 'sql',
          theme: 'rubyblue',
          lineNumbers: true,
          line: true,
        }}
        onBeforeChange={(editor, data, value) => {
          setV(value);
        }}
        onChange={(editor, data, value) => {}}
      />
    </Modal>
  );
};

export default DDLModal;
