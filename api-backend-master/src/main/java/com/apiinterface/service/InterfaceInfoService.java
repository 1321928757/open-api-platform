package com.apiinterface.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.apiinterface.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.baomidou.mybatisplus.extension.service.IService;
import com.apiinterface.model.entity.InterfaceInfo;
import com.apiinterface.model.vo.InterfaceInfoVO;

import java.util.List;

/**
* @author 26550
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-09-19 10:32:35
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 校验
     *
     * @param interfaceInfo
     * @param add
     */
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

    /**
     * 获取脱敏的api接口信息
     *
     * @param interfaceInfo api接口信息对象
     * @return 脱敏后的api接口信息对象
     */
    InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo);

    /**
     * 获取脱敏的api接口信息
     *
     * @param interfaceInfoList api接口信息对象列表
     * @return 脱敏后的api接口信息对象列表
     */
    List<InterfaceInfoVO> getInterfaceInfoVO(List<InterfaceInfo> interfaceInfoList);

    /**
     * 封装查询条件
     *
     * @param interfaceInfoQueryRequest 查询数据对象
     * @return 查询条件对象
     */
    QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest);


    /**
     * 更新接口调用次数
     * @param userId
     * @param interfaceId
     * @return
     */
    boolean updateInterfaceCallCount(Long userId,Long interfaceId);
}
