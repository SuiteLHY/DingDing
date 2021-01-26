package github.com.suitelhy.dingding.core.domain.service.security;

import github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResource;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.core.domain.repository.security.SecurityRoleRepository;
import github.com.suitelhy.dingding.core.domain.service.LogService;
import github.com.suitelhy.dingding.core.domain.service.UserAccountOperationInfoService;
import github.com.suitelhy.dingding.core.domain.service.security.impl.SecurityRoleServiceImpl;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityService;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.security.Security;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
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
 * @see SecurityRole
 * @see SecurityRoleRepository
 *
 * @see SecurityRoleServiceImpl
 *
 * @see LogService
 */
@Transactional(isolation = Isolation.READ_COMMITTED
        , propagation = Propagation.REQUIRED
        , readOnly = true
        , rollbackFor = Exception.class
        , timeout = 15)
public interface SecurityRoleService
        extends EntityService {

    //===== 查询数据业务操作 =====//

    /**
     * 判断存在
     *
     * @param code  角色编码    {@link SecurityRole}
     *
     * @return {@link java.lang.Boolean}
     */
    /*@Transactional*/
    @NotNull Boolean existsByCode(@NotNull String code)
            throws IllegalArgumentException;

    /**
     * 查询所有
     *
     * @param pageIndex     分页索引, 从0开始
     * @param pageSize      分页 - 每页容量
     *
     * @return              {@link Page}
     */
    @NotNull Page<SecurityRole> selectAll(int pageIndex, int pageSize)
            throws IllegalArgumentException;

    /**
     * 查询总页数
     *
     * @Description 查询数据列表 - 分页 - 总页数
     *
     * @param pageSize  [分页 - 每页容量]
     *
     * @return [分页 - 总页数]
     */
    @NotNull Long selectCount(int pageSize)
            throws IllegalArgumentException;

    /**
     * 查询指定的角色
     *
     * @param code  角色编码    {@link SecurityRole}
     *
     * @return {@link SecurityRole}
     */
    @NotNull SecurityRole selectRoleByCode(@NotNull String code)
            throws IllegalArgumentException;

//    /**
//     * 查询 (关联的) 资源
//     *
//     * @param code  角色编码    {@link SecurityRole}
//     *
//     * @return 资源的数据  {@link SecurityResource}
//     *
//     * @see SecurityResource
//     */
//    @Nullable
//    List<Map<String, Object>> selectResourceByCode(@NotNull String code)
//            throws IllegalArgumentException;

    //===== 添改数据业务操作 =====//

    /**
     * 新增一个角色
     *
     * @Description
     * · 完整的业务流程.
     *
     * @param roleVo    [安全模块 VO -> 角色].    {@link Security.RoleVo}
     *
     * @return 操作是否成功 / 是否已存在相同的有效数据
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean insert(@NotNull Security.RoleVo roleVo)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 新增一个角色
     *
     * @Description
     * · 完整的业务流程.
     *
     * @param role                              [（安全认证）角色], 必须合法.   {@link SecurityRole}
     * @param operator                          操作者
     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
     *
     * @return 操作是否成功 / 是否已存在相同的有效数据
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean insert(@NotNull SecurityRole role
            , @NotNull SecurityUser operator
            , @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
            throws IllegalArgumentException, BusinessAtomicException;

//    /**
//     * 新增 角色 - 资源 关联
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
//    default boolean insertResource(@NotNull SecurityRole role, @NotNull SecurityResource resource, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if (null == role || null == resource) {
//            return false;
//        }
//        /*System.out.println("===== insertResource(SecurityRole, SecurityResource) =====");*/
//
//        Set<SecurityRole> roles = new HashSet<>(1);
//        roles.add(role);
//
//        Set<SecurityResource> resources = new HashSet<>(1);
//        resources.add(resource);
//
//        return insertResource(roles, resources, operator);
//    }
//
//    /**
//     * 新增 角色 - 资源 关联
//     *
//     * @param role      [（安全认证）角色], 必须合法且已持久化.       {@link SecurityRole}
//     * @param resources [（安全认证）资源], 必须全部合法且已持久化.    {@link SecurityResource}
//     * @param operator  操作者
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    default boolean insertResource(@NotNull SecurityRole role, @NotNull Set<SecurityResource> resources, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if (null == role
//                || (null == resources || resources.isEmpty())) {
//            return false;
//        }
//        /*System.out.println("===== insertResource(SecurityRole, Set<SecurityResource>) =====");*/
//
//        Set<SecurityRole> roles = new HashSet<>(1);
//        roles.add(role);
//
//        return insertResource(roles, resources, operator);
//    }
//
//    /**
//     * 新增 角色 - 资源 关联
//     *
//     * @param roles     [（安全认证）角色], 必须全部合法且已持久化.   {@link SecurityRole}
//     * @param resource  [（安全认证）资源], 必须合法且已持久化.      {@link SecurityResource}
//     * @param operator  操作者
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , timeout = 15)
//    default boolean insertResource(@NotNull Set<SecurityRole> roles, @NotNull SecurityResource resource, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if ((null == roles || roles.isEmpty())
//                || null == resource) {
//            return false;
//        }
//        /*System.out.println("===== insertResource(Set<SecurityRole>, SecurityResource) =====");*/
//
//        Set<SecurityResource> resources = new HashSet<>(1);
//        resources.add(resource);
//
//        return insertResource(roles, resources, operator);
//    }
//
//    /**
//     * 新增 角色 - 资源 关联
//     *
//     * @param roles         [（安全认证）角色], 必须全部合法且已持久化.    {@link SecurityRole}
//     * @param resources     [（安全认证）资源], 必须全部合法且已持久化.    {@link SecurityResource}
//     * @param operator      操作者
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , timeout = 15)
//    boolean insertResource(@NotNull Set<SecurityRole> roles, @NotNull Set<SecurityResource> resources, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 更新指定的角色
     *
     * @Description 全量更新.
     * · 完整的业务流程.
     *
     * @param role                              [（安全认证）角色]  {@link SecurityRole}
     * @param operator                          操作者
     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
     *
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    boolean update(@NotNull SecurityRole role
            , @NotNull SecurityUser operator
            , @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 更新指定的角色
     *
     * @Description 增量更新.
     * · 完整的业务流程.
     *
     * @param old_role                          原始版本业务全量数据.
     * @param new_role_data                     需要更新的数据.
     * · 数据格式:
     * {
     *    role_name : [角色名称],
     *    role_description : [角色描述]
     * }
     * @param operator                          操作者
     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
     *
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    boolean update(@NotNull SecurityRole old_role
            , @NotNull Map<String, Object> new_role_data
            , @NotNull SecurityUser operator
            , @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
            throws IllegalArgumentException, BusinessAtomicException;

    //===== 删除数据业务操作 =====//

    /**
     * 删除指定的角色
     *
     * @Description 删除成功后校验持久化数据; 主要为了避免在未提交的事务中进行对操作结果的非预期判断.
     * · 完整业务流程的一部分.
     *
     * @param role                              [（安全认证）角色]  {@link SecurityRole}
     * @param operator                          操作者
     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
     *
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    boolean delete(@NotNull SecurityRole role
            , @NotNull SecurityUser operator
            , @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
            throws IllegalArgumentException, BusinessAtomicException;

//    /**
//     * 删除 角色 - 资源 关联
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
//    default boolean deleteResource(@NotNull SecurityRole role, @NotNull SecurityResource resource, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if (null == role || role.isEmpty()) {
//            //-- 非法输入: 角色编码
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[（安全认证）角色]"
//                    , role
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == resource || resource.isEmpty()) {
//            //-- 非法输入: [（安全认证）资源]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[（安全认证）资源]"
//                    , resource
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        Set<SecurityRole> roles = new HashSet<>(1);
//        roles.add(role);
//
//        Set<SecurityResource> resources = new HashSet<>(1);
//        resources.add(resource);
//
//        return deleteResource(roles, resources, operator);
//    }
//
//    /**
//     * 删除 角色 - 资源 关联
//     *
//     * @param role          [（安全认证）角色], 必须合法且已持久化.       {@link SecurityRole}
//     * @param resources     [（安全认证）资源], 必须全部合法且已持久化.    {@link SecurityResource}
//     * @param operator      操作者
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    default boolean deleteResource(@NotNull SecurityRole role, @NotNull Set<SecurityResource> resources, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if (null == role || role.isEmpty()) {
//            //-- 非法输入: 角色编码
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[（安全认证）角色]"
//                    , role
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == resources || resources.isEmpty()) {
//            //-- 非法输入: [（安全认证）资源]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[（安全认证）资源]"
//                    , resources
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        Set<SecurityRole> roles = new HashSet<>(1);
//        roles.add(role);
//
//        return deleteResource(roles, resources, operator);
//    }
//
//    /**
//     * 删除 角色 - 资源 关联
//     *
//     * @param roles     [（安全认证）角色], 必须全部合法且已持久化.   {@link SecurityRole}
//     * @param resource  [（安全认证）资源], 必须合法且已持久化.      {@link SecurityResource}
//     * @param operator  操作者
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    default boolean deleteResource(@NotNull Set<SecurityRole> roles, @NotNull SecurityResource resource, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if (null == roles || roles.isEmpty()) {
//            //-- 非法输入: 角色编码
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[（安全认证）角色]"
//                    , roles
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == resource || resource.isEmpty()) {
//            //-- 非法输入: [（安全认证）资源]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[（安全认证）资源]"
//                    , resource
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        Set<SecurityResource> resources = new HashSet<>(1);
//        resources.add(resource);
//
//        return deleteResource(roles, resources, operator);
//    }
//
//    /**
//     * 删除 角色 - 资源 关联
//     *
//     * @param roles         [（安全认证）角色], 必须全部合法且已持久化.    {@link SecurityRole}
//     * @param resources     [（安全认证）资源], 必须全部合法且已持久化.    {@link SecurityResource}
//     * @param operator      操作者
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    boolean deleteResource(@NotNull Set<SecurityRole> roles, @NotNull Set<SecurityResource> resources, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;

}
