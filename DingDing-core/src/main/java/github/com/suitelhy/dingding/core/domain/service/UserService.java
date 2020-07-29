package github.com.suitelhy.dingding.core.domain.service;

import github.com.suitelhy.dingding.core.domain.entity.User;
import github.com.suitelhy.dingding.core.domain.service.impl.UserServiceImpl;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityService;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

/**
 * 用户 - 业务接口
 *
 * @see User
 * @see UserServiceImpl
 */
@Transactional(isolation = Isolation.READ_COMMITTED
        , propagation = Propagation.REQUIRED
        , readOnly = true
        , rollbackFor = Exception.class
        , timeout = 15)
public interface UserService
        extends EntityService {

    /**
     * 查询用户列表
     *
     * @param pageIndex     分页索引, 从0开始
     * @param pageSize      分页 - 每页容量
     * @return {@link org.springframework.data.domain.Page}
     */
    Page<User> selectAll(int pageIndex, int pageSize);

    /**
     * 查询用户列表 - 分页 - 总页数
     *
     * @param pageSize  分页 - 每页容量
     * @return 分页 - 总页数
     */
    Long selectCount(int pageSize);

    /**
     * 查询指定的用户
     *
     * @param userid
     * @return
     */
    User selectUserByUserid(@NotNull String userid);

    /**
     * 查询指定的用户
     *
     * @param username
     * @return
     */
    User selectUserByUsername(@NotNull String username);

    /**
     * 新增一个用户
     *
     * @param user
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean insert(@NotNull User user);

    /**
     * 更新指定的用户
     *
     * @param user
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean update(@NotNull User user);

    /**
     * 删除指定的用户
     *
     * @param user
     * @return 操作是否成功
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean delete(@NotNull User user);

    /**
     * 删除指定的用户
     *
     * @param user
     * @return 操作是否成功
     * @Description 删除成功后校验持久化数据; 主要是
     * -> 避免在未提交的事务中进行对操作结果的非预期判断.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean deleteAndValidate(@NotNull User user);

}
