package cn.zhengcaiyun.idata.develop.dal.repo.job;

public interface JobContentSqlRepo {

    /**
     * 是否绑定了函数
     * @param udfId
     * @return
     */
    Boolean ifBindUDF(Long udfId);
}
