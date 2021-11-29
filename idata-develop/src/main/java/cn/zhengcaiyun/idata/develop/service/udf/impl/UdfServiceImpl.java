package cn.zhengcaiyun.idata.develop.service.udf.impl;

import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.commons.enums.DeleteEnum;
import cn.zhengcaiyun.idata.commons.exception.GeneralException;
import cn.zhengcaiyun.idata.develop.dal.dao.DevJobUdfDao;
import cn.zhengcaiyun.idata.develop.dal.model.DevJobUdf;
import cn.zhengcaiyun.idata.develop.service.udf.UdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UdfServiceImpl implements UdfService {

    @Autowired
    private DevJobUdfDao devUdfDao;


    @Override
    public Boolean delete(Long id) {
        DevJobUdf udf = new DevJobUdf();
        udf.setEditTime(new Date());
        udf.setEditor(OperatorContext.getCurrentOperator().getNickname());
        udf.setDel(DeleteEnum.DEL_NO.val);
        udf.setId(id);
        devUdfDao.updateByPrimaryKeySelective(udf);
        return true;
    }

    @Override
    public Long add(DevJobUdf udf) {
        devUdfDao.insertSelective(udf);
        return udf.getId();
    }

    @Override
    public Boolean update(DevJobUdf udf) {
        devUdfDao.updateByPrimaryKey(udf);
        return true;
    }

    @Override
    public DevJobUdf findById(Long id) {
        return devUdfDao.selectByPrimaryKey(id).orElseThrow(() -> new GeneralException(id + "不存在"));
    }
}
