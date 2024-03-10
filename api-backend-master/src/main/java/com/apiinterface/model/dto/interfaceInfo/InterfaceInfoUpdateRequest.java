package com.apiinterface.model.dto.interfaceInfo;


import lombok.Data;

import java.io.Serializable;


/**
 * 用户更新请求
 *
 */
@Data
public class InterfaceInfoUpdateRequest implements Serializable {
    /**
     * 接口id
     */
    private Long id;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求类型
     */
    private String method;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 响应参数
     */
    private String responseParams;

    /**
     * 调用花费
     */
    private Integer callCost;

    /**
     * 接口状态（0-开启 1-关闭）
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}