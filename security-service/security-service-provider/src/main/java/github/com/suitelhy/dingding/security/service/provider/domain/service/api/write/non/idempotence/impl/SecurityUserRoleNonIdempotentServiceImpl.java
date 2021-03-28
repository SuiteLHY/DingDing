package github.com.suitelhy.dingding.security.service.provider.domain.service.api.write.non.idempotence.impl;

import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUserRole;
import github.com.suitelhy.dingding.security.service.api.domain.service.write.non.idempotence.SecurityUserRoleNonIdempotentService;
import github.com.suitelhy.dingding.security.service.provider.domain.service.SecurityUserRoleService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * （安全认证）用户 ←→ 角色
 *
 * @Description [（安全认证）用户 ←→ 角色]关联关系 - 业务接口.
 *
 * @Design
 * · 写入操作
 * · 非幂等性
 *
 * @see SecurityUserRole
 * @see github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo.Strategy.ClusterVo#FAIL_FAST
 */
@Service(cluster = "failfast")
public class SecurityUserRoleNonIdempotentServiceImpl
        implements SecurityUserRoleNonIdempotentService {

    @Autowired
    private SecurityUserRoleService securityUserRoleService;

}
