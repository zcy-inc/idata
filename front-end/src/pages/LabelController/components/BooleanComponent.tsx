import type {RadioChangeEvent } from 'antd';
import { Radio } from 'antd';
import type {FC} from 'react';
import React from 'react';
interface IDynamicFormProps {
  value?: boolean;
  onChange?: (value: boolean) => void;
}
const BooleanComponent: FC<IDynamicFormProps>= (props) => {
  const {value=false,onChange,...other} = props;
  const onValueChange = (e: RadioChangeEvent) => {
    onChange?.(e.target.value);
  };

  return (
    <Radio.Group onChange={onValueChange} value={value} {...other}>
      <Radio value={true}>是</Radio>
      <Radio value={false}>否</Radio>
    </Radio.Group>
  );
};
export default BooleanComponent
