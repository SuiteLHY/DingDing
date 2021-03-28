package github.com.suitelhy.dingding.log.service.api.domain.service.write.idempotence;

import github.com.suitelhy.dingding.log.service.api.domain.entity.Log;

import javax.validation.constraints.NotNull;

/**
 * 日志记录 - 业务接口
 *
 * @Design
 * · 写入操作
 * · 幂等性
 *
 * @see github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo.Strategy.ClusterVo#FORKING
 */
public interface LogIdempotentWriteService {

    /**
     * 新增日志记录
     *
     * @param log {@linkplain Log 日志记录}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在有效数据}
     */
    boolean insert(@NotNull Log log)
            throws IllegalArgumentException;

    /**
     * 删除日志记录
     *
     * @param id    {@linkplain Log.Validator#id(String) [日志记录 - 编号]}
     *
     * @return {@link Boolean#TYPE 操作是否成功 / 指定日志记录是否已被删除}
     */
    boolean deleteById(@NotNull Long id)
            throws IllegalArgumentException;

}
