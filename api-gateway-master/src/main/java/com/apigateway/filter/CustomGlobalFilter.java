package com.apigateway.filter;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import com.apiinterface.provider.OpenService;
import com.apiinterface.provider.model.InterfaceInfo;
import com.apiinterface.provider.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 自定义全局过滤器
 * 实现请求转发后的业务逻辑
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private OpenService openService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("---全局过滤器配置---");
        //1. 请求日志
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String path = String.valueOf(request.getPath());
        String method = String.valueOf(request.getMethod());
        log.info("请求ID标识:" + request.getId());
        log.info("请求路径:" + path);
        log.info("请求方法:" + method);
        log.info("请求参数:" + request.getQueryParams());
        log.info("请求来源IP:" + request.getRemoteAddress().getHostString());
        //2. todo 请求黑名单，限制某个IP的访问


        //3. 用户鉴权(判断请求头的ak,sk是否合法)
        HttpHeaders headers = request.getHeaders();
        String secretKey = null;
        String accessKey = headers.getFirst("accessKey");
        String timestamp = headers.getFirst("timestamp");
        String body = headers.getFirst("body");
        // todo 根据accessKey在数据库查找是否存在，并拿到secretKey
        User invokeUser = openService.getInvokeUser(accessKey);
        if (invokeUser != null) {
            secretKey = invokeUser.getSecretKey();
        } else {
            log.info("secretKey校验失败！");
            return handleError(response);
        }
        //判断时间戳，使相同的请求头不能一直发送
        long timestampNow = System.currentTimeMillis() / 1000;
        assert timestamp != null;
        if (timestampNow - Long.parseLong(timestamp) > 5) {
            return handleError(response);
        }
        //验证签名
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("accessKey", accessKey);
        hashMap.put("body", body);
        hashMap.put("timestamp", timestamp);
        String data = hashMap.toString() + secretKey;
        Sign sign = SecureUtil.sign(SignAlgorithm.MD5withRSA);
        byte[] signed = sign.sign(data);
        boolean verify = sign.verify(data.getBytes(), signed);
        if (!verify) {
            return handleError(response);
        }
        //4. 接口是否存在
        // todo 从数据库里查询接口
        InterfaceInfo interfaceInfo = openService.getInterfaceInfo("http://154.12.27.76:8102" +path);
        //5.转发请求
        Long userId = invokeUser.getId();
        Long interfaceInfoId = interfaceInfo.getId();
        return handleResponse(exchange, chain, userId, interfaceInfoId);
    }

    @Override
    public int getOrder() {
        return -1;
    }


    /**
     * 使用装饰者模式增强Response的功能
     *
     * @param exchange
     * @param chain
     * @return
     */

    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, Long userId, Long interfaceInfoId) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();

            HttpStatus statusCode = originalResponse.getStatusCode();

            if (statusCode == HttpStatus.OK) {
                // 拦截了响应的写入操作，并添加了调用次数的记录逻辑。
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        //log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            //
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                // 调用成功记录调用次数
                                try {
                                    // 用户额度扣减，调用次数+1
                                    Boolean aBoolean = openService.invokeCount(userId,interfaceInfoId);
                                    if(!aBoolean){
                                        throw new Exception("记录调用次数");
                                    }
                                    log.info("接口调用次数加一");
                                } catch (Exception e) {
                                    log.error("invokeCount error", e);
                                }
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);//释放掉内存
                                // 构建日志
                                StringBuilder sb2 = new StringBuilder(200);
                                List<Object> rspArgs = new ArrayList<>();
                                rspArgs.add(originalResponse.getStatusCode());
                                //rspArgs.add(requestUrl);
                                String data = new String(content, StandardCharsets.UTF_8);//data
                                sb2.append(data);
                                // 打印响应日志
                                log.info("响应结果" + data);
                                return bufferFactory.wrap(content);
                            }));
                        } else {
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange);//降级处理返回数据
        } catch (Exception e) {
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }

    public Mono<Void> handleError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }
}

