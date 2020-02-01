package github.com.suitelhy.dingding.sso.application.task;

import github.com.suitelhy.dingding.sso.infrastructure.application.dto.UserDto;

import java.util.List;

/**
 * 用户 - 任务调度接口
 */
public interface UserTask {

    /**
     * 查询用户列表
     * @param pageCount 页码, 从1开始
     * @param pageSize
     * @return 用户 Entity 对象集合
     */
    List<UserDto> selectAll(int pageCount, int pageSize);

    /**
     * 查询指定的用户
     * @param userid
     * @return 用户 Entity 对象
     */
    UserDto selectUserByUserid(String userid);

    /**
     * 查询指定的用户
     * @param username
     * @return
     */
    UserDto selectUserByUsername(String username);

    /**
     * 查询用户列表 - 分页 - 总页数
     * @param pageSize 分页 - 每页容量
     * @return 分页 - 总页数
     */
    Long selectCount(int pageSize);

    /**
     * 新增一个用户
     * @param user
     * @return 业务操作是否成功
     */
    boolean insert(UserDto user
            , String password
            , String ip
            , String lasttime);

    /**
     * 更新指定的用户
     * @param user
     * @return 业务操作是否成功
     */
    boolean update(UserDto user
            , String password
            , String ip
            , String lasttime);

    /**
     * 删除指定的用户
     * @param user
     * @return 业务操作是否成功
     */
    boolean delete(UserDto user, String password);

}
