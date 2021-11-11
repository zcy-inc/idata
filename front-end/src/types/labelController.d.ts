export type TSubjectType = "TABLE" | "COLUMN" ;

export interface ILabelDefines {
  id?: number
  labelName?: string
  labelCode?: string
  labelRequired?: 1|0
  subjectType?: TSubjectType
  labelTag?: "ENUM_VALUE_LABEL"|"STRING_LABEL"|"BOOLEAN_LABEL"
}
