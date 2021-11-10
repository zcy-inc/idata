export type TSubjectType = "TABLE" | "COLUMN" ;

export interface ILabelDefines {
  labelName: string
  labelCode: string
  labelRequired: boolean
  labelTag: "ENUM_VALUE_LABEL"|"STRING_LABEL"|"BOOLEAN_LABEL"
}
