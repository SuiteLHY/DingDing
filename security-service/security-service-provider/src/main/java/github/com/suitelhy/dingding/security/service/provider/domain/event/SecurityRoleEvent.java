package github.com.suitelhy.dingding.security.service.provider.domain.event;

import github.com.suitelhy.dingding.core.infrastructure.domain.Page;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.AggregateEvent;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.*;
import github.com.suitelhy.dingding.security.service.api.domain.vo.Security;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.security.service.provider.domain.event.impl.SecurityRoleEventImpl;
import github.com.suitelhy.dingding.security.service.provider.domain.service.SecurityResourceService;
import github.com.suitelhy.dingding.security.service.provider.domain.service.SecurityRoleResourceService;
import github.com.suitelhy.dingding.security.service.provider.domain.service.SecurityRoleService;
import github.com.suitelhy.dingding.security.service.provider.domain.service.SecurityUserRoleService;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * （安全认证）角色 - 复杂业务接口
 *
 * @see SecurityRole
 * @see SecurityResource
 * @see SecurityUser
 * @see SecurityRoleResource
 * @see SecurityUserRole
 * @see SecurityRoleEventImpl
 * @see SecurityRoleService
 * @see SecurityResourceService
 * @see SecurityUserRoleService
 * @see SecurityRoleResourceService
 */
@Transactional(isolation = Isolation.SERIALIZABLE
        , propagation = Propagation.REQUIRED
        , readOnly = false
        , rollbackFor = Exception.class
        , timeout = 15)
public interface SecurityRoleEvent
        extends AggregateEvent {

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
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    boolean existRoleOnUserByUsername(@NotNull String username)
            throws IllegalArgumentException;

    /**
     * 判断[（安全认证）资源]是否存在 (关联的) [（安全认证）角色]
     *
     * @Description 完整的业务流程.
     *
     * @param resourceCode  {@linkplain SecurityRoleResource.Validator#resourceCode(String) 资源编码}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    boolean existRoleOnResourceByResourceCode(@NotNull String resourceCode)
            throws IllegalArgumentException;

    /**
     * 判断[（安全认证）角色]是否存在 (关联的) [（安全认证）资源]
     *
     * @Description 完整的业务流程.
     *
     * @param roleCode  {@linkplain SecurityRoleResource.Validator#roleCode(String) 角色编码}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    boolean existResourceOnRoleByRoleCode(@NotNull String roleCode)
            throws IllegalArgumentException;

    /**
     * 查询 (指定角色关联的) 资源
     *
     * @Description 完整的业务流程.
     *
     * @param roleCode  {@linkplain SecurityRole.Validator#code(String) 角色编码}
     *
     * @return {@linkplain SecurityResource (指定角色关联的) 资源}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    @NotNull List<SecurityResource> selectResourceOnRoleByRoleCode(@NotNull String roleCode)
            throws IllegalArgumentException;

    /**
     * 查询 (指定资源关联的) 角色
     *
     * @Description 完整的业务流程.
     *
     * @param resourceCode  {@linkplain SecurityResource#getCode() 资源编码}
     *
     * @return {@linkplain SecurityRole (指定资源关联的) 角色}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    @NotNull List<SecurityRole> selectRoleOnResourceByResourceCode(@NotNull String resourceCode)
            throws IllegalArgumentException;

    /**
     * 查询 (关联的) [（安全认证）角色]
     *
     * @Description 完整的业务流程.
     *
     * @param username  {@linkplain SecurityUser.Validator#username(String) 用户名称}
     *
     * @return {@linkplain SecurityRole (关联的) [（安全认证）角色]}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    @NotNull List<SecurityRole> selectRoleOnUserByUsername(@NotNull String username)
            throws IllegalArgumentException;

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
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    @NotNull Page<SecurityRole> selectPageRoleOnUserByUsername(@NotNull String username, int pageIndex, int pageSize)
            throws IllegalArgumentException;

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
    boolean insertUserRoleRelationship(@NotNull SecurityUserRole userRole, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 新增一个[（安全认证）用户 ←→ 角色]关联关系
     *
     * @Description 完整的业务流程.
     *
     * @param user      {@linkplain SecurityUser [（安全认证）用户]}
     * @param role      {@linkplain SecurityRole [（安全认证）角色]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在相同的有效数据}
     */
    default boolean insertUserRoleRelationship(@NotNull SecurityUser user, @NotNull SecurityRole role, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == user || user.isEmpty()) {
            //-- 非法输入: （安全认证）用户
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "（安全认证）用户"
                    , user
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == role || role.isEmpty()) {
            //-- 非法输入: （安全认证）角色
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "（安全认证）角色"
                    , role
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        Set<SecurityRole> roles = new HashSet<>(1);
        roles.add(role);

        return insertUserRoleRelationship(user, roles, operator);
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
    boolean insertUserRoleRelationship(@NotNull SecurityUser user, @NotNull Set<SecurityRole> roles, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

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
    boolean insertUserRoleRelationship(@NotNull SecurityUser user, @NotNull Security.RoleVo roleVo, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 新增[角色 - 资源]关联
     *
     * @Description 完整的业务流程.
     *
     * @param role      {@linkplain SecurityRole [（安全认证）角色]}, 必须合法且已持久化.
     * @param resource  {@linkplain SecurityResource [（安全认证）资源]}, 必须合法且已持久化.
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    default boolean insertRoleResourceRelationship(@NotNull SecurityRole role, @NotNull SecurityResource resource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
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

        return insertRoleResourceRelationship(roles, resources, operator);
    }

    /**
     * 新增[角色 - 资源]关联
     *
     * @Description 完整的业务流程.
     *
     * @param role      {@linkplain SecurityRole [（安全认证）角色]}, 必须合法且已持久化.
     * @param resources {@linkplain SecurityResource [（安全认证）资源]}, 必须全部合法且已持久化.
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    default boolean insertRoleResourceRelationship(@NotNull SecurityRole role, @NotNull Set<SecurityResource> resources, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
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

        return insertRoleResourceRelationship(roles, resources, operator);
    }

    /**
     * 新增[角色 - 资源]关联
     *
     * @Description 完整的业务流程.
     *
     * @param roles     {@linkplain SecurityRole [（安全认证）角色]}, 必须全部合法且已持久化.
     * @param resource  {@linkplain SecurityResource [（安全认证）资源]}, 必须合法且已持久化.
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    default boolean insertRoleResourceRelationship(@NotNull Set<SecurityRole> roles, @NotNull SecurityResource resource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
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

        return insertRoleResourceRelationship(roles, resources, operator);
    }

    /**
     * 新增[角色 - 资源]关联
     *
     * @Description 完整的业务流程.
     *
     * @param roles     {@linkplain SecurityRole [（安全认证）角色]}, 必须全部合法且已持久化.
     * @param resources {@linkplain SecurityResource [（安全认证）资源]}, 必须全部合法且已持久化.
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    boolean insertRoleResourceRelationship(@NotNull Set<SecurityRole> roles, @NotNull Set<SecurityResource> resources, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

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
    boolean updateRole(@NotNull SecurityRole role, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

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
    boolean updateRole(@NotNull SecurityRole old_role
            , @NotNull Map<String, Object> new_role_data
            , @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

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
    boolean deleteRole(@NotNull SecurityRole role, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

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
    boolean deleteUserRoleRelationship(@NotNull SecurityUser user, @NotNull SecurityRole role, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

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
    boolean deleteUserRoleRelationshipOnUser(@NotNull SecurityUser user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

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
    boolean deleteUserRoleRelationshipOnRole(@NotNull SecurityRole role, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 删除[角色 - 资源]关联
     *
     * @Description 完整的业务流程.
     *
     * @param role      {@linkplain SecurityRole [（安全认证）角色]}, 必须合法且已持久化.
     * @param resource  {@linkplain SecurityResource [（安全认证）资源]}, 必须合法且已持久化.
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    default boolean deleteRoleResourceRelationship(@NotNull SecurityRole role, @NotNull SecurityResource resource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
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
     * @Description 完整的业务流程.
     *
     * @param role      {@linkplain SecurityRole [（安全认证）角色]}, 必须合法且已持久化.
     * @param resources {@linkplain SecurityResource [（安全认证）资源]}, 必须全部合法且已持久化.
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    default boolean deleteRoleResourceRelationship(@NotNull SecurityRole role, @NotNull Set<SecurityResource> resources, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
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
     * @Description 完整的业务流程.
     *
     * @param roles     {@linkplain SecurityRole [（安全认证）角色]}, 必须全部合法且已持久化.
     * @param resource  {@linkplain SecurityResource [（安全认证）资源]}, 必须合法且已持久化.
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    default boolean deleteRoleResourceRelationship(@NotNull Set<SecurityRole> roles, @NotNull SecurityResource resource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
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
     * @Description 完整的业务流程.
     *
     * @param roles     {@linkplain SecurityRole [（安全认证）角色]}, 必须全部合法且已持久化.
     * @param resources {@linkplain SecurityResource [（安全认证）资源]}, 必须全部合法且已持久化.
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    boolean deleteRoleResourceRelationship(@NotNull Set<SecurityRole> roles, @NotNull Set<SecurityResource> resources, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

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
    boolean deleteRoleResourceRelationshipOnRole(@NotNull SecurityRole role, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

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
    boolean deleteRoleResourceRelationshipOnResource(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

}
