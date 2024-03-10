package com.apiinterface.model.dto.user;

import java.io.Serializable;
import lombok.Data;

/**
 * 用户创建请求
 *
 */
@Data
public class UserAddRequest implements Serializable {

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 账号
     */
    private String account;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户角色: user, admin
     */
    private String role;

    private static final long serialVersionUID = 1L;
}