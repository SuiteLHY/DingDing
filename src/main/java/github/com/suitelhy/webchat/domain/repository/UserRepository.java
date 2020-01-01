package github.com.suitelhy.webchat.domain.repository;

import github.com.suitelhy.webchat.domain.entity.User;
import github.com.suitelhy.webchat.infrastructure.domain.sql.provider.UserSqlProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户信息 - 实体 (Entity) 持久化接口
 */
// @Mapper 注解: 标记持久化对象 ({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER}).
//-> 需要注意的是: 被 @Mapper 标记的接口不能定义重载方法; 因为通过 @Mapper 注入的实现类, 其所有方法的参数列表一致.
// 参考资料: <a href="https://blog.csdn.net/phenomenonsTell/article/details/79033144">
//-> 【CSDN】MyBatis中的@Mapper注解及配套注解使用详解（上）</a>
@Mapper
@Repository("userRepository")
public interface UserRepository {

    //===== select data =====//
    /**
     * 查询指定范围的用户信息
     * @param offset 起点数据索引, 第一条数据对应的 <param>offset</param> 为 <code>0</code>
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
