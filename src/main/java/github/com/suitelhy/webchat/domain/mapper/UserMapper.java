package github.com.suitelhy.webchat.domain.mapper;

import github.com.suitelhy.webchat.domain.entity.User;
import github.com.suitelhy.webchat.domain.entity.sql.provider.UserSqlProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 用户数据基础交互业务接口
 */
@Mapper
public interface UserMapper {

    //===== select data =====//
    /**
     * 查询指定范围的用户信息
     * @param offset
     * @param limit
     * @return
     */
    @SelectProvider(value = UserSqlProvider.class, method = "selectAll")
    List<User> selectAll(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 查询用户信息总数
     * @return
     */
    @SelectProvider(value = UserSqlProvider.class, method = "selectCount")
    User selectCount();

    /**
     * 查询指定的用户信息
     * @param userid
     * @return
     */
    @SelectProvider(value = UserSqlProvider.class, method = "selectUserByUserid")
    User selectUserByUserid(@Param("userid") String userid);

    //===== insert data =====//
    /**
     * 新增一条用户信息
     * @param user
     * @return
     */
    @UpdateProvider(value = UserSqlProvider.class, method = "insert")
    boolean insert(@Param("user") User user);

    //===== update data =====//
    /**
     * 修改指定的用户信息
     * @param user
     * @return
     */
    @UpdateProvider(value = UserSqlProvider.class, method = "update")
    boolean update(@Param("user") User user);

    //===== delete data =====//
    /**
     * 删除指定的用户信息
     * @param user
     * @return
     */
    @UpdateProvider(value = UserSqlProvider.class, method = "delete")
    boolean delete(@Param("user") User user);

}
