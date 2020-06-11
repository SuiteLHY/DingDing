package github.com.suitelhy.dingding.core.domain.service.security;

import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;

/**
 * (安全) 用户
 *
 * @Description (安全) 用户 - 业务接口.
 */
public interface SecurityUserService {

    /**
     * 查询所有
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize
     * @return
     */
    Page<SecurityUser> selectAll(int pageIndex, int pageSize);

    /**
     * 查询总页数
     *
     * @Description 查询数据列表 - 分页 - 总页数
     *
     * @param pageSize 分页 - 每页容量
     * @return 分页 - 总页数
     */
    Long selectCount(int pageSize);

    /**
     * 查询指定的用户
     *
     * @param userId
     * @return
     */
    SecurityUser selectUserByUserId(@NotNull String userId);

    /**
     * 查询指定的用户
     *
     * @param username
     * @return
     */
    SecurityUser selectUserByUsername(@NotNull String username);

    /**
     * 新增一个用户
     *
     * @param user
     * @return 操作是否成功
     */
    boolean insert(@NotNull SecurityUser user);

    /**
     * 更新指定的用户
     *
     * @param user
     * @return 操作是否成功
     */
    boolean update(@NotNull SecurityUser user);

    /**
     * 删除指定的用户
     *
     * @param user
     * @return 操作是否成功
     */
    boolean delete(@NotNull SecurityUser user);

    /**
     * 删除指定的用户
     *
     * @Description 删除成功后校验持久化数据;
     *->    主要为了避免在未提交的事务中进行对操作结果的非预期判断.
     *
     * @param user
     * @return 操作是否成功
     */
    boolean deleteAndValidate(@NotNull SecurityUser user);

}
