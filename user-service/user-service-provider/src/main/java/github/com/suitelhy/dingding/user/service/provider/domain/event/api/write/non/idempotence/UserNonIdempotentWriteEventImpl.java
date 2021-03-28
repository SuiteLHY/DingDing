package github.com.suitelhy.dingding.user.service.provider.domain.event.api.write.non.idempotence;

import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.security.service.api.domain.entity.User;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserPersonInfo;
import github.com.suitelhy.dingding.user.service.api.domain.event.write.non.idempotence.UserNonIdempotentWriteEvent;
import github.com.suitelhy.dingding.user.service.provider.domain.event.UserEvent;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;

/**
 * 用户 - 复杂业务接口
 *
 * @Design
 * · 写入操作
 * · 非幂等性
 *
 * @see User
 * @see UserAccountOperationInfo
 * @see UserPersonInfo
 * @see github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo.Strategy.ClusterVo#FAIL_FAST
 */
@Service(cluster = "failfast")
public class UserNonIdempotentWriteEventImpl
        implements UserNonIdempotentWriteEvent {

    @Autowired
    private UserEvent userEvent;

    /**
     * 新增(=注册)一个用户
     *
     * @Description 完整的业务流程.
     *
     * @param user                     {@linkplain User 预期新增的用户 -> 用户基础信息}
     * @param userAccountOperationInfo {@linkplain UserAccountOperationInfo 预期新增的用户 -> [用户 -> 账户操作基础记录]}
     * @param userPersonInfo           {@linkplain UserPersonInfo [用户 -> 个人信息]}
     * @param operator                 {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     *
     * @throws IllegalArgumentException
     * @throws BusinessAtomicException
     */
    @Override
    public boolean registerUser(@NotNull User user, @NotNull UserAccountOperationInfo userAccountOperationInfo, @NotNull UserPersonInfo userPersonInfo, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return userEvent.registerUser(user, userAccountOperationInfo, userPersonInfo, operator);
    }

    /**
     * 删除指定的用户
     *
     * @Description 完整的业务流程.
     *
     * @param user     {@linkplain User 被修改的用户 -> 基础信息}
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean deleteUser(@NotNull User user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return userEvent.deleteUser(user, operator);
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
        return userEvent.deleteUserRoleRelationship(user, role, operator);
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
        return userEvent.deleteUserRoleRelationshipOnUser(user, operator);
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
        return userEvent.deleteUserRoleRelationshipOnRole(role, operator);
    }

    /**
     * 销毁指定的用户
     *
     * @Description 完整的业务流程.
     *
     * @Design 数据保留, 正常业务功能不可用.
     *
     * @param user     {@linkplain User 被修改的用户 -> 基础信息}
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean destroyUser(@NotNull User user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return userEvent.destroyUser(user, operator);
    }

}
