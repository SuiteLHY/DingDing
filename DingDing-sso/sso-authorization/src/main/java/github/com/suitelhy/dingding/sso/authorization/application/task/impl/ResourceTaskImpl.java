package github.com.suitelhy.dingding.sso.authorization.application.task.impl;

import github.com.suitelhy.dingding.core.infrastructure.application.PageImpl;
import github.com.suitelhy.dingding.core.infrastructure.application.model.TaskResult;
import github.com.suitelhy.dingding.core.infrastructure.domain.Page;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.HashSet;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Resource;
import github.com.suitelhy.dingding.core.infrastructure.util.Toolbox;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityResource;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityResourceUrl;
import github.com.suitelhy.dingding.security.service.api.domain.event.read.SecurityResourceReadEvent;
import github.com.suitelhy.dingding.security.service.api.domain.event.read.SecurityRoleReadEvent;
import github.com.suitelhy.dingding.security.service.api.domain.event.write.idempotence.SecurityResourceIdempotentEvent;
import github.com.suitelhy.dingding.security.service.api.domain.event.write.idempotence.SecurityRoleIdempotentEvent;
import github.com.suitelhy.dingding.security.service.api.domain.event.write.non.idempotence.SecurityResourceNonIdempotentEvent;
import github.com.suitelhy.dingding.security.service.api.domain.event.write.non.idempotence.SecurityRoleNonIdempotentEvent;
import github.com.suitelhy.dingding.security.service.api.domain.service.read.SecurityResourceReadService;
import github.com.suitelhy.dingding.security.service.api.domain.service.read.SecurityRoleReadService;
import github.com.suitelhy.dingding.security.service.api.domain.service.read.SecurityUserReadService;
import github.com.suitelhy.dingding.security.service.api.domain.service.write.idempotence.SecurityResourceIdempotentService;
import github.com.suitelhy.dingding.security.service.api.domain.service.write.idempotence.SecurityRoleIdempotentService;
import github.com.suitelhy.dingding.security.service.api.domain.service.write.idempotence.SecurityUserIdempotentService;
import github.com.suitelhy.dingding.security.service.api.domain.service.write.non.idempotence.SecurityResourceNonIdempotentService;
import github.com.suitelhy.dingding.security.service.api.domain.service.write.non.idempotence.SecurityRoleNonIdempotentService;
import github.com.suitelhy.dingding.security.service.api.domain.service.write.non.idempotence.SecurityUserNonIdempotentService;
import github.com.suitelhy.dingding.security.service.api.domain.vo.Security;
import github.com.suitelhy.dingding.security.service.api.infrastructure.web.AbstractSecurityUser;
import github.com.suitelhy.dingding.security.service.api.infrastructure.web.OAuth2AuthenticationInfo;
import github.com.suitelhy.dingding.sso.authorization.application.task.ResourceTask;
import github.com.suitelhy.dingding.sso.authorization.infrastructure.application.dto.ResourceDto;
import github.com.suitelhy.dingding.sso.authorization.infrastructure.application.dto.RoleDto;
import github.com.suitelhy.dingding.user.service.api.domain.event.read.UserReadEvent;
import github.com.suitelhy.dingding.user.service.api.domain.event.write.idempotence.UserIdempotentWriteEvent;
import github.com.suitelhy.dingding.user.service.api.domain.event.write.non.idempotence.UserNonIdempotentWriteEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * 资源 - 任务调度实现
 *
 * @see ResourceTask
 */
@Service("resourceTask")
@Slf4j
public class ResourceTaskImpl
        implements ResourceTask {

    @Reference
    private SecurityResourceReadService securityResourceReadService;

    @Reference
    private SecurityResourceIdempotentService securityResourceIdempotentService;

    @Reference
    private SecurityResourceNonIdempotentService securityResourceNonIdempotentService;

    @Reference
    private SecurityRoleReadService securityRoleReadService;

    @Reference
    private SecurityRoleIdempotentService securityRoleIdempotentService;

    @Reference
    private SecurityRoleNonIdempotentService securityRoleNonIdempotentService;

    /*@Autowired
    private SecurityRoleResourceService securityRoleResourceService;*/

    @Reference
    private SecurityUserReadService securityUserReadService;

    @Reference
    private SecurityUserIdempotentService securityUserIdempotentService;

    @Reference
    private SecurityUserNonIdempotentService securityUserNonIdempotentService;

    @Reference
    private SecurityRoleReadEvent securityRoleReadEvent;

    @Reference
    private SecurityRoleIdempotentEvent securityRoleIdempotentEvent;

    @Reference
    private SecurityRoleNonIdempotentEvent securityRoleNonIdempotentEvent;

    @Reference
    private SecurityResourceReadEvent securityResourceReadEvent;

    @Reference
    private SecurityResourceIdempotentEvent securityResourceIdempotentEvent;

    @Reference
    private SecurityResourceNonIdempotentEvent securityResourceNonIdempotentEvent;

    @Reference
    private UserReadEvent userReadEvent;

    @Reference
    private UserIdempotentWriteEvent userIdempotentWriteEvent;

    @Reference
    private UserNonIdempotentWriteEvent userNonIdempotentWriteEvent;

    /**
     * 查询所有资源
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     *
     * @return {@link TaskResult}
     */
    private @NotNull TaskResult<Page<ResourceDto>> selectAllResource(int pageIndex, int pageSize) {
        @NotNull TaskResult<Page<ResourceDto>> taskResult;
        @NotNull Page<ResourceDto> resultData = Page.empty();
        final @NotNull List<ResourceDto> resultData_content = new ArrayList<>(0);
        try {
            @NotNull Page<SecurityResource> roleList = securityResourceReadService.selectAll(pageIndex, pageSize);
            if (! roleList.getContent().isEmpty()) {
                for (@NotNull SecurityResource each : roleList) {
                    @NotNull ResourceDto eachRole = ResourceDto.Factory.RESOURCE_DTO.create(each);

                    resultData_content.add(eachRole);
                }
            }
            resultData = PageImpl.Factory.DEFAULT.create(resultData_content, roleList);

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
     * 查询所有资源
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     * @param operator  {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @Override
    public @NotNull TaskResult<Page<ResourceDto>> selectAllResource(int pageIndex, int pageSize, @NotNull AbstractSecurityUser operator) {
        @NotNull TaskResult<Page<ResourceDto>> taskResult;
        final @NotNull Page<ResourceDto> resultData = Page.empty();
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

            return selectAllResource(pageIndex, pageSize);
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
     * 查询所有资源
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     * @param operator  操作者
     *
     * @return {@link TaskResult}
     */
    @Override
    public @NotNull TaskResult<Page<ResourceDto>> selectAllResource(int pageIndex, int pageSize, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator) {
        @NotNull TaskResult<Page<ResourceDto>> taskResult;
        final @NotNull Page<ResourceDto> resultData = Page.empty();
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

            return selectAllResource(pageIndex, pageSize);
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
     * 查询 (指定的) 资源
     *
     * @param resourceCode 资源编码
     *
     * @return {@link TaskResult}
     */
    private @NotNull TaskResult<ResourceDto> selectResourceByCode(@NotNull String resourceCode) {
        @NotNull TaskResult<ResourceDto> taskResult;
        @NotNull ResourceDto resultData;
        try {
            if (null == resourceCode || ! SecurityResource.Validator.RESOURCE.code(resourceCode)) {
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "资源编码"
                        , resourceCode
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            final SecurityResource result = securityResourceReadService.selectResourceByCode(resourceCode);
            if (null != result && ! result.isEmpty()) {
                resultData = ResourceDto.Factory.RESOURCE_DTO.create(result);

                taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.SUCCESS
                        , null
                        , resultData);
            } else {
                resultData = ResourceDto.Factory.RESOURCE_DTO.createDefault();

                taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.SUCCESS
                        , null
                        , resultData);
            }
        } catch (IllegalArgumentException e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = ResourceDto.Factory.RESOURCE_DTO.createDefault();
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

            resultData = ResourceDto.Factory.RESOURCE_DTO.createDefault();
            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 查询 (指定的) 资源
     *
     * @param resourceCode 资源编码
     *
     * @return {@link TaskResult}
     */
    @Override
    public @NotNull TaskResult<ResourceDto> selectResourceByCode(@NotNull String resourceCode, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator) {
        @NotNull TaskResult<ResourceDto> taskResult;
        @NotNull ResourceDto resultData;
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

            return selectResourceByCode(resourceCode);
        } catch (IllegalArgumentException e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = ResourceDto.Factory.RESOURCE_DTO.createDefault();

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

            resultData = ResourceDto.Factory.RESOURCE_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 查询 (关联的) 资源
     *
     * @param roleCode 角色编码
     *
     * @return {@link ResourceDto} 集合
     */
    private @NotNull TaskResult<List<ResourceDto>> selectResourceByRoleCode(@NotNull String roleCode) {
        @NotNull TaskResult<List<ResourceDto>> taskResult;
        final @NotNull List<ResourceDto> resultData = new ArrayList<>(0);
        try {
            final @NotNull List<SecurityResource> resourceList = securityResourceReadEvent.selectResourceOnRoleByRoleCode(roleCode);
            if (! resourceList.isEmpty()) {
                for (@NotNull SecurityResource eachResource : resourceList) {
                    if (eachResource.isEmpty()) {
                        continue;
                    }

                    resultData.add(ResourceDto.Factory.RESOURCE_DTO.create(eachResource));
                }
            }

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
     * 查询 (关联的) 资源
     *
     * @param roleCode 角色编码
     *
     * @return {@link ResourceDto} 集合
     */
    @Override
    public @NotNull TaskResult<List<ResourceDto>> selectResourceByRoleCode(@NotNull String roleCode, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator) {
        @NotNull TaskResult<List<ResourceDto>> taskResult;
        final @NotNull List<ResourceDto> resultData = new ArrayList<>(0);
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

            return selectResourceByRoleCode(roleCode);
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
     * 查询 (关联的) 资源
     *
     * @param username 用户名称
     *
     * @return {@link ResourceDto} 集合
     */
    private @NotNull TaskResult<List<ResourceDto>> selectResourceByUsername(@NotNull String username) {
        @NotNull TaskResult<List<ResourceDto>> taskResult;
        final @NotNull List<ResourceDto> resultData = new ArrayList<>(0);
        try {
            @NotNull List<SecurityResource> roleList = userReadEvent.selectResourceOnUserByUsername(username);
            if (! roleList.isEmpty()) {
                for (SecurityResource each : roleList) {
                    resultData.add(ResourceDto.Factory.RESOURCE_DTO.create(each));
                }
            }

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.SUCCESS
                    , null
                    , resultData);
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
     * 查询 (关联的) 资源
     *
     * @param username  用户名称
     * @param operator  {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link ResourceDto} 集合
     */
    @Override
    public @NotNull TaskResult<List<ResourceDto>> selectResourceByUsername(@NotNull String username
            , OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator)
    {
        @NotNull TaskResult<List<ResourceDto>> taskResult;
        final @NotNull List<ResourceDto> resultData = new ArrayList<>(0);
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

            return selectResourceByUsername(username);
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
     * 查询 (关联的) [URL 信息]
     *
     * @param resourceCode 资源编码
     *
     * @return {@link TaskResult}
     */
    private @NotNull TaskResult<ResourceDto> selectUrlInfoByResourceCode(@NotNull String resourceCode) {
        @NotNull TaskResult<ResourceDto> taskResult;
        @NotNull ResourceDto resultData;
        try {
            if (null == resourceCode || ! SecurityResource.Validator.RESOURCE.code(resourceCode)) {
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "资源编码"
                        , resourceCode
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            boolean existedEffectedData = false;

            final SecurityResource result_resource = securityResourceReadService.selectResourceByCode(resourceCode);
            if (null != result_resource && ! result_resource.isEmpty()) {
                existedEffectedData = true;
            }

            if (existedEffectedData) {
                //--- 存在有效（安全）资源的情况
                final @NotNull List<SecurityResourceUrl> result_urlInfo = securityResourceReadEvent.selectUrlInfoOnResourceByResourceCode(resourceCode);

                if (null != result_urlInfo && ! result_urlInfo.isEmpty()) {
                    final @NotNull HashSet<Map<String, Object>> urlInfoSet = new HashSet<>(0);

                    for (@NotNull SecurityResourceUrl eachUrlInfo : result_urlInfo) {
                        final @NotNull Map<String, Object> urlInfoMap = new HashMap<>(3);
                        urlInfoMap.put("clientId", eachUrlInfo.getClientId());
                        urlInfoMap.put("urlPath", eachUrlInfo.getUrlPath());
                        urlInfoMap.put("urlMethod", eachUrlInfo.getUrlMethod());

                        urlInfoSet.add(urlInfoMap);
                    }

                    resultData = ResourceDto.Factory.RESOURCE_DTO.create(result_resource, urlInfoSet);
                } else {
                    resultData = ResourceDto.Factory.RESOURCE_DTO.create(result_resource);
                }
            } else {
                //--- 不存在有效（安全）资源的情况
                resultData = ResourceDto.Factory.RESOURCE_DTO.createDefault();
            }

            if (existedEffectedData) {
                taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.SUCCESS
                        , null
                        , resultData);
            } else {
                taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE_INPUT_PARAMETER
                        , null
                        , resultData);
            }
        } catch (IllegalArgumentException e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);
            /* System.err.println(String.format("===== selectUrlInfoByResourceCode =====\n%s", e.getMessage()));*/

            resultData = ResourceDto.Factory.RESOURCE_DTO.createDefault();

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

            resultData = ResourceDto.Factory.RESOURCE_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 查询 (关联的) [URL 信息]
     *
     * @param resourceCode  资源编码
     * @param operator      {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @Override
    public @NotNull TaskResult<ResourceDto> selectUrlInfoByResourceCode(@NotNull String resourceCode, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator) {
        @NotNull TaskResult<ResourceDto> taskResult;
        @NotNull ResourceDto resultData;
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

            return selectUrlInfoByResourceCode(resourceCode);
        } catch (IllegalArgumentException e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = ResourceDto.Factory.RESOURCE_DTO.createDefault();

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

            resultData = ResourceDto.Factory.RESOURCE_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 添加资源
     *
     * @Description 添加单个资源.
     *
     * @param resourceCode       资源编码              {@link ResourceDto#getCode()}
     * @param resourceIcon       图标                 {@link ResourceDto#getIcon()}
     * @param resourceLink       资源链接              {@link ResourceDto#getLink()}
     * @param resourceName       资源名称              {@link ResourceDto#getName()}
     * @param resourceParentCode [父节点 <- 资源编码]   {@link ResourceDto#getParentCode()}
     * @param resourceSort       序号                 {@link ResourceDto#getSort()}
     * @param resourceType_Value [资源类型 -> VO 的值] {@link ResourceDto#getType()}
     * @param operator_username  [操作者 - 用户名称]
     *
     * @return {@link TaskResult}
     */
    private @NotNull TaskResult<ResourceDto> addResource(@NotNull String resourceCode
            , String resourceIcon
            , String resourceLink
            , @NotNull String resourceName
            , String resourceParentCode
            , @NotNull Integer resourceSort
            , Integer resourceType_Value
            , @NotNull String operator_username)
    {
        @NotNull TaskResult<ResourceDto> taskResult;
        @NotNull ResourceDto resultData;
        try {
            final @NotNull SecurityResource new_resource = SecurityResource.Factory.RESOURCE.create(resourceCode
                    , resourceIcon
                    , resourceLink
                    , resourceName
                    , resourceParentCode
                    , resourceSort
                    , Toolbox.VoUtil.getInstance().getVoByValue(Resource.TypeVo.class, resourceType_Value));

            boolean insertResourceSuccessFlag = securityResourceIdempotentEvent.insertResource(new_resource
                    , securityUserReadService.selectByUsername(operator_username));
            if (insertResourceSuccessFlag) {
                //--- 业务操作成功
                resultData = ResourceDto.Factory.RESOURCE_DTO.create(new_resource);

                taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.SUCCESS
                        , null
                        , resultData);
            } else {
                //--- 业务操作失败
                resultData = ResourceDto.Factory.RESOURCE_DTO.createDefault();

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

            resultData = ResourceDto.Factory.RESOURCE_DTO.createDefault();

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

            resultData = ResourceDto.Factory.RESOURCE_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 添加资源
     *
     * @Description 添加单个资源.
     *
     * @param resourceCode          {@linkplain ResourceDto#getCode() 资源编码}
     * @param resourceIcon          {@linkplain ResourceDto#getIcon() 图标}
     * @param resourceLink          {@linkplain ResourceDto#getLink() 资源链接}
     * @param resourceName          {@linkplain ResourceDto#getName() 资源名称}
     * @param resourceParentCode    {@linkplain ResourceDto#getParentCode() [父节点 <- 资源编码]}
     * @param resourceSort          {@linkplain ResourceDto#getSort() 序号}
     * @param resourceType_Value    {@linkplain ResourceDto#getType() [资源类型 -> VO 的值]}
     * @param operator              {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @Override
    public @NotNull TaskResult<ResourceDto> addResource(@NotNull String resourceCode
            , String resourceIcon
            , String resourceLink
            , @NotNull String resourceName
            , String resourceParentCode
            , @NotNull Integer resourceSort
            , Integer resourceType_Value
            , @NotNull AbstractSecurityUser operator)
    {
        @NotNull TaskResult<ResourceDto> taskResult;
        @NotNull ResourceDto resultData;
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

            return addResource(resourceCode
                    , resourceIcon
                    , resourceLink
                    , resourceName
                    , resourceParentCode
                    , resourceSort
                    , resourceType_Value
                    , operator.getUsername());
        } catch (IllegalArgumentException e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = ResourceDto.Factory.RESOURCE_DTO.createDefault();
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

            resultData = ResourceDto.Factory.RESOURCE_DTO.createDefault();
            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 添加资源
     *
     * @Description 添加单个资源.
     *
     * @param resourceCode          {@linkplain ResourceDto#getCode() 资源编码}
     * @param resourceIcon          {@linkplain ResourceDto#getIcon() 图标}
     * @param resourceLink          {@linkplain ResourceDto#getLink() 资源链接}
     * @param resourceName          {@linkplain ResourceDto#getName() 资源名称}
     * @param resourceParentCode    {@linkplain ResourceDto#getParentCode() [父节点 <- 资源编码]}
     * @param resourceSort          {@linkplain ResourceDto#getSort() 序号}
     * @param resourceType_Value    {@linkplain ResourceDto#getType() [资源类型 -> VO 的值]}
     * @param operator              {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @Override
    public @NotNull TaskResult<ResourceDto> addResource(@NotNull String resourceCode
            , String resourceIcon
            , String resourceLink
            , @NotNull String resourceName
            , String resourceParentCode
            , @NotNull Integer resourceSort
            , Integer resourceType_Value
            , OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator)
    {
        @NotNull TaskResult<ResourceDto> taskResult;
        @NotNull ResourceDto resultData;
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

            return addResource(resourceCode
                    , resourceIcon
                    , resourceLink
                    , resourceName
                    , resourceParentCode
                    , resourceSort
                    , resourceType_Value
                    , operator.getUserName());
        } catch (IllegalArgumentException e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = ResourceDto.Factory.RESOURCE_DTO.createDefault();

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

            resultData = ResourceDto.Factory.RESOURCE_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 添加[资源 - 角色]关联
     *
     * @param existedResource   {@linkplain ResourceDto [(安全) 资源]}
     * @param existedRole       {@linkplain RoleDto [(安全) 角色]}
     * @param operator_username [操作者 - 用户名称]
     *
     * @return {@link TaskResult}
     */
    private @NotNull TaskResult<Boolean> addResourceToRole(@NotNull ResourceDto existedResource, @NotNull RoleDto existedRole, @NotNull String operator_username) {
        @NotNull TaskResult<Boolean> taskResult;
        boolean resultData = false;
        try {
            if (null == existedResource || existedResource.isEmpty()) {
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "(安全) 资源"
                        , existedResource
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            if (null == existedRole || existedRole.isEmpty()) {
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "(安全) 角色"
                        , existedRole
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }


            @NotNull Set<SecurityResource> resources = new java.util.HashSet<>(1);
            resources.addAll(securityRoleReadEvent.selectResourceOnRoleByRoleCode(existedResource.getCode()));

            resultData = securityRoleNonIdempotentEvent.insertRoleResourceRelationship(securityRoleReadService.selectRoleByCode(existedRole.getCode())
                    , resources
                    , userReadEvent.selectSecurityUserByUsername(operator_username));
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
     * 添加[资源 - 角色]关联
     *
     * @param existedResource   {@linkplain ResourceDto [(安全) 资源]}
     * @param existedRole       {@linkplain RoleDto [(安全) 角色]}
     * @param operator          {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @Override
    public @NotNull TaskResult<Boolean> addResourceToRole(@NotNull ResourceDto existedResource, @NotNull RoleDto existedRole, @NotNull AbstractSecurityUser operator) {
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

            return addResourceToRole(existedResource, existedRole, operator.getUsername());
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
     * 添加[资源 - 角色]关联
     *
     * @param existedResource   {@linkplain ResourceDto [(安全) 资源]}
     * @param existedRole       {@linkplain RoleDto [(安全) 角色]}
     * @param operator          {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @Override
    public @NotNull TaskResult<Boolean> addResourceToRole(@NotNull ResourceDto existedResource
            , @NotNull RoleDto existedRole
            , OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator)
    {
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

            return addResourceToRole(existedResource, existedRole, operator.getUserName());
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
     * 添加[资源 - URL]关联
     *
     * @param existedResource   {@linkplain ResourceDto [(安全) 资源]}
     * @param urlInfo           {@linkplain RoleDto [URL 信息]}
     * @param operator_username [操作者 - 用户名称]
     *
     * @return {@link TaskResult}
     */
    private @NotNull TaskResult<Boolean> addUrlToResource(@NotNull ResourceDto existedResource, @NotNull String[] urlInfo, @NotNull String operator_username) {
        @NotNull TaskResult<Boolean> taskResult;
        boolean resultData = false;
        try {
            if (null == existedResource || existedResource.isEmpty()) {
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "(安全) 资源"
                        , existedResource
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            if (! SecurityResourceUrl.Validator.RESOURCE_URL.urlInfo(urlInfo)) {
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "[URL 信息]"
                        , Arrays.toString(urlInfo)
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            resultData = securityResourceNonIdempotentEvent.insertResourceUrlRelationship(securityResourceReadService.selectResourceByCode(existedResource.getCode())
                    , urlInfo
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
     * 添加[资源 - URL]关联
     *
     * @param existedResource   {@linkplain ResourceDto [(安全) 资源]}
     * @param urlInfo           {@linkplain RoleDto [URL 信息]}
     * @param operator          {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @Override
    public @NotNull TaskResult<Boolean> addUrlToResource(@NotNull ResourceDto existedResource, @NotNull String[] urlInfo, @NotNull AbstractSecurityUser operator) {
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

            return addUrlToResource(existedResource, urlInfo, operator.getUsername());
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
     * 添加[资源 - URL]关联
     *
     * @param existedResource   {@linkplain ResourceDto [(安全) 资源]}
     * @param urlInfo           {@linkplain RoleDto [URL 信息]}
     * @param operator          {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @Override
    public @NotNull TaskResult<Boolean> addUrlToResource(@NotNull ResourceDto existedResource
            , @NotNull String[] urlInfo
            , OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator)
    {
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

            return addUrlToResource(existedResource, urlInfo, operator.getUserName());
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
     * 更新指定资源
     *
     * @Description 局部更新.
     *
     * @param old_resource_data 被替换的资源数据    {@link Map <String, Object>}
     *                          · 数据格式:
     *                          {
     *                          "resource_code" : [资源编码],
     *                          "resource_icon" : [图标],
     *                          "resource_link" : [资源链接],
     *                          "resource_name" : [资源名称],
     *                          "resource_parentCode" : [父节点 <- 资源编码],
     *                          "resource_sort" : [序号],
     *                          "resource_type_value" : [资源类型 -> VO 的值]
     *                          }
     * @param new_resource_data 替换的资源数据     {@link Map <String, Object>}
     *                          · 数据格式:
     *                          {
     *                          "resource_icon" : [图标],
     *                          "resource_link" : [资源链接],
     *                          "resource_name" : [资源名称],
     *                          "resource_parentCode" : [父节点 <- 资源编码],
     *                          "resource_sort" : [序号],
     *                          "resource_type_value" : [资源类型 -> VO 的值]
     *                          }
     * @param operator_username [操作者 - 用户名称]
     *
     * @return {@link TaskResult}
     */
    private @NotNull TaskResult<ResourceDto> updateResource(@NotNull @NotNull Map<String, Object> old_resource_data
            , @NotNull Map<String, Object> new_resource_data
            , @NotNull String operator_username)
    {
        @NotNull TaskResult<ResourceDto> taskResult;
        @NotNull ResourceDto resultData;
        try {
            if (null == old_resource_data || old_resource_data.isEmpty()) {
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "被替换的资源数据"
                        , old_resource_data
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            if (null == new_resource_data || new_resource_data.isEmpty()) {
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "替换的资源数据"
                        , new_resource_data
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            final @NotNull SecurityResource old_resource = SecurityResource.Factory.RESOURCE.create((String) old_resource_data.get("resource_code")
                    , (String) old_resource_data.get("resource_icon")
                    , (String) old_resource_data.get("resource_link")
                    , (String) old_resource_data.get("resource_name")
                    , (String) old_resource_data.get("resource_parentCode")
                    , (Integer) old_resource_data.get("resource_sort")
                    , Toolbox.VoUtil.getInstance().getVoByValue(Resource.TypeVo.class, (Integer) old_resource_data.get("resource_type_value")));
            final @NotNull SecurityResource new_resource = SecurityResource.Factory.RESOURCE.create((String) old_resource_data.get("resource_code")
                    , (String) new_resource_data.get("resource_icon")
                    , (String) new_resource_data.get("resource_link")
                    , (String) new_resource_data.get("resource_name")
                    , (String) new_resource_data.get("resource_parentCode")
                    , (Integer) new_resource_data.get("resource_sort")
                    , Toolbox.VoUtil.getInstance().getVoByValue(Resource.TypeVo.class, (Integer) new_resource_data.get("resource_type_value")));

            boolean updateResourceSuccessFlag = securityResourceNonIdempotentService.update(old_resource
                    , new_resource_data
                    , securityUserReadService.selectByUsername(operator_username));
            // (业务操作是否符合预期)
            if (updateResourceSuccessFlag) {
                //--- 业务操作成功
                resultData = ResourceDto.Factory.RESOURCE_DTO.create(new_resource);

                taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.SUCCESS
                        , null
                        , resultData);
            } else {
                //--- 业务操作失败
                resultData = ResourceDto.Factory.RESOURCE_DTO.create(old_resource);

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

            resultData = ResourceDto.Factory.RESOURCE_DTO.createDefault();

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

            resultData = ResourceDto.Factory.RESOURCE_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 更新指定资源
     *
     * @Description 局部更新.
     *
     * @param old_resource_data {@linkplain Map<String, Object> 被替换的资源数据}
     *                          · 数据格式:
     *                          {
     *                          "resource_code" : [资源编码],
     *                          "resource_icon" : [图标],
     *                          "resource_link" : [资源链接],
     *                          "resource_name" : [资源名称],
     *                          "resource_parentCode" : [父节点 <- 资源编码],
     *                          "resource_sort" : [序号],
     *                          "resource_type_value" : [资源类型 -> VO 的值]
     *                          }
     * @param new_resource_data {@linkplain Map<String, Object> 替换的资源数据}
     *                          · 数据格式:
     *                          {
     *                          "resource_icon" : [图标],
     *                          "resource_link" : [资源链接],
     *                          "resource_name" : [资源名称],
     *                          "resource_parentCode" : [父节点 <- 资源编码],
     *                          "resource_sort" : [序号],
     *                          "resource_type_value" : [资源类型 -> VO 的值]
     *                          }
     * @param operator          {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @Override
    public @NotNull TaskResult<ResourceDto> updateResource(@NotNull @NotNull Map<String, Object> old_resource_data
            , @NotNull Map<String, Object> new_resource_data
            , @NotNull AbstractSecurityUser operator)
    {
        @NotNull TaskResult<ResourceDto> taskResult;
        @NotNull ResourceDto resultData;
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

            return updateResource(old_resource_data, new_resource_data, operator.getUsername());
        } catch (IllegalArgumentException e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = ResourceDto.Factory.RESOURCE_DTO.createDefault();
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

            resultData = ResourceDto.Factory.RESOURCE_DTO.createDefault();
            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 更新指定资源
     *
     * @Description 局部更新.
     *
     * @param old_resource_data {@linkplain Map<String, Object> 被替换的资源数据}
     *                          · 数据格式:
     *                          {
     *                          "resource_code" : [资源编码],
     *                          "resource_icon" : [图标],
     *                          "resource_link" : [资源链接],
     *                          "resource_name" : [资源名称],
     *                          "resource_parentCode" : [父节点 <- 资源编码],
     *                          "resource_sort" : [序号],
     *                          "resource_type_value" : [资源类型 -> VO 的值]
     *                          }
     * @param new_resource_data {@linkplain Map<String, Object> 替换的资源数据}
     *                          · 数据格式:
     *                          {
     *                          "resource_icon" : [图标],
     *                          "resource_link" : [资源链接],
     *                          "resource_name" : [资源名称],
     *                          "resource_parentCode" : [父节点 <- 资源编码],
     *                          "resource_sort" : [序号],
     *                          "resource_type_value" : [资源类型 -> VO 的值]
     *                          }
     * @param operator          {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @Override
    public @NotNull TaskResult<ResourceDto> updateResource(@NotNull @NotNull Map<String, Object> old_resource_data
            , @NotNull Map<String, Object> new_resource_data
            , OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator)
    {
        @NotNull TaskResult<ResourceDto> taskResult;
        @NotNull ResourceDto resultData;
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

            return updateResource(old_resource_data, new_resource_data, operator.getUserName());
        } catch (IllegalArgumentException e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = ResourceDto.Factory.RESOURCE_DTO.createDefault();

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

            resultData = ResourceDto.Factory.RESOURCE_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 删除指定资源
     *
     * @param resourceCode      {@linkplain SecurityResource#getCode() 资源编码}
     * @param operator_username [操作者 - 用户名称]
     *
     * @return {@link TaskResult}
     */
    private @NotNull TaskResult<ResourceDto> deleteResource(@NotNull String resourceCode, @NotNull String operator_username) {
        @NotNull TaskResult<ResourceDto> taskResult;
        @NotNull ResourceDto resultData;
        try {
            if (null == resourceCode || ! SecurityResource.Validator.RESOURCE.code(resourceCode)) {
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "被替换的资源数据"
                        , resourceCode
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            final SecurityResource existedResource = securityResourceReadService.selectResourceByCode(resourceCode);

            String message = null;
            boolean deleteResourceSuccessFlag;
            if (null != existedResource) {
                deleteResourceSuccessFlag = securityResourceNonIdempotentEvent.deleteResource(existedResource
                        , securityUserReadService.selectByUsername(operator_username));
            } else {
                deleteResourceSuccessFlag = true;

                message = "不存在指定的资源";
            }

            // (业务操作是否符合预期)
            if (deleteResourceSuccessFlag) {
                //--- 业务操作成功
                resultData = ResourceDto.Factory.RESOURCE_DTO.create(existedResource);

                taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.SUCCESS
                        , message
                        , resultData);
            } else {
                //--- 业务操作失败
                resultData = ResourceDto.Factory.RESOURCE_DTO.create(existedResource);

                taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE_BUSINESS
                        , message
                        , resultData);
            }
        } catch (IllegalArgumentException e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = ResourceDto.Factory.RESOURCE_DTO.createDefault();

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

            resultData = ResourceDto.Factory.RESOURCE_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 删除指定资源
     *
     * @param resourceCode  {@linkplain SecurityResource#getCode() 资源编码}
     * @param operator      {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @Override
    public @NotNull TaskResult<ResourceDto> deleteResource(@NotNull String resourceCode, @NotNull AbstractSecurityUser operator) {
        @NotNull TaskResult<ResourceDto> taskResult;
        @NotNull ResourceDto resultData;
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

            return deleteResource(resourceCode, operator.getUsername());
        } catch (IllegalArgumentException e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = ResourceDto.Factory.RESOURCE_DTO.createDefault();

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

            resultData = ResourceDto.Factory.RESOURCE_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 删除指定资源
     *
     * @param resourceCode  {@linkplain SecurityResource#getCode() 资源编码}
     * @param operator      {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @Override
    public @NotNull TaskResult<ResourceDto> deleteResource(@NotNull String resourceCode, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator) {
        @NotNull TaskResult<ResourceDto> taskResult;
        @NotNull ResourceDto resultData;
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

            return deleteResource(resourceCode, operator.getUserName());
        } catch (IllegalArgumentException e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            resultData = ResourceDto.Factory.RESOURCE_DTO.createDefault();

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

            resultData = ResourceDto.Factory.RESOURCE_DTO.createDefault();

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

}
