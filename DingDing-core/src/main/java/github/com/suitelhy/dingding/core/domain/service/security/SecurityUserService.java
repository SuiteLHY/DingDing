//package github.com.suitelhy.dingding.core.domain.service.security;
//
//import github.com.suitelhy.dingding.core.domain.entity.security.*;
//import github.com.suitelhy.dingding.core.domain.repository.security.SecurityUserRepository;
//import github.com.suitelhy.dingding.core.domain.service.LogService;
//import github.com.suitelhy.dingding.core.domain.service.UserAccountOperationInfoService;
//import github.com.suitelhy.dingding.core.domain.service.security.impl.SecurityUserServiceImpl;
//import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityService;
//import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
//import org.springframework.data.domain.Page;
//import org.springframework.transaction.annotation.Isolation;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.validation.constraints.NotNull;
//
///**
// * (安全) 用户 - 业务
// *
// * @Description (安全) 用户 - 业务接口.
// *
// * @see SecurityUser
// * @see SecurityUserRepository
// * @see SecurityUserServiceImpl
// * @see SecurityRoleService
// * @see SecurityUserRoleService
// * @see UserAccountOperationInfoService
// */
//@Transactional(isolation = Isolation.SERIALIZABLE
//        , propagation = Propagation.REQUIRED
//        , readOnly = false
//        , rollbackFor = Exception.class
//        , timeout = 15)
//public interface SecurityUserService
//        extends EntityService {
//
//    /**
//     * 判断存在
//     *
//     * @param userId    {@linkplain SecurityUser.Validator#userId(String)} [用户 ID]}
//     *
//     * @return {@linkplain Boolean#TYPE 判断结果}
//     *
//     * @throws IllegalArgumentException
//     */
//    @Transactional(isolation = Isolation.READ_COMMITTED
//            , propagation = Propagation.SUPPORTS
//            , readOnly = true
//            , rollbackFor = Exception.class
//            , timeout = 10)
//    boolean existByUserId(@NotNull String userId)
//            throws IllegalArgumentException;
//
//    /**
//     * 判断存在
//     *
//     * @param username  {@linkplain SecurityUser.Validator#username(String) 用户名称}
//     *
//     * @return {@linkplain Boolean#TYPE 判断结果}
//     *
//     * @throws IllegalArgumentException
//     */
//    @Transactional(isolation = Isolation.READ_COMMITTED
//            , propagation = Propagation.SUPPORTS
//            , readOnly = true
//            , rollbackFor = Exception.class
//            , timeout = 10)
//    boolean existByUsername(@NotNull String username)
//            throws IllegalArgumentException;
//
//    /**
//     * 是否具有管理员权限
//     *
//     * @param username  {@linkplain SecurityUser.Validator#username(String) 用户名称}
//     *
//     * @return {@linkplain Boolean#TYPE 判断结果}
//     */
//    @Transactional(isolation = Isolation.READ_COMMITTED
//            , propagation = Propagation.SUPPORTS
//            , readOnly = true
//            , rollbackFor = Exception.class
//            , timeout = 10)
//    boolean existAdminPermission(@NotNull String username)
//            throws IllegalArgumentException;
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
//    @NotNull Page<SecurityUser> selectAll(int pageIndex, int pageSize)
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
//     * @param userId    {@linkplain SecurityUser.Validator#userId(String) [用户 ID]}
//     *
//     * @return {@link SecurityUser}
//     */
//    @Transactional(isolation = Isolation.READ_COMMITTED
//            , propagation = Propagation.SUPPORTS
//            , readOnly = true
//            , rollbackFor = Exception.class
//            , timeout = 10)
//    @NotNull SecurityUser selectByUserId(@NotNull String userId)
//            throws IllegalArgumentException;
//
//    /**
//     * 查询
//     *
//     * @param username  {@linkplain SecurityUser.Validator#username(String) 用户名称}
//     *
//     * @return {@link SecurityUser}
//     */
//    @Transactional(isolation = Isolation.READ_COMMITTED
//            , propagation = Propagation.SUPPORTS
//            , readOnly = true
//            , rollbackFor = Exception.class
//            , timeout = 10)
//    @NotNull SecurityUser selectByUsername(@NotNull String username)
//            throws IllegalArgumentException;
//
//    /**
//     * 新增一个用户
//     *
//     * @Description
//     * · 完整业务流程的一部分.
//     *
//     * @param user      {@linkplain SecurityUser [（安全认证）用户]}
//     * @param operator  {@linkplain SecurityUser 操作者}
//     *
//     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在相同的有效数据}
//     */
//    boolean insert(@NotNull SecurityUser user, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;
//
//    /**
//     * 更新指定的用户
//     *
//     * @param user      {@linkplain SecurityUser [（安全认证）用户]}
//     * @param operator  {@linkplain SecurityUser 操作者}
//     *
//     * @return {@linkplain Boolean#TYPE 操作是否成功}
//     */
//    boolean update(@NotNull SecurityUser user, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;
//
//    /**
//     * 删除指定的用户
//     *
//     * @Description 删除成功后校验持久化数据; 主要为了避免在未提交的事务中进行对操作结果的非预期判断.
//     * · 完整业务流程的一部分.
//     *
//     * @param user      {@linkplain SecurityUser [（安全认证）用户]}
//     * @param operator  {@linkplain SecurityUser 操作者}
//     *
//     * @return {@linkplain Boolean#TYPE 操作是否成功 / 指定的用户是否已被删除}
//     */
//    boolean delete(@NotNull SecurityUser user, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;
//
//}
