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
