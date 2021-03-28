//package github.com.suitelhy.dingding.core.domain.event.impl;
//
//import github.com.suitelhy.dingding.core.domain.aggregate.UserAggregate;
//import github.com.suitelhy.dingding.core.domain.entity.Log;
//import github.com.suitelhy.dingding.core.domain.entity.User;
//import github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo;
//import github.com.suitelhy.dingding.core.domain.entity.UserPersonInfo;
//import github.com.suitelhy.dingding.core.domain.entity.security.*;
//import github.com.suitelhy.dingding.core.domain.event.UserEvent;
//import github.com.suitelhy.dingding.core.domain.event.security.SecurityRoleEvent;
//import github.com.suitelhy.dingding.core.domain.service.LogService;
//import github.com.suitelhy.dingding.core.domain.service.UserAccountOperationInfoService;
//import github.com.suitelhy.dingding.core.domain.service.UserPersonInfoService;
//import github.com.suitelhy.dingding.core.domain.service.UserService;
//import github.com.suitelhy.dingding.core.domain.service.security.*;
//import github.com.suitelhy.dingding.core.infrastructure.domain.vo.HandleType;
//import github.com.suitelhy.dingding.security.service.api.domain.vo.Security;
//import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Isolation;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.validation.constraints.NotNull;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//
///**
// * 用户 - 复杂业务实现
// *
// * @see User
// * @see UserAccountOperationInfo
// * @see UserPersonInfo
// * @see UserAggregate
// * @see UserEvent
// * @see UserService
// * @see UserAccountOperationInfoService
// * @see UserPersonInfoService
// */
//@Order(Ordered.LOWEST_PRECEDENCE)
//@Service("userEvent")
//@Transactional(isolation = Isolation.READ_COMMITTED
//        , propagation = Propagation.REQUIRED
//        , readOnly = true
//        , rollbackFor = Exception.class
//        , timeout = 15)
//public class UserEventImpl
//        implements UserEvent {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private UserAccountOperationInfoService userAccountOperationInfoService;
//
//    @Autowired
//    private UserPersonInfoService userPersonInfoService;
//
//    @Autowired
//    private SecurityResourceService securityResourceService;
//
//    @Autowired
//    private SecurityRoleService securityRoleService;
//
//    @Autowired
//    private SecurityUserRoleService securityUserRoleService;
//
//    @Autowired
//    private SecurityRoleResourceService securityRoleResourceService;
//
//    @Autowired
//    private SecurityResourceUrlService securityResourceUrlService;
//
//    @Autowired
//    private SecurityUserService securityUserService;
//
//    @Autowired
//    private LogService logService;
//
//    //===== 查询操作业务 =====//
//
//    /**
//     * 查询指定的用户 -> 基础信息
//     *
//     * @param username {@link User.Validator#username(String)}
//     * @return {@link UserAccountOperationInfo}
//     * @Description 完整的业务流程.
//     * @see User
//     */
//    @Override
//    public @NotNull
//    User selectUserByUsername(@NotNull String username)
//            throws IllegalArgumentException {
//        if (!User.Validator.USER.username(username)) {
//            //-- 非法输入: 用户名称
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "用户名称"
//                    , username
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        final User result = userService.selectUserByUsername(username);
//        return (null != result)
//                ? result
//                : User.Factory.USER.createDefault();
//    }
//
//    /**
//     * 查询指定的用户 -> 账户操作记录
//     *
//     * @param username {@link UserAccountOperationInfo.Validator#username(String)}
//     * @return {@link UserAccountOperationInfo}
//     * @Description 完整的业务流程.
//     * @see UserAccountOperationInfo
//     * @see User
//     */
//    @Override
//    public @NotNull
//    UserAccountOperationInfo selectUserAccountOperationInfoByUsername(@NotNull String username)
//            throws IllegalArgumentException {
//        if (!UserAccountOperationInfo.Validator.USER.username(username)) {
//            //-- 非法输入: 用户名称
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "用户名称"
//                    , username
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        final UserAccountOperationInfo result = userAccountOperationInfoService.selectByUsername(username);
//        return (null != result)
//                ? result
//                : UserAccountOperationInfo.Factory.USER.createDefault();
//    }
//
//    /**
//     * 查询指定的用户 -> 个人信息
//     *
//     * @param username {@link UserPersonInfo.Validator#username(String)}
//     * @return {@link UserPersonInfo}
//     * @Description 完整的业务流程.
//     * @see UserPersonInfo
//     * @see User
//     */
//    @Override
//    public @NotNull
//    UserPersonInfo selectUserPersonInfoByUsername(@NotNull String username)
//            throws IllegalArgumentException {
//        if (!UserPersonInfo.Validator.USER.username(username)) {
//            //-- 非法输入: 用户名称
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "用户名称"
//                    , username
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        final UserPersonInfo result = userPersonInfoService.selectByUsername(username);
//        return (null != result)
//                ? result
//                : UserPersonInfo.Factory.USER.createDefault();
//    }
//
//    /**
//     * 查询指定的用户 -> [（安全认证）用户]
//     *
//     * @param username {@link User.Validator#username(String)}
//     * @return {@link UserAccountOperationInfo}
//     * @Description 完整的业务流程.
//     * @see SecurityUser
//     */
//    @Override
//    public @NotNull
//    SecurityUser selectSecurityUserByUsername(@NotNull String username)
//            throws IllegalArgumentException {
//        if (!User.Validator.USER.username(username)) {
//            //-- 非法输入: 用户名称
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "用户名称"
//                    , username
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        final User user = userService.selectUserByUsername(username);
//        if (null == user || user.isEmpty()) {
//            return SecurityUser.Factory.USER.createDefault();
//        }
//
//        final SecurityUser result = securityUserService.selectByUsername(username);
//        return (null != result)
//                ? result
//                : SecurityUser.Factory.USER.createDefault();
//    }
//
//    /**
//     * 查询 (关联的) 角色 -> (关联的) 资源
//     *
//     * @param username 用户名称    {@link SecurityUser.Validator#username(String)}
//     * @return 资源集合 {@link SecurityResource}
//     * @Description 完整的业务流程.
//     * @see github.com.suitelhy.dingding.core.domain.event.security.SecurityResourceEvent#selectResourceOnUserByUsername(String)
//     */
//    @Override
//    public @NotNull
//    List<SecurityResource> selectResourceOnUserByUsername(@NotNull String username)
//            throws IllegalArgumentException {
//        if (!SecurityUser.Validator.USER.username(username)) {
//            //-- 非法输入: 用户名称
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "用户名称"
//                    , username
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        final @NotNull List<SecurityResource> result = new ArrayList<>(1);
//
//        final @NotNull List<SecurityUserRole> user_userRoles = securityUserRoleService.selectByUsername(username);
//        if (!user_userRoles.isEmpty()) {
//            for (@NotNull SecurityUserRole eachUserRole : user_userRoles) {
//                if (eachUserRole.isEmpty()) {
//                    continue;
//                }
//
//                final @NotNull SecurityRole eachUserRole_role = securityRoleService.selectRoleByCode(eachUserRole.getRoleCode());
//                if (null == eachUserRole_role || eachUserRole_role.isEmpty()) {
//                    continue;
//                }
//
//                final @NotNull List<SecurityRoleResource> eachRole_roleResources = securityRoleResourceService.selectByRoleCode(eachUserRole_role.getCode());
//                if (!eachRole_roleResources.isEmpty()) {
//                    for (@NotNull SecurityRoleResource eachRoleResource : eachRole_roleResources) {
//                        if (eachRoleResource.isEmpty()) {
//                            continue;
//                        }
//
//                        final SecurityResource eachRoleResource_resource = securityResourceService.selectResourceByCode(eachRoleResource.getResourceCode());
//                        if (null != eachRoleResource_resource && !eachRoleResource_resource.isEmpty()) {
//                            result.add(eachRoleResource_resource);
//                        }
//                    }
//                }
//            }
//        }
//
//        return result;
//    }
//
//    /**
//     * 查询 (关联的) [用户 -> （安全认证）角色]
//     *
//     * @param username 用户名称    {@link SecurityUser.Validator#username(String)}
//     * @return {@link SecurityRole}
//     * @Description 完整的业务流程.
//     * @see github.com.suitelhy.dingding.core.domain.event.security.SecurityRoleEvent#selectRoleOnUserByUsername(String)
//     */
//    @Override
//    public @NotNull
//    List<SecurityRole> selectRoleOnUserByUsername(@NotNull String username)
//            throws IllegalArgumentException {
//        if (!SecurityUser.Validator.USER.username(username)) {
//            //-- 非法输入: 用户名称
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "用户名称"
//                    , username
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        final @NotNull List<SecurityRole> result = new ArrayList<>(0);
//
//        final @NotNull SecurityUser securityUser = securityUserService.selectByUsername(username);
//        if (securityUser.isEmpty()) {
//            return result;
//        }
//
//        @NotNull List<SecurityUserRole> securityUser_userRoles = securityUserRoleService.selectByUsername(securityUser.getUsername());
//        if (!securityUser_userRoles.isEmpty()) {
//            for (@NotNull SecurityUserRole eachUserRole : securityUser_userRoles) {
//                if (eachUserRole.isEmpty()) {
//                    continue;
//                }
//
//                final @NotNull SecurityRole eachUserRole_role = securityRoleService.selectRoleByCode(eachUserRole.getRoleCode());
//                if (!eachUserRole_role.isEmpty()) {
//                    result.add(eachUserRole_role);
//                }
//            }
//        }
//
//        return result;
//    }
//
//    /**
//     * 查询[用户 -> (关联的) 角色 -> (关联的) 资源 -> URL 相关信息]
//     *
//     * @param username 用户名称    {@link SecurityUser.Validator#username(String)}
//     * @return {@link SecurityResourceUrl}
//     * @Description 完整的业务流程.
//     * @see github.com.suitelhy.dingding.core.domain.event.security.SecurityUrlEvent#selectUrlInfoOnUserByUsername(String)
//     */
//    @Override
//    public @NotNull
//    List<SecurityResourceUrl> selectUrlInfoOnUserByUsername(@NotNull String username)
//            throws IllegalArgumentException {
//        if (!SecurityUser.Validator.USER.username(username)) {
//            //-- 非法输入: 用户名称
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "用户名称"
//                    , username
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        final @NotNull List<SecurityResourceUrl> result = new ArrayList<>(1);
//
//        final @NotNull User user = userService.selectUserByUsername(username);
//        if (null == user || user.isEmpty()) {
//            return result;
//        }
//
//        final @NotNull SecurityUser user_securityUser = securityUserService.selectByUsername(username);
//        if (null == user_securityUser || user_securityUser.isEmpty()) {
//            return result;
//        }
//
//        final @NotNull List<SecurityUserRole> user_userRoles = securityUserRoleService.selectByUsername(username);
//        if (!user_userRoles.isEmpty()) {
//            for (@NotNull SecurityUserRole eachUserRole : user_userRoles) {
//                if (eachUserRole.isEmpty()) {
//                    continue;
//                }
//
//                final @NotNull SecurityRole eachUserRole_role = securityRoleService.selectRoleByCode(eachUserRole.getRoleCode());
//                if (null == eachUserRole_role || eachUserRole_role.isEmpty()) {
//                    continue;
//                }
//
//                final @NotNull List<SecurityRoleResource> role_roleResources = securityRoleResourceService.selectByRoleCode(eachUserRole_role.getCode());
//                if (!role_roleResources.isEmpty()) {
//                    for (@NotNull SecurityRoleResource eachRoleResource : role_roleResources) {
//                        if (eachRoleResource.isEmpty()) {
//                            continue;
//                        }
//
//                        final @NotNull SecurityResource eachRoleResource_resource = securityResourceService.selectResourceByCode(eachRoleResource.getResourceCode());
//                        if (null == eachRoleResource_resource || eachRoleResource_resource.isEmpty()) {
//                            continue;
//                        }
//
//                        final @NotNull List<SecurityResourceUrl> eachRoleResource_resource_resourceUrl = securityResourceUrlService.selectByResourceCode(eachRoleResource_resource.getCode());
//                        if (!eachRoleResource_resource_resourceUrl.isEmpty()) {
//                            for (@NotNull SecurityResourceUrl eachResourceUrl : eachRoleResource_resource_resourceUrl) {
//                                if (eachResourceUrl.isEmpty()) {
//                                    continue;
//                                }
//
//                                result.add(eachResourceUrl);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        return result;
//    }
//
//    /**
//     * 查询[用户 -> (关联的) 角色 -> (关联的) 资源 -> URL 相关信息]
//     *
//     * @param username 用户名称         {@link SecurityUser.Validator#username(String)}
//     * @param clientId [资源服务器 ID]  {@link SecurityResourceUrl#getClientId()}
//     * @return {@link SecurityResourceUrl}
//     * @Description 完整的业务流程.
//     * @see github.com.suitelhy.dingding.core.domain.event.security.SecurityUrlEvent#selectUrlInfoOnUserByUsernameAndClientId(String, String)
//     */
//    @Override
//    public @NotNull
//    List<SecurityResourceUrl> selectUrlInfoOnUserByUsernameAndClientId(@NotNull String username, @NotNull String clientId)
//            throws IllegalArgumentException {
//        if (!SecurityUser.Validator.USER.username(username)) {
//            //-- 非法输入: 用户名称
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "用户名称"
//                    , username
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (!SecurityResourceUrl.Validator.RESOURCE_URL.clientId(clientId)) {
//            //-- 非法输入: [资源服务器 ID]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "[资源服务器 ID]"
//                    , clientId
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        final @NotNull List<SecurityResourceUrl> result = new ArrayList<>(1);
//
//        final @NotNull User user = userService.selectUserByUsername(username);
//        if (null == user || user.isEmpty()) {
//            return result;
//        }
//
//        final @NotNull SecurityUser user_securityUser = securityUserService.selectByUsername(username);
//        if (null == user_securityUser || user_securityUser.isEmpty()) {
//            return result;
//        }
//
//        final @NotNull List<SecurityUserRole> user_userRoles = securityUserRoleService.selectByUsername(username);
//        if (!user_userRoles.isEmpty()) {
//            for (@NotNull SecurityUserRole eachUserRole : user_userRoles) {
//                if (eachUserRole.isEmpty()) {
//                    continue;
//                }
//
//                final @NotNull SecurityRole eachUserRole_role = securityRoleService.selectRoleByCode(eachUserRole.getRoleCode());
//                if (null == eachUserRole_role || eachUserRole_role.isEmpty()) {
//                    continue;
//                }
//
//                final @NotNull List<SecurityRoleResource> role_roleResources = securityRoleResourceService.selectByRoleCode(eachUserRole_role.getCode());
//                if (!role_roleResources.isEmpty()) {
//                    for (@NotNull SecurityRoleResource eachRoleResource : role_roleResources) {
//                        if (eachRoleResource.isEmpty()) {
//                            continue;
//                        }
//
//                        final @NotNull SecurityResource eachRoleResource_resource = securityResourceService.selectResourceByCode(eachRoleResource.getResourceCode());
//                        if (null == eachRoleResource_resource || eachRoleResource_resource.isEmpty()) {
//                            continue;
//                        }
//
//                        final @NotNull List<SecurityResourceUrl> eachRoleResource_resource_resourceUrl = securityResourceUrlService.selectByResourceCode(eachRoleResource_resource.getCode());
//                        if (!eachRoleResource_resource_resourceUrl.isEmpty()) {
//                            for (@NotNull SecurityResourceUrl eachResourceUrl : eachRoleResource_resource_resourceUrl) {
//                                if (eachResourceUrl.isEmpty()) {
//                                    continue;
//                                } else if (!eachResourceUrl.getClientId().equals(clientId)) {
//                                    continue;
//                                }
//
//                                result.add(eachResourceUrl);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        return result;
//    }
//
//    //===== 添加操作业务 =====//
//
//    /**
//     * 新增一个[（安全认证）用户 ←→ 角色]关联关系
//     *
//     * @param userRole [（安全认证）用户 ←→ 角色]
//     * @param operator 操作者
//     * @return 操作是否成功 / 是否已存在相同的有效数据
//     * @Description 完整的业务流程.
//     * @see SecurityRoleEvent#insertUserRoleRelationship(SecurityUserRole, SecurityUser)
//     */
//    @Override
//    public boolean insertUserRoleRelationship(@NotNull SecurityUserRole userRole, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException {
//        if (null == userRole || userRole.isEmpty()) {
//            //-- 非法输入: [（安全认证）用户 ←→ 角色]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[（安全认证）用户 ←→ 角色]"
//                    , userRole
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == operator || operator.isEmpty()) {
//            //-- 非法输入: 操作者
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
//        if (null == operator_userAccountOperationInfo || operator_userAccountOperationInfo.isEmpty()) {
//            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
//            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】<-【%s】</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , "无[有效的账户操作基础记录]"
//                    , operator
//                    , operator_userAccountOperationInfo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        if (!securityUserRoleService.insert(userRole, operator, operator_userAccountOperationInfo)) {
//            //-- 操作失败: 新增[（安全认证）用户 ←→ 角色]关联关系
//            throw new BusinessAtomicException(String.format("操作失败:<description>%s</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__ADD.name
//                    , userRole
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        return true;
//    }
//
//    /**
//     * 新增多个[（安全认证）用户 ←→ 角色]关联关系
//     *
//     * @param user     （安全认证）用户
//     * @param roles    （安全认证）角色
//     * @param operator 操作者
//     * @return 操作是否成功 / 是否已存在完全相同的有效数据集合
//     * @see github.com.suitelhy.dingding.core.domain.event.security.SecurityRoleEvent#insertUserRoleRelationship(SecurityUser, Set, SecurityUser)
//     */
//    @Override
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    public boolean insertUserRoleRelationship(@NotNull SecurityUser user, @NotNull Set<SecurityRole> roles, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException {
//        if ((null == user || user.isEmpty())) {
//            //-- 非法输入: [（安全认证）用户]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[（安全认证）用户]"
//                    , user
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == roles || roles.isEmpty()) {
//            //-- 非法输入: [（安全认证）角色]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[（安全认证）角色]"
//                    , roles
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        } else {
//            for (SecurityRole each : roles) {
//                if ((null == each || each.isEmpty())) {
//                    //-- 非法输入: [（安全认证）角色]
//                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                            , "[（安全认证）角色]"
//                            , each
//                            , this.getClass().getName()
//                            , Thread.currentThread().getStackTrace()[1].getMethodName()
//                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//                }
//            }
//        }
//        if (null == operator || operator.isEmpty()) {
//            //-- 非法输入: 操作者
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
//        if (null == operator_userAccountOperationInfo || operator_userAccountOperationInfo.isEmpty()) {
//            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
//            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- 【%s】</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , "无[有效的账户操作基础记录]"
//                    , operator
//                    , operator_userAccountOperationInfo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        final @NotNull List<SecurityUserRole> existedUserRoles = securityUserRoleService.selectByUsername(user.getUsername());
//
//        if (!existedUserRoles.isEmpty()) {
//            //--- 角色存在绑定的资源的情况
//            for (@NotNull SecurityRole eachRole : roles) {
//                boolean exist = false;
//                for (@NotNull SecurityUserRole existedUserRole : existedUserRoles) {
//                    if (existedUserRole.equals(user, eachRole)) {
//                        exist = true;
//                        break;
//                    }
//                }
//                if (exist) {
//                    continue;
//                }
//
//                final @NotNull SecurityUserRole eachUserRole = SecurityUserRole.Factory.USER_ROLE.create(user.getUsername(), eachRole.getCode());
//                if (!securityUserRoleService.insert(eachUserRole, operator, operator_userAccountOperationInfo)) {
//                    //-- 操作失败: 新增[（安全认证）用户 ←→ 角色]关联关系
//                    throw new BusinessAtomicException(String.format("操作失败:<description>%s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                            , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__ADD.name
//                            , eachUserRole
//                            , this.getClass().getName()
//                            , Thread.currentThread().getStackTrace()[1].getMethodName()
//                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//                }
//            }
//        } else {
//            //--- 角色不存在绑定的资源的情况
//            for (@NotNull SecurityRole each : roles) {
//                final @NotNull SecurityUserRole eachUserRole = SecurityUserRole.Factory.USER_ROLE.create(user.getUsername(), each.getCode());
//                if (!securityUserRoleService.insert(eachUserRole, operator, operator_userAccountOperationInfo)) {
//                    //-- 操作失败: 新增[（安全认证）用户 ←→ 角色]关联关系
//                    throw new BusinessAtomicException(String.format("操作失败:<description>%s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                            , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__ADD.name
//                            , eachUserRole
//                            , this.getClass().getName()
//                            , Thread.currentThread().getStackTrace()[1].getMethodName()
//                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//                }
//            }
//        }
//
//        return true;
//    }
//
//    /**
//     * 新增一个[（安全认证）用户 ←→ 角色]关联关系
//     *
//     * @param user     （安全认证）用户
//     * @param roleVo   [(安全) 用户 -> 角色] {@link Security.RoleVo}, 仅需保证合法性, 不需要保证持久化.
//     * @param operator 操作者
//     * @return 操作是否成功 / 是否已存在相同的有效数据
//     * @see github.com.suitelhy.dingding.core.domain.event.security.SecurityRoleEvent#insertUserRoleRelationship(SecurityUser, Security.RoleVo, SecurityUser)
//     */
//    @Override
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    public boolean insertUserRoleRelationship(@NotNull SecurityUser user, @NotNull Security.RoleVo roleVo, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException {
//        if ((null == user || user.isEmpty())) {
//            //-- 非法输入: [（安全认证）用户]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[（安全认证）用户]"
//                    , user
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == roleVo) {
//            //-- 非法输入: [(安全) 用户 -> 角色]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[(安全) 用户 -> 角色]"
//                    , roleVo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == operator || operator.isEmpty()) {
//            //-- 非法输入: 操作者
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
//        if (null == operator_userAccountOperationInfo || operator_userAccountOperationInfo.isEmpty()) {
//            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
//            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- 【%s】</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , "无[有效的账户操作基础记录]"
//                    , operator
//                    , operator_userAccountOperationInfo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        final @NotNull List<SecurityUserRole> existedUserRoles = securityUserRoleService.selectByUsername(user.getUsername());
//
//        if (!existedUserRoles.isEmpty()) {
//            //--- 角色存在绑定的资源的情况
//            @NotNull SecurityRole role = SecurityRole.Factory.ROLE.create(roleVo);
//
//            boolean exist = false;
//            for (@NotNull SecurityUserRole existedUserRole : existedUserRoles) {
//                if (existedUserRole.equals(user, role)) {
//                    exist = true;
//                    break;
//                }
//            }
//            if (exist) {
//                return true;
//            } else {
//                @NotNull SecurityUserRole userRole = SecurityUserRole.Factory.USER_ROLE.create(user.getUsername(), role.getCode());
//                if (!securityUserRoleService.insert(userRole, operator, operator_userAccountOperationInfo)) {
//                    //-- 操作失败: 新增[（安全认证）用户 ←→ 角色]关联关系
//                    throw new BusinessAtomicException(String.format("操作失败:<description>%s</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                            , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__ADD.name
//                            , userRole
//                            , this.getClass().getName()
//                            , Thread.currentThread().getStackTrace()[1].getMethodName()
//                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//                }
//            }
//        } else {
//            //--- 角色不存在绑定的资源的情况
//            @NotNull SecurityRole role = SecurityRole.Factory.ROLE.create(roleVo);
//
//            final @NotNull SecurityUserRole userRole = SecurityUserRole.Factory.USER_ROLE.create(user.getUsername(), role.getCode());
//            if (!securityUserRoleService.insert(userRole, operator, operator_userAccountOperationInfo)) {
//                //-- 操作失败: 新增[（安全认证）用户 ←→ 角色]关联关系
//                throw new BusinessAtomicException(String.format("操作失败:<description>%s</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                        , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__ADD.name
//                        , userRole
//                        , this.getClass().getName()
//                        , Thread.currentThread().getStackTrace()[1].getMethodName()
//                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//            }
//        }
//
//        return true;
//    }
//
//    /**
//     * 新增(=注册)一个用户
//     *
//     * @param user                     预期新增的用户 -> 用户基础信息               {@link User}
//     * @param userAccountOperationInfo 预期新增的用户 -> [用户 -> 账户操作基础记录]  {@link UserAccountOperationInfo}
//     * @param userPersonInfo           [用户 -> 个人信息]                        {@link UserPersonInfo}
//     * @param operator                 操作者
//     * @return 操作是否成功
//     * @throws IllegalArgumentException
//     * @throws BusinessAtomicException
//     * @Description 完整的业务流程.
//     * @see UserAccountOperationInfoService
//     */
//    @Override
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    public boolean registerUser(@NotNull User user, @NotNull UserAccountOperationInfo userAccountOperationInfo, @NotNull UserPersonInfo userPersonInfo
//            , @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException {
//        if (null == user || !user.isEntityLegal()) {
//            //-- 非法输入: 预期新增的用户 -> 用户基础信息
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "预期新增的用户 -> 用户基础信息"
//                    , user
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == userAccountOperationInfo
//                || !userAccountOperationInfo.isEntityLegal()
//                || !userAccountOperationInfo.equals(user)) {
//            //-- 非法输入: 预期新增的用户 -> [用户 -> 账户操作基础记录]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "预期新增的用户 -> [用户 -> 账户操作基础记录]"
//                    , userAccountOperationInfo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == userPersonInfo
//                || !userPersonInfo.isEntityLegal()
//                || !userPersonInfo.equals(user)) {
//            //-- 非法输入: 预期新增的用户 -> [用户 -> 个人信息]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "预期新增的用户 -> [用户 -> 个人信息]"
//                    , userPersonInfo
//                    , user
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == operator || operator.isEmpty()) {
//            //-- 非法输入: 操作者
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
//        if (null == operator_userAccountOperationInfo || operator_userAccountOperationInfo.isEmpty()) {
//            //-- 非法输入: 操作者 <- 无有效的账户操作记录
//            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】<-【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , "无有效的账户操作记录"
//                    , operator_userAccountOperationInfo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (!Boolean.TRUE.equals(securityUserService.existAdminPermission(operator.getUsername()))) {
//            //-- 非法输入: 操作者 <- 没有足够的操作权限
//            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】<-【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , "没有足够的操作权限"
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        /*System.err.println("【调试用】==> UserServiceImpl#insert(...) 校验完成! <==");*/
//
//        // [用户 -> 用户基础信息]
//        if (!userService.insert(user, operator, operator_userAccountOperationInfo)) {
//            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】<-【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , HandleType.LogVo.USER__REGISTRATION.name
//                    , HandleType.LogVo.USER__USER__ADD.name
//                    , user
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (user.isEmpty() && null == user.getUserid()) {
//            user = userService.selectUserByUsername(user.getUsername());
//        }
//
//        //===== 关联的[用户 -> 账户操作基础记录] =====//
//
//        if (!userAccountOperationInfoService.insert(userAccountOperationInfo, operator)) {
//            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】<-【%s】</description>->【%s】&【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , HandleType.LogVo.USER__REGISTRATION.name
//                    , HandleType.LogVo.USER__USER_ACCOUNT_OPERATION_INFO__ADD.name
//                    , user
//                    , userAccountOperationInfo
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        //===== 关联的[用户 -> 个人信息] =====//
//
//        if (!userPersonInfoService.insert(userPersonInfo, operator)) {
//            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】<-【%s】</description>->【%s】&【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , HandleType.LogVo.USER__REGISTRATION.name
//                    , HandleType.LogVo.USER__USER_PERSON_INFO__ADD.name
//                    , user
//                    , userPersonInfo
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        //===== 关联的[（安全认证）用户] =====//
//
//        @NotNull SecurityUser newSecurityUser = SecurityUser.Factory.USER.create(user);
//        if (!securityUserService.insert(newSecurityUser, operator, operator_userAccountOperationInfo)) {
//            //-- 操作失败: 新增关联的 (安全) 用户
//            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】<-【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , HandleType.LogVo.USER__REGISTRATION.name
//                    , HandleType.LogVo.SECURITY__SECURITY_USER__ADD.name
//                    , newSecurityUser
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (newSecurityUser.isEmpty() && null == newSecurityUser.getUserId()) {
//            newSecurityUser = securityUserService.selectByUsername(newSecurityUser.getUsername());
//        }
//
//        // 关联的[（安全认证）角色]
//        if (!securityRoleService.existsByCode(Security.RoleVo.USER.name())
//                && !securityRoleService.insert(Security.RoleVo.USER)) {
//            //-- 操作失败: 新增[（安全认证）角色]
//            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】<-【%s】<-【%s】</description>->【%s】&【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , HandleType.LogVo.USER__REGISTRATION.name
//                    , HandleType.LogVo.SECURITY__SECURITY_USER__ADD.name
//                    , HandleType.LogVo.SECURITY__SECURITY_ROLE__ADD.name
//                    , user
//                    , Security.RoleVo.USER
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        // 关联的[（安全认证）用户 ←→ 角色]关联关系
//        final @NotNull SecurityUserRole userRole = SecurityUserRole.Factory.USER_ROLE.create(newSecurityUser
//                , Security.RoleVo.USER);
//        if (!securityUserRoleService.insert(userRole, operator, operator_userAccountOperationInfo)) {
//            //-- 操作失败: 新增[(安全) 用户 - 角色]关联关系
//            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】<-【%s】<-【%s】</description>->【%s】&【%s】&【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , HandleType.LogVo.USER__REGISTRATION.name
//                    , HandleType.LogVo.SECURITY__SECURITY_USER__ADD.name
//                    , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__ADD.name
//                    , user
//                    , newSecurityUser
//                    , userRole
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        //===== 操作日志记录 =====//
//
//        final @NotNull Log newLog_User = Log.Factory.User.LOG.create(null
//                , null
//                , HandleType.LogVo.USER__REGISTRATION
//                , user
//                , operator
//                , operator_userAccountOperationInfo);
//        if (!logService.insert(newLog_User)) {
//            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】<-【%s】</description>->【%s】&【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , HandleType.LogVo.USER__REGISTRATION.name
//                    , "生成操作日志记录"
//                    , user
//                    , newLog_User
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        //======//
//
//        return true;
//    }
//
//    //===== 修改操作业务 =====//
//
//    /**
//     * 更新指定的用户 -> 基础信息
//     *
//     * @param user     被修改的用户 -> 基础信息
//     * @param operator 操作者
//     * @return 操作是否成功
//     * @Description 完整的业务流程.
//     * @see UserService#update(User, SecurityUser, UserAccountOperationInfo)
//     */
//    @Override
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    public boolean updateUser(@NotNull User user, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException {
//        if (null == user || user.isEmpty()) {
//            //-- 非法输入: 被修改的用户
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "被修改的用户"
//                    , user
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == operator || operator.isEmpty()) {
//            //-- 非法输入: 操作者
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        final UserAccountOperationInfo operator_OperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
//        if (null == operator_OperationInfo || operator_OperationInfo.isEmpty()) {
//            //-- 非法输入: 操作者 <- 无有效的账户操作记录
//            throw new IllegalArgumentException(String.format("非法参数:<description>%s</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者 <- 无有效的账户操作记录"
//                    , operator_OperationInfo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        return userService.update(user, operator, operator_OperationInfo);
//    }
//
//    /**
//     * 更新指定的用户 -> 账户操作基础记录
//     *
//     * @param userAccountOperationInfo 被修改的用户 -> 账户操作基础记录  {@link UserAccountOperationInfo}
//     * @param operator                 操作者
//     * @return 操作是否成功
//     * @Description 完整的业务流程.
//     */
//    @Override
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    public boolean updateUserAccountOperationInfo(@NotNull UserAccountOperationInfo userAccountOperationInfo, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException {
//        if (null == userAccountOperationInfo || userAccountOperationInfo.isEmpty()) {
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[被修改的用户 -> 账户操作基础记录]"
//                    , userAccountOperationInfo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        final @NotNull User user_OldData = userService.selectUserByUsername(userAccountOperationInfo.getUsername());
//        if (null == user_OldData || user_OldData.isEmpty()) {
//            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[被修改的用户 -> 账户操作基础记录]"
//                    , "已存在的[用户 - 基础信息]"
//                    , userAccountOperationInfo
//                    , user_OldData
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        final @NotNull UserAccountOperationInfo userAccountOperationInfo_oldData = userAccountOperationInfoService.selectByUsername(userAccountOperationInfo.getUsername());
//        if (null == userAccountOperationInfo_oldData || userAccountOperationInfo_oldData.isEmpty()) {
//            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[被修改的用户 -> 账户操作基础记录]"
//                    , "已存在的[用户 -> 账户操作基础记录]"
//                    , userAccountOperationInfo
//                    , userAccountOperationInfo_oldData
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == operator || operator.isEmpty()) {
//            //-- 非法输入: 操作者
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        return userAccountOperationInfoService.update(userAccountOperationInfo, securityUserService.selectByUsername(operator.getUsername()));
//    }
//
//    /**
//     * 更新指定的用户 -> 个人信息
//     *
//     * @param userPersonInfo 被修改的用户 -> 个人信息  {@link UserPersonInfo}
//     * @param operator       操作者
//     * @return 操作是否成功
//     * @Description 完整的业务流程.
//     */
//    @Override
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    public boolean updateUserPersonInfo(@NotNull UserPersonInfo userPersonInfo, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException {
//        if (null == userPersonInfo || userPersonInfo.isEmpty()) {
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[被修改的用户 -> 个人信息]"
//                    , userPersonInfo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        final @NotNull User user_OldData = userService.selectUserByUsername(userPersonInfo.getUsername());
//        if (null == user_OldData || user_OldData.isEmpty()) {
//            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[被修改的用户 -> 个人信息]"
//                    , "已存在的[用户 - 基础信息]"
//                    , userPersonInfo
//                    , user_OldData
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        final @NotNull UserAccountOperationInfo userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(userPersonInfo.getUsername());
//        if (null == userAccountOperationInfo || userAccountOperationInfo.isEmpty()) {
//            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[被修改的用户 -> 个人信息]"
//                    , "[用户 -> 账户操作基础记录]"
//                    , userPersonInfo
//                    , userAccountOperationInfo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == operator || operator.isEmpty()) {
//            //-- 非法输入: 操作者
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        return userPersonInfoService.update(userPersonInfo, securityUserService.selectByUsername(operator.getUsername()));
//    }
//
//    /**
//     * 删除指定的用户
//     *
//     * @param user     被修改的用户 -> 基础信息
//     * @param operator 操作者
//     * @return 操作是否成功
//     * @Description 完整的业务流程.
//     */
//    @Override
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    public boolean deleteUser(@NotNull User user, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException {
//        if (null == user || user.isEmpty()) {
//            //-- 非法输入: 被删除的用户
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "被删除的用户"
//                    , user
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == operator || operator.isEmpty()) {
//            //-- 非法输入: 操作者
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
//        if (null == operator_userAccountOperationInfo || operator_userAccountOperationInfo.isEmpty()) {
//            //-- 非法输入: 操作者 <- 无有效的账户操作记录
//            throw new IllegalArgumentException(String.format("非法参数:<description>%s</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者 <- 无有效的账户操作记录"
//                    , operator_userAccountOperationInfo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        /*if (! operator.equals(user)
//                && ! securityUserService.existAdminPermission(operator.getUsername())) {
//            //-- 非法输入: 操作者 <- 没有所需的操作权限
//            throw new IllegalArgumentException(String.format("非法参数:<description>%s</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者 <- 没有所需的操作权限"
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }*/
//
//        //===== 删除关联的[（安全认证）用户 ←→ 角色]关联关系 =====//
//
//        final @NotNull List<SecurityUserRole> user_userRoleRelationship = securityUserRoleService.selectByUsername(user.getUsername());
//        if (!user_userRoleRelationship.isEmpty()) {
//            for (@NotNull SecurityUserRole existedUserRole : user_userRoleRelationship) {
//                if (!securityUserRoleService.delete(existedUserRole, operator, operator_userAccountOperationInfo)) {
//                    throw new BusinessAtomicException(String.format("操作失败:<description>【%s】<-【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                            , HandleType.LogVo.USER__USER__DELETION.name
//                            , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__DELETION.name
//                            , existedUserRole
//                            , this.getClass().getName()
//                            , Thread.currentThread().getStackTrace()[1].getMethodName()
//                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//                }
//            }
//        }
//
//        //===== 删除关联的[（安全认证）用户] =====//
//
//        final SecurityUser user_securityUser = securityUserService.selectByUsername(user.getUsername());
//        if (null != user_securityUser
//                && !securityUserService.delete(user_securityUser, operator, operator_userAccountOperationInfo)) {
//            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】<-【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , HandleType.LogVo.USER__USER__DELETION.name
//                    , HandleType.LogVo.SECURITY__SECURITY_USER__DELETE.name
//                    , user_securityUser
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        //===== 删除关联的[用户 -> 个人信息] =====//
//
//        final UserPersonInfo user_userPersonInfo = userPersonInfoService.selectByUsername(user.getUsername());
//        if (null != user_userPersonInfo
//                && !userPersonInfoService.deleteAndValidate(user_userPersonInfo, operator)) {
//            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】<-【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , HandleType.LogVo.USER__USER__DELETION.name
//                    , HandleType.LogVo.USER__USER_PERSON_INFO__DELETE.name
//                    , user_userPersonInfo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        //===== 删除关联的[用户 -> 账户操作基础记录] =====//
//
//        final UserAccountOperationInfo user_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(user.getUsername());
//        if (null != user_userAccountOperationInfo
//                && !userAccountOperationInfoService.deleteAndValidate(user_userAccountOperationInfo, operator)) {
//            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】<-【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , HandleType.LogVo.USER__USER__DELETION.name
//                    , HandleType.LogVo.USER__USER_ACCOUNT_OPERATION_INFO__DELETE.name
//                    , user_userAccountOperationInfo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        //==========//
//
//        if (!userService.deleteAndValidate(user, operator, operator_userAccountOperationInfo)) {
//            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】<-【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , HandleType.LogVo.USER__USER__DELETION.name
//                    , HandleType.LogVo.USER__USER__DATA_DELETION.name
//                    , user
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        //=== 日志记录操作 ===//
//
//        final @NotNull Log newLog_User = Log.Factory.User.LOG.create(null
//                , null
//                , HandleType.LogVo.USER__USER__DELETION
//                , user
//                , operator
//                , operator_userAccountOperationInfo);
//        if (!logService.insert(newLog_User)) {
//            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】<-【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , HandleType.LogVo.USER__USER__DELETION.name
//                    , "生成操作日志失败"
//                    , newLog_User
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        //======//
//
//        return true;
//    }
//
//    /**
//     * 删除[（安全认证）用户 ←→ 角色]关联关系
//     *
//     * @param user     [（安全认证）用户], 必须合法且已持久化.  {@link SecurityUser}
//     * @param role     [（安全认证）角色], 必须合法且已持久化.  {@link SecurityRole}
//     * @param operator 操作者
//     * @return 操作是否成功
//     * @Description 完整的业务流程.
//     * @see github.com.suitelhy.dingding.core.domain.event.security.SecurityRoleEvent#deleteUserRoleRelationship(SecurityUser, SecurityRole, SecurityUser)
//     */
//    @Override
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    public boolean deleteUserRoleRelationship(@NotNull SecurityUser user, @NotNull SecurityRole role, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException {
//        if (null == user || user.isEmpty()) {
//            //-- 非法输入: [（安全认证）用户]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[（安全认证）用户]"
//                    , user
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == role || role.isEmpty()) {
//            //-- 非法输入: [（安全认证）角色]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[（安全认证）角色]"
//                    , role
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == operator || operator.isEmpty()) {
//            //-- 非法输入: 操作者
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
//        if (null == operator_userAccountOperationInfo || operator_userAccountOperationInfo.isEmpty()) {
//            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
//            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】<-【%s】</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , "无[有效的账户操作基础记录]"
//                    , operator
//                    , operator_userAccountOperationInfo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        final @NotNull SecurityUserRole existedUserRole = securityUserRoleService.selectByUsernameAndRoleCode(user.getUsername(), role.getCode());
//        if (null == existedUserRole || existedUserRole.isEmpty()) {
//            return true;
//        } else {
//            if (!securityUserRoleService.delete(existedUserRole, operator, operator_userAccountOperationInfo)) {
//                throw new BusinessAtomicException(String.format("操作失败:<description>【%s】<-【%s】</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                        , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__DELETION.name
//                        , "执行后数据异常"
//                        , user
//                        , role
//                        , this.getClass().getName()
//                        , Thread.currentThread().getStackTrace()[1].getMethodName()
//                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//            }
//        }
//
//        return true;
//    }
//
//    /**
//     * 删除指定的[（安全认证）用户]关联的所有[（安全认证）用户 ←→ 角色]关联关系
//     *
//     * @param user     [（安全认证）用户], 必须合法且已持久化.  {@link SecurityUser}
//     * @param operator 操作者
//     * @return 操作是否成功
//     * @Description 完整的业务流程.
//     * @see github.com.suitelhy.dingding.core.domain.event.security.SecurityRoleEvent#deleteUserRoleRelationshipOnUser(SecurityUser, SecurityUser)
//     */
//    @Override
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    public boolean deleteUserRoleRelationshipOnUser(@NotNull SecurityUser user, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException {
//        if (null == user || user.isEmpty()) {
//            //-- 非法输入: [（安全认证）用户]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[（安全认证）用户]"
//                    , user
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == operator || operator.isEmpty()) {
//            //-- 非法输入: 操作者
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
//        if (null == operator_userAccountOperationInfo || operator_userAccountOperationInfo.isEmpty()) {
//            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
//            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】<-【%s】</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , "无[有效的账户操作基础记录]"
//                    , operator
//                    , operator_userAccountOperationInfo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        final @NotNull List<SecurityUserRole> existedUserRoles = securityUserRoleService.selectByUsername(user.getUsername());
//        if (null == existedUserRoles || existedUserRoles.isEmpty()) {
//            return true;
//        } else {
//            for (@NotNull SecurityUserRole eachUserRole : existedUserRoles) {
//                if (!securityUserRoleService.delete(eachUserRole, operator, operator_userAccountOperationInfo)) {
//                    throw new BusinessAtomicException(String.format("操作失败:<description>【%s】<-【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                            , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__DELETION.name
//                            , "操作失败"
//                            , eachUserRole
//                            , this.getClass().getName()
//                            , Thread.currentThread().getStackTrace()[1].getMethodName()
//                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//                }
//            }
//        }
//
//        return true;
//    }
//
//    /**
//     * 删除指定的[（安全认证）角色]关联的所有[（安全认证）用户 ←→ 角色]关联关系
//     *
//     * @param role     [（安全认证）角色], 必须合法且已持久化.  {@link SecurityRole}
//     * @param operator 操作者
//     * @return 操作是否成功
//     * @Description 完整的业务流程.
//     * @see github.com.suitelhy.dingding.core.domain.event.security.SecurityRoleEvent#deleteUserRoleRelationshipOnRole(SecurityRole, SecurityUser)
//     */
//    @Override
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    public boolean deleteUserRoleRelationshipOnRole(@NotNull SecurityRole role, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException {
//        if (null == role || role.isEmpty()) {
//            //-- 非法输入: [（安全认证）角色]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[（安全认证）角色]"
//                    , role
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == operator || operator.isEmpty()) {
//            //-- 非法输入: 操作者
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
//        if (null == operator_userAccountOperationInfo || operator_userAccountOperationInfo.isEmpty()) {
//            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
//            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】<-【%s】</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , "无[有效的账户操作基础记录]"
//                    , operator
//                    , operator_userAccountOperationInfo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        final @NotNull List<SecurityUserRole> existedUserRoles = securityUserRoleService.selectByRoleCode(role.getCode());
//        if (null == existedUserRoles || existedUserRoles.isEmpty()) {
//            return true;
//        } else {
//            for (@NotNull SecurityUserRole eachUserRole : existedUserRoles) {
//                if (!securityUserRoleService.delete(eachUserRole, operator, operator_userAccountOperationInfo)) {
//                    throw new BusinessAtomicException(String.format("操作失败:<description>【%s】<-【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                            , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__DELETION.name
//                            , "操作失败"
//                            , eachUserRole
//                            , this.getClass().getName()
//                            , Thread.currentThread().getStackTrace()[1].getMethodName()
//                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//                }
//            }
//        }
//
//        return true;
//    }
//
//}
