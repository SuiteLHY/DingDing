package github.com.suitelhy.dingding.security.service.provider.domain.service.api.write.idempotence.impl;

import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityResource;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.security.service.api.domain.service.write.idempotence.SecurityResourceIdempotentService;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.security.service.provider.domain.service.SecurityResourceService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;

/**
 * (安全) 资源
 *
 * @Description (安全) 资源 - 业务接口.
 *
 * @Design
 * · 写入操作
 * · 幂等性
 *
 * @see github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo.Strategy.ClusterVo#FORKING
 */
@Service(cluster = "forking")
public class SecurityResourceIdempotentServiceImpl
        implements SecurityResourceIdempotentService {

    @Autowired
    private SecurityResourceService securityResourceService;

    /**
     * 新增一个资源
     *
     * @param resource {@linkplain SecurityResource [（安全认证）资源]}
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在相同的有效数据}
     */
    @Override
    public boolean insert(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityResourceService.insert(resource, operator);
    }

    /**
     * 更新指定的资源
     *
     * @param resource {@linkplain SecurityResource [（安全认证）资源]}
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     *
     * @throws IllegalArgumentException
     * @throws BusinessAtomicException
     */
    @Override
    public boolean update(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityResourceService.update(resource, operator);
    }

}
