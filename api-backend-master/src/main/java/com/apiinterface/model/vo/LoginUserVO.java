package com.apiinterface.model.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 已登录用户视图（脱敏）
 *
 **/
@Data
public class LoginUserVO implements Serializable {

    /**
     * 用户 id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户简介
     */
    private String profile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户角色：user/admin/ban
     */
    private String role;

    /**
     * 访问凭证
     */
    private String accessKey;

    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 用户调用次数
     */
    private Long callCount;

    /**
     * 用户邀请码
     */
    private String inviteCode;



    private static final long serialVersionUID = 1L;
}