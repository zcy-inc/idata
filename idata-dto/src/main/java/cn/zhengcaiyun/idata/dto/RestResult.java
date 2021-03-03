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
package cn.zhengcaiyun.idata.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author shiyin
 * @date 2021-03-01 21:18
 */
public class RestResult<T> {
    @ApiModelProperty(required = true)
    private boolean success;
    @ApiModelProperty(required = true)
    private String code;
    private String msg;
    private T data;

    public static RestResult success() {
        RestResult result = new RestResult();
        result.setSuccess(true);
        result.setData("");
        result.setCode("");
        result.setMsg("");
        return result;
    }

    public static <T> RestResult<T> success(T data) {
        RestResult<T> result = new RestResult<>();
        result.setSuccess(true);
        result.setData(data);
        result.setCode("");
        result.setMsg("");
        return result;
    }

    public static <T> RestResult<T> error(String code, String msg) {
        RestResult<T> result = new RestResult<>();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public String toString() {
        return "Result(success=" + this.isSuccess() + ", code=" + this.getCode() + ", msg=" + this.getMsg() + ", data=" + this.getData() + ")";
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
