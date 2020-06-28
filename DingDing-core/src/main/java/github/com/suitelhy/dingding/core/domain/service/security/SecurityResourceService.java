package github.com.suitelhy.dingding.core.domain.service.security;

import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResource;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResourceUrl;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRoleResource;
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
 * (安全) 资源
 *
 * @Description (安全) 资源 - 业务接口.
 *
 * @see EntityService
 */
@Transactional(isolation = Isolation.READ_COMMITTED
        , propagation = Propagation.REQUIRED
        , readOnly = true
        , rollbackFor = Exception.class
        , timeout = 15)
public interface SecurityResourceService
        extends EntityService {

    /**
     * 查询所有
     *
     * @param pageIndex 分页索引, 从 0 开始.
     * @param pageSize
     * @return
     */
    Page<SecurityResource> selectAll(int pageIndex, int pageSize);

    /**
     * 查询所有 URL - ROLE 权限对应关系
     *
     * @return
     */
    Map<String, List<Object>> selectAllUrlRoleMap();

    /**
     * 查询总页数
     *
     * @Description 查询数据列表 - 分页 - 总页数.
     *
     * @param pageSize  分页 - 每页容量
     * @return 分页 - 总页数
     */
    Long selectCount(int pageSize);

    /**
     * 查询指定的资源
     *
     * @param code
     * @return
     */
    SecurityResource selectResourceByCode(@NotNull String code);

    /**
     * 查询 (关联的) 角色
     *
     * @param code              {@link SecurityResource#getCode()}
     * @return (安全) 角色 集合   {@link SecurityRole}
     * @see SecurityRoleResource
     */
    List<Map<String, Object>> selectRoleByCode(@NotNull String code);

    /**
     * 查询 (关联的) URL
     *
     * @param code          {@link SecurityResource#getCode()}
     * @return URL集合       {@link SecurityResourceUrl#getUrlPath()}
     * @see SecurityResourceUrl
     */
    List<Map<String, Object>> selectUrlByCode(@NotNull String code);

    /**
     * 新增一个资源
     *
     * @param resource
     * @return 操作是否成功 / 是否已存在相同的有效数据
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean insert(@NotNull SecurityResource resource);

    /**
     * 新增 角色 关联
     *
     * @param resource      资源, 必须合法且已持久化. {@link SecurityResource}
     * @param role          角色, 必须合法且已持久化. {@link SecurityRole}
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    default boolean insertRole(@NotNull SecurityResource resource, @NotNull SecurityRole role) {
        if (null == role || null == resource) {
            return false;
        }

        Set<SecurityResource> resources = new HashSet<>(1);
        resources.add(resource);

        Set<SecurityRole> roles = new HashSet<>(1);
        roles.add(role);

        return insertRole(resources, roles);
    }

    /**
     * 新增 角色 关联
     *
     * @param resources     资源, 必须全部合法且已持久化. {@link SecurityResource}
     * @param role          角色, 必须合法且已持久化. {@link SecurityRole}
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    default boolean insertRole(@NotNull Set<SecurityResource> resources, @NotNull SecurityRole role) {
        if ((null == resources || resources.isEmpty())
                || null == role) {
            return false;
        }

        Set<SecurityRole> roles = new HashSet<>(1);
        roles.add(role);

        return insertRole(resources, roles);
    }

    /**
     * 新增 角色 关联
     *
     * @param resource      资源, 必须合法且已持久化. {@link SecurityResource}
     * @param roles         角色, 必须全部合法且已持久化. {@link SecurityRole}
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    default boolean insertRole(@NotNull SecurityResource resource, @NotNull Set<SecurityRole> roles) {
        if (null == resource
                || (null == roles || roles.isEmpty())) {
            return false;
        }

        Set<SecurityResource> resources = new HashSet<>(1);
        resources.add(resource);

        return insertRole(resources, roles);
    }

    /**
     * 新增 角色 关联
     *
     * @param resources     资源, 必须全部合法且已持久化. {@link SecurityResource}
     * @param roles         角色, 必须全部合法且已持久化. {@link SecurityRole}
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    boolean insertRole(@NotNull Set<SecurityResource> resources, @NotNull Set<SecurityRole> roles);

    /**
     * 新增 URL 关联
     *
     * @Description 将一个资源 (必须合法且已持久化) 与一个 URL (若未持久化则新增) 进行关联.
     *
     * @param resource
     * @param url
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    default boolean insertUrl(@NotNull SecurityResource resource, @NotNull String url) {
        if (null == resource || null == url) {
            return false;
        }

        Set<SecurityResource> resources = new HashSet<>(1);
        resources.add(resource);

        Set<String> urls = new HashSet<>(1);
        urls.add(url);

        return insertUrl(resources, urls);
    }

    /**
     * 新增 URL 关联
     *
     * @Description 将一个资源 (必须合法且已持久化) 与若干 URL (若未持久化则新增) 进行关联.
     *
     * @param resource
     * @param urls
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    default boolean insertUrl(@NotNull SecurityResource resource, @NotNull Set<String> urls) {
        if (null == resource || null == urls) {
            return false;
        }

        Set<SecurityResource> resources = new HashSet<>(1);
        resources.add(resource);

        return insertUrl(resources, urls);
    }

    /**
     * 新增 URL 关联
     *
     * @Description 将若干资源 (必须合法且已持久化) 与一个 URL (若未持久化则新增) 进行关联.
     *
     * @param resources
     * @param url
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    default boolean insertUrl(@NotNull Set<SecurityResource> resources, @NotNull String url) {
        if (null == resources || null == url) {
            return false;
        }

        Set<String> urls = new HashSet<>(1);
        urls.add(url);

        return insertUrl(resources, urls);
    }

    /**
     * 新增 URL 关联
     *
     * @Description 将若干资源 (必须合法且已持久化) 与若干 URL (若未持久化则新增) 进行关联.
     *
     * @param resources
     * @param urls
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean insertUrl(@NotNull Set<SecurityResource> resources, @NotNull Set<String> urls);

    /**
     * 更新指定的资源
     *
     * @param resource
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean update(@NotNull SecurityResource resource);

    /**
     * 删除指定的资源
     *
     * @Description 删除成功后校验持久化数据;
     *->    主要为了避免在未提交的事务中进行对操作结果的非预期判断.
     *
     * @param resource
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean delete(@NotNull SecurityResource resource);

    /**
     * 新增 角色 关联
     *
     * @param resource      资源, 必须合法且已持久化. {@link SecurityResource}
     * @param role          角色, 必须合法且已持久化. {@link SecurityRole}
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    default boolean deleteRole(@NotNull SecurityResource resource, @NotNull SecurityRole role) {
        if (null == role || null == resource) {
            return false;
        }

        Set<SecurityResource> resources = new HashSet<>(1);
        resources.add(resource);

        Set<SecurityRole> roles = new HashSet<>(1);
        roles.add(role);

        return deleteRole(resources, roles);
    }

    /**
     * 删除 角色 关联
     *
     * @param resources     资源, 必须全部合法且已持久化. {@link SecurityResource}
     * @param role          角色, 必须合法且已持久化. {@link SecurityRole}
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    default boolean deleteRole(@NotNull Set<SecurityResource> resources, @NotNull SecurityRole role) {
        if ((null == resources || resources.isEmpty())
                || null == role) {
            return false;
        }

        Set<SecurityRole> roles = new HashSet<>(1);
        roles.add(role);

        return deleteRole(resources, roles);
    }

    /**
     * 删除 角色 关联
     *
     * @param resource      资源, 必须合法且已持久化. {@link SecurityResource}
     * @param roles         角色, 必须全部合法且已持久化. {@link SecurityRole}
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    default boolean deleteRole(@NotNull SecurityResource resource, @NotNull Set<SecurityRole> roles) {
        if (null == resource
                || (null == roles || roles.isEmpty())) {
            return false;
        }

        Set<SecurityResource> resources = new HashSet<>(1);
        resources.add(resource);

        return deleteRole(resources, roles);
    }

    /**
     * 删除 角色 关联
     *
     * @param resources     资源, 必须全部合法且已持久化. {@link SecurityResource}
     * @param roles         角色, 必须全部合法且已持久化. {@link SecurityRole}
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    boolean deleteRole(@NotNull Set<SecurityResource> resources, @NotNull Set<SecurityRole> roles);

    /**
     * 删除 URL 关联
     *
     * @Description 将一个资源 (必须合法且已持久化) 与一个 URL (若未持久化则新增) 的关联进行删除.
     *
     * @param resource
     * @param url
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    default boolean deleteUrl(@NotNull SecurityResource resource, @NotNull String url) {
        if (null == resource || null == url) {
            return false;
        }

        Set<SecurityResource> resources = new HashSet<>(1);
        resources.add(resource);

        Set<String> urls = new HashSet<>(1);
        urls.add(url);

        return deleteUrl(resources, urls);
    }

    /**
     * 删除 URL 关联
     *
     * @Description 将一个资源 (必须合法且已持久化) 与若干 URL (若未持久化则新增) 的关联进行删除.
     *
     * @param resource
     * @param urls
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    default boolean deleteUrl(@NotNull SecurityResource resource, @NotNull Set<String> urls) {
        if (null == resource || null == urls) {
            return false;
        }

        Set<SecurityResource> resources = new HashSet<>(1);
        resources.add(resource);

        return deleteUrl(resources, urls);
    }

    /**
     * 删除 URL 关联
     *
     * @Description 将若干资源 (必须合法且已持久化) 与一个 URL (若未持久化则新增) 的关联进行删除.
     *
     * @param resources
     * @param url
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    default boolean deleteUrl(@NotNull Set<SecurityResource> resources, @NotNull String url) {
        if (null == resources || null == url) {
            return false;
        }

        Set<String> urls = new HashSet<>(1);
        urls.add(url);

        return deleteUrl(resources, urls);
    }

    /**
     * 删除 URL 关联
     *
     * @Description 将若干资源 (必须合法且已持久化) 与若干 URL (若未持久化则新增) 的关联进行删除.
     *
     * @param resources
     * @param urls
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean deleteUrl(@NotNull Set<SecurityResource> resources, @NotNull Set<String> urls);

}
