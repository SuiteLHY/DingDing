//package github.com.suitelhy.dingding.core.domain.service.security;
//
//import github.com.suitelhy.dingding.core.domain.entity.security.*;
//import github.com.suitelhy.dingding.core.domain.repository.security.SecurityUserRoleRepository;
//import github.com.suitelhy.dingding.core.domain.service.LogService;
//import github.com.suitelhy.dingding.core.domain.service.UserAccountOperationInfoService;
//import github.com.suitelhy.dingding.core.domain.service.security.impl.SecurityUserRoleServiceImpl;
//import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityService;
//import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
//import org.springframework.data.domain.Page;
//import org.springframework.transaction.annotation.Isolation;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.validation.constraints.NotNull;
//import java.util.List;
//
///**
// * （安全认证）用户 ←→ 角色
// *
// * @Description [（安全认证）用户 ←→ 角色]关联关系 - 业务接口.
// *
// * @see SecurityUserRole
// * @see SecurityUserRoleRepository
// * @see SecurityUserRoleServiceImpl
// * @see UserAccountOperationInfoService
// */
//@Transactional(isolation = Isolation.SERIALIZABLE
//        , propagation = Propagation.REQUIRED
//        , readOnly = false
//        , rollbackFor = Exception.class
//        , timeout = 15)
//public interface SecurityUserRoleService
//        extends EntityService {
//
////    /**
////     * 判断是否存在 (关联的) [（安全认证）角色]
////     *
////     * @param username  用户名称    {@link SecurityUserRole.Validator#username(String)}
////     *
////     * @return {@link boolean}
////     */
////    boolean existRoleByUsername(@NotNull String username)
////            throws IllegalArgumentException;
//
//    /**
//     * 查询所有
//     *
//     * @param pageIndex 分页索引, 从0开始
//     * @param pageSize  分页 - 每页容量
//     *
//     * @return {@link Page}
//     */
//    @Transactional(isolation = Isolation.READ_COMMITTED
//            , propagation = Propagation.SUPPORTS
//            , readOnly = true
//            , rollbackFor = Exception.class
//            , timeout = 10)
//    @NotNull Page<SecurityUserRole> selectAll(int pageIndex, int pageSize)
//            throws IllegalArgumentException;
//
//    /**
//     * 查询总页数
//     *
//     * @Description 查询数据列表 - 分页 - 总页数
//     *
//     * @param pageSize 分页 - 每页容量
//     *
//     * @return 分页 - 总页数
//     */
//    @Transactional(isolation = Isolation.READ_COMMITTED
//            , propagation = Propagation.SUPPORTS
//            , readOnly = true
//            , rollbackFor = Exception.class
//            , timeout = 10)
//    @NotNull Long selectCount(int pageSize)
//            throws IllegalArgumentException;
//
//    /**
//     * 查询
//     *
//     * @param username 用户名称    {@link SecurityUserRole.Validator#username(String)}
//     *
//     * @return {@link SecurityUserRole}
//     */
//    @Transactional(isolation = Isolation.READ_COMMITTED
//            , propagation = Propagation.SUPPORTS
//            , readOnly = true
//            , rollbackFor = Exception.class
//            , timeout = 10)
//    @NotNull List<SecurityUserRole> selectByUsername(@NotNull String username)
//            throws IllegalArgumentException;
//
//    /**
//     * 查询
//     *
//     * @param roleCode  {@linkplain SecurityUserRole.Validator#roleCode(String) 角色编码}
//     *
//     * @return {@link SecurityUserRole}
//     */
//    @Transactional(isolation = Isolation.READ_COMMITTED
//            , propagation = Propagation.SUPPORTS
//            , readOnly = true
//            , rollbackFor = Exception.class
//            , timeout = 10)
//    @NotNull List<SecurityUserRole> selectByRoleCode(@NotNull String roleCode)
//            throws IllegalArgumentException;
//
//    /**
//     * 查询
//     *
//     * @param username  {@linkplain SecurityUserRole.Validator#username(String) 用户名称}
//     * @param roleCode  {@linkplain SecurityUserRole.Validator#roleCode(String) 角色编码}
//     *
//     * @return {@link SecurityUserRole}
//     */
//    @Transactional(isolation = Isolation.READ_COMMITTED
//            , propagation = Propagation.SUPPORTS
//            , readOnly = true
//            , rollbackFor = Exception.class
//            , timeout = 10)
//    @NotNull SecurityUserRole selectByUsernameAndRoleCode(@NotNull String username, @NotNull String roleCode)
//            throws IllegalArgumentException;
//
////    /**
////     * 查询 (关联的) [（安全认证）角色]
////     *
////     * @param username  用户名称    {@link SecurityUserRole.Validator#username(String)}
////     *
////     * @return {@link SecurityRole}
////     */
////    @NotNull List<SecurityRole> selectRoleByUsername(@NotNull String username)
////            throws IllegalArgumentException;
//
//    /**
//     * 新增一个[（安全认证）用户 ←→ 角色]关联关系
//     *
//     * @param userRole  {@linkplain SecurityUserRole [（安全认证）用户 ←→ 角色]}
//     * @param operator  {@linkplain SecurityUser 操作者}
//     *
//     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在相同的有效数据}
//     */
//    boolean insert(@NotNull SecurityUserRole userRole, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;
//
//    /**
//     * 删除指定的[（安全认证）用户 ←→ 角色]关联关系
//     *
//     * @Description 删除成功后校验持久化数据; 主要为了避免在未提交的事务中进行对操作结果的非预期判断.
//     *
//     * @param userRole  {@linkplain SecurityUserRole [（安全认证）用户 ←→ 角色]}
//     * @param operator  {@linkplain SecurityUser 操作者}
//     *
//     * @return {@linkplain Boolean#TYPE 操作是否成功 / 指定数据是否已被删除}
//     */
//    boolean delete(@NotNull SecurityUserRole userRole, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;
//
//}
