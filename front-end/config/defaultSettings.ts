import { Settings as LayoutSettings } from '@ant-design/pro-layout';

const Settings: LayoutSettings & {
  pwa?: boolean;
  logo?: string;
} = {
  headerHeight: 58,
  navTheme: 'light',
  primaryColor: '#304FFE',
  layout: 'mix',
  contentWidth: 'Fluid',
  fixedHeader: true,
  fixSiderbar: true,
  colorWeak: false,
  title: '',
  pwa: false,
  logo: 'https://sitecdn.zcycdn.com/f2e-assets/98aefdbd-ae05-428a-b954-44c10b303f94.svg',
  iconfontUrl: '//at.alicdn.com/t/font_2411167_841hs576aa7.js',
};

export default Settings;
