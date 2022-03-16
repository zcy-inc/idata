import { DAGStatus } from '@/constants/datadev';
/**
 * 生成随机[a-z]的字符串
 * @param len 字符串长度
 * @returns string
 */
export function getRandomStr(len: number) {
  let str = '';
  for (let i = 0; i < len; i++) {
    str += String.fromCharCode(Math.random() * (122 - 97) + 97);
  }
  return str;
}

export function isEnumType(type: unknown) {
  return type !== 'STRING' && type !== 'BOOLEAN';
}

export function getDAGStatus(status?: DAGStatus) {
  switch (status) {
    case DAGStatus.ON:
      return '上线';
    case DAGStatus.OFF:
      return '下线';
    default:
      return '未上线';
  }
}
