package com.tomgrx.shortlink.core.common.convention.errorcode;

/**
 * 平台错误码定义
 */
public interface IErrorCode {
    /**
     * 错误码
     */
    String code();

    /**
     * 错误信息
     */
    String message();
}
