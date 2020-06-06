package github.com.suitelhy.dingding.core.domain.repository.security;

import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 日志记录 - 基础业务
 *
 * @Description 日志记录数据 - 基础交互业务接口.
 */
/*// 此处选择使用 Mybatis-Spring 的XML文件配置方式实现 mapper, 用来演示复杂SQL情景下的一种设计思路:
//-> 聚焦于 SQL.
@Mapper*/
public interface SecurityUserRepository
        extends JpaRepository<SecurityUser, Long> {

    //===== Select Data =====//

    /**
     * 查询总数
     *
     * @return
     */
    @Override
    long count();

    /**
     * 查询所有
     *
     * @param pageable
     * @return
     */
    @Override
    Page<SecurityUser> findAll(Pageable pageable);

    /**
     * 查询
     *
     * @param username
     * @param pageable
     * @return
     */
    List<SecurityUser> findByUsername(String username, Pageable pageable);

    /**
     * 查询角色
     *
     * @param username
     * @return
     */
    @Query(nativeQuery = true
            , value = "select ur.role_code as code, r.name as name from SECURITY_USER_ROLE ur "
                    + "left join SECURITY_ROLE r on ur.role_code = r.code "
                    + "where ur.username = :username")
    List<Map<String, Object>> selectRoleByUsername(String username);

    //===== Insert Data =====//

    /**
     * 新增/更新日志记录
     *
     * @param securityUser
     * @return
     */
    @Override
    SecurityUser saveAndFlush(SecurityUser securityUser);

    //===== Delete Data =====//

    /**
     * 删除指定的日志记录
     *
     * @param userId    用户ID
     */
    @Modifying
    @Override
    @Transactional
    void deleteById(Long userId);

    @Modifying
    @Transactional
    long removeByUsername(String username);

}
