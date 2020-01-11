package github.com.suitelhy.webchat.domain.service;

import github.com.suitelhy.webchat.domain.entity.User;

import java.util.List;

/**
 * 用户 - 业务接口
 */
public interface UserService {

    /**
     * 查询用户列表
     * @param dataIndex 从第 <param>dataIndex</param> + 1 条数据开始查询
     * @param pageSize
     * @return 用户 Entity 对象集合
     */
    List<User> selectAll(int dataIndex, int pageSize);

    /**
     * 查询用户列表 - 分页 - 总页数
     * @param pageSize 分页 - 每页容量
     * @return 分页 - 总页数
     */
    Integer selectCount(int pageSize);

    /**
     * 查询指定的用户
     * @param userid
     * @return 用户 Entity 对象
     */
    User selectUserByUserid(String userid);

    /**
     * 新增一个用户
     * @param user
     * @return 操作是否成功
     */
    boolean insert(User user);

    /**
     * 更新指定的用户
     * @param user
     * @return 操作是否成功
     */
    boolean update(User user);

    /**
     * 删除指定的用户
     * @param user
     * @return 操作是否成功
     */
    boolean delete(User user);

    /**
     * 删除指定的用户
     * @Description 删除成功后校验持久化数据; 主要是
     *-> 避免在未提交的事务中进行对操作结果的非预期判断.
     * @param user
     * @return 操作是否成功
     */
    boolean deleteAndValidate(User user);

}
