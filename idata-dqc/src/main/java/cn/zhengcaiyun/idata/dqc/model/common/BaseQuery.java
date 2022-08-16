package cn.zhengcaiyun.idata.dqc.model.common;

/**
 * Created by zheng on 16/6/13.
 */
public class BaseQuery {
    private Integer curPage ;
    private Integer pageSize = 20;
    private Integer startIndex;

    public Integer getStartIndex() {
        if (startIndex == null || startIndex < 0) {
            if (pageSize == null || curPage == null || curPage <= 0) {
                return 0;
            }
            return pageSize * (curPage - 1);
        } else {
            return startIndex;
        }

    }

    public Integer getCurPage() {
        return curPage;
    }

    public void setCurPage(Integer curPage) {
        this.curPage = curPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }
}
