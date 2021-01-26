package github.com.suitelhy.dingding.core.domain.service.impl;

import github.com.suitelhy.dingding.core.domain.entity.Log;
import github.com.suitelhy.dingding.core.domain.entity.User;
import github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.core.domain.repository.UserAccountOperationInfoRepository;
import github.com.suitelhy.dingding.core.domain.service.LogService;
import github.com.suitelhy.dingding.core.domain.service.UserAccountOperationInfoService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityUserService;
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
 * @see User
 * @see UserAccountOperationInfo
 * @see UserAccountOperationInfoService
 * @see LogService
 * @see SecurityUserService
 */
@Order(Ordered.LOWEST_PRECEDENCE)
@Service("userAccountOperationInfoService")
@Transactional(isolation = Isolation.READ_COMMITTED
        , propagation = Propagation.REQUIRED
        , readOnly = true
        , rollbackFor = Exception.class
        , timeout = 15)
public class UserAccountOperationInfoServiceImpl
        implements UserAccountOperationInfoService {

    @Autowired
    private UserAccountOperationInfoRepository userAccountOperationInfoRepository;

    @Autowired
    private LogService logService;

    @Autowired
    private SecurityUserService securityUserService;

    /**
     * 查询记录列表
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     * @return {@link Page}
     */
    @Override
    public Page<UserAccountOperationInfo> selectAll(int pageIndex, int pageSize) {
        if (pageIndex < 0) {
            //-- 非法输入: <param>dataIndex</param>
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

        Sort.TypedSort<UserAccountOperationInfo> typedSort = Sort.sort(UserAccountOperationInfo.class);
        Sort sort = typedSort.by(UserAccountOperationInfo::getRegistrationTime).ascending()
                .and(typedSort.by(UserAccountOperationInfo::getLastLoginTime).ascending());
        Pageable page = PageRequest.of(pageIndex, pageSize, sort);

        return userAccountOperationInfoRepository.findAll(page);
    }

    /**
     * 查询记录列表 - 分页 - 总页数
     *
     * @param pageSize 分页 - 每页容量
     *
     * @return 分页 - 总页数
     */
    @Override
    public Long selectCount(int pageSize) {
        if (pageSize < 1) {
            //-- 非法输入: <param>pageSize</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "pageSize"
                    , pageSize
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        final long dataNumber = userAccountOperationInfoRepository.count();
        return (dataNumber % pageSize == 0)
                ? (dataNumber / pageSize)
                : (dataNumber / pageSize + 1);
    }

    /**
     * 查询指定的记录
     *
     * @param id    [数据 ID]
     *
     * @return {@link UserAccountOperationInfo}
     */
    @Override
    public UserAccountOperationInfo selectById(@NotNull String id) {
        if (!UserAccountOperationInfo.Validator.USER.id(id)) {
            //-- 非法输入: [数据 ID]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "[数据 ID]"
                    , id
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        Optional<UserAccountOperationInfo> result = userAccountOperationInfoRepository.findById(id);
        return result.orElse(null);
    }

    /**
     * 查询指定的记录
     *
     * @param username  用户名称
     *
     * @return {@link UserAccountOperationInfo}
     */
    @Override
    public UserAccountOperationInfo selectByUsername(@NotNull String username) {
        if (!UserAccountOperationInfo.Validator.USER.username(username)) {
            //-- 非法输入: 用户名称
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "用户名称"
                    , username
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        Optional<UserAccountOperationInfo> result = userAccountOperationInfoRepository.findByUsername(username);
        return result.orElse(null);
    }

    /**
     * 新增一条记录
     *
     * @param user      [用户 -> 账户操作基础记录]    {@link UserAccountOperationInfo}
     * @param operator  操作者                     {@link SecurityUser}
     *
     * @return 操作是否成功
     */
    @Override
    public boolean insert(@NotNull UserAccountOperationInfo user, @NotNull SecurityUser operator)
            throws BusinessAtomicException
    {
        if (null == user || !user.isEntityLegal()) {
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
        final UserAccountOperationInfo operatorOperationInfo = userAccountOperationInfoRepository.findByUsername(operator.getUsername()).orElse(null);
        if (null == operatorOperationInfo || operatorOperationInfo.isEmpty()) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础户记录]
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , "无[有效的账户操作基础记录]"
                    , operatorOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        if (userAccountOperationInfoRepository.existsByUsername(user.id())) {
            //--- 已存在指定用户的情况
            return !userAccountOperationInfoRepository.findByUsername(user.id()).get().isEmpty();
        }

        final UserAccountOperationInfo newUser = userAccountOperationInfoRepository.saveAndFlush(user);
        if (newUser.isEmpty()) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.USER__USER_ACCOUNT_OPERATION_INFO__ADD.name
                    , "更新后的数据异常"
                    , newUser
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        //=== 操作日志记录 ===//
        final Log newLog_UserAccountOperationInfo = Log.Factory.UserAccountOperationInfo.LOG.create(null
                , null
                , HandleType.LogVo.USER__USER_ACCOUNT_OPERATION_INFO__ADD
                , newUser
                , operator
                , operatorOperationInfo);
        if (!logService.insert(newLog_UserAccountOperationInfo)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.USER__USER_ACCOUNT_OPERATION_INFO__ADD.name
                    , "生成操作日志失败"
                    , newLog_UserAccountOperationInfo
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
     * @param userAccountOperationInfo  [用户 -> 账户操作基础记录]
     * @param operator                  操作者
     *
     * @return 操作是否成功
     */
    @Override
    public boolean update(@NotNull UserAccountOperationInfo userAccountOperationInfo, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == userAccountOperationInfo || userAccountOperationInfo.isEmpty()) {
            //-- 非法输入: [用户 -> 账户操作基础记录]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "[用户 -> 账户操作基础记录]"
                    , userAccountOperationInfo
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
        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoRepository.findByUsername(operator.getUsername()).orElse(null);
        if (null == operator_userAccountOperationInfo || operator_userAccountOperationInfo.isEmpty()) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础户记录]
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , "无[有效的账户操作基础记录]"
                    , operator_userAccountOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (! operator_userAccountOperationInfo.equals(userAccountOperationInfo)
                && ! securityUserService.existAdminPermission(userAccountOperationInfo.getUsername())) {
            //-- 非法输入: 操作者 <- 无[足够的操作权限]
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , "无[足够的操作权限]"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        final @NotNull UserAccountOperationInfo newUser = userAccountOperationInfoRepository.saveAndFlush(userAccountOperationInfo);
        if (newUser.isEmpty()) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.USER__USER_ACCOUNT_OPERATION_INFO__UPDATE.name
                    , "更新后的数据异常"
                    , newUser
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        //=== 操作日志记录 ===//
        final Log newLog_UserAccountOperationInfo = Log.Factory.UserAccountOperationInfo.LOG.create(null
                , null
                , HandleType.LogVo.USER__USER_ACCOUNT_OPERATION_INFO__UPDATE
                , newUser
                , operator
                , operator_userAccountOperationInfo);
        if (!logService.insert(newLog_UserAccountOperationInfo)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.USER__USER_ACCOUNT_OPERATION_INFO__UPDATE.name
                    , "生成操作日志失败"
                    , newLog_UserAccountOperationInfo
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
     * @param user      [用户 -> 账户操作基础记录]
     * @param operator  操作者
     *
     * @return 操作是否成功
     */
    @Override
    public boolean delete(@NotNull UserAccountOperationInfo user, @NotNull SecurityUser operator)
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
        final UserAccountOperationInfo operatorOperationInfo = userAccountOperationInfoRepository.findByUsername(operator.getUsername()).orElse(null);
        if (null == operatorOperationInfo || operatorOperationInfo.isEmpty()) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础户记录]
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , "无[有效的账户操作基础记录]"
                    , operatorOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        if (!userAccountOperationInfoRepository.existsByUsername(user.getUsername())) {
            return true;
        }

        userAccountOperationInfoRepository.deleteByUsername(user.getUsername());

        //=== 操作日志记录 ===//
        final Log newLog_UserAccountOperationInfo = Log.Factory.UserAccountOperationInfo.LOG.create(null
                , null
                , HandleType.LogVo.USER__USER_ACCOUNT_OPERATION_INFO__DELETE
                , user
                , operator
                , operatorOperationInfo);
        if (!logService.insert(newLog_UserAccountOperationInfo)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.USER__USER_ACCOUNT_OPERATION_INFO__DELETE.name
                    , "生成操作日志失败"
                    , newLog_UserAccountOperationInfo
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
     * @Description 删除成功后校验持久化数据; 主要是避免在未提交的事务中进行对操作结果的非预期判断.
     *
     * @param user      [用户 -> 账户操作基础记录]
     * @param operator  操作者
     *
     * @return 操作是否成功
     */
    @Override
    public boolean deleteAndValidate(@NotNull UserAccountOperationInfo user, @NotNull SecurityUser operator)
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
        final UserAccountOperationInfo operatorOperationInfo = userAccountOperationInfoRepository.findByUsername(operator.getUsername()).orElse(null);
        if (null == operatorOperationInfo || operatorOperationInfo.isEmpty()) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础户记录]
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , "无[有效的账户操作基础记录]"
                    , operatorOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        if (!userAccountOperationInfoRepository.existsByUsername(user.getUsername())) {
            return true;
        }

        userAccountOperationInfoRepository.deleteByUsername(user.getUsername());
        if (userAccountOperationInfoRepository.existsByUsername(user.getUsername())) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.USER__USER_ACCOUNT_OPERATION_INFO__DELETE.name
                    , "更新后的数据异常"
                    , user
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        //=== 操作日志记录 ===//
        final Log newLog_UserAccountOperationInfo = Log.Factory.UserAccountOperationInfo.LOG.create(null
                , null
                , HandleType.LogVo.USER__USER_ACCOUNT_OPERATION_INFO__DELETE
                , user
                , operator
                , operatorOperationInfo);
        if (!logService.insert(newLog_UserAccountOperationInfo)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.USER__USER_ACCOUNT_OPERATION_INFO__DELETE.name
                    , "生成操作日志失败"
                    , newLog_UserAccountOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        //======//

        return true;
    }

}
