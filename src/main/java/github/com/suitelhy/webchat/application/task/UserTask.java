package github.com.suitelhy.webchat.application.task;

import github.com.suitelhy.webchat.domain.entity.User;

import java.util.List;

/**
 * 用户 - 任务调度接口
 */
public interface UserTask {

    /**
     * 查询用户列表
     * @param dataIndex 从第 <param>dataIndex</param> + 1 条数据开始查询
     * @param pageSize
     * @return 用户 Entity 对象集合
     */
    List<User> selectAll(int dataIndex, int pageSize);

    /**
     * 查询指定的用户
     * @param userid
     * @return 用户 Entity 对象
     */
    User selectUserByUserid(String userid);

    /**
     * 查询用户列表 - 分页 - 总页数
     * @param pageSize 分页 - 每页容量
     * @return 分页 - 总页数
     */
    Integer selectCount(int pageSize);

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

}
