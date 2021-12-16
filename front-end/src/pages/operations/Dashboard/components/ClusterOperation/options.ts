import { EChartsOption } from 'echarts-for-react';
import moment from 'moment';

export const optionsPie: EChartsOption = {
  tooltip: {
    trigger: 'axis',
    backgroundColor: '#141736',
    textStyle: { color: '#fff' },
    axisPointer: {
      type: 'line',
      lineStyle: {
        color: 'rgba(0, 0, 0, 0.05)',
        type: 'solid',
        width: 24,
      },
    },
  },
  legend: {
    textStyle: { color: '#717b98' },
    right: 24,
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true,
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    axisLine: {
      lineStyle: {
        color: '#f0f1f5',
      },
    },
    axisLabel: {
      color: '#717b98',
    },
    axisTick: {
      show: false,
    },
    data: [...Array(7).keys()]
      .map((day) => moment(new Date(Date.now() - 86400000 * day)).format('MM-DD'))
      .reverse(),
  },
  yAxis: {
    type: 'value',
    max: 100,
    axisLabel: { color: '#717b98', formatter: '{value}%' },
    splitLine: {
      lineStyle: {
        type: 'dashed',
      },
    },
  },
  series: [
    {
      name: '成功率',
      type: 'line',
      data: [12, 13, 10, 13, 9, 23, 21],
      itemStyle: { color: '#05cc87' },
    },
    {
      name: '失败率',
      type: 'line',
      data: [22, 18, 19, 23, 29, 33, 31],
      itemStyle: { color: '#f1331f' },
    },
  ],
};
