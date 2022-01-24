import { AutoCompletionLangs } from '@/constants/datadev';
import { getAutocompletionTipConfigs } from '@/services/global';

const getWordsSingle = (() => {
  let words: any;
  return async (language: AutoCompletionLangs) => {
    if (words) {
      return words;
    }
    const res = await getAutocompletionTipConfigs({ autocompletionType: language });
    return res?.data;
  };
})();
export const getWords = async (language: AutoCompletionLangs) => {
  const words = await getWordsSingle(language);
  return words;
};
