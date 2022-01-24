/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.zhengcaiyun.idata.commons.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author shiyin
 * @date 2021-03-01 21:18
 */
public class RestResult<T> {
    public static final String SUCCESS_CODE = "200";
    public static final String INPUT_ERROR_CODE = "400";
    public static final String UNAUTHORIZED_ERROR_CODE = "401";
    public static final String FORBIDDEN_ERROR_CODE = "403";
    public static final String INTERNAL_ERROR_CODE = "500";

    @ApiModelProperty(required = true)
    private boolean success;
    @ApiModelProperty(required = true)
    private String code;
    private String msg;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String causeMsg;
    private T data;

    private RestResult() {}

    public static RestResult success() {
        return result(true, SUCCESS_CODE, "", null, "");
    }

    public static <T> RestResult<T> success(T data) {
       return result(true, SUCCESS_CODE, "", null, data);
    }

    public static <T> RestResult<T> success(String code, T data) {
        return result(true, code, "", null, data);
    }

    public static <T> RestResult<T> error(String msg, String causeMsg) {
        return result(false, INTERNAL_ERROR_CODE, msg, causeMsg, null);
    }

    public static <T> RestResult<T> error(String code, String msg, String causeMsg) {
        return result(false, code, msg, causeMsg, null);
    }

    private static <T> RestResult<T> result(boolean success, String resultCode,
                                          String msg, String causeMsg, T data) {
        RestResult<T> restResult = new RestResult<>();
        restResult.setSuccess(success);
        restResult.setCode(resultCode);
        restResult.setMsg(msg);
        restResult.setCauseMsg(causeMsg);
        restResult.setData(data);
        return restResult;
    }

    public String toString() {
        return "RestResult(success=" + this.isSuccess()
                + ", code=" + this.getCode()
                + ", msg=" + this.getMsg()
                + ", causeMsg=" + this.getCauseMsg()
                + ", data=" + this.getData() + ")";
    }

    // GaS
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCauseMsg() {
        return causeMsg;
    }

    public void setCauseMsg(String causeMsg) {
        this.causeMsg = causeMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
