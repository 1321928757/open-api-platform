package com.apiinterface.service;

import com.apiinterface.model.entity.CallRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.apiinterface.model.vo.CallRecordVO;

import java.util.List;

/**
* @author 26550
* @description 针对表【call_record】的数据库操作Service
* @createDate 2023-10-07 19:15:36
*/
public interface CallRecordService extends IService<CallRecord> {
    /**
     * 记录用户调用数据
     * @param userId
     * @param interfaceId
     * @return
     */
    boolean updateUserCallRecord(Long userId,Long interfaceId);

    /**
     * 获得调用记录列表
     * @param userId
     * @return
     */
    List<CallRecordVO> getCallRecordList(Long userId);



}
