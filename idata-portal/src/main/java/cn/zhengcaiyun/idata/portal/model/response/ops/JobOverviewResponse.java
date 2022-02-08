package cn.zhengcaiyun.idata.portal.model.response.ops;

import cn.zhengcaiyun.idata.portal.model.response.NameValueResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * DS调度任务情况概览
 */
public class JobOverviewResponse {

    /**
     *作业总数
     */
    private Integer total;

    /**
     * 等待运行
     */
    private Integer ready;

    /**
     * 运行中
     */
    private Integer running;

    /**
     * 失败数
     */
    private Integer failure;

    /**
     * 成功数
     */
    private Integer success;

    /**
     * 其他
     */
    private Integer other;

    /**
     * 饼图
     */
    private List<NameValueResponse> nameValueResponseList = new ArrayList<>();

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getReady() {
        return ready;
    }

    public void setReady(Integer ready) {
        this.ready = ready;
    }

    public Integer getRunning() {
        return running;
    }

    public void setRunning(Integer running) {
        this.running = running;
    }

    public Integer getFailure() {
        return failure;
    }

    public void setFailure(Integer failure) {
        this.failure = failure;
    }

    public Integer getOther() {
        return other;
    }

    public void setOther(Integer other) {
        this.other = other;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public List<NameValueResponse> getNameValueResponseList() {
        return nameValueResponseList;
    }

    public void setNameValueResponseList(List<NameValueResponse> nameValueResponseList) {
        this.nameValueResponseList = nameValueResponseList;
    }

    public Integer calcTotal() {
        int sum = 0;
        if (ready != null) {
            sum += ready;
        }
        if (running != null) {
            sum += running;
        }
        if (failure != null) {
            sum += failure;
        }
        if (success != null) {
            sum += success;
        }
        if (other != null) {
            sum += other;
        }
        return sum;
    }
}
