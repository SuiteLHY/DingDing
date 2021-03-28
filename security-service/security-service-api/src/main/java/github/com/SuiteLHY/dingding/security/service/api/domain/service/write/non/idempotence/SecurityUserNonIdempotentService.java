package github.com.suitelhy.dingding.security.service.api.domain.service.write.non.idempotence;

import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;

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
public interface SecurityUserNonIdempotentService {
}
