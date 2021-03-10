/**
 * 在生产环境 代理是无法生效的，所以这里没有生产环境的配置
 * The agent cannot take effect in the production environment
 * so there is no configuration of the production environment
 * For details, please see
 * https://pro.ant.design/docs/deploy
 */
export default {
  dev: {
    '/zcy/api/**/*': {
      target: 'http://idata-staging.cai-inc.com/',
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
