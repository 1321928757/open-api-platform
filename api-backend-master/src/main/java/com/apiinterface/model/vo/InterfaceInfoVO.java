package com.apiinterface.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class InterfaceInfoVO implements Serializable {
    /**
     * 接口id
     */
    private Long id;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口图片
     */
    private String img;

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
     * 接口被调用次数
     */
    private Long callCount;

    /**
     * 接口状态（0-开启 1-关闭）
     */
    private Integer status;

    /**
     * 创建人id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}
