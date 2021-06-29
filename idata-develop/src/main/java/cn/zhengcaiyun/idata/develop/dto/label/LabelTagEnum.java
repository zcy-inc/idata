package cn.zhengcaiyun.idata.develop.dto.label;

/**
 * @author caizhedong
 * @date 2021-05-18 21:01
 */
public enum LabelTagEnum {
    // 文本|布尔|用户标签|枚举标签|枚举值标签|属性标签
    STRING_LABEL,
    BOOLEAN_LABEL,
    USER_LABEL,
    ENUM_LABEL,
    ENUM_VALUE_LABEL,
    ATTRIBUTE_LABEL,
    // 维度|原子指标|派生指标|复合指标|修饰词
    DIMENSION_LABEL,
    ATOMIC_METRIC_LABEL,
    DERIVE_METRIC_LABEL,
    COMPLEX_METRIC_LABEL,
    MODIFIER_LABEL,
    // 停用 维度|原子指标|派生指标|复合指标|修饰词
    DIMENSION_LABEL_DISABLE,
    ATOMIC_METRIC_LABEL_DISABLE,
    DERIVE_METRIC_LABEL_DISABLE,
    COMPLEX_METRIC_LABEL_DISABLE,
    MODIFIER_LABEL_DISABLE;
}
