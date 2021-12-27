import { FC, useEffect, useRef } from 'react';
import React from 'react';
import type { MonacoEditorProps } from 'react-monaco-editor';
import MonacoEditor from 'react-monaco-editor';
import Snippets from './snippets';
import { getWords } from './keywords';
import { IDisposable } from 'monaco-editor';

const defaultProps = {
  height: '100%',
  width: '100%',
  language: 'sql',
  theme: 'vs-dark',
  options: { automaticLayout: true, quickSuggestions: false },
};
const SqlEditor: FC<
  MonacoEditorProps & {
    formRef: any;
  }
> = (props) => {
  const monacoRef = useRef<any>();
  const monacoProviderRef = useRef<IDisposable>();

  useEffect(() => {
    console.log(props);
    return () => {
      monacoProviderRef.current?.dispose();
      monacoRef.current?.dispose();
    };
  }, []);

  return (
    <MonacoEditor
      {...defaultProps}
      {...props}
      ref={props?.formRef}
      options={{
        automaticLayout: true,
        quickSuggestions: false,
        suggestOnTriggerCharacters: true,
      }}
      editorDidMount={async (editor, monacoInner) => {
        const { basicAutocompletionTips, dbTableNames = [], columnNames = [] } = await getWords();
        const sqlSnippets = new Snippets(monacoInner, [
          ...basicAutocompletionTips,
          ...dbTableNames,
          ...columnNames,
        ]);
        monacoRef.current = editor;
        monacoProviderRef.current = monacoInner.languages.registerCompletionItemProvider('sql', {
          triggerCharacters: [' '],
          provideCompletionItems: (model, position) => {
            const suggestions = sqlSnippets.provideCompletionItems(model, position);
            return suggestions;
          },
        });
      }}
    />
  );
};
export default SqlEditor;
