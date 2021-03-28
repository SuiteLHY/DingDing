package github.com.suitelhy.dingding.user.service.provider.domain.repository;

import github.com.suitelhy.dingding.security.service.api.domain.entity.User;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityRepository;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.Set;

/**
 * 用户信息 - 持久化接口
 *
 * @Description 用户信息 - 实体 (Entity) 持久化接口.
 * @see User
 */
/*// @Mapper 注解: 标记持久化对象 ({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER}).
//-> 需要注意的是: 被 @Mapper 标记的接口不能定义重载方法; 因为通过 @Mapper 注入的实现类, 其所有方法的参数列表一致.
// 参考资料: <a href="https://blog.csdn.net/phenomenonsTell/article/details/79033144">
//-> 【CSDN】MyBatis中的@Mapper注解及配套注解使用详解（上）</a>
@Mapper*/
/*@Repository("userRepository")*/
// @NoRepositoryBean: Spring Data不应在运行时为该存储库自动注入实例. <delete>此处仅为了定义明确和避免拓展接口时出现问题.</delete>
//-> 然而用了就无法对该接口声明任何 Bean !... (orz)
/*@NoRepositoryBean*/
public interface UserRepository
        extends JpaRepository<User, String>, EntityRepository {

    //===== Select Data =====//

    /**
     * 查询用户总数
     *
     * @return
     */
    @Override
    long count();

    /**
     * 判断存在
     *
     * @param username
     * @return
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    boolean existsByUsername(@NotNull String username);

    /**
     * 判断存在
     *
     * @param username
     * @param statusVoSet
     * @return
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    boolean existsByUsernameAndStatusIn(@NotNull String username, @NotNull Set<Account.StatusVo> statusVoSet);

    /**
     * 查询所有用户列表
     *
     * @param pageable
     * @return
     */
    @Override
    @NotNull
    Page<User> findAll(Pageable pageable);

    /**
     * 查询用户
     *
     * @param userid
     * @return {@link Optional}
     */
    /*@Query("select u from User u where u.userid = ?1")*/
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    @NotNull
    Optional<User> findUserByUserid(@NotNull String userid);

    /**
     * 查询用户
     *
     * @param username
     * @return
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    @NotNull
    Optional<User> findUserByUsername(@NotNull String username);

    /**
     * 查询用户
     *
     * @param username
     * @param status
     * @return
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    @NotNull
    Optional<User> findUserByUsernameAndStatus(@NotNull String username, @NotNull Account.StatusVo status);

    /**
     * 查询用户
     *
     * @param username
     * @param statusVoSet
     * @return
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    @NotNull
    Optional<User> findUserByUsernameAndStatusIn(@NotNull String username, @NotNull Set<Account.StatusVo> statusVoSet);

    //===== Insert Data =====//

    /**
     * 新增(/修改)用户
     *
     * @param user
     * @return
     */
    @Override
    @Modifying
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    User saveAndFlush(@NotNull User user);

    //===== Update Data =====//

    /**
     * 修改指定用户信息
     *
     * @param status
     * @param username
     * @return {@link Integer#TYPE}
     */
    @Modifying
    @Query("update User u set u.status = ?1 where u.username = ?2 ")
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    int modifyStatusByUsername(@NotNull Account.StatusVo status, @NotNull String username);

    //===== Delete Data =====//

//    /**
//     * 删除指定用户
//     *
//     * @param id
//     */
//    @Modifying
//    /*@Query("delete from User where userid = ?1")*/
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED)
//    void deleteByIdAndStatus(Long id, Integer status);

    /**
     * 删除给定的实体
     *
     * @param user
     * @throws IllegalArgumentException                               - in case the given entity is null.
     * @throws org.springframework.dao.EmptyResultDataAccessException - in case the given entity is non persistent.
     * @Description Spring Data JPA 提供的 <method>delete(T entity)</method> 无法保证操作 <b>执行且成功</b>
     * , 应该换用 <method>deleteById(String id)</method>.
     * @Reference {@link <a href="https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html#delete-T-">CrudRepository (Spring Data Core 2.2.3.RELEASE API) # delete-T-</a>}
     * @Update 不建议使用!
     */
    @Override
    @Modifying
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    void delete(@NotNull User user);

    /**
     * 删除给定的 id 所指定的实体
     *
     * @param id
     * @throws IllegalArgumentException                               - in case the given id is null.
     * @throws org.springframework.dao.EmptyResultDataAccessException - in case the given id -> entity is non persistent.
     * @Description 可以保证删除操作 <b>执行且成功</b>.
     * @Reference {@link <a href="https://stackoverflow.com/questions/31346356/spring-data-jpa-deleteby-query-not-working?noredirect=1&lq=1">Spring Data JPA DeleteBy查询不起作用 - Stack Overflow</a>}
     * {@link <a href="https://stackoverflow.com/questions/23723025/spring-data-delete-by-is-supported">Spring Data: "delete by" is supported? - Stack Overflow</a>}
     */
    @Override
    @Modifying
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    void deleteById(@NotNull String id);

    //----- !DANGER: 禁止提供基于此类判断条件不够严谨的删除方法的业务接口!
    //------> 判断条件应该是包含 <method>id()</method> 的交集!
    //------> 虽然目前业务设计是以 <field>userid</field> 作为 <method>id()</method>
    //------> 的返回值, 但是并不能从结构上、在版本迭代的情况下做出保证.
    /*@Modifying
    @Transactional
    long removeByUserid(String userid);*/

    /*@Modifying
    @Transactional
    long removeDistinctByUserid(String userid);*/
    //-----//

}
