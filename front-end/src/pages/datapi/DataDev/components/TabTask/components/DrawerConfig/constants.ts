import { SchRerunMode } from '@/constants/datadev';

export const restartOptions = [
  { label: '皆可重跑', value: SchRerunMode.ALWAYS },
  { label: '失败重跑', value: SchRerunMode.FAILED },
  { label: '皆不重跑', value: SchRerunMode.NEVER },
];

export const concurrentOptions = [...Array(20).keys()].map((_) => ({
  label: _ + 1,
  value: _ + 1,
}));
