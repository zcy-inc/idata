import { FC, useEffect, useRef } from 'react';
import React from 'react';
import type { MonacoEditorProps } from 'react-monaco-editor';
import MonacoEditor from 'react-monaco-editor';
import Snippets from './snippets';
import { getWords } from './keywords';
import { IDisposable } from 'monaco-editor';
import { AutoCompletionLangs } from '@/constants/datadev';

const defaultProps = {
  height: '100%',
  width: '100%',
  language: 'sql',
  theme: 'vs-dark',
  options: {
    automaticLayout: true,
    suggestOnTriggerCharacters: true,
  },
};
const SqlEditor: FC<
  MonacoEditorProps & {
    formRef: any;
  }
> = (props) => {
  const monacoRef = useRef<any>();
  const monacoProviderRef = useRef<IDisposable>();

  useEffect(() => {
    return () => {
      monacoProviderRef.current?.dispose();
      monacoRef.current?.dispose();
    };
  }, []);

  return (
    <MonacoEditor
      {...defaultProps}
      {...props}
      language={props.language}
      ref={props?.formRef}
      options={{
        automaticLayout: true,
        suggestOnTriggerCharacters: true,
      }}
      editorDidMount={async (editor, monacoInner) => {
        const {
          basicAutocompletionTips,
          dbTableNames = [],
          columnName = [],
        } = await getWords(props.language?.toUpperCase() as AutoCompletionLangs);
        const dbName = dbTableNames[0]?.split(',')?.[0];
        const tblName = dbTableNames[0]?.split(',')?.[1];
        const sqlSnippets = new Snippets(
          monacoInner,
          [...basicAutocompletionTips, ...dbTableNames, ...columnName],
          async () => columnName,
          null,
          [
            {
              dbName,
              tables: [{ tblName, tableColumns: columnName }],
            },
          ],
        );
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
