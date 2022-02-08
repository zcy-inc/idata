package cn.zhengcaiyun.idata.develop.dto.job;

import java.util.Objects;

/**
 * @author caizhedong
 * @date 2021-11-18 下午4:39
 */

public class JobArgumentDto {
    private String argumentValue;
    private String argumentRemark;

    // GaS
    public String getArgumentValue() {
        return argumentValue;
    }

    public void setArgumentValue(String argumentValue) {
        this.argumentValue = argumentValue;
    }

    public String getArgumentRemark() {
        return argumentRemark;
    }

    public void setArgumentRemark(String argumentRemark) {
        this.argumentRemark = argumentRemark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobArgumentDto that = (JobArgumentDto) o;
        return Objects.equals(argumentValue, that.argumentValue) &&
                Objects.equals(argumentRemark, that.argumentRemark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(argumentValue, argumentRemark);
    }
}
