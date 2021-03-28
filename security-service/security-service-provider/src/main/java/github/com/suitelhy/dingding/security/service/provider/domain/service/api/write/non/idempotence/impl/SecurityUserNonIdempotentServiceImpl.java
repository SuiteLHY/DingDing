package github.com.suitelhy.dingding.security.service.provider.domain.service.api.write.non.idempotence.impl;

import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.security.service.api.domain.service.write.non.idempotence.SecurityRoleNonIdempotentService;
import github.com.suitelhy.dingding.security.service.api.domain.service.write.non.idempotence.SecurityUserNonIdempotentService;
import github.com.suitelhy.dingding.security.service.provider.domain.service.SecurityUserService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * (安全) 用户 - 业务
 *
 * @Description (安全) 用户 - 业务接口.
 *
 * @Design
 * · 写入操作
 * · 非幂等性
 *
 * @see SecurityUser
 * @see SecurityRoleNonIdempotentService
 * @see github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo.Strategy.ClusterVo#FAIL_FAST
 */
@Service(cluster = "failfast")
public class SecurityUserNonIdempotentServiceImpl
        implements SecurityUserNonIdempotentService {

    @Autowired
    private SecurityUserService securityUserService;

}
