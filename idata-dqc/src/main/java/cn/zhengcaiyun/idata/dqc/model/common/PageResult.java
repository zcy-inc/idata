package cn.zhengcaiyun.idata.dqc.model.common;

import lombok.Data;

/**
 * Created by zheng on 17/7/18.
 */
@Data
public class PageResult<T> {
    private T data;
    private Integer totalElements = 0;
    private Integer pageSize = 20;
    private Integer curPage = 1;
    private Integer totalPages = 1;

    public Integer getTotalPages() {
        if (totalElements == null || totalElements <= 0) {
            return 0;
        }

        if (pageSize == null || pageSize <= 0) {
            return 0;
        }

        return totalElements % pageSize == 0 ? (totalElements / pageSize) : (totalElements / pageSize + 1);
    }

    public Integer getCurPage() {
        if(curPage == null){
            return 1;
        }
        return curPage;
    }


}