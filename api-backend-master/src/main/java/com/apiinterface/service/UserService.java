package com.apiinterface.service;

import com.apiinterface.model.entity.User;
import com.apiinterface.model.vo.LoginUserVO;
import com.apiinterface.model.vo.UserVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.apiinterface.model.dto.user.UserQueryRequest;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务
 *
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @param inviteCode 邀请码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword,String inviteCode);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request HttpServletRequest
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);


    /**
     * 获取当前登录用户
     *
     * @param request HttpServletRequest
     * @return 用户对象
     */
    User getLoginUser(HttpServletRequest request);


    /**
     * 当前登录是否为管理员
     *
     * @param request HttpServletRequest
     * @return true 是管理员 false 不是管理员
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 判断某个用户是否是管理员
     * @param user 用户
     * @return true 是管理员 false 不是管理员
     */
    boolean isAdmin(User user);

    /**
     * 用户注销
     *
     * @param request HttpServletRequest
     * @return 是否成功
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取脱敏的已登录用户信息
     * @param user 用户对象
     * @return 脱敏后的用户对象
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param user 用户对象
     * @return 脱敏后的用户对象
     */
    UserVO getUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param userList 用户对象列表
     * @return 脱敏后的用户对象列表
     */
    List<UserVO> getUserVO(List<User> userList);

    /**
     * 封装查询条件
     *
     * @param userQueryRequest 查询数据对象
     * @return 查询条件对象
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 更新密钥
     * @param user
     * @return
     */
    boolean updateMyVoucher(User user);

    /**
     * 修改用户调用次数
     * @param userId
     * @param interfaceId
     * @return
     */
    boolean updateUserCallCount(Long userId,Long interfaceId);
}
