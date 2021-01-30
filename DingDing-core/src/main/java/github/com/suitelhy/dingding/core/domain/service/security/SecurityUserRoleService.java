package github.com.suitelhy.dingding.core.domain.service.security;

import github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.core.domain.entity.security.*;
import github.com.suitelhy.dingding.core.domain.repository.security.SecurityUserRoleRepository;
import github.com.suitelhy.dingding.core.domain.service.LogService;
import github.com.suitelhy.dingding.core.domain.service.UserAccountOperationInfoService;
import github.com.suitelhy.dingding.core.domain.service.security.impl.SecurityUserRoleServiceImpl;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityService;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.security.Security;
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
 * （安全认证）用户 ←→ 角色
 *
 * @Description [（安全认证）用户 ←→ 角色]关联关系 - 业务接口.
 * @see SecurityUserRole
 * @see SecurityUserRoleRepository
 * @see SecurityUserRoleServiceImpl
 * @see UserAccountOperationInfoService
 * @see LogService
 */
@Transactional(isolation = Isolation.READ_COMMITTED
        , propagation = Propagation.REQUIRED
        , readOnly = true
        , timeout = 15)
public interface SecurityUserRoleService
        extends EntityService {

//    /**
//     * 判断是否存在 (关联的) [（安全认证）角色]
//     *
//     * @param username  用户名称    {@link SecurityUserRole.Validator#username(String)}
//     *
//     * @return {@link boolean}
//     */
//    boolean existRoleByUsername(@NotNull String username)
//            throws IllegalArgumentException;

    /**
     * 查询所有
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     * @return {@link Page}
     */
    Page<SecurityUserRole> selectAll(int pageIndex, int pageSize)
            throws IllegalArgumentException;

    /**
     * 查询总页数
     *
     * @param pageSize 分页 - 每页容量
     * @return 分页 - 总页数
     * @Description 查询数据列表 - 分页 - 总页数
     */
    @NotNull
    Long selectCount(int pageSize)
            throws IllegalArgumentException;

    /**
     * 查询
     *
     * @param username 用户名称    {@link SecurityUserRole.Validator#username(String)}
     * @return {@link SecurityUserRole}
     */
    @NotNull
    List<SecurityUserRole> selectByUsername(@NotNull String username)
            throws IllegalArgumentException;

    /**
     * 查询
     *
     * @param roleCode 角色编码    {@link SecurityUserRole.Validator#roleCode(String)}
     * @return {@link SecurityUserRole}
     */
    @NotNull
    List<SecurityUserRole> selectByRoleCode(@NotNull String roleCode)
            throws IllegalArgumentException;

    /**
     * 查询
     *
     * @param username 用户名称    {@link SecurityUserRole.Validator#username(String)}
     * @param roleCode 角色编码    {@link SecurityUserRole.Validator#roleCode(String)}
     * @return {@link SecurityUserRole}
     */
    @NotNull
    SecurityUserRole selectByUsernameAndRoleCode(@NotNull String username, @NotNull String roleCode)
            throws IllegalArgumentException;

//    /**
//     * 查询 (关联的) [（安全认证）角色]
//     *
//     * @param username  用户名称    {@link SecurityUserRole.Validator#username(String)}
//     *
//     * @return {@link SecurityRole}
//     */
//    @NotNull List<SecurityRole> selectRoleByUsername(@NotNull String username)
//            throws IllegalArgumentException;

    /**
     * 新增一个[（安全认证）用户 ←→ 角色]关联关系
     *
     * @param userRole                          [（安全认证）用户 ←→ 角色]    {@link SecurityUserRole}
     * @param operator                          操作者
     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
     * @return 操作是否成功 / 是否已存在相同的有效数据
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean insert(@NotNull SecurityUserRole userRole
            , @NotNull SecurityUser operator
            , @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
            throws IllegalArgumentException, BusinessAtomicException;

//    /**
//     * 新增一个[（安全认证）用户 ←→ 角色]关联关系
//     *
//     * @param user      （安全认证）用户
//     * @param role      （安全认证）角色
//     * @param operator  操作者
//     *
//     * @return 操作是否成功 / 是否已存在相同的有效数据
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    default boolean insertUserRoleRelationship(@NotNull SecurityUser user, @NotNull SecurityRole role, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if (null == user || user.isEmpty()) {
//            //-- 非法输入: （安全认证）用户
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "（安全认证）用户"
//                    , user
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == role || role.isEmpty()) {
//            //-- 非法输入: （安全认证）角色
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "（安全认证）角色"
//                    , role
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        Set<SecurityRole> roles = new HashSet<>(1);
//        roles.add(role);
//
//        return insertUserRoleRelationship(user, roles, operator);
//    }
//
//    /**
//     * 新增多个[（安全认证）用户 ←→ 角色]关联关系
//     *
//     * @param user      （安全认证）用户
//     * @param roles     （安全认证）角色
//     * @param operator  操作者
//     *
//     * @return 操作是否成功 / 是否已存在完全相同的有效数据集合
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    boolean insertUserRoleRelationship(@NotNull SecurityUser user, @NotNull Set<SecurityRole> roles, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;
//
//    /**
//     * 新增一个[（安全认证）用户 ←→ 角色]关联关系
//     *
//     * @param user      （安全认证）用户
//     * @param roleVo    [(安全) 用户 -> 角色] {@link Security.RoleVo}, 仅需保证合法性, 不需要保证持久化.
//     * @param operator  操作者
//     *
//     * @return 操作是否成功 / 是否已存在相同的有效数据
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    boolean insertUserRoleRelationship(@NotNull SecurityUser user, @NotNull Security.RoleVo roleVo, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 删除指定的[（安全认证）用户 ←→ 角色]关联关系
     *
     * @param userRole                          [（安全认证）用户 ←→ 角色]    {@link SecurityUserRole}
     * @param operator                          操作者
     * @param operator_userAccountOperationInfo [操作者 -> 账户操作基础记录]
     * @return 操作是否成功
     * @Description 删除成功后校验持久化数据;
     * 主要为了避免在未提交的事务中进行对操作结果的非预期判断.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean delete(@NotNull SecurityUserRole userRole
            , @NotNull SecurityUser operator
            , @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
            throws IllegalArgumentException, BusinessAtomicException;

//    /**
//     * 删除指定的[（安全认证）用户]关联的所有[（安全认证）用户 ←→ 角色]关联关系
//     *
//     * @param user      [（安全认证）用户], 必须合法且已持久化.  {@link SecurityUser}
//     * @param operator  操作者
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    boolean delete(@NotNull SecurityUser user, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;

//    /**
//     * 删除指定的[（安全认证）角色]关联的所有[（安全认证）用户 ←→ 角色]关联关系
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

//    /**
//     * 删除[（安全认证）用户 ←→ 角色]关联关系
//     *
//     * @param user      [（安全认证）用户], 必须合法且已持久化.  {@link SecurityUser}
//     * @param role      [（安全认证）角色], 必须合法且已持久化.  {@link SecurityRole}
//     * @param operator  操作者
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    boolean delete(@NotNull SecurityUser user, @NotNull SecurityRole role, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;

}
