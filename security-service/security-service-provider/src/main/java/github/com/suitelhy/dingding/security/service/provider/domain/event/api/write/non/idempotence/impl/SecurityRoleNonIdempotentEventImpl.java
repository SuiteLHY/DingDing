package github.com.suitelhy.dingding.security.service.provider.domain.event.api.write.non.idempotence.impl;

import github.com.suitelhy.dingding.security.service.api.domain.entity.security.*;
import github.com.suitelhy.dingding.security.service.api.domain.event.write.non.idempotence.SecurityRoleNonIdempotentEvent;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.security.service.provider.domain.event.SecurityRoleEvent;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;

/**
 * （安全认证）角色 - 复杂业务接口
 *
 * @Design
 * · 写入操作
 * · 非幂等性
 *
 * @see SecurityRole
 * @see SecurityResource
 * @see SecurityUser
 * @see SecurityRoleResource
 * @see SecurityUserRole
 * @see github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo.Strategy.ClusterVo#FAIL_FAST
 */
@Service(cluster = "failfast")
public class SecurityRoleNonIdempotentEventImpl
        implements SecurityRoleNonIdempotentEvent {

    @Autowired
    private SecurityRoleEvent securityRoleEvent;

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
    @Override
    public boolean insertRoleResourceRelationship(@NotNull Set<SecurityRole> roles, @NotNull Set<SecurityResource> resources, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityRoleEvent.insertRoleResourceRelationship(roles, resources, operator);
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
    public boolean updateRole(@NotNull SecurityRole old_role, @NotNull Map<String, Object> new_role_data, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityRoleEvent.updateRole(old_role, new_role_data, operator);
    }

    /**
     * 删除指定的角色
     *
     * @Description 完整的业务流程.
     *
     * @param role     {@linkplain SecurityRole [（安全认证）角色]}
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean deleteRole(@NotNull SecurityRole role, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityRoleEvent.deleteRole(role, operator);
    }

    /**
     * 删除[（安全认证）用户 ←→ 角色]关联关系
     *
     * @Description 完整的业务流程.
     *
     * @param user     {@linkplain SecurityUser [（安全认证）用户]}, 必须合法且已持久化.
     * @param role     {@linkplain SecurityRole [（安全认证）角色]}, 必须合法且已持久化.
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean deleteUserRoleRelationship(@NotNull SecurityUser user, @NotNull SecurityRole role, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityRoleEvent.deleteUserRoleRelationship(user, role, operator);
    }

    /**
     * 删除指定的[（安全认证）用户]关联的所有[（安全认证）用户 ←→ 角色]关联关系
     *
     * @Description 完整的业务流程.
     *
     * @param user     {@linkplain SecurityUser [（安全认证）用户]}, 必须合法且已持久化.
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean deleteUserRoleRelationshipOnUser(@NotNull SecurityUser user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityRoleEvent.deleteUserRoleRelationshipOnUser(user, operator);
    }

    /**
     * 删除指定的[（安全认证）角色]关联的所有[（安全认证）用户 ←→ 角色]关联关系
     *
     * @Description 完整的业务流程.
     *
     * @param role     {@linkplain SecurityRole [（安全认证）角色]}, 必须合法且已持久化.
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean deleteUserRoleRelationshipOnRole(@NotNull SecurityRole role, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityRoleEvent.deleteUserRoleRelationshipOnRole(role, operator);
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
    public boolean deleteRoleResourceRelationship(@NotNull Set<SecurityRole> roles, @NotNull Set<SecurityResource> resources, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityRoleEvent.deleteRoleResourceRelationship(roles, resources, operator);
    }

    /**
     * 删除指定的[（安全认证）角色]关联的所有[（安全认证）角色 ←→ 资源]关联关系
     *
     * @Description 完整的业务流程.
     *
     * @param role     {@linkplain SecurityRole [（安全认证）角色]}, 必须合法且已持久化.
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean deleteRoleResourceRelationshipOnRole(@NotNull SecurityRole role, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityRoleEvent.deleteRoleResourceRelationshipOnRole(role, operator);
    }

    /**
     * 删除指定的[（安全认证）资源]关联的所有[（安全认证）角色 ←→ 资源]关联关系
     *
     * @Description 完整的业务流程.
     *
     * @param resource {@linkplain SecurityResource [（安全认证）资源]}, 必须合法且已持久化.
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean deleteRoleResourceRelationshipOnResource(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityRoleEvent.deleteRoleResourceRelationshipOnResource(resource, operator);
    }

}
