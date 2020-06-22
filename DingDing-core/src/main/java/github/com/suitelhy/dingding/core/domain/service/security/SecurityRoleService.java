package github.com.suitelhy.dingding.core.domain.service.security;

import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResource;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.core.domain.repository.security.SecurityRoleRepository;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityService;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * (安全) 角色
 *
 * @Description (安全) 角色 - 业务接口.
 *
 * @see EntityService
 * @see github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole
 * @see github.com.suitelhy.dingding.core.domain.repository.security.SecurityRoleRepository
 */
@Transactional(isolation = Isolation.READ_COMMITTED
        , propagation = Propagation.REQUIRED
        , readOnly = true
        /*, rollbackFor = Exception.class*/
        , timeout = 15)
public interface SecurityRoleService
        extends EntityService {

    /**
     * 判断存在
     *
     * @param code          角色编码    {@link SecurityRole}
     * @return {@link java.lang.Boolean}
     */
    /*@Transactional*/
    Boolean existsByCode(@NotNull String code);

    /**
     * 查询所有
     *
     * @param pageIndex     分页索引, 从0开始
     * @param pageSize      分页 - 每页容量
     * @return              {@link Page}
     */
    Page<SecurityRole> selectAll(int pageIndex, int pageSize);

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
     * 查询指定的角色
     *
     * @param code          角色编码    {@link SecurityRole}
     * @return {@link SecurityRole}
     */
    SecurityRole selectRoleByCode(@NotNull String code);

    /**
     * 查询 (关联的) 资源
     *
     * @param code          角色编码    {@link SecurityRole}
     * @return              资源的数据  {@link SecurityResource}
     * @see SecurityResource
     */
    @Nullable
    List<Map<String, Object>> selectResourceByCode(@NotNull String code);

    /**
     * 新增一个角色
     *
     * @param role          角色, 必须合法.   {@link SecurityRole}
     * @return 操作是否成功 / 是否已存在相同的有效数据
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean insert(@NotNull SecurityRole role);

    /**
     * 新增 角色 - 资源 关联
     *
     * @param role          角色, 必须合法且已持久化.  {@link SecurityRole}
     * @param resource      资源, 必须合法且已持久化.  {@link SecurityResource}
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    default boolean insertResource(@NotNull SecurityRole role, @NotNull SecurityResource resource) {
        if (null == role || null == resource) {
            return false;
        }
        /*System.out.println("===== insertResource(SecurityRole, SecurityResource) =====");*/

        Set<SecurityRole> roles = new HashSet<>(1);
        roles.add(role);

        Set<SecurityResource> resources = new HashSet<>(1);
        resources.add(resource);

        return insertResource(roles, resources);
    }

    /**
     * 新增 角色 - 资源 关联
     *
     * @param role          角色, 必须合法且已持久化.       {@link SecurityRole}
     * @param resources     资源, 必须全部合法且已持久化.    {@link SecurityResource}
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    default boolean insertResource(@NotNull SecurityRole role, @NotNull Set<SecurityResource> resources) {
        if (null == role
                || (null == resources || resources.isEmpty())) {
            return false;
        }
        /*System.out.println("===== insertResource(SecurityRole, Set<SecurityResource>) =====");*/

        Set<SecurityRole> roles = new HashSet<>(1);
        roles.add(role);

        return insertResource(roles, resources);
    }

    /**
     * 新增 角色 - 资源 关联
     *
     * @param roles         角色, 必须全部合法且已持久化.   {@link SecurityRole}
     * @param resource      资源, 必须合法且已持久化.      {@link SecurityResource}
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    default boolean insertResource(@NotNull Set<SecurityRole> roles, @NotNull SecurityResource resource) {
        if ((null == roles || roles.isEmpty())
                || null == resource) {
            return false;
        }
        /*System.out.println("===== insertResource(Set<SecurityRole>, SecurityResource) =====");*/

        Set<SecurityResource> resources = new HashSet<>(1);
        resources.add(resource);

        return insertResource(roles, resources);
    }

    /**
     * 新增 角色 - 资源 关联
     *
     * @param roles         角色, 必须全部合法且已持久化.    {@link SecurityRole}
     * @param resources     资源, 必须全部合法且已持久化.    {@link SecurityResource}
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    boolean insertResource(@NotNull Set<SecurityRole> roles, @NotNull Set<SecurityResource> resources);

    /**
     * 更新指定的角色
     *
     * @param role          角色  {@link SecurityRole}
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    boolean update(@NotNull SecurityRole role);

    /**
     * 删除指定的角色
     *
     * @Description 删除成功后校验持久化数据;
     *->    主要为了避免在未提交的事务中进行对操作结果的非预期判断.
     *
     * @param role          角色  {@link SecurityRole}
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    boolean delete(@NotNull SecurityRole role);

    /**
     * 删除 角色 - 资源 关联
     *
     * @param role          角色, 必须合法且已持久化.  {@link SecurityRole}
     * @param resource      资源, 必须合法且已持久化.  {@link SecurityResource}
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    default boolean deleteResource(@NotNull SecurityRole role, @NotNull SecurityResource resource) {
        if (null == role || null == resource) {
            return false;
        }

        Set<SecurityRole> roles = new HashSet<>(1);
        roles.add(role);

        Set<SecurityResource> resources = new HashSet<>(1);
        resources.add(resource);

        return deleteResource(roles, resources);
    }

    /**
     * 删除 角色 - 资源 关联
     *
     * @param role          角色, 必须合法且已持久化.       {@link SecurityRole}
     * @param resources     资源, 必须全部合法且已持久化.    {@link SecurityResource}
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    default boolean deleteResource(@NotNull SecurityRole role, @NotNull Set<SecurityResource> resources) {
        if (null == role
                || (null == resources || resources.isEmpty())) {
            return false;
        }

        Set<SecurityRole> roles = new HashSet<>(1);
        roles.add(role);

        return deleteResource(roles, resources);
    }

    /**
     * 删除 角色 - 资源 关联
     *
     * @param roles         角色, 必须全部合法且已持久化.   {@link SecurityRole}
     * @param resource      资源, 必须合法且已持久化.      {@link SecurityResource}
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    default boolean deleteResource(@NotNull Set<SecurityRole> roles, @NotNull SecurityResource resource) {
        if ((null == roles || roles.isEmpty())
                || null == resource) {
            return false;
        }

        Set<SecurityResource> resources = new HashSet<>(1);
        resources.add(resource);

        return deleteResource(roles, resources);
    }

    /**
     * 删除 角色 - 资源 关联
     *
     * @param roles         角色, 必须全部合法且已持久化.    {@link SecurityRole}
     * @param resources     资源, 必须全部合法且已持久化.    {@link SecurityResource}
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    boolean deleteResource(@NotNull Set<SecurityRole> roles, @NotNull Set<SecurityResource> resources);

}
