package github.com.suitelhy.dingding.core.domain.event.security;

import github.com.suitelhy.dingding.core.domain.entity.security.*;
import github.com.suitelhy.dingding.core.domain.event.security.impl.SecurityResourceEventImpl;
import github.com.suitelhy.dingding.core.domain.service.LogService;
import github.com.suitelhy.dingding.core.domain.service.UserAccountOperationInfoService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityResourceService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityResourceUrlService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityRoleResourceService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityRoleService;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.AggregateEvent;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * (安全) 资源 - 复杂业务接口
 *
 * @see SecurityResource
 * @see SecurityRole
 * @see SecurityResourceUrl
 * @see SecurityRoleResource
 * @see SecurityResourceEventImpl
 * @see SecurityResourceService
 * @see SecurityRoleService
 * @see SecurityResourceUrlService
 * @see SecurityRoleResourceService
 * @see UserAccountOperationInfoService
 * @see LogService
 */
@Transactional(isolation = Isolation.READ_COMMITTED
        , propagation = Propagation.REQUIRED
        , readOnly = true
        , rollbackFor = Exception.class
        , timeout = 15)
public interface SecurityResourceEvent
        extends AggregateEvent {

    //===== 查询操作业务 =====//

    /**
     * 判断[（安全认证）角色]是否存在 (关联的) [（安全认证）资源]
     *
     * @param roleCode 角色编码    {@link SecurityRoleResource.Validator#roleCode(String)}
     * @return {@link Boolean#TYPE}
     * @Description 完整的业务流程.
     */
    boolean existResourceOnRoleByRoleCode(@NotNull String roleCode)
            throws IllegalArgumentException;

    /**
     * 判断[（安全认证）角色]是否存在 (关联的) [（安全认证）资源]
     *
     * @param urlInfo URL相关信息.    {@link SecurityResourceUrl.Validator#urlInfo(String[])}
     * @return {@link Boolean#TYPE}
     * @Description 完整的业务流程.
     */
    boolean existResourceOnUrlInfo(@NotNull String[] urlInfo)
            throws IllegalArgumentException;

    /**
     * 判断[（安全认证）资源]是否存在 (关联的) [（安全认证）角色]
     *
     * @param resourceCode 资源编码    {@link SecurityRoleResource.Validator#resourceCode(String)}
     * @return {@link boolean}
     * @Description 完整的业务流程.
     */
    boolean existRoleOnResourceByResourceCode(@NotNull String resourceCode)
            throws IllegalArgumentException;

    /**
     * 判断[（安全认证）资源]是否存在 (关联的) [URL 相关信息]
     *
     * @param resourceCode 资源编码    {@link SecurityResource.Validator#code(String)}
     * @return {@link Boolean#TYPE}
     * @Description 完整的业务流程.
     */
    boolean existUrlInfoOnResourceByResourceCode(@NotNull String resourceCode)
            throws IllegalArgumentException;

    /**
     * 查询 (指定角色关联的) 资源
     *
     * @param roleCode 角色编码    {@link SecurityRole.Validator#code(String)}
     * @return {@link SecurityResource}
     * @Description 完整的业务流程.
     */
    @NotNull
    List<SecurityResource> selectResourceOnRoleByRoleCode(@NotNull String roleCode)
            throws IllegalArgumentException;

    /**
     * 查询[(关联的) 角色 -> (关联的) 资源]
     *
     * @param username 用户名称    {@link SecurityUser.Validator#username(String)}
     * @return 资源集合 {@link SecurityResource}
     * @Description 完整的业务流程.
     * @see github.com.suitelhy.dingding.core.domain.event.UserEvent#selectResourceOnUserByUsername(String)
     */
    @NotNull
    List<SecurityResource> selectResourceOnUserByUsername(@NotNull String username)
            throws IllegalArgumentException;

    /**
     * 查询 (指定资源关联的) 角色
     *
     * @param resourceCode 资源编码    {@link SecurityResource#getCode()}
     * @return {@link SecurityRole}
     * @Description 完整的业务流程.
     */
    @NotNull
    List<SecurityRole> selectRoleOnResourceByResourceCode(@NotNull String resourceCode)
            throws IllegalArgumentException;

    /**
     * 查询[URL 相关信息]
     *
     * @param resourceCode 资源编码    {@link SecurityResourceUrl.Validator#code(String)}
     * @return {@link SecurityResourceUrl}
     * @Description 完整的业务流程.
     * @see github.com.suitelhy.dingding.core.domain.event.security.SecurityUrlEvent#selectUrlInfoOnResourceByResourceCode(String)
     */
    @NotNull
    List<SecurityResourceUrl> selectUrlInfoOnResourceByResourceCode(@NotNull String resourceCode)
            throws IllegalArgumentException;

    //===== 添加操作业务 =====//

    /**
     * 新增一个[（安全认证）资源]
     *
     * @param resource [（安全认证）资源]  {@link SecurityResource}
     * @param operator 操作者
     * @return 操作是否成功 / 是否已存在相同的有效数据
     * @Description 完整的业务流程.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean insertResource(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 新增一个[（安全认证）资源 ←→ URL]关联关系
     *
     * @param resourceUrl [（安全认证）资源 ←→ URL]   {@link SecurityResourceUrl}
     * @param operator    操作者
     * @return 操作是否成功 / 是否已存在相同的有效数据
     * @Description 完整的业务流程.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean insertResourceUrlRelationship(@NotNull SecurityResourceUrl resourceUrl, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 新增一个[（安全认证）资源 ←→ URL]关联关系
     *
     * @param resource [（安全认证）资源], 必须合法且已持久化.   {@link SecurityResource}
     * @param urlInfo  [URL 相关信息]                       {@link SecurityResourceUrl.Validator#urlInfo(String[])}
     * @param operator 操作者
     * @return 操作是否成功
     * @Description 完整的业务流程.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean insertResourceUrlRelationship(@NotNull SecurityResource resource
            , @NotNull String[] urlInfo
            , @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 新增[角色 - 资源]关联
     *
     * @param role     [（安全认证）角色], 必须合法且已持久化.  {@link SecurityRole}
     * @param resource [（安全认证）资源], 必须合法且已持久化.  {@link SecurityResource}
     * @param operator 操作者
     * @return 操作是否成功
     * @Description 完整的业务流程.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    default boolean insertRoleResourceRelationship(@NotNull SecurityRole role, @NotNull SecurityResource resource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException {
        if (null == role || role.isEmpty()) {
            //-- 非法输入: 角色编码
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）角色]"
                    , role
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == resource || resource.isEmpty()) {
            //-- 非法输入: [（安全认证）资源]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）资源]"
                    , resource
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        /*System.out.println("===== insertResource(SecurityRole, SecurityResource) =====");*/

        final @NotNull Set<SecurityRole> roles = new HashSet<>(1);
        roles.add(role);

        final @NotNull Set<SecurityResource> resources = new HashSet<>(1);
        resources.add(resource);

        return insertRoleResourceRelationship(roles, resources, operator);
    }

    /**
     * 新增[角色 - 资源]关联
     *
     * @param role      [（安全认证）角色], 必须合法且已持久化.       {@link SecurityRole}
     * @param resources [（安全认证）资源], 必须全部合法且已持久化.    {@link SecurityResource}
     * @param operator  操作者
     * @return 操作是否成功
     * @Description 完整的业务流程.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    default boolean insertRoleResourceRelationship(@NotNull SecurityRole role, @NotNull Set<SecurityResource> resources, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException {
        if (null == role || role.isEmpty()) {
            //-- 非法输入: 角色编码
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）角色]"
                    , role
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == resources || resources.isEmpty()) {
            //-- 非法输入: [（安全认证）资源]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）资源]"
                    , resources
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        /*System.out.println("===== insertResource(SecurityRole, Set<SecurityResource>) =====");*/

        final @NotNull Set<SecurityRole> roles = new HashSet<>(1);
        roles.add(role);

        return insertRoleResourceRelationship(roles, resources, operator);
    }

    /**
     * 新增[角色 - 资源]关联
     *
     * @param roles    [（安全认证）角色], 必须全部合法且已持久化.   {@link SecurityRole}
     * @param resource [（安全认证）资源], 必须合法且已持久化.      {@link SecurityResource}
     * @param operator 操作者
     * @return 操作是否成功
     * @Description 完整的业务流程.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    default boolean insertRoleResourceRelationship(@NotNull Set<SecurityRole> roles, @NotNull SecurityResource resource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException {
        if (null == roles || roles.isEmpty()) {
            //-- 非法输入: 角色编码
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）角色]"
                    , roles
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == resource || resource.isEmpty()) {
            //-- 非法输入: [（安全认证）资源]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）资源]"
                    , resource
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        /*System.out.println("===== insertResource(Set<SecurityRole>, SecurityResource) =====");*/

        final @NotNull Set<SecurityResource> resources = new HashSet<>(1);
        resources.add(resource);

        return insertRoleResourceRelationship(roles, resources, operator);
    }

    /**
     * 新增[角色 - 资源]关联
     *
     * @param roles     [（安全认证）角色], 必须全部合法且已持久化.    {@link SecurityRole}
     * @param resources [（安全认证）资源], 必须全部合法且已持久化.    {@link SecurityResource}
     * @param operator  操作者
     * @return 操作是否成功
     * @Description 完整的业务流程.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean insertRoleResourceRelationship(@NotNull Set<SecurityRole> roles, @NotNull Set<SecurityResource> resources, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    //===== 修改操作业务 =====//

    /**
     * 更新指定的资源
     *
     * @param resource [（安全认证）资源]  {@link SecurityResource}
     * @param operator 操作者
     * @return 操作是否成功
     * @Description 全量更新.
     * · 完整的业务流程.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean updateResource(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    //===== 删除操作业务 =====//

    /**
     * 删除指定的资源
     *
     * @param resource [（安全认证）资源]
     * @param operator 操作者
     * @return 操作是否成功
     * @Description · 完整的业务流程.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean deleteResource(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 删除指定的[（安全认证）资源 ←→ URL]关联关系
     *
     * @param resourceUrl [（安全认证）资源 ←→ URL]   {@link SecurityResourceUrl}
     * @param operator    操作者
     * @return 操作是否成功
     * @Description · 完整的业务流程.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean deleteResourceUrlRelationship(@NotNull SecurityResourceUrl resourceUrl, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 删除指定的[（安全认证）资源 ←→ URL]关联关系
     *
     * @param resource [（安全认证）资源], 必须合法且已持久化.  {@link SecurityResource}
     * @param urlInfo  [URL 相关信息]                      {@link SecurityResourceUrl.Validator#urlInfo(String[])}
     * @param operator 操作者
     * @return 操作是否成功
     * @Description · 完整的业务流程.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean deleteResourceUrlRelationship(@NotNull SecurityResource resource
            , @NotNull String[] urlInfo
            , @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 删除指定的[（安全认证）资源]关联的所有[（安全认证）资源 ←→ URL]关联关系
     *
     * @param resource [（安全认证）资源]  {@link SecurityResource}
     * @param operator 操作者
     * @return 操作是否成功
     * @Description · 完整的业务流程.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean deleteResourceUrlRelationshipOnResource(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 删除指定的[URL 信息]关联的所有[（安全认证）资源 ←→ URL]关联关系
     *
     * @param urlInfo  [URL 信息]    {@link SecurityResourceUrl.Validator#urlInfo(String[])}
     * @param operator 操作者
     * @return 操作是否成功
     * @Description · 完整的业务流程.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean deleteResourceUrlRelationshipOnUrlInfo(@NotNull String[] urlInfo, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 删除[角色 - 资源]关联
     *
     * @param role     [（安全认证）角色], 必须合法且已持久化.  {@link SecurityRole}
     * @param resource [（安全认证）资源], 必须合法且已持久化.  {@link SecurityResource}
     * @param operator 操作者
     * @return 操作是否成功
     * @Description 完整的业务流程.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    default boolean deleteRoleResourceRelationship(@NotNull SecurityRole role, @NotNull SecurityResource resource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException {
        if (null == role || role.isEmpty()) {
            //-- 非法输入: 角色编码
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）角色]"
                    , role
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == resource || resource.isEmpty()) {
            //-- 非法输入: [（安全认证）资源]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）资源]"
                    , resource
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        @NotNull Set<SecurityRole> roles = new HashSet<>(1);
        roles.add(role);

        @NotNull Set<SecurityResource> resources = new HashSet<>(1);
        resources.add(resource);

        return deleteRoleResourceRelationship(roles, resources, operator);
    }

    /**
     * 删除[角色 - 资源]关联
     *
     * @param role      [（安全认证）角色], 必须合法且已持久化.       {@link SecurityRole}
     * @param resources [（安全认证）资源], 必须全部合法且已持久化.    {@link SecurityResource}
     * @param operator  操作者
     * @return 操作是否成功
     * @Description 完整的业务流程.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    default boolean deleteRoleResourceRelationship(@NotNull SecurityRole role, @NotNull Set<SecurityResource> resources, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException {
        if (null == role || role.isEmpty()) {
            //-- 非法输入: 角色编码
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）角色]"
                    , role
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == resources || resources.isEmpty()) {
            //-- 非法输入: [（安全认证）资源]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）资源]"
                    , resources
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        @NotNull Set<SecurityRole> roles = new HashSet<>(1);
        roles.add(role);

        return deleteRoleResourceRelationship(roles, resources, operator);
    }

    /**
     * 删除[角色 - 资源]关联
     *
     * @param roles    [（安全认证）角色], 必须全部合法且已持久化.   {@link SecurityRole}
     * @param resource [（安全认证）资源], 必须合法且已持久化.      {@link SecurityResource}
     * @param operator 操作者
     * @return 操作是否成功
     * @Description 完整的业务流程.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    default boolean deleteRoleResourceRelationship(@NotNull Set<SecurityRole> roles, @NotNull SecurityResource resource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException {
        if (null == roles || roles.isEmpty()) {
            //-- 非法输入: 角色编码
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）角色]"
                    , roles
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == resource || resource.isEmpty()) {
            //-- 非法输入: [（安全认证）资源]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）资源]"
                    , resource
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        @NotNull Set<SecurityResource> resources = new HashSet<>(1);
        resources.add(resource);

        return deleteRoleResourceRelationship(roles, resources, operator);
    }

    /**
     * 删除[角色 - 资源]关联
     *
     * @param roles     [（安全认证）角色], 必须全部合法且已持久化.    {@link SecurityRole}
     * @param resources [（安全认证）资源], 必须全部合法且已持久化.    {@link SecurityResource}
     * @param operator  操作者
     * @return 操作是否成功
     * @Description 完整的业务流程.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean deleteRoleResourceRelationship(@NotNull Set<SecurityRole> roles, @NotNull Set<SecurityResource> resources, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 删除指定的[（安全认证）角色]关联的所有[（安全认证）角色 ←→ 资源]关联关系
     *
     * @param role     [（安全认证）角色], 必须合法且已持久化.  {@link SecurityRole}
     * @param operator 操作者
     * @return 操作是否成功
     * @Description 完整的业务流程.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean deleteRoleResourceRelationshipOnRole(@NotNull SecurityRole role, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 删除指定的[（安全认证）资源]关联的所有[（安全认证）角色 ←→ 资源]关联关系
     *
     * @param resource [（安全认证）资源], 必须合法且已持久化.  {@link SecurityResource}
     * @param operator 操作者
     * @return 操作是否成功
     * @Description 完整的业务流程.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean deleteRoleResourceRelationshipOnResource(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

}
