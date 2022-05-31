import React, { useState, useRef, forwardRef } from 'react';
import ReactDOM from 'react-dom';
import { Button, Modal } from 'antd';
import type { ModalProps } from 'antd'; 

interface Attrs {
  modalProps?: ModalProps;
  formProps?: any;
  beforeConfirm?: (dialog: any, form: any, done: () => void) => void;
  beforeHide?: (dialog: any, form: any, done: () => void) => void;
  render?: () => void;
  hideFooter?: boolean;
  btns?: {
    positive?: string | boolean,
    negetive?: string | boolean,
    other?: Record<string, any>[]
  }
}
const defaultAttrs = {
  modalProps: {
    width : 700
  },
  beforeConfirm: (dialog: any, form: any, done: () => void) => {
    done();
  },
  beforeHide(dialog: any, form: any, done: () => void) {
    done();
  },
  btns: {
    positive: '确定',
    negetive: '取消',
    other: []
  },
  hideFooter: false,
  title: ''
};

export default (title = '对话框', customAttrs: Attrs, DialogContent: any) => {
  const tempDom = document.createElement('div');
  document.body.appendChild(tempDom);

  if (customAttrs.render) {
    DialogContent = customAttrs;
  }

  customAttrs = { ...defaultAttrs, ...customAttrs };

  customAttrs.modalProps = customAttrs.modalProps || { title: ''};
  customAttrs.modalProps.title = title;

  const ChildComponent = forwardRef(DialogContent);

  return new Promise(resolve => {
    const ZcyDialog = () => {
      const [visible, setVisible] = useState(true);
      const [loading, setLoading] = useState(false);
      const $form = useRef();

      const handleCancel = () => {
        console.log(123213);
        customAttrs.beforeHide && customAttrs.beforeHide(dialogInstance, $form.current, () => {
          setVisible(false)
        });
      }

      const showLoading = ()  => {
       setLoading(true)
      }

      const hideLoading = ()  => {
        setLoading(false)
      }

      const dialogInstance = {
        handleCancel,
        showLoading,
        hideLoading
      }

      const handleOk = () => {
        customAttrs.beforeConfirm && customAttrs.beforeConfirm(dialogInstance, $form.current, () => {
          handleCancel();
          confirm();
        });
      }

      const confirm = () => {
        resolve($form.current);
      }

      let footer: JSX.Element [] = [];
      if (customAttrs.hideFooter) {
        footer = [];
      } else {
        customAttrs.btns =  customAttrs.btns || {};
        customAttrs.btns.other = customAttrs.btns.other || [];
        customAttrs.btns.other.forEach((btn: any, index: number) => {
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
        if (customAttrs.btns.negetive) {
          footer.push(
            <Button
              key="back"
              onClick={handleCancel}
            >{customAttrs.btns.negetive}
            </Button>
          );
        }
        if (customAttrs.btns.positive) {
          footer.push(
            <Button
              key="submit"
              type="primary"
              loading={loading}
              onClick={handleOk}
            >
              {customAttrs.btns.positive}
            </Button>
          );
        }
      }
      return <Modal
        visible={visible}
        onCancel={handleCancel}
        footer={footer?.length ? footer : false}
        onOk={confirm}
        {...customAttrs.modalProps}
      >
      <ChildComponent ref={$form} {...customAttrs.formProps} dialog={dialogInstance} />
    </Modal>
    }
    ReactDOM.render(
      <ZcyDialog />,
      tempDom
    );
  })
}