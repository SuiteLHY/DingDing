package github.com.suitelhy.dingding.user.service.provider.domain.event.api.read;

import github.com.suitelhy.dingding.security.service.api.domain.entity.User;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityResource;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityResourceUrl;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserPersonInfo;
import github.com.suitelhy.dingding.user.service.api.domain.event.read.UserReadEvent;
import github.com.suitelhy.dingding.user.service.provider.domain.event.UserEvent;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户 - 复杂业务接口
 *
 * @Design
 * · 只读操作
 * · 幂等性
 *
 * @see User
 * @see UserAccountOperationInfo
 * @see UserPersonInfo
 * @see github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo.Strategy.ClusterVo#FAILOVER
 */
@Service(cluster = "failover")
public class UserReadEventImpl
        implements UserReadEvent {

    @Autowired
    private UserEvent userEvent;

    /**
     * 查询指定的用户 -> 基础信息
     *
     * @Description 完整的业务流程.
     *
     * @param username {@linkplain User.Validator#username(String) 用户名称}
     *
     * @return {@linkplain User 指定的用户 -> 基础信息}
     */
    @Override
    public @NotNull User selectUserByUsername(@NotNull String username)
            throws IllegalArgumentException
    {
        return userEvent.selectUserByUsername(username);
    }

    /**
     * 查询指定的用户 -> 账户操作记录
     *
     * @Description 完整的业务流程.
     *
     * @param username {@linkplain UserAccountOperationInfo.Validator#username(String) 用户名称}
     *
     * @return {@linkplain UserAccountOperationInfo 指定的用户 -> 账户操作记录}
     *
     * @see User
     * @see UserAccountOperationInfo
     */
    @Override
    public @NotNull UserAccountOperationInfo selectUserAccountOperationInfoByUsername(@NotNull String username)
            throws IllegalArgumentException
    {
        return userEvent.selectUserAccountOperationInfoByUsername(username);
    }

    /**
     * 查询指定的用户 -> 个人信息
     *
     * @Description 完整的业务流程.
     *
     * @param username {@linkplain UserPersonInfo.Validator#username(String) 用户名称}
     *
     * @return {@linkplain UserPersonInfo 指定的用户 -> 个人信息}
     *
     * @see UserPersonInfo
     * @see User
     */
    @Override
    public @NotNull UserPersonInfo selectUserPersonInfoByUsername(@NotNull String username)
            throws IllegalArgumentException
    {
        return userEvent.selectUserPersonInfoByUsername(username);
    }

    /**
     * 查询指定的用户 -> [（安全认证）用户]
     *
     * @Description 完整的业务流程.
     *
     * @param username {@link User.Validator#username(String)}
     *
     * @return {@link UserAccountOperationInfo}
     *
     * @see SecurityUser
     */
    @Override
    public @NotNull SecurityUser selectSecurityUserByUsername(@NotNull String username)
            throws IllegalArgumentException
    {
        return userEvent.selectSecurityUserByUsername(username);
    }

    /**
     * 查询 (关联的) 角色 -> (关联的) 资源
     *
     * @Description 完整的业务流程.
     *
     * @param username {@linkplain SecurityUser.Validator#username(String) 用户名称}
     *
     * @return 资源集合 {@link SecurityResource}
     */
    @Override
    public @NotNull List<SecurityResource> selectResourceOnUserByUsername(@NotNull String username)
            throws IllegalArgumentException
    {
        return userEvent.selectResourceOnUserByUsername(username);
    }

    /**
     * 查询 (关联的) [用户 -> （安全认证）角色]
     *
     * @Description 完整的业务流程.
     *
     * @param username {@linkplain SecurityUser.Validator#username(String) 用户名称}
     *
     * @return {@linkplain SecurityRole 指定用户对应的[（安全认证）角色]}
     */
    @Override
    public @NotNull List<SecurityRole> selectRoleOnUserByUsername(@NotNull String username)
            throws IllegalArgumentException
    {
        return userEvent.selectRoleOnUserByUsername(username);
    }

    /**
     * 查询[用户 -> (关联的) 角色 -> (关联的) 资源 -> URL 相关信息]
     *
     * @Description 完整的业务流程.
     *
     * @param username {@linkplain SecurityUser.Validator#username(String) 用户名称}
     *
     * @return {@linkplain SecurityResourceUrl [用户 -> (关联的) 角色 -> (关联的) 资源 -> URL 相关信息]}
     */
    @Override
    public @NotNull List<SecurityResourceUrl> selectUrlInfoOnUserByUsername(@NotNull String username)
            throws IllegalArgumentException
    {
        return userEvent.selectUrlInfoOnUserByUsername(username);
    }

    /**
     * 查询[用户 -> (关联的) 角色 -> (关联的) 资源 -> URL 相关信息]
     *
     * @Description 完整的业务流程.
     *
     * @param username {@linkplain SecurityUser.Validator#username(String) 用户名称}
     * @param clientId {@linkplain SecurityResourceUrl#getClientId() [资源服务器 ID]}
     *
     * @return {@link SecurityResourceUrl}
     */
    @Override
    public @NotNull List<SecurityResourceUrl> selectUrlInfoOnUserByUsernameAndClientId(@NotNull String username, @NotNull String clientId)
            throws IllegalArgumentException
    {
        return userEvent.selectUrlInfoOnUserByUsernameAndClientId(username, clientId);
    }

}
