package cn.zhengcaiyun.idata.portal.model.request;

public class PageWrapper<T> {

    /**
     * 当前页码
     */
    private Integer pageNum;

    /**
     * 一页条数
     */
    private Integer pageSize;

    /**
     * 筛选体
     */
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
