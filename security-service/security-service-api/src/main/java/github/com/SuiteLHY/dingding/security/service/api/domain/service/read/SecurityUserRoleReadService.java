package github.com.suitelhy.dingding.security.service.api.domain.service.read;

import github.com.suitelhy.dingding.core.infrastructure.domain.PageImpl;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUserRole;
//import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * （安全认证）用户 ←→ 角色
 *
 * @Description [（安全认证）用户 ←→ 角色]关联关系 - 业务接口.
 *
 * @Design
 * · 只读操作
 * · 幂等性
 *
 * @see SecurityUserRole
 * @see github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo.Strategy.ClusterVo#FAILOVER
 */
public interface SecurityUserRoleReadService {

    /**
     * 查询所有
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     *
     * @return {@link PageImpl}
     */
    @NotNull PageImpl<SecurityUserRole> selectAll(int pageIndex, int pageSize)
            throws IllegalArgumentException;

    /**
     * 查询总页数
     *
     * @Description 查询数据列表 - 分页 - 总页数
     *
     * @param pageSize 分页 - 每页容量
     *
     * @return 分页 - 总页数
     */
    @NotNull Long selectCount(int pageSize)
            throws IllegalArgumentException;

    /**
     * 查询
     *
     * @param username  {@linkplain SecurityUserRole.Validator#username(String) 用户名称}
     *
     * @return {@link SecurityUserRole}
     */
    @NotNull List<SecurityUserRole> selectByUsername(@NotNull String username)
            throws IllegalArgumentException;

    /**
     * 查询
     *
     * @param roleCode  {@linkplain SecurityUserRole.Validator#roleCode(String) 角色编码}
     *
     * @return {@link SecurityUserRole}
     */
    @NotNull List<SecurityUserRole> selectByRoleCode(@NotNull String roleCode)
            throws IllegalArgumentException;

    /**
     * 查询
     *
     * @param username  {@linkplain SecurityUserRole.Validator#username(String) 用户名称}
     * @param roleCode  {@linkplain SecurityUserRole.Validator#roleCode(String) 角色编码}
     *
     * @return {@link SecurityUserRole}
     */
    @NotNull SecurityUserRole selectByUsernameAndRoleCode(@NotNull String username, @NotNull String roleCode)
            throws IllegalArgumentException;

}
