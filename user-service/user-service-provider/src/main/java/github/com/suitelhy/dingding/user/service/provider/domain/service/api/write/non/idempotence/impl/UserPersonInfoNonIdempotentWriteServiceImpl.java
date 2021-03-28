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
import github.com.suitelhy.dingding.user.service.api.domain.service.write.non.idempotence.UserPersonInfoNonIdempotentWriteService;
import github.com.suitelhy.dingding.user.service.provider.domain.event.UserEvent;
import github.com.suitelhy.dingding.user.service.provider.domain.service.UserPersonInfoService;
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
 * @see UserPersonInfo
 * @see Dubbo.Strategy.ClusterVo#FAIL_FAST
 */
@Service(cluster = "failfast")
public class UserPersonInfoNonIdempotentWriteServiceImpl
        implements UserPersonInfoNonIdempotentWriteService {

    @Autowired
    private UserPersonInfoService userPersonInfoService;

    @Autowired
    private UserEvent userEvent;

    @Reference
    private SecurityUserReadService securityUserReadService;

    @Reference
    private SecurityUserRoleReadService securityUserRoleReadService;

    /**
     * 新增一条记录
     *
     * @param user     {@linkplain UserPersonInfo [用户 -> 个人信息]}
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean insert(@NotNull UserPersonInfo user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return userPersonInfoService.insert(user, operator);
    }

    /**
     * 更新指定的用户 -> [用户 -> 个人信息]
     *
     * @param old_userPersonInfo      {@linkplain UserPersonInfo 被修改的[用户 -> 个人信息] <- 原始版本业务全量数据}
     * @param new_userPersonInfo_data {@linkplain Map 被修改的[用户 -> 个人信息] <- 需要更新的数据}
     * · 数据结构:
     * {
     *  "nickname": [用户 - 昵称],
     *  "age": [用户 - 年龄],
     *  "faceImage": [用户 - 头像],
     *  "introduction": [用户 - 简介],
     *  "sex": [用户 - 性别]
     * }
     * @param operator                {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean update(@NotNull UserPersonInfo old_userPersonInfo, @NotNull Map<String, Object> new_userPersonInfo_data, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == old_userPersonInfo || old_userPersonInfo.isEmpty()) {
            //-- 非法输入: 被修改的[用户 - 个人信息] <- 原始版本业务全量数据
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "被修改的[用户 - 个人信息] <- 原始版本业务全量数据"
                    , old_userPersonInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == new_userPersonInfo_data
                || (! new_userPersonInfo_data.containsKey("nickname") && ! new_userPersonInfo_data.containsKey("age") && ! new_userPersonInfo_data.containsKey("faceImage") && ! new_userPersonInfo_data.containsKey("introduction") && ! new_userPersonInfo_data.containsKey("sex")))
        {
            //-- 非法输入: 被修改的[用户 - 个人信息] <- 需要更新的数据
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "被修改的[用户 - 个人信息] <- 需要更新的数据"
                    , new_userPersonInfo_data
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
        final boolean userPersonInfoAndOperatorEqualsFlag;
        if (! (userPersonInfoAndOperatorEqualsFlag = old_userPersonInfo.equals(operator))
                && ! securityUserReadService.existAdminPermission(operator.getUsername()))
        {
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
                if (null != eachRoleVo
                        && (userPersonInfoAndOperatorEqualsFlag || eachRoleVo.isAdministrator()))
                {
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

        return userPersonInfoService.update(old_userPersonInfo, new_userPersonInfo_data, operator
                , operator_UserAccountOperationInfo, operator_userRole, operator_role);
    }

    /**
     * 删除指定的记录
     *
     * @param user     {@linkplain UserPersonInfo [用户 -> 个人信息]}
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean delete(@NotNull UserPersonInfo user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return userPersonInfoService.delete(user, operator);
    }

}
