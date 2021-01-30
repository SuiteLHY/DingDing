package github.com.suitelhy.dingding.core.domain.event;

import github.com.suitelhy.dingding.core.domain.aggregate.UserAggregate;
import github.com.suitelhy.dingding.core.domain.entity.User;
import github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.core.domain.entity.UserPersonInfo;
import github.com.suitelhy.dingding.core.domain.entity.security.*;
import github.com.suitelhy.dingding.core.domain.event.impl.UserEventImpl;
import github.com.suitelhy.dingding.core.domain.service.UserAccountOperationInfoService;
import github.com.suitelhy.dingding.core.domain.service.UserPersonInfoService;
import github.com.suitelhy.dingding.core.domain.service.UserService;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.AggregateEvent;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.security.Security;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户 - 复杂业务接口
 *
 * @see User
 * @see UserAccountOperationInfo
 * @see UserPersonInfo
 * @see UserAggregate
 * @see UserEventImpl
 * @see UserService
 * @see UserAccountOperationInfoService
 * @see UserPersonInfoService
 */
@Transactional(isolation = Isolation.READ_COMMITTED
        , propagation = Propagation.REQUIRED
        , readOnly = true
        , rollbackFor = Exception.class
        , timeout = 15)
public interface UserEvent
        extends AggregateEvent {

    //===== 查询操作业务 =====//

    /**
     * 查询指定的用户 -> 基础信息
     *
     * @param username {@link User.Validator#username(String)}
     * @return {@link UserAccountOperationInfo}
     * @Description 完整的业务流程.
     * @see User
     */
    @NotNull
    User selectUserByUsername(@NotNull String username)
            throws IllegalArgumentException;

    /**
     * 查询指定的用户 -> 账户操作记录
     *
     * @param username {@link UserAccountOperationInfo.Validator#username(String)}
     * @return {@link UserAccountOperationInfo}
     * @Description 完整的业务流程.
     * @see User
     * @see UserAccountOperationInfo
     */
    @NotNull
    UserAccountOperationInfo selectUserAccountOperationInfoByUsername(@NotNull String username)
            throws IllegalArgumentException;

    /**
     * 查询指定的用户 -> 个人信息
     *
     * @param username {@link UserPersonInfo.Validator#username(String)}
     * @return {@link UserPersonInfo}
     * @Description 完整的业务流程.
     * @see UserPersonInfo
     * @see User
     */
    @NotNull
    UserPersonInfo selectUserPersonInfoByUsername(@NotNull String username)
            throws IllegalArgumentException;

    /**
     * 查询指定的用户 -> [（安全认证）用户]
     *
     * @param username {@link User.Validator#username(String)}
     * @return {@link UserAccountOperationInfo}
     * @Description 完整的业务流程.
     * @see SecurityUser
     */
    @NotNull
    SecurityUser selectSecurityUserByUsername(@NotNull String username)
            throws IllegalArgumentException;

    /**
     * 查询 (关联的) 角色 -> (关联的) 资源
     *
     * @param username 用户名称    {@link SecurityUser.Validator#username(String)}
     * @return 资源集合 {@link SecurityResource}
     * @Description 完整的业务流程.
     * @see github.com.suitelhy.dingding.core.domain.event.security.SecurityResourceEvent#selectResourceOnUserByUsername(String)
     */
    @NotNull
    List<SecurityResource> selectResourceOnUserByUsername(@NotNull String username)
            throws IllegalArgumentException;

    /**
     * 查询 (关联的) [用户 -> （安全认证）角色]
     *
     * @param username 用户名称    {@link SecurityUser.Validator#username(String)}
     * @return {@link SecurityRole}
     * @Description 完整的业务流程.
     * @see github.com.suitelhy.dingding.core.domain.event.security.SecurityRoleEvent#selectRoleOnUserByUsername(String)
     */
    @NotNull
    List<SecurityRole> selectRoleOnUserByUsername(@NotNull String username)
            throws IllegalArgumentException;

    /**
     * 查询[用户 -> (关联的) 角色 -> (关联的) 资源 -> URL 相关信息]
     *
     * @param username 用户名称    {@link SecurityUser.Validator#username(String)}
     * @return {@link SecurityResourceUrl}
     * @Description 完整的业务流程.
     * @see github.com.suitelhy.dingding.core.domain.event.security.SecurityUrlEvent#selectUrlInfoOnUserByUsername(String)
     */
    @NotNull
    List<SecurityResourceUrl> selectUrlInfoOnUserByUsername(@NotNull String username)
            throws IllegalArgumentException;

    /**
     * 查询[用户 -> (关联的) 角色 -> (关联的) 资源 -> URL 相关信息]
     *
     * @param username 用户名称         {@link SecurityUser.Validator#username(String)}
     * @param clientId [资源服务器 ID]  {@link SecurityResourceUrl#getClientId()}
     * @return {@link SecurityResourceUrl}
     * @Description 完整的业务流程.
     * @see github.com.suitelhy.dingding.core.domain.event.security.SecurityUrlEvent#selectUrlInfoOnUserByUsernameAndClientId(String, String)
     */
    @NotNull
    List<SecurityResourceUrl> selectUrlInfoOnUserByUsernameAndClientId(@NotNull String username, @NotNull String clientId)
            throws IllegalArgumentException;

    //===== 添加操作业务 =====//

    /**
     * 新增一个[（安全认证）用户 ←→ 角色]关联关系
     *
     * @param userRole [（安全认证）用户 ←→ 角色]
     * @param operator 操作者
     * @return 操作是否成功 / 是否已存在相同的有效数据
     * @Description 完整的业务流程.
     * @see github.com.suitelhy.dingding.core.domain.event.security.SecurityRoleEvent#insertUserRoleRelationship(SecurityUserRole, SecurityUser)
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean insertUserRoleRelationship(@NotNull SecurityUserRole userRole, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 新增一个[（安全认证）用户 ←→ 角色]关联关系
     *
     * @param user     （安全认证）用户
     * @param role     （安全认证）角色
     * @param operator 操作者
     * @return 操作是否成功 / 是否已存在相同的有效数据
     * @Description 完整的业务流程.
     * @see github.com.suitelhy.dingding.core.domain.event.security.SecurityRoleEvent#insertUserRoleRelationship(SecurityUser, SecurityRole, SecurityUser)
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    default boolean insertUserRoleRelationship(@NotNull SecurityUser user, @NotNull SecurityRole role, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException {
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

        final @NotNull Set<SecurityRole> roles = new HashSet<>(1);
        roles.add(role);

        return insertUserRoleRelationship(user, roles, operator);
    }

    /**
     * 新增多个[（安全认证）用户 ←→ 角色]关联关系
     *
     * @param user     （安全认证）用户
     * @param roles    （安全认证）角色
     * @param operator 操作者
     * @return 操作是否成功 / 是否已存在完全相同的有效数据集合
     * @Description 完整的业务流程.
     * @see github.com.suitelhy.dingding.core.domain.event.security.SecurityRoleEvent#insertUserRoleRelationship(SecurityUser, Set, SecurityUser)
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean insertUserRoleRelationship(@NotNull SecurityUser user, @NotNull Set<SecurityRole> roles, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 新增一个[（安全认证）用户 ←→ 角色]关联关系
     *
     * @param user     （安全认证）用户
     * @param roleVo   [(安全) 用户 -> 角色] {@link Security.RoleVo}, 仅需保证合法性, 不需要保证持久化.
     * @param operator 操作者
     * @return 操作是否成功 / 是否已存在相同的有效数据
     * @Description 完整的业务流程.
     * @see github.com.suitelhy.dingding.core.domain.event.security.SecurityRoleEvent#insertUserRoleRelationship(SecurityUser, Security.RoleVo, SecurityUser)
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean insertUserRoleRelationship(@NotNull SecurityUser user, @NotNull Security.RoleVo roleVo, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 新增(=注册)一个用户
     *
     * @param user                     预期新增的用户 -> 用户基础信息               {@link User}
     * @param userAccountOperationInfo 预期新增的用户 -> [用户 -> 账户操作基础记录]  {@link UserAccountOperationInfo}
     * @param userPersonInfo           [用户 -> 个人信息]                        {@link UserPersonInfo}
     * @param operator                 操作者
     * @return 操作是否成功
     * @throws IllegalArgumentException
     * @throws BusinessAtomicException
     * @Description 完整的业务流程.
     * @see UserAccountOperationInfoService
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean registerUser(@NotNull User user, @NotNull UserAccountOperationInfo userAccountOperationInfo, @NotNull UserPersonInfo userPersonInfo
            , @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    //===== 修改操作业务 =====//

    /**
     * 更新指定的用户 -> 基础信息
     *
     * @param user     被修改的用户 -> 基础信息
     * @param operator 操作者
     * @return 操作是否成功
     * @Description 完整的业务流程.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean updateUser(@NotNull User user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 更新指定的用户 -> 账户操作基础记录
     *
     * @param userAccountOperationInfo 被修改的用户 -> 账户操作基础记录  {@link UserAccountOperationInfo}
     * @param operator                 操作者
     * @return 操作是否成功
     * @Description 完整的业务流程.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean updateUserAccountOperationInfo(@NotNull UserAccountOperationInfo userAccountOperationInfo, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 更新指定的用户 -> 个人信息
     *
     * @param userPersonInfo 被修改的用户 -> 个人信息  {@link UserPersonInfo}
     * @param operator       操作者
     * @return 操作是否成功
     * @Description 完整的业务流程.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean updateUserPersonInfo(@NotNull UserPersonInfo userPersonInfo, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    //===== 删除操作业务 =====//

    /**
     * 删除指定的用户
     *
     * @param user     被修改的用户 -> 基础信息
     * @param operator 操作者
     * @return 操作是否成功
     * @Description 完整的业务流程.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean deleteUser(@NotNull User user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 删除[（安全认证）用户 ←→ 角色]关联关系
     *
     * @param user     [（安全认证）用户], 必须合法且已持久化.  {@link SecurityUser}
     * @param role     [（安全认证）角色], 必须合法且已持久化.  {@link SecurityRole}
     * @param operator 操作者
     * @return 操作是否成功
     * @Description 完整的业务流程.
     * @see github.com.suitelhy.dingding.core.domain.event.security.SecurityRoleEvent#deleteUserRoleRelationship(SecurityUser, SecurityRole, SecurityUser)
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean deleteUserRoleRelationship(@NotNull SecurityUser user, @NotNull SecurityRole role, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 删除指定的[（安全认证）用户]关联的所有[（安全认证）用户 ←→ 角色]关联关系
     *
     * @param user     [（安全认证）用户], 必须合法且已持久化.  {@link SecurityUser}
     * @param operator 操作者
     * @return 操作是否成功
     * @Description 完整的业务流程.
     * @see github.com.suitelhy.dingding.core.domain.event.security.SecurityRoleEvent#deleteUserRoleRelationshipOnUser(SecurityUser, SecurityUser)
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean deleteUserRoleRelationshipOnUser(@NotNull SecurityUser user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 删除指定的[（安全认证）角色]关联的所有[（安全认证）用户 ←→ 角色]关联关系
     *
     * @param role     [（安全认证）角色], 必须合法且已持久化.  {@link SecurityRole}
     * @param operator 操作者
     * @return 操作是否成功
     * @Description 完整的业务流程.
     * @see github.com.suitelhy.dingding.core.domain.event.security.SecurityRoleEvent#deleteUserRoleRelationshipOnRole(SecurityRole, SecurityUser)
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean deleteUserRoleRelationshipOnRole(@NotNull SecurityRole role, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

}
