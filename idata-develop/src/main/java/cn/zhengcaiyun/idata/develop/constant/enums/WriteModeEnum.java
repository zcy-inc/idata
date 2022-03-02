package cn.zhengcaiyun.idata.develop.constant.enums;

public enum WriteModeEnum {
    ;

    /**
     * 数据集成-数据抽取写入模式
     */
    public enum DiEnum {
        init,
        overwrite,
        append;
    }

    /**
     * 数据集成-数据回流写入模式
     */
    public enum BackFlowEnum {
        INSERT,
        UPSERT,
        OVERWRITE;
    }

    /**
     * sql写入模式
     */
    public enum SqlEnum {
        UPSERT,
        OVERWRITE;
    }


}
