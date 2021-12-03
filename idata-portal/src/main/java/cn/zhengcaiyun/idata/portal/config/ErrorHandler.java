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

import cn.zhengcaiyun.idata.commons.exception.ExecuteSqlException;
import cn.zhengcaiyun.idata.commons.exception.ExternalIntegrationException;
import cn.zhengcaiyun.idata.commons.exception.NameDuplicateException;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shiyin
 * @date 2021-03-10 13:35
 */
@ControllerAdvice
public class ErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    private static final String BAD_REQUEST_MSG = "客户端请求参数错误";

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public RestResult exceptionHandler(Exception error, HttpServletRequest req) {
        log.warn("uri: {}, method: {}, stack: {}", req.getRequestURI(), req.getMethod(), ExceptionUtils.getStackTrace(error));
        if (error instanceof IllegalArgumentException) {
            return RestResult.error(RestResult.INPUT_ERROR_CODE, error.getMessage(), ExceptionUtils.getRootCauseMessage(error));
        }
        if (error instanceof IllegalStateException) {
            return RestResult.error(RestResult.INTERNAL_ERROR_CODE, error.getMessage(), ExceptionUtils.getRootCauseMessage(error));
        }
        if (error instanceof ExecuteSqlException) {
            return RestResult.error(RestResult.INTERNAL_ERROR_CODE, error.getMessage(), ExceptionUtils.getRootCauseMessage(error));
        }
        if (error instanceof ExternalIntegrationException) {
            return RestResult.error(RestResult.INTERNAL_ERROR_CODE, error.getMessage(), ExceptionUtils.getRootCauseMessage(error));
        }
        if (error instanceof BindException) {
            // 处理 form data方式调用接口校验失败抛出的异常
            BindException bindException = (BindException) error;
            List<FieldError> fieldErrors = bindException.getBindingResult().getFieldErrors();
            List<String> collect = fieldErrors.stream().map(o -> o.getDefaultMessage()).collect(Collectors.toList());
            return RestResult.error(HttpStatus.BAD_REQUEST.value() + "", BAD_REQUEST_MSG, String.join("\n", collect));
        }
        if (error instanceof MethodArgumentNotValidException) {
            // 处理 form data方式调用接口校验失败抛出的异常
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) error;
            List<FieldError> fieldErrors = methodArgumentNotValidException.getBindingResult().getFieldErrors();
            List<String> collect = fieldErrors.stream().map(o -> o.getDefaultMessage()).collect(Collectors.toList());
            return RestResult.error(HttpStatus.BAD_REQUEST.value() + "", BAD_REQUEST_MSG, String.join("\n", collect));
        }
        if (error instanceof ConstraintViolationException) {
            // 处理 form data方式调用接口校验失败抛出的异常
            ConstraintViolationException constraintViolationException = (ConstraintViolationException) error;
            Set<ConstraintViolation<?>> constraintViolations = constraintViolationException.getConstraintViolations();
            List<String> collect = constraintViolations.stream().map(o -> o.getMessage()).collect(Collectors.toList());
            return RestResult.error(HttpStatus.BAD_REQUEST.value() + "", BAD_REQUEST_MSG, String.join("\n", collect));
        }

        if (error instanceof HttpMessageNotReadableException || error instanceof HttpRequestMethodNotSupportedException || error instanceof MissingServletRequestParameterException) {
            return RestResult.error(RestResult.INPUT_ERROR_CODE, "接口输入错误", ExceptionUtils.getRootCauseMessage(error));
        }

        if (error instanceof NameDuplicateException) {
            return RestResult.error("名称重复", error.getMessage());
        }
        return RestResult.error("服务器内部错误", ExceptionUtils.getRootCauseMessage(error));
    }

}
