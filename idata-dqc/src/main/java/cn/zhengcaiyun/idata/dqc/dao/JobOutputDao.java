package cn.zhengcaiyun.idata.dqc.dao;

import cn.zhengcaiyun.idata.dqc.model.vo.JobOutputVO;

/**
 * 数据开发-作业输出(JobOutput)表数据库访问层
 *
 * @author zheng
 * @since 2022-07-11 10:01:47
 */
public interface JobOutputDao {
    JobOutputVO getById(Long id);

    JobOutputVO getDIJobTable(Long id);

}

