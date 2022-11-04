import React, { useState, useRef, forwardRef, useMemo } from "react";
import { Modal, Button } from 'antd';
import type { ModalProps } from 'antd/lib/modal';
import _ from 'lodash';

interface OptionTypes {
  modalProps: ModalProps;
  handleConfirm: (T: any) => void | any;
  customConfirm?: (a: any, b: any, c: any) => void | any;
  beforeCancel: (a: any, b: any) => Promise<void>;
  btns: {
    positive: string | boolean;
    negetive: string | boolean;
    other: Record<string, any> [];
  };
  hideFooter: boolean;
}

const defaultOptions = {
  modalProps: {
    width: 700,
  },
  handleConfirm: () => Promise.resolve(),
  beforeCancel: () => Promise.resolve(),
  hideFooter: false,
  btns: {
    positive: '确定',
    negetive: '取消',
    other: []
  }
};

function useModal(Content: any, modalTitle?: string, options = {}): [(props: any) => JSX.Element, (V: boolean, T?: string) => void] {
  const [visible, setVisible] = useState(false);
  const [title, setTitle] = useState(modalTitle);
  let mergedOptions: OptionTypes = _.defaultsDeep(options, defaultOptions);
  mergedOptions.modalProps.title = title;

  const CustomModal = (props: any) => {
    const [loading, setLoading] = useState(false);
    const $form = useRef<{
      getValues?: () => Record<string, any>;
    }>();

    const handleCancel = async () => {
      try {
        await mergedOptions.beforeCancel(dialogInstance, $form.current)
        setVisible(false);
      } catch(e) {
        console.error(e);
      }
    }
  
    const showLoading = ()  => {
      setLoading(true);
    }
  
    const hideLoading = ()  => {
      setLoading(false)
    }
  
    const dialogInstance = {
      handleCancel,
      showLoading,
      hideLoading
    }
  
    const handleOk = async () => {
      if(mergedOptions?.customConfirm) {
        mergedOptions.customConfirm(dialogInstance, $form.current, handleCancel);
        return;
      }
      // 1、showLoading
      // 2、验证、接口
      // 3、关闭loading
      // 4、验证成功关闭弹窗，验证失败
      if($form.current?.getValues) {
        showLoading();
        try {
          const res = await $form.current.getValues();
          await mergedOptions?.handleConfirm(res);
          handleCancel();
        } catch(e) {
          console.error(e);
        }
        hideLoading();
      }
    }

    let footer: JSX.Element [] = [];
    if (mergedOptions?.hideFooter) {
      footer = [];
    } else {
      mergedOptions.btns.other.forEach((btn: any, index: number) => {
        footer.push(
          <Button
            key={index}
            type={btn.type}
            size={btn.size}
            loading={loading}
            onClick={() => {}}
          >{btn.title}
          </Button>
        );
      });
      if (mergedOptions.btns.negetive) {
        footer.push(
          <Button
            key="back"
            onClick={handleCancel}
          >{mergedOptions.btns.negetive}
          </Button>
        );
      }
      if (mergedOptions.btns.positive) {
        footer.push(
          <Button
            key="submit"
            type="primary"
            loading={loading}
            onClick={handleOk}
          >
            {mergedOptions.btns.positive}
          </Button>
        );
      }
    }

  const ChildComponent = useMemo(() => forwardRef(Content), [props]);
    return <Modal
      onOk={handleOk}
      onCancel={handleCancel}
      footer={footer}
      {...mergedOptions.modalProps}
      visible={visible}
    >
      <ChildComponent ref={$form} {...props} />
    </Modal>
  }

  return [
    CustomModal,
    (visible: boolean, title?: string) => {
      setVisible(visible);
      if(title) {
        setTitle(title);
      }
    }
  ];
};

export default useModal;