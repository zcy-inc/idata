import { FC, useEffect, useRef } from 'react';
import React from 'react';
import type { MonacoEditorProps } from 'react-monaco-editor';
import MonacoEditor from 'react-monaco-editor';
import Snippets from './snippets';
import { getWords } from './keywords';
import { IDisposable } from 'monaco-editor';
import { AutoCompletionLangs } from '@/constants/datadev';

interface DbSchema {
  dbName: string;
  tableNames: string[];
}

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
          columns = [],
        } = await getWords(props.language?.toUpperCase() as AutoCompletionLangs);
        const dbSchema = dbTableNames.map((_: DbSchema) => {
          return {
            dbName: _.dbName,
            tables: _.tableNames.map((t) => ({
              tblName: t,
            })),
          };
        });
        const sqlSnippets = new Snippets(
          monacoInner,
          [...basicAutocompletionTips],
          async () => {
            return columns;
          },
          null,
          dbSchema,
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
