package cn.zhengcaiyun.idata.user.service.auth.impl;

import cn.zhengcaiyun.idata.core.spi.loader.ServiceProvidersLoader;
import cn.zhengcaiyun.idata.core.spi.loader.ServiceProvidersLoaders;
import cn.zhengcaiyun.idata.user.constant.enums.AuthResourceTypeEnum;
import cn.zhengcaiyun.idata.user.service.auth.AuthResourceService;
import cn.zhengcaiyun.idata.user.spi.AuthResourceSupplier;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthResourceServiceImpl implements AuthResourceService {

    private final ServiceProvidersLoader serviceProvidersLoader;

    @Autowired
    public AuthResourceServiceImpl(ServiceProvidersLoader serviceProvidersLoader) {
        this.serviceProvidersLoader = serviceProvidersLoader;
    }

    @Override
    public List<String> fetchAuthResource(AuthResourceTypeEnum resourceType, JSONObject paramJson) {
        if (AuthResourceTypeEnum.tables == resourceType) {
            AuthResourceSupplier resourceSupplier = ServiceProvidersLoaders.loadProviderIfPresent(serviceProvidersLoader, AuthResourceSupplier.class, "hive");
            return resourceSupplier.supplyResources(paramJson);
        }
        return Lists.newArrayList();
    }
}
