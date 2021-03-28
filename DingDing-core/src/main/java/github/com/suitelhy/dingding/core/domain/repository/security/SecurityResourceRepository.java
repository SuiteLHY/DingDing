//package github.com.suitelhy.dingding.core.domain.repository.security;
//
//import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResource;
//import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResourceUrl;
//import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole;
//import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRoleResource;
//import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityRepository;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.lang.Nullable;
//import org.springframework.transaction.annotation.Isolation;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.validation.constraints.NotNull;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
///**
// * 资源
// *
// * @Description 资源 - 基础交互业务接口.
// */
//public interface SecurityResourceRepository
//        extends JpaRepository<SecurityResource, Long>, EntityRepository {
//
//    //===== Select Data =====//
//
//    /**
//     * 查询总数
//     *
//     * @return
//     */
//    @Override
//    long count();
//
//    /**
//     * 查询父节点资源编码对应的资源数
//     *
//     * @param parentCode
//     * @return
//     */
//    long countByParentCode(@NotNull String parentCode);
//
//    /**
//     * 判断存在
//     *
//     * @param entityId {@link SecurityResource#id()}
//     * @return
//     * @see SecurityResource#id()
//     */
//    /*@Lock(LockModeType.PESSIMISTIC_WRITE)*/
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED)
//    boolean existsByCode(@NotNull String entityId);
//
//    /**
//     * 判断存在
//     *
//     * @param code
//     * @param parentCode
//     * @return
//     */
//    /*@Lock(LockModeType.PESSIMISTIC_WRITE)*/
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED)
//    boolean existsByCodeAndParentCode(@NotNull String code, @NotNull String parentCode);
//
//    /**
//     * 查询所有
//     *
//     * @param pageable
//     * @return {@link org.springframework.data.domain.Page}
//     */
//    @Override
//    @NotNull
//    Page<SecurityResource> findAll(Pageable pageable);
//
//    /**
//     * 查询
//     *
//     * @param entityId {@link SecurityResource#id()}
//     * @return {@link java.util.Optional}
//     * @see SecurityResource#id()
//     */
//    @NotNull
//    Optional<SecurityResource> findSecurityResourceByCode(@NotNull String entityId);
//
//    /**
//     * 查询父节点资源编码对应的资源
//     *
//     * @param parentCode
//     * @param pageable
//     * @return {@link org.springframework.data.domain.Page}
//     */
//    @NotNull
//    Page<SecurityResource> findAllByParentCode(@NotNull String parentCode, Pageable pageable);
//
//    /**
//     * 查询所有 URL - ROLE 权限对应关系
//     *
//     * @return
//     */
//    @Query(nativeQuery = true
//            , value = "select sru.url_path as url_path "
//            + ", sru.url_method as url_method "
//            + ", role.`code` as role_code "
//            + ", sru.client_id as client_id "
//            + "from SECURITY_RESOURCE_URL sru "
//            + "left join SECURITY_RESOURCE resource on resource.code = sru.code "
//            + "left join SECURITY_ROLE_RESOURCE rr on rr.resource_code = resource.code "
//            + "left join SECURITY_ROLE role on role.code = rr.role_code "
//            + "left join SECURITY_USER_ROLE ur on ur.role_code = rr.role_code "
//            + "left join SECURITY_USER user on user.username = ur.username "
//            + "where user.username is not null "
//            + "group by url_path, url_method, role_code ")
//    List<Map<String, Object>> selectAllUrlRoleMap();
//
//    /**
//     * 查询指定资源服务器的所有 URL - ROLE 权限对应关系
//     *
//     * @param clientId 资源服务器 ID; {@link SecurityResourceUrl#getClientId()}
//     * @return
//     */
//    @Query(nativeQuery = true
//            , value = "select sru.url_path as url_path "
//            + ", sru.url_method as url_method "
//            + ", role.`code` as role_code "
//            + ", sru.client_id as client_id "
//            + "from SECURITY_RESOURCE_URL sru "
//            + "left join SECURITY_RESOURCE resource on resource.code = sru.code "
//            + "left join SECURITY_ROLE_RESOURCE rr on rr.resource_code = resource.code "
//            + "left join SECURITY_ROLE role on role.code = rr.role_code "
//            + "left join SECURITY_USER_ROLE ur on ur.role_code = rr.role_code "
//            + "left join SECURITY_USER user on user.username = ur.username "
//            + "where user.username is not null and sru.client_id = :clientId "
//            + "group by url_path, url_method, role_code ")
//    List<Map<String, Object>> selectUrlRoleMap(@NotNull @Param("clientId") String clientId);
//
//    /**
//     * 查询资源
//     *
//     * @param code 资源编码    {@link SecurityResource#getCode()}
//     * @return 结果集
//     * @see SecurityRole
//     * @see SecurityRoleResource
//     * @see SecurityResource
//     */
//    @Query(nativeQuery = true
//            , value = "select role.`code` as code "
//            + ", role.`name` as name "
//            + ", role.description as description "
//            + "from SECURITY_ROLE role "
//            + "left join SECURITY_ROLE_RESOURCE rr on rr.role_code = role.`code` "
//            + "left join SECURITY_RESOURCE resource on resource.`code` = rr.resource_code "
//            + "where resource.`code` = :code ")
//    List<Map<String, Object>> selectRoleByCode(@Param("code") String code);
//
//    /**
//     * 查询 URL
//     *
//     * @param code 资源编码    {@link SecurityResource#getCode()}
//     * @return 结果集
//     * @see SecurityResource
//     * @see SecurityResourceUrl
//     */
//    @Query(nativeQuery = true
//            , value = "select ru.url_path as url_path "
//            + "from SECURITY_RESOURCE_URL ru "
//            + "left join SECURITY_RESOURCE r on r.code = ru.code "
//            + "where ru.code = :code ")
//    List<Map<String, Object>> selectUrlByCode(@NotNull @Param("code") String code);
//
//    //===== Insert Data =====//
//
//    /**
//     * 新增/更新日志记录
//     *
//     * @param resource
//     * @return {@link SecurityResource}
//     */
//    @Override
//    @Modifying
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED)
//    SecurityResource saveAndFlush(SecurityResource resource);
//
//    //===== Delete Data =====//
//
//    /**
//     * 删除
//     *
//     * @param id 记录ID
//     */
//    @Override
//    @Modifying
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED)
//    void deleteById(@NotNull Long id);
//
//    /**
//     * 删除
//     *
//     * @param code
//     */
//    @Modifying
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED)
//    long removeByCode(@NotNull String code);
//
//    /**
//     * 删除
//     *
//     * @param code
//     * @param parentCode
//     * @return
//     */
//    @Modifying
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED)
//    long removeByCodeAndParentCode(@NotNull String code, @Nullable String parentCode);
//
//}
