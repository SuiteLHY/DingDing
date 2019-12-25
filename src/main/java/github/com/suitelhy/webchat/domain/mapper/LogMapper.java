package github.com.suitelhy.webchat.domain.mapper;

import github.com.suitelhy.webchat.domain.entity.Log;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 日志记录数据基础交互业务接口
 */
// @Mapper 注解: 参考自 <a href="https://blog.csdn.net/phenomenonsTell/article/details/79033144">
//-> 【CSDN】MyBatis中的@Mapper注解及配套注解使用详解（上）</a>; 需要注意的是: @Mapper 修饰的类中
//-> 不能重载
/*@Mapper*/
// 使用 SpringFramework 的 xml 配置方式实现 mapper.
@Repository
public interface LogMapper {

    //===== select data =====//
    /**
     * 查询指定范围的日志记录
     * @param offset
     * @param limit
     * @return
     */
    @Select("")
    List<Log> selectAll(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 查询日志记录总数
     * @return
     */
    Log selectCount();

    Log selectCountByUserid(@Param("userid") String userid);

    /**
     * 查询指定用户的日志记录
     * @param userid
     * @param offset
     * @param limit
     * @return
     */
    List<Log> selectLogByUserid(@Param("userid") String userid
            , @Param("offset") int offset
            , @Param("limit") int limit);

    //===== insert data =====//
    boolean insert(Log log);

    //===== update data =====//

    //===== delete data =====//
    /**
     * 删除指定记录ID的日志记录
     * @param id
     * @return
     */
    boolean delete(String id);

    /**
     * 删除指定用户对应的所有日志记录
     * @param userid
     * @return
     */
    boolean deleteThisUser(String userid);

    // 参考项目中提供了 deleteAll 业务接口; 如果更深入一些, 在实际生产情景下,
    //-> 不应该提供这类不严谨的业务接口 (...), 删除操作也仅限于用户权限以内.
    // 即便是删除指定账户, 也应该在数据库中保留必要的信息以供备案 (常见的策略
    //-> 是提供业务层面的删除接口, 而持久层数据不做修改或者在保留必要信息的基
    //-> 础上改变状态).
    /*boolean deleteAll();*/

}
