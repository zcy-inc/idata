package cn.zhengcaiyun.idata.label.compute.sql.transform;

import cn.zhengcaiyun.idata.label.dto.label.rule.DimensionDefDto;
import cn.zhengcaiyun.idata.label.dto.label.rule.IndicatorDefDto;
import cn.zhengcaiyun.idata.label.dto.label.rule.LabelRuleDto;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 19:56
 **/
@Component
public class SqlTranslator {
    public String translate(LabelRuleDto ruleDto) {
        List<IndicatorDefDto> indicatorDefs = ruleDto.getIndicatorDefs();
        List<DimensionDefDto> dimensionDefs = ruleDto.getDimensionDefs();
        return null;
    }
}
