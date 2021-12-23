export interface DefaultResponse {
  success: boolean;
  code: string;
  msg: string;
  causeMsg: string;
}

export interface IMenuItem {
  name: string;
  enable: boolean;
  featureCode: string;
  children: IMenuItem[];
}

export interface  IAutocompletionTipConfigs{
  basicAutocompletionTips: string[]
  dbTableNames: string[]
  columnNames: string[]
}
