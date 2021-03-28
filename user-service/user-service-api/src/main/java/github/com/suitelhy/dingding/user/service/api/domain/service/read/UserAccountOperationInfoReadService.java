package github.com.suitelhy.dingding.user.service.api.domain.service.read;

import github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo;
import github.com.suitelhy.dingding.core.infrastructure.domain.PageImpl;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserAccountOperationInfo;

import javax.validation.constraints.NotNull;

/**
 * 用户 -> 账户操作记录 - 业务接口
 *
 * @Design
 * · 只读操作
 * · 幂等性
 *
 * @see UserAccountOperationInfo
 * @see Dubbo.Strategy.ClusterVo#FAILOVER
 */
public interface UserAccountOperationInfoReadService {

    //===== 查询操作业务 =====//

    /**
     * 查询记录列表
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     *
     * @return {@link PageImpl}
     */
    @NotNull PageImpl<UserAccountOperationInfo> selectAll(int pageIndex, int pageSize);

    /**
     * 查询记录列表 - 分页 - 总页数
     *
     * @param pageSize 分页 - 每页容量
     *
     * @return 分页 - 总页数
     */
    @NotNull Long selectCount(int pageSize);

    /**
     * 查询指定的记录
     *
     * @param id
     *
     * @return {@link UserAccountOperationInfo}
     */
    @NotNull UserAccountOperationInfo selectById(@NotNull String id);

    /**
     * 查询指定的记录
     *
     * @param username
     *
     * @return {@link UserAccountOperationInfo}
     */
    @NotNull UserAccountOperationInfo selectByUsername(@NotNull String username);

}
