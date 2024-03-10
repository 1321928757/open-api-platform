package com.apiinterface.model.vo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 用户视图（脱敏）
 *
 */
@Data
public class UserVO implements Serializable {

    /**
     * id
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
     * 账号
     */
    private String account;

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
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}