package github.com.suitelhy.dingding.core.domain.service.impl;

import github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.core.domain.entity.UserPersonInfo;
import github.com.suitelhy.dingding.core.domain.service.UserAccountOperationInfoService;
import github.com.suitelhy.dingding.core.domain.service.UserPersonInfoService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityUserService;
import github.com.suitelhy.dingding.core.domain.entity.Log;
import github.com.suitelhy.dingding.core.domain.entity.User;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.core.domain.repository.UserRepository;
import github.com.suitelhy.dingding.core.domain.service.LogService;
import github.com.suitelhy.dingding.core.domain.service.UserService;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Account;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.HandleType;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Optional;


/**
 * 用户信息 - 业务实现
 *
 * @Description {@link UserService}
 * @see User
 * @see UserAccountOperationInfo
 * @see UserService
 * @see UserPersonInfoService
 * @see SecurityUserService
 * @see UserAccountOperationInfoService
 * @see LogService
 */
@Order(Ordered.LOWEST_PRECEDENCE)
@Service("userService")
@Transactional(isolation = Isolation.READ_COMMITTED
        , propagation = Propagation.REQUIRED
        , readOnly = true
        , rollbackFor = Exception.class
        , timeout = 15)
public class UserServiceImpl
        implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAccountOperationInfoService userAccountOperationInfoService;

    @Autowired
    private UserPersonInfoService userPersonInfoService;

    @Autowired
    private SecurityUserService securityUserService;

    @Autowired
    private LogService logService;

    /**
     * 判断存在 ->指定的用户
     *
     * @param username {@link User.Validator#username(String)}
     * @return {@link Boolean#TYPE}
     * @throws IllegalArgumentException
     */
    @Override
    public boolean existUserByUsername(@NotNull String username)
            throws IllegalArgumentException {
        if (!User.Validator.USER.username(username)) {
            //-- 非法输入: 用户名称
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "用户名称"
                    , username
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return userRepository.existsByUsernameAndStatusIn(username, Account.StatusVo.DATA_EXISTENCE_CERTIFICATE);
    }

    /**
     * 查询用户列表
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     * @return {@link org.springframework.data.domain.Page}
     * @throws IllegalArgumentException
     */
    @Override
    public Page<User> selectAll(int pageIndex, int pageSize)
            throws IllegalArgumentException {
        if (pageIndex < 0) {
            //-- 非法输入: <param>pageIndex</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "pageIndex"
                    , pageIndex
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (pageSize < 1) {
            //-- 非法输入: <param>pageSize</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "pageSize"
                    , pageSize
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        Sort.TypedSort<User> typedSort = Sort.sort(User.class);
        Sort sort = typedSort.by(User::getUserid).ascending()
                .and(typedSort.by(User::getStatus).ascending());
        Pageable page = PageRequest.of(pageIndex, pageSize, sort);

        return userRepository.findAll(page);
    }

    @Override
    public @NotNull
    Long selectCount(int pageSize)
            throws IllegalArgumentException {
        if (pageSize < 1) {
            //-- 非法输入: <param>pageSize</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "pageSize"
                    , pageSize
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        long dataNumber = userRepository.count();
        return (dataNumber % pageSize == 0)
                ? (dataNumber / pageSize)
                : (dataNumber / pageSize + 1);
    }

    /**
     * 查询指定的用户
     *
     * @param userid {@link User.Validator#userid(String)}
     * @return {@link User}
     * @throws IllegalArgumentException
     */
    @Override
    public @NotNull
    User selectUserByUserid(@NotNull String userid)
            throws IllegalArgumentException {
        if (!User.Validator.USER.userid(userid)) {
            //-- 非法输入: [用户 ID]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "[用户 ID]"
                    , userid
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return userRepository.findUserByUserid(userid)
                .orElseGet(User.Factory.USER::createDefault);
    }

    /**
     * 查询指定的用户
     *
     * @param username {@link User.Validator#username(String)}
     * @return {@link User}
     * @throws IllegalArgumentException
     */
    @Override
    public @NotNull
    User selectUserByUsername(@NotNull String username)
            throws IllegalArgumentException {
        if (!User.Validator.USER.username(username)) {
            //-- 非法输入: 用户名称
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "用户名称"
                    , username
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return userRepository.findUserByUsernameAndStatusIn(username, Account.StatusVo.DATA_EXISTENCE_CERTIFICATE)
                .orElseGet(User.Factory.USER::createDefault);
    }

//    /**
//     * 查询指定的用户 -> 账户操作记录
//     *
//     * @param username  {@link UserAccountOperationInfo.Validator#username(String)}
//     *
//     * @return {@link UserAccountOperationInfo}
//     *
//     * @see UserAccountOperationInfo
//     * @see UserAccountOperationInfoService
//     *
//     * @throws IllegalArgumentException
//     */
//    @Override
//    public UserAccountOperationInfo selectUserAccountOperationInfo(@NotNull String username)
//            throws IllegalArgumentException
//    {
//        if (!UserAccountOperationInfo.Validator.USER.username(username)) {
//            //-- 非法输入: 用户名称
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "用户名称"
//                    , username
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        return userAccountOperationInfoService.selectByUsername(username);
//    }

//    /**
//     * 查询指定的用户 -> 账户操作记录
//     *
//     * @param username  {@link UserPersonInfo.Validator#username(String)}
//     *
//     * @return {@link UserPersonInfo}
//     *
//     * @see UserPersonInfo
//     * @see UserPersonInfoService
//     */
//    @Override
//    public UserPersonInfo selectUserPersonInfo(@NotNull String username)
//            throws IllegalArgumentException
//    {
//        if (!UserPersonInfo.Validator.USER.username(username)) {
//            //-- 非法输入: 用户名称
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "用户名称"
//                    , username
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        return userPersonInfoService.selectByUsername(username);
//    }

//    /**
//     * 新增一个用户
//     *
//     * @param user                      预期新增的用户 -> 用户基础信息               {@link User}
//     * @param userAccountOperationInfo  预期新增的用户 -> [用户 -> 账户操作基础记录]  {@link UserAccountOperationInfo}
//     * @param userPersonInfo            预期新增的用户 -> [用户 -> 个人信息]        {@link UserPersonInfo}
//     * @param operator                  操作者
//     *
//     * @return 操作是否成功
//     *
//     * @see UserAccountOperationInfoService
//     *
//     * @throws IllegalArgumentException
//     * @throws BusinessAtomicException
//     */
//    @Override
//    @Transactional(isolation  = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , timeout = 15)
//    public boolean insert(@NotNull User user, @NotNull UserAccountOperationInfo userAccountOperationInfo, @NotNull UserPersonInfo userPersonInfo
//            , @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if (null == user || !user.isEntityLegal()) {
//            //-- 非法输入: 预期新增的用户 -> 用户基础信息
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "预期新增的用户 -> 用户基础信息"
//                    , user
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == userAccountOperationInfo
//                || !userAccountOperationInfo.isEntityLegal()
//                || !userAccountOperationInfo.equals(user))
//        {
//            //-- 非法输入: 预期新增的用户 -> [用户 -> 账户操作基础记录]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "预期新增的用户 -> [用户 -> 账户操作基础记录]"
//                    , userAccountOperationInfo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == userPersonInfo
//                || !userPersonInfo.isEntityLegal()
//                || !userPersonInfo.equals(user))
//        {
//            //-- 非法输入: 预期新增的用户 -> [用户 -> 个人信息]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】&【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "预期新增的用户 -> [用户 -> 个人信息]"
//                    , userPersonInfo
//                    , user
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == operator || operator.isEmpty()) {
//            //-- 非法输入: 操作者
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "操作者"
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        final UserAccountOperationInfo operator_OperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
//        if (null == operator_OperationInfo || operator_OperationInfo.isEmpty()) {
//            //-- 非法输入: 操作者 <- 无有效的账户操作记录
//            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "操作者"
//                    , "无有效的账户操作记录"
//                    , operator_OperationInfo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (!Boolean.TRUE.equals(securityUserService.existAdminPermission(operator.getUsername()))) {
//            //-- 非法输入: 操作者 <- 没有足够的操作权限
//            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "操作者"
//                    , "没有足够的操作权限"
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        /*System.err.println("【调试用】==> UserServiceImpl#insert(...) 校验完成! <==");*/
//
//        if (userRepository.existsByUsernameAndStatusIn(user.getUsername(), Account.StatusVo.DATA_EXISTENCE_CERTIFICATE)) {
//            //--- 已存在指定用户的情况
//            return !userRepository.findUserByUsernameAndStatusIn(user.getUsername(), Account.StatusVo.DATA_EXISTENCE_CERTIFICATE)
//                    .get()
//                    .isEmpty();
//        }
//
//        final User newUser = userRepository.saveAndFlush(user);
//        if (newUser.isEmpty()) {
//            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , HandleType.LogVo.USER__USER__ADD.name
//                    , "操作后的用户数据异常"
//                    , newUser
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        //===== (关联数据处理) =====//
//
//        // [用户 -> 账户操作基础记录]
//        if (!userAccountOperationInfoService.insert(userAccountOperationInfo, operator)) {
//            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , HandleType.LogVo.USER__REGISTRATION.name
//                    , "新增关联的[用户 -> 账户操作基础记录]失败"
//                    , userAccountOperationInfo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        // [用户 -> 个人信息]
//        if (!userPersonInfoService.insert(userPersonInfo, operator)) {
//            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , HandleType.LogVo.USER__REGISTRATION.name
//                    , "新增关联的[用户 -> 个人信息]失败"
//                    , userPersonInfo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        // 用户关联的安全信息
//        final SecurityUser newSecurityUser = SecurityUser.Factory.USER.create(newUser);
//        if (!securityUserService.insert(newSecurityUser, operator)) {
//            //-- 操作失败: 新增关联的 (安全) 用户
//            throw new BusinessAtomicException(String.format("操作失败:<description>%s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , HandleType.LogVo.SECURITY__SECURITY_USER__ADD.name
//                    , newSecurityUser
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        //==========//
//
//        //=== 操作日志记录 ===//
//
//        final Log newLog_User = Log.Factory.User.LOG.create(null
//                , null
//                , HandleType.LogVo.USER__USER__ADD
//                , newUser
//                , operator
//                , operator_OperationInfo);
//        if (!logService.insert(newLog_User)) {
//            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , HandleType.LogVo.USER__USER__ADD.name
//                    , "生成操作日志记录"
//                    , newLog_User
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        //======//
//
//        return true;
//    }

    /**
     * 新增一个用户
     *
     * @param user                   预期新增的用户 -> 用户基础信息
     * @param operator               操作者
     * @param operator_OperationInfo [操作者 - 账户操作基础记录]
     * @return 操作是否成功
     * @throws IllegalArgumentException
     * @throws BusinessAtomicException
     * @Description 完整业务的一部分.
     * @see github.com.suitelhy.dingding.core.domain.event.UserEvent#registerUser(User, UserAccountOperationInfo, UserPersonInfo, SecurityUser)
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    public boolean insert(@NotNull User user, @NotNull SecurityUser operator, @NotNull UserAccountOperationInfo operator_OperationInfo)
            throws IllegalArgumentException, BusinessAtomicException {
        if (null == user || !user.isEntityLegal()) {
            //-- 非法输入: 预期新增的用户 -> 用户基础信息
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "预期新增的用户 -> 用户基础信息"
                    , user
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == operator || operator.isEmpty()) {
            //-- 非法输入: 操作者
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == operator_OperationInfo || operator_OperationInfo.isEmpty()) {
            //-- 非法输入: 操作者 <- 无有效的账户操作记录
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , "无有效的账户操作记录"
                    , operator_OperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        if (userRepository.existsByUsernameAndStatusIn(user.getUsername(), Account.StatusVo.DATA_EXISTENCE_CERTIFICATE)) {
            //--- 已存在指定用户的情况
            return !userRepository.findUserByUsernameAndStatusIn(user.getUsername(), Account.StatusVo.DATA_EXISTENCE_CERTIFICATE)
                    .orElseGet(User.Factory.USER::createDefault)
                    .isEmpty();
        }

        final @NotNull User newUser = userRepository.saveAndFlush(user);
        if (newUser.isEmpty()) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.USER__USER__ADD.name
                    , "操作后的用户数据异常"
                    , newUser
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        //=== 操作日志记录 ===//

        final @NotNull Log newLog_User = Log.Factory.User.LOG.create(null
                , null
                , HandleType.LogVo.USER__USER__ADD
                , newUser
                , operator
                , operator_OperationInfo);
        if (!logService.insert(newLog_User)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.USER__USER__ADD.name
                    , "生成操作日志记录"
                    , newLog_User
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        //======//

        return true;
    }

    /**
     * 更新指定的用户
     *
     * @param user                   被修改的用户
     * @param operator               操作者
     * @param operator_OperationInfo [操作者 - 账户操作基础记录]
     * @return 操作是否成功
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , rollbackFor = Exception.class
            , timeout = 15)
    public boolean update(@NotNull User user, @NotNull SecurityUser operator, @NotNull UserAccountOperationInfo operator_OperationInfo)
            throws IllegalArgumentException, BusinessAtomicException {
        if (null == user || user.isEmpty()) {
            //-- 非法输入: 被修改的用户
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "被修改的用户"
                    , user
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == operator || operator.isEmpty()) {
            //-- 非法输入: 操作者
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == operator_OperationInfo
                || operator_OperationInfo.isEmpty()
                || !operator_OperationInfo.equals(operator)) {
            //-- 非法输入: 操作者 <- 无有效的账户操作记录
            throw new IllegalArgumentException(String.format("非法参数:<description>%s</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者 <- 无有效的账户操作记录"
                    , operator_OperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (!operator.equals(user)
                && !securityUserService.existAdminPermission(operator.getUsername())) {
            //-- 非法输入: 操作者 <- 没有所需的操作权限
            throw new IllegalArgumentException(String.format("非法参数:<description>%s</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者 <- 没有所需的操作权限"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        final User newUser = userRepository.saveAndFlush(user);
        if (newUser.isEmpty()) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.USER__USER__UPDATE.name
                    , "操作后的数据异常"
                    , newUser
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        //===== 操作日志记录 =====//

        final Log newLog_User = Log.Factory.User.LOG.create(null
                , null
                , HandleType.LogVo.USER__USER__UPDATE
                , newUser
                , operator
                , operator_OperationInfo);
        if (!logService.insert(newLog_User)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.USER__USER__UPDATE.name
                    , "生成操作日志失败"
                    , newLog_User
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        //==========//

        return true;
    }

    /**
     * 删除指定的用户
     *
     * @param user                              被删除的用户
     * @param operator                          操作者 (与{@param user}一致, 或拥有足够高的权限)
     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
     * @return 操作是否成功
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    public boolean delete(@NotNull User user, @NotNull SecurityUser operator, @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
            throws IllegalArgumentException, BusinessAtomicException {
        if (null == user || user.isEmpty()) {
            //-- 非法输入: 被删除的用户
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "被删除的用户"
                    , user
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == operator || operator.isEmpty()) {
            //-- 非法输入: 操作者
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == operator_userAccountOperationInfo
                || operator_userAccountOperationInfo.isEmpty()
                || !operator_userAccountOperationInfo.equals(operator)) {
            //-- 非法输入: 操作者 <- 无有效的账户操作记录
            throw new IllegalArgumentException(String.format("非法参数:<description>%s</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者 <- 无有效的账户操作记录"
                    , operator_userAccountOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (!operator.equals(user)
                && !securityUserService.existAdminPermission(operator.getUsername())) {
            //-- 非法输入: 操作者 <- 没有所需的操作权限
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , "没有所需的操作权限"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        /*//===== (先删除关联的用户信息) =====//

        if (! userAccountOperationInfoService.deleteAndValidate(operator_userAccountOperationInfo, operator)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.USER__USER_ACCOUNT_OPERATION_INFO__DELETE.name
                    , "删除失败"
                    , operator_userAccountOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        //==========//*/

        userRepository.deleteById(user.getUserid());

        //=== 日志记录操作 ===//

        final @NotNull Log newLog_User = Log.Factory.User.LOG.create(null
                , null
                , HandleType.LogVo.USER__USER__DATA_DELETION
                , user
                , operator
                , operator_userAccountOperationInfo);
        if (!logService.insert(newLog_User)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.USER__USER__DATA_DELETION.name
                    , "生成操作日志失败"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        //======//

        return true;
    }

    /**
     * 删除指定的用户
     *
     * @param user                              被删除的用户
     * @param operator                          操作者 (与{@param user}一致, 或拥有足够高的权限)
     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
     * @return 操作是否成功
     * @Description 删除成功后校验持久化数据; 主要是避免在未提交的事务中进行对操作结果的非预期判断.
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    public boolean deleteAndValidate(@NotNull User user, @NotNull SecurityUser operator, @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
            throws IllegalArgumentException, BusinessAtomicException {
        if (null == user || user.isEmpty()) {
            //-- 非法输入: 被删除的用户
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "被删除的用户"
                    , user
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == operator || operator.isEmpty()) {
            //-- 非法输入: 操作者
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        /*final UserAccountOperationInfo operator_OperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());*/
        if (null == operator_userAccountOperationInfo
                || operator_userAccountOperationInfo.isEmpty()
                || !operator_userAccountOperationInfo.equals(operator)) {
            //-- 非法输入: 操作者 <- 无有效的账户操作记录
            throw new IllegalArgumentException(String.format("非法参数:<description>%s</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者 <- 无有效的账户操作记录"
                    , operator_userAccountOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (!operator.equals(user)
                && !securityUserService.existAdminPermission(operator.getUsername())) {
            //-- 非法输入: 操作者 <- 没有所需的操作权限
            throw new IllegalArgumentException(String.format("非法参数:<description>%s</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者 <- 没有所需的操作权限"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        /*//===== (先删除关联的用户信息) =====//

        UserAccountOperationInfo userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(user.getUsername());
        if (null != userAccountOperationInfo
                && ! userAccountOperationInfoService.deleteAndValidate(userAccountOperationInfo, operator))
        {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.USER__USER_ACCOUNT_OPERATION_INFO__DELETE.name
                    , "操作后的用户数据异常"
                    , userAccountOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        //==========//*/

        userRepository.deleteById(user.getUserid());
        if (userRepository.existsById(user.getUserid())) {
            return false;
        }

        //=== 日志记录操作 ===//

        final @NotNull Log newLog_User = Log.Factory.User.LOG.create(null
                , null
                , HandleType.LogVo.USER__USER__DATA_DELETION
                , user
                , operator
                , operator_userAccountOperationInfo);
        if (!logService.insert(newLog_User)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.USER__USER__DATA_DELETION.name
                    , "生成操作日志失败"
                    , newLog_User
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        //======//

        return true;
    }

}
