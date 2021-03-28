package github.com.suitelhy.dingding.user.service.provider.domain.service.api.write.idempotence.impl;

import github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.security.service.api.domain.entity.User;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.user.service.api.domain.service.write.idempotence.UserIdempotentWriteService;
import github.com.suitelhy.dingding.user.service.provider.domain.service.UserAccountOperationInfoService;
import github.com.suitelhy.dingding.user.service.provider.domain.service.UserService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;

/**
 * 用户 - 业务接口
 *
 * @Design
 * · 写入操作
 * · 非幂等性
 *
 * @see User
 * @see UserAccountOperationInfo
 * @see Dubbo.Strategy.ClusterVo#FORKING
 */
@Service(cluster = "forking")
public class UserIdempotentWriteServiceImpl
        implements UserIdempotentWriteService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserAccountOperationInfoService userAccountOperationInfoService;

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
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        final @NotNull UserAccountOperationInfo operator_OperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
        return userService.update(user, operator, operator_OperationInfo);
    }

    /**
     * 删除指定的用户
     *
     * @Description 删除成功后校验持久化数据; 主要是避免在未提交的事务中进行对操作结果的非预期判断.
     *
     * @param user      被删除的用户
     * @param operator  操作者 (与{@param user}一致, 或拥有足够高的权限)
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 指定数据是否已被删除}
     */
    @Override
    public boolean deleteAndValidate(@NotNull User user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == operator || operator.isEmpty()) {
            //-- 非法输入: 操作者
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
        return userService.deleteAndValidate(user, operator, operator_userAccountOperationInfo);
    }

}
