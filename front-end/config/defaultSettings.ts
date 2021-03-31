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
  title: '',
  pwa: false,
  logo: 'https://sitecdn.zcycdn.com/f2e-assets/8e7350f7-a0de-460b-b2ba-a861eab56f06.svg',
  iconfontUrl: '//at.alicdn.com/t/font_2310080_1i868rl2a12.js',
};

export default Settings;
