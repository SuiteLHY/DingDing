//package github.com.suitelhy.dingding.core.domain.repository.security;
//
//import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRoleResource;
//import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityRepository;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
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
// * 角色 - 资源
// *
// * @Description 角色 - 资源 关联关系 -> 基础交互业务接口.
// */
//public interface SecurityRoleResourceRepository
//        extends JpaRepository<SecurityRoleResource, Long>, EntityRepository {
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
//     * 判断存在
//     *
//     * @param resourceCode 资源编码
//     * @return
//     */
//    /*@Lock(LockModeType.PESSIMISTIC_WRITE)*/
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED)
//    boolean existsAllByResourceCode(@NotNull String resourceCode);
//
//    /**
//     * 判断存在
//     *
//     * @param roleCode 角色编码
//     * @return
//     */
//    /*@Lock(LockModeType.PESSIMISTIC_WRITE)*/
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED)
//    boolean existsAllByRoleCode(@NotNull String roleCode);
//
//    /**
//     * 判断存在
//     *
//     * @param roleCode     角色编码
//     * @param resourceCode 资源编码
//     * @return
//     */
//    /*@Lock(LockModeType.PESSIMISTIC_WRITE)*/
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED)
//    boolean existsByRoleCodeAndResourceCode(@NotNull String roleCode, @NotNull String resourceCode);
//
//    /**
//     * 查询
//     *
//     * @param roleCode     角色编码
//     * @param resourceCode 资源编码
//     * @return
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED)
//    Optional<SecurityRoleResource> findByRoleCodeAndResourceCode(@NotNull String roleCode, @NotNull String resourceCode);
//
//    /**
//     * 查询所有
//     *
//     * @param resourceCode 资源编码
//     * @return
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED)
//    List<SecurityRoleResource> findAllByResourceCode(@NotNull String resourceCode);
//
//    /**
//     * 查询所有
//     *
//     * @param resourceCode 资源编码
//     * @param pageable     {@link Pageable}
//     * @return {@link Page}
//     */
//    Page<SecurityRoleResource> findAllByResourceCode(@NotNull String resourceCode, Pageable pageable);
//
//    /**
//     * 查询所有
//     *
//     * @param roleCode 角色编码
//     * @return
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED)
//    List<SecurityRoleResource> findAllByRoleCode(@NotNull String roleCode);
//
//    /**
//     * 查询所有
//     *
//     * @param roleCode 角色编码
//     * @param pageable {@link Pageable}
//     * @return {@link Page}
//     */
//    Page<SecurityRoleResource> findAllByRoleCode(@NotNull String roleCode, Pageable pageable);
//
//    /**
//     * 查询所有
//     *
//     * @param rodeCode 角色编码
//     * @return
//     */
//    @Query(nativeQuery = true
//            , value = "select resource.`code` as `code` "
//            + ", resource.`name` as `name` "
//            + "from security_role r "
//            + "left join security_role_resource rr on rr.role_code = r.code "
//            + "left join security_resource resource on resource.`code` = rr.resource_code "
//            + "where r.`code` = :rodeCode ")
//    List<Map<String, Object>> selectResourceByRoleCode(@Param("rodeCode") String rodeCode);
//
//    //===== Insert Data =====//
//
//    /**
//     * 新增/更新日志记录
//     *
//     * @param entity {@link SecurityRoleResource}
//     * @return {@link SecurityRoleResource}
//     */
//    @Override
//    @Modifying
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED)
//    SecurityRoleResource saveAndFlush(SecurityRoleResource entity);
//
//    //===== Delete Data =====//
//
//    /**
//     * 删除
//     *
//     * @param id 数据ID
//     */
//    @Override
//    @Modifying
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED)
//    void deleteById(Long id);
//
//    /**
//     * 删除
//     *
//     * @param resourceCode 资源编码
//     * @return
//     */
//    @Modifying
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED)
//    long removeByResourceCode(String resourceCode);
//
//    /**
//     * 删除
//     *
//     * @param roleCode 角色编码
//     * @return
//     */
//    @Modifying
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED)
//    long removeByRoleCode(String roleCode);
//
//    /**
//     * 删除
//     *
//     * @param roleCode     角色编码
//     * @param resourceCode 资源编码
//     * @return
//     */
//    @Modifying
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED)
//    long removeByRoleCodeAndResourceCode(String roleCode, String resourceCode);
//
//}
