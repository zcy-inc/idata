package cn.zhengcaiyun.idata.dqc.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class TableVO {
    private String tableName;
    private String owner;
    private String tableAlias;
    private Boolean partitioned;
    private List<LableVO> lableList;
}
