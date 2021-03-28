//package github.com.suitelhy.dingding.core.domain.event.security.impl;
//
//import github.com.suitelhy.dingding.core.domain.entity.Log;
//import github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo;
//import github.com.suitelhy.dingding.core.domain.entity.security.*;
//import github.com.suitelhy.dingding.core.domain.event.UserEvent;
//import github.com.suitelhy.dingding.core.domain.event.security.SecurityResourceEvent;
//import github.com.suitelhy.dingding.core.domain.event.security.SecurityRoleEvent;
//import github.com.suitelhy.dingding.core.domain.service.LogService;
//import github.com.suitelhy.dingding.core.domain.service.UserAccountOperationInfoService;
//import github.com.suitelhy.dingding.core.domain.service.security.*;
//import github.com.suitelhy.dingding.core.infrastructure.domain.vo.HandleType;
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
//import java.util.Arrays;
//import java.util.List;
//import java.util.Set;
//
///**
// * (安全) 资源 - 复杂业务实现
// *
// * @see SecurityResource
// * @see SecurityRole
// * @see SecurityResourceUrl
// * @see SecurityRoleResource
// * @see SecurityRoleEvent
// * @see SecurityResourceService
// * @see SecurityRoleService
// * @see SecurityResourceUrlService
// * @see SecurityRoleResourceService
// * @see UserAccountOperationInfoService
// * @see LogService
// */
//@Order(Ordered.LOWEST_PRECEDENCE)
//@Service("securityResourceEvent")
//@Transactional(isolation = Isolation.READ_COMMITTED
//        , propagation = Propagation.REQUIRED
//        , readOnly = true
//        , rollbackFor = Exception.class
//        , timeout = 15)
//public class SecurityResourceEventImpl
//        implements SecurityResourceEvent {
//
//    @Autowired
//    private SecurityResourceService securityResourceService;
//
//    @Autowired
//    private SecurityRoleService securityRoleService;
//
//    @Autowired
//    private SecurityResourceUrlService securityResourceUrlService;
//
//    @Autowired
//    private SecurityRoleResourceService securityRoleResourceService;
//
//    @Autowired
//    private SecurityUserRoleService securityUserRoleService;
//
//    @Autowired
//    private UserAccountOperationInfoService userAccountOperationInfoService;
//
//    @Autowired
//    private LogService logService;
//
//    //===== 查询操作业务 =====//
//
//    /**
//     * 判断[（安全认证）角色]是否存在 (关联的) [（安全认证）资源]
//     *
//     * @param roleCode 角色编码 {@link SecurityRole.Validator#code(String)}
//     * @return {@link Boolean#TYPE}
//     * @Description 完整的业务流程.
//     */
//    @Override
//    public boolean existResourceOnRoleByRoleCode(@NotNull String roleCode) {
//        if (!SecurityRoleResource.Validator.ROLE_RESOURCE.roleCode(roleCode)) {
//            //-- 非法输入: 角色编码
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "角色编码"
//                    , roleCode
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        return securityRoleResourceService.existResourceByRoleCode(roleCode);
//    }
//
//    /**
//     * 判断[（安全认证）角色]是否存在 (关联的) [（安全认证）资源]
//     *
//     * @param urlInfo URL相关信息.    {@link SecurityResourceUrl.Validator#urlInfo(String[])}
//     * @return {@link Boolean#TYPE}
//     * @Description 完整的业务流程.
//     */
//    @Override
//    public boolean existResourceOnUrlInfo(@NotNull String[] urlInfo)
//            throws IllegalArgumentException {
//        if (!SecurityResourceUrl.Validator.RESOURCE_URL.urlInfo(urlInfo)) {
//            //-- 非法输入: [URL 相关信息]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[URL 相关信息]"
//                    , Arrays.toString(urlInfo)
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        return securityResourceUrlService.existResourceByUrlInfo(urlInfo);
//    }
//
//    /**
//     * 判断[（安全认证）资源]是否存在 (关联的) [（安全认证）角色]
//     *
//     * @param resourceCode 资源编码 {@link SecurityResource.Validator#code(String)}
//     * @return {@link Boolean#TYPE}
//     * @Description 完整的业务流程.
//     */
//    @Override
//    public boolean existRoleOnResourceByResourceCode(@NotNull String resourceCode) {
//        if (!SecurityRoleResource.Validator.ROLE_RESOURCE.resourceCode(resourceCode)) {
//            //-- 非法输入: 资源编码
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "资源编码"
//                    , resourceCode
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        return securityRoleResourceService.existRoleByResourceCode(resourceCode);
//    }
//
//    /**
//     * 判断[（安全认证）资源]是否存在 (关联的) [URL 相关信息]
//     *
//     * @param resourceCode 资源编码    {@link SecurityResource.Validator#code(String)}
//     * @return {@link Boolean#TYPE}
//     * @Description 完整的业务流程.
//     */
//    @Override
//    public boolean existUrlInfoOnResourceByResourceCode(@NotNull String resourceCode)
//            throws IllegalArgumentException {
//        if (!SecurityResourceUrl.Validator.RESOURCE_URL.code(resourceCode)) {
//            //-- 非法输入: 资源编码
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "资源编码"
//                    , resourceCode
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        return securityResourceUrlService.existUrlByResourceCode(resourceCode);
//    }
//
//    /**
//     * 查询 (指定角色关联的) 资源
//     *
//     * @param roleCode 角色编码    {@link SecurityRole.Validator#code(String)}
//     * @return {@link SecurityResource}
//     * @Description 完整的业务流程.
//     */
//    @Override
//    public @NotNull
//    List<SecurityResource> selectResourceOnRoleByRoleCode(@NotNull String roleCode)
//            throws IllegalArgumentException {
//        if (!SecurityRole.Validator.ROLE.code(roleCode)) {
//            //-- 非法输入: 角色编码
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "角色编码"
//                    , roleCode
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        final @NotNull List<SecurityResource> result = new ArrayList<>(0);
//
//        final SecurityRole role = securityRoleService.selectRoleByCode(roleCode);
//        if (null == role || role.isEmpty()) {
//            return result;
//        }
//
//        @NotNull List<SecurityRoleResource> role_roleResources = securityRoleResourceService.selectByRoleCode(roleCode);
//        if (!role_roleResources.isEmpty()) {
//            for (@NotNull SecurityRoleResource eachRoleResource : role_roleResources) {
//                if (eachRoleResource.isEmpty()) {
//                    continue;
//                }
//                SecurityResource eachResource = securityResourceService.selectResourceByCode(eachRoleResource.getResourceCode());
//                if (null == eachResource || eachResource.isEmpty()) {
//                    continue;
//                }
//
//                result.add(eachResource);
//            }
//        }
//
//        return result;
//    }
//
//    /**
//     * 查询[(关联的) 角色 -> (关联的) 资源]
//     *
//     * @param username 用户名称    {@link SecurityUser.Validator#username(String)}
//     * @return 资源集合 {@link SecurityResource}
//     * @Description 完整的业务流程.
//     * @see UserEvent#selectResourceOnUserByUsername(String)
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
//     * 查询 (指定资源关联的) 角色
//     *
//     * @param resourceCode 资源编码    {@link SecurityResource#getCode()}
//     * @return {@link SecurityRole}
//     * @Description 完整的业务流程.
//     */
//    @Override
//    public @NotNull
//    List<SecurityRole> selectRoleOnResourceByResourceCode(@NotNull String resourceCode)
//            throws IllegalArgumentException {
//        if (!SecurityResource.Validator.RESOURCE.code(resourceCode)) {
//            //-- 非法输入: 资源编码
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "资源编码"
//                    , resourceCode
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        final @NotNull List<SecurityRole> result = new ArrayList<>(0);
//
//        final @NotNull SecurityResource resource = securityResourceService.selectResourceByCode(resourceCode);
//        if (null == resource || resource.isEmpty()) {
//            return result;
//        }
//
//        final @NotNull List<SecurityRoleResource> resource_roleResources = securityRoleResourceService.selectByResourceCode(resourceCode);
//        if (!resource_roleResources.isEmpty()) {
//            for (@NotNull SecurityRoleResource eachRoleResource : resource_roleResources) {
//                if (eachRoleResource.isEmpty()) {
//                    continue;
//                }
//                final @NotNull SecurityRole eachRole = securityRoleService.selectRoleByCode(eachRoleResource.getRoleCode());
//                if (null == eachRole || eachRole.isEmpty()) {
//                    continue;
//                }
//
//                result.add(eachRole);
//            }
//        }
//
//        return result;
//    }
//
//    /**
//     * 查询[URL 相关信息]
//     *
//     * @param resourceCode 资源编码    {@link SecurityResourceUrl.Validator#code(String)}
//     * @return {@link SecurityResourceUrl}
//     * @Description 完整的业务流程.
//     * @see github.com.suitelhy.dingding.core.domain.event.security.SecurityUrlEvent#selectUrlInfoOnResourceByResourceCode(String)
//     */
//    @Override
//    public @NotNull
//    List<SecurityResourceUrl> selectUrlInfoOnResourceByResourceCode(@NotNull String resourceCode)
//            throws IllegalArgumentException {
//        if (!SecurityResourceUrl.Validator.RESOURCE_URL.code(resourceCode)) {
//            //-- 非法输入: 资源编码
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "资源编码"
//                    , resourceCode
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        return securityResourceUrlService.selectByResourceCode(resourceCode);
//    }
//
//    /**
//     * 新增一个[（安全认证）资源]
//     *
//     * @param resource [（安全认证）资源]  {@link SecurityResource}
//     * @param operator 操作者
//     * @return 操作是否成功 / 是否已存在相同的有效数据
//     * @Description 完整的业务流程.
//     */
//    @Override
//    public boolean insertResource(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException {
//        if (null == resource || !resource.isEntityLegal()) {
//            //-- 非法输入: [（安全认证）资源]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[（安全认证）资源]"
//                    , resource
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
//            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , "无[有效的账户操作基础记录]"
//                    , operator_userAccountOperationInfo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        return securityResourceService.insert(resource, operator, operator_userAccountOperationInfo);
//    }
//
//    //===== 添加操作业务 =====//
//
//    /**
//     * 新增一个[（安全认证）资源 ←→ URL]关联关系
//     *
//     * @param resourceUrl [（安全认证）资源 ←→ URL]   {@link SecurityResourceUrl}
//     * @param operator    操作者
//     * @return 操作是否成功 / 是否已存在相同的有效数据
//     * @Description 完整的业务流程.
//     */
//    @Override
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    public boolean insertResourceUrlRelationship(@NotNull SecurityResourceUrl resourceUrl, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException {
//        if (null == resourceUrl || !resourceUrl.isEntityLegal()) {
//            //-- 非法输入: [（安全认证）资源 ←→ URL]关联关系
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[（安全认证）资源 ←→ URL]关联关系"
//                    , resourceUrl
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
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        SecurityResource existedResource = securityResourceService.selectResourceByCode(resourceUrl.getCode());
//        if (null == existedResource || existedResource.isEmpty()) {
//            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- 【%s】</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[（安全认证）资源 ←→ URL]关联关系"
//                    , "关联的[（安全认证）资源]"
//                    , resourceUrl
//                    , existedResource
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        if (!securityResourceUrlService.insert(resourceUrl, operator, operator_userAccountOperationInfo)) {
//            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__ADD.name
//                    , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__ADD.name
//                    , resourceUrl
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        return true;
//    }
//
//    /**
//     * 新增一个[（安全认证）资源 ←→ URL]关联关系
//     *
//     * @param resource [（安全认证）资源], 必须合法且已持久化.   {@link SecurityResource}
//     * @param urlInfo  [URL 相关信息]                       {@link SecurityResourceUrl.Validator#urlInfo(String[])}
//     * @param operator 操作者
//     * @return 操作是否成功
//     * @Description 完整的业务流程.
//     */
//    @Override
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    public boolean insertResourceUrlRelationship(@NotNull SecurityResource resource
//            , @NotNull String[] urlInfo
//            , @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException {
//        if (null == resource || resource.isEmpty()) {
//            //-- 非法输入: [（安全认证）资源]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[（安全认证）资源]"
//                    , resource
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (!SecurityResourceUrl.Validator.RESOURCE_URL.urlInfo(urlInfo)) {
//            //-- 非法输入: [URL 相关信息]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[URL 相关信息]"
//                    , resource
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
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        SecurityResource existedResource = securityResourceService.selectResourceByCode(resource.getCode());
//        if (null == existedResource || existedResource.isEmpty()) {
//            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- 【%s】</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[（安全认证）资源]"
//                    , "已存在的[（安全认证）资源]"
//                    , resource
//                    , existedResource
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        final @NotNull SecurityResourceUrl resourceUrl = SecurityResourceUrl.Factory.RESOURCE_URL.create(resource, urlInfo);
//        if (!securityResourceUrlService.insert(resourceUrl, operator, operator_userAccountOperationInfo)) {
//            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__ADD.name
//                    , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__ADD.name
//                    , resourceUrl
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        return true;
//    }
//
//    /**
//     * 新增 角色 - 资源 关联
//     *
//     * @param roles     [（安全认证）角色], 必须全部合法且已持久化.    {@link SecurityRole}
//     * @param resources [（安全认证）资源], 必须全部合法且已持久化.    {@link SecurityResource}
//     * @param operator  操作者
//     * @return 操作是否成功
//     * @Description 完整的业务流程.
//     * @Issue · 多次单元测试后发现 并发一致性问题 - 读脏数据 的问题 (主要使用成熟方案解决问题, 故没有分配时间进行更多不必要地步进式问题跟踪).
//     * {@link <a href="https://github.com/CyC2018/CS-Notes/blob/master/notes/%E6%95%B0%E6%8D%AE%E5%BA%93%E7%B3%BB%E7%BB%9F%E5%8E%9F%E7%90%86.md#%E8%AF%BB%E8%84%8F%E6%95%B0%E6%8D%AE">CS-Notes/数据库系统原理.md at master · CyC2018/CS-Notes</a>}
//     * @Solution · 并发一致性问题解决方案: {@link <a href="https://juejin.im/post/5c1852526fb9a04a0c2e5db6">Spring Boot+SQL/JPA实战悲观锁和乐观锁 - 掘金</a>}
//     */
//    @Override
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    public boolean insertRoleResourceRelationship(@NotNull Set<SecurityRole> roles, @NotNull Set<SecurityResource> resources, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException {
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
//                if (null == each || each.isEmpty()) {
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
//        if (null == resources || resources.isEmpty()) {
//            //-- 非法输入: [（安全认证）资源]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[（安全认证）资源]"
//                    , resources
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        } else {
//            for (SecurityResource each : resources) {
//                if (null == each || each.isEmpty()) {
//                    //-- 非法输入: [（安全认证）资源]
//                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                            , "[（安全认证）资源]"
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
//        final @NotNull UserAccountOperationInfo operatorOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
//        if (null == operatorOperationInfo || operatorOperationInfo.isEmpty()) {
//            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
//            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- 【%s】</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , "无[有效的账户操作基础记录]"
//                    , operator
//                    , operatorOperationInfo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        /*System.err.println("========== insertResource(Set<SecurityRole>, Set<SecurityResource>) ==========");
//        System.err.println("角色参数:" + roles);
//        System.err.println("资源参数:" + resources);*/
//
//        boolean result = false;
//
//        for (@NotNull SecurityRole each : roles) {
//            if (!securityRoleResourceService.existResourceByRoleCode(each.getCode())) {
//                //--- 角色不存在绑定的资源的情况
//                /*System.err.println("角色" + each + "不存在绑定的资源集合");*/
//                for (@NotNull SecurityResource eachResource : resources) {
//                    /*System.err.println("== 开始绑定, 资源" + eachResource + "... ==");*/
//                    final @NotNull SecurityRoleResource eachRoleResource = SecurityRoleResource.Factory.ROLE_RESOURCE.create(each, eachResource);
//                    if (!securityRoleResourceService.insert(eachRoleResource, operator)) {
//                        throw new BusinessAtomicException(String.format("操作失败:<description>%s!</description>->【%s】&【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                                , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD.name
//                                , each
//                                , eachResource
//                                , operator
//                                , this.getClass().getName()
//                                , Thread.currentThread().getStackTrace()[1].getMethodName()
//                                , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//                    }
//                    /*System.err.println("== 绑定成功: 角色" + each + " - 资源" + eachResource + " ==");*/
//
//                    //=== 操作日志记录 ===//
//                    final @NotNull Log newLog_SecurityRoleResource = Log.Factory.SecurityRoleResource.LOG.create(null
//                            , null
//                            , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD
//                            , SecurityRoleResource.Factory.ROLE_RESOURCE.create(each.getCode(), eachResource.getCode())
//                            , operator
//                            , operatorOperationInfo);
//                    if (!logService.insert(newLog_SecurityRoleResource)) {
//                        throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                                , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD.name
//                                , "生成操作日志记录"
//                                , newLog_SecurityRoleResource
//                                , this.getClass().getName()
//                                , Thread.currentThread().getStackTrace()[1].getMethodName()
//                                , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//                    }
//                    //======//
//                }
//            } else {
//                //--- 角色存在绑定的资源的情况
//                final @NotNull List<SecurityRoleResource> existedRoleResources = securityRoleResourceService.selectByRoleCode(each.getCode());
//
//                /*System.err.println("角色" + each + "存在绑定的资源集合: " + existedRoleResources);*/
//                for (@NotNull SecurityResource eachResource : resources) {
//                    boolean exist = false;
//                    for (@NotNull SecurityRoleResource existedRoleResource : existedRoleResources) {
//                        if (existedRoleResource.equals(each, eachResource)) {
//                            exist = true;
//                            break;
//                        }
//                    }
//                    if (exist) continue;
//
//                    /*System.err.println("== 开始绑定, 资源" + eachResource + "... ==");*/
//                    final @NotNull SecurityRoleResource eachRoleResource = SecurityRoleResource.Factory.ROLE_RESOURCE.create(each, eachResource);
//                    if (!securityRoleResourceService.insert(eachRoleResource, operator)) {
//                        throw new BusinessAtomicException(String.format("操作失败:<description>%s!</description>->【%s】&【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                                , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD.name
//                                , each
//                                , eachResource
//                                , operator
//                                , this.getClass().getName()
//                                , Thread.currentThread().getStackTrace()[1].getMethodName()
//                                , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//                    }
//                    /*System.err.println("== 绑定成功: 角色" + each + " - 资源" + eachResource + " ==");*/
//
//                    //=== 操作日志记录 ===//
//                    final @NotNull Log newLog_SecurityRoleResource = Log.Factory.SecurityRoleResource.LOG.create(null
//                            , null
//                            , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD
//                            , SecurityRoleResource.Factory.ROLE_RESOURCE.create(each.getCode(), eachResource.getCode())
//                            , operator
//                            , operatorOperationInfo);
//                    if (!logService.insert(newLog_SecurityRoleResource)) {
//                        throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                                , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD.name
//                                , "生成操作日志记录"
//                                , newLog_SecurityRoleResource
//                                , this.getClass().getName()
//                                , Thread.currentThread().getStackTrace()[1].getMethodName()
//                                , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//                    }
//                    //======//
//                }
//            }
//
//            result = true;
//        }
//
//        return result;
//    }
//
//    //===== 修改操作业务 =====//
//
//    /**
//     * 更新指定的资源
//     *
//     * @param resource [（安全认证）资源]  {@link SecurityResource}
//     * @param operator 操作者
//     * @return 操作是否成功
//     * @Description 全量更新.
//     * · 完整的业务流程.
//     */
//    @Override
//    public boolean updateResource(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException {
//        if (null == resource || resource.isEmpty()) {
//            //-- 非法输入: [（安全认证）资源]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[（安全认证）资源]"
//                    , resource
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
//        return securityResourceService.update(resource, operator, operator_userAccountOperationInfo);
//    }
//
//    //===== 删除操作业务 =====//
//
//    /**
//     * 删除指定的资源
//     *
//     * @param resource [（安全认证）资源]
//     * @param operator 操作者
//     * @return 操作是否成功
//     * @Description · 完整的业务流程.
//     */
//    @Override
//    public boolean deleteResource(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException {
//        if (null == resource || resource.isEmpty()) {
//            //-- 非法输入: [（安全认证）资源]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[（安全认证）资源]"
//                    , resource
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
//        final SecurityResource existedResource = securityResourceService.selectResourceByCode(resource.getCode());
//        if (null == existedResource || existedResource.isEmpty()) {
//            //-- 预期删除的指定[（安全认证）资源]不存在有效数据的情况
//            return false;
//        }
//
//        //===== 删除关联的[（安全认证）资源 ←→ URL]关联关系 =====//
//
//        @NotNull List<SecurityResourceUrl> existedResourceUrls = securityResourceUrlService.selectByResourceCode(resource.getCode());
//        if (!existedResourceUrls.isEmpty()) {
//            for (@NotNull SecurityResourceUrl eachResourceUrl : existedResourceUrls) {
//                if (!securityResourceUrlService.delete(eachResourceUrl, operator, operator_userAccountOperationInfo)) {
//                    throw new IllegalArgumentException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                            , HandleType.LogVo.SECURITY__SECURITY_RESOURCE__DELETION.name
//                            , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__DELETION.name
//                            , resource
//                            , eachResourceUrl
//                            , this.getClass().getName()
//                            , Thread.currentThread().getStackTrace()[1].getMethodName()
//                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//                }
//            }
//        }
//
//        //===== 删除关联的[（安全认证）角色 ←→ 资源]关联关系 =====//
//
//        @NotNull List<SecurityRoleResource> existedRoleResources = securityRoleResourceService.selectByResourceCode(resource.getCode());
//        if (!existedRoleResources.isEmpty()) {
//            for (@NotNull SecurityRoleResource eachRoleResource : existedRoleResources) {
//                if (!securityRoleResourceService.delete(eachRoleResource, operator, operator_userAccountOperationInfo)) {
//                    throw new IllegalArgumentException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                            , HandleType.LogVo.SECURITY__SECURITY_RESOURCE__DELETION.name
//                            , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__DELETION.name
//                            , resource
//                            , eachRoleResource
//                            , this.getClass().getName()
//                            , Thread.currentThread().getStackTrace()[1].getMethodName()
//                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//                }
//            }
//        }
//
//        //==========//
//
//        if (!securityResourceService.delete(resource, operator, operator_userAccountOperationInfo)) {
//            throw new IllegalArgumentException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , HandleType.LogVo.SECURITY__SECURITY_RESOURCE__DELETION.name
//                    , HandleType.LogVo.SECURITY__SECURITY_RESOURCE__DELETION.name
//                    , resource
//                    , resource
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        return true;
//    }
//
//    /**
//     * 删除指定的[（安全认证）资源 ←→ URL]关联关系
//     *
//     * @param resourceUrl [（安全认证）资源 ←→ URL]   {@link SecurityResourceUrl}
//     * @param operator    操作者
//     * @return 操作是否成功
//     * @Description · 完整的业务流程.
//     */
//    @Override
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    public boolean deleteResourceUrlRelationship(@NotNull SecurityResourceUrl resourceUrl, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException {
//        if (null == resourceUrl || !resourceUrl.isEmpty()) {
//            //-- 非法输入: [（安全认证）资源 ←→ URL]关联关系
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[（安全认证）资源 ←→ URL]关联关系"
//                    , resourceUrl
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
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        if (!securityResourceUrlService.delete(resourceUrl, operator, operator_userAccountOperationInfo)) {
//            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__DELETION.name
//                    , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__DELETION.name
//                    , resourceUrl
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        return true;
//    }
//
//    /**
//     * 删除指定的[（安全认证）资源 ←→ URL]关联关系
//     *
//     * @param resource [（安全认证）资源], 必须合法且已持久化.  {@link SecurityResource}
//     * @param urlInfo  [URL 相关信息]                      {@link SecurityResourceUrl.Validator#urlInfo(String[])}
//     * @param operator 操作者
//     * @return 操作是否成功
//     * @Description · 完整的业务流程.
//     */
//    @Override
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    public boolean deleteResourceUrlRelationship(@NotNull SecurityResource resource, @NotNull String[] urlInfo, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException {
//        if (null == resource || resource.isEmpty()) {
//            //-- 非法输入: [（安全认证）资源]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[（安全认证）资源]"
//                    , resource
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (!SecurityResourceUrl.Validator.RESOURCE_URL.urlInfo(urlInfo)) {
//            //-- 非法输入: [URL 相关信息]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[URL 相关信息]"
//                    , Arrays.toString(urlInfo)
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
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        @NotNull SecurityResourceUrl resourceUrl = SecurityResourceUrl.Factory.RESOURCE_URL.create(resource, urlInfo);
//        if (securityResourceUrlService.existResourceUrlByResourceUrl(resourceUrl)) {
//            resourceUrl = securityResourceUrlService.selectByResourceUrl(resourceUrl);
//            if (resourceUrl.isEmpty()) {
//                return true;
//            }
//
//            if (!securityResourceUrlService.delete(resourceUrl, operator, operator_userAccountOperationInfo)) {
//                throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                        , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__DELETION.name
//                        , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__DELETION.name
//                        , resourceUrl
//                        , this.getClass().getName()
//                        , Thread.currentThread().getStackTrace()[1].getMethodName()
//                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//            }
//        }
//        return true;
//    }
//
//    /**
//     * 删除指定的[（安全认证）资源]关联的所有[（安全认证）资源 ←→ URL]关联关系
//     *
//     * @param resource [（安全认证）资源]  {@link SecurityResource}
//     * @param operator 操作者
//     * @return 操作是否成功
//     * @Description · 完整的业务流程.
//     */
//    @Override
//    public boolean deleteResourceUrlRelationshipOnResource(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException {
//        if (null == resource || !resource.isEmpty()) {
//            //-- 非法输入: [（安全认证）资源]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[（安全认证）资源]"
//                    , resource
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
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        final @NotNull List<SecurityResourceUrl> existedResourceUrls = securityResourceUrlService.selectByResourceCode(resource.getCode());
//        if (!existedResourceUrls.isEmpty()) {
//            for (@NotNull SecurityResourceUrl eachResourceUrl : existedResourceUrls) {
//                if (!securityResourceUrlService.delete(eachResourceUrl, operator, operator_userAccountOperationInfo)) {
//                    throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                            , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__DELETION.name
//                            , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__DELETION.name
//                            , eachResourceUrl
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
//     * 删除指定的[URL 信息]关联的所有[（安全认证）资源 ←→ URL]关联关系
//     *
//     * @param urlInfo  [URL 信息]    {@link SecurityResourceUrl.Validator#urlInfo(String[])}
//     * @param operator 操作者
//     * @return 操作是否成功
//     * @Description · 完整的业务流程.
//     */
//    @Override
//    public boolean deleteResourceUrlRelationshipOnUrlInfo(@NotNull String[] urlInfo, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException {
//        if (!SecurityResourceUrl.Validator.RESOURCE_URL.urlInfo(urlInfo)) {
//            //-- 非法输入: [URL 信息]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[URL 信息]"
//                    , Arrays.toString(urlInfo)
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
//        return securityResourceUrlService.delete(urlInfo, operator, operator_userAccountOperationInfo);
//    }
//
//    /**
//     * 删除[角色 - 资源]关联
//     *
//     * @param roles     [（安全认证）角色], 必须全部合法且已持久化.    {@link SecurityRole}
//     * @param resources [（安全认证）资源], 必须全部合法且已持久化.    {@link SecurityResource}
//     * @param operator  操作者
//     * @return 操作是否成功
//     * @Description 完整的业务流程.
//     */
//    @Override
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    public boolean deleteRoleResourceRelationship(@NotNull Set<SecurityRole> roles, @NotNull Set<SecurityResource> resources, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException {
//        if ((null == roles || roles.isEmpty())) {
//            //-- 非法输入: [（安全认证）角色]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[（安全认证）角色]"
//                    , roles
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        } else {
//            for (SecurityRole each : roles) {
//                if (null == each || each.isEmpty()) {
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
//        if (null == resources || resources.isEmpty()) {
//            //-- 非法输入: [（安全认证）资源]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[（安全认证）资源]"
//                    , resources
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        } else {
//            for (SecurityResource each : resources) {
//                if (null == each || each.isEmpty()) {
//                    //-- 非法输入: [（安全认证）资源]
//                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                            , "[（安全认证）资源]"
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
//        boolean result = false;
//
//        for (@NotNull SecurityRole each : roles) {
//            if (!securityRoleResourceService.existResourceByRoleCode(each.getCode())) {
//                //--- 角色不存在绑定的资源的情况
//            } else {
//                //--- 角色存在绑定的资源的情况
//                final @NotNull List<SecurityRoleResource> existedRoleResources = securityRoleResourceService.selectByRoleCode(each.getCode());
//
//                for (@NotNull SecurityResource eachResource : resources) {
//                    SecurityRoleResource existedRoleResource = null;
//                    for (@NotNull SecurityRoleResource roleResource : existedRoleResources) {
//                        if (roleResource.equals(each, eachResource)) {
//                            existedRoleResource = roleResource;
//                            break;
//                        }
//                    }
//                    if (null == existedRoleResource || existedRoleResource.isEmpty()) {
//                        continue;
//                    }
//
//                    /*if (! securityRoleResourceService.delete(each, eachResource, operator)) {*/
//                    if (!securityRoleResourceService.delete(existedRoleResource, operator, operator_userAccountOperationInfo)) {
//                        throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】&【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                                , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__DELETION.name
//                                , "执行后数据异常"
//                                , each
//                                , eachResource
//                                , operator
//                                , this.getClass().getName()
//                                , Thread.currentThread().getStackTrace()[1].getMethodName()
//                                , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//                    }
//                }
//            }
//
//            result = true;
//        }
//
//        return result;
//    }
//
//    //
//
//    /**
//     * 删除指定的 [（安全认证）角色] 的所有[（安全认证）角色 ←→ 资源]关联关系
//     *
//     * @param role     [（安全认证）角色], 必须合法且已持久化.  {@link SecurityRole}
//     * @param operator 操作者
//     * @return 操作是否成功
//     * @Description 完整的业务流程.
//     */
//    @Override
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    public boolean deleteRoleResourceRelationshipOnRole(@NotNull SecurityRole role, @NotNull SecurityUser operator)
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
//        final @NotNull UserAccountOperationInfo operatorOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
//        if (null == operatorOperationInfo || operatorOperationInfo.isEmpty()) {
//            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
//            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- 【%s】</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , "无[有效的账户操作基础记录]"
//                    , operator
//                    , operatorOperationInfo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        final @NotNull List<SecurityRoleResource> existedRoleResources = securityRoleResourceService.selectByRoleCode(role.getCode());
//        if (null == existedRoleResources || existedRoleResources.isEmpty()) {
//            return true;
//        } else {
//            for (@NotNull SecurityRoleResource eachRoleResource : existedRoleResources) {
//                if (!securityRoleResourceService.delete(eachRoleResource, operator, operatorOperationInfo)) {
//                    throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                            , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__DELETION.name
//                            , "操作失败"
//                            , eachRoleResource
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
//     * 删除指定的 [（安全认证）资源] 的所有[（安全认证）角色 ←→ 资源]关联关系
//     *
//     * @param resource [（安全认证）资源], 必须合法且已持久化.  {@link SecurityResource}
//     * @param operator 操作者
//     * @return 操作是否成功
//     * @Description 完整的业务流程.
//     */
//    @Override
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    public boolean deleteRoleResourceRelationshipOnResource(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException {
//        if (null == resource || resource.isEmpty()) {
//            //-- 非法输入: [（安全认证）资源]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[（安全认证）资源]"
//                    , resource
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
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        final @NotNull List<SecurityRoleResource> existedRoleResources = securityRoleResourceService.selectByResourceCode(resource.getCode());
//        if (null == existedRoleResources || existedRoleResources.isEmpty()) {
//            return true;
//        } else {
//            for (@NotNull SecurityRoleResource eachRoleResource : existedRoleResources) {
//                if (!securityRoleResourceService.delete(eachRoleResource, operator, operator_userAccountOperationInfo)) {
//                    throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                            , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__DELETION.name
//                            , "操作失败"
//                            , resource
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
