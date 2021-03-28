package github.com.suitelhy.dingding.user.service.api.domain.event.read;

import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityResource;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityResourceUrl;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.security.service.api.domain.entity.User;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserPersonInfo;

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
public interface UserReadEvent {

    //===== 查询操作业务 =====//

    /**
     * 查询指定的用户 -> 基础信息
     *
     * @Description 完整的业务流程.
     *
     * @param username  {@linkplain User.Validator#username(String) 用户名称}
     *
     * @return {@linkplain User 指定的用户 -> 基础信息}
     */
    @NotNull User selectUserByUsername(@NotNull String username)
            throws IllegalArgumentException;

    /**
     * 查询指定的用户 -> 账户操作记录
     *
     * @Description 完整的业务流程.
     *
     * @param username  {@linkplain UserAccountOperationInfo.Validator#username(String) 用户名称}
     *
     * @return {@linkplain UserAccountOperationInfo 指定的用户 -> 账户操作记录}
     *
     * @see User
     * @see UserAccountOperationInfo
     */
    @NotNull UserAccountOperationInfo selectUserAccountOperationInfoByUsername(@NotNull String username)
            throws IllegalArgumentException;

    /**
     * 查询指定的用户 -> 个人信息
     *
     * @Description 完整的业务流程.
     *
     * @param username  {@linkplain UserPersonInfo.Validator#username(String) 用户名称}
     *
     * @return {@linkplain UserPersonInfo 指定的用户 -> 个人信息}
     *
     * @see UserPersonInfo
     * @see User
     */
    @NotNull UserPersonInfo selectUserPersonInfoByUsername(@NotNull String username)
            throws IllegalArgumentException;

    /**
     * 查询指定的用户 -> [（安全认证）用户]
     *
     * @Description 完整的业务流程.
     *
     * @param username  {@link User.Validator#username(String)}
     *
     * @return {@link UserAccountOperationInfo}
     *
     * @see SecurityUser
     */
    @NotNull SecurityUser selectSecurityUserByUsername(@NotNull String username)
            throws IllegalArgumentException;

    /**
     * 查询 (关联的) 角色 -> (关联的) 资源
     *
     * @Description 完整的业务流程.
     *
     * @param username  {@linkplain SecurityUser.Validator#username(String) 用户名称}
     *
     * @return 资源集合 {@link SecurityResource}
     */
    @NotNull List<SecurityResource> selectResourceOnUserByUsername(@NotNull String username)
            throws IllegalArgumentException;

    /**
     * 查询 (关联的) [用户 -> （安全认证）角色]
     *
     * @Description 完整的业务流程.
     *
     * @param username  {@linkplain SecurityUser.Validator#username(String) 用户名称}
     *
     * @return {@linkplain SecurityRole 指定用户对应的[（安全认证）角色]}
     */
    @NotNull List<SecurityRole> selectRoleOnUserByUsername(@NotNull String username)
            throws IllegalArgumentException;

    /**
     * 查询[用户 -> (关联的) 角色 -> (关联的) 资源 -> URL 相关信息]
     *
     * @Description 完整的业务流程.
     *
     * @param username  {@linkplain SecurityUser.Validator#username(String) 用户名称}
     *
     * @return {@linkplain SecurityResourceUrl [用户 -> (关联的) 角色 -> (关联的) 资源 -> URL 相关信息]}
     */
    @NotNull List<SecurityResourceUrl> selectUrlInfoOnUserByUsername(@NotNull String username)
            throws IllegalArgumentException;

    /**
     * 查询[用户 -> (关联的) 角色 -> (关联的) 资源 -> URL 相关信息]
     *
     * @Description 完整的业务流程.
     *
     * @param username  {@linkplain SecurityUser.Validator#username(String) 用户名称}
     * @param clientId  {@linkplain SecurityResourceUrl#getClientId() [资源服务器 ID]}
     *
     * @return {@link SecurityResourceUrl}
     */
    @NotNull List<SecurityResourceUrl> selectUrlInfoOnUserByUsernameAndClientId(@NotNull String username, @NotNull String clientId)
            throws IllegalArgumentException;

}
