package github.com.suitelhy.dingding.security.service.provider.domain.service.api.write.non.idempotence.impl;

import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityResource;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.security.service.api.domain.service.write.non.idempotence.SecurityResourceNonIdempotentService;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.security.service.provider.domain.service.SecurityResourceService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * (安全) 资源
 *
 * @Description (安全) 资源 - 业务接口.
 *
 * @Design
 * · 写入操作
 * · 非幂等性
 *
 * @see github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo.Strategy.ClusterVo#FAIL_FAST
 */
@Service(cluster = "failfast")
public class SecurityResourceNonIdempotentServiceImpl
        implements SecurityResourceNonIdempotentService {

    @Autowired
    private SecurityResourceService securityResourceService;

    /**
     * 更新指定的资源
     *
     * @param old_resource      {@linkplain SecurityResource [（安全认证）资源]} <- 原始版本业务全量数据
     * @param new_resource_data [（安全认证）资源] <- 需要更新的数据
     *                          · 数据结构:
     *                          {
     *                          “resource_icon”: [图标],
     *                          “resource_link“: [资源链接],
     *                          “resource_name“: [资源名称],
     *                          “resource_parentCode“: [父节点 <- 资源编码],
     *                          “resource_sort“: [序号],
     *                          “resource_type“: [资源类型]
     *                          }
     * @param operator          {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean update(@NotNull SecurityResource old_resource, @NotNull Map<String, Object> new_resource_data, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityResourceService.update(old_resource, new_resource_data, operator);
    }

    /**
     * 删除指定的资源
     *
     * @Description 删除成功后校验持久化数据; 主要为了避免在未提交的事务中进行对操作结果的非预期判断.
     * · 完整业务流程的一部分
     *
     * @param resource {@linkplain SecurityResource [（安全认证）资源]}
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean delete(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityResourceService.delete(resource, operator);
    }

}
