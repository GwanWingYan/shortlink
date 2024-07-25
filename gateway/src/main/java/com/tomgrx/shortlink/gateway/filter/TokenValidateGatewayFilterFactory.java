package com.tomgrx.shortlink.gateway.filter;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.tomgrx.shortlink.gateway.dto.GatewayErrorResult;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.tomgrx.shortlink.constant.RedisKeyConstant.LOGIN_KEY_PREFIX;

/**
 * 检验用户请求中的 userName 和 token 是否存在且合法
 * 注：在 application.yaml 中， 这个 filter 的名字是 'TokenValidate'，不包含后缀。这是 Spring Cloud Gateway filter 命名规范的一部分。
 * 注：除了白名单中的 HTTP 请求，其他请求都要经历鉴权，请求需要带上两个 HTTP header（userName 和 token）
 */
@Component
public class TokenValidateGatewayFilterFactory extends AbstractGatewayFilterFactory<TokenValidateGatewayFilterConfig> {

    private final StringRedisTemplate stringRedisTemplate;

    public TokenValidateGatewayFilterFactory(StringRedisTemplate stringRedisTemplate) {
        super(TokenValidateGatewayFilterConfig.class);
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public GatewayFilter apply(TokenValidateGatewayFilterConfig config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // 放行匹配白名单的请求
            String requestPath = request.getPath().toString();
            String requestMethod = request.getMethod().name();
            if (isRequestInWhiteList(requestPath, requestMethod, config.getWhiteList())) {
                return chain.filter(exchange);
            }

            String userName = request.getHeaders().getFirst("userName");
            String token = request.getHeaders().getFirst("token");

            // 放行 header 包含有效 userName 和 token 的请求
            Object userInfo;
            if (StringUtils.hasText(userName) && StringUtils.hasText(token) && (userInfo = stringRedisTemplate.opsForHash().get(LOGIN_KEY_PREFIX + userName, token)) != null) {
                JSONObject userInfoJsonObject = JSON.parseObject(userInfo.toString());
                ServerHttpRequest newRequest = exchange.getRequest()
                        .mutate()
                        .headers(httpHeaders -> {
                            httpHeaders.set("userId", userInfoJsonObject.getString("id"));
                            httpHeaders.set("realName", URLEncoder.encode(userInfoJsonObject.getString("realName"), StandardCharsets.UTF_8));
                        })
                        .build();
                ServerWebExchange newExchange = exchange.mutate()
                        .request(newRequest)
                        .build();
                return chain.filter(newExchange);
            }

            // 拒绝其他请求，一律视为非法
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.writeWith(Mono.fromSupplier(() -> {
                DataBufferFactory bufferFactory = response.bufferFactory();
                GatewayErrorResult resultMessage = GatewayErrorResult.builder()
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .message("Token validation error")
                        .build();
                return bufferFactory.wrap(JSON.toJSONString(resultMessage).getBytes());
            }));
        };
    }

    /**
     * 判断请求是否匹配白名单
     *
     * @param requestPath 请求路径
     * @param requestMethod 请求方法
     * @param whiteList 白名单
     */
    private boolean isRequestInWhiteList(String requestPath, String requestMethod, List<TokenValidateGatewayFilterConfig.Entry> whiteList) {
        if (CollectionUtils.isEmpty(whiteList)) {
            return true;
        }

        for (TokenValidateGatewayFilterConfig.Entry entry : whiteList) {
            // 判断请求方法是否匹配
            String method = entry.getMethod();
            if (!method.equals("*") && !method.equals(requestMethod)) {
                continue;
            }

            // 判断请求路径是否匹配
            String path = entry.getPath();
            // 精确匹配
            if (path.equals(requestPath)) {
                return true;
            }
            // 模糊匹配
            if (path.endsWith("/*")) {
                path = path.substring(0, path.length() - 2);
                if (requestPath.startsWith(path)) {
                    return true;
                }
            }
        }

        return false;
    }
}
