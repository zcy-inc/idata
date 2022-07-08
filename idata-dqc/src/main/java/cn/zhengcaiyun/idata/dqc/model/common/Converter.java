package cn.zhengcaiyun.idata.dqc.model.common;

import cn.zhengcaiyun.idata.dqc.model.entity.MonitorBaseline;
import cn.zhengcaiyun.idata.dqc.model.entity.MonitorRule;
import cn.zhengcaiyun.idata.dqc.model.entity.MonitorTable;
import cn.zhengcaiyun.idata.dqc.model.entity.MonitorTemplate;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorBaselineVO;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorRuleVO;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorTableVO;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorTemplateVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

public interface Converter {
    MonitorRuleConverter MONITOR_RULE_CONVERTER = Mappers.getMapper(MonitorRuleConverter.class);
    MonitorTemplateConverter MONITOR_TEMPLATE_CONVERTER = Mappers.getMapper(MonitorTemplateConverter.class);
    MonitorTableConverter MONITOR_TABLE_CONVERTER = Mappers.getMapper(MonitorTableConverter.class);
    MonitorBaselineConverter MONITOR_BASELINE_CONVERTER = Mappers.getMapper(MonitorBaselineConverter.class);

    @Mapper
    interface MonitorRuleConverter {
        //字段名不一致的时候用这个
//        @Mapping(source = "a", target = "b")
        MonitorRuleVO toVo(MonitorRule monitorRule);
        MonitorRule toDto(MonitorRuleVO vo);
    }

    @Mapper
    interface MonitorTemplateConverter {
        MonitorTemplateVO toVo(MonitorTemplate template);
        MonitorTemplate toDto(MonitorTemplateVO vo);
    }

    @Mapper
    interface MonitorTableConverter {
        MonitorTableVO toVo(MonitorTable table);
        MonitorTable toDto(MonitorTableVO vo);
    }

    @Mapper
    interface MonitorBaselineConverter {
        MonitorBaselineVO toVo(MonitorBaseline table);
        MonitorBaseline toDto(MonitorBaselineVO vo);
    }
}
