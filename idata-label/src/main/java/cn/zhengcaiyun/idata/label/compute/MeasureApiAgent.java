package cn.zhengcaiyun.idata.label.compute;

import cn.zhengcaiyun.idata.develop.api.MeasureApi;
import cn.zhengcaiyun.idata.develop.dto.label.LabelDto;
import cn.zhengcaiyun.idata.develop.dto.label.ModifierDto;
import cn.zhengcaiyun.idata.develop.dto.label.SpecialAttributeDto;
import cn.zhengcaiyun.idata.develop.dto.measure.MeasureDto;
import cn.zhengcaiyun.idata.label.compute.metadata.DimensionMetadata;
import cn.zhengcaiyun.idata.label.compute.metadata.IndicatorMetadata;
import cn.zhengcaiyun.idata.label.compute.metadata.ObjectMetadata;
import cn.zhengcaiyun.idata.label.dto.label.rule.DimensionDefDto;
import cn.zhengcaiyun.idata.label.dto.label.rule.IndicatorDefDto;
import cn.zhengcaiyun.idata.label.enums.ObjectTypeEnum;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-29 18:40
 **/
@Component
public class MeasureApiAgent {

    private final MeasureApi measureApi;

    @Autowired
    public MeasureApiAgent(MeasureApi measureApi) {
        this.measureApi = measureApi;
    }

    public Optional<IndicatorMetadata> getIndicatorMetadata(String indicatorCode) {
        if (StringUtils.isEmpty(indicatorCode)) return Optional.empty();

        List<MeasureDto> measureDtoList = measureApi.getMeasures(Lists.newArrayList(indicatorCode));
        if (CollectionUtils.isEmpty(measureDtoList)){
            return Optional.empty();
        }
        return Optional.of(convertToIndicatorMetadata( measureDtoList.get(0)));
    }

    private IndicatorMetadata convertToIndicatorMetadata(MeasureDto measureDto){
        IndicatorMetadata metadata = new IndicatorMetadata();
        metadata.setCode(measureDto.getLabelCode());
        metadata.setName(measureDto.getLabelName());
        List<LabelDto> measureLabels = measureDto.getMeasureLabels();
        if (CollectionUtils.isEmpty(measureLabels)){
            return null;
        }
        metadata.setTable(measureLabels.get(0).getTableName());
        metadata.setColumn(measureLabels.get(0).getColumnName());
        SpecialAttributeDto specialAttributeDto = measureDto.getSpecialAttribute();
        if (Objects.isNull(specialAttributeDto)){
            return null;
        }
        metadata.setFunction(specialAttributeDto.getAggregatorCode());
        List<ModifierDto> modifiers = specialAttributeDto.getModifiers();
        if (!CollectionUtils.isEmpty(modifiers)){
            ModifierDto modifierDto = modifiers.get(0);
            IndicatorMetadata.DecorateWordMetadata decorateWord = new IndicatorMetadata.DecorateWordMetadata();
            //todo finish column
            decorateWord.setColumn("");
            decorateWord.setParams(modifierDto.getEnumValues());
            metadata.setDecorateWord(decorateWord);
        }
        return metadata;
    }

    public Optional<Map<String,DimensionMetadata>> getDimensionMetadata(List<String> dimensionCodes) {
        if (CollectionUtils.isEmpty(dimensionCodes)) return Optional.empty();
        List<MeasureDto> measureDtoList = measureApi.getMeasures(dimensionCodes);
        if (CollectionUtils.isEmpty(measureDtoList)){
            return Optional.empty();
        }
        Map<String,DimensionMetadata> metadataMap = Maps.newHashMap();
        for (MeasureDto measureDto:measureDtoList){
            DimensionMetadata metadata= convertToDimensionMetadata( measureDto);
            metadataMap.put(metadata.getCode(),metadata);
        }
        return Optional.of(metadataMap);
    }

    private DimensionMetadata convertToDimensionMetadata(MeasureDto measureDto){
        DimensionMetadata metadata = new DimensionMetadata();
        metadata.setCode(measureDto.getLabelCode());
        metadata.setName(measureDto.getLabelName());
        List<LabelDto> measureLabels = measureDto.getMeasureLabels();
        if (CollectionUtils.isEmpty(measureLabels)){
            return null;
        }
        metadata.setTable(measureLabels.get(0).getTableName());
        metadata.setColumn(measureLabels.get(0).getColumnName());
        return metadata;
    }

    public ObjectMetadata getObjectMetadata(ObjectTypeEnum objectTypeEnum) {
        checkArgument(objectTypeEnum != null, "未选择标签主体.");
        // 从指标系统获取维度数据，转换为 ObjectDto
        return null;
    }

    public Optional<ObjectMetadata> getObjectMetadata(List<String> objectCodes) {
        if (CollectionUtils.isEmpty(objectCodes)) return Optional.empty();
        List<MeasureDto> measureDtoList = measureApi.getMeasures(objectCodes);
        if (CollectionUtils.isEmpty(measureDtoList)){
            return Optional.empty();
        }
        Map<String,ObjectMetadata> metadataMap = Maps.newHashMap();
        for (MeasureDto measureDto:measureDtoList){
            DimensionMetadata metadata= convertToDimensionMetadata( measureDto);
            metadataMap.put(metadata.getCode(),metadata);
        }
        return Optional.of(metadataMap);
    }

    private ObjectMetadata convertToObjectMetadata(MeasureDto measureDto){
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setCode(measureDto.getLabelCode());
        metadata.setName(measureDto.getLabelName());
        List<LabelDto> measureLabels = measureDto.getMeasureLabels();
        if (CollectionUtils.isEmpty(measureLabels)){
            return null;
        }
        metadata.setTable(measureLabels.get(0).getTableName());
        metadata.setColumn(measureLabels.get(0).getColumnName());
        return metadata;
    }

}
