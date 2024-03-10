package com.apiinterface.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.apiinterface.common.ErrorCode;
import com.apiinterface.constant.CommonConstant;
import com.apiinterface.constant.UserConstant;
import com.apiinterface.exception.BusinessException;
import com.apiinterface.exception.ThrowUtils;
import com.apiinterface.mapper.UserMapper;
import com.apiinterface.model.dto.user.UserQueryRequest;
import com.apiinterface.model.entity.User;
import com.apiinterface.model.vo.LoginUserVO;
import com.apiinterface.model.vo.UserVO;
import com.apiinterface.service.UserService;
import com.apiinterface.utils.SqlUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.apiinterface.model.enums.UserRoleEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import static com.apiinterface.common.ErrorCode.OPERATION_ERROR;

/**
 * 用户服务实现
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "pengpeng";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String inviteCode) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        //synchronized 防止线程竞争
        synchronized (userAccount.intern()) {
            // 账户不能重复
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("account", userAccount);
            long count = this.baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
            }
            // 邀请码校验，给邀请人颁奖！
            boolean inviteUpdate = false;
            if(StringUtils.isNotEmpty(inviteCode)){
                QueryWrapper<User> wrapper = new QueryWrapper<>();
                wrapper.eq("invite_code", inviteCode);
                User one = getOne(wrapper);
                if (one == null) {
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "邀请码无效");
                }
                UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("id", one.getId());
                updateWrapper.setSql("call_count = call_count + 100"); //邀请奖励
                inviteUpdate = update(updateWrapper);
                ThrowUtils.throwIf(!inviteUpdate, ErrorCode.SYSTEM_ERROR);
            }
            // 2. 加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            //默认用户名
            String substring = userAccount.substring(userAccount.length() - 4);
            String userName = "用户" + substring;
            //随机生成密钥
            String accessKey = DigestUtils.md5DigestAsHex((SALT + userAccount + RandomUtil.randomNumbers(5)).getBytes());
            String secretKey = DigestUtils.md5DigestAsHex((SALT + userAccount + RandomUtil.randomNumbers(8)).getBytes());
            //生成邀请码
            String newInviteCode = RandomUtil.randomString(6);
            // 3. 插入数据
            User user = new User();
            // 邀请注册奖励
            if (inviteUpdate) {
                user.setCallCount(100L);
            }
            user.setAccount(userAccount);
            user.setUserName(userName);
            user.setAvatar("https://paopao-img-1320034963.cos.ap-beijing.myqcloud.com/images/userAvatar.png");
            user.setPassword(encryptPassword);
            user.setAccessKey(accessKey);
            user.setSecretKey(secretKey);
            user.setInviteCode(newInviteCode);
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }

            return user.getId();
        }
    }

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", userAccount);
        queryWrapper.eq("password", encryptPassword);
        User user = this.baseMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, account cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 3. 记录用户的登录态
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, user);
        return this.getLoginUserVO(user);
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }


    @Override
    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User user = (User) userObj;
        return isAdmin(user);
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getRole());
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        if (request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 移除登录态
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return true;
    }

    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public List<UserVO> getUserVO(List<User> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }

        Long id = userQueryRequest.getId();
        String userName = userQueryRequest.getUserName();
        String role = userQueryRequest.getRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(role), "role", role);
        queryWrapper.like(StringUtils.isNotBlank(userName), "userName", userName);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 更新密钥
     *
     * @param user
     * @return
     */
    @Override
    public boolean updateMyVoucher(User user) {
        String userAccount = user.getAccount();
        //随机生成密钥
        String accessKey = DigestUtils.md5DigestAsHex((SALT + userAccount + RandomUtil.randomNumbers(5)).getBytes());
        String secretKey = DigestUtils.md5DigestAsHex((SALT + userAccount + RandomUtil.randomNumbers(8)).getBytes());
        user.setAccessKey(accessKey);
        user.setSecretKey(secretKey);
        boolean result = updateById(user);
        ThrowUtils.throwIf(!result, OPERATION_ERROR);
        return true;
    }

    @Override
    public boolean updateUserCallCount(Long userId, Long interfaceId) {
        if (userId == null && interfaceId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", userId);
        wrapper.setSql("call_count = call_count - 1");

        return update(wrapper);
    }

}
