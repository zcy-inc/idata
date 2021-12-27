import { getAutocompletionTipConfigs }  from '@/services/global'

const getWordsSingle = (()=>{
  let words
  return async()=>{
        if(words){
          return words
        }
        const res = await getAutocompletionTipConfigs()
        return res?.data;
  }
})()
export const getWords = async()=>{
      const words =  await getWordsSingle();
      return words
}


