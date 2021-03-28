package github.com.suitelhy.dingding.security.service.api.domain.service.read;

import github.com.suitelhy.dingding.core.infrastructure.domain.PageImpl;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityRole;
//import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;

/**
 * (安全) 角色
 *
 * @Description (安全) 角色 - 业务接口.
 *
 * @Design
 * · 只读操作
 * · 幂等性
 *
 * @see SecurityRole
 * @see github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo.Strategy.ClusterVo#FAILOVER
 */
public interface SecurityRoleReadService {

    //===== 查询数据业务操作 =====//

    /**
     * 判断存在
     *
     * @param code  {@linkplain SecurityRole 角色编码}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    @NotNull Boolean existsByCode(@NotNull String code)
            throws IllegalArgumentException;

    /**
     * 查询所有
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     *
     * @return {@link PageImpl}
     */
    @NotNull PageImpl<SecurityRole> selectAll(int pageIndex, int pageSize)
            throws IllegalArgumentException;

    /**
     * 查询总页数
     *
     * @Description 查询数据列表 - 分页 - 总页数
     *
     * @param pageSize [分页 - 每页容量]
     *
     * @return [分页 - 总页数]
     */
    @NotNull Long selectCount(int pageSize)
            throws IllegalArgumentException;

    /**
     * 查询指定的角色
     *
     * @param code  {@linkplain SecurityRole 角色编码}
     *
     * @return {@link SecurityRole}
     */
    @NotNull SecurityRole selectRoleByCode(@NotNull String code)
            throws IllegalArgumentException;

}
