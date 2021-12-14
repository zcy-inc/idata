package cn.zhengcaiyun.idata.portal.model.request.ops;

public class JobStateRequest {

    /**
     * state 1：队列 2：运行中 6：失败  7：成功  -1：其他
     */
    private Integer state;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
