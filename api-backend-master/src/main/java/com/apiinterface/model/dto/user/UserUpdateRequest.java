package com.apiinterface.model.dto.user;

import java.io.Serializable;

import lombok.Data;

/**
 * 用户更新请求
 *
 */
@Data
public class UserUpdateRequest implements Serializable {
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
     * 用户账号
     */
    private String account;
    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户角色：user/admin/ban
     */
    private String role;


    private static final long serialVersionUID = 1L;
}