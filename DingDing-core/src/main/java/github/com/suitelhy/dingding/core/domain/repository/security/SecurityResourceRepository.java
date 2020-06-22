package github.com.suitelhy.dingding.core.domain.repository.security;

import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResource;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResourceUrl;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRoleResource;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 资源
 *
 * @Description 资源 - 基础交互业务接口.
 */
public interface SecurityResourceRepository
        extends JpaRepository<SecurityResource, Long>, EntityRepository {

    //===== Select Data =====//

    /**
     * 查询总数
     *
     * @return
     */
    @Override
    long count();

    /**
     * 查询父节点资源编码对应的资源数
     *
     * @param parentCode
     * @return
     */
    long countByParentCode(@NotNull String parentCode);

    /**
     * 判断存在
     *
     * @param entityId          {@link SecurityResource#id()}
     * @return
     * @see SecurityResource#id()
     */
    /*@Lock(LockModeType.PESSIMISTIC_WRITE)*/
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    boolean existsByCode(@NotNull String entityId);

    /**
     * 判断存在
     *
     * @param code
     * @param parentCode
     * @return
     */
    /*@Lock(LockModeType.PESSIMISTIC_WRITE)*/
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    boolean existsByCodeAndParentCode(@NotNull String code, @NotNull String parentCode);

    /**
     * 查询所有
     *
     * @param pageable
     * @return  {@link org.springframework.data.domain.Page}
     */
    @Override
    Page<SecurityResource> findAll(Pageable pageable);

    /**
     * 查询
     *
     * @param entityId          {@link SecurityResource#id()}
     * @return  {@link java.util.Optional}
     * @see SecurityResource#id()
     */
    Optional<SecurityResource> findByCode(@NotNull String entityId);

    /**
     * 查询父节点资源编码对应的资源
     *
     * @param parentCode
     * @param pageable
     * @return {@link org.springframework.data.domain.Page}
     */
    Page<SecurityResource> findAllByParentCode(@NotNull String parentCode, Pageable pageable);

    /**
     * 查询资源
     *
     * @param code          资源编码    {@link SecurityResource#getCode()}
     * @return 结果集
     * @see SecurityRole
     * @see SecurityRoleResource
     * @see SecurityResource
     */
    @Query(nativeQuery = true
            , value = "select role.`code` as code "
                    + ", role.`name` as name "
                    + ", role.description as description "
                    + "from SECURITY_ROLE role "
                    + "left join SECURITY_ROLE_RESOURCE rr on rr.role_code = role.`code` "
                    + "left join SECURITY_RESOURCE resource on resource.`code` = rr.resource_code "
                    + "where resource.`code` = :code ")
    List<Map<String, Object>> selectRoleByCode(@Param("code") String code);

    /**
     * 查询 URL
     *
     * @param code          资源编码    {@link SecurityResource#getCode()}
     * @return 结果集
     * @see SecurityResource
     * @see SecurityResourceUrl
     */
    @Query(nativeQuery = true
            , value = "select ru.url_path as url_path "
                    + "from SECURITY_RESOURCE_URL ru "
                    + "left join SECURITY_RESOURCE r on r.code = ru.code "
                    + "where ru.code = :code ")
    List<Map<String, Object>> selectUrlByCode(@NotNull @Param("code") String code);

    //===== Insert Data =====//

    /**
     * 新增/更新日志记录
     *
     * @param resource
     * @return {@link github.com.suitelhy.dingding.core.domain.entity.security.SecurityResource}
     */
    @Override
    @Modifying
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    SecurityResource saveAndFlush(SecurityResource resource);

    //===== Delete Data =====//

    /**
     * 删除
     *
     * @param id 记录ID
     */
    @Override
    @Modifying
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    void deleteById(@NotNull Long id);

    /**
     * 删除
     *
     * @param code
     */
    @Modifying
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    long removeByCode(@NotNull String code);

    /**
     * 删除
     *
     * @param code
     * @param parentCode
     * @return
     */
    @Modifying
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    long removeByCodeAndParentCode(@NotNull String code, @Nullable String parentCode);

}
