package com.apiinterface.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

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
     * 接口图片
     */
    @TableField(value = "img")
    private String img;

    /**
     * 接口地址
     */
    @TableField(value = "url")
    private String url;

    /**
     * 请求类型
     */
    @TableField(value = "method")
    private String method;

    /**
     * 请求参数
     */
    @TableField(value = "request_params")
    private String requestParams;

    /**
     * 响应参数
     */
    @TableField(value = "response_params")
    private String responseParams;

    /**
     * 调用花费
     */
    @TableField(value = "call_cost")
    private Integer callCost;

    /**
     * 接口被调用次数
     */
    @TableField(value = "call_count")
    private Long callCount;

    /**
     * 接口状态（0-开启 1-关闭）
     */
    @TableField(value = "status")
    private Integer status;

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