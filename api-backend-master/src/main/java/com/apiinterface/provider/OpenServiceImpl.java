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
package com.apiinterface.provider;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.apiinterface.common.ErrorCode;
import com.apiinterface.exception.BusinessException;
import com.apiinterface.exception.ThrowUtils;
import com.apiinterface.model.entity.InterfaceInfo;
import com.apiinterface.model.entity.User;
import com.apiinterface.model.enums.InterfaceInfoEnum;
import com.apiinterface.service.CallRecordService;
import com.apiinterface.service.InterfaceInfoService;
import com.apiinterface.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.Objects;

import static com.apiinterface.model.enums.InterfaceInfoEnum.OFFLINE;

@DubboService
public class OpenServiceImpl implements OpenService {

    @Resource
    private UserService userService;
    @Resource
    private CallRecordService callRecordService;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    /**
     * 通过ak获得当前调用的用户信息,验证sk是否正确
     *
     * @param accessKey
     * @return
     */
    @Override
    public User getInvokeUser(String accessKey) {
        if (accessKey == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("access_key", accessKey);
        User user = userService.getOne(wrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return user;
    }

    /**
     * 从数据库查询接口是否存在
     *
     * @param url
     * @return
     */
    @Override
    public InterfaceInfo getInterfaceInfo(String url) {
        if (url == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("url", url);
        InterfaceInfo interfaceInfo = interfaceInfoService.getOne(wrapper);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        if (Objects.equals(interfaceInfo.getStatus(), OFFLINE.getValue())) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR);
        }
        return interfaceInfo;
    }

    /**
     * 记录接口调用次数
     *
     * @param userId
     * @param interfaceInfoId
     * @return
     */
    @Override
    @Transactional
    public Boolean invokeCount(Long userId, Long interfaceInfoId) {
        if (userId == null || interfaceInfoId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(interfaceInfoId);
        User user = userService.getById(userId);
        if (interfaceInfo == null || user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if (!Objects.equals(interfaceInfo.getStatus(), InterfaceInfoEnum.ONLINE.getValue())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口已经被禁用");
        }
        if (user.getCallCount() <= 0 || user.getCallCount() - interfaceInfo.getCallCost() <= 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "您的调用次数已用尽");
        }
        //更新用户调用次数
        boolean result = userService.updateUserCallCount(userId, interfaceInfoId);
        ThrowUtils.throwIf(!result,ErrorCode.OPERATION_ERROR);
        //更新接口调用次数
        boolean result2 = interfaceInfoService.updateInterfaceCallCount(userId, interfaceInfoId);
        ThrowUtils.throwIf(!result2,ErrorCode.OPERATION_ERROR);
        //添加调用信息
        boolean result3 = callRecordService.updateUserCallRecord(userId, interfaceInfoId);
        ThrowUtils.throwIf(!result3,ErrorCode.OPERATION_ERROR);
        return true;
    }
}
