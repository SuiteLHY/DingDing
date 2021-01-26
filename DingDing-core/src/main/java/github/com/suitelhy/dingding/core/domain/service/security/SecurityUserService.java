package github.com.suitelhy.dingding.core.domain.service.security;

import github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.core.domain.entity.security.*;
import github.com.suitelhy.dingding.core.domain.repository.security.SecurityUserRepository;
import github.com.suitelhy.dingding.core.domain.service.LogService;
import github.com.suitelhy.dingding.core.domain.service.UserAccountOperationInfoService;
import github.com.suitelhy.dingding.core.domain.service.security.impl.SecurityUserServiceImpl;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityService;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * (安全) 用户 - 业务
 *
 * @Description (安全) 用户 - 业务接口.
 *
 * @see SecurityUser
 * @see SecurityUserRepository
 *
 * @see SecurityUserServiceImpl
 *
 * @see SecurityRoleService
 * @see SecurityUserRoleService
 * @see UserAccountOperationInfoService
 * @see LogService
 */
@Transactional(isolation = Isolation.READ_COMMITTED
        , propagation = Propagation.REQUIRED
        , readOnly = true
        , timeout = 15)
public interface SecurityUserService
        extends EntityService {

    /**
     * 判断存在
     *
     * @param userId {@link SecurityUser#getUserId()}
     *
     * @return 判断结果
     *
     * @throws IllegalArgumentException
     */
    boolean existByUserId(@NotNull String userId)
            throws IllegalArgumentException;

    /**
     * 判断存在
     *
     * @param username {@link SecurityUser#getUsername()}
     *
     * @return 判断结果
     *
     * @throws IllegalArgumentException
     */
    boolean existByUsername(@NotNull String username)
            throws IllegalArgumentException;

    /**
     * 是否具有管理员权限
     *
     * @param username
     *
     * @return
     */
    boolean existAdminPermission(@NotNull String username)
            throws IllegalArgumentException;

//    /**
//     * 判断是否存在 (关联的) [（安全认证）角色]
//     *
//     * @param username  用户名称    {@link SecurityUser.Validator#username(String)}
//     *
//     * @return {@link boolean}
//     */
//    boolean existRoleByUsername(@NotNull String username)
//            throws IllegalArgumentException;

    /**
     * 查询所有
     *
     * @param pageIndex     分页索引, 从0开始
     * @param pageSize      分页 - 每页容量
     *
     * @return {@link Page}
     */
    @NotNull Page<SecurityUser> selectAll(int pageIndex, int pageSize)
            throws IllegalArgumentException;

    /**
     * 查询总页数
     *
     * @Description 查询数据列表 - 分页 - 总页数
     *
     * @param pageSize      分页 - 每页容量
     *
     * @return              分页 - 总页数
     */
    @NotNull Long selectCount(int pageSize)
            throws IllegalArgumentException;

    /**
     * 查询
     *
     * @param userId    用户ID    {@link SecurityUser.Validator#userId(String)}
     *
     * @return {@link SecurityUser}
     */
    @NotNull SecurityUser selectByUserId(@NotNull String userId)
            throws IllegalArgumentException;

    /**
     * 查询
     *
     * @param username  用户名称    {@link SecurityUser.Validator#username(String)}
     *
     * @return {@link SecurityUser}
     */
    @NotNull SecurityUser selectByUsername(@NotNull String username)
            throws IllegalArgumentException;

//    /**
//     * 查询 (关联的) 角色
//     *
//     * @param username  用户名称    {@link SecurityUser.Validator#username(String)}
//     *
//     * @return {@link SecurityRole}
//     */
//    @NotNull
//    List<SecurityRole> selectRoleByUsername(@NotNull String username)
//            throws IllegalArgumentException;

//    /**
//     * 查询 (关联的) 角色 -> (关联的) 资源
//     *
//     * @param username  用户名称    {@link SecurityUser.Validator#username(String)}
//     *
//     * @return 资源集合 {@link SecurityResource}
//     */
//    @NotNull List<SecurityResource> selectResourceByUsername(@NotNull String username)
//            throws IllegalArgumentException;

//    /**
//     * 查询 (关联的) 角色 -> (关联的) 资源
//     *
//     * @param username  用户名称
//     * @param clientId  资源服务器 ID    {@link SecurityResourceUrl#getClientId()}
//     *
//     * @return 资源集合 {@link SecurityResource}
//     */
//    List<Map<String, Object>> selectUrlPathByUsernameAndClientId(@NotNull String username, @NotNull String clientId)
//            throws IllegalArgumentException;

    /**
     * 新增一个用户
     *
     * @Description
     * · 完整业务流程的一部分.
     *
     * @param user                              [（安全认证）用户]
     * @param operator                          操作者
     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
     *
     * @return 操作是否成功 / 是否已存在相同的有效数据
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean insert(@NotNull SecurityUser user
            , @NotNull SecurityUser operator
            , @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
            throws IllegalArgumentException, BusinessAtomicException;

//    /**
//     * 新增 角色 关联
//     *
//     * @param user      用户, 必须合法且已持久化.  {@link SecurityUser}
//     * @param role      角色, 必须合法且已持久化.  {@link SecurityRole}
//     * @param operator  操作者
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , timeout = 15)
//    default boolean insertRole(@NotNull SecurityUser user, @NotNull SecurityRole role, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if (null == user || null == role) {
//            return false;
//        }
//
//        Set<SecurityUser> users = new HashSet<>(1);
//        users.add(user);
//
//        Set<SecurityRole> roles = new HashSet<>(1);
//        roles.add(role);
//
//        return insertRole(users, roles, operator);
//    }
//
//    /**
//     * 新增 角色 关联
//     *
//     * @param user      用户, 必须合法且已持久化.      {@link SecurityUser}
//     * @param roles     角色, 必须全部合法且已持久化.   {@link SecurityRole}
//     * @param operator  操作者
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , timeout = 15)
//    default boolean insertRole(@NotNull SecurityUser user, @NotNull Set<SecurityRole> roles, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if (null == user
//                || (null == roles || roles.isEmpty())) {
//            return false;
//        }
//
//        Set<SecurityUser> users = new HashSet<>(1);
//        users.add(user);
//
//        return insertRole(users, roles, operator);
//    }
//
//    /**
//     * 新增 角色 关联
//     *
//     * @param users     用户, 必须全部合法且已持久化.    {@link SecurityUser}
//     * @param role      角色, 必须合法且已持久化.       {@link SecurityRole}
//     * @param operator  操作者
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , timeout = 15)
//    default boolean insertRole(@NotNull Set<SecurityUser> users, @NotNull SecurityRole role, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if ((null == users || users.isEmpty())
//                || null == role) {
//            return false;
//        }
//
//        Set<SecurityRole> roles = new HashSet<>(1);
//        roles.add(role);
//
//        return insertRole(users, roles, operator);
//    }
//
//    /**
//     * 新增 角色 关联
//     *
//     * @param users     用户, 必须全部合法且已持久化.    {@link SecurityUser}
//     * @param roles     角色, 必须全部合法且已持久化.    {@link SecurityRole}
//     * @param operator  操作者
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , timeout = 15)
//    boolean insertRole(@NotNull Set<SecurityUser> users, @NotNull Set<SecurityRole> roles, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 更新指定的用户
     *
     * @param user                              [（安全认证）用户]
     * @param operator                          操作者
     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
     *
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean update(@NotNull SecurityUser user
            , @NotNull SecurityUser operator
            , @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 删除指定的用户
     *
     * @Description 删除成功后校验持久化数据; 主要为了避免在未提交的事务中进行对操作结果的非预期判断.
     * · 完整业务流程的一部分.
     *
     * @param user                              [（安全认证）用户]
     * @param operator                          操作者
     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
     *
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    boolean delete(@NotNull SecurityUser user
            , @NotNull SecurityUser operator
            , @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
            throws IllegalArgumentException, BusinessAtomicException;

//    /**
//     * 删除 角色 关联
//     *
//     * @param user      用户, 必须合法且已持久化.  {@link SecurityUser}
//     * @param role      角色, 必须合法且已持久化.  {@link SecurityRole}
//     * @param operator  操作者
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , timeout = 15)
//    default boolean deleteRole(@NotNull SecurityUser user, @NotNull SecurityRole role, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if (null == user || null == role) {
//            return false;
//        }
//
//        Set<SecurityUser> users = new HashSet<>(1);
//        users.add(user);
//
//        Set<SecurityRole> roles = new HashSet<>(1);
//        roles.add(role);
//
//        return deleteRole(users, roles, operator);
//    }
//
//    /**
//     * 删除 角色 关联
//     *
//     * @param user      用户, 必须合法且已持久化.      {@link SecurityUser}
//     * @param roles     角色, 必须全部合法且已持久化.   {@link SecurityRole}
//     * @param operator  操作者
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , timeout = 15)
//    default boolean deleteRole(@NotNull SecurityUser user, @NotNull Set<SecurityRole> roles, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if (null == user
//                || (null == roles || roles.isEmpty())) {
//            return false;
//        }
//
//        Set<SecurityUser> users = new HashSet<>(1);
//        users.add(user);
//
//        return deleteRole(users, roles, operator);
//    }
//
//    /**
//     * 删除 角色 关联
//     *
//     * @param users     用户, 必须全部合法且已持久化.    {@link SecurityUser}
//     * @param role      角色, 必须合法且已持久化.       {@link SecurityRole}
//     * @param operator  操作者
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , timeout = 15)
//    default boolean deleteRole(@NotNull Set<SecurityUser> users, @NotNull SecurityRole role, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if ((null == users || users.isEmpty())
//                || null == role) {
//            return false;
//        }
//
//        Set<SecurityRole> roles = new HashSet<>(1);
//        roles.add(role);
//
//        return deleteRole(users, roles, operator);
//    }
//
//    /**
//     * 删除 角色 关联
//     *
//     * @param users     用户, 必须全部合法且已持久化.    {@link SecurityUser}
//     * @param roles     角色, 必须全部合法且已持久化.    {@link SecurityRole}
//     * @param operator  操作者
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , timeout = 15)
//    boolean deleteRole(@NotNull Set<SecurityUser> users, @NotNull Set<SecurityRole> roles, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;

}
