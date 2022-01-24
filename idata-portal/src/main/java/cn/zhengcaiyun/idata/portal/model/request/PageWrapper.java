package cn.zhengcaiyun.idata.portal.model.request;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("分页wrapper")
public class PageWrapper<T> {

    @ApiModelProperty("当前页码")
    private Integer pageNum;

    @ApiModelProperty("一页条数")
    private Integer pageSize;

    @ApiModelProperty("筛选体")
    private T condition;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public T getCondition() {
        return condition;
    }

    public void setCondition(T condition) {
        this.condition = condition;
    }
}
