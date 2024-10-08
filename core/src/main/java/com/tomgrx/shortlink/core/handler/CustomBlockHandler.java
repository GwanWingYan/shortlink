package com.tomgrx.shortlink.core.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.tomgrx.shortlink.core.common.convention.result.Result;
import com.tomgrx.shortlink.core.dto.req.ShortlinkCreateReqDTO;
import com.tomgrx.shortlink.core.dto.resp.ShortlinkCreateRespDTO;

/**
 * 自定义流控策略
 */
public class CustomBlockHandler {

    public static Result<ShortlinkCreateRespDTO> createShortlinkBlockHandlerMethod(ShortlinkCreateReqDTO requestParam, BlockException exception) {
        return new Result<ShortlinkCreateRespDTO>().setCode("B100000").setMessage("当前访问网站人数过多，请稍后再试...");
    }
}
