export function dataToList(data: Record<string, any>) {
  return Object.keys(data).map(configValueKey => {
    const {configValue,configValueRemarks}=data[configValueKey];
    return {
      configValueKey,
      configValue,
      configValueRemarks
    }
  })
}
