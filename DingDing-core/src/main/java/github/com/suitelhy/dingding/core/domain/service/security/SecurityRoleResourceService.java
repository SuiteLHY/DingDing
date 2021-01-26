package github.com.suitelhy.dingding.core.domain.service.security;

import github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.core.domain.entity.security.*;
import github.com.suitelhy.dingding.core.domain.repository.security.SecurityRoleResourceRepository;
import github.com.suitelhy.dingding.core.domain.service.LogService;
import github.com.suitelhy.dingding.core.domain.service.UserAccountOperationInfoService;
import github.com.suitelhy.dingding.core.domain.service.security.impl.SecurityRoleResourceServiceImpl;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityService;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * （安全认证）角色 ←→ 资源
 *
 * @Description [（安全认证）角色 ←→ 资源]关联关系 - 业务接口.
 *
 * @see SecurityRoleResource
 * @see SecurityRoleResourceRepository
 * @see SecurityRoleResourceServiceImpl
 * @see UserAccountOperationInfoService
 * @see LogService
 */
@Transactional(isolation = Isolation.READ_COMMITTED
        , propagation = Propagation.REQUIRED
        , readOnly = true
        , timeout = 15)
public interface SecurityRoleResourceService
        extends EntityService {

    /**
     * 判断[（安全认证）资源]是否存在 (关联的) [（安全认证）角色]
     *
     * @param resourceCode  资源编码    {@link SecurityRoleResource.Validator#resourceCode(String)}
     *
     * @return {@link boolean}
     */
    boolean existRoleByResourceCode(@NotNull String resourceCode);

    /**
     * 判断[（安全认证）角色]是否存在 (关联的) [（安全认证）资源]
     *
     * @param roleCode  角色编码    {@link SecurityRoleResource.Validator#roleCode(String)}
     *
     * @return {@link boolean}
     */
    boolean existResourceByRoleCode(@NotNull String roleCode);

    /**
     * 查询所有
     *
     * @param pageIndex     分页索引, 从0开始
     * @param pageSize      分页 - 每页容量
     *
     * @return {@link Page}
     */
    Page<SecurityRoleResource> selectAll(int pageIndex, int pageSize);

    /**
     * 查询总页数
     *
     * @Description 查询数据列表 - 分页 - 总页数
     *
     * @param pageSize      分页 - 每页容量
     *
     * @return              分页 - 总页数
     */
    @NotNull Long selectCount(int pageSize);

    /**
     * 查询
     *
     * @param resourceCode  资源编码    {@link SecurityRoleResource.Validator#resourceCode(String)}
     *
     * @return {@link SecurityRoleResource}
     */
    @NotNull List<SecurityRoleResource> selectByResourceCode(@NotNull String resourceCode);

    /**
     * 查询
     *
     * @param roleCode  角色编码    {@link SecurityRoleResource.Validator#roleCode(String)}
     *
     * @return {@link SecurityRoleResource}
     */
    @NotNull List<SecurityRoleResource> selectByRoleCode(@NotNull String roleCode);

    //===== 添加操作业务 =====//

    /**
     * 新增一个[（安全认证）角色 ←→ 资源]关联关系
     *
     * @param roleResource  [（安全认证）角色 ←→ 资源]    {@link SecurityRoleResource}
     * @param operator      操作者
     *
     * @return 操作是否成功 / 是否已存在相同的有效数据
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    boolean insert(@NotNull SecurityRoleResource roleResource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

//    /**
//     * 新增一个[（安全认证）角色 ←→ 资源]关联关系
//     *
//     * @param role      （安全认证）角色
//     * @param resource  （安全认证）资源
//     * @param operator  操作者
//     *
//     * @return 操作是否成功 / 是否已存在相同的有效数据
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    default boolean insert(@NotNull SecurityRole role, @NotNull SecurityResource resource, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if (null == role) {
//            //-- 非法输入: （安全认证）角色
//            throw new IllegalArgumentException(this.getClass().getSimpleName()
//                    .concat(" -> 非法输入: （安全认证）角色"));
//        }
//        if (null == resource) {
//            //-- 非法输入: （安全认证）资源
//            throw new IllegalArgumentException(this.getClass().getSimpleName()
//                    .concat(" -> 非法输入: （安全认证）资源"));
//        }
//
//        Set<SecurityResource> resources = new HashSet<>(1);
//        resources.add(resource);
//
//        return insert(role, resources, operator);
//    }
//
//    /**
//     * 新增多个[（安全认证）角色 ←→ 资源]关联关系
//     *
//     * @param role          （安全认证）角色
//     * @param resources     （安全认证）资源
//     * @param operator      操作者
//     *
//     * @return 操作是否成功 / 是否已存在完全相同的有效数据集合
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    boolean insert(@NotNull SecurityRole role, @NotNull Set<SecurityResource> resources, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;

    //===== 删除操作业务 =====//

    /**
     * 删除指定的[（安全认证）角色 ←→ 资源]关联关系
     *
     * @Description 删除成功后校验持久化数据; 主要为了避免在未提交的事务中进行对操作结果的非预期判断.
     * · 完整业务流程的一部分
     *
     * @param roleResource                      [（安全认证）角色 ←→ 资源]    {@link SecurityRoleResource}
     * @param operator                          操作者
     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
     *
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean delete(@NotNull SecurityRoleResource roleResource
            , @NotNull SecurityUser operator
            , @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
            throws IllegalArgumentException, BusinessAtomicException;

//    /**
//     * 删除指定的[（安全认证）角色]关联的所有[（安全认证）角色 ←→ 资源]关联关系
//     *
//     * @param role      [（安全认证）角色], 必须合法且已持久化.  {@link SecurityRole}
//     * @param operator  操作者
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    boolean delete(@NotNull SecurityRole role, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;
//
//    /**
//     * 删除指定的[（安全认证）资源]关联的所有[（安全认证）角色 ←→ 资源]关联关系
//     *
//     * @param resource      [（安全认证）资源], 必须合法且已持久化.  {@link SecurityResource}
//     * @param operator      操作者
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    boolean delete(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;

//    /**
//     * 删除[（安全认证）角色 ←→ 资源]关联关系
//     *
//     * @param role      [（安全认证）角色], 必须合法且已持久化.  {@link SecurityRole}
//     * @param resource  [（安全认证）资源], 必须合法且已持久化.  {@link SecurityResource}
//     * @param operator  操作者
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    boolean delete(@NotNull SecurityRole role
//            , @NotNull SecurityResource resource
//            , @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;

}
