package github.com.suitelhy.dingding.user.service.provider.domain.service;

import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityService;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.security.service.api.domain.entity.User;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUserRole;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserPersonInfo;
import github.com.suitelhy.dingding.user.service.provider.domain.service.impl.UserPersonInfoServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * 用户 -> 账户操作记录 - 业务接口
 *
 * @see UserPersonInfo
 * @see User
 * @see UserPersonInfoServiceImpl
 * @see UserAccountOperationInfoService
 * @see LogService
 */
@Transactional(isolation = Isolation.SERIALIZABLE
        , propagation = Propagation.REQUIRED
        , readOnly = false
        , rollbackFor = Exception.class
        , timeout = 15)
public interface UserPersonInfoService
        extends EntityService {

    /**
     * 查询记录列表
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     *
     * @return {@link Page}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    @NotNull Page<UserPersonInfo> selectAll(int pageIndex, int pageSize);

    /**
     * 查询记录列表 - 分页 - 总页数
     *
     * @param pageSize 分页 - 每页容量
     *
     * @return 分页 - 总页数
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    @NotNull Long selectCount(int pageSize);

    /**
     * 查询指定的记录
     *
     * @param id    {@linkplain UserPersonInfo.Validator#id(String) 数据 ID}
     *
     * @return {@link UserPersonInfo}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    @NotNull UserPersonInfo selectById(@NotNull String id);

    /**
     * 查询指定的记录
     *
     * @param username  {@linkplain User.Validator#username(String) 用户名称}
     *
     * @return {@link UserPersonInfo}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    @NotNull UserPersonInfo selectByUsername(@NotNull String username);

    /**
     * 查询指定的记录
     *
     * @param nickname 用户 - 昵称 {@link UserPersonInfo.Validator#nickname(String)}
     *
     * @return {@link UserPersonInfo}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    @NotNull UserPersonInfo selectByNickname(@NotNull String nickname);

    //===== 添加操作 =====//

    /**
     * 新增一条记录
     *
     * @param user      {@linkplain UserPersonInfo [用户 -> 个人信息]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    boolean insert(@NotNull UserPersonInfo user, @NotNull SecurityUser operator)
            throws BusinessAtomicException;

    //===== 修改操作 =====//

    /**
     * 更新指定的记录
     *
     * @param user      {@linkplain UserPersonInfo [用户 -> 个人信息]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    boolean update(@NotNull UserPersonInfo user, @NotNull SecurityUser operator)
            throws BusinessAtomicException;

    /**
     * 更新指定的记录
     *
     * @param old_userPersonInfo                {@linkplain UserPersonInfo 被修改的[用户 -> 个人信息]}
     * @param new_userPersonInfo_data           {@linkplain Map 被修改的[用户 -> 个人信息] <- 需要更新的数据}
     * · 数据结构:
     * {
     *  "nickname": [用户 - 昵称],
     *  "age": [用户 - 年龄],
     *  "faceImage": [用户 - 头像],
     *  "introduction": [用户 - 简介],
     *  "sex": [用户 - 性别]
     * }
     * @param operator                          {@linkplain SecurityUser 操作者}
     * @param operator_UserAccountOperationInfo {@linkplain UserAccountOperationInfo [操作者 - 账户操作基础记录]}
     * @param operator_userRole                 {@linkplain SecurityUserRole [操作者 - （安全认证）用户 ←→ 角色]}
     * @param operator_role                     {@linkplain SecurityRole [操作者 - （安全认证）角色]}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    boolean update(@NotNull UserPersonInfo old_userPersonInfo, @NotNull Map<String, Object> new_userPersonInfo_data, @NotNull SecurityUser operator
            , @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo, @NotNull SecurityUserRole operator_userRole, @NotNull SecurityRole operator_role)
            throws IllegalArgumentException, BusinessAtomicException;

    //===== 删除操作 =====//

    /**
     * 删除指定的记录
     *
     * @param user      {@linkplain UserPersonInfo [用户 -> 个人信息]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    boolean delete(@NotNull UserPersonInfo user, @NotNull SecurityUser operator)
            throws BusinessAtomicException;

    /**
     * 删除指定的记录
     *
     * @Description 删除成功后校验持久化数据; 主要是避免在未提交的事务中进行对操作结果的非预期判断.
     *
     * @param user      {@linkplain UserPersonInfo [用户 -> 个人信息]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    boolean deleteAndValidate(@NotNull UserPersonInfo user, @NotNull SecurityUser operator)
            throws BusinessAtomicException;

}
