package github.com.suitelhy.dingding.security.service.provider.domain.event.api.write.idempotence.impl;

import github.com.suitelhy.dingding.security.service.api.domain.entity.security.*;
import github.com.suitelhy.dingding.security.service.api.domain.event.write.idempotence.SecurityRoleIdempotentEvent;
import github.com.suitelhy.dingding.security.service.api.domain.vo.Security;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.security.service.provider.domain.event.SecurityRoleEvent;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * （安全认证）角色 - 复杂业务接口
 *
 * @Design
 * · 写入操作
 * · 幂等性
 *
 * @see SecurityRole
 * @see SecurityResource
 * @see SecurityUser
 * @see SecurityRoleResource
 * @see SecurityUserRole
 * @see github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo.Strategy.ClusterVo#FORKING
 */
@Service(cluster = "forking")
public class SecurityRoleIdempotentEventImpl
        implements SecurityRoleIdempotentEvent {

    @Autowired
    private SecurityRoleEvent securityRoleEvent;

    /**
     * 新增一个[（安全认证）用户 ←→ 角色]关联关系
     *
     * @Description 完整的业务流程.
     *
     * @param userRole {@linkplain SecurityUserRole [（安全认证）用户 ←→ 角色]}
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在相同的有效数据}
     */
    @Override
    public boolean insertUserRoleRelationship(@NotNull SecurityUserRole userRole, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityRoleEvent.insertUserRoleRelationship(userRole, operator);
    }

    /**
     * 新增多个[（安全认证）用户 ←→ 角色]关联关系
     *
     * @Description 完整的业务流程.
     *
     * @param user     {@linkplain SecurityUser [（安全认证）用户]}
     * @param roles    {@linkplain SecurityRole [（安全认证）角色]}
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在完全相同的有效数据集合}
     */
    @Override
    public boolean insertUserRoleRelationship(@NotNull SecurityUser user, @NotNull Set<SecurityRole> roles, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityRoleEvent.insertUserRoleRelationship(user, roles, operator);
    }

    /**
     * 新增一个[（安全认证）用户 ←→ 角色]关联关系
     *
     * @Description 完整的业务流程.
     *
     * @param user     {@linkplain SecurityUser [（安全认证）用户]}
     * @param roleVo   {@linkplain Security.RoleVo [(安全) 用户 -> 角色]}, 仅需保证合法性, 不需要保证持久化.
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在相同的有效数据}
     */
    @Override
    public boolean insertUserRoleRelationship(@NotNull SecurityUser user, Security.@NotNull RoleVo roleVo, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityRoleEvent.insertUserRoleRelationship(user, roleVo, operator);
    }

    /**
     * 更新指定的角色
     *
     * @Description 全量更新.
     * · 完整的业务流程.
     *
     * @param role     {@linkplain SecurityRole [（安全认证）角色]}
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean updateRole(@NotNull SecurityRole role, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityRoleEvent.updateRole(role, operator);
    }

}
