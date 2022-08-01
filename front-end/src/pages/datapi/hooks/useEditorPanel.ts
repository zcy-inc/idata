import { useState, useRef } from 'react';
import { EditorPanelProps } from '../components/EditorPanel';
import type { IRange } from 'monaco-editor';

const collapsedSize = 'calc(100% - 40px)';
const defaultExpandedSize = 'calc(100% - 200px)';

export const useEditorPanel = () => {
  const [log, setLog] = useState<string[]>([]); // 日志
  const [results, setResults] = useState<Record<string, unknown>[][]>([]); // 执行结果
  const [resultHeader, setResultHeader] = useState<string [][]>([]); // 执行结果
  const [size, setSize] = useState<number | string>(collapsedSize);
  const [expand, setExpand] = useState(false);
  const editorRef = useRef<any>(); // monaco实例

  const handleExpandChange = (val?: boolean) => {
    let newExpand = val;
    if (typeof newExpand !== 'boolean') {
      newExpand = !expand;
    }
    if (newExpand) {
      setSize(defaultExpandedSize);
    } else {
      setSize(collapsedSize);
    }
    setExpand(newExpand);
  };

  // 获取调试代码
  const getDebugCode = () => {
    const editor = editorRef.current?.editor;
    const range = editor?.getSelection() as IRange;
    let value = editor?.getModel()?.getValueInRange(range);
    if (!value) {
      value = editor?.getValue();
    }
    return value;
  };

  // 移除执行结果
  const removeResult = (i: number) => {
    setResults([...results.slice(0, i), ...results.slice(i+1)]);
    setResultHeader([...resultHeader.slice(0, i), ...resultHeader.slice(i+1)]);
  };
  const panelProps: EditorPanelProps = {
    expand,
    log,
    size,
    setSize,
    results,
    resultHeader,
    handleExpandChange,
    removeResult,
    maxSize: collapsedSize,
  };

  return {
    panelProps,
    editorRef,
    setLog,
    setResults,
    setResultHeader,
    handleExpandChange,
    getDebugCode,
  };
};
