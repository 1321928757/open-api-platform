package com.apiinterface.model.dto.interfaceInfo;


import lombok.Data;

import java.io.Serializable;


/**
 * 用户调用请求
 *
 */
@Data
public class InterfaceInfoInvokeRequest implements Serializable {

    /**
     * 接口id
     */
    private Long id;

    /**
     * 请求参数
     */
    private String requestParams;

    private static final long serialVersionUID = 1L;
}