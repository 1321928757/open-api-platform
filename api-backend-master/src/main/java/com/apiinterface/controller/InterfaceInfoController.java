package com.apiinterface.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.apiinterface.sdk.client.ApiClient;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.apiinterface.annotation.AuthCheck;
import com.apiinterface.common.BaseResponse;
import com.apiinterface.common.IdRequest;
import com.apiinterface.common.ErrorCode;
import com.apiinterface.common.ResultUtils;
import com.apiinterface.constant.UserConstant;
import com.apiinterface.exception.BusinessException;
import com.apiinterface.exception.ThrowUtils;
import com.apiinterface.model.dto.interfaceInfo.*;
import com.apiinterface.model.entity.InterfaceInfo;
import com.apiinterface.model.entity.User;
import com.apiinterface.model.enums.InterfaceInfoEnum;
import com.apiinterface.model.vo.InterfaceInfoVO;
import com.apiinterface.service.InterfaceInfoService;
import com.apiinterface.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * api接口信息
 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;
    @Resource
    private UserService userService;

    /**
     * 创建api接口信息
     *
     * @param interfaceInfoAddRequest 新增api接口信息对象
     * @param request                 新增api接口信息id
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);
        //参数校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, true);

        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());

        boolean result = interfaceInfoService.save(interfaceInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(interfaceInfo.getId());
    }

    /**
     * 删除api接口信息
     *
     * @param idRequest 删除api接口信息id
     * @param request   是否成功
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = false;
        if (idRequest.getIds() == null) {
            result = interfaceInfoService.removeById(idRequest.getId());
        } else {
            result = interfaceInfoService.removeByIds(Arrays.asList(idRequest.getIds()));
        }

        return ResultUtils.success(result);
    }

    /**
     * 更新api接口信息
     *
     * @param interfaceInfoUpdateRequest 更新数据对象
     * @param request                    HttpServletRequest
     * @return 是否成功
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest,
                                                     HttpServletRequest request) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
        //参数校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, true);

        boolean result = interfaceInfoService.updateById(interfaceInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 发布接口
     *
     * @param idRequest
     * @param request
     * @return
     */
    @PostMapping("/online")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(idRequest.getId());
        interfaceInfo.setStatus(InterfaceInfoEnum.ONLINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 下线接口
     *
     * @param idRequest
     * @param request
     * @return
     */
    @PostMapping("/offline")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> offlineInterfaceInfo(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(idRequest.getId());
        interfaceInfo.setStatus(InterfaceInfoEnum.OFFLINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 测试调用接口
     *
     * @param invokeRequest
     * @param request
     * @return
     */
    @PostMapping("/invoke")
    public Object invokeInterfaceInfo(@RequestBody InterfaceInfoInvokeRequest invokeRequest, HttpServletRequest request) {
        if (invokeRequest == null || invokeRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = invokeRequest.getId();
        //String requestParams = invokeRequest.getRequestParams();
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        User loginUser = userService.getLoginUser(request);
        if (interfaceInfo == null || loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if (!Objects.equals(interfaceInfo.getStatus(), InterfaceInfoEnum.ONLINE.getValue())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口已经被禁用");
        }
        if (loginUser.getCallCount() <= 0 || loginUser.getCallCount() - interfaceInfo.getCallCost() <= 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "您的调用次数已用尽");
        }

        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();
        ApiClient apiClient = new ApiClient(accessKey, secretKey);
        String url = interfaceInfo.getUrl();
        Gson gson = new Gson();
        Object object = gson.fromJson(invokeRequest.getRequestParams(), Object.class);
        String json = JSONUtil.toJsonStr(object);
        String body = HttpRequest.post(url)
                .addHeaders(apiClient.getHashMap(json))
                .body(json)
                .execute().body();
        return body;
    }

    /**
     * 根据 id 获取api接口信息（仅管理员）
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(interfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(interfaceInfo);
    }

    /**
     * 根据 id 获取包装类
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<InterfaceInfoVO> getInterfaceInfoVOById(long id, HttpServletRequest request) {
        BaseResponse<InterfaceInfo> response = getInterfaceInfoById(id, request);
        InterfaceInfo interfaceInfo = response.getData();
        return ResultUtils.success(interfaceInfoService.getInterfaceInfoVO(interfaceInfo));
    }

    /**
     * 分页获取api接口信息列表（仅管理员）
     *
     * @param interfaceInfoQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(@RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest,
                                                                     HttpServletRequest request) {
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size),
                interfaceInfoService.getQueryWrapper(interfaceInfoQueryRequest));
        return ResultUtils.success(interfaceInfoPage);
    }

    /**
     * 分页获取api接口信息封装列表
     *
     * @param interfaceInfoQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<InterfaceInfoVO>> listInterfaceInfoVOByPage(@RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest,
                                                                         HttpServletRequest request) {
        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size),
                interfaceInfoService.getQueryWrapper(interfaceInfoQueryRequest));
        Page<InterfaceInfoVO> interfaceInfoVOPage = new Page<>(current, size, interfaceInfoPage.getTotal());
        List<InterfaceInfoVO> interfaceInfoVO = interfaceInfoService.getInterfaceInfoVO(interfaceInfoPage.getRecords());
        interfaceInfoVOPage.setRecords(interfaceInfoVO);
        return ResultUtils.success(interfaceInfoVOPage);
    }

}
