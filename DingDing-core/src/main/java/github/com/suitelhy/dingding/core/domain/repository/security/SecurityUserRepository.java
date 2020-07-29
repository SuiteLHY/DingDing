package github.com.suitelhy.dingding.core.domain.repository.security;

import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * (安全) 用户
 *
 * @Description (安全) 用户 - 基础交互业务接口.
 *
 * @see SecurityUser
 */
public interface SecurityUserRepository
        extends JpaRepository<SecurityUser, String>, EntityRepository {

    //===== Select Data =====//

    /**
     * 查询总数
     *
     * @return
     */
    @Override
    long count();

    /**
     * 判断存在
     *
     * @param entityId      {@link SecurityUser#id()}
     * @return
     * @see SecurityUser#id()
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    boolean existsByUsername(@NotNull String entityId);

    /**
     * 查询所有
     *
     * @param pageable
     * @return {@link Page}
     */
    @Override
    Page<SecurityUser> findAll(Pageable pageable);

    /**
     * 查询
     *
     * @param username
     * @return {@link Optional}
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    Optional<SecurityUser> findByUsername(@NotNull String username);

    /**
     * 查询角色
     *
     * @param username
     * @return
     */
    @Query(nativeQuery = true
            , value = "select ur.role_code as code, r.name as name from SECURITY_USER_ROLE ur "
                    + "left join SECURITY_ROLE r on ur.role_code = r.code "
                    + "where ur.username = :username ")
    List<Map<String, Object>> selectRoleByUsername(@NotNull @Param("username") String username);

    /**
     * 查询资源
     *
     * @param username
     * @return
     */
    @Query(nativeQuery = true
            , value = "select sr.code as code "
                    + ", sr.icon as icon "
                    + ", sr.link as link "
                    + ", sr.name as name "
                    + ", sr.parent_code as parent_code "
                    + ", sr.sort as sort "
                    + ", sr.type as type "
                    + "from SECURITY_RESOURCE sr "
                    + "left join SECURITY_ROLE_RESOURCE rr on rr.resource_code = sr.code "
                    + "left join SECURITY_USER_ROLE ur on ur.role_code = rr.role_code "
                    + "where ur.username = :username "
                    + "group by code, name ")
    List<Map<String, Object>> selectResourceByUsername(@NotNull @Param("username") String username);

    /**
     * 查询 URL
     *
     * @param username
     * @return
     */
    @Query(nativeQuery = true
            , value = "select sru.client_id as client_id "
                    + ", sru.url_path as url_path "
                    + "from SECURITY_RESOURCE_URL sru "
                    + "left join SECURITY_ROLE_RESOURCE rr on rr.resource_code = sru.code "
                    + "left join SECURITY_USER_ROLE ur on ur.role_code = rr.role_code "
                    + "where ur.username = :username and sru.client_id = :clientId "
                    + "group by url_path ")
    List<Map<String, Object>> selectURLByUsernameAndClientId(@NotNull @Param("username") String username
            , @NotNull @Param("clientId") String clientId);

    //===== Insert Data =====//

    /**
     * 新增/更新日志记录
     *
     * @param securityUser          {@link SecurityUser}
     * @return {@link SecurityUser}
     */
    @Override
    @Modifying
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    SecurityUser saveAndFlush(@NotNull SecurityUser securityUser);

    //===== Delete Data =====//

    /**
     * 删除指定用户
     *
     * @param userId    用户ID
     */
    @Override
    @Modifying
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    void deleteById(@NotNull String userId);

    /**
     * 删除指定用户
     *
     * @param username  用户名
     * @return
     */
    @Modifying
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    long removeByUsername(@NotNull String username);

}
