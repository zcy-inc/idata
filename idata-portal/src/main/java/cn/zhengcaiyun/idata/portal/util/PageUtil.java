package cn.zhengcaiyun.idata.portal.util;

import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobHistory;
import cn.zhengcaiyun.idata.develop.dto.job.JobHistoryDto;
import cn.zhengcaiyun.idata.portal.model.response.ops.JobHistoryResponse;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    /**
     * PageInfo<> 分页数据实际类型转换
     * @param s
     * @param fun
     * @param <T>
     * @param <E>
     * @return
     */
    public static <T, E> PageInfo<JobHistoryResponse> convertType(PageInfo<T> s, Function<T, E> fun) {
        PageInfo pageInfo = new PageInfo();
        BeanUtils.copyProperties(s, pageInfo);
        List<E> responseList = s.getList().stream().map(e -> fun.apply(e)).collect(Collectors.toList());
        pageInfo.setList(responseList);
        return pageInfo;
    }

}
