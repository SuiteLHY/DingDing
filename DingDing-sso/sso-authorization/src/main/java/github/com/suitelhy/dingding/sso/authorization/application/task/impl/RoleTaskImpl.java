package github.com.suitelhy.dingding.sso.authorization.application.task.impl;

import github.com.suitelhy.dingding.core.infrastructure.application.PageImpl;
import github.com.suitelhy.dingding.core.infrastructure.domain.Page;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUserRole;
import github.com.suitelhy.dingding.security.service.api.domain.event.read.SecurityRoleReadEvent;
import github.com.suitelhy.dingding.security.service.api.domain.event.write.idempotence.SecurityRoleIdempotentEvent;
import github.com.suitelhy.dingding.security.service.api.domain.event.write.non.idempotence.SecurityRoleNonIdempotentEvent;
import github.com.suitelhy.dingding.security.service.api.domain.service.read.SecurityRoleReadService;
import github.com.suitelhy.dingding.security.service.api.domain.service.read.SecurityUserReadService;
import github.com.suitelhy.dingding.security.service.api.domain.service.write.idempotence.SecurityRoleIdempotentService;
import github.com.suitelhy.dingding.security.service.api.domain.service.write.idempotence.SecurityUserIdempotentService;
import github.com.suitelhy.dingding.security.service.api.domain.service.write.non.idempotence.SecurityRoleNonIdempotentService;
import github.com.suitelhy.dingding.security.service.api.domain.service.write.non.idempotence.SecurityUserNonIdempotentService;
import github.com.suitelhy.dingding.core.infrastructure.application.model.TaskResult;
import github.com.suitelhy.dingding.security.service.api.domain.vo.Security;
import github.com.suitelhy.dingding.security.service.api.infrastructure.web.AbstractSecurityUser;
import github.com.suitelhy.dingding.security.service.api.infrastructure.web.OAuth2AuthenticationInfo;
import github.com.suitelhy.dingding.sso.authorization.application.task.RoleTask;
import github.com.suitelhy.dingding.sso.authorization.infrastructure.application.dto.RoleDto;
import github.com.suitelhy.dingding.sso.authorization.infrastructure.application.dto.UserDto;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.user.service.api.domain.event.read.UserReadEvent;
import github.com.suitelhy.dingding.user.service.api.domain.event.write.idempotence.UserIdempotentWriteEvent;
import github.com.suitelhy.dingding.user.service.api.domain.event.write.non.idempotence.UserNonIdempotentWriteEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 角色 - 任务调度实现
 *
 * @see RoleTask
 */
@Service("roleTask")
@Slf4j
public class RoleTaskImpl
        implements RoleTask {

    @Reference
    private SecurityRoleReadService securityRoleReadService;

    @Reference
    private SecurityRoleIdempotentService securityRoleIdempotentService;

    @Reference
    private SecurityRoleNonIdempotentService securityRoleNonIdempotentService;

    @Reference
    private SecurityUserReadService securityUserReadService;

    @Reference
    private SecurityUserIdempotentService securityUserIdempotentService;

    @Reference
    private SecurityUserNonIdempotentService securityUserNonIdempotentService;

    @Reference
    private UserReadEvent userReadEvent;

    @Reference
    private UserIdempotentWriteEvent userIdempotentWriteEvent;

    @Reference
    private UserNonIdempotentWriteEvent userNonIdempotentWriteEvent;

    @Reference
    private SecurityRoleReadEvent securityRoleReadEvent;

    @Reference
    private SecurityRoleIdempotentEvent securityRoleIdempotentEvent;

    @Reference
    private SecurityRoleNonIdempotentEvent securityRoleNonIdempotentEvent;

    /**
     * 查询所有角色
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     *
     * @return {@link TaskResult}
     */
    private @NotNull TaskResult<Page<RoleDto>> selectAllRole(int pageIndex, int pageSize) {
        @NotNull TaskResult<Page<RoleDto>> taskResult;
        @NotNull Page<RoleDto> resultData = Page.empty();
        final @NotNull List<RoleDto> resultData_content = new ArrayList<>(0);
        try {
            @NotNull Page<SecurityRole> rolePage = securityRoleReadService.selectAll(pageIndex, pageSize);
            if (! rolePage.getContent().isEmpty()) {
                for (@NotNull SecurityRole each : rolePage) {
                    @NotNull RoleDto eachRole = RoleDto.Factory.ROLE_DTO.create(each);

                    resultData_content.add(eachRole);
                }
            }
            resultData = PageImpl.Factory.DEFAULT.create(resultData_content, rolePage);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.SUCCESS
                    , null
                    , resultData);
        } catch (IllegalArgumentException e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

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

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 查询所有角色
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     * @param operator  {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @Override
    public @NotNull TaskResult<Page<RoleDto>> selectAllRole(int pageIndex, int pageSize, @NotNull AbstractSecurityUser operator) {
        @NotNull TaskResult<Page<RoleDto>> taskResult;
        @NotNull Page<RoleDto> resultData = Page.empty();
        try {
            if (null == operator || operator.isEmpty()) {
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                        , "操作者"
                        , operator
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            if (! securityUserReadService.existAdminPermission(operator.getUsername())) {
                throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                        , "操作者"
                        , "无足够的操作权限"
                        , operator
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            return selectAllRole(pageIndex, pageSize);
        } catch (IllegalArgumentException e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

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

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 查询所有角色
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     * @param operator  {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @Override
    public @NotNull TaskResult<Page<RoleDto>> selectAllRole(int pageIndex, int pageSize, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator) {
        @NotNull TaskResult<Page<RoleDto>> taskResult;
        final @NotNull Page<RoleDto> resultData = Page.empty();
        try {
            if (null == operator) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("非法的身份认证凭证");
            }
            if (! Boolean.TRUE.equals(operator.isActive())) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("无效的身份认证凭证");
            }
            if (null == operator.getAuthorities()) {
                throw new AccessDeniedException("权限不足");
            } else {
                boolean isInsufficientPermissions = false;
                for (@NotNull String authority : operator.getAuthorities()) {
                    if (Security.RoleVo.USER.name().equals(authority)) {
                        isInsufficientPermissions = true;
                        break;
                    }
                }
                if (! isInsufficientPermissions) {
                    throw new AccessDeniedException("权限不足");
                }
            }

            return selectAllRole(pageIndex, pageSize);
        } catch (IllegalArgumentException e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

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

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 查询 (指定的) 角色
     *
     * @param roleCode 角色编码
     *
     * @return {@link TaskResult}
     */
    private @NotNull TaskResult<RoleDto> selectRoleByCode(@NotNull String roleCode) {
        @NotNull TaskResult<RoleDto> taskResult;
        @NotNull RoleDto resultData;
        try {
            if (null == roleCode || ! SecurityRole.Validator.ROLE.code(roleCode)) {
                //-- 非法参数: 角色编码
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "角色编码"
                        , roleCode
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            final @NotNull SecurityRole role = securityRoleReadService.selectRoleByCode(roleCode);

            if (null != role && ! role.isEmpty()) {
                resultData = RoleDto.Factory.ROLE_DTO.create(role);

                taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.SUCCESS
                        , null
                        , resultData);
            } else {
                resultData = RoleDto.Factory.ROLE_DTO.createDefault();

                taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.SUCCESS
                        , "指定数据不存在!"
                        , resultData);
            }
        } catch (IllegalArgumentException e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = RoleDto.Factory.ROLE_DTO.createDefault();

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

            resultData = RoleDto.Factory.ROLE_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 查询 (指定的) 角色
     *
     * @param roleCode  角色编码
     * @param operator  {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @Override
    public @NotNull TaskResult<RoleDto> selectRoleByCode(@NotNull String roleCode, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator) {
        @NotNull TaskResult<RoleDto> taskResult;
        @NotNull RoleDto resultData;
        try {
            if (null == operator) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("非法的身份认证凭证");
            }
            if (! Boolean.TRUE.equals(operator.isActive())) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("无效的身份认证凭证");
            }
            if (null == operator.getAuthorities()) {
                throw new AccessDeniedException("权限不足");
            } else {
                boolean isInsufficientPermissions = false;
                for (@NotNull String authority : operator.getAuthorities()) {
                    if (Security.RoleVo.USER.name().equals(authority)) {
                        isInsufficientPermissions = true;
                        break;
                    }
                }
                if (! isInsufficientPermissions) {
                    throw new AccessDeniedException("权限不足");
                }
            }

            return selectRoleByCode(roleCode);
        } catch (IllegalArgumentException e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = RoleDto.Factory.ROLE_DTO.createDefault();
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

            resultData = RoleDto.Factory.ROLE_DTO.createDefault();
            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 查询 (关联的) 角色
     *
     * @param username 用户名称
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     *
     * @return {@link TaskResult}
     */
    private @NotNull TaskResult<Page<RoleDto>> selectRoleByUsername(@NotNull String username, int pageIndex, int pageSize) {
        @NotNull TaskResult<Page<RoleDto>> taskResult;
        @NotNull Page<RoleDto> resultData = Page.empty();
        final @NotNull List<RoleDto> resultData_content = new ArrayList<>(0);
        try {
            if (null == username
                    || ! SecurityUserRole.Validator.USER_ROLE.username(username))
            {
                //-- 非法参数: 用户名称
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "用户名称"
                        , username
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            @NotNull Page<SecurityRole> rolePage = securityRoleReadEvent.selectPageRoleOnUserByUsername(username, pageIndex, pageSize);
            for (@NotNull SecurityRole each : rolePage) {
                final @NotNull RoleDto eachRole = RoleDto.Factory.ROLE_DTO.create(each.getCode()
                        , each.getName()
                        , each.getDescription());

                resultData_content.add(eachRole);
            }
            resultData = PageImpl.Factory.DEFAULT.create(resultData_content, rolePage);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.SUCCESS
                    , null
                    , resultData);
        } catch (IllegalArgumentException e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

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

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 查询 (关联的) 角色
     *
     * @param username  用户名称
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     * @param operator  {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @Override
    public @NotNull TaskResult<Page<RoleDto>> selectRoleByUsername(@NotNull String username, int pageIndex, int pageSize
            , OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator)
    {
        @NotNull TaskResult<Page<RoleDto>> taskResult;
        final @NotNull Page<RoleDto> resultData = Page.empty();
        try {
            if (null == operator) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("非法的身份认证凭证");
            }
            if (! Boolean.TRUE.equals(operator.isActive())) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("无效的身份认证凭证");
            }
            if (null == operator.getAuthorities()) {
                throw new AccessDeniedException("权限不足");
            } else {
                boolean isInsufficientPermissions = false;
                for (@NotNull String authority : operator.getAuthorities()) {
                    if (Security.RoleVo.USER.name().equals(authority)) {
                        isInsufficientPermissions = true;
                        break;
                    }
                }
                if (! isInsufficientPermissions) {
                    throw new AccessDeniedException("权限不足");
                }
            }

            return selectRoleByUsername(username, pageIndex, pageSize);
        } catch (IllegalArgumentException e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

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

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 添加角色
     *
     * @Description 添加单个角色.
     *
     * @param roleCode        {@link SecurityRole.Validator#code(String)}
     * @param roleName        {@link SecurityRole.Validator#name(String)}
     * @param roleDescription {@link SecurityRole.Validator#description(String)}
     *
     * @return {@link TaskResult}
     */
    private @NotNull TaskResult<RoleDto> addRole(@NotNull String roleCode
            , @NotNull String roleName
            , @Nullable String roleDescription
            , @NotNull String operator_username)
    {
        @NotNull TaskResult<RoleDto> taskResult;
        @NotNull RoleDto resultData;
        try {
            final @NotNull SecurityUser operator = userReadEvent.selectSecurityUserByUsername(operator_username);
            final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userReadEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

            final @NotNull SecurityRole new_role = SecurityRole.Factory.ROLE.create(roleCode, roleName, roleDescription);

            boolean insertRoleSuccessFlag = securityRoleIdempotentService.insert(new_role, operator);
            if (insertRoleSuccessFlag /*&& !new_role.equals(securityRoleService.selectRoleByCode(new_role.getCode()))*/) {
                /*throw new IllegalArgumentException("非法输入: （安全认证）角色 <- 指定的角色已存在!");*/
                //--- 业务操作成功
                resultData = RoleDto.Factory.ROLE_DTO.create(new_role);

                taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.SUCCESS
                        , null
                        , resultData);
            } else {
                //--- 业务操作失败
                resultData = RoleDto.Factory.ROLE_DTO.createDefault();

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

            resultData = RoleDto.Factory.ROLE_DTO.createDefault();

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

            resultData = RoleDto.Factory.ROLE_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 添加角色
     *
     * @Description 添加单个角色.
     *
     * @param roleCode          {@link SecurityRole.Validator#code(String)}
     * @param roleName          {@link SecurityRole.Validator#name(String)}
     * @param roleDescription   {@link SecurityRole.Validator#description(String)}
     * @param operator          {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @Override
    public @NotNull TaskResult<RoleDto> addRole(@NotNull String roleCode
            , @NotNull String roleName
            , @Nullable String roleDescription
            , @NotNull AbstractSecurityUser operator)
    {
        @NotNull TaskResult<RoleDto> taskResult;
        @NotNull RoleDto resultData;
        try {
            if (! securityUserReadService.existAdminPermission(operator.getUsername())) {
                throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "操作者"
                        , "无足够的操作权限"
                        , operator
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            return addRole(roleCode, roleName, roleDescription, operator.getUsername());
        } catch (IllegalArgumentException e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = RoleDto.Factory.ROLE_DTO.createDefault();

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

            resultData = RoleDto.Factory.ROLE_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 添加角色
     *
     * @Description 添加单个角色.
     *
     * @param roleCode          {@link SecurityRole.Validator#code(String)}
     * @param roleName          {@link SecurityRole.Validator#name(String)}
     * @param roleDescription   {@link SecurityRole.Validator#description(String)}
     * @param operator          {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @Override
    public @NotNull TaskResult<RoleDto> addRole(@NotNull String roleCode
            , @NotNull String roleName
            , @Nullable String roleDescription
            , OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator)
    {
        @NotNull TaskResult<RoleDto> taskResult;
        @NotNull RoleDto resultData;
        try {
            if (null == operator) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("非法的身份认证凭证");
            }
            if (! Boolean.TRUE.equals(operator.isActive())) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("无效的身份认证凭证");
            }
            if (null == operator.getAuthorities()) {
                throw new AccessDeniedException("权限不足");
            } else {
                boolean isInsufficientPermissions = false;
                for (@NotNull String authority : operator.getAuthorities()) {
                    for (Security.RoleVo eachRole : Security.RoleVo.ADMINISTRATOR_ROLE_VO__SET) {
                        if (eachRole.name().equals(authority)) {
                            isInsufficientPermissions = true;
                            break;
                        }
                    }
                }
                if (! isInsufficientPermissions) {
                    throw new AccessDeniedException("权限不足");
                }
            }

            return addRole(roleCode, roleName, roleDescription, operator.getUserName());
        } catch (IllegalArgumentException e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = RoleDto.Factory.ROLE_DTO.createDefault();

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

            resultData = RoleDto.Factory.ROLE_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 添加[(安全) 角色 - 用户]关联
     *
     * @param existedRole       {@link RoleDto}
     * @param existedUser       {@link UserDto}
     * @param operator_username [操作者 - 用户名称]
     *
     * @return {@link TaskResult}
     */
    private @NotNull TaskResult<Boolean> addRoleToUser(@NotNull RoleDto existedRole, @NotNull UserDto existedUser, @NotNull String operator_username) {
        @NotNull TaskResult<Boolean> taskResult;
        boolean resultData = false;
        try {
            if (null == existedRole || existedRole.isEmpty()) {
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "(安全) 角色"
                        , existedRole
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            if (null == existedUser || existedUser.isEmpty()) {
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "用户"
                        , existedUser
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            resultData = userIdempotentWriteEvent.insertUserRoleRelationship(securityUserReadService.selectByUsername(existedUser.getUsername())
                    , securityRoleReadService.selectRoleByCode(existedRole.getCode())
                    , securityUserReadService.selectByUsername(operator_username));
            if (resultData) {
                //--- 业务操作成功
                taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.SUCCESS
                        , null
                        , resultData);
            } else {
                //--- 业务操作失败
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

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 添加[(安全) 角色 - 用户]关联
     *
     * @param existedRole   {@link RoleDto}
     * @param existedUser   {@link UserDto}
     * @param operator      {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @Override
    public @NotNull TaskResult<Boolean> addRoleToUser(@NotNull RoleDto existedRole, @NotNull UserDto existedUser, @NotNull AbstractSecurityUser operator) {
        @NotNull TaskResult<Boolean> taskResult;
        boolean resultData = false;
        try {
            if (null == operator
                    || ! securityUserReadService.existAdminPermission(operator.getUsername())) {
                throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "操作者"
                        , "无足够的操作权限"
                        , operator
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            return addRoleToUser(existedRole, existedUser, operator.getUsername());
        } catch (IllegalArgumentException e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

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

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 添加[(安全) 角色 - 用户]关联
     *
     * @param existedRole   {@link RoleDto}
     * @param existedUser   {@link UserDto}
     * @param operator      {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @Override
    public @NotNull TaskResult<Boolean> addRoleToUser(@NotNull RoleDto existedRole, @NotNull UserDto existedUser, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator) {
        @NotNull TaskResult<Boolean> taskResult;
        boolean resultData = false;
        try {
            if (null == operator) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("非法的身份认证凭证");
            }
            if (! Boolean.TRUE.equals(operator.isActive())) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("无效的身份认证凭证");
            }
            if (null == operator.getAuthorities()) {
                throw new AccessDeniedException("权限不足");
            } else {
                boolean isInsufficientPermissions = false;
                for (@NotNull String authority : operator.getAuthorities()) {
                    for (Security.RoleVo eachRole : Security.RoleVo.ADMINISTRATOR_ROLE_VO__SET) {
                        if (eachRole.name().equals(authority)) {
                            isInsufficientPermissions = true;
                            break;
                        }
                    }
                }
                if (! isInsufficientPermissions) {
                    throw new AccessDeniedException("权限不足");
                }
            }

            return addRoleToUser(existedRole, existedUser, operator.getUserName());
        } catch (IllegalArgumentException e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

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

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 更新指定角色
     *
     * @Description 局部更新.
     *
     * @param old_roleCode          {@link SecurityRole.Validator#code(String)}
     * @param old_roleName          {@link SecurityRole.Validator#name(String)}
     * @param old_roleDescription   {@link SecurityRole.Validator#description(String)}
     * @param new_role_data         {@linkplain Map<String, Object> 替换的角色数据}
     *                            · 数据格式:
     *                            {
     *                            "role_name" : [角色名称],
     *                            "role_description" : [角色描述]
     *                            }
     * @param operator_username     [操作者 - 用户名称]
     *
     * @return {@link TaskResult}
     */
    private @NotNull TaskResult<RoleDto> updateRole(@NotNull String old_roleCode
            , @NotNull String old_roleName
            , @Nullable String old_roleDescription
            , @NotNull Map<String, Object> new_role_data
            , @NotNull String operator_username)
    {
        /*if (!SecurityRole.Validator.ROLE.code(old_roleCode)
                || !SecurityRole.Validator.ROLE.name(old_roleName)
                || !SecurityRole.Validator.ROLE.description(old_roleDescription)
                || null == new_role_data
                || (!new_role_data.containsKey("role_name") && !new_role_data.containsKey("role_description"))
                || !SecurityUser.Validator.USER.username(username)) {
            return null;
        }
        if (new_role_data.containsKey("role_name")
                && !SecurityRole.Validator.ROLE.name((String) new_role_data.get("role_name"))) {
            return null;
        }
        if (new_role_data.containsKey("role_description")
                && !SecurityRole.Validator.ROLE.description((String) new_role_data.get("role_description"))) {
            return null;
        }*/
        @NotNull TaskResult<RoleDto> taskResult;
        @NotNull RoleDto resultData;
        try {
            if (null == new_role_data || new_role_data.isEmpty()) {
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "替换的角色数据"
                        , new_role_data
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            final @NotNull SecurityRole old_role = SecurityRole.Factory.ROLE.create(old_roleCode
                    , old_roleName
                    , old_roleDescription);
            final @NotNull SecurityRole new_role = SecurityRole.Factory.ROLE.create(old_role.getCode()
                    , new_role_data.containsKey("role_name")
                            ? (String) new_role_data.get("role_name")
                            : old_role.getName()
                    , new_role_data.containsKey("role_description")
                            ? (String) new_role_data.get("role_description")
                            : old_role.getDescription());

            boolean updateRoleSuccessFlag = securityRoleNonIdempotentEvent.updateRole(old_role
                    , new_role_data
                    , securityUserReadService.selectByUsername(operator_username));
            // (业务操作是否符合预期)
            if (updateRoleSuccessFlag) {
                //--- 业务操作成功
                resultData = RoleDto.Factory.ROLE_DTO.create(new_role);

                taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.SUCCESS
                        , null
                        , resultData);
            } else {
                //--- 业务操作失败
                resultData = RoleDto.Factory.ROLE_DTO.create(old_role);

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

            resultData = RoleDto.Factory.ROLE_DTO.createDefault();

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

            resultData = RoleDto.Factory.ROLE_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 更新指定角色
     *
     * @Description 局部更新.
     *
     * @param old_roleCode          {@link SecurityRole.Validator#code(String)}
     * @param old_roleName          {@link SecurityRole.Validator#name(String)}
     * @param old_roleDescription   {@link SecurityRole.Validator#description(String)}
     * @param new_role_data         {@linkplain Map<String, Object> 替换的角色数据}
     *                              · 数据格式:
     *                              {
     *                              "role_name" : [角色名称],
     *                              "role_description" : [角色描述]
     *                              }
     * @param operator              {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @Override
    public @NotNull TaskResult<RoleDto> updateRole(@NotNull String old_roleCode
            , @NotNull String old_roleName
            , @Nullable String old_roleDescription
            , @NotNull Map<String, Object> new_role_data
            , @NotNull AbstractSecurityUser operator)
    {
        @NotNull TaskResult<RoleDto> taskResult;
        @NotNull RoleDto resultData;
        try {
            if (! securityUserReadService.existAdminPermission(operator.getUsername())) {
                throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "操作者"
                        , "无足够的操作权限"
                        , operator
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            return updateRole(old_roleCode
                    , old_roleName
                    , old_roleDescription
                    , new_role_data
                    , operator.getUsername());
        } catch (IllegalArgumentException e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = RoleDto.Factory.ROLE_DTO.createDefault();

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

            resultData = RoleDto.Factory.ROLE_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 更新指定角色
     *
     * @Description 局部更新.
     *
     * @param old_roleCode          {@link SecurityRole.Validator#code(String)}
     * @param old_roleName          {@link SecurityRole.Validator#name(String)}
     * @param old_roleDescription   {@link SecurityRole.Validator#description(String)}
     * @param new_role_data         {@linkplain Map<String, Object> 替换的角色数据}
     *                              · 数据格式:
     *                              {
     *                              "role_name" : [角色名称],
     *                              "role_description" : [角色描述]
     *                              }
     * @param operator              {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @Override
    public @NotNull TaskResult<RoleDto> updateRole(@NotNull String old_roleCode
            , @NotNull String old_roleName
            , @Nullable String old_roleDescription
            , @NotNull Map<String, Object> new_role_data
            , OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator)
    {
        @NotNull TaskResult<RoleDto> taskResult;
        @NotNull RoleDto resultData;
        try {
            if (null == operator) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("非法的身份认证凭证");
            }
            if (! Boolean.TRUE.equals(operator.isActive())) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("无效的身份认证凭证");
            }
            if (null == operator.getAuthorities()) {
                throw new AccessDeniedException("权限不足");
            } else {
                boolean isInsufficientPermissions = false;
                for (@NotNull String authority : operator.getAuthorities()) {
                    for (Security.RoleVo eachRole : Security.RoleVo.ADMINISTRATOR_ROLE_VO__SET) {
                        if (eachRole.name().equals(authority)) {
                            isInsufficientPermissions = true;
                            break;
                        }
                    }
                }
                if (! isInsufficientPermissions) {
                    throw new AccessDeniedException("权限不足");
                }
            }

            return updateRole(old_roleCode, old_roleName, old_roleDescription
                    , new_role_data, operator.getUserName());
        } catch (IllegalArgumentException e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = RoleDto.Factory.ROLE_DTO.createDefault();
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

            resultData = RoleDto.Factory.ROLE_DTO.createDefault();
            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 删除指定角色
     *
     * @param roleCode          {@link SecurityRole#getCode()}
     * @param operator_username [操作者 - 用户名称]
     *
     * @return {@link TaskResult}
     */
    private @NotNull TaskResult<RoleDto> deleteRole(@NotNull String roleCode, @NotNull String operator_username) {
        /*if (!SecurityRole.Validator.ROLE.code(roleCode)
                || !SecurityUser.Validator.USER.username(username)) {
            return null;
        }*/
        @NotNull TaskResult<RoleDto> taskResult;
        @NotNull RoleDto resultData;
        try {
            final @NotNull SecurityRole role = securityRoleReadService.selectRoleByCode(roleCode);

            boolean deleteRoleSuccessFlag = false;
            if (! role.isEmpty()) {
                deleteRoleSuccessFlag = securityRoleNonIdempotentEvent.deleteRole(role
                        , securityUserReadService.selectByUsername(operator_username));
            }

            // (业务操作是否符合预期)
            if (deleteRoleSuccessFlag) {
                //--- 业务操作成功
                resultData = RoleDto.Factory.ROLE_DTO.create(role);

                taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.SUCCESS
                        , null
                        , resultData);
            } else {
                //--- 业务操作失败
                if (! securityRoleReadService.existsByCode(role.getCode())) {
                    resultData = RoleDto.Factory.ROLE_DTO.createDefault();

                    taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE_INPUT_PARAMETER
                            , "指定的角色不存在"
                            , resultData);
                } else {
                    resultData = RoleDto.Factory.ROLE_DTO.create(role);

                    taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE_BUSINESS
                            , null
                            , resultData);
                }
            }
        } catch (IllegalArgumentException e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = RoleDto.Factory.ROLE_DTO.createDefault();

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

            resultData = RoleDto.Factory.ROLE_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 删除指定角色
     *
     * @param roleCode  {@link SecurityRole#getCode()}
     * @param operator  {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @Override
    public @NotNull TaskResult<RoleDto> deleteRole(@NotNull String roleCode, @NotNull AbstractSecurityUser operator) {
        @NotNull TaskResult<RoleDto> taskResult;
        @NotNull RoleDto resultData;
        try {
            if (! securityUserReadService.existAdminPermission(operator.getUsername())) {
                throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "操作者"
                        , "无足够的操作权限"
                        , operator
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            return deleteRole(roleCode, operator.getUsername());
        } catch (IllegalArgumentException e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = RoleDto.Factory.ROLE_DTO.createDefault();

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

            resultData = RoleDto.Factory.ROLE_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 删除指定角色
     *
     * @param roleCode  {@link SecurityRole#getCode()}
     * @param operator  {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @Override
    public @NotNull TaskResult<RoleDto> deleteRole(@NotNull String roleCode, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator) {
        @NotNull TaskResult<RoleDto> taskResult;
        @NotNull RoleDto resultData;
        try {
            if (null == operator) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("非法的身份认证凭证");
            }
            if (! Boolean.TRUE.equals(operator.isActive())) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("无效的身份认证凭证");
            }
            if (null == operator.getAuthorities()) {
                throw new AccessDeniedException("权限不足");
            } else {
                boolean isInsufficientPermissions = false;
                for (@NotNull String authority : operator.getAuthorities()) {
                    for (Security.RoleVo eachRole : Security.RoleVo.ADMINISTRATOR_ROLE_VO__SET) {
                        if (eachRole.name().equals(authority)) {
                            isInsufficientPermissions = true;
                            break;
                        }
                    }
                }
                if (! isInsufficientPermissions) {
                    throw new AccessDeniedException("权限不足");
                }
            }

            return deleteRole(roleCode, operator.getUserName());
        } catch (IllegalArgumentException e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = RoleDto.Factory.ROLE_DTO.createDefault();

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

            resultData = RoleDto.Factory.ROLE_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

}
