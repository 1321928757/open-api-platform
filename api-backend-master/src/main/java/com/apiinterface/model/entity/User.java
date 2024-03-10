package com.apiinterface.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 用户信息表
 *
 * @TableName user
 */
@TableName(value = "user")
@Data
public class User implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 账号
     */
    @TableField(value = "account")
    private String account;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 用户昵称
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 用户头像
     */
    @TableField(value = "avatar")
    private String avatar;


    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 访问凭证
     */
    @TableField(value = "access_key")
    private String accessKey;

    /**
     * 密钥
     */
    @TableField(value = "secret_key")
    private String secretKey;

    /**
     * 用户角色：user/admin/ban
     */
    @TableField(value = "role")
    private String role;

    /**
     * 用户调用次数
     */
    @TableField(value = "call_count")
    private Long callCount;

    /**
     * 用户邀请码
     */
    @TableField(value = "invite_code")
    private String inviteCode;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    @TableField(value = "is_delete")
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}