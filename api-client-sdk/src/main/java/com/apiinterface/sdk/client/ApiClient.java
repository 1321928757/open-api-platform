package com.apiinterface.sdk.client;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.apiinterface.sdk.model.WeatherRequest;
import com.apiinterface.sdk.utils.SignUtil;


import java.util.HashMap;
import java.util.Map;

public class ApiClient {
    //访问密钥
    private String accessKey;
    //密钥
    private String secretKey;

    public ApiClient(String assessKey, String secretKey) {
        this.accessKey = assessKey;
        this.secretKey = secretKey;
    }

    /**
     * 获得天气信息api
     * @param weatherRequest
     * @return
     */
    public String getWeather(WeatherRequest weatherRequest) {
        String json = JSONUtil.toJsonStr(weatherRequest);
        String body = HttpRequest.post("http://154.12.27.76:8102/api/weather")
                .addHeaders(getHashMap(json))
                .body(json)
                .execute().body();
        return body;
    }




    public Map<String, String> getHashMap(String json) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("accessKey", accessKey);
        //密钥等重要数据不要在请求中传输
        //hashMap.put("secretKey", secretKey);
        //请求参数
        hashMap.put("body", json);
        //时间戳
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        //签名
        hashMap.put("sign", SignUtil.getSign(hashMap,secretKey));
        return hashMap;
    }

}

