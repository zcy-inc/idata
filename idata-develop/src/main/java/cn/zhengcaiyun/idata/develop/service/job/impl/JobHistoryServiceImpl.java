package cn.zhengcaiyun.idata.develop.service.job.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.PageUtil;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobHistoryDao;
import cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobHistoryMyDao;
import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobHistory;
import cn.zhengcaiyun.idata.develop.service.job.JobHistoryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobHistoryDynamicSqlSupport.devJobHistory;
import static org.mybatis.dynamic.sql.SqlBuilder.isNotEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.select;

@Service
public class JobHistoryServiceImpl implements JobHistoryService {

    @Autowired
    private DevJobHistoryDao devJobHistoryDao;

    @Autowired
    private DevJobHistoryMyDao devJobHistoryMyDao;

    @Override
    public void batchUpsert(List<DevJobHistory> devJobHistoryList) {
        devJobHistoryMyDao.batchUpsert(devJobHistoryList);
    }

    @Override
    public PageInfo<DevJobHistory> pagingJobHistory(Long id, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        var builder = select(devJobHistory.allColumns()).from(devJobHistory).where(devJobHistory.jobId, isNotEqualTo(id));
        List<DevJobHistory> list = devJobHistoryDao.selectMany(builder.build().render(RenderingStrategies.MYBATIS3));
        PageInfo<DevJobHistory> pageInfo = new PageInfo<>(list);
        
        return pageInfo;
    }

}
