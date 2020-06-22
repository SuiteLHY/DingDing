package github.com.suitelhy.dingding.core.domain.service.security;

import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResource;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUserRole;
import github.com.suitelhy.dingding.core.domain.repository.security.SecurityUserRepository;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityService;
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
 * (安全) 用户
 *
 * @Description (安全) 用户 - 业务接口.
 *
 * @see SecurityUser
 * @see SecurityUserRepository
 */
@Transactional(isolation = Isolation.READ_COMMITTED
        , propagation = Propagation.REQUIRED
        , readOnly = true
        , timeout = 15)
public interface SecurityUserService
        extends EntityService {

    /**
     * 查询所有
     *
     * @param pageIndex     分页索引, 从0开始
     * @param pageSize      分页 - 每页容量
     * @return {@link Page}
     */
    Page<SecurityUser> selectAll(int pageIndex, int pageSize);

    /**
     * 查询总页数
     *
     * @Description 查询数据列表 - 分页 - 总页数
     *
     * @param pageSize      分页 - 每页容量
     * @return              分页 - 总页数
     */
    Long selectCount(int pageSize);

    /**
     * 查询
     *
     * @param userId
     * @return {@link SecurityUser}
     */
    SecurityUser selectUserByUserId(@NotNull String userId);

    /**
     * 查询
     *
     * @param username
     * @return {@link SecurityUser}
     */
    SecurityUser selectUserByUsername(@NotNull String username);

    /**
     * 查询 (关联的) 角色
     *
     * @param username
     * @return {@link SecurityUserRole}
     */
    List<Map<String, Object>> selectRoleByUsername(@NotNull String username);

    /**
     * 查询 (关联的) 角色 -> (关联的) 资源
     *
     * @param username
     * @return 资源集合         {@link SecurityResource}
     */
    List<Map<String, Object>> selectResourceByUsername(@NotNull String username);

    /**
     * 查询 (关联的) 角色 -> (关联的) 资源
     *
     * @param username
     * @return 资源集合         {@link SecurityResource}
     */
    List<Map<String, Object>> selectUrlPathByUsername(@NotNull String username);

    /**
     * 新增一个用户
     *
     * @param user
     * @return 操作是否成功 / 是否已存在相同的有效数据
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    boolean insert(@NotNull SecurityUser user);

    /**
     * 新增 角色 关联
     *
     * @param user          用户, 必须合法且已持久化.  {@link SecurityUser}
     * @param role          角色, 必须合法且已持久化.  {@link SecurityRole}
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    default boolean insertRole(@NotNull SecurityUser user, @NotNull SecurityRole role) {
        if (null == user || null == role) {
            return false;
        }

        Set<SecurityUser> users = new HashSet<>(1);
        users.add(user);

        Set<SecurityRole> roles = new HashSet<>(1);
        roles.add(role);

        return insertRole(users, roles);
    }

    /**
     * 新增 角色 关联
     *
     * @param user          用户, 必须合法且已持久化.      {@link SecurityUser}
     * @param roles         角色, 必须全部合法且已持久化.   {@link SecurityRole}
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    default boolean insertRole(@NotNull SecurityUser user, @NotNull Set<SecurityRole> roles) {
        if (null == user
                || (null == roles || roles.isEmpty())) {
            return false;
        }

        Set<SecurityUser> users = new HashSet<>(1);
        users.add(user);

        return insertRole(users, roles);
    }

    /**
     * 新增 角色 关联
     *
     * @param users         用户, 必须全部合法且已持久化.    {@link SecurityUser}
     * @param role          角色, 必须合法且已持久化.       {@link SecurityRole}
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    default boolean insertRole(@NotNull Set<SecurityUser> users, @NotNull SecurityRole role) {
        if ((null == users || users.isEmpty())
                || null == role) {
            return false;
        }

        Set<SecurityRole> roles = new HashSet<>(1);
        roles.add(role);

        return insertRole(users, roles);
    }

    /**
     * 新增 角色 关联
     *
     * @param users         用户, 必须全部合法且已持久化.    {@link SecurityUser}
     * @param roles         角色, 必须全部合法且已持久化.    {@link SecurityRole}
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    boolean insertRole(@NotNull Set<SecurityUser> users, @NotNull Set<SecurityRole> roles);

    /**
     * 更新指定的用户
     *
     * @param user
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    boolean update(@NotNull SecurityUser user);

    /**
     * 删除指定的用户
     *
     * @Description 删除成功后校验持久化数据;
     *->    主要为了避免在未提交的事务中进行对操作结果的非预期判断.
     *
     * @param user
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    boolean delete(@NotNull SecurityUser user);

    /**
     * 删除 角色 关联
     *
     * @param user          用户, 必须合法且已持久化.  {@link SecurityUser}
     * @param role          角色, 必须合法且已持久化.  {@link SecurityRole}
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    default boolean deleteRole(@NotNull SecurityUser user, @NotNull SecurityRole role) {
        if (null == user || null == role) {
            return false;
        }

        Set<SecurityUser> users = new HashSet<>(1);
        users.add(user);

        Set<SecurityRole> roles = new HashSet<>(1);
        roles.add(role);

        return deleteRole(users, roles);
    }

    /**
     * 删除 角色 关联
     *
     * @param user          用户, 必须合法且已持久化.      {@link SecurityUser}
     * @param roles         角色, 必须全部合法且已持久化.   {@link SecurityRole}
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    default boolean deleteRole(@NotNull SecurityUser user, @NotNull Set<SecurityRole> roles) {
        if (null == user
                || (null == roles || roles.isEmpty())) {
            return false;
        }

        Set<SecurityUser> users = new HashSet<>(1);
        users.add(user);

        return deleteRole(users, roles);
    }

    /**
     * 删除 角色 关联
     *
     * @param users         用户, 必须全部合法且已持久化.    {@link SecurityUser}
     * @param role          角色, 必须合法且已持久化.       {@link SecurityRole}
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    default boolean deleteRole(@NotNull Set<SecurityUser> users, @NotNull SecurityRole role) {
        if ((null == users || users.isEmpty())
                || null == role) {
            return false;
        }

        Set<SecurityRole> roles = new HashSet<>(1);
        roles.add(role);

        return deleteRole(users, roles);
    }

    /**
     * 删除 角色 关联
     *
     * @param users         用户, 必须全部合法且已持久化.    {@link SecurityUser}
     * @param roles         角色, 必须全部合法且已持久化.    {@link SecurityRole}
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    boolean deleteRole(@NotNull Set<SecurityUser> users, @NotNull Set<SecurityRole> roles);

}
