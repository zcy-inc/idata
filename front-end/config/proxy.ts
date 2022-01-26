/**
 * 在生产环境 代理是无法生效的，所以这里没有生产环境的配置
 * The agent cannot take effect in the production environment
 * so there is no configuration of the production environment
 * For details, please see
 * https://pro.ant.design/docs/deploy
 */
export default {
  dev: {
    '/api/**/*': {
      target: 'http://idata-staging.cai-inc.com/',
      // target: 'http://idata.cai-inc.com/',
      // target: 'http://10.201.81.101:9527', // shiguang
      // target: 'http://10.201.34.135:9527', // yiran
      // target: 'http://10.201.123.94:9527'  // beisheng
      changeOrigin: true,
      pathRewrite: { '^': '' },
    },
    // '/api/': {
    //   target: 'http://idata-staging.cai-inc.com/',
    //   // target: 'http://10.201.63.219:2000',
    //   changeOrigin: true,
    //   pathRewrite: { '^': '' },
    //   // pathRewrite: { '^/api/v1/idata': '' },
    // },
  },
  test: {
    '/api/': {
      target: 'https://preview.pro.ant.design',
      changeOrigin: true,
      pathRewrite: { '^': '' },
    },
  },
  pre: {
    '/api/': {
      target: 'your pre url',
      changeOrigin: true,
      pathRewrite: { '^': '' },
    },
  },
};
