package github.com.suitelhy.dingding.security.service.provider.domain.event.impl;

import github.com.suitelhy.dingding.core.infrastructure.domain.Page;
import github.com.suitelhy.dingding.core.infrastructure.domain.PageImpl;
import github.com.suitelhy.dingding.core.infrastructure.domain.Pageable;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.HandleType;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.*;
import github.com.suitelhy.dingding.security.service.api.domain.vo.Security;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.security.service.provider.domain.event.SecurityRoleEvent;
import github.com.suitelhy.dingding.security.service.provider.domain.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * （安全认证）角色 - 复杂业务实现
 *
 * @see SecurityRole
 * @see SecurityResource
 * @see SecurityUser
 * @see SecurityRoleResource
 * @see SecurityUserRole
 * @see SecurityRoleEvent
 * @see SecurityRoleService
 * @see SecurityResourceService
 * @see SecurityUserRoleService
 * @see SecurityRoleResourceService
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Service("securityRoleEvent")
@Transactional(isolation = Isolation.SERIALIZABLE
        , propagation = Propagation.REQUIRED
        , readOnly = false
        , rollbackFor = Exception.class
        , timeout = 15)
public class SecurityRoleEventImpl
        implements SecurityRoleEvent {

    @Autowired
    private SecurityRoleService securityRoleService;

    @Autowired
    private SecurityUserService securityUserService;

    @Autowired
    private SecurityResourceService securityResourceService;

    @Autowired
    private SecurityUserRoleService securityUserRoleService;

    @Autowired
    private SecurityRoleResourceService securityRoleResourceService;

    //===== 查询操作业务 =====//

    /**
     * 判断是否存在 (关联的) [（安全认证）角色]
     *
     * @Description 完整的业务流程.
     *
     * @param username  {@linkplain SecurityUser.Validator#username(String) 用户名称}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public boolean existRoleOnUserByUsername(@NotNull String username)
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

        final @NotNull SecurityUser securityUser = securityUserService.selectByUsername(username);
        if (securityUser.isEmpty()) {
            return false;
        }

        @NotNull List<SecurityUserRole> securityUser_userRoles = securityUserRoleService.selectByUsername(securityUser.getUsername());
        if (! securityUser_userRoles.isEmpty()) {
            for (@NotNull SecurityUserRole eachUserRole : securityUser_userRoles) {
                if (eachUserRole.isEmpty()) {
                    continue;
                }

                final @NotNull SecurityRole eachUserRole_role = securityRoleService.selectRoleByCode(eachUserRole.getRoleCode());
                if (! eachUserRole_role.isEmpty()) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 判断[（安全认证）资源]是否存在 (关联的) [（安全认证）角色]
     *
     * @Description 完整的业务流程.
     *
     * @param resourceCode  {@linkplain SecurityRoleResource.Validator#resourceCode(String) 资源编码}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public boolean existRoleOnResourceByResourceCode(@NotNull String resourceCode) {
        if (! SecurityRoleResource.Validator.ROLE_RESOURCE.resourceCode(resourceCode)) {
            //-- 非法输入: 资源编码
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "资源编码"
                    , resourceCode
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return securityRoleResourceService.existRoleByResourceCode(resourceCode);
    }

    /**
     * 判断[（安全认证）角色]是否存在 (关联的) [（安全认证）资源]
     *
     * @Description 完整的业务流程.
     *
     * @param roleCode  {@linkplain SecurityRoleResource.Validator#roleCode(String) 角色编码}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public boolean existResourceOnRoleByRoleCode(@NotNull String roleCode) {
        if (! SecurityRoleResource.Validator.ROLE_RESOURCE.roleCode(roleCode)) {
            //-- 非法输入: 角色编码
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "角色编码"
                    , roleCode
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return securityRoleResourceService.existResourceByRoleCode(roleCode);
    }

    /**
     * 查询 (指定角色关联的) 资源
     *
     * @Description 完整的业务流程.
     *
     * @param roleCode  {@linkplain SecurityRole.Validator#code(String) 角色编码}
     *
     * @return {@linkplain SecurityResource (指定角色关联的) 资源}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public @NotNull List<SecurityResource> selectResourceOnRoleByRoleCode(@NotNull String roleCode)
            throws IllegalArgumentException
    {
        if (! SecurityRole.Validator.ROLE.code(roleCode)) {
            //-- 非法输入: 角色编码
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "角色编码"
                    , roleCode
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        final @NotNull List<SecurityResource> result = new ArrayList<>(0);

        final SecurityRole role = securityRoleService.selectRoleByCode(roleCode);
        if (null == role || role.isEmpty()) {
            return result;
        }

        @NotNull List<SecurityRoleResource> role_roleResources = securityRoleResourceService.selectByRoleCode(roleCode);
        if (! role_roleResources.isEmpty()) {
            for (@NotNull SecurityRoleResource eachRoleResource : role_roleResources) {
                if (eachRoleResource.isEmpty()) {
                    continue;
                }
                SecurityResource eachResource = securityResourceService.selectResourceByCode(eachRoleResource.getResourceCode());
                if (null == eachResource || eachResource.isEmpty()) {
                    continue;
                }

                result.add(eachResource);
            }
        }

        return result;
    }

    /**
     * 查询 (指定资源关联的) 角色
     *
     * @Description 完整的业务流程.
     *
     * @param resourceCode  {@linkplain SecurityResource#getCode() 资源编码}
     *
     * @return {@linkplain SecurityRole (指定资源关联的) 角色}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public @NotNull List<SecurityRole> selectRoleOnResourceByResourceCode(@NotNull String resourceCode)
            throws IllegalArgumentException
    {
        if (! SecurityResource.Validator.RESOURCE.code(resourceCode)) {
            //-- 非法输入: 资源编码
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "资源编码"
                    , resourceCode
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        final @NotNull List<SecurityRole> result = new ArrayList<>(0);

        final @NotNull SecurityResource resource = securityResourceService.selectResourceByCode(resourceCode);
        if (null == resource || resource.isEmpty()) {
            return result;
        }

        final @NotNull List<SecurityRoleResource> resource_roleResources = securityRoleResourceService.selectByResourceCode(resourceCode);
        if (! resource_roleResources.isEmpty()) {
            for (@NotNull SecurityRoleResource eachRoleResource : resource_roleResources) {
                if (eachRoleResource.isEmpty()) {
                    continue;
                }
                final @NotNull SecurityRole eachRole = securityRoleService.selectRoleByCode(eachRoleResource.getRoleCode());
                if (null == eachRole || eachRole.isEmpty()) {
                    continue;
                }

                result.add(eachRole);
            }
        }

        return result;
    }

    /**
     * 查询 (关联的) [（安全认证）角色]
     *
     * @Description 完整的业务流程.
     *
     * @param username  {@linkplain SecurityUser.Validator#username(String) 用户名称}
     *
     * @return {@linkplain SecurityRole (关联的) [（安全认证）角色]}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public @NotNull List<SecurityRole> selectRoleOnUserByUsername(@NotNull String username)
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

        final @NotNull List<SecurityRole> result = new ArrayList<>(0);

        final @NotNull SecurityUser securityUser = securityUserService.selectByUsername(username);
        if (securityUser.isEmpty()) {
            return result;
        }

        @NotNull List<SecurityUserRole> securityUser_userRoles = securityUserRoleService.selectByUsername(securityUser.getUsername());
        if (! securityUser_userRoles.isEmpty()) {
            for (@NotNull SecurityUserRole eachUserRole : securityUser_userRoles) {
                if (eachUserRole.isEmpty()) {
                    continue;
                }

                final @NotNull SecurityRole eachUserRole_role = securityRoleService.selectRoleByCode(eachUserRole.getRoleCode());
                if (! eachUserRole_role.isEmpty()) {
                    result.add(eachUserRole_role);
                }
            }
        }

        return result;
    }

    /**
     * 查询 (关联的) [（安全认证）角色]
     *
     * @Description 完整的业务流程.
     *
     * @param username  {@linkplain SecurityUser.Validator#username(String) 用户名称}
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     *
     * @return {@linkplain SecurityRole (关联的) [（安全认证）角色]}
     */
    @Override
    public @NotNull Page<SecurityRole> selectPageRoleOnUserByUsername(@NotNull String username, int pageIndex, int pageSize)
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

        @NotNull Page<SecurityRole> result = Page.empty();
        final @NotNull List<SecurityRole> result_content = new ArrayList<>(0);

        final @NotNull SecurityUser securityUser = securityUserService.selectByUsername(username);
        if (securityUser.isEmpty()) {
            return result;
        }

        org.springframework.data.domain.Page<SecurityUserRole> securityUser_userRolePage = securityUserRoleService.selectPageByUsername(securityUser.getUsername(), pageIndex, pageSize);
        if (! securityUser_userRolePage.isEmpty()) {
            for (@NotNull SecurityUserRole eachUserRole : securityUser_userRolePage) {
                if (eachUserRole.isEmpty()) {
                    continue;
                }

                final @NotNull SecurityRole eachUserRole_role = securityRoleService.selectRoleByCode(eachUserRole.getRoleCode());
                if (! eachUserRole_role.isEmpty()) {
                    result_content.add(eachUserRole_role);
                }
            }

            result = PageImpl.Factory.DEFAULT.create(result_content, securityUser_userRolePage);
        }

        return result;
    }

    //===== 添加操作业务 =====//

    /**
     * 新增一个[（安全认证）用户 ←→ 角色]关联关系
     *
     * @Description 完整的业务流程.
     *
     * @param userRole  {@linkplain SecurityUserRole [（安全认证）用户 ←→ 角色]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在相同的有效数据}
     */
    @Override
    public boolean insertUserRoleRelationship(@NotNull SecurityUserRole userRole, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == userRole || userRole.isEmpty()) {
            //-- 非法输入: [（安全认证）用户 ←→ 角色]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）用户 ←→ 角色]"
                    , userRole
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
        /*final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
        if (null == operator_userAccountOperationInfo || operator_userAccountOperationInfo.isEmpty()) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】<-【%s】</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , "无[有效的账户操作基础记录]"
                    , operator
                    , operator_userAccountOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }*/

        if (! securityUserRoleService.insert(userRole, operator)) {
            //-- 操作失败: 新增[（安全认证）用户 ←→ 角色]关联关系
            throw new BusinessAtomicException(String.format("操作失败:<description>%s</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__ADD.name
                    , userRole
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return true;
    }

    /**
     * 新增多个[（安全认证）用户 ←→ 角色]关联关系
     *
     * @Description 完整的业务流程.
     *
     * @param user      {@linkplain SecurityUser [（安全认证）用户]}
     * @param roles     {@linkplain SecurityRole [（安全认证）角色]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在完全相同的有效数据集合}
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    public boolean insertUserRoleRelationship(@NotNull SecurityUser user, @NotNull Set<SecurityRole> roles, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if ((null == user || user.isEmpty())) {
            //-- 非法输入: [（安全认证）用户]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）用户]"
                    , user
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == roles || roles.isEmpty()) {
            //-- 非法输入: [（安全认证）角色]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）角色]"
                    , roles
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        } else {
            for (@NotNull SecurityRole each : roles) {
                if (each.isEmpty()) {
                    //-- 非法输入: [（安全认证）角色]
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[（安全认证）角色]"
                            , each
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
            }
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
        /*final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
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
        }*/

        final @NotNull List<SecurityUserRole> existedUserRoles = securityUserRoleService.selectByUsername(user.getUsername());

        if (! existedUserRoles.isEmpty()) {
            //--- 角色存在绑定的资源的情况
            for (@NotNull SecurityRole eachRole : roles) {
                boolean exist = false;
                for (@NotNull SecurityUserRole existedUserRole : existedUserRoles) {
                    if (existedUserRole.equals(user, eachRole)) {
                        exist = true;
                        break;
                    }
                }
                if (exist) {
                    continue;
                }

                final @NotNull SecurityUserRole eachUserRole = SecurityUserRole.Factory.USER_ROLE.create(user.getUsername(), eachRole.getCode());
                if (! securityUserRoleService.insert(eachUserRole, operator)) {
                    //-- 操作失败: 新增[（安全认证）用户 ←→ 角色]关联关系
                    throw new BusinessAtomicException(String.format("操作失败:<description>%s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__ADD.name
                            , eachUserRole
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
            }
        } else {
            //--- 角色不存在绑定的资源的情况
            for (@NotNull SecurityRole each : roles) {
                final @NotNull SecurityUserRole eachUserRole = SecurityUserRole.Factory.USER_ROLE.create(user.getUsername(), each.getCode());
                if (! securityUserRoleService.insert(eachUserRole, operator)) {
                    //-- 操作失败: 新增[（安全认证）用户 ←→ 角色]关联关系
                    throw new BusinessAtomicException(String.format("操作失败:<description>%s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__ADD.name
                            , eachUserRole
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
            }
        }

        return true;
    }

    /**
     * 新增一个[（安全认证）用户 ←→ 角色]关联关系
     *
     * @Description 完整的业务流程.
     *
     * @param user      {@linkplain SecurityUser [（安全认证）用户]}
     * @param roleVo    {@linkplain Security.RoleVo [(安全) 用户 -> 角色]}, 仅需保证合法性, 不需要保证持久化.
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在相同的有效数据}
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    public boolean insertUserRoleRelationship(@NotNull SecurityUser user, @NotNull Security.RoleVo roleVo, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if ((null == user || user.isEmpty())) {
            //-- 非法输入: [（安全认证）用户]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）用户]"
                    , user
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == roleVo) {
            //-- 非法输入: [(安全) 用户 -> 角色]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[(安全) 用户 -> 角色]"
                    , roleVo
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
        /*final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
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
        }*/

        final @NotNull List<SecurityUserRole> existedUserRoles = securityUserRoleService.selectByUsername(user.getUsername());

        if (! existedUserRoles.isEmpty()) {
            //--- 角色存在绑定的资源的情况
            @NotNull SecurityRole role = SecurityRole.Factory.ROLE.create(roleVo);

            boolean exist = false;
            for (@NotNull SecurityUserRole existedUserRole : existedUserRoles) {
                if (existedUserRole.equals(user, role)) {
                    exist = true;
                    break;
                }
            }
            if (exist) {
                return true;
            } else {
                @NotNull SecurityUserRole userRole = SecurityUserRole.Factory.USER_ROLE.create(user.getUsername(), role.getCode());
                if (! securityUserRoleService.insert(userRole, operator)) {
                    //-- 操作失败: 新增[（安全认证）用户 ←→ 角色]关联关系
                    throw new BusinessAtomicException(String.format("操作失败:<description>%s</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__ADD.name
                            , userRole
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
            }
        } else {
            //--- 角色不存在绑定的资源的情况
            @NotNull SecurityRole role = SecurityRole.Factory.ROLE.create(roleVo);

            final @NotNull SecurityUserRole userRole = SecurityUserRole.Factory.USER_ROLE.create(user.getUsername(), role.getCode());
            if (! securityUserRoleService.insert(userRole, operator)) {
                //-- 操作失败: 新增[（安全认证）用户 ←→ 角色]关联关系
                throw new BusinessAtomicException(String.format("操作失败:<description>%s</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                        , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__ADD.name
                        , userRole
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
        }

        return true;
    }

    /**
     * 新增[角色 - 资源]关联
     *
     * @Description 完整的业务流程.
     *
     * @Issue
     * · 多次单元测试后发现[并发一致性问题 - 读脏数据]的问题 (主要使用成熟方案解决问题, 故没有分配时间进行更多不必要地步进式问题跟踪).
     * {@linkplain <a href="https://github.com/CyC2018/CS-Notes/blob/master/notes/%E6%95%B0%E6%8D%AE%E5%BA%93%E7%B3%BB%E7%BB%9F%E5%8E%9F%E7%90%86.md#%E8%AF%BB%E8%84%8F%E6%95%B0%E6%8D%AE">CS-Notes/数据库系统原理.md at master · CyC2018/CS-Notes</a> [并发一致性问题 - 读脏数据]}
     *
     * @Solution
     * {@linkplain <a href="https://juejin.im/post/5c1852526fb9a04a0c2e5db6">Spring Boot+SQL/JPA实战悲观锁和乐观锁 - 掘金</a> 并发一致性问题解决方案}
     *
     * @param roles     {@linkplain SecurityRole [（安全认证）角色]}, 必须全部合法且已持久化.
     * @param resources {@linkplain SecurityResource [（安全认证）资源]}, 必须全部合法且已持久化.
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    public boolean insertRoleResourceRelationship(@NotNull Set<SecurityRole> roles, @NotNull Set<SecurityResource> resources, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == roles || roles.isEmpty()) {
            //-- 非法输入: [（安全认证）角色]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）角色]"
                    , roles
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        } else {
            for (@NotNull SecurityRole each : roles) {
                if (each.isEmpty()) {
                    //-- 非法输入: [（安全认证）角色]
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[（安全认证）角色]"
                            , each
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
            }
        }
        if (null == resources || resources.isEmpty()) {
            //-- 非法输入: [（安全认证）资源]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）资源]"
                    , resources
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        } else {
            for (@NotNull SecurityResource each : resources) {
                if (each.isEmpty()) {
                    //-- 非法输入: [（安全认证）资源]
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[（安全认证）资源]"
                            , each
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
            }
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
        /*final @NotNull UserAccountOperationInfo operatorOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
        if (null == operatorOperationInfo || operatorOperationInfo.isEmpty()) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- 【%s】</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , "无[有效的账户操作基础记录]"
                    , operator
                    , operatorOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }*/
        /*System.err.println("========== insertResource(Set<SecurityRole>, Set<SecurityResource>) ==========");
        System.err.println("角色参数:" + roles);
        System.err.println("资源参数:" + resources);*/

        boolean result = false;

        for (@NotNull SecurityRole each : roles) {
            if (! securityRoleResourceService.existResourceByRoleCode(each.getCode())) {
                //--- 角色不存在绑定的资源的情况
                /*System.err.println("角色" + each + "不存在绑定的资源集合");*/
                for (@NotNull SecurityResource eachResource : resources) {
                    /*System.err.println("== 开始绑定, 资源" + eachResource + "... ==");*/
                    final @NotNull SecurityRoleResource eachRoleResource = SecurityRoleResource.Factory.ROLE_RESOURCE.create(each, eachResource);
                    if (! securityRoleResourceService.insert(eachRoleResource, operator)) {
                        throw new BusinessAtomicException(String.format("操作失败:<description>%s!</description>->【%s】&【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                                , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD.name
                                , each
                                , eachResource
                                , operator
                                , this.getClass().getName()
                                , Thread.currentThread().getStackTrace()[1].getMethodName()
                                , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                    }
                    /*System.err.println("== 绑定成功: 角色" + each + " - 资源" + eachResource + " ==");*/

                    /*//=== 操作日志记录 ===//
                    final @NotNull Log newLog_SecurityRoleResource = Log.Factory.SecurityRoleResource.LOG.create(null
                            , null
                            , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD
                            , SecurityRoleResource.Factory.ROLE_RESOURCE.create(each.getCode(), eachResource.getCode())
                            , operator
                            , operatorOperationInfo);
                    if (! logService.insert(newLog_SecurityRoleResource)) {
                        throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                                , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD.name
                                , "生成操作日志记录"
                                , newLog_SecurityRoleResource
                                , this.getClass().getName()
                                , Thread.currentThread().getStackTrace()[1].getMethodName()
                                , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                    }
                    //======//*/
                }
            } else {
                //--- 角色存在绑定的资源的情况
                final @NotNull List<SecurityRoleResource> existedRoleResources = securityRoleResourceService.selectByRoleCode(each.getCode());

                /*System.err.println("角色" + each + "存在绑定的资源集合: " + existedRoleResources);*/
                for (@NotNull SecurityResource eachResource : resources) {
                    boolean exist = false;
                    for (@NotNull SecurityRoleResource existedRoleResource : existedRoleResources) {
                        if (existedRoleResource.equals(each, eachResource)) {
                            exist = true;
                            break;
                        }
                    }
                    if (exist) continue;

                    /*System.err.println("== 开始绑定, 资源" + eachResource + "... ==");*/
                    final @NotNull SecurityRoleResource eachRoleResource = SecurityRoleResource.Factory.ROLE_RESOURCE.create(each, eachResource);
                    if (! securityRoleResourceService.insert(eachRoleResource, operator)) {
                        throw new BusinessAtomicException(String.format("操作失败:<description>%s!</description>->【%s】&【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                                , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD.name
                                , each
                                , eachResource
                                , operator
                                , this.getClass().getName()
                                , Thread.currentThread().getStackTrace()[1].getMethodName()
                                , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                    }
                    /*System.err.println("== 绑定成功: 角色" + each + " - 资源" + eachResource + " ==");*/

                    /*//=== 操作日志记录 ===//
                    final @NotNull Log newLog_SecurityRoleResource = Log.Factory.SecurityRoleResource.LOG.create(null
                            , null
                            , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD
                            , SecurityRoleResource.Factory.ROLE_RESOURCE.create(each.getCode(), eachResource.getCode())
                            , operator
                            , operatorOperationInfo);
                    if (!logService.insert(newLog_SecurityRoleResource)) {
                        throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                                , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD.name
                                , "生成操作日志记录"
                                , newLog_SecurityRoleResource
                                , this.getClass().getName()
                                , Thread.currentThread().getStackTrace()[1].getMethodName()
                                , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                    }
                    //======//*/
                }
            }

            result = true;
        }

        return result;
    }

    //===== 修改操作业务 =====//

    /**
     * 更新指定的角色
     *
     * @Description 全量更新.
     * · 完整的业务流程.
     *
     * @param role      {@linkplain SecurityRole [（安全认证）角色]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    public boolean updateRole(@NotNull SecurityRole role, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == role || role.isEmpty()) {
            //-- 非法输入: 非法角色
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "非法角色"
                    , role
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
        /*final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
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
        }*/

        return securityRoleService.update(role, operator);
    }

    /**
     * 更新指定的角色
     *
     * @Description 增量更新.
     * · 完整的业务流程.
     *
     * @param old_role      原始版本业务全量数据.
     * @param new_role_data 需要更新的数据.
     * · 数据格式:
     * {
     *  "role_name" : [角色名称],
     *  "role_description" : [角色描述]
     * }
     * @param operator      {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    public boolean updateRole(@NotNull SecurityRole old_role
            , @NotNull Map<String, Object> new_role_data
            , @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == old_role || ! old_role.isEntityLegal()) {
            //-- 非法输入: 原始版本业务全量数据
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "原始版本业务全量数据"
                    , old_role
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == new_role_data
                || (! new_role_data.containsKey("role_name") && ! new_role_data.containsKey("role_description"))) {
            //-- 非法输入: 需要更新的数据
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "需要更新的数据"
                    , new_role_data
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (new_role_data.containsKey("role_code")
                && ! SecurityRole.Validator.ROLE.code((String) new_role_data.get("role_code"))) {
            //-- 非法输入: 需要更新的数据 => 角色编码
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "需要更新的数据 => 角色编码"
                    , new_role_data
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (new_role_data.containsKey("role_name")
                && ! SecurityRole.Validator.ROLE.name((String) new_role_data.get("role_name"))) {
            //-- 非法输入: 需要更新的数据 => 角色名称
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "需要更新的数据 => 角色名称"
                    , new_role_data
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (new_role_data.containsKey("role_description")
                && ! SecurityRole.Validator.ROLE.description((String) new_role_data.get("role_description"))) {
            //-- 非法输入: 需要更新的数据 => 角色描述
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "需要更新的数据 => 角色描述"
                    , new_role_data
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
        /*final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
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
        }*/

        return securityRoleService.update(old_role, new_role_data, operator);
    }

    //===== 删除操作业务 =====//

    /**
     * 删除指定的角色
     *
     * @Description 完整的业务流程.
     *
     * @param role      {@linkplain SecurityRole [（安全认证）角色]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    public boolean deleteRole(@NotNull SecurityRole role, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == role || role.isEmpty()) {
            //-- 非法输入: 非法角色
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "非法角色"
                    , role
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
        /*final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
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
        }*/

        //===== 删除关联的[（安全认证）用户 ←→ 角色] =====//

        @NotNull List<SecurityUserRole> role_userRoles = securityUserRoleService.selectByRoleCode(role.getCode());
        if (! role_userRoles.isEmpty()) {
            for (@NotNull SecurityUserRole eachUserRole : role_userRoles) {
                if (eachUserRole.isEmpty()) {
                    continue;
                }

                if (! securityUserRoleService.delete(eachUserRole, operator)) {
                    throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , HandleType.LogVo.SECURITY__SECURITY_ROLE__DELETION.name
                            , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__DELETION.name
                            , role
                            , eachUserRole
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
            }
        }

        //===== 删除关联的[（安全认证）角色 ←→ 资源] =====//

        @NotNull List<SecurityRoleResource> role_roleResources = securityRoleResourceService.selectByRoleCode(role.getCode());
        if (! role_roleResources.isEmpty()) {
            for (@NotNull SecurityRoleResource eachRoleResource : role_roleResources) {
                if (eachRoleResource.isEmpty()) {
                    continue;
                }

                if (! securityRoleResourceService.delete(eachRoleResource, operator)) {
                    throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , HandleType.LogVo.SECURITY__SECURITY_ROLE__DELETION.name
                            , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__DELETION.name
                            , role
                            , eachRoleResource
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
            }
        }

        //==========//

        if (! securityRoleService.delete(role, operator)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , HandleType.LogVo.SECURITY__SECURITY_ROLE__DELETION.name
                    , HandleType.LogVo.SECURITY__SECURITY_ROLE__DELETION.name
                    , role
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        /*//=== 操作日志记录 ===//
        final @NotNull Log newLog_SecurityRole = Log.Factory.SecurityRole.LOG.create(null
                , null
                , HandleType.LogVo.SECURITY__SECURITY_ROLE__DELETION
                , role
                , operator
                , operator_userAccountOperationInfo);
        if (!logService.insert(newLog_SecurityRole)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , HandleType.LogVo.SECURITY__SECURITY_ROLE__DELETION.name
                    , "生成操作日志记录"
                    , newLog_SecurityRole
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        //======//*/

        return true;
    }

    /**
     * 删除[（安全认证）用户 ←→ 角色]关联关系
     *
     * @Description 完整的业务流程.
     *
     * @param user      {@linkplain SecurityUser [（安全认证）用户]}, 必须合法且已持久化.
     * @param role      {@linkplain SecurityRole [（安全认证）角色]}, 必须合法且已持久化.
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    public boolean deleteUserRoleRelationship(@NotNull SecurityUser user, @NotNull SecurityRole role, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == user || user.isEmpty()) {
            //-- 非法输入: [（安全认证）用户]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）用户]"
                    , user
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == role || role.isEmpty()) {
            //-- 非法输入: [（安全认证）角色]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）角色]"
                    , role
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
        /*final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
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
        }*/

        final @NotNull SecurityUserRole existedUserRole = securityUserRoleService.selectByUsernameAndRoleCode(user.getUsername(), role.getCode());
        if (null == existedUserRole || existedUserRole.isEmpty()) {
            return true;
        } else {
            if (securityUserRoleService.delete(existedUserRole, operator)) {
                throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                        , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__DELETION.name
                        , "执行后数据异常"
                        , user
                        , role
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
        }

        return true;
    }

    /**
     * 删除指定的[（安全认证）用户]关联的所有[（安全认证）用户 ←→ 角色]关联关系
     *
     * @Description 完整的业务流程.
     *
     * @param user      {@linkplain SecurityUser [（安全认证）用户]}, 必须合法且已持久化.
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    public boolean deleteUserRoleRelationshipOnUser(@NotNull SecurityUser user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == user || user.isEmpty()) {
            //-- 非法输入: [（安全认证）用户]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）用户]"
                    , user
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
        /*final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
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
        }*/

        final @NotNull List<SecurityUserRole> existedUserRoles = securityUserRoleService.selectByUsername(user.getUsername());
        if (null == existedUserRoles || existedUserRoles.isEmpty()) {
            return true;
        } else {
            for (@NotNull SecurityUserRole eachUserRole : existedUserRoles) {
                if (! securityUserRoleService.delete(eachUserRole, operator)) {
                    throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__DELETION.name
                            , "操作失败"
                            , eachUserRole
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
            }
        }

        return true;
    }

    /**
     * 删除指定的[（安全认证）角色]关联的所有[（安全认证）用户 ←→ 角色]关联关系
     *
     * @Description 完整的业务流程.
     *
     * @param role      {@linkplain SecurityRole [（安全认证）角色]}, 必须合法且已持久化.
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    public boolean deleteUserRoleRelationshipOnRole(@NotNull SecurityRole role, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == role || role.isEmpty()) {
            //-- 非法输入: [（安全认证）角色]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）角色]"
                    , role
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
        /*final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
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
        }*/

        final @NotNull List<SecurityUserRole> existedUserRoles = securityUserRoleService.selectByRoleCode(role.getCode());
        if (null == existedUserRoles || existedUserRoles.isEmpty()) {
            return true;
        } else {
            for (@NotNull SecurityUserRole eachUserRole : existedUserRoles) {
                if (! securityUserRoleService.delete(eachUserRole, operator)) {
                    throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__DELETION.name
                            , "操作失败"
                            , eachUserRole
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
            }
        }

        return true;
    }

    /**
     * 删除[角色 - 资源]关联
     *
     * @Description 完整的业务流程.
     *
     * @param roles     {@linkplain SecurityRole [（安全认证）角色]}, 必须全部合法且已持久化.
     * @param resources {@linkplain SecurityResource [（安全认证）资源]}, 必须全部合法且已持久化.
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    public boolean deleteRoleResourceRelationship(@NotNull Set<SecurityRole> roles, @NotNull Set<SecurityResource> resources, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException {
        if (null == roles || roles.isEmpty()) {
            //-- 非法输入: [（安全认证）角色]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）角色]"
                    , roles
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        } else {
            for (@NotNull SecurityRole each : roles) {
                if (each.isEmpty()) {
                    //-- 非法输入: [（安全认证）角色]
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[（安全认证）角色]"
                            , each
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
            }
        }
        if (null == resources || resources.isEmpty()) {
            //-- 非法输入: [（安全认证）资源]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）资源]"
                    , resources
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        } else {
            for (@NotNull SecurityResource each : resources) {
                if (each.isEmpty()) {
                    //-- 非法输入: [（安全认证）资源]
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[（安全认证）资源]"
                            , each
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
            }
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
        /*final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
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
        }*/

        boolean result = false;

        for (@NotNull SecurityRole each : roles) {
            if (! securityRoleResourceService.existResourceByRoleCode(each.getCode())) {
                //--- 角色不存在绑定的资源的情况
            } else {
                //--- 角色存在绑定的资源的情况
                final @NotNull List<SecurityRoleResource> existedRoleResources = securityRoleResourceService.selectByRoleCode(each.getCode());

                for (@NotNull SecurityResource eachResource : resources) {
                    SecurityRoleResource existedRoleResource = null;
                    for (@NotNull SecurityRoleResource roleResource : existedRoleResources) {
                        if (roleResource.equals(each, eachResource)) {
                            existedRoleResource = roleResource;
                            break;
                        }
                    }
                    if (null == existedRoleResource || existedRoleResource.isEmpty()) {
                        continue;
                    }

                    if (! securityRoleResourceService.delete(existedRoleResource, operator)) {
                        throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】&【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                                , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__DELETION.name
                                , "执行后数据异常"
                                , each
                                , eachResource
                                , operator
                                , this.getClass().getName()
                                , Thread.currentThread().getStackTrace()[1].getMethodName()
                                , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                    }
                }
            }

            result = true;
        }

        return result;
    }

    /**
     * 删除指定的[（安全认证）角色]关联的所有[（安全认证）角色 ←→ 资源]关联关系
     *
     * @Description 完整的业务流程.
     *
     * @param role      {@linkplain SecurityRole [（安全认证）角色]}, 必须合法且已持久化.
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    public boolean deleteRoleResourceRelationshipOnRole(@NotNull SecurityRole role, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == role || role.isEmpty()) {
            //-- 非法输入: [（安全认证）角色]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）角色]"
                    , role
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
        /*final @NotNull UserAccountOperationInfo operatorOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
        if (null == operatorOperationInfo || operatorOperationInfo.isEmpty()) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- 【%s】</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , "无[有效的账户操作基础记录]"
                    , operator
                    , operatorOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }*/

        final @NotNull List<SecurityRoleResource> existedRoleResources = securityRoleResourceService.selectByRoleCode(role.getCode());
        if (null == existedRoleResources || existedRoleResources.isEmpty()) {
            return true;
        } else {
            for (@NotNull SecurityRoleResource eachRoleResource : existedRoleResources) {
                if (! securityRoleResourceService.delete(eachRoleResource, operator)) {
                    throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__DELETION.name
                            , "操作失败"
                            , eachRoleResource
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
            }
        }

        return true;
    }

    /**
     * 删除指定的[（安全认证）资源]关联的所有[（安全认证）角色 ←→ 资源]关联关系
     *
     * @Description 完整的业务流程.
     *
     * @param resource  {@linkplain SecurityResource [（安全认证）资源]}, 必须合法且已持久化.
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    public boolean deleteRoleResourceRelationshipOnResource(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
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
        if (null == operator || operator.isEmpty()) {
            //-- 非法输入: 操作者
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        /*final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
        if (null == operator_userAccountOperationInfo || operator_userAccountOperationInfo.isEmpty()) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }*/

        final @NotNull List<SecurityRoleResource> existedRoleResources = securityRoleResourceService.selectByResourceCode(resource.getCode());
        if (null == existedRoleResources || existedRoleResources.isEmpty()) {
            return true;
        } else {
            for (@NotNull SecurityRoleResource eachRoleResource : existedRoleResources) {
                if (! securityRoleResourceService.delete(eachRoleResource, operator)) {
                    throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__DELETION.name
                            , "操作失败"
                            , resource
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
            }
        }

        return true;
    }

}
