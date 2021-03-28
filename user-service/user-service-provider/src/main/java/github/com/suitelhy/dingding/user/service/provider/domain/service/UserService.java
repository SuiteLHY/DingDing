package github.com.suitelhy.dingding.user.service.provider.domain.service;

import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityService;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.security.service.api.domain.entity.User;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUserRole;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.user.service.provider.domain.service.impl.UserServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * 用户 - 业务接口
 *
 * @see User
 * @see UserAccountOperationInfo
 * @see UserServiceImpl
 * @see UserPersonInfoService
 * @see UserAccountOperationInfoService
 * @see LogService
 */
@Transactional(isolation = Isolation.SERIALIZABLE
        , propagation = Propagation.REQUIRED
        , readOnly = false
        , rollbackFor = Exception.class
        , timeout = 15)
public interface UserService
        extends EntityService {

    //===== 查询操作 =====//

    /**
     * 判断存在 -> 指定的用户
     *
     * @param username {@link User.Validator#username(String)}
     *
     * @return {@link Boolean#TYPE}
     *
     * @throws IllegalArgumentException
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    boolean existUserByUsername(@NotNull String username)
            throws IllegalArgumentException;

    /**
     * 查询用户列表
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     *
     * @return {@link Page}
     *
     * @throws IllegalArgumentException
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    @NotNull Page<User> selectAll(int pageIndex, int pageSize)
            throws IllegalArgumentException;

    /**
     * 查询用户列表 - 分页 - 总页数
     *
     * @param pageSize 分页 - 每页容量
     *
     * @return 分页 - 总页数
     *
     * @throws IllegalArgumentException
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    @NotNull Long selectCount(int pageSize)
            throws IllegalArgumentException;

    /**
     * 查询指定的用户
     *
     * @param userid {@link User.Validator#userid(String)}
     *
     * @return {@link User}
     *
     * @throws IllegalArgumentException
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    @NotNull User selectUserByUserid(@NotNull String userid)
            throws IllegalArgumentException;

    /**
     * 查询指定的用户
     *
     * @param username {@link User.Validator#username(String)}
     *
     * @return {@link User}
     *
     * @throws IllegalArgumentException
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    @NotNull User selectUserByUsername(@NotNull String username)
            throws IllegalArgumentException;

//    /**
//     * 查询指定的用户 -> 账户操作记录
//     *
//     * @param username  {@link UserAccountOperationInfo.Validator#username(String)}
//     *
//     * @return {@link UserAccountOperationInfo}
//     *
//     * @see UserAccountOperationInfo
//     * @see UserAccountOperationInfoService
//     */
//    UserAccountOperationInfo selectUserAccountOperationInfo(@NotNull String username)
//            throws IllegalArgumentException;

//    /**
//     * 查询指定的用户 -> 个人信息
//     *
//     * @param username  {@link UserPersonInfo.Validator#username(String)}
//     *
//     * @return {@link UserPersonInfo}
//     *
//     * @see UserPersonInfo
//     * @see UserPersonInfoService
//     */
//    UserPersonInfo selectUserPersonInfo(@NotNull String username)
//            throws IllegalArgumentException;

    //===== 添加操作 =====//

//    /**
//     * 新增一个用户
//     *
//     * @param user                      预期新增的用户 -> 用户基础信息               {@link User}
//     * @param userAccountOperationInfo  预期新增的用户 -> [用户 -> 账户操作基础记录]  {@link UserAccountOperationInfo}
//     * @param userPersonInfo            [用户 -> 个人信息]                        {@link UserPersonInfo}
//     * @param operator                  操作者
//     *
//     * @return 操作是否成功
//     *
//     * @see UserAccountOperationInfoService
//     *
//     * @throws IllegalArgumentException
//     * @throws BusinessAtomicException
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    boolean insert(@NotNull User user, @NotNull UserAccountOperationInfo userAccountOperationInfo, @NotNull UserPersonInfo userPersonInfo
//            , @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 新增一个用户
     *
     * @Description 完整业务的一部分.
     *
     * @param user                              预期新增的用户 -> 用户基础信息
     * @param operator                          操作者
     * @param operator_UserAccountOperationInfo [操作者 - 账户操作基础记录]
     *
     * @return 操作是否成功
     *
     * @throws IllegalArgumentException
     * @throws BusinessAtomicException
     */
    boolean insert(@NotNull User user, @NotNull SecurityUser operator, @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo)
            throws IllegalArgumentException, BusinessAtomicException;

    //===== 修改操作 =====//

    /**
     * 更新指定的用户
     *
     * @param user                              被修改的用户
     * @param operator                          操作者
     * @param operator_UserAccountOperationInfo [操作者 - 账户操作基础记录]
     *
     * @return 操作是否成功
     */
    boolean update(@NotNull User user, @NotNull SecurityUser operator, @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 更新指定的用户
     *
     * @param old_user                          {@linkplain User 被修改的用户 <- 原始版本业务全量数据}
     * @param new_user_data                     {@linkplain Map 被修改的用户 <- 需要更新的数据}
     * · 数据结构:
     * {
     *  “password”: [用户密码_明文],
     *  “status”: [账号状态]
     * }
     * @param operator                          {@linkplain SecurityUser 操作者}
     * @param operator_UserAccountOperationInfo {@linkplain UserAccountOperationInfo [操作者 - 账户操作基础记录]}
     * @param operator_userRole                 {@linkplain SecurityUserRole [操作者 - （安全认证）用户 ←→ 角色]}
     * @param operator_role                     {@linkplain SecurityRole [操作者 - （安全认证）角色]}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    boolean update(@NotNull User old_user, @NotNull Map<String, Object> new_user_data, @NotNull SecurityUser operator
            , @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo, @NotNull SecurityUserRole operator_userRole, @NotNull SecurityRole operator_role)
            throws IllegalArgumentException, BusinessAtomicException;

    //===== 删除操作 =====//

    /**
     * 删除指定的用户
     *
     * @param user                              被删除的用户
     * @param operator                          操作者 (与{@param user}一致, 或拥有足够高的权限)
     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
     *
     * @return 操作是否成功
     */
    boolean delete(@NotNull User user
            , @NotNull SecurityUser operator
            , @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 删除指定的用户
     *
     * @Description 删除成功后校验持久化数据; 主要是避免在未提交的事务中进行对操作结果的非预期判断.
     *
     * @param user                              被删除的用户
     * @param operator                          操作者 (与{@param user}一致, 或拥有足够高的权限)
     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
     *
     * @return 操作是否成功
     */
    boolean deleteAndValidate(@NotNull User user
            , @NotNull SecurityUser operator
            , @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
            throws IllegalArgumentException, BusinessAtomicException;

}
