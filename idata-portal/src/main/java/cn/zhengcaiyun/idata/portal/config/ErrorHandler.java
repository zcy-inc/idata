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
package cn.zhengcaiyun.idata.portal.config;

import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author shiyin
 * @date 2021-03-10 13:35
 */
@ControllerAdvice
public class ErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public RestResult exceptionHandler(Exception error, HttpServletRequest req) {
        log.error("uri: {}, method: {}, stack: {}", req.getRequestURI(), req.getMethod(),
                ExceptionUtils.getStackTrace(error));
        if (error instanceof IllegalArgumentException
                || error instanceof HttpMessageNotReadableException
                || error instanceof HttpRequestMethodNotSupportedException) {
            return RestResult.error(RestResult.INPUT_ERROR_CODE, "接口输入错误", ExceptionUtils.getRootCauseMessage(error));
        }
        return RestResult.error("服务器内部错误", ExceptionUtils.getRootCauseMessage(error));
    }
}
