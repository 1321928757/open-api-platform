package com.apiinterface.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CallRecordVO extends InterfaceInfoVO implements Serializable {

    /**
     * 用户调用该接口次数
     */
    private Long userCallCount;

    /**
     * 最后调用的时间
     */
    private Date lastCallTime;

}
