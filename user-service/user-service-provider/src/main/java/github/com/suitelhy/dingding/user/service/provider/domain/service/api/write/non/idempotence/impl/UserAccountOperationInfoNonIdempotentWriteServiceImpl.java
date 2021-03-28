package github.com.suitelhy.dingding.user.service.provider.domain.service.api.write.non.idempotence.impl;

import github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUserRole;
import github.com.suitelhy.dingding.security.service.api.domain.service.read.SecurityUserReadService;
import github.com.suitelhy.dingding.security.service.api.domain.service.read.SecurityUserRoleReadService;
import github.com.suitelhy.dingding.security.service.api.domain.vo.Security;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserPersonInfo;
import github.com.suitelhy.dingding.user.service.api.domain.service.write.non.idempotence.UserAccountOperationInfoNonIdempotentWriteService;
import github.com.suitelhy.dingding.user.service.provider.domain.event.UserEvent;
import github.com.suitelhy.dingding.user.service.provider.domain.service.UserAccountOperationInfoService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 用户 -> 账户操作记录 - 业务接口
 *
 * @Design
 * · 写入操作
 * · 非幂等性
 *
 * @see UserAccountOperationInfo
 * @see Dubbo.Strategy.ClusterVo#FAIL_FAST
 */
@Service(cluster = "failfast")
public class UserAccountOperationInfoNonIdempotentWriteServiceImpl
        implements UserAccountOperationInfoNonIdempotentWriteService {

    @Autowired
    private UserAccountOperationInfoService userAccountOperationInfoService;

    @Autowired
    private UserEvent userEvent;

    @Reference
    private SecurityUserReadService securityUserReadService;

    @Reference
    private SecurityUserRoleReadService securityUserRoleReadService;

    /**
     * 新增一条记录
     *
     * @param user     {@linkplain UserAccountOperationInfo [用户 -> 账户操作基础记录]}
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean insert(@NotNull UserAccountOperationInfo user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return userAccountOperationInfoService.insert(user, operator);
    }

    /**
     * 更新指定的用户 -> [用户 -> 账户操作基础记录]
     *
     * @param old_userAccountOperationInfo      {@linkplain UserAccountOperationInfo 被修改的[用户 -> 账户操作基础记录] <- 原始版本业务全量数据}
     * @param new_userAccountOperationInfo_data {@linkplain Map 被修改的[用户 -> 账户操作基础记录] <- 需要更新的数据}
     * · 数据结构:
     * {
     *  "ip": [最后登陆 IP],
     *  "lastLoginTime": 最后登录时间
     * }
     * @param operator                          {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean update(@NotNull UserAccountOperationInfo old_userAccountOperationInfo, @NotNull Map<String, Object> new_userAccountOperationInfo_data, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == old_userAccountOperationInfo || old_userAccountOperationInfo.isEmpty()) {
            //-- 非法输入: 被修改的[用户 - 账户操作基础记录] <- 原始版本业务全量数据
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "被修改的[用户 - 账户操作基础记录] <- 原始版本业务全量数据"
                    , old_userAccountOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == new_userAccountOperationInfo_data
                || (! new_userAccountOperationInfo_data.containsKey("ip") && ! new_userAccountOperationInfo_data.containsKey("lastLoginTime")))
        {
            //-- 非法输入: 被修改的[用户 - 账户操作基础记录] <- 需要更新的数据
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "被修改的[用户 - 账户操作基础记录] <- 需要更新的数据"
                    , new_userAccountOperationInfo_data
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

        final @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());
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

        return userAccountOperationInfoService.update(old_userAccountOperationInfo, new_userAccountOperationInfo_data, operator
                , operator_UserAccountOperationInfo, operator_userRole, operator_role);
    }

    /**
     * 删除指定的记录
     *
     * @param user     {@linkplain UserAccountOperationInfo [用户 -> 账户操作基础记录]}
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean delete(@NotNull UserAccountOperationInfo user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return userAccountOperationInfoService.delete(user, operator);
    }

}
