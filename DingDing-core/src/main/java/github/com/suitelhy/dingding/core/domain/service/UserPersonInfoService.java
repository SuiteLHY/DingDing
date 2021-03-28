//package github.com.suitelhy.dingding.core.domain.service;
//
//import github.com.suitelhy.dingding.core.domain.entity.User;
//import github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo;
//import github.com.suitelhy.dingding.core.domain.entity.UserPersonInfo;
//import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser;
//import github.com.suitelhy.dingding.core.domain.repository.UserPersonInfoRepository;
//import github.com.suitelhy.dingding.core.domain.service.impl.UserAccountOperationInfoServiceImpl;
//import github.com.suitelhy.dingding.core.domain.service.impl.UserPersonInfoServiceImpl;
//import github.com.suitelhy.dingding.core.domain.service.impl.UserServiceImpl;
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
// * 用户 -> 账户操作记录 - 业务接口
// *
// * @see UserPersonInfo
// * @see User
// * @see UserPersonInfoRepository
// * @see UserPersonInfoServiceImpl
// * @see UserAccountOperationInfoService
// * @see LogService
// */
//@Transactional(isolation = Isolation.READ_COMMITTED
//        , propagation = Propagation.REQUIRED
//        , readOnly = true
//        , rollbackFor = Exception.class
//        , timeout = 15)
//public interface UserPersonInfoService
//        extends EntityService {
//
//    /**
//     * 查询记录列表
//     *
//     * @param pageIndex 分页索引, 从0开始
//     * @param pageSize  分页 - 每页容量
//     * @return {@link Page}
//     */
//    Page<UserPersonInfo> selectAll(int pageIndex, int pageSize);
//
//    /**
//     * 查询记录列表 - 分页 - 总页数
//     *
//     * @param pageSize 分页 - 每页容量
//     * @return 分页 - 总页数
//     */
//    Long selectCount(int pageSize);
//
//    /**
//     * 查询指定的记录
//     *
//     * @param id 数据 ID   {@link UserPersonInfo.Validator#id(String)}
//     * @return {@link UserPersonInfo}
//     */
//    UserPersonInfo selectById(@NotNull String id);
//
//    /**
//     * 查询指定的记录
//     *
//     * @param username 用户名称    {@link User.Validator#username(String)}
//     * @return {@link UserPersonInfo}
//     */
//    UserPersonInfo selectByUsername(@NotNull String username);
//
//    /**
//     * 查询指定的记录
//     *
//     * @param nickname 用户 - 昵称 {@link UserPersonInfo.Validator#nickname(String)}
//     * @return {@link UserPersonInfo}
//     */
//    UserPersonInfo selectByNickname(@NotNull String nickname);
//
//    /**
//     * 新增一条记录
//     *
//     * @param user     [用户 -> 个人信息]    {@link UserPersonInfo}
//     * @param operator 操作者               {@link SecurityUser}
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    boolean insert(@NotNull UserPersonInfo user, @NotNull SecurityUser operator)
//            throws BusinessAtomicException;
//
//    /**
//     * 更新指定的记录
//     *
//     * @param user     [用户 -> 个人信息]    {@link UserPersonInfo}
//     * @param operator 操作者               {@link SecurityUser}
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    boolean update(@NotNull UserPersonInfo user, @NotNull SecurityUser operator)
//            throws BusinessAtomicException;
//
//    /**
//     * 删除指定的记录
//     *
//     * @param user     [用户 -> 个人信息]    {@link UserPersonInfo}
//     * @param operator 操作者               {@link SecurityUser}
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    boolean delete(@NotNull UserPersonInfo user, @NotNull SecurityUser operator)
//            throws BusinessAtomicException;
//
//    /**
//     * 删除指定的记录
//     *
//     * @param user     [用户 -> 个人信息]    {@link UserPersonInfo}
//     * @param operator 操作者               {@link SecurityUser}
//     * @return 操作是否成功
//     * @Description 删除成功后校验持久化数据; 主要是避免在未提交的事务中进行对操作结果的非预期判断.
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    boolean deleteAndValidate(@NotNull UserPersonInfo user, @NotNull SecurityUser operator)
//            throws BusinessAtomicException;
//
//}
