package github.com.suitelhy.dingding.core.domain.event.security.impl;

import github.com.suitelhy.dingding.core.domain.entity.User;
import github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.core.domain.entity.security.*;
import github.com.suitelhy.dingding.core.domain.event.UserEvent;
import github.com.suitelhy.dingding.core.domain.event.security.SecurityUrlEvent;
import github.com.suitelhy.dingding.core.domain.service.LogService;
import github.com.suitelhy.dingding.core.domain.service.UserAccountOperationInfoService;
import github.com.suitelhy.dingding.core.domain.service.UserService;
import github.com.suitelhy.dingding.core.domain.service.security.*;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.HandleType;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * (安全) 资源 - 复杂业务实现
 *
 * @see SecurityResource
 * @see SecurityResourceUrl
 *
 * @see SecurityUrlEvent
 *
 * @see SecurityResourceService
 * @see SecurityResourceUrlService
 * @see UserAccountOperationInfoService
 * @see LogService
 */
@Order(Ordered.LOWEST_PRECEDENCE)
@Service("securityUrlEvent")
@Transactional(isolation = Isolation.READ_COMMITTED
        , propagation = Propagation.REQUIRED
        , readOnly = true
        , rollbackFor = Exception.class
        , timeout = 15)
public class SecurityUrlEventImpl
        implements SecurityUrlEvent {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityUserService securityUserService;

    @Autowired
    private SecurityRoleService securityRoleService;

    @Autowired
    private SecurityResourceService securityResourceService;

    @Autowired
    private SecurityUserRoleService securityUserRoleService;

    @Autowired
    private SecurityRoleResourceService securityRoleResourceService;

    @Autowired
    private SecurityResourceUrlService securityResourceUrlService;

    @Autowired
    private UserAccountOperationInfoService userAccountOperationInfoService;

    @Autowired
    private LogService logService;

    //===== 查询操作业务 =====//

    /**
     * 判断[（安全认证）角色]是否存在 (关联的) [（安全认证）资源]
     *
     * @Description 完整的业务流程.
     *
     * @param urlInfo URL相关信息.    {@link SecurityResourceUrl.Validator#urlInfo(String[])}
     *
     * @return {@link Boolean#TYPE}
     */
    @Override
    public boolean existResourceOnUrlInfo(@NotNull String[] urlInfo)
            throws IllegalArgumentException
    {
        if (! SecurityResourceUrl.Validator.RESOURCE_URL.urlInfo(urlInfo)) {
            //-- 非法输入: [URL 相关信息]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[URL 相关信息]"
                    , Arrays.toString(urlInfo)
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return securityResourceUrlService.existResourceByUrlInfo(urlInfo);
    }

    /**
     * 判断[（安全认证）资源]是否存在 (关联的) [URL 相关信息]
     *
     * @Description 完整的业务流程.
     *
     * @param resourceCode 资源编码    {@link SecurityResource.Validator#code(String)}
     *
     * @return {@link Boolean#TYPE}
     */
    @Override
    public boolean existUrlInfoOnResourceByResourceCode(@NotNull String resourceCode)
            throws IllegalArgumentException
    {
        if (! SecurityResourceUrl.Validator.RESOURCE_URL.code(resourceCode)) {
            //-- 非法输入: 资源编码
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "资源编码"
                    , resourceCode
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return securityResourceUrlService.existUrlByResourceCode(resourceCode);
    }

    /**
     * 查询[URL 相关信息]
     *
     * @Description 完整的业务流程.
     *
     * @param resourceCode 资源编码    {@link SecurityResourceUrl.Validator#code(String)}
     *
     * @return {@link SecurityResourceUrl}
     *
     * @see github.com.suitelhy.dingding.core.domain.event.security.SecurityResourceEvent#selectUrlInfoOnResourceByResourceCode(String)
     */
    @Override
    public @NotNull List<SecurityResourceUrl> selectUrlInfoOnResourceByResourceCode(@NotNull String resourceCode)
            throws IllegalArgumentException
    {
        if (! SecurityResourceUrl.Validator.RESOURCE_URL.code(resourceCode)) {
            //-- 非法输入: 资源编码
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "资源编码"
                    , resourceCode
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return securityResourceUrlService.selectByResourceCode(resourceCode);
    }

    /**
     * 查询[用户 -> (关联的) 角色 -> (关联的) 资源 -> URL 相关信息]
     *
     * @Description 完整的业务流程.
     *
     * @param username 用户名称    {@link SecurityUser.Validator#username(String)}
     *
     * @return {@link SecurityResourceUrl}
     *
     * @see UserEvent#selectUrlInfoOnUserByUsername(String)
     */
    @Override
    public @NotNull List<SecurityResourceUrl> selectUrlInfoOnUserByUsername(@NotNull String username)
            throws IllegalArgumentException
    {
        if (! SecurityUser.Validator.USER.username(username)) {
            //-- 非法输入: 用户名称
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "用户名称"
                    , username
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        final @NotNull List<SecurityResourceUrl> result = new ArrayList<>(1);

        final @NotNull User user = userService.selectUserByUsername(username);
        if (null == user || user.isEmpty()) {
            return result;
        }

        final @NotNull SecurityUser user_securityUser = securityUserService.selectByUsername(username);
        if (null == user_securityUser || user_securityUser.isEmpty()) {
            return result;
        }

        final @NotNull List<SecurityUserRole> user_userRoles = securityUserRoleService.selectByUsername(username);
        if (! user_userRoles.isEmpty()) {
            for (@NotNull SecurityUserRole eachUserRole : user_userRoles) {
                if (eachUserRole.isEmpty()) {
                    continue;
                }

                final @NotNull SecurityRole eachUserRole_role = securityRoleService.selectRoleByCode(eachUserRole.getRoleCode());
                if (null == eachUserRole_role || eachUserRole_role.isEmpty()) {
                    continue;
                }

                final @NotNull List<SecurityRoleResource> role_roleResources = securityRoleResourceService.selectByRoleCode(eachUserRole_role.getCode());
                if (! role_roleResources.isEmpty()) {
                    for (@NotNull SecurityRoleResource eachRoleResource : role_roleResources) {
                        if (eachRoleResource.isEmpty()) {
                            continue;
                        }

                        final @NotNull SecurityResource eachRoleResource_resource = securityResourceService.selectResourceByCode(eachRoleResource.getResourceCode());
                        if (null == eachRoleResource_resource || eachRoleResource_resource.isEmpty()) {
                            continue;
                        }

                        final @NotNull List<SecurityResourceUrl> eachRoleResource_resource_resourceUrl = securityResourceUrlService.selectByResourceCode(eachRoleResource_resource.getCode());
                        if (! eachRoleResource_resource_resourceUrl.isEmpty()) {
                            for (@NotNull SecurityResourceUrl eachResourceUrl : eachRoleResource_resource_resourceUrl) {
                                if (eachResourceUrl.isEmpty()) {
                                    continue;
                                }

                                result.add(eachResourceUrl);
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * 查询[用户 -> (关联的) 角色 -> (关联的) 资源 -> URL 相关信息]
     *
     * @Description 完整的业务流程.
     *
     * @param username 用户名称         {@link SecurityUser.Validator#username(String)}
     * @param clientId [资源服务器 ID]  {@link SecurityResourceUrl#getClientId()}
     *
     * @return {@link SecurityResourceUrl}
     *
     * @see UserEvent#selectUrlInfoOnUserByUsernameAndClientId(String, String)
     */
    @Override
    public @NotNull List<SecurityResourceUrl> selectUrlInfoOnUserByUsernameAndClientId(@NotNull String username, @NotNull String clientId)
            throws IllegalArgumentException
    {
        if (! SecurityUser.Validator.USER.username(username)) {
            //-- 非法输入: 用户名称
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "用户名称"
                    , username
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (! SecurityResourceUrl.Validator.RESOURCE_URL.clientId(clientId)) {
            //-- 非法输入: [资源服务器 ID]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "[资源服务器 ID]"
                    , clientId
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        final @NotNull List<SecurityResourceUrl> result = new ArrayList<>(1);

        final @NotNull User user = userService.selectUserByUsername(username);
        if (null == user || user.isEmpty()) {
            return result;
        }

        final @NotNull SecurityUser user_securityUser = securityUserService.selectByUsername(username);
        if (null == user_securityUser || user_securityUser.isEmpty()) {
            return result;
        }

        final @NotNull List<SecurityUserRole> user_userRoles = securityUserRoleService.selectByUsername(username);
        if (! user_userRoles.isEmpty()) {
            for (@NotNull SecurityUserRole eachUserRole : user_userRoles) {
                if (eachUserRole.isEmpty()) {
                    continue;
                }

                final @NotNull SecurityRole eachUserRole_role = securityRoleService.selectRoleByCode(eachUserRole.getRoleCode());
                if (null == eachUserRole_role || eachUserRole_role.isEmpty()) {
                    continue;
                }

                final @NotNull List<SecurityRoleResource> role_roleResources = securityRoleResourceService.selectByRoleCode(eachUserRole_role.getCode());
                if (! role_roleResources.isEmpty()) {
                    for (@NotNull SecurityRoleResource eachRoleResource : role_roleResources) {
                        if (eachRoleResource.isEmpty()) {
                            continue;
                        }

                        final @NotNull SecurityResource eachRoleResource_resource = securityResourceService.selectResourceByCode(eachRoleResource.getResourceCode());
                        if (null == eachRoleResource_resource || eachRoleResource_resource.isEmpty()) {
                            continue;
                        }

                        final @NotNull List<SecurityResourceUrl> eachRoleResource_resource_resourceUrl = securityResourceUrlService.selectByResourceCode(eachRoleResource_resource.getCode());
                        if (! eachRoleResource_resource_resourceUrl.isEmpty()) {
                            for (@NotNull SecurityResourceUrl eachResourceUrl : eachRoleResource_resource_resourceUrl) {
                                if (eachResourceUrl.isEmpty()) {
                                    continue;
                                } else if (! eachResourceUrl.getClientId().equals(clientId)) {
                                    continue;
                                }

                                result.add(eachResourceUrl);
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * 查询[URL 相关信息]
     *
     * @Description 完整的业务流程.
     *
     * @param clientId [资源服务器 ID]             {@link SecurityResourceUrl.Validator#clientId(String)}
     * @param urlPath  [资源对应的 URL (Path部分)] {@link SecurityResourceUrl.Validator#urlPath(String)}
     *
     * @return {@link SecurityResourceUrl}
     */
    @Override
    public @NotNull List<SecurityResourceUrl> selectUrlInfoByClientIdAndUrlPath(@NotNull String clientId, @NotNull String urlPath)
            throws IllegalArgumentException
    {
        if (! SecurityResourceUrl.Validator.RESOURCE_URL.clientId(clientId)) {
            //-- 非法输入: [资源服务器 ID]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[资源服务器 ID]"
                    , clientId
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (! SecurityResourceUrl.Validator.RESOURCE_URL.urlPath(urlPath)) {
            //-- 非法输入: [资源对应的 URL (Path部分)]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[资源对应的 URL (Path部分)]"
                    , urlPath
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return securityResourceUrlService.selectByClientIdAndUrlPath(clientId, urlPath);
    }

    /**
     * 查询[（安全认证）资源 ←→ URL]
     *
     * @Description 完整的业务流程.
     *
     * @param resourceCode 资源编码    {@link SecurityResourceUrl.Validator#code(String)}
     *
     * @return {@link SecurityResourceUrl}
     */
    @Override
    public @NotNull List<SecurityResourceUrl> selectResourceUrlRelationshipOnResourceByResourceCode(@NotNull String resourceCode)
            throws IllegalArgumentException
    {
        if (! SecurityResourceUrl.Validator.RESOURCE_URL.code(resourceCode)) {
            //-- 非法输入: 资源编码
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "资源编码"
                    , resourceCode
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return securityResourceUrlService.selectByResourceCode(resourceCode);
    }

    /**
     * 查询[（安全认证）资源 ←→ URL]
     *
     * @Description 完整的业务流程.
     *
     * @param clientId [资源服务器 ID]             {@link SecurityResourceUrl.Validator#clientId(String)}
     * @param urlPath  [资源对应的 URL (Path部分)] {@link SecurityResourceUrl.Validator#urlPath(String)}
     *
     * @return {@link SecurityResourceUrl}
     */
    @Override
    public @NotNull List<SecurityResourceUrl> selectResourceUrlRelationshipOnUrlInfoByClientIdAndUrlPath(@NotNull String clientId, @NotNull String urlPath)
            throws IllegalArgumentException
    {
        if (! SecurityResourceUrl.Validator.RESOURCE_URL.clientId(clientId)) {
            //-- 非法输入: [资源服务器 ID]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[资源服务器 ID]"
                    , clientId
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (! SecurityResourceUrl.Validator.RESOURCE_URL.urlPath(urlPath)) {
            //-- 非法输入: [资源对应的 URL (Path部分)]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[资源对应的 URL (Path部分)]"
                    , urlPath
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return securityResourceUrlService.selectByClientIdAndUrlPath(clientId, urlPath);
    }

    //===== 添加操作业务 =====//

    /**
     * 新增一个[URL 相关信息]
     *
     * @Description 完整的业务流程.
     *
     * @param resource [（安全认证）资源], 必须合法且已持久化.   {@link SecurityResource}
     * @param urlInfo  [URL 相关信息]                       {@link SecurityResourceUrl.Validator#urlInfo(String[])}
     * @param operator 操作者
     *
     * @return 操作是否成功
     */
    @Override
    public boolean insertUrlInfo(@NotNull SecurityResource resource, @NotNull String[] urlInfo, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == resource || resource.isEmpty()) {
            //-- 非法输入: [（安全认证）资源]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）资源]"
                    , resource
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (! SecurityResourceUrl.Validator.RESOURCE_URL.urlInfo(urlInfo)) {
            //-- 非法输入: [URL 相关信息]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[URL 相关信息]"
                    , resource
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == operator || operator.isEmpty()) {
            //-- 非法输入: 操作者
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
        if (null == operator_userAccountOperationInfo || operator_userAccountOperationInfo.isEmpty()) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        SecurityResource existedResource = securityResourceService.selectResourceByCode(resource.getCode());
        if (null == existedResource || existedResource.isEmpty()) {
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- 【%s】</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）资源]"
                    , "已存在的[（安全认证）资源]"
                    , resource
                    , existedResource
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        final @NotNull SecurityResourceUrl resourceUrl = SecurityResourceUrl.Factory.RESOURCE_URL.create(resource, urlInfo);
        if (! securityResourceUrlService.insert(resourceUrl, operator, operator_userAccountOperationInfo)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__ADD.name
                    , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__ADD.name
                    , resourceUrl
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return true;
    }

    /**
     * 新增一个[（安全认证）资源 ←→ URL]关联关系
     *
     * @Description 完整的业务流程.
     *
     * @param resourceUrl [（安全认证）资源 ←→ URL]   {@link SecurityResourceUrl}
     * @param operator    操作者
     *
     * @return 操作是否成功 / 是否已存在相同的有效数据
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    public boolean insertResourceUrlRelationship(@NotNull SecurityResourceUrl resourceUrl, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == resourceUrl || ! resourceUrl.isEntityLegal()) {
            //-- 非法输入: [（安全认证）资源 ←→ URL]关联关系
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）资源 ←→ URL]关联关系"
                    , resourceUrl
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == operator || operator.isEmpty()) {
            //-- 非法输入: 操作者
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
        if (null == operator_userAccountOperationInfo || operator_userAccountOperationInfo.isEmpty()) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        SecurityResource existedResource = securityResourceService.selectResourceByCode(resourceUrl.getCode());
        if (null == existedResource || existedResource.isEmpty()) {
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- 【%s】</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）资源 ←→ URL]关联关系"
                    , "关联的[（安全认证）资源]"
                    , resourceUrl
                    , existedResource
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        if (! securityResourceUrlService.insert(resourceUrl, operator, operator_userAccountOperationInfo)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__ADD.name
                    , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__ADD.name
                    , resourceUrl
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return true;
    }

    /**
     * 新增一个[（安全认证）资源 ←→ URL]关联关系
     *
     * @Description 完整的业务流程.
     *
     * @param resource [（安全认证）资源], 必须合法且已持久化.   {@link SecurityResource}
     * @param urlInfo  [URL 相关信息]                       {@link SecurityResourceUrl.Validator#urlInfo(String[])}
     * @param operator 操作者
     *
     * @return 操作是否成功
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    public boolean insertResourceUrlRelationship(@NotNull SecurityResource resource
            , @NotNull String[] urlInfo
            , @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == resource || resource.isEmpty()) {
            //-- 非法输入: [（安全认证）资源]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）资源]"
                    , resource
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (! SecurityResourceUrl.Validator.RESOURCE_URL.urlInfo(urlInfo)) {
            //-- 非法输入: [URL 相关信息]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[URL 相关信息]"
                    , resource
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == operator || operator.isEmpty()) {
            //-- 非法输入: 操作者
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
        if (null == operator_userAccountOperationInfo || operator_userAccountOperationInfo.isEmpty()) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        SecurityResource existedResource = securityResourceService.selectResourceByCode(resource.getCode());
        if (null == existedResource || existedResource.isEmpty()) {
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- 【%s】</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）资源]"
                    , "已存在的[（安全认证）资源]"
                    , resource
                    , existedResource
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        final @NotNull SecurityResourceUrl resourceUrl = SecurityResourceUrl.Factory.RESOURCE_URL.create(resource, urlInfo);
        if (! securityResourceUrlService.insert(resourceUrl, operator, operator_userAccountOperationInfo)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__ADD.name
                    , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__ADD.name
                    , resourceUrl
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return true;
    }

    //===== 修改操作业务 =====//

    //===== 删除操作业务 =====//

    /**
     * 删除指定的[URL 相关信息]
     *
     * @Description
     * · 完整的业务流程.
     *
     * @param urlInfo   [URL 相关信息]   {@link SecurityResourceUrl.Validator#urlInfo(String[])}
     * @param operator  操作者
     *
     * @return 操作是否成功
     */
    @Override
    public boolean deleteUrlInfo(@NotNull String[] urlInfo, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (! SecurityResourceUrl.Validator.RESOURCE_URL.urlInfo(urlInfo)) {
            //-- 非法输入: [URL 信息]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[URL 信息]"
                    , Arrays.toString(urlInfo)
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == operator || operator.isEmpty()) {
            //-- 非法输入: 操作者
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
        if (null == operator_userAccountOperationInfo || operator_userAccountOperationInfo.isEmpty()) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- 【%s】</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , "无[有效的账户操作基础记录]"
                    , operator
                    , operator_userAccountOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return securityResourceUrlService.delete(urlInfo, operator, operator_userAccountOperationInfo);
    }

    /**
     * 删除指定的[（安全认证）资源]关联的所有[URL 相关信息]
     *
     * @Description · 完整的业务流程.
     *
     * @param resource [（安全认证）资源]  {@link SecurityResource}
     * @param operator 操作者
     *
     * @return 操作是否成功
     */
    @Override
    public boolean deleteUrlInfoOnResource(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == resource || ! resource.isEmpty()) {
            //-- 非法输入: [（安全认证）资源]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）资源]"
                    , resource
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == operator || operator.isEmpty()) {
            //-- 非法输入: 操作者
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
        if (null == operator_userAccountOperationInfo || operator_userAccountOperationInfo.isEmpty()) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        final @NotNull List<SecurityResourceUrl> existedResourceUrls = securityResourceUrlService.selectByResourceCode(resource.getCode());
        if (! existedResourceUrls.isEmpty()) {
            for (@NotNull SecurityResourceUrl eachResourceUrl : existedResourceUrls) {
                if (! securityResourceUrlService.delete(eachResourceUrl, operator, operator_userAccountOperationInfo)) {
                    throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__DELETION.name
                            , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__DELETION.name
                            , eachResourceUrl
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
            }
        }

        return true;
    }

    /**
     * 删除指定的[（安全认证）资源 ←→ URL]关联关系
     *
     * @Description · 完整的业务流程.
     *
     * @param resourceUrl [（安全认证）资源 ←→ URL]   {@link SecurityResourceUrl}
     * @param operator    操作者
     *
     * @return 操作是否成功
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    public boolean deleteResourceUrlRelationship(@NotNull SecurityResourceUrl resourceUrl, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == resourceUrl || ! resourceUrl.isEmpty()) {
            //-- 非法输入: [（安全认证）资源 ←→ URL]关联关系
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）资源 ←→ URL]关联关系"
                    , resourceUrl
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == operator || operator.isEmpty()) {
            //-- 非法输入: 操作者
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
        if (null == operator_userAccountOperationInfo || operator_userAccountOperationInfo.isEmpty()) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        if (! securityResourceUrlService.delete(resourceUrl, operator, operator_userAccountOperationInfo)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__DELETION.name
                    , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__DELETION.name
                    , resourceUrl
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return true;
    }

    /**
     * 删除指定的[（安全认证）资源 ←→ URL]关联关系
     *
     * @Description · 完整的业务流程.
     *
     * @param resource [（安全认证）资源], 必须合法且已持久化.  {@link SecurityResource}
     * @param urlInfo  [URL 相关信息]                      {@link SecurityResourceUrl.Validator#urlInfo(String[])}
     * @param operator 操作者
     *
     * @return 操作是否成功
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    public boolean deleteResourceUrlRelationship(@NotNull SecurityResource resource, @NotNull String[] urlInfo, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == resource || ! resource.isEmpty()) {
            //-- 非法输入: [（安全认证）资源]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）资源]"
                    , resource
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (! SecurityResourceUrl.Validator.RESOURCE_URL.urlInfo(urlInfo)) {
            //-- 非法输入: [URL 相关信息]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[URL 相关信息]"
                    , Arrays.toString(urlInfo)
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == operator || operator.isEmpty()) {
            //-- 非法输入: 操作者
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
        if (null == operator_userAccountOperationInfo || operator_userAccountOperationInfo.isEmpty()) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        final @NotNull SecurityResourceUrl resourceUrl = SecurityResourceUrl.Factory.RESOURCE_URL.create(resource, urlInfo);
        if (! securityResourceUrlService.delete(resourceUrl, operator, operator_userAccountOperationInfo)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__DELETION.name
                    , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__DELETION.name
                    , resourceUrl
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return true;
    }

    /**
     * 删除指定的[（安全认证）资源]关联的所有[（安全认证）资源 ←→ URL]关联关系
     *
     * @Description · 完整的业务流程.
     *
     * @param resource [（安全认证）资源]  {@link SecurityResource}
     * @param operator 操作者
     *
     * @return 操作是否成功
     */
    @Override
    public boolean deleteResourceUrlRelationshipOnResource(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == resource || ! resource.isEmpty()) {
            //-- 非法输入: [（安全认证）资源]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）资源]"
                    , resource
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == operator || operator.isEmpty()) {
            //-- 非法输入: 操作者
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
        if (null == operator_userAccountOperationInfo || operator_userAccountOperationInfo.isEmpty()) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        final @NotNull List<SecurityResourceUrl> existedResourceUrls = securityResourceUrlService.selectByResourceCode(resource.getCode());
        if (! existedResourceUrls.isEmpty()) {
            for (@NotNull SecurityResourceUrl eachResourceUrl : existedResourceUrls) {
                if (! securityResourceUrlService.delete(eachResourceUrl, operator, operator_userAccountOperationInfo)) {
                    throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__DELETION.name
                            , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__DELETION.name
                            , eachResourceUrl
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
            }
        }

        return true;
    }

    /**
     * 删除指定的[URL 信息]关联的所有[（安全认证）资源 ←→ URL]关联关系
     *
     * @Description
     * · 完整的业务流程.
     *
     * @param urlInfo  [URL 信息]    {@link SecurityResourceUrl.Validator#urlInfo(String[])}
     * @param operator 操作者
     *
     * @return 操作是否成功
     */
    @Override
    public boolean deleteResourceUrlRelationshipOnUrlInfo(@NotNull String[] urlInfo, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (! SecurityResourceUrl.Validator.RESOURCE_URL.urlInfo(urlInfo)) {
            //-- 非法输入: [URL 信息]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[URL 信息]"
                    , Arrays.toString(urlInfo)
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == operator || operator.isEmpty()) {
            //-- 非法输入: 操作者
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
        if (null == operator_userAccountOperationInfo || operator_userAccountOperationInfo.isEmpty()) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- 【%s】</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , "无[有效的账户操作基础记录]"
                    , operator
                    , operator_userAccountOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return securityResourceUrlService.delete(urlInfo, operator, operator_userAccountOperationInfo);
    }

}
