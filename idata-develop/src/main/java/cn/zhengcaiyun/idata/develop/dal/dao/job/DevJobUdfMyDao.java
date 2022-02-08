package cn.zhengcaiyun.idata.develop.dal.dao.job;

import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobUdf;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DevJobUdfMyDao {

    List<String> getConcatHdfsPath(List<Long> idList);

    List<DevJobUdf> getByIds(List<Long> idList);
}
