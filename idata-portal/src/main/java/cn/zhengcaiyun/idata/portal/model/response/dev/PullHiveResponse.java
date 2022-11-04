package cn.zhengcaiyun.idata.portal.model.response.dev;

import cn.zhengcaiyun.idata.connector.spi.hive.dto.CompareInfoNewDTO;
import cn.zhengcaiyun.idata.develop.dto.table.ColumnInfoDto;

import java.util.List;

/**
 * @author zhanqian
 * @date 2022/6/21 3:05 PM
 * @description
 */
public class PullHiveResponse {

    private CompareInfoNewDTO compareInfoNewDTO;

    private List<ColumnInfoDto> columnInfoDtoList;

    public CompareInfoNewDTO getCompareInfoNewDTO() {
        return compareInfoNewDTO;
    }

    public void setCompareInfoNewDTO(CompareInfoNewDTO compareInfoNewDTO) {
        this.compareInfoNewDTO = compareInfoNewDTO;
    }

    public List<ColumnInfoDto> getColumnInfoDtoList() {
        return columnInfoDtoList;
    }

    public void setColumnInfoDtoList(List<ColumnInfoDto> columnInfoDtoList) {
        this.columnInfoDtoList = columnInfoDtoList;
    }
}
