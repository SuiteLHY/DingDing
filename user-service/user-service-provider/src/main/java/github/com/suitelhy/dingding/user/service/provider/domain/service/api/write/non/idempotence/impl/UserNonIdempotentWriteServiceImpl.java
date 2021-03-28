package github.com.suitelhy.dingding.user.service.provider.domain.service.api.write.non.idempotence.impl;

import github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.security.service.api.domain.entity.User;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUserRole;
import github.com.suitelhy.dingding.security.service.api.domain.service.read.SecurityUserReadService;
import github.com.suitelhy.dingding.security.service.api.domain.service.read.SecurityUserRoleReadService;
import github.com.suitelhy.dingding.security.service.api.domain.vo.Security;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.user.service.api.domain.service.write.non.idempotence.UserNonIdempotentWriteService;
import github.com.suitelhy.dingding.user.service.provider.domain.event.UserEvent;
import github.com.suitelhy.dingding.user.service.provider.domain.service.UserAccountOperationInfoService;
import github.com.suitelhy.dingding.user.service.provider.domain.service.UserService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 用户 - 业务接口
 *
 * @Design
 * · 写入操作
 * · 非幂等性
 *
 * @see User
 * @see UserAccountOperationInfo
 * @see Dubbo.Strategy.ClusterVo#FAIL_FAST
 */
@Service(cluster = "failfast")
public class UserNonIdempotentWriteServiceImpl
        implements UserNonIdempotentWriteService {

    @Autowired
    private UserEvent userEvent;

    @Autowired
    private UserService userService;

    @Autowired
    private UserAccountOperationInfoService userAccountOperationInfoService;

    @Reference
    private SecurityUserReadService securityUserReadService;

    @Reference
    private SecurityUserRoleReadService securityUserRoleReadService;

    /**
     * 新增一个用户
     *
     * @Description 完整业务的一部分.
     *
     * @param user                   预期新增的用户 -> 用户基础信息
     * @param operator               操作者
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     *
     * @throws IllegalArgumentException
     * @throws BusinessAtomicException
     */
    @Override
    public boolean insert(@NotNull User user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == operator || operator.isEmpty()) {
            //-- 非法输入: 操作者
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        final @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
        return userService.insert(user, operator, operator_UserAccountOperationInfo);
    }

    /**
     * 更新指定的用户
     *
     * @param user      被修改的用户
     * @param operator  操作者
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean update(@NotNull User user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == operator || operator.isEmpty()) {
            //-- 非法输入: 操作者
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        final @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
        return userService.update(user, operator, operator_UserAccountOperationInfo);
    }

    /**
     * 更新指定的用户 -> [用户 - 基础信息]
     *
     * @param old_user      {@linkplain User 被修改的[用户 - 基础信息] <- 原始版本业务全量数据}
     * @param new_user_data {@linkplain Map 被修改的[用户 - 基础信息] <- 需要更新的数据}
     * · 数据结构:
     * {
     *  “password”: [用户密码_明文],
     *  “status”: [账号状态]
     * }
     * @param operator      {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean update(@NotNull User old_user, @NotNull Map<String, Object> new_user_data, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == old_user || old_user.isEmpty()) {
            //-- 非法输入: 被修改的[用户 - 基础信息] <- 原始版本业务全量数据
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "被修改的[用户 - 基础信息] <- 原始版本业务全量数据"
                    , old_user
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == new_user_data
                || (! new_user_data.containsKey("password") && ! new_user_data.containsKey("status")))
        {
            //-- 非法输入: 被修改的[用户 - 基础信息] <- 需要更新的数据
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "被修改的[用户 - 基础信息] <- 需要更新的数据"
                    , new_user_data
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
        if (! securityUserReadService.existAdminPermission(operator.getUsername())) {
            //-- 非法输入: 操作者
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】<-【%s】</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , "没有所需的操作权限"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        final @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
        final @NotNull List<SecurityRole> operator_roles = userEvent.selectRoleOnUserByUsername(operator.getUsername());
        @NotNull SecurityRole operator_role = SecurityRole.Factory.ROLE.createDefault();
        final @NotNull SecurityUserRole operator_userRole;

        if (! operator_roles.isEmpty()) {
            for (@NotNull SecurityRole eachRole : operator_roles) {
                Security.RoleVo eachRoleVo = SecurityRole.changeIntoRoleVo(eachRole);
                if (null != eachRoleVo && eachRoleVo.isAdministrator()) {
                    operator_role = eachRole;
                    break;
                }
            }
        }
        if (operator_role.isEmpty()) {
            //-- 非法输入: 操作者
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】<-【%s】</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , "没有所需的操作权限"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        operator_userRole = securityUserRoleReadService.selectByUsernameAndRoleCode(operator.getUsername(), operator_role.getCode());

        return userService.update(old_user, new_user_data, operator
                , operator_UserAccountOperationInfo, operator_userRole, operator_role);
    }

    /**
     * 删除指定的用户
     *
     * @param user      被删除的用户
     * @param operator  操作者 (与{@param user}一致, 或拥有足够高的权限)
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean delete(@NotNull User user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == operator || operator.isEmpty()) {
            //-- 非法输入: 操作者
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
        return userService.delete(user, operator, operator_userAccountOperationInfo);
    }

}
