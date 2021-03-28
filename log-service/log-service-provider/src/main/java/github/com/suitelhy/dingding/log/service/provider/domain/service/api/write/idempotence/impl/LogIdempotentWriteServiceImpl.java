package github.com.suitelhy.dingding.log.service.provider.domain.service.api.write.idempotence.impl;

import github.com.suitelhy.dingding.log.service.api.domain.entity.Log;
import github.com.suitelhy.dingding.log.service.api.domain.service.write.idempotence.LogIdempotentWriteService;
import github.com.suitelhy.dingding.log.service.provider.domain.service.LogService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;

/**
 * 日志记录 - 业务接口实现
 *
 * @Design
 * · 写入操作
 * · 幂等性
 *
 * @see github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo.Strategy.ClusterVo#FORKING
 */
@Service(cluster = "forking")
public class LogIdempotentWriteServiceImpl
        implements LogIdempotentWriteService {

    @Autowired
    private LogService logService;

    /**
     * 新增日志记录
     *
     * @param log {@linkplain Log 日志记录}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在有效数据}
     */
    @Override
    public boolean insert(@NotNull Log log)
            throws IllegalArgumentException
    {
        return logService.insert(log);
    }

    /**
     * 删除日志记录
     *
     * @param id {@linkplain Log.Validator#id(String) [日志记录 - 编号]}
     *
     * @return {@link Boolean#TYPE 操作是否成功 / 指定日志记录是否已被删除}
     */
    @Override
    public boolean deleteById(@NotNull Long id)
            throws IllegalArgumentException
    {
        return logService.deleteById(id);
    }

}
