package cn.zhengcaiyun.idata.label.service.folder;

import cn.zhengcaiyun.idata.label.dto.LabFolderTreeNodeDto;

import java.util.List;
import java.util.function.Supplier;

/**
 * @description: 文件树节点提供者接口，由各业务模块实现，提供业务树节点数据
 * @author: yangjianhua
 * @create: 2021-06-22 11:40
 **/
public interface LabFolderTreeNodeSupplier extends Supplier<List<LabFolderTreeNodeDto>> {

}
