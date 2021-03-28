//package github.com.suitelhy.dingding.core.domain.service.security;
//
//import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole;
//import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser;
//import github.com.suitelhy.dingding.core.domain.repository.security.SecurityRoleRepository;
//import github.com.suitelhy.dingding.core.domain.service.LogService;
//import github.com.suitelhy.dingding.core.domain.service.security.impl.SecurityRoleServiceImpl;
//import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityService;
//import github.com.suitelhy.dingding.security.service.api.domain.vo.Security;
//import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
//import org.springframework.data.domain.Page;
//import org.springframework.transaction.annotation.Isolation;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.validation.constraints.NotNull;
//import java.util.Map;
//
///**
// * (安全) 角色
// *
// * @Description (安全) 角色 - 业务接口.
// *
// * @see SecurityRole
// * @see SecurityRoleRepository
// * @see SecurityRoleServiceImpl
// */
//@Transactional(isolation = Isolation.SERIALIZABLE
//        , propagation = Propagation.REQUIRED
//        , readOnly = false
//        , rollbackFor = Exception.class
//        , timeout = 15)
//public interface SecurityRoleService
//        extends EntityService {
//
//    //===== 查询数据业务操作 =====//
//
//    /**
//     * 判断存在
//     *
//     * @param code  {@linkplain SecurityRole 角色编码}
//     *
//     * @return {@linkplain Boolean#TYPE 判断结果}
//     */
//    @Transactional(isolation = Isolation.READ_COMMITTED
//            , propagation = Propagation.SUPPORTS
//            , readOnly = true
//            , rollbackFor = Exception.class
//            , timeout = 10)
//    @NotNull Boolean existsByCode(@NotNull String code)
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
//    @NotNull Page<SecurityRole> selectAll(int pageIndex, int pageSize)
//            throws IllegalArgumentException;
//
//    /**
//     * 查询总页数
//     *
//     * @Description 查询数据列表 - 分页 - 总页数
//     *
//     * @param pageSize [分页 - 每页容量]
//     *
//     * @return [分页 - 总页数]
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
//     * 查询指定的角色
//     *
//     * @param code  {@linkplain SecurityRole 角色编码}
//     *
//     * @return {@link SecurityRole}
//     */
//    @Transactional(isolation = Isolation.READ_COMMITTED
//            , propagation = Propagation.SUPPORTS
//            , readOnly = true
//            , rollbackFor = Exception.class
//            , timeout = 10)
//    @NotNull SecurityRole selectRoleByCode(@NotNull String code)
//            throws IllegalArgumentException;
//
//    //===== 添改数据业务操作 =====//
//
//    /**
//     * 新增一个角色
//     *
//     * @Description
//     * · 完整的业务流程.
//     *
//     * @param roleVo    {@linkplain Security.RoleVo [安全模块 VO -> 角色]}
//     *
//     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在相同的有效数据}
//     */
//    boolean insert(@NotNull Security.RoleVo roleVo)
//            throws IllegalArgumentException, BusinessAtomicException;
//
//    /**
//     * 新增一个角色
//     *
//     * @Description
//     * · 完整的业务流程.
//     *
//     * @param role      {@linkplain SecurityRole [（安全认证）角色]}, 必须合法.
//     * @param operator  {@linkplain SecurityUser 操作者}
//     *
//     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在相同的有效数据}
//     */
//    boolean insert(@NotNull SecurityRole role, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;
//
//    /**
//     * 更新指定的角色
//     *
//     * @Description 全量更新.
//     * · 完整的业务流程.
//     *
//     * @param role      {@linkplain SecurityRole [（安全认证）角色]}
//     * @param operator  {@linkplain SecurityUser 操作者}
//     *
//     * @return {@linkplain Boolean#TYPE 操作是否成功}
//     */
//    boolean update(@NotNull SecurityRole role, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;
//
//    /**
//     * 更新指定的角色
//     *
//     * @Description 增量更新.
//     * · 完整的业务流程.
//     *
//     * @param old_role      {@linkplain SecurityRole 原始版本业务全量数据}.
//     * @param new_role_data 需要更新的数据.
//     * · 数据格式:
//     * {
//     *  "role_name" : [角色名称],
//     *  "role_description" : [角色描述]
//     * }
//     * @param operator      {@linkplain SecurityUser 操作者}
//     *
//     * @return {@linkplain Boolean#TYPE 操作是否成功}
//     */
//    boolean update(@NotNull SecurityRole old_role
//            , @NotNull Map<String, Object> new_role_data
//            , @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;
//
//    //===== 删除数据业务操作 =====//
//
//    /**
//     * 删除指定的角色
//     *
//     * @Description 删除成功后校验持久化数据; 主要为了避免在未提交的事务中进行对操作结果的非预期判断.
//     * · 完整业务流程的一部分.
//     *
//     * @param role      {@linkplain SecurityRole [（安全认证）角色]}
//     * @param operator  {@linkplain SecurityUser 操作者}
//     *
//     * @return {@linkplain Boolean#TYPE 操作是否成功}
//     */
//    boolean delete(@NotNull SecurityRole role, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;
//
//}
