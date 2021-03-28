package github.com.suitelhy.dingding.security.service.provider.domain.service.impl;

import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.HandleType;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.security.service.provider.domain.repository.SecurityUserRepository;
import github.com.suitelhy.dingding.security.service.provider.domain.service.SecurityRoleService;
import github.com.suitelhy.dingding.security.service.provider.domain.service.SecurityUserRoleService;
import github.com.suitelhy.dingding.security.service.provider.domain.service.SecurityUserService;
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
import java.util.List;
import java.util.Map;

/**
 * (安全) 用户
 *
 * @Description (安全) 用户 - 业务实现.
 *
 * @see SecurityUser
 * @see SecurityUserRepository
 * @see SecurityUserService
 * @see SecurityRoleService
 * @see SecurityUserRoleService
 */
@Order(Ordered.LOWEST_PRECEDENCE)
@Service("securityUserService")
@Transactional(isolation = Isolation.SERIALIZABLE
        , propagation = Propagation.REQUIRED
        , readOnly = false
        , rollbackFor = Exception.class
        , timeout = 15)
public class SecurityUserServiceImpl
        implements SecurityUserService {

    @Autowired
    private SecurityUserRepository repository;

    /**
     * 判断存在
     *
     * @param userId    {@linkplain SecurityUser.Validator#userId(String)} [用户 ID]}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     *
     * @throws IllegalArgumentException
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public boolean existByUserId(@NotNull String userId)
            throws IllegalArgumentException {
        if (! SecurityUser.Validator.USER.userId(userId)) {
            //-- 非法输入: [用户 ID]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "[用户 ID]"
                    , userId
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return repository.existsByUserId(userId);
    }

    /**
     * 判断存在
     *
     * @param username  {@linkplain SecurityUser.Validator#username(String) 用户名称}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     *
     * @throws IllegalArgumentException
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public boolean existByUsername(@NotNull String username)
            throws IllegalArgumentException
    {
        if (! SecurityUser.Validator.USER.username(username)) {
            //-- 非法输入: 用户名称
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "用户名称"
                    , username
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return repository.existsByUsername(username);
    }

    /**
     * 是否具有管理员权限
     *
     * @param username  {@linkplain SecurityUser.Validator#username(String) 用户名称}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public boolean existAdminPermission(@NotNull String username)
            throws IllegalArgumentException
    {
        if (null == username || ! SecurityUser.Validator.USER.username(username)) {
            //-- 非法输入: 用户名称
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "用户名称"
                    , username
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        @NotNull List<Map<String, Object>> roleList = repository.selectRoleByUsername(username);
        if (! roleList.isEmpty()) {
            for (final @NotNull Map<String, Object> each : roleList) {
                @NotNull SecurityRole eachRole = SecurityRole.Factory.ROLE.update(
                        Long.parseLong(String.valueOf(each.get("id")))
                        , (String) each.get("code")
                        , (String) each.get("name")
                        , (String) each.get("description"));
                if (SecurityRole.Validator.isAdminRole(eachRole)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 查询所有
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     *
     * @return {@link Page}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public Page<SecurityUser> selectAll(int pageIndex, int pageSize) {
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

        Sort.TypedSort<SecurityUser> typedSort = Sort.sort(SecurityUser.class);
        @NotNull Sort sort = typedSort.by(SecurityUser::getUserId).ascending()
                .and(typedSort.by(SecurityUser::getStatus).ascending());
        @NotNull Pageable page = PageRequest.of(pageIndex, pageSize, sort);

        return repository.findAll(page);
    }

    /**
     * 查询总页数
     *
     * @Description 查询数据列表 - 分页 - 总页数
     *
     * @param pageSize 分页 - 每页容量
     *
     * @return 分页 - 总页数
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public @NotNull Long selectCount(int pageSize) {
        if (pageSize < 1) {
            //-- 非法输入: <param>pageSize</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "pageSize"
                    , pageSize
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        long dataNumber = repository.count();
        return (dataNumber % pageSize == 0)
                ? (dataNumber / pageSize)
                : (dataNumber / pageSize + 1);
    }

    /**
     * 查询
     *
     * @param userId    {@linkplain SecurityUser.Validator#userId(String) [用户 ID]}
     *
     * @return {@link SecurityUser}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public @NotNull SecurityUser selectByUserId(@NotNull String userId) {
        if (! SecurityUser.Validator.USER.userId(userId)) {
            //-- 非法输入: [用户 ID]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "[用户 ID]"
                    , userId
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return repository.findById(userId)
                .orElseGet(SecurityUser.Factory.USER::createDefault);
    }

    /**
     * 查询
     *
     * @param username  {@linkplain SecurityUser.Validator#username(String) 用户名称}
     *
     * @return {@link SecurityUser}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public @NotNull SecurityUser selectByUsername(@NotNull String username) {
        if (! SecurityUser.Validator.USER.username(username)) {
            //-- 非法输入: 用户名称
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "用户名称"
                    , username
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return repository.findByUsername(username)
                .orElseGet(SecurityUser.Factory.USER::createDefault);
    }

    /**
     * 新增一个用户
     *
     * @Description
     * · 完整业务流程的一部分.
     *
     * @param user      {@linkplain SecurityUser [（安全认证）用户]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在相同的有效数据}
     */
    @Override
    public boolean insert(@NotNull SecurityUser user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == user || ! user.isEntityLegal()) {
            //-- 非法输入: 预期新增的用户
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "预期新增的用户"
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
        /*final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());*/
        /*if (null == operator_userAccountOperationInfo
                || operator_userAccountOperationInfo.isEmpty()
                || ! operator_userAccountOperationInfo.equals(operator))
        {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】&【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , "无[有效的账户操作基础记录]"
                    , operator
                    , operator_userAccountOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }*/

        if (repository.existsByUsername(user.getUsername())) {
            //--- 已存在相同数据 (根据 EntityID 判断) 的情况
            return ! repository.findByUsername(user.getUsername())
                    .orElseGet(SecurityUser.Factory.USER::createDefault)
                    .isEmpty();
        }

        final @NotNull SecurityUser newUser = repository.saveAndFlush(user);
        if (newUser.isEmpty()) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.SECURITY__SECURITY_USER__ADD.name
                    , "更新后的数据异常"
                    , newUser
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        /*//=== 操作日志记录 ===//

        final @NotNull Log newLog_SecurityUser = Log.Factory.SecurityUser.LOG.create(null
                , null
                , HandleType.LogVo.SECURITY__SECURITY_USER__ADD
                , newUser
                , operator
                , operator_userAccountOperationInfo);
        if (! logService.insert(newLog_SecurityUser)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.SECURITY__SECURITY_USER__ADD.name
                    , "生成操作日志记录"
                    , newLog_SecurityUser
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        //======//*/

        return true;
    }

    /**
     * 更新指定的用户
     *
     * @param user      {@linkplain SecurityUser [（安全认证）用户]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean update(@NotNull SecurityUser user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == user || user.isEmpty()) {
            //-- 非法输入: 非法用户
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "非法用户"
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
        /*final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());*/
        /*if (null == operator_userAccountOperationInfo
                || operator_userAccountOperationInfo.isEmpty()
                || ! operator_userAccountOperationInfo.equals(operator))
        {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础户记录]
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】&【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , "无[有效的账户操作基础记录]"
                    , operator
                    , operator_userAccountOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }*/

        final @NotNull SecurityUser newUser = repository.saveAndFlush(user);
        if (newUser.isEmpty()) {
            return false;
        }

        /*//===== 关联数据 =====//

        if (! securityRoleService.existsByCode(Security.RoleVo.USER.name())
                && ! securityRoleService.insert(Security.RoleVo.USER))
        {
            //-- 操作失败: 新增 (安全) 角色
            throw new BusinessAtomicException(String.format("操作失败:<description>%s!</description>->【%s】&【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.SECURITY__SECURITY_ROLE__ADD.name
                    , Security.RoleVo.USER
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        final @NotNull SecurityUserRole newUserRole  = SecurityUserRole.Factory.USER_ROLE.create(newUser.getUsername()
                , Security.RoleVo.USER.name());
        if (! userEvent.insertUserRoleRelationship(newUserRole, operator)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>%s!</description>->【%s】&【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__ADD.name
                    , newUserRole
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        //==========//*/

        /*//=== 操作日志记录 ===//

        final @NotNull Log newLog_SecurityUser = Log.Factory.SecurityUser.LOG.create(null
                , null
                , HandleType.LogVo.SECURITY__SECURITY_USER__ADD
                , user
                , operator
                , operator_userAccountOperationInfo);
        if (! logService.insert(newLog_SecurityUser)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.SECURITY__SECURITY_USER__ADD.name
                    , "生成操作日志记录"
                    , newLog_SecurityUser
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        //======//*/

        return true;
    }

    /**
     * 删除指定的用户
     *
     * @Description 删除成功后校验持久化数据; 主要为了避免在未提交的事务中进行对操作结果的非预期判断.
     * · 完整业务流程的一部分.
     *
     * @param user      {@linkplain SecurityUser [（安全认证）用户]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 指定的用户是否已被删除}
     */
    @Override
    public boolean delete(@NotNull SecurityUser user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == user || user.isEmpty()) {
            //-- 非法输入: 非法用户
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "非法用户"
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
        /*final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());*/
        /*if (null == operator_userAccountOperationInfo
                || operator_userAccountOperationInfo.isEmpty()
                || ! operator_userAccountOperationInfo.equals(operator)) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础户记录]
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】&【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , "无[有效的账户操作基础记录]"
                    , operator
                    , operator_userAccountOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }*/

        if (! repository.existsById(user.getUserId())) {
            //--- 不存在预期删除的数据的情况
            return true;
        } else {
            //--- 存在预期删除的数据的情况
            repository.deleteById(user.getUserId());
            if (repository.existsById(user.getUserId())) {
                throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , HandleType.LogVo.SECURITY__SECURITY_USER__DELETE.name
                        , "执行后数据异常"
                        , user
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            /*//=== 操作日志记录 ===//

            final @NotNull Log newLog_SecurityUser = Log.Factory.SecurityUser.LOG.create(null
                    , null
                    , HandleType.LogVo.SECURITY__SECURITY_USER__DELETE
                    , user
                    , operator
                    , operator_userAccountOperationInfo);
            if (! logService.insert(newLog_SecurityUser)) {
                throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , HandleType.LogVo.SECURITY__SECURITY_USER__DELETE.name
                        , "生成操作日志记录"
                        , newLog_SecurityUser
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            //======//*/
        }

        return true;
    }

}
