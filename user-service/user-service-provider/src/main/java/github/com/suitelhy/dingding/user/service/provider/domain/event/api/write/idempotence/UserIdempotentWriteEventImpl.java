package github.com.suitelhy.dingding.user.service.provider.domain.event.api.write.idempotence;

import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.security.service.api.domain.entity.User;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUserRole;
import github.com.suitelhy.dingding.security.service.api.domain.vo.Security;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserPersonInfo;
import github.com.suitelhy.dingding.user.service.api.domain.event.write.idempotence.UserIdempotentWriteEvent;
import github.com.suitelhy.dingding.user.service.provider.domain.event.UserEvent;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * 用户 - 复杂业务接口
 *
 * @Design
 * · 写入操作
 * · 幂等性
 *
 * @see User
 * @see UserAccountOperationInfo
 * @see UserPersonInfo
 * @see github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo.Strategy.ClusterVo#FORKING
 */
@Service(cluster = "forking")
public class UserIdempotentWriteEventImpl
        implements UserIdempotentWriteEvent {

    @Autowired
    private UserEvent userEvent;

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
        return userEvent.insertUserRoleRelationship(userRole, operator);
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
        return userEvent.insertUserRoleRelationship(user, roles, operator);
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
        return userEvent.insertUserRoleRelationship(user, roleVo, operator);
    }

    /**
     * 更新指定的用户 -> 基础信息
     *
     * @Description 完整的业务流程.
     *
     * @param user     {@linkplain User 被修改的用户 -> 基础信息}
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean updateUser(@NotNull User user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return userEvent.updateUser(user, operator);
    }

    /**
     * 更新指定的用户 -> 账户操作基础记录
     *
     * @Description 完整的业务流程.
     *
     * @param userAccountOperationInfo {@linkplain UserAccountOperationInfo 被修改的用户 -> 账户操作基础记录}
     * @param operator                 {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean updateUserAccountOperationInfo(@NotNull UserAccountOperationInfo userAccountOperationInfo, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return userEvent.updateUserAccountOperationInfo(userAccountOperationInfo, operator);
    }

    /**
     * 更新指定的用户 -> 个人信息
     *
     * @Description 完整的业务流程.
     *
     * @param userPersonInfo {@linkplain UserPersonInfo 被修改的用户 -> 个人信息}
     * @param operator       {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean updateUserPersonInfo(@NotNull UserPersonInfo userPersonInfo, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return userEvent.updateUserPersonInfo(userPersonInfo, operator);
    }

}
