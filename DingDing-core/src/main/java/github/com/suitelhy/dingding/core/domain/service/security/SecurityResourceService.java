package github.com.suitelhy.dingding.core.domain.service.security;

import github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.core.domain.entity.security.*;
import github.com.suitelhy.dingding.core.domain.service.security.impl.SecurityResourceServiceImpl;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityService;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.ContainArrayHashMap;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.ContainArrayHashSet;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * (安全) 资源
 *
 * @Description (安全) 资源 - 业务接口.
 *
 * @see EntityService
 * @see SecurityResourceServiceImpl
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
     *
     * @return {@link Page}
     */
    Page<SecurityResource> selectAll(int pageIndex, int pageSize)
            throws IllegalArgumentException;

    /**
     * 查询所有 [URL - ROLE] 权限对应关系
     *
     * @return {@link ContainArrayHashMap}
     */
    @NotNull ContainArrayHashMap<String, List<Object>> selectAllUrlRoleMap()
            throws IllegalArgumentException;

    /**
     * 查询所有 [URL - ROLE] 权限对应关系
     *
     * @param clientId  [资源服务器 ID]  {@link SecurityResourceUrl#getClientId()}
     *
     * @return {@link ContainArrayHashMap}
     */
    @NotNull ContainArrayHashMap<String, List<Object>> selectUrlRoleMap(@NotNull String clientId)
            throws IllegalArgumentException;

    /**
     * 查询总页数
     *
     * @Description 查询数据列表 - 分页 - 总页数.
     *
     * @param pageSize  分页 - 每页容量
     *
     * @return 分页 - 总页数
     */
    @NotNull Long selectCount(int pageSize)
            throws IllegalArgumentException;

    /**
     * 查询指定的资源
     *
     * @param code  资源编码    {@link SecurityResource#getCode()}
     *
     * @return {@link SecurityResource}
     */
    @NotNull SecurityResource selectResourceByCode(@NotNull String code)
            throws IllegalArgumentException;

//    /**
//     * 查询 (关联的) 角色
//     *
//     * @param code  {@link SecurityResource#getCode()}
//     *
//     * @return (安全) 角色 集合   {@link SecurityRole}
//     *
//     * @see SecurityRoleResource
//     */
//    @Nullable List<Map<String, Object>> selectRoleByCode(@NotNull String code)
//            throws IllegalArgumentException;

//    /**
//     * 查询 (关联的) URL
//     *
//     * @param code  资源编码    {@link SecurityResource#getCode()}
//     *
//     * @return [URL 集合] {@link SecurityResourceUrl#getUrlPath()}
//     * · 数据结构:
//     * {
//     *     clientId : [资源服务器 ID],
//     *     urlPath : [资源对应的 URL (Path部分)],
//     *     urlMethod : [资源对应的 URL Method]
//     * }
//     *
//     * @see SecurityResourceUrl
//     */
//    @NotNull List<Map<String, Object>> selectUrlByCode(@NotNull String code)
//            throws IllegalArgumentException;

    /**
     * 新增一个资源
     *
     * @param resource                          [（安全认证）资源]
     * @param operator                          操作者
     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
     *
     * @return 操作是否成功 / 是否已存在相同的有效数据
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean insert(@NotNull SecurityResource resource
            , @NotNull SecurityUser operator
            , @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
            throws IllegalArgumentException, BusinessAtomicException;

//    /**
//     * 新增 角色 关联
//     *
//     * @param resource      资源, 必须合法且已持久化. {@link SecurityResource}
//     * @param role          角色, 必须合法且已持久化. {@link SecurityRole}
//     * @param operator      操作者
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , timeout = 15)
//    default boolean insertRole(@NotNull SecurityResource resource, @NotNull SecurityRole role, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if (null == role || null == resource) {
//            return false;
//        }
//
//        Set<SecurityResource> resources = new HashSet<>(1);
//        resources.add(resource);
//
//        Set<SecurityRole> roles = new HashSet<>(1);
//        roles.add(role);
//
//        return insertRole(resources, roles, operator);
//    }
//
//    /**
//     * 新增 角色 关联
//     *
//     * @param resources     资源, 必须全部合法且已持久化. {@link SecurityResource}
//     * @param role          角色, 必须合法且已持久化. {@link SecurityRole}
//     * @param operator      操作者
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , timeout = 15)
//    default boolean insertRole(@NotNull Set<SecurityResource> resources, @NotNull SecurityRole role, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if ((null == resources || resources.isEmpty())
//                || null == role) {
//            return false;
//        }
//
//        Set<SecurityRole> roles = new HashSet<>(1);
//        roles.add(role);
//
//        return insertRole(resources, roles, operator);
//    }
//
//    /**
//     * 新增 角色 关联
//     *
//     * @param resource      资源, 必须合法且已持久化. {@link SecurityResource}
//     * @param roles         角色, 必须全部合法且已持久化. {@link SecurityRole}
//     * @param operator      操作者
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , timeout = 15)
//    default boolean insertRole(@NotNull SecurityResource resource, @NotNull Set<SecurityRole> roles, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if (null == resource
//                || (null == roles || roles.isEmpty())) {
//            return false;
//        }
//
//        Set<SecurityResource> resources = new HashSet<>(1);
//        resources.add(resource);
//
//        return insertRole(resources, roles, operator);
//    }
//
//    /**
//     * 新增 角色 关联
//     *
//     * @param resources     资源, 必须全部合法且已持久化. {@link SecurityResource}
//     * @param roles         角色, 必须全部合法且已持久化. {@link SecurityRole}
//     * @param operator      操作者
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , timeout = 15)
//    boolean insertRole(@NotNull Set<SecurityResource> resources, @NotNull Set<SecurityRole> roles, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;

//    /**
//     * 新增 URL 关联
//     *
//     * @Description 将一个资源 (必须合法且已持久化) 与一个 URL (若未持久化则新增) 进行关联.
//     *
//     * @param resource                          [（安全认证）资源]
//     * @param urlInfo                           [URL 信息]
//     * @param operator                          操作者
//     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    default boolean insertUrl(@NotNull SecurityResource resource
//            , @NotNull String[] urlInfo
//            , @NotNull SecurityUser operator
//            , @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if (null == resource) {
//            //-- 非法输入: （安全认证）资源
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "（安全认证）资源"
//                    , resource
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == urlInfo) {
//            //-- 非法输入: [URL 信息]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "[URL 信息]"
//                    , urlInfo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        Set<SecurityResource> resources = new HashSet<>(1);
//        resources.add(resource);
//
//        /**
//         * @see ContainArrayHashSet
//         */
//        ContainArrayHashSet<String> urlInfoSet = new ContainArrayHashSet<>(1);
//        urlInfoSet.add(urlInfo);
//
//        return insertUrl(resources, urlInfoSet, operator
//                , operator_userAccountOperationInfo);
//    }
//
//    /**
//     * 新增 URL 关联
//     *
//     * @Description 将一个资源 (必须合法且已持久化) 与若干 URL (若未持久化则新增) 进行关联.
//     *
//     * @param resource                          [（安全认证）资源]
//     * @param urls                              [URL 信息集合]
//     * @param operator                          操作者
//     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    default boolean insertUrl(@NotNull SecurityResource resource
//            , @NotNull ContainArrayHashSet<String> urls
//            , @NotNull SecurityUser operator
//            , @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if (null == resource) {
//            //-- 非法输入: （安全认证）资源
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "（安全认证）资源"
//                    , resource
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == urls) {
//            //-- 非法输入: [URL 信息集合]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "[URL 信息集合]"
//                    , urls
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        Set<SecurityResource> resources = new HashSet<>(1);
//        resources.add(resource);
//
//        return insertUrl(resources, urls, operator
//                , operator_userAccountOperationInfo);
//    }
//
//    /**
//     * 新增 URL 关联
//     *
//     * @Description 将若干资源 (必须合法且已持久化) 与一个 URL (若未持久化则新增) 进行关联.
//     *
//     * @param resources                         [（安全认证）资源]集合
//     * @param url                               [URL 信息]
//     * @param operator                          操作者
//     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    default boolean insertUrl(@NotNull Set<SecurityResource> resources
//            , @NotNull String[] url
//            , @NotNull SecurityUser operator
//            , @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if (null == resources || resources.isEmpty()) {
//            //-- 非法输入: [（安全认证）资源]集合
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "[（安全认证）资源]集合"
//                    , resources
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == url) {
//            //-- 非法输入: [URL 信息]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "[URL 信息]"
//                    , url
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        ContainArrayHashSet<String> urls = new ContainArrayHashSet<>(1);
//        urls.add(url);
//
//        return insertUrl(resources, urls, operator
//                , operator_userAccountOperationInfo);
//    }
//
//    /**
//     * 新增 URL 关联
//     *
//     * @Description 将若干资源 (必须合法且已持久化) 与若干 URL (若未持久化则新增) 进行关联.
//     *
//     * @param resources                         [（安全认证）资源]集合
//     * @param urls                              [URL 信息]集合
//     * @param operator                          操作者
//     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    boolean insertUrl(@NotNull Set<SecurityResource> resources
//            , @NotNull ContainArrayHashSet<String> urls
//            , @NotNull SecurityUser operator
//            , @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
//            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 更新指定的资源
     *
     * @param resource                          [（安全认证）资源]  {@link SecurityResource}
     * @param operator                          操作者
     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
     *
     * @return 操作是否成功
     *
     * @throws IllegalArgumentException
     * @throws BusinessAtomicException
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean update(@NotNull SecurityResource resource
            , @NotNull SecurityUser operator
            , @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 更新指定的资源
     *
     * @param old_resource      [（安全认证）资源] <- 原始版本业务全量数据
     * @param new_resource_data [（安全认证）资源] <- 需要更新的数据
     * · 数据结构:
     * {
     *      resource_icon: [图标],
     *      resource_link: [资源链接],
     *      resource_name: [资源名称],
     *      resource_parentCode: [父节点 <- 资源编码],
     *      resource_sort: [序号],
     *      resource_type: [资源类型]
     * }
     * @param operator          操作者
     *
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean update(@NotNull SecurityResource old_resource, @NotNull Map<String, Object> new_resource_data, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 删除指定的资源
     *
     * @Description 删除成功后校验持久化数据; 主要为了避免在未提交的事务中进行对操作结果的非预期判断.
     * · 完整业务流程的一部分.
     *
     * @param resource                          [（安全认证）资源]
     * @param operator                          操作者
     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
     *
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean delete(@NotNull SecurityResource resource
            , @NotNull SecurityUser operator
            , @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
            throws IllegalArgumentException, BusinessAtomicException;

//    /**
//     * 新增 角色 关联
//     *
//     * @param resource      资源, 必须合法且已持久化. {@link SecurityResource}
//     * @param role          角色, 必须合法且已持久化. {@link SecurityRole}
//     * @param operator      操作者
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , timeout = 15)
//    default boolean deleteRole(@NotNull SecurityResource resource, @NotNull SecurityRole role, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if (null == role || null == resource) {
//            return false;
//        }
//
//        Set<SecurityResource> resources = new HashSet<>(1);
//        resources.add(resource);
//
//        Set<SecurityRole> roles = new HashSet<>(1);
//        roles.add(role);
//
//        return deleteRole(resources, roles, operator);
//    }
//
//    /**
//     * 删除 角色 关联
//     *
//     * @param resources     资源, 必须全部合法且已持久化. {@link SecurityResource}
//     * @param role          角色, 必须合法且已持久化. {@link SecurityRole}
//     * @param operator      操作者
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , timeout = 15)
//    default boolean deleteRole(@NotNull Set<SecurityResource> resources, @NotNull SecurityRole role, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if ((null == resources || resources.isEmpty())
//                || null == role) {
//            return false;
//        }
//
//        Set<SecurityRole> roles = new HashSet<>(1);
//        roles.add(role);
//
//        return deleteRole(resources, roles, operator);
//    }
//
//    /**
//     * 删除 角色 关联
//     *
//     * @param resource      （安全认证）资源, 必须合法且已持久化. {@link SecurityResource}
//     * @param roles         （安全认证）角色, 必须全部合法且已持久化. {@link SecurityRole}
//     * @param operator      操作者
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , timeout = 15)
//    default boolean deleteRole(@NotNull SecurityResource resource, @NotNull Set<SecurityRole> roles, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if (null == resource
//                || (null == roles || roles.isEmpty())) {
//            return false;
//        }
//
//        Set<SecurityResource> resources = new HashSet<>(1);
//        resources.add(resource);
//
//        return deleteRole(resources, roles, operator);
//    }
//
//    /**
//     * 删除 角色 关联
//     *
//     * @param resources     （安全认证）资源, 必须全部合法且已持久化. {@link SecurityResource}
//     * @param roles         （安全认证）角色, 必须全部合法且已持久化. {@link SecurityRole}
//     * @param operator      操作者
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , timeout = 15)
//    boolean deleteRole(@NotNull Set<SecurityResource> resources, @NotNull Set<SecurityRole> roles, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;

//    /**
//     * 删除 URL 关联
//     *
//     * @Description 将一个资源 (必须合法且已持久化) 与一个 URL (若未持久化则新增) 的关联进行删除.
//     *
//     * @param resource  （安全认证）资源, 必须全部合法且已持久化. {@link SecurityResource}
//     * @param url       （安全认证）URL信息, 必须全部合法且已持久化. {@link SecurityRole}
//     * @param operator  操作者
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    default boolean deleteUrl(@NotNull SecurityResource resource, @NotNull String[] url, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if (null == resource || null == url) {
//            return false;
//        }
//
//        Set<SecurityResource> resources = new HashSet<>(1);
//        resources.add(resource);
//
//        ContainArrayHashSet<String> urls = new ContainArrayHashSet<>(1);
//        urls.add(url);
//
//        return deleteUrl(resources, urls, operator);
//    }
//
//    /**
//     * 删除 URL 关联
//     *
//     * @Description 将一个资源 (必须合法且已持久化) 与若干 URL (若未持久化则新增) 的关联进行删除.
//     *
//     * @param resource  （安全认证）资源, 必须全部合法且已持久化. {@link SecurityResource}
//     * @param urls      （安全认证）URL信息, 必须全部合法且已持久化. {@link SecurityRole}
//     * @param operator  操作者
//     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    default boolean deleteUrl(@NotNull SecurityResource resource, @NotNull ContainArrayHashSet<String> urls, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if (null == resource || null == urls) {
//            return false;
//        }
//
//        Set<SecurityResource> resources = new HashSet<>(1);
//        resources.add(resource);
//
//        return deleteUrl(resources, urls, operator);
//    }
//
//    /**
//     * 删除 URL 关联
//     *
//     * @Description 将若干资源 (必须合法且已持久化) 与一个 URL (若未持久化则新增) 的关联进行删除.
//     *
//     * @param resources                         [（安全认证）资源], 必须全部合法且已持久化 {@link SecurityResource}
//     * @param urlInfo                           [（安全认证）角色], 必须全部合法且已持久化 {@link SecurityRole}
//     * @param operator                          操作者
//     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    default boolean deleteUrl(@NotNull Set<SecurityResource> resources, @NotNull String[] urlInfo, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if (null == resources || null == urlInfo) {
//            return false;
//        }
//
//        ContainArrayHashSet<String> urls = new ContainArrayHashSet<>(1);
//        urls.add(urlInfo);
//
//        return deleteUrl(resources, urls, operator);
//    }
//
//    /**
//     * 删除 URL 关联
//     *
//     * @Description 将若干资源 (必须合法且已持久化) 与若干 URL (若未持久化则新增) 的关联进行删除.
//     *
//     * @param resources                         [（安全认证）资源], 必须全部合法且已持久化 {@link SecurityResource}
//     * @param urlInfoSet                        [URL 相关信息]                       {@link SecurityResourceUrl}
//     * @param operator                          操作者
//     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    boolean deleteUrl(@NotNull Set<SecurityResource> resources
//            , @NotNull ContainArrayHashSet<String> urlInfoSet
//            , @NotNull SecurityUser operator
//            , @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
//            throws IllegalArgumentException, BusinessAtomicException;

}
