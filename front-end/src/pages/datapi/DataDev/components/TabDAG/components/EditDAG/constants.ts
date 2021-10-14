export const monthOptions = [
  { label: '一月', value: '1' },
  { label: '二月', value: '2' },
  { label: '三月', value: '3' },
  { label: '四月', value: '4' },
  { label: '五月', value: '5' },
  { label: '六月', value: '6' },
  { label: '七月', value: '7' },
  { label: '八月', value: '8' },
  { label: '九月', value: '9' },
  { label: '十月', value: '10' },
  { label: '十一月', value: '11' },
  { label: '十二月', value: '12' },
];

export const weekOptions = [
  { label: '星期一', value: 'MON' },
  { label: '星期二', value: 'TUE' },
  { label: '星期三', value: 'WED' },
  { label: '星期四', value: 'THU' },
  { label: '星期五', value: 'FRI' },
  { label: '星期六', value: 'SAT' },
  { label: '星期日', value: 'SUN' },
];

export const dayOptions = [...Array(31)].map((_, i) => ({
  label: `${i + 1}号`,
  value: `${i + 1}`,
}));

export const hourOptions = [...Array(24)].map((_, i) => ({
  label: `${i}时`,
  value: i,
}));
