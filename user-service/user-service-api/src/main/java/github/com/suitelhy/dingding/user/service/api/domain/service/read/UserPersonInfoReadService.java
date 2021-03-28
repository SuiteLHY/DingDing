package github.com.suitelhy.dingding.user.service.api.domain.service.read;

import github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo;
import github.com.suitelhy.dingding.core.infrastructure.domain.PageImpl;
import github.com.suitelhy.dingding.security.service.api.domain.entity.User;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserPersonInfo;

import javax.validation.constraints.NotNull;

/**
 * 用户 -> 账户操作记录 - 业务接口
 *
 * @Design
 * · 只读操作
 * · 幂等性
 *
 * @see UserPersonInfo
 * @see Dubbo.Strategy.ClusterVo#FAILOVER
 */
public interface UserPersonInfoReadService {

    //===== 查询操作业务 =====//

    /**
     * 查询记录列表
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     *
     * @return {@link PageImpl}
     */
    @NotNull PageImpl<UserPersonInfo> selectAll(int pageIndex, int pageSize)
            throws IllegalArgumentException;

    /**
     * 查询记录列表 - 分页 - 总页数
     *
     * @param pageSize 分页 - 每页容量
     *
     * @return 分页 - 总页数
     */
    @NotNull Long selectCount(int pageSize)
            throws IllegalArgumentException;

    /**
     * 查询指定的记录
     *
     * @param id 数据 ID   {@link UserPersonInfo.Validator#id(String)}
     *
     * @return {@link UserPersonInfo}
     */
    @NotNull UserPersonInfo selectById(@NotNull String id)
            throws IllegalArgumentException;

    /**
     * 查询指定的记录
     *
     * @param username 用户名称    {@link User.Validator#username(String)}
     *
     * @return {@link UserPersonInfo}
     */
    @NotNull UserPersonInfo selectByUsername(@NotNull String username)
            throws IllegalArgumentException;

    /**
     * 查询指定的记录
     *
     * @param nickname 用户 - 昵称 {@link UserPersonInfo.Validator#nickname(String)}
     *
     * @return {@link UserPersonInfo}
     */
    @NotNull UserPersonInfo selectByNickname(@NotNull String nickname)
            throws IllegalArgumentException;

}
