package github.com.suitelhy.dingding.security.service.api.domain.service.write.idempotence;

import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityResourceUrl;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;

import javax.validation.constraints.NotNull;

/**
 * [（安全认证）资源 ←→ URL]
 *
 * @Description [（安全认证）资源 ←→ 资源]关联关系 - 业务接口.
 *
 * @Design
 * · 写入操作
 * · 幂等性
 *
 * @see SecurityResourceUrl
 * @see github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo.Strategy.ClusterVo#FORKING
 */
public interface SecurityResourceUrlIdempotentService {

    /**
     * 新增一个[（安全认证）资源 ←→ URL]关联关系
     *
     * @param resourceUrl   {@linkplain SecurityResourceUrl [（安全认证）资源 ←→ URL]}
     * @param operator      {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在相同的有效数据}
     */
    boolean insert(@NotNull SecurityResourceUrl resourceUrl, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

}
