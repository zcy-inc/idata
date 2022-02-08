package cn.zhengcaiyun.idata.develop.service.job.impl;

import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.commons.enums.DeleteEnum;
import cn.zhengcaiyun.idata.commons.exception.GeneralException;
import cn.zhengcaiyun.idata.commons.exception.NameDuplicateException;
import cn.zhengcaiyun.idata.develop.cache.DevTreeNodeLocalCache;
import cn.zhengcaiyun.idata.develop.constant.enums.FunctionModuleEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobContentSqlDao;
import cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobUdfDao;
import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobUdf;
import cn.zhengcaiyun.idata.develop.event.job.publisher.JobEventPublisher;
import cn.zhengcaiyun.idata.develop.service.access.DevAccessService;
import cn.zhengcaiyun.idata.develop.service.job.JobContentCommonService;
import cn.zhengcaiyun.idata.develop.service.job.JobUdfService;
import cn.zhengcaiyun.idata.system.dto.ResourceTypeEnum;
import cn.zhengcaiyun.idata.user.service.UserAccessService;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobUdfDynamicSqlSupport.devJobUdf;
import static com.google.common.base.Preconditions.checkArgument;
import static org.mybatis.dynamic.sql.SqlBuilder.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

@Service
public class JobUdfServiceImpl implements JobUdfService {

    @Autowired
    private DevJobUdfDao devUdfDao;

    @Autowired
    private JobContentCommonService jobContentCommonService;

    @Autowired
    private DevTreeNodeLocalCache devTreeNodeLocalCache;

    @Autowired
    private DevAccessService devAccessService;

    @Override
    public Boolean delete(Long id) throws IllegalAccessException {
        //校验是否绑定了函数，无法删除
        Boolean binded = jobContentCommonService.ifBindUDF(id);
        if (binded) {
            throw new GeneralException("该函数已绑定了SQL作业");
        }

        DevJobUdf devJobUdf = devUdfDao.selectByPrimaryKey(id).orElseThrow(() -> new IllegalArgumentException("数据不存在"));
        devAccessService.checkDeleteAccess(OperatorContext.getCurrentOperator().getId(), devJobUdf.getFolderId());

        //已删除的直接返回
        if (devJobUdf.getDel() == DeleteEnum.DEL_YES.val) {
            return true;
        }

        String udfName = devJobUdf.getUdfName();
        DevJobUdf udf = new DevJobUdf();
        udf.setEditTime(new Date());
        udf.setEditor(OperatorContext.getCurrentOperator().getNickname());
        udf.setDel(DeleteEnum.DEL_YES.val);
        // 此处修改name是为了防止唯一键冲突
        udf.setUdfName(String.format("%s[delete_%s]", udfName, id));
        udf.setId(id);
        devUdfDao.updateByPrimaryKeySelective(udf);

        devTreeNodeLocalCache.invalidate(FunctionModuleEnum.DEV_FUN);
        return true;
    }

    @Override
    public Long add(DevJobUdf udf) {
        try {
            devUdfDao.insertSelective(udf);
            devTreeNodeLocalCache.invalidate(FunctionModuleEnum.DEV_FUN);
        } catch (DuplicateKeyException duplicateKeyException) {
            throw new NameDuplicateException("UDF名：" + udf.getUdfName() + " 重复");
        }
        return udf.getId();
    }

    @Override
    public Boolean update(DevJobUdf udf) {
        try {
            devUdfDao.updateByPrimaryKey(udf);
        } catch (DuplicateKeyException duplicateKeyException) {
            throw new GeneralException("UDF名：" + udf.getUdfName() + " 重复");
        }
        return true;
    }

    @Override
    public DevJobUdf findById(Long id) {
        return devUdfDao.selectByPrimaryKey(id).orElseThrow(() -> new GeneralException(id + "不存在"));
    }

    @Override
    public List<DevJobUdf> load() {
        return devUdfDao.selectMany(select(devJobUdf.allColumns())
                .from(devJobUdf)
                .where(devJobUdf.del, isNotEqualTo(1))
                .build().render(RenderingStrategies.MYBATIS3));
    }
}
