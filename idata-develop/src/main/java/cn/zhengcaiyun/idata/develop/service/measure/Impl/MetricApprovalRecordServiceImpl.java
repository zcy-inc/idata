package cn.zhengcaiyun.idata.develop.service.measure.Impl;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.commons.enums.DeleteEnum;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.develop.condition.metric.MetricApprovalRecordCondition;
import cn.zhengcaiyun.idata.develop.constant.enums.MetricApprovalStatusEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.MetricStatusEnum;
import cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDefineDao;
import cn.zhengcaiyun.idata.develop.dal.model.DevLabelDefine;
import cn.zhengcaiyun.idata.develop.dal.model.metric.MetricApprovalRecord;
import cn.zhengcaiyun.idata.develop.dal.repo.metric.MetricApprovalRecordRepo;
import cn.zhengcaiyun.idata.develop.dto.label.EnumValueDto;
import cn.zhengcaiyun.idata.develop.dto.measure.MetricApprovalRecordDto;
import cn.zhengcaiyun.idata.develop.service.label.EnumService;
import cn.zhengcaiyun.idata.develop.service.measure.MetricApprovalRecordService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDefineDynamicSqlSupport.devLabelDefine;
import static com.google.common.base.Preconditions.checkArgument;
import static org.mybatis.dynamic.sql.SqlBuilder.and;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

@Service
public class MetricApprovalRecordServiceImpl implements MetricApprovalRecordService {

    private final MetricApprovalRecordRepo metricApprovalRecordRepo;
    private final DevLabelDefineDao devLabelDefineDao;
    private final EnumService enumService;

    @Autowired
    public MetricApprovalRecordServiceImpl(MetricApprovalRecordRepo metricApprovalRecordRepo,
                                           DevLabelDefineDao devLabelDefineDao,
                                           EnumService enumService) {
        this.metricApprovalRecordRepo = metricApprovalRecordRepo;
        this.devLabelDefineDao = devLabelDefineDao;
        this.enumService = enumService;
    }

    @Override
    public Page<MetricApprovalRecordDto> paging(MetricApprovalRecordCondition condition) {
        Page<MetricApprovalRecord> page = metricApprovalRecordRepo.paging(condition);
        if (ObjectUtils.isEmpty(page.getContent())) {
            return Page.empty();
        }

//        Map<String, List<EnumValueDto>> bizProcessMap = enumService.getEnumValues("bizProcessEnum:ENUM")
//                .stream()
//                .collect(Collectors.groupingBy(EnumValueDto::getValueCode));
//        Map<String, List<EnumValueDto>> bizDomainMap = enumService.getEnumValues("domainIdEnum:ENUM")
//                .stream()
//                .collect(Collectors.groupingBy(EnumValueDto::getValueCode));

        List<MetricApprovalRecordDto> dtoList = page.getContent().stream()
                .map(record -> convert(record, Maps.newHashMap(), Maps.newHashMap()))
                .collect(Collectors.toList());
        return Page.newOne(dtoList, page.getTotal());
    }

    @Override
    @Transactional
    public Boolean approve(Long id, String remark, Operator operator) {
        MetricApprovalRecord approvalRecord = getApprovalRecord(id);
        checkArgument(Objects.equals(approvalRecord.getApprovalStatus(), MetricApprovalStatusEnum.WAIT_APPROVE.getVal()), "审批状态已变更，不能再审批");

        metricApprovalRecordRepo.updateStatusWhenApprove(id, MetricApprovalStatusEnum.WAIT_APPROVE,
                MetricApprovalStatusEnum.APPROVED, operator.getNickname(), remark);
        setMetricEnable(approvalRecord.getMetricId(), approvalRecord.getMetricTag(), MetricStatusEnum.ENABLE);
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean reject(Long id, String remark, Operator operator) {
        MetricApprovalRecord approvalRecord = getApprovalRecord(id);
        checkArgument(Objects.equals(approvalRecord.getApprovalStatus(), MetricApprovalStatusEnum.WAIT_APPROVE.getVal()), "审批状态已变更，不能再审批");

        metricApprovalRecordRepo.updateStatusWhenApprove(id, MetricApprovalStatusEnum.WAIT_APPROVE,
                MetricApprovalStatusEnum.REJECTED, operator.getNickname(), remark);
        setMetricEnable(approvalRecord.getMetricId(), approvalRecord.getMetricTag(), MetricStatusEnum.DRAFT);
        return Boolean.TRUE;
    }

    private MetricApprovalRecord getApprovalRecord(Long id) {
        checkArgument(Objects.nonNull(id), "记录编号不存在");
        Optional<MetricApprovalRecord> recordOptional = metricApprovalRecordRepo.query(id);
        checkArgument(recordOptional.isPresent(), "审批记录不存在");
        return recordOptional.get();
    }

    public void setMetricEnable(String metricId, String metricTag, MetricStatusEnum metricStatusEnum) {
        Optional<DevLabelDefine> labelDefineOptional = devLabelDefineDao.selectOne(dsl -> dsl.where(devLabelDefine.labelCode, isEqualTo(metricId),
                and(devLabelDefine.del, isEqualTo(DeleteEnum.DEL_NO.val))));
        checkArgument(labelDefineOptional.isPresent(), "指标不存在");
        DevLabelDefine labelDefine = labelDefineOptional.get();
        checkArgument(labelDefine.getLabelTag().equals(metricTag + MetricStatusEnum.APPROVE.getSuffix()), "指标状态已变更，不能再审批");

        String newLabelTag = metricTag + metricStatusEnum.getSuffix();
        devLabelDefineDao.update(c -> c.set(devLabelDefine.labelTag).equalTo(newLabelTag)
                .where(devLabelDefine.labelCode, isEqualTo(metricId),
                        and(devLabelDefine.del, isEqualTo(DeleteEnum.DEL_NO.val))));
    }

    private MetricApprovalRecordDto convert(MetricApprovalRecord record,
                                            Map<String, List<EnumValueDto>> bizProcessMap,
                                            Map<String, List<EnumValueDto>> bizDomainMap) {
        MetricApprovalRecordDto dto = MetricApprovalRecordDto.from(record);
        List<EnumValueDto> bizProcessList = bizProcessMap.get(record.getBizProcess());
        if (!CollectionUtils.isEmpty(bizProcessList)) {
            dto.setBizProcessName(bizProcessList.get(0).getEnumValue());
        } else {
            dto.setBizProcessName("-");
        }
        List<EnumValueDto> bizDomainList = bizDomainMap.get(record.getBizDomain());
        if (!CollectionUtils.isEmpty(bizDomainList)) {
            dto.setBizDomainName(bizDomainList.get(0).getEnumValue());
        } else {
            dto.setBizDomainName("-");
        }
        return dto;
    }
}
