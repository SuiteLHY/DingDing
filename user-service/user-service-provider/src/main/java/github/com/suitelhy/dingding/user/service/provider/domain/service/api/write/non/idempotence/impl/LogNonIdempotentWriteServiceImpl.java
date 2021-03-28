package github.com.suitelhy.dingding.user.service.provider.domain.service.api.write.non.idempotence.impl;

import github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo;
import github.com.suitelhy.dingding.user.service.api.domain.entity.Log;
import github.com.suitelhy.dingding.user.service.api.domain.service.write.non.idempotence.LogNonIdempotentWriteService;
import org.apache.dubbo.config.annotation.Service;

import javax.validation.constraints.NotNull;

/**
 * 日志记录 - 业务接口
 *
 * @Design
 * · 写入操作
 * · 非幂等性
 *
 * @see Log
 * @see Dubbo.Strategy.ClusterVo#FAIL_FAST
 */
@Service(cluster = "failfast")
public class LogNonIdempotentWriteServiceImpl
        implements LogNonIdempotentWriteService {

    /**
     * 新增日志记录
     *
     * @param log {@link Log}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean insert(@NotNull Log log) throws IllegalArgumentException {
        return false;
    }

    /**
     * 删除日志记录
     *
     * @param id [日志记录 - 编号] {@link Log.Validator#id(String)}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean deleteById(@NotNull Long id) throws IllegalArgumentException {
        return false;
    }

}
