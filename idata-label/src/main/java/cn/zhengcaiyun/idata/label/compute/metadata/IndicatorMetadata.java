package cn.zhengcaiyun.idata.label.compute.metadata;

import java.util.List;

/**
 * 指标信息
 *
 * @description: 数据来自指标系统
 * @author: yangjianhua
 * @create: 2021-06-25 09:35
 **/
public class IndicatorMetadata {
    private String code;
    private String name;
    private String table;
    private String column;
    private String function;
    private DecorateWordMetadata decorateWord;

    /**
     * 条件:
     * 等于：equal，
     * 大于：greater，
     * 小于：less，
     * 大于等于：greaterOrEqual，
     * 小于等于：lessOrEqual，
     * 介于两个值之间：between
     */
    private String indicatorCondition;
    private Long[] indicatorParams;

    public static class DecorateWordMetadata {
        private String column;
        private List<String> params;

        public String getColumn() {
            return column;
        }

        public void setColumn(String column) {
            this.column = column;
        }

        public List<String> getParams() {
            return params;
        }

        public void setParams(List<String> params) {
            this.params = params;
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getIndicatorCondition() {
        return indicatorCondition;
    }

    public void setIndicatorCondition(String indicatorCondition) {
        this.indicatorCondition = indicatorCondition;
    }

    public Long[] getIndicatorParams() {
        return indicatorParams;
    }

    public void setIndicatorParams(Long[] indicatorParams) {
        this.indicatorParams = indicatorParams;
    }

    public DecorateWordMetadata getDecorateWord() {
        return decorateWord;
    }

    public void setDecorateWord(DecorateWordMetadata decorateWord) {
        this.decorateWord = decorateWord;
    }
}
