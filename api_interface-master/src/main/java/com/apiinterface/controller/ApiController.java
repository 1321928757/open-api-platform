package com.apiinterface.controller;

import cn.hutool.http.HttpUtil;
import com.apiinterface.common.BaseResponse;
import com.apiinterface.common.ResultUtils;
import com.apiinterface.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class ApiController {
    /**
     * 天气查询
     * @param weatherRequest
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/weather")
    public BaseResponse<Object> getNameByPost(@RequestBody WeatherRequest weatherRequest) throws JsonProcessingException {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("city_name", weatherRequest.getCity_name());
        System.out.println(weatherRequest.getCity_name());
        String s = HttpUtil.get("https://api.oioweb.cn/api/weather/weather", paramMap);
        Map<String, Object> map = stringObjectMap(s);
        Map<String, Object> resultMap = new HashMap<>();
        System.out.println(map);
        return ResultUtils.success(map);
    }

    /**
     * 每日英语
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/OneDayEnglish")
    public BaseResponse<Object> getOneDayEnglish() throws JsonProcessingException {
        String s = HttpUtil.get("https://api.oioweb.cn/api/common/OneDayEnglish");
        Map<String, Object> map = stringObjectMap(s);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", map.get("result"));
        return ResultUtils.success(resultMap);
    }

    /**
     * 电话地址查询
     * @param tel
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/telAddress")
    public BaseResponse<Object> getTelAddress(@RequestBody TelAddressRequest tel) throws JsonProcessingException {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("mobile", tel.getMobile());
        String s = HttpUtil.get("https://api.oioweb.cn/api/common/teladress",paramMap);
        Map<String, Object> map = stringObjectMap(s);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", map.get("result"));
        return ResultUtils.success(resultMap);
    }

    /**
     * 垃圾分类查询
     * @param request
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/rubbish")
    public BaseResponse<Object> getRubbish(@RequestBody RubbishRequest request) throws JsonProcessingException {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", request.getName());
        String s = HttpUtil.get("https://api.oioweb.cn/api/common/rubbish",paramMap);
        Map<String, Object> map = stringObjectMap(s);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", map.get("result"));
        return ResultUtils.success(resultMap);
    }

    /**
     * qq信息
     * @param qq
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/qqInfo")
    public BaseResponse<Object> getQQInfo(@RequestBody QQInfoRequest qq) throws JsonProcessingException {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("qq", qq.getQq());
        String s = HttpUtil.get("https://api.oioweb.cn/api/qq/info",paramMap);
        Map<String, Object> map = stringObjectMap(s);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", map.get("result"));
        return ResultUtils.success(resultMap);
    }

    /**
     * 土味情话
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/loveTalk")
    public BaseResponse<Object> getLoveTalk() throws JsonProcessingException {
        String s = HttpUtil.get("https://api.uomg.com/api/rand.qinghua?format=json");
        Map<String, Object> map = stringObjectMap(s);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", map.get("content"));
        return ResultUtils.success(resultMap);
    }

    /**
     * 随机头像
     * @param request
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/avatar")
    public BaseResponse<Object> getAvatar(@RequestBody AvatarRequest request) throws JsonProcessingException {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("format","json");
        if(request.getSort()!=null){
            paramMap.put("sort", request.getSort());
        }
        String s = HttpUtil.get("https://api.uomg.com/api/rand.avatar",paramMap);
        Map<String, Object> map = stringObjectMap(s);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("imgurl", map.get("imgurl"));
        return ResultUtils.success(resultMap);
    }

    @PostMapping("/translate")
    public BaseResponse<Object> getTranslate(@RequestBody TranslateRequest request) throws JsonProcessingException {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("sourceText",request.getSourceText());
        String s = HttpUtil.get("https://api.oioweb.cn/api/txt/QQFanyi",paramMap);
        Map<String, Object> map = stringObjectMap(s);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", map.get("result"));
        return ResultUtils.success(resultMap);
    }






    private Map<String, Object> stringObjectMap(String s) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(s, new TypeReference<Map<String, Object>>() {
        });
    }

}
