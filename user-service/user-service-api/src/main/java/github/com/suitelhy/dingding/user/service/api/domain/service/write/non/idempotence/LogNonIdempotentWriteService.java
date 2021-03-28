package github.com.suitelhy.dingding.user.service.api.domain.service.write.non.idempotence;

import github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo;
import github.com.suitelhy.dingding.user.service.api.domain.entity.Log;

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
public interface LogNonIdempotentWriteService {

    //===== 添加操作 =====//

    /**
     * 新增日志记录
     *
     * @param log {@link Log}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    boolean insert(@NotNull Log log)
            throws IllegalArgumentException;

    //===== 删除操作 =====//

    /**
     * 删除日志记录
     *
     * @param id [日志记录 - 编号] {@link Log.Validator#id(String)}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    boolean deleteById(@NotNull Long id)
            throws IllegalArgumentException;

}
