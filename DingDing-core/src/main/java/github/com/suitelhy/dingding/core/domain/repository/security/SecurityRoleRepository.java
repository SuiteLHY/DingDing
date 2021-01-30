package github.com.suitelhy.dingding.core.domain.repository.security;

import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole;
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
 * (安全 - 用户) 角色
 *
 * @Description (安全 - 用户) 角色 - 基础交互业务接口.
 */
public interface SecurityRoleRepository
        extends JpaRepository<SecurityRole, Long>, EntityRepository {

    //===== Select Data =====//

    /**
     * 查询总数
     *
     * @return
     */
    @Override
    long count();

    /**
     * 查询指定的角色名对应的数据数量
     *
     * @param name
     * @return
     */
    long countByName(String name);

    /**
     * 判断存在
     *
     * @param entityId {@link SecurityRole#id()}
     * @return 判断结果
     * @see SecurityRole#id()
     */
    /*@Lock(LockModeType.PESSIMISTIC_WRITE)*/
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    boolean existsByCode(@NotNull String entityId);

    /**
     * 查询所有
     *
     * @param pageable
     * @return
     */
    @Override
    Page<SecurityRole> findAll(Pageable pageable);

    /**
     * 查询指定角色
     *
     * @param entityId {@link SecurityRole#id()}
     * @return {@link Optional}
     * @see SecurityRole#id()
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    Optional<SecurityRole> findByCode(String entityId);

    /**
     * 查询
     *
     * @param name
     * @param pageable
     * @return
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    List<SecurityRole> findByName(String name, Pageable pageable);

    /**
     * 查询资源
     *
     * @param code
     * @return
     */
    @Query(nativeQuery = true
            , value = "select resource.`code` as code "
            + ", resource.icon as icon "
            + ", resource.link as link "
            + ", resource.`name` as name "
            + ", resource.parent_code as parent_code "
            + ", resource.sort as sort "
            + ", resource.type as type "
            + "from SECURITY_RESOURCE resource "
            + "left join SECURITY_ROLE_RESOURCE rr on rr.resource_code = resource.code "
            + "where rr.role_code = :code ")
    List<Map<String, Object>> selectResourceByCode(@Param("code") String code);

    /**
     * 查询 URL
     *
     * @param code
     * @return
     */
    @Query(nativeQuery = true
            , value = "select ru.url_path as url_path "
            + "from SECURITY_RESOURCE_URL ru "
            + "left join SECURITY_RESOURCE r on r.code = ru.code "
            + "left join SECURITY_ROLE_RESOURCE rr on rr.resource_code = r.code "
            + "where rr.role_code = :code ")
    List<Map<String, Object>> selectUrlByCode(@Param("code") String code);


    //===== Insert Data =====//

    /**
     * 新增/更新日志记录
     *
     * @param role
     * @return
     */
    @Override
    @Modifying
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    SecurityRole saveAndFlush(SecurityRole role);

    //===== Delete Data =====//

    /**
     * 删除指定角色
     *
     * @param id ID
     */
    @Override
    @Modifying
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    void deleteById(Long id);

    /**
     * 删除指定角色
     *
     * @param code 角色编码
     */
    @Modifying
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    long removeByCode(String code);

}
