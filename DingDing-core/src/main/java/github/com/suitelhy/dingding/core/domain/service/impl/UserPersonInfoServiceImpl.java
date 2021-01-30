package github.com.suitelhy.dingding.core.domain.service.impl;

import github.com.suitelhy.dingding.core.domain.entity.Log;
import github.com.suitelhy.dingding.core.domain.entity.User;
import github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.core.domain.entity.UserPersonInfo;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.core.domain.repository.UserPersonInfoRepository;
import github.com.suitelhy.dingding.core.domain.service.LogService;
import github.com.suitelhy.dingding.core.domain.service.UserAccountOperationInfoService;
import github.com.suitelhy.dingding.core.domain.service.UserPersonInfoService;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.HandleType;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * 用户 -> 账户操作记录 - 业务实现
 *
 * @see UserPersonInfo
 * @see User
 * @see UserPersonInfoRepository
 * @see UserPersonInfoService
 * @see UserAccountOperationInfoService
 * @see LogService
 */
@Order(Ordered.LOWEST_PRECEDENCE)
@Service("userPersonInfoService")
@Transactional(isolation = Isolation.READ_COMMITTED
        , propagation = Propagation.REQUIRED
        , readOnly = true
        , rollbackFor = Exception.class
        , timeout = 15)
public class UserPersonInfoServiceImpl
        implements UserPersonInfoService {

    @Autowired
    private UserPersonInfoRepository userPersonInfoRepository;

    @Autowired
    private UserAccountOperationInfoService userAccountOperationInfoService;

    @Autowired
    private LogService logService;

    /**
     * 查询记录列表
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     * @return {@link Page}
     */
    @Override
    public @NotNull
    Page<UserPersonInfo> selectAll(int pageIndex, int pageSize) {
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

        Sort.TypedSort<UserPersonInfo> typedSort = Sort.sort(UserPersonInfo.class);
        Sort sort = typedSort.by(UserPersonInfo::getUsername).ascending();
        Pageable page = PageRequest.of(pageIndex, pageSize, sort);

        return userPersonInfoRepository.findAll(page);
    }

    /**
     * 查询记录列表 - 分页 - 总页数
     *
     * @param pageSize 分页 - 每页容量
     * @return 分页 - 总页数
     */
    @Override
    public @NotNull
    Long selectCount(int pageSize) {
        if (pageSize < 1) {
            //-- 非法输入: <param>pageSize</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "pageSize"
                    , pageSize
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        final long dataNumber = userPersonInfoRepository.count();
        return (dataNumber % pageSize == 0)
                ? (dataNumber / pageSize)
                : (dataNumber / pageSize + 1);
    }

    /**
     * 查询指定的记录
     *
     * @param id 数据 ID   {@link UserPersonInfo.Validator#id(String)}
     * @return {@link UserPersonInfo}
     */
    @Override
    public @NotNull
    UserPersonInfo selectById(@NotNull String id) {
        if (!UserPersonInfo.Validator.USER.id(id)) {
            //-- 非法输入: [数据 ID]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "[数据 ID]"
                    , id
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return userPersonInfoRepository.findById(id)
                .orElseGet(UserPersonInfo.Factory.USER::createDefault);
    }

    /**
     * 查询指定的记录
     *
     * @param username 用户名称    {@link User.Validator#username(String)}
     * @return {@link UserPersonInfo}
     */
    @Override
    public @NotNull
    UserPersonInfo selectByUsername(@NotNull String username) {
        if (!UserPersonInfo.Validator.USER.username(username)) {
            //-- 非法输入: 用户名称
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "用户名称"
                    , username
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return userPersonInfoRepository.findByUsername(username)
                .orElseGet(UserPersonInfo.Factory.USER::createDefault);
    }

    /**
     * 查询指定的记录
     *
     * @param nickname [用户 - 昵称]   {@link UserPersonInfo.Validator#nickname(String)}
     * @return {@link UserPersonInfo}
     */
    @Override
    public @NotNull
    UserPersonInfo selectByNickname(@NotNull String nickname) {
        if (!UserPersonInfo.Validator.USER.username(nickname)) {
            //-- 非法输入: [用户 - 昵称]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "[用户 - 昵称]"
                    , nickname
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return userPersonInfoRepository.findByNickname(nickname)
                .orElseGet(UserPersonInfo.Factory.USER::createDefault);
    }

    /**
     * 新增一条记录
     *
     * @param user     [用户 -> 个人信息]    {@link UserPersonInfo}
     * @param operator 操作者               {@link SecurityUser}
     * @return 操作是否成功
     */
    @Override
    public boolean insert(@NotNull UserPersonInfo user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException {
        if (null == user || !user.isEntityLegal()) {
            //-- 非法输入: [用户 -> 个人信息]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "[用户 - 个人信息]"
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
        final @NotNull UserAccountOperationInfo operator_OperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
        if (null == operator_OperationInfo || operator_OperationInfo.isEmpty()) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础户记录]
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , "无[有效的账户操作基础记录]"
                    , user
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        if (userPersonInfoRepository.existsByUsername(user.getUsername())) {
            //--- 已存在指定用户的情况
            return !userPersonInfoRepository.findByUsername(user.getUsername()).get().isEmpty();
        }

        final UserPersonInfo newUser = userPersonInfoRepository.saveAndFlush(user);
        if (newUser.isEmpty()) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.USER__USER_PERSON_INFO__ADD.name
                    , "操作后的用户数据异常"
                    , newUser
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        //=== 操作日志记录 ===//
        final @NotNull Log newLog_UserPersonInfo = Log.Factory.UserPersonInfo.LOG.create(null
                , null
                , HandleType.LogVo.USER__USER_PERSON_INFO__ADD
                , newUser
                , operator
                , operator_OperationInfo);
        if (!logService.insert(newLog_UserPersonInfo)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.USER__USER_PERSON_INFO__ADD.name
                    , "生成操作日志记录失败"
                    , newUser
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        //======//

        return true;
    }

    /**
     * 更新指定的记录
     *
     * @param user     [用户 -> 个人信息]    {@link UserPersonInfo}
     * @param operator 操作者               {@link SecurityUser}
     * @return 操作是否成功
     */
    @Override
    public boolean update(@NotNull UserPersonInfo user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException {
        if (null == user || user.isEmpty()) {
            //-- 非法输入: [用户 -> 账户操作基础记录]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "[用户 -> 账户操作基础记录]"
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
        final @NotNull UserAccountOperationInfo operator_OperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
        if (null == operator_OperationInfo || operator_OperationInfo.isEmpty()) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础户记录]
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , "无[有效的账户操作基础户记录]"
                    , operator_OperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        final @NotNull UserPersonInfo newUser = userPersonInfoRepository.saveAndFlush(user);
        if (newUser.isEmpty()) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.USER__USER_PERSON_INFO__UPDATE.name
                    , "操作后的用户数据异常"
                    , newUser
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        //=== 操作日志记录 ===//
        final @NotNull Log newLog_UserPersonInfo = Log.Factory.UserPersonInfo.LOG.create(null
                , null
                , HandleType.LogVo.USER__USER_PERSON_INFO__UPDATE
                , newUser
                , operator
                , operator_OperationInfo);
        if (!logService.insert(newLog_UserPersonInfo)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.USER__USER_PERSON_INFO__UPDATE.name
                    , "生成操作日志记录失败"
                    , newUser
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        //======//

        return true;
    }

    /**
     * 删除指定的记录
     *
     * @param user     [用户 -> 个人信息]    {@link UserPersonInfo}
     * @param operator 操作者               {@link SecurityUser}
     * @return 操作是否成功
     */
    @Override
    public boolean delete(@NotNull UserPersonInfo user, @NotNull SecurityUser operator)
            throws BusinessAtomicException {
        if (null == user || user.isEmpty()) {
            //-- 非法输入: [用户 -> 账户操作基础记录]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "[用户 -> 账户操作基础记录]"
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
        final UserAccountOperationInfo operator_OperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
        if (null == operator_OperationInfo || operator_OperationInfo.isEmpty()) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础户记录]
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , "无[有效的账户操作基础记录]"
                    , operator_OperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        if (!userPersonInfoRepository.existsByUsername(user.getUsername())) {
            return true;
        }

        userPersonInfoRepository.deleteByUsername(user.getUsername());

        //=== 操作日志记录 ===//

        final Log newLog_UserPersonInfo = Log.Factory.UserPersonInfo.LOG.create(null
                , null
                , HandleType.LogVo.USER__USER_PERSON_INFO__DELETE
                , user
                , operator
                , operator_OperationInfo);
        if (!logService.insert(newLog_UserPersonInfo)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.USER__USER_PERSON_INFO__DELETE.name
                    , "生成操作日志记录失败"
                    , newLog_UserPersonInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        //======//

        return true;
    }

    /**
     * 删除指定的记录
     *
     * @param user     [用户 -> 个人信息]    {@link UserPersonInfo}
     * @param operator 操作者               {@link SecurityUser}
     * @return 操作是否成功
     * @Description 删除成功后校验持久化数据; 主要是避免在未提交的事务中进行对操作结果的非预期判断.
     */
    @Override
    public boolean deleteAndValidate(@NotNull UserPersonInfo user, @NotNull SecurityUser operator)
            throws BusinessAtomicException {
        if (null == user || user.isEmpty()) {
            //-- 非法输入: [用户 -> 账户操作基础记录]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "[用户 -> 账户操作基础记录]"
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
        final UserAccountOperationInfo operator_OperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
        if (null == operator_OperationInfo || operator_OperationInfo.isEmpty()) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础户记录]
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , "无[有效的账户操作基础记录]"
                    , operator_OperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        if (!userPersonInfoRepository.existsByUsername(user.getUsername())) {
            return true;
        }

        userPersonInfoRepository.deleteByUsername(user.getUsername());
        if (userPersonInfoRepository.existsByUsername(user.getUsername())) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.USER__USER_PERSON_INFO__DELETE.name
                    , "更新后的数据异常"
                    , user
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        //=== 操作日志记录 ===//
        final Log newLog_UserPersonInfo = Log.Factory.UserPersonInfo.LOG.create(null
                , null
                , HandleType.LogVo.USER__USER_PERSON_INFO__DELETE
                , user
                , operator, operator_OperationInfo);
        if (!logService.insert(newLog_UserPersonInfo)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.USER__USER_PERSON_INFO__DELETE.name
                    , "生成操作日志记录失败"
                    , newLog_UserPersonInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        //======//

        return true;
    }

}
