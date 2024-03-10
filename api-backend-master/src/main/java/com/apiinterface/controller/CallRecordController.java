package com.apiinterface.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.apiinterface.common.BaseResponse;
import com.apiinterface.common.ErrorCode;
import com.apiinterface.common.ResultUtils;
import com.apiinterface.exception.BusinessException;
import com.apiinterface.exception.ThrowUtils;
import com.apiinterface.model.entity.CallRecord;
import com.apiinterface.model.entity.User;
import com.apiinterface.model.vo.CallRecordVO;
import com.apiinterface.service.CallRecordService;
import com.apiinterface.service.InterfaceInfoService;
import com.apiinterface.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 接口调用信息
 */
@RestController
@RequestMapping("/callRecord")
@Slf4j
public class CallRecordController {

    @Resource
    private InterfaceInfoService interfaceInfoService;
    @Resource
    private UserService userService;
    @Resource
    private CallRecordService callRecordService;


    /**
     * 删除接口调用信息
     *
     * @param request   是否成功
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteAllCallRecord(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        QueryWrapper<CallRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",loginUser.getId());
        boolean remove = callRecordService.remove(wrapper);
        ThrowUtils.throwIf(!remove,ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 获取接口调用信息列表
     *
     * @param request
     * @return
     */
    @PostMapping("/list")
    public BaseResponse<List<CallRecordVO>> getCallRecordList(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        List<CallRecordVO> callRecordList = callRecordService.getCallRecordList(loginUser.getId());
        return ResultUtils.success(callRecordList);
    }

}
