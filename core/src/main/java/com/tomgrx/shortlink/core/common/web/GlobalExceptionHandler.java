package com.tomgrx.shortlink.core.common.web;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.tomgrx.shortlink.core.common.convention.errorcode.BaseErrorCode;
import com.tomgrx.shortlink.core.common.convention.exception.AbstractException;
import com.tomgrx.shortlink.core.common.convention.result.Result;
import com.tomgrx.shortlink.core.common.convention.result.Results;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

/**
 * 全局异常处理器
 */
@Component
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 拦截参数验证异常
     */
    @SneakyThrows
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<?> validExceptionHandler(HttpServletRequest request, MethodArgumentNotValidException ex) {
        FieldError firstFieldError = CollectionUtil.getFirst(ex.getBindingResult().getFieldErrors());
        String exceptionStr = Optional.ofNullable(firstFieldError)
                .map(FieldError::getDefaultMessage)
                .orElse(StrUtil.EMPTY);
        logError(request, exceptionStr);
        return Results.failure(BaseErrorCode.CLIENT_ERROR.code(), exceptionStr);
    }

    /**
     * 拦截应用内抛出的异常
     */
    @ExceptionHandler(value = {AbstractException.class})
    public Result<?> abstractException(HttpServletRequest request, AbstractException ex) {
        logError(request, ex.getMessage());
        return Results.failure(ex);
    }

    /**
     * 拦截未捕获异常
     */
    @ExceptionHandler(value = Throwable.class)
    public Result<?> defaultErrorHandler(HttpServletRequest request, Throwable throwable) {
        logError(request, throwable.getMessage());
        return Results.failure();
    }

    private void logError(HttpServletRequest request, String exStr) {
        log.error("[{}] {} [ex] {}", request.getMethod(), getUrl(request), exStr);
    }

    private String getUrl(HttpServletRequest request) {
        if (StringUtils.hasText(request.getQueryString())) {
            return request.getRequestURL().toString() + "?" + request.getQueryString();
        }
        return request.getRequestURL().toString();
    }
}
