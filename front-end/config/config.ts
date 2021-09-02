// https://umijs.org/config/
import { defineConfig } from 'umi';
import defaultSettings from './defaultSettings';
import proxy from './proxy';
import routes from './routes';

const MonacoWebpackPlugin = require('monaco-editor-webpack-plugin');

const { REACT_APP_ENV } = process.env;

export default defineConfig({
  hash: true,
  history: { type: 'hash' },
  publicPath: '/idata-portal/',
  antd: {},
  favicon: 'https://sitecdn.zcycdn.com/zcy/desktop/media/img/favicon.ico',
  dva: {
    hmr: true,
  },
  layout: {
    siderWidth: 288,
    ...defaultSettings,
  },
  locale: {
    default: 'zh-CN',
    antd: true,
    // default true, when it is true, will use `navigator.language` overwrite default
    baseNavigator: true,
  },
  dynamicImport: {
    loading: '@ant-design/pro-layout/es/PageLoading',
  },
  targets: {
    ie: 11,
  },
  // umi routes: https://umijs.org/docs/routing
  routes,
  // Theme for antd: https://ant.design/docs/react/customize-theme-cn
  theme: {
    'primary-color': '#304FFE',
    'success-color': '#05cc87',
    'warning-color': '#ff9324',
    'error-color': '#ff5753',
    // 'disabled-color': '#eeeff2',

    'text-color': '#2d3956',
    'border-color-base': '#ebedf3',
    'input-placeholder-color': '#bfc4d5',
    'background-color-light': '#f8f8fa',

    'border-radius-base': '4px',
  },
  esbuild: {},
  title: false,
  ignoreMomentLocale: true,
  proxy: proxy[REACT_APP_ENV || 'dev'],
  manifest: {
    basePath: '/',
  },
  // https://github.com/zthxxx/react-dev-inspector
  plugins: ['react-dev-inspector/plugins/umi/react-inspector'],
  inspectorConfig: {
    // loader options type and docs see below
    exclude: [],
    babelPlugins: [],
    babelOptions: {},
  },
  resolve: {
    includes: ['src/components'],
  },
  chainWebpack(memo, { env, webpack, createCSSRule }) {
    // 设置 alias
    memo.resolve.alias.set('foo', '/tmp/a/b/foo');
    memo.plugin('monaco-editor').use(MonacoWebpackPlugin, [{ languages: ['sql'] }]);
  },
});
