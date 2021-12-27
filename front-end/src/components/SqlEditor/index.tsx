import type { FC } from 'react';
import React from 'react';
import type { MonacoEditorProps } from 'react-monaco-editor';
import MonacoEditor from 'react-monaco-editor';
import Snippets from './snippets'
import { getWords } from './keywords';

const defaultProps = {
  height: "400",
  language: "sql",
  theme: "vs-dark",
  options: { automaticLayout: true, quickSuggestions: false }
};
const SqlEditor: FC<MonacoEditorProps> = (props) => {
  return (<MonacoEditor
    {...defaultProps}
    {...props}
    options={{
      suggestOnTriggerCharacters: true
    }}


    editorDidMount={async (editor, monacoInner) => {
      const { basicAutocompletionTips, dbTableNames=[], columnNames=[]} = await getWords();
      const sqlSnippets = new Snippets(
        monacoInner,
        [...basicAutocompletionTips,...dbTableNames,...columnNames]
        );
      monacoInner.languages.registerCompletionItemProvider(
        'sql',
        {
          triggerCharacters: [' '],
          provideCompletionItems: (model, position) => {
            const suggestions = sqlSnippets.provideCompletionItems(model, position);
            return suggestions;
          }
        },
      );
    }}
  />)
}
export default SqlEditor
