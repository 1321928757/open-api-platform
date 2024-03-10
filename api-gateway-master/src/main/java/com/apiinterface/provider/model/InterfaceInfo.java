package com.apiinterface.provider.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 接口信息
 * @TableName interface_info
 */
@TableName(value ="interface_info")
@Data
public class InterfaceInfo implements Serializable {
    /**
     * 接口id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 接口名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 接口描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 接口地址
     */
    @TableField(value = "url")
    private String url;

    /**
     * 请求参数
     */
    @TableField(value = "request_params")
    private String requestParams;

    /**
     * 请求头
     */
    @TableField(value = "request_header")
    private String requestHeader;

    /**
     * 请求体
     */
    @TableField(value = "response_header")
    private String responseHeader;

    /**
     * 请求类型
     */
    @TableField(value = "method")
    private String method;

    /**
     * 接口状态（1-开启 0-关闭）
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 接口被调用次数
     */
    @TableField(value = "call_count")
    private Long callCount;

    /**
     * 创建人id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 是否删除（0-否 1-是）
     */
    @TableLogic
    @TableField(value = "is_delete")

    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}