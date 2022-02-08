import { EChartsOption } from 'echarts-for-react';

export const optionsPie: EChartsOption = {
  legend: {
    orient: 'vertical',
    right: 24,
    bottom: 24,
    itemHeight: 9,
    itemWidth: 9,
    textStyle: { color: '#717b98' },
  },
  series: [
    {
      name: 'JobSchedulState',
      type: 'pie',
      radius: ['48%', '70%'],
      center: ['40%', '50%'],
      label: { formatter: '{b}: {c}', color: '#717b98' },
      labelLine: { smooth: true },
      itemStyle: { borderColor: '#fff', borderWidth: 1 },
      data: [
        { value: 1048, name: '成功', itemStyle: { color: '#05cc87' } },
        { value: 735, name: '等待运行', itemStyle: { color: '#304ffe' } },
        { value: 580, name: '运行中', itemStyle: { color: '#ffc027' } },
        { value: 484, name: '失败', itemStyle: { color: '#f1331f' } },
        { value: 300, name: '其他', itemStyle: { color: '#565b64' } },
      ],
    },
  ],
};
