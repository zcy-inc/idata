package cn.zhengcaiyun.idata.portal.util;

import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobHistory;
import com.github.pagehelper.PageInfo;

import java.util.List;

public class PageUtil {

    /**
     * 将pageHelper的pageInfo转换成业务的Page BO
     * @param pageInfo
     * @param <T>
     * @return
     */
    public static <T> Page<T> covertMine(PageInfo<T> pageInfo) {
        Page<T> page = new Page<>();
        page.setContent(pageInfo.getList());
        page.setTotal(pageInfo.getTotal());
        page.setPageNum(pageInfo.getPageNum());
        page.setPageSize(pageInfo.getSize());
        page.setPages(pageInfo.getPages());
        return page;
    }
}
