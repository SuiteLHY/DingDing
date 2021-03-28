//package github.com.suitelhy.dingding.core.domain.repository.security;
//
//import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole;
//import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser;
//import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUserRole;
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
// * （安全认证）用户 ←→ 角色
// *
// * @Description [（安全认证）用户 ←→ 角色]关联关系 -> 基础交互业务接口.
// */
//public interface SecurityUserRoleRepository
//        extends JpaRepository<SecurityUserRole, Long>, EntityRepository {
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
//     * @param roleCode {@link SecurityUserRole.Validator#roleCode(String)}
//     * @return
//     * @Description Returns whether an entity with the given parameters exists.
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED)
//    boolean existsAllByRoleCode(@NotNull String roleCode);
//
//    /**
//     * 判断存在
//     *
//     * @param username {@link SecurityUserRole#getUsername()}
//     * @return
//     * @Description Returns whether an entity with the given parameters exists.
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED)
//    boolean existsAllByUsername(@NotNull String username);
//
//    /**
//     * 判断存在
//     *
//     * @param username {@link SecurityUserRole#getUsername()}
//     * @param roleCode {@link SecurityUserRole#getRoleCode()}
//     * @return
//     * @Description Returns whether an entity with the given parameters exists.
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED)
//    boolean existsByUsernameAndRoleCode(@NotNull String username, @NotNull String roleCode);
//
//    /**
//     * 查询
//     *
//     * @param username {@link SecurityUserRole.Validator#username(String)}
//     * @param roleCode {@link SecurityUserRole.Validator#roleCode(String)}
//     * @return {@link Page}
//     */
//    Optional<SecurityUserRole> findByUsernameAndRoleCode(@NotNull String username, @NotNull String roleCode);
//
//    /**
//     * 查询所有
//     *
//     * @param username {@link SecurityUserRole#getUsername()}
//     * @param pageable {@link Pageable}
//     * @return {@link Page}
//     */
//    Page<SecurityUserRole> findAllByUsername(@NotNull String username, Pageable pageable);
//
//    /**
//     * 查询所有
//     *
//     * @param username {@link SecurityUserRole#getUsername()}
//     * @return {@link List}
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED)
//    List<SecurityUserRole> findAllByUsername(@NotNull String username);
//
//    /**
//     * 查询所有
//     *
//     * @param rodeCode {@link SecurityUserRole#getRoleCode()}
//     * @param pageable {@link Pageable}
//     * @return {@link Page}
//     */
//    Page<SecurityUserRole> findAllByRoleCode(@NotNull String rodeCode, Pageable pageable);
//
//    /**
//     * 查询所有
//     *
//     * @param rodeCode {@link SecurityUserRole#getRoleCode()}
//     * @return {@link List}
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED)
//    List<SecurityUserRole> findAllByRoleCode(@NotNull String rodeCode);
//
//    /**
//     * 查询 (关联的) 角色
//     *
//     * @param username
//     * @return {@link SecurityRole}, {@link Map}
//     * @see SecurityUser
//     * @see SecurityUserRole
//     * @see SecurityRole
//     */
//    @Query(nativeQuery = true
//            , value = "select r.id as role_id \n"
//            + ", r.`code` as role_code \n"
//            + ", r.name as role_name \n"
//            + ", r.description as role_description \n"
//            + "from security_user u \n"
//            + "left join security_user_role ur on ur.username = u.username \n"
//            + "left join security_role r on r.`code` = ur.role_code \n"
//            + "where u.username = :username ")
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED)
//    List<Map<String, Object>> selectRoleByUsername(@NotNull @Param("username") String username);
//
//    //===== Insert Data =====//
//
//    /**
//     * 新增/更新
//     *
//     * @param entity
//     * @return {@link SecurityUserRole}
//     */
//    @Override
//    @Modifying
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED)
//    SecurityUserRole saveAndFlush(@NotNull SecurityUserRole entity);
//
//    //===== Delete Data =====//
//
//    /**
//     * 删除
//     *
//     * @param id 数据ID        {@link SecurityUserRole#getId()}
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
//     * @param username 用户名     {@link SecurityUserRole#getUsername()}
//     * @return
//     */
//    @Modifying
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED)
//    long removeByUsername(@NotNull String username);
//
//    /**
//     * 删除
//     *
//     * @param roleCode 角色编码    {@link SecurityUserRole#getRoleCode()}
//     * @return
//     */
//    @Modifying
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED)
//    long removeByRoleCode(@NotNull String roleCode);
//
//    /**
//     * 删除
//     *
//     * @param username 用户名     {@link SecurityUserRole#getUsername()}
//     * @param roleCode 角色编码    {@link SecurityUserRole#getRoleCode()}
//     * @return
//     */
//    @Modifying
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED)
//    long removeByUsernameAndRoleCode(@NotNull String username, @NotNull String roleCode);
//
//}
