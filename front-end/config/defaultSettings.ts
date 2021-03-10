import { Settings as LayoutSettings } from '@ant-design/pro-layout';

const Settings: LayoutSettings & {
  pwa?: boolean;
  logo?: string;
} = {
  headerHeight: 52,
  navTheme: 'light',
  primaryColor: '#304FFE',
  layout: 'mix',
  contentWidth: 'Fluid',
  fixedHeader: false,
  fixSiderbar: true,
  colorWeak: false,
  title: 'IData',
  pwa: false,
  logo: 'https://sitecdn.zcycdn.com/f2e-assets/d64f1b62-87ab-47ea-8e14-2ab710914b1c.png',
  iconfontUrl: '//at.alicdn.com/t/font_2310080_1i868rl2a12.js',
};

export default Settings;
