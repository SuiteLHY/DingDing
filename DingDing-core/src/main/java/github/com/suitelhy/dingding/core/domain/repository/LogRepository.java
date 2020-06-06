package github.com.suitelhy.dingding.core.domain.repository;

import github.com.suitelhy.dingding.core.domain.entity.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 日志记录 - 基础业务
 *
 * @Description 日志记录数据 - 基础交互业务接口.
 */
/*// 此处选择使用 Mybatis-Spring 的XML文件配置方式实现 mapper, 用来演示复杂SQL情景下的一种设计思路:
//-> 聚焦于 SQL.
@Mapper*/
public interface LogRepository
        extends JpaRepository<Log, Long> {

    //===== Select Data =====//

    /**
     * 查询日志记录总数
     *
     * @return
     */
    @Override
    long count();

    /**
     * 查询指定用户对应的日志记录数
     *
     * @param userid
     * @return
     */
    long countByUserid(String userid);

    /**
     * 查询所有日志记录
     *
     * @param pageable
     * @return
     */
    @Override
    Page<Log> findAll(Pageable pageable);

    /**
     * 查询指定用户的日志记录
     *
     * @param userid
     * @param pageable
     * @return
     */
    List<Log> findByUserid(String userid, Pageable pageable);

    //===== Insert Data =====//

    /**
     * 新增/更新日志记录
     *
     * @param log
     * @return
     */
    @Override
    Log saveAndFlush(Log log);

    //===== Delete Data =====//

    /**
     * 删除指定的日志记录
     *
     * @param id 记录ID
     */
    @Override
    void deleteById(Long id);

    /**
     * 删除指定用户对应的所有日志记录
     *
     * @param userid
     */
    long removeByUserid(String userid);

    // 参考项目中提供了 deleteAll 业务接口; 如果更深入一些, 在实际生产情景下,
    //-> 不应该提供这类不严谨的业务接口 (...), 删除操作也仅限于用户权限以内.
    // 即便是删除指定账户, 也应该在数据库中保留必要的信息以供备案 (常见的策略
    //-> 是提供业务层面的删除接口, 而持久层数据不做修改或者在保留必要信息的基
    //-> 础上改变状态).
    /*boolean deleteAll();*/

}
