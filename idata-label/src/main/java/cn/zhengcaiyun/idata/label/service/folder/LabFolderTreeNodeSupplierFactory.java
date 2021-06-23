package cn.zhengcaiyun.idata.label.service.folder;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @description: 工具类
 * @author: yangjianhua
 * @create: 2021-06-22 11:51
 **/
public class LabFolderTreeNodeSupplierFactory {

    private static final Map<String, LabFolderTreeNodeSupplier> supplierMap = Maps.newConcurrentMap();

    public static void register(String belong, LabFolderTreeNodeSupplier supplier) {
        supplierMap.put(belong, supplier);
    }

    public static LabFolderTreeNodeSupplier getSupplier(String belong) {
        if (StringUtils.isEmpty(belong)) return null;
        return supplierMap.get(belong);
    }
}
