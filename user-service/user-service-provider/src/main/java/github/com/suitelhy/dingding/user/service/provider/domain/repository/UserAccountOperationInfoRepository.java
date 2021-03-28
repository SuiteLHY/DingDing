package github.com.suitelhy.dingding.user.service.provider.domain.repository;

import github.com.suitelhy.dingding.user.service.api.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityRepository;
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

/**
 * 用户 -> 账户操作记录 - 持久化接口
 *
 * @Description 用户信息 - 实体 (Entity) 持久化接口.
 *
 * @see UserAccountOperationInfo
 */
public interface UserAccountOperationInfoRepository
        extends JpaRepository<UserAccountOperationInfo, String>, EntityRepository {

    //===== Select Data =====//

    /**
     * 查询记录总数
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
     * 查询所有记录列表
     *
     * @param pageable
     * @return
     */
    @Override
    Page<UserAccountOperationInfo> findAll(Pageable pageable);

    /**
     * 查询记录
     *
     * @param id
     * @return
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    Optional<UserAccountOperationInfo> findById(String id);

    /**
     * 查询记录
     *
     * @param username
     * @return
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    Optional<UserAccountOperationInfo> findByUsername(String username);

    //===== Insert Data =====//

    /**
     * 新增(/修改)用户操作记录
     *
     * @param user
     * @return
     */
    @Override
    @Modifying
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    UserAccountOperationInfo saveAndFlush(UserAccountOperationInfo user);

    //===== Update Data =====//

    /**
     * 修改指定记录
     *
     * @param username      用户名称
     * @param ip            最后登陆IP
     * @param lastLoginTime 最后登录时间
     * @return 修改的记录数量 (操作成功时预期为 1)
     */
    @Modifying
    @Query("update UserAccountOperationInfo u set u.ip = ?2, u.lastLoginTime = ?3 "
            + "where u.username = ?1 ")
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    int modifyByUsername(String username, String ip, String lastLoginTime);

    //===== Delete Data =====//

    /**
     * 删除给定的实体
     *
     * @param user
     * @throws IllegalArgumentException                               - in case the given entity is null.
     * @throws org.springframework.dao.EmptyResultDataAccessException - in case the given entity is non persistent.
     * @Description Spring Data JPA 提供的 <method>delete(T entity)</method> 无法保证操作 <b>执行且成功</b> !
     * -> , 应该换用 <method>deleteById(String id)</method>.
     * @Reference -> <a href="https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html#delete-T-">
     * ->     CrudRepository (Spring Data Core 2.2.3.RELEASE API) # delete-T-</a>
     * @Update 不建议使用!
     */
    @Override
    @Modifying
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    void delete(UserAccountOperationInfo user);

    /**
     * 删除给定的 id 所指定的实体
     *
     * @param id
     * @throws IllegalArgumentException                               - in case the given id is null.
     * @throws org.springframework.dao.EmptyResultDataAccessException - in case the given id -> entity is non persistent.
     * @Description 可以保证删除操作 <b>执行且成功</b>.
     * @Reference <a href="https://stackoverflow.com/questions/31346356/spring-data-jpa-deleteby-query-not-working?noredirect=1&lq=1">
     * ->     Spring Data JPA DeleteBy查询不起作用 - Stack Overflow</a>
     * -> , <a href="https://stackoverflow.com/questions/23723025/spring-data-delete-by-is-supported">
     * ->      Spring Data: "delete by" is supported? - Stack Overflow</a>
     */
    @Override
    @Modifying
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    void deleteById(String id);

    /**
     * 删除给定的 username 所指定的实体
     *
     * @param username
     * @throws IllegalArgumentException                               - in case the given id is null.
     * @throws org.springframework.dao.EmptyResultDataAccessException - in case the given id -> entity is non persistent.
     * @Description 可以保证删除操作 <b>执行且成功</b>.
     * @Reference <a href="https://stackoverflow.com/questions/31346356/spring-data-jpa-deleteby-query-not-working?noredirect=1&lq=1">
     * ->     Spring Data JPA DeleteBy查询不起作用 - Stack Overflow</a>
     * -> , <a href="https://stackoverflow.com/questions/23723025/spring-data-delete-by-is-supported">
     * ->      Spring Data: "delete by" is supported? - Stack Overflow</a>
     */
    @Modifying
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    void deleteByUsername(String username);

}
