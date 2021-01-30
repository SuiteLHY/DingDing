package github.com.suitelhy.dingding.core.application.task.impl;

import github.com.suitelhy.dingding.core.application.task.LogTask;
import github.com.suitelhy.dingding.core.application.task.UserTask;
import github.com.suitelhy.dingding.core.domain.entity.User;
import github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.core.domain.entity.UserPersonInfo;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.core.domain.event.UserEvent;
import github.com.suitelhy.dingding.core.domain.service.UserAccountOperationInfoService;
import github.com.suitelhy.dingding.core.domain.service.UserPersonInfoService;
import github.com.suitelhy.dingding.core.domain.service.UserService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityUserService;
import github.com.suitelhy.dingding.core.infrastructure.application.dto.BasicUserDto;
import github.com.suitelhy.dingding.core.infrastructure.application.dto.UserDto;
import github.com.suitelhy.dingding.core.infrastructure.application.model.TaskResult;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.security.Security;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.core.infrastructure.web.AbstractSecurityUser;
import github.com.suitelhy.dingding.core.infrastructure.web.OAuth2AuthenticationInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息 - 业务调度实现
 *
 * @see UserTask
 */
@Service("userTask")
// 通过 lombok 使用 Slf4j 日志框架
@Slf4j
public class UserTaskImpl
        implements UserTask {

    @Autowired
    private SecurityUserService securityUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserAccountOperationInfoService userAccountOperationInfoService;

    @Autowired
    private UserPersonInfoService userPersonInfoService;

    @Autowired
    private UserEvent userEvent;

    @Autowired
    private LogTask logTask;

    /**
     * 查询用户列表
     *
     * @param pageCount 页码, 从1开始
     * @param pageSize  每页数据容量, 最大值为 20
     * @return {@link TaskResult}
     */
    private @NotNull
    TaskResult<List<UserDto>> selectAll(int pageCount, int pageSize) {
        @NotNull TaskResult<List<UserDto>> result;
        @NotNull List<UserDto> resultData = new ArrayList<>(0);
        try {
            if (pageCount < 1) {
                //-- 非法输入: 页码
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "页码"
                        , pageCount
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            if (pageSize < 5 || pageSize > 20) {
                //-- 非法输入: 每页容量
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "每页容量"
                        , pageSize
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            final List<User> userList = userService.selectAll(--pageCount, pageSize).getContent();
            /*final List<UserAccountOperationInfo> userAccountOperationInfoList = userAccountOperationInfoService.selectAll(--pageCount, pageSize)
                    .getContent();
            final List<UserPersonInfo> userPersonInfoServiceList = userPersonInfoService.selectAll(--pageCount, pageSize)
                    .getContent();*/
            if (!userList.isEmpty()) {
                for (final User eachUser : userList) {
                    /*resultData.add(UserDto.Factory.USER_DTO.create(new SecurityUser(each, passwordEncoder)));*/
                    final UserAccountOperationInfo eachUser_OperationInfo;
                    final UserPersonInfo eachUser_PersonInfo;

                    /*for (int i = 0; i < userAccountOperationInfoList.size(); i++) {
                        UserAccountOperationInfo eachOperationInfo = userAccountOperationInfoList.get(i);
                        if (eachOperationInfo.equals(eachUser)) {
                            eachUser_OperationInfo = eachOperationInfo;

                            userAccountOperationInfoList.remove(i);
                            break;
                        }
                    }*/
                    eachUser_OperationInfo = userAccountOperationInfoService.selectByUsername(eachUser.getUsername());
                    if (null == eachUser_OperationInfo || eachUser_OperationInfo.isEmpty()) {
                        break;
                    }

                    /*for (int i = 0; i < userPersonInfoServiceList.size(); i++) {
                        UserPersonInfo eachPersonIfo = userPersonInfoServiceList.get(i);
                        if (eachPersonIfo.equals(eachUser)) {
                            eachUser_PersonInfo = eachPersonIfo;

                            userPersonInfoServiceList.remove(i);
                            break;
                        }
                    }*/
                    eachUser_PersonInfo = userPersonInfoService.selectByUsername(eachUser.getUsername());
                    if (null == eachUser_PersonInfo || eachUser_PersonInfo.isEmpty()) {
                        break;
                    }

                    resultData.add(UserDto.Factory.USER_DTO.create(eachUser, eachUser_OperationInfo, eachUser_PersonInfo));
                }
            }

            result = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.SUCCESS, null, resultData);
        } catch (Exception e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            result = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE, null, resultData);
            result.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return result;
    }

    /**
     * 查询用户列表
     *
     * @param pageCount 页码, 从1开始
     * @param pageSize  每页数据容量, 最大值为 20
     * @param operator  操作者
     * @return 操作结果 {@link TaskResult}
     */
    @Override
    public @NotNull
    TaskResult<List<UserDto>> selectAll(int pageCount, int pageSize, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator) {
        @NotNull TaskResult<List<UserDto>> result;
        @NotNull List<UserDto> resultData;
        try {
            if (null == operator) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("非法的身份认证凭证");
            }
            if (!Boolean.TRUE.equals(operator.isActive())) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("无效的身份认证凭证");
            }
            if (null == operator.getAuthorities()) {
                throw new AccessDeniedException("权限不足");
            } else {
                boolean isInsufficientPermissions = false;
                for (String authority : operator.getAuthorities()) {
                    if (Security.RoleVo.USER.name().equals(authority)) {
                        isInsufficientPermissions = true;
                        break;
                    }
                }
                if (!isInsufficientPermissions) {
                    throw new AccessDeniedException("权限不足");
                }
            }

            return selectAll(pageCount, pageSize);
        } catch (Exception e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = new ArrayList<>(0);

            result = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            result.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return result;
    }

    /**
     * 查询指定的用户
     *
     * @param userid 用户编码
     * @return {@link TaskResult}
     */
    private @NotNull
    TaskResult<UserDto> selectUserByUserid(@NotNull String userid) {
        @NotNull TaskResult<UserDto> taskResult;
        @NotNull UserDto resultData;
        try {
            /*resultData = UserDto.Factory.USER_DTO.create(
                    new SecurityUser(userService.selectUserByUserid(userid), passwordEncoder));*/
            final @NotNull User user = userService.selectUserByUserid(userid);
            if (null == user || user.isEmpty()) {
                throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "用户编码"
                        , "无效的用户"
                        , userid
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            /*UserAccountOperationInfo userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(user.getUsername());*/
            final UserAccountOperationInfo userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(user.getUsername());

            /*UserPersonInfo userPersonInfo = userPersonInfoService.selectByUsername(user.getUsername());*/
            final UserPersonInfo userPersonInfo = userEvent.selectUserPersonInfoByUsername(user.getUsername());

            resultData = UserDto.Factory.USER_DTO.create(user, userAccountOperationInfo, userPersonInfo);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.SUCCESS
                    , null
                    , resultData);
        } catch (Exception e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = UserDto.Factory.USER_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 查询指定的用户
     *
     * @param userid
     * @param operator 操作者
     * @return 操作结果     {@link TaskResult}
     */
    @Override
    public @NotNull
    TaskResult<UserDto> selectUserByUserid(@NotNull String userid, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator) {
        @NotNull TaskResult<UserDto> taskResult;
        @NotNull UserDto resultData;
        try {
            if (null == operator) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("非法的身份认证凭证");
            }
            if (!Boolean.TRUE.equals(operator.isActive())) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("无效的身份认证凭证");
            }
            if (null == operator.getAuthorities()) {
                throw new AccessDeniedException("权限不足");
            } else {
                boolean isInsufficientPermissions = false;
                for (String authority : operator.getAuthorities()) {
                    if (Security.RoleVo.USER.name().equals(authority)) {
                        isInsufficientPermissions = true;
                        break;
                    }
                }
                if (!isInsufficientPermissions) {
                    throw new AccessDeniedException("权限不足");
                }
            }

            return selectUserByUserid(userid);
        } catch (Exception e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = UserDto.Factory.USER_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 查询指定的用户
     *
     * @param username 用户名称
     * @return {@link TaskResult}
     */
    private @NotNull
    TaskResult<UserDto> selectUserByUsername(@NotNull String username) {
        @NotNull TaskResult<UserDto> taskResult;
        @NotNull UserDto resultData;
        try {
            /*resultData = UserDto.Factory.USER_DTO.create(
                    new SecurityUser(userService.selectUserByUsername(username), passwordEncoder));*/
            final @NotNull User user = userService.selectUserByUsername(username);
            if (null == user || user.isEmpty()) {
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "用户名称"
                        , username
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            /*UserAccountOperationInfo userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(username);*/
            final UserAccountOperationInfo userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(username);

            /*UserPersonInfo userPersonInfo = userPersonInfoService.selectByUsername(username);*/
            final UserPersonInfo userPersonInfo = userEvent.selectUserPersonInfoByUsername(username);

            resultData = UserDto.Factory.USER_DTO.create(user, userAccountOperationInfo, userPersonInfo);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.SUCCESS
                    , null
                    , resultData);
        } catch (Exception e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = UserDto.Factory.USER_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 查询指定的用户
     *
     * @param username
     * @param operator 操作者
     * @return 操作结果     {@link TaskResult}
     */
    @Override
    public @NotNull
    TaskResult<UserDto> selectUserByUsername(String username, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator) {
        @NotNull TaskResult<UserDto> taskResult;
        @NotNull UserDto resultData;
        try {
            if (null == operator) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("非法的身份认证凭证");
            }
            if (!Boolean.TRUE.equals(operator.isActive())) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("无效的身份认证凭证");
            }
            if (null == operator.getAuthorities()) {
                throw new AccessDeniedException("权限不足");
            } else {
                boolean isInsufficientPermissions = false;
                for (String authority : operator.getAuthorities()) {
                    if (Security.RoleVo.USER.name().equals(authority)) {
                        isInsufficientPermissions = true;
                        break;
                    }
                }
                if (!isInsufficientPermissions) {
                    throw new AccessDeniedException("权限不足");
                }
            }

            return selectUserByUsername(username);
        } catch (Exception e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = UserDto.Factory.USER_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE, null, resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 查询用户列表 - 分页 - 总页数
     *
     * @param pageSize 分页 - 每页容量
     * @return [分页 - 总页数]   {@link TaskResult}
     */
    private @NotNull
    TaskResult<Long> selectCount(int pageSize) {
        @NotNull TaskResult<Long> taskResult;
        @NotNull Long resultData;
        try {
            if (pageSize < 5 || pageSize > 20) {
                //-- 非法输入: 每页容量
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "每页容量"
                        , pageSize
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            resultData = userService.selectCount(pageSize);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.SUCCESS
                    , null
                    , resultData);
        } catch (Exception e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = 0L;

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 查询用户列表 - 分页 - 总页数
     *
     * @param pageSize 分页 - 每页容量
     * @param operator 操作者
     * @return 操作结果, [分页 - 总页数]   {@link TaskResult}
     */
    @Override
    public @NotNull
    TaskResult<Long> selectCount(int pageSize, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator) {
        @NotNull TaskResult<Long> taskResult;
        long resultData;
        try {
            if (null == operator) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("非法的身份认证凭证");
            }
            if (!Boolean.TRUE.equals(operator.isActive())) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("无效的身份认证凭证");
            }
            if (null == operator.getAuthorities()) {
                throw new AccessDeniedException("权限不足");
            } else {
                boolean isInsufficientPermissions = false;
                for (String authority : operator.getAuthorities()) {
                    if (Security.RoleVo.USER.name().equals(authority)) {
                        isInsufficientPermissions = true;
                        break;
                    }
                }
                if (!isInsufficientPermissions) {
                    throw new AccessDeniedException("权限不足");
                }
            }

            return selectCount(pageSize);
        } catch (Exception e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = 0L;

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 新增一个用户
     *
     * @param userDto           用户          {@link UserDto}
     * @param passwordPlaintext 用户密码_明文
     * @param operator_username [操作者 -> 用户名称]
     * @return 操作结果   {@link TaskResult}
     */
    private @NotNull
    TaskResult<UserDto> registerUser(@NotNull UserDto userDto, @NotNull String passwordPlaintext, @NotNull String operator_username) {
        @NotNull TaskResult<UserDto> taskResult;
        @NotNull UserDto resultData;
        try {
            if (null == userDto) {
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "用户"
                        , userDto
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            if (!User.Validator.USER.passwordPlaintext(passwordPlaintext)) {
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "用户密码_明文"
                        , passwordPlaintext
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            final @NotNull User newUser = userDto.dtoId(userDto.getUsername(), passwordPlaintext);
            if (null == newUser) {
                throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s</description>->【%s】&【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "用户"
                        , "用户密码"
                        , userDto
                        , passwordPlaintext
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            final @NotNull UserAccountOperationInfo newUser_UserAccountOperationInfo = userDto.dtoId_UserAccountOperationInfo(newUser);
            if (null == newUser_UserAccountOperationInfo
                    || !newUser_UserAccountOperationInfo.isEntityLegal()) {
                //-- 非法参数: 用户 <- [User - 拓展属性]-[用户 -> 账户操作基础记录]为空
                throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】&【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "用户"
                        , "[User - 拓展属性]-[用户 -> 账户操作基础记录]为空"
                        , userDto
                        , newUser_UserAccountOperationInfo
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            final @NotNull UserPersonInfo newUser_UserPersonInfo = userDto.dtoId_UserPersonInfo(newUser);
            if (null == newUser_UserPersonInfo
                    || !newUser_UserPersonInfo.isEntityLegal()) {
                //-- 非法参数: 用户 <- [User - 拓展属性]-[用户 -> 个人信息]为空
                throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】&【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "用户"
                        , "[User - 拓展属性]-[用户 -> 个人信息]为空"
                        , userDto
                        , newUser_UserPersonInfo
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            boolean userRegisterSuccessFlag = userEvent.registerUser(newUser, newUser_UserAccountOperationInfo, newUser_UserPersonInfo
                    , securityUserService.selectByUsername(operator_username));
            if (userRegisterSuccessFlag) {
                resultData = userDto;

                taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.SUCCESS
                        , null
                        , resultData);
            } else {
                resultData = UserDto.Factory.USER_DTO.createDefault();

                taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE_BUSINESS
                        , null
                        , resultData);
            }
        } catch (Exception e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = UserDto.Factory.USER_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 新增一个用户
     *
     * @param userDto           用户          {@link UserDto}
     * @param passwordPlaintext 用户密码_明文
     * @param operator          操作者         {@link AbstractSecurityUser}
     * @return 操作结果   {@link TaskResult}
     */
    @Override
    public @NotNull
    TaskResult<UserDto> registerUser(@NotNull UserDto userDto, @NotNull String passwordPlaintext, @NotNull AbstractSecurityUser operator) {
        @NotNull TaskResult<UserDto> taskResult;
        @NotNull UserDto resultData;
        try {
            if (null == operator || operator.isEmpty()) {
                //-- 非法参数: 操作者
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "操作者"
                        , operator
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            final @NotNull UserAccountOperationInfo operator_OperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
            if (null == operator_OperationInfo || operator_OperationInfo.isEmpty()) {
                //-- 非法参数: 操作者 <- 无有效的账户操作记录
                throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "操作者"
                        , "无有效的账户操作记录"
                        , operator_OperationInfo
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            if (!securityUserService.existAdminPermission(operator.getUsername())) {
                //-- 非法参数: 操作者 <- 没有足够的操作权限
                throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "操作者"
                        , "没有足够的操作权限"
                        , operator
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            return registerUser(userDto, passwordPlaintext, operator.getUsername());
        } catch (Exception e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = UserDto.Factory.USER_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE, null, resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 新增一个用户
     *
     * @param userDto           用户          {@link UserDto}
     * @param passwordPlaintext 用户密码_明文
     * @param operator          操作者
     * @return 操作结果   {@link TaskResult}
     */
    @Override
    public @NotNull
    TaskResult<UserDto> registerUser(@NotNull UserDto userDto, @NotNull String passwordPlaintext, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator) {
        @NotNull TaskResult<UserDto> taskResult;
        @NotNull UserDto resultData;
        try {
            if (null == operator) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("非法的身份认证凭证");
            }
            if (!Boolean.TRUE.equals(operator.isActive())) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("无效的身份认证凭证");
            }
            if (null == operator.getAuthorities()) {
                throw new AccessDeniedException("权限不足");
            } else {
                boolean isInsufficientPermissions = false;
                for (String authority : operator.getAuthorities()) {
                    for (Security.RoleVo eachRole : Security.RoleVo.ADMINISTRATOR_ROLE_VO__SET) {
                        if (eachRole.name().equals(authority)) {
                            isInsufficientPermissions = true;
                            break;
                        }
                    }
                }
                if (!isInsufficientPermissions) {
                    throw new AccessDeniedException("权限不足");
                }
            }

            return registerUser(userDto, passwordPlaintext, operator.getUserName());
        } catch (Exception e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = UserDto.Factory.USER_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE, null, resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 更新指定的用户
     *
     * @param userDto           用户                              {@link UserDto}
     * @param operator_username [操作者（具备管理员权限） - 用户名称]
     * @return 操作结果   {@link TaskResult}
     * @Description 更新的数据包括[用户 - 基础信息]和[用户 -> 个人信息].
     */
    private @NotNull
    TaskResult<UserDto> update(@NotNull UserDto userDto, @NotNull String operator_username) {
        @NotNull TaskResult<UserDto> taskResult;
        @NotNull UserDto resultData;
        try {
            final @NotNull User user_OldData = userService.selectUserByUsername(userDto.getUsername());
            if (null == user_OldData || user_OldData.isEmpty()) {
                throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s</description>->【%s】&【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "用户"
                        , "已存在的[用户 - 基础信息]"
                        , userDto
                        , user_OldData
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            final @NotNull User user = userDto.dtoId(user_OldData);
            if (null == user || user.isEmpty()) {
                throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s</description>->【%s】&【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "用户"
                        , "[用户 - 基础信息]"
                        , userDto
                        , user
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            final @NotNull UserAccountOperationInfo userAccountOperationInfo = userDto.dtoId_UserAccountOperationInfo(user);
            if (null == userAccountOperationInfo || userAccountOperationInfo.isEmpty()) {
                throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s</description>->【%s】&【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "用户"
                        , "[用户 -> 账户操作基础记录]"
                        , userDto
                        , userAccountOperationInfo
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            final @NotNull UserPersonInfo userPersonInfo = userDto.dtoId_UserPersonInfo(user);
            if (null == userPersonInfo || userPersonInfo.isEmpty()) {
                throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s</description>->【%s】&【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "用户"
                        , "[用户 -> 个人信息]"
                        , userDto
                        , userPersonInfo
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            final @NotNull SecurityUser operator_SecurityUser;
            boolean userUpdateSuccessFlag = userEvent.updateUser(user, operator_SecurityUser = securityUserService.selectByUsername(operator_username));
            if (userUpdateSuccessFlag) {
                userUpdateSuccessFlag = userEvent.updateUserPersonInfo(userPersonInfo, operator_SecurityUser);
            }

            if (userUpdateSuccessFlag) {
                resultData = userDto;

                taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.SUCCESS
                        , null
                        , resultData);
            } else {
                resultData = UserDto.Factory.USER_DTO.createDefault();

                taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE_BUSINESS
                        , null
                        , resultData);
            }
        } catch (IllegalArgumentException e) {
            //--- 非法参数异常
            log.warn("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = UserDto.Factory.USER_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE_INPUT_PARAMETER
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        } catch (BusinessAtomicException e) {
            //--- 业务原子性异常
            log.warn("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = UserDto.Factory.USER_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE_BUSINESS
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        } catch (Exception e) {
            //--- 其他异常
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = UserDto.Factory.USER_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 更新指定的用户
     *
     * @param userDto  用户                    {@link UserDto}
     * @param operator 操作者（具备管理员权限）    {@link AbstractSecurityUser}
     * @return 操作结果   {@link TaskResult}
     * @Description 更新的数据包括[用户 - 基础信息]和[用户 -> 个人信息].
     */
    @NotNull
    @Override
    public TaskResult<UserDto> update(@NotNull UserDto userDto, @NotNull AbstractSecurityUser operator) {
        @NotNull TaskResult<UserDto> taskResult;
        @NotNull UserDto resultData;
        try {
            if (!securityUserService.existAdminPermission(operator.getUsername())) {
                throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "操作者"
                        , "无足够的操作权限"
                        , operator
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            return update(userDto, operator.getUsername());
        } catch (IllegalArgumentException e) {
            //--- 非法参数异常
            log.warn("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = UserDto.Factory.USER_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE_INPUT_PARAMETER
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        } catch (Exception e) {
            //--- 其他异常
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = UserDto.Factory.USER_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 更新指定的用户
     *
     * @param userDto  用户                    {@link UserDto}
     * @param operator 操作者（具备管理员权限）
     * @return 操作结果   {@link TaskResult}
     * @Description 更新的数据包括[用户 - 基础信息]和[用户 -> 个人信息].
     */
    @Override
    public @NotNull
    TaskResult<UserDto> update(@NotNull UserDto userDto, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator) {
        @NotNull TaskResult<UserDto> taskResult;
        @NotNull UserDto resultData;
        try {
            if (null == operator) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("非法的身份认证凭证");
            }
            if (!Boolean.TRUE.equals(operator.isActive())) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("无效的身份认证凭证");
            }
            if (null == operator.getAuthorities()) {
                throw new AccessDeniedException("权限不足");
            } else {
                boolean isInsufficientPermissions = false;
                for (String authority : operator.getAuthorities()) {
                    for (Security.RoleVo eachRole : Security.RoleVo.ADMINISTRATOR_ROLE_VO__SET) {
                        if (eachRole.name().equals(authority)) {
                            isInsufficientPermissions = true;
                            break;
                        }
                    }
                }
                if (!isInsufficientPermissions) {
                    throw new AccessDeniedException("权限不足");
                }
            }

            return update(userDto, operator.getUserName());
        } catch (IllegalArgumentException e) {
            //--- 非法参数异常
            log.warn("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = UserDto.Factory.USER_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE_INPUT_PARAMETER
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        } catch (Exception e) {
            //--- 其他异常
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = UserDto.Factory.USER_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 更新指定的用户 -> [用户 -> 个人信息]
     *
     * @param userDto           用户              {@link UserDto}
     * @param operator_username [操作者 - 用户名称]
     * @return 操作结果   {@link TaskResult}
     * @Description 更新的数据包括[用户 -> 个人信息].
     */
    private @NotNull
    TaskResult<UserDto> updateUserPersonInfo(@NotNull UserDto userDto, @NotNull String operator_username) {
        @NotNull TaskResult<UserDto> taskResult;
        @NotNull UserDto resultData;
        try {
            final @NotNull User user_OldData = userService.selectUserByUsername(userDto.getUsername());
            if (null == user_OldData || user_OldData.isEmpty()) {
                throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s</description>->【%s】&【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "用户"
                        , "已存在的[用户 - 基础信息]"
                        , userDto
                        , user_OldData
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            /*final @NotNull User user = userDto.dtoId(user_OldData);
            if (null == user || user.isEmpty()) {
                throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s</description>->【%s】&【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "用户"
                        , "[用户 - 基础信息]"
                        , userDto
                        , user
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }*/
            final @NotNull UserAccountOperationInfo userAccountOperationInfo = userDto.dtoId_UserAccountOperationInfo(/*user*/user_OldData);
            if (null == userAccountOperationInfo || userAccountOperationInfo.isEmpty()) {
                throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s</description>->【%s】&【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "用户"
                        , "[用户 -> 账户操作基础记录]"
                        , userDto
                        , userAccountOperationInfo
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            final @NotNull UserPersonInfo userPersonInfo = userDto.dtoId_UserPersonInfo(/*user*/user_OldData);
            if (null == userPersonInfo || userPersonInfo.isEmpty()) {
                throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s</description>->【%s】&【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "用户"
                        , "[用户 -> 个人信息]"
                        , userDto
                        , userPersonInfo
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            boolean userUpdateSuccessFlag = userEvent.updateUserPersonInfo(userPersonInfo, securityUserService.selectByUsername(operator_username));

            if (userUpdateSuccessFlag) {
                resultData = userDto;

                taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.SUCCESS
                        , null
                        , resultData);
            } else {
                resultData = UserDto.Factory.USER_DTO.createDefault();

                taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE_BUSINESS
                        , null
                        , resultData);
            }
        } catch (IllegalArgumentException e) {
            //--- 非法参数异常
            log.warn("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = UserDto.Factory.USER_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE_INPUT_PARAMETER
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        } catch (BusinessAtomicException e) {
            //--- 业务原子性异常
            log.warn("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = UserDto.Factory.USER_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE_BUSINESS
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        } catch (Exception e) {
            //--- 其他异常
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = UserDto.Factory.USER_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 更新指定的用户 -> [用户 -> 个人信息]
     *
     * @param userDto  用户      {@link UserDto}
     * @param operator 操作者    {@link AbstractSecurityUser}
     * @return 操作结果   {@link TaskResult}
     * @Description 更新的数据包括[用户 -> 个人信息].
     */
    @NotNull
    @Override
    public TaskResult<UserDto> updateUserPersonInfo(@NotNull UserDto userDto, @NotNull AbstractSecurityUser operator) {
        @NotNull TaskResult<UserDto> taskResult;
        @NotNull UserDto resultData;
        try {
            if (null == operator || operator.isEmpty()) {
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "操作者"
                        , operator
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            if (!securityUserService.existAdminPermission(operator.getUsername())
                    && !(null != userDto && userDto.isDtoLegal() && userDto.equals(operator))) {
                throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "操作者"
                        , "无足够的操作权限"
                        , operator
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            return updateUserPersonInfo(userDto, operator.getUsername());
        } catch (IllegalArgumentException e) {
            //--- 非法参数异常
            log.warn("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = UserDto.Factory.USER_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE_INPUT_PARAMETER
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        } catch (Exception e) {
            //--- 其他异常
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = UserDto.Factory.USER_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 更新指定的用户 -> [用户 -> 个人信息]
     *
     * @param userDto  用户      {@link UserDto}
     * @param operator 操作者
     * @return 操作结果   {@link TaskResult}
     * @Description 更新的数据包括[用户 -> 个人信息].
     */
    @Override
    public @NotNull
    TaskResult<UserDto> updateUserPersonInfo(@NotNull UserDto userDto, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator) {
        @NotNull TaskResult<UserDto> taskResult;
        @NotNull UserDto resultData;
        try {
            if (null == operator) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("非法的身份认证凭证");
            }
            if (!Boolean.TRUE.equals(operator.isActive())) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("无效的身份认证凭证");
            }
            if (null == operator.getAuthorities()) {
                throw new AccessDeniedException("权限不足");
            } else {
                boolean hasSufficientPermissions = (null != userDto && userDto.isDtoLegal() && userDto.equals(operator));
                if (!hasSufficientPermissions) {
                    for (String authority : operator.getAuthorities()) {
                        for (Security.RoleVo eachRole : Security.RoleVo.ADMINISTRATOR_ROLE_VO__SET) {
                            if (eachRole.name().equals(authority)) {
                                hasSufficientPermissions = true;
                                break;
                            }
                        }
                    }
                }
                if (!hasSufficientPermissions) {
                    throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                            , "操作者"
                            , "无足够的操作权限"
                            , operator
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
            }

            return updateUserPersonInfo(userDto, operator.getUserName());
        } catch (IllegalArgumentException e) {
            //--- 非法参数异常
            log.warn("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = UserDto.Factory.USER_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE_INPUT_PARAMETER
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        } catch (Exception e) {
            //--- 其他异常
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = UserDto.Factory.USER_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 删除指定的用户
     *
     * @param userDto           用户              {@link BasicUserDto}
     * @param operator_username [操作者 - 用户名称]
     * @return 操作结果   {@link TaskResult}
     */
    private @NotNull
    TaskResult<BasicUserDto> delete(@NotNull BasicUserDto userDto, @NotNull String operator_username) {
        @NotNull TaskResult<BasicUserDto> taskResult;
        @NotNull BasicUserDto resultData;
        try {
            if (null == userDto || !userDto.isDtoLegal()) {
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "用户"
                        , userDto
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            final @NotNull User existedUser = userService.selectUserByUsername(userDto.getUsername());
            /*final @NotNull User user = userDto.dtoId(existedUser);*/
            if (null == existedUser || existedUser.isEmpty()) {
                throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s</description>->【%s】&【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "用户"
                        , "不存在有效的[用户 - 基础信息]"
                        , userDto
                        , existedUser
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            boolean userDeleteSuccessFlag = userEvent.deleteUser(existedUser, securityUserService.selectByUsername(operator_username));
            if (userDeleteSuccessFlag) {
                resultData = userDto;

                taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.SUCCESS
                        , null
                        , resultData);
            } else {
                resultData = BasicUserDto.Factory.USER_DTO.createDefault();

                taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE_BUSINESS
                        , null
                        , resultData);
            }
        } catch (IllegalArgumentException e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = BasicUserDto.Factory.USER_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE_INPUT_PARAMETER
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        } catch (Exception e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = BasicUserDto.Factory.USER_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 删除指定的用户
     *
     * @param userDto  用户      {@link BasicUserDto}
     * @param operator 操作者    {@link AbstractSecurityUser}
     * @return 操作结果   {@link TaskResult}
     */
    @Override
    public @NotNull
    TaskResult<BasicUserDto> delete(@NotNull BasicUserDto userDto/*, @NotNull String passwordPlaintext*/, @NotNull AbstractSecurityUser operator) {
        @NotNull TaskResult<BasicUserDto> taskResult;
        @NotNull BasicUserDto resultData;
        try {
            if (!(!userDto.isEmpty() && userDto.equals(operator))
                    && !securityUserService.existAdminPermission(operator.getUsername())) {
                throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "操作者"
                        , "无足够的操作权限"
                        , operator
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            return delete(userDto, operator.getUsername());
        } catch (IllegalArgumentException e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = BasicUserDto.Factory.USER_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE_INPUT_PARAMETER
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        } catch (Exception e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = BasicUserDto.Factory.USER_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 删除指定的用户
     *
     * @param userDto  用户      {@link BasicUserDto}
     * @param operator 操作者
     * @return 操作结果   {@link TaskResult}
     */
    @Override
    public @NotNull
    TaskResult<BasicUserDto> delete(@NotNull BasicUserDto userDto, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator) {
        @NotNull TaskResult<BasicUserDto> taskResult;
        @NotNull BasicUserDto resultData;
        try {
            if (null == operator) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("非法的身份认证凭证");
            }
            if (!Boolean.TRUE.equals(operator.isActive())) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("无效的身份认证凭证");
            }
            if (null == operator.getAuthorities()) {
                throw new AccessDeniedException("权限不足");
            } else {
                boolean hasSufficientPermissions = (null != userDto && !userDto.isEmpty() && userDto.equals(operator));
                if (!hasSufficientPermissions) {
                    for (String authority : operator.getAuthorities()) {
                        for (Security.RoleVo eachRole : Security.RoleVo.ADMINISTRATOR_ROLE_VO__SET) {
                            if (eachRole.name().equals(authority)) {
                                hasSufficientPermissions = true;
                                break;
                            }
                        }
                    }
                }
                if (!hasSufficientPermissions) {
                    throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                            , "操作者"
                            , "无足够的操作权限"
                            , operator
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
            }

            return delete(userDto, operator.getUserName());
        } catch (IllegalArgumentException e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = BasicUserDto.Factory.USER_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE_INPUT_PARAMETER
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        } catch (Exception e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = BasicUserDto.Factory.USER_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

}