package github.com.suitelhy.dingding.security.service.api.domain.service.write.non.idempotence;

import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUserRole;

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
public interface SecurityUserRoleNonIdempotentService {
}
