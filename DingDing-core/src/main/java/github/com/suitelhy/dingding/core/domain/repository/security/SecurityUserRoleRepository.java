package github.com.suitelhy.dingding.core.domain.repository.security;

import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

/**
 * 用户 - 角色
 *
 * @Description 用户 - 角色 关联关系 -> 基础交互业务接口.
 */
public interface SecurityUserRoleRepository
        extends JpaRepository<SecurityUserRole, Long> {

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
     * @Description Returns whether an entity with the given parameters exists.
     *
     * @param username
     * @param roleCode
     * @return
     */
    boolean existsByUsernameAndRoleCode(@NotNull String username, @NotNull String roleCode);

    /**
     * 查询所有
     *
     * @param username
     * @return
     */
    Page<SecurityUserRole> findAllByUsername(@NotNull String username, Pageable pageable);

    /**
     * 查询所有
     *
     * @param rodeCode
     * @return
     */
    Page<SecurityUserRole> findAllByRoleCode(@NotNull String rodeCode, Pageable pageable);

    //===== Insert Data =====//

    /**
     * 新增/更新日志记录
     *
     * @param entity
     * @return
     */
    @Override
    SecurityUserRole saveAndFlush(@NotNull SecurityUserRole entity);

    //===== Delete Data =====//

    /**
     * 删除
     *
     * @param id    数据ID
     */
    @Override
    @Transactional
    void deleteById(@NotNull Long id);

    /**
     * 删除
     *
     * @param username  用户名
     * @return
     */
    @Modifying
    @Transactional
    long removeByUsername(@NotNull String username);

    /**
     * 删除
     *
     * @param roleCode  角色编码
     * @return
     */
    @Modifying
    @Transactional
    long removeByRoleCode(@NotNull String roleCode);

    /**
     * 删除
     *
     * @param username  用户名
     * @param roleCode  角色编码
     * @return
     */
    @Modifying
    @Transactional
    long removeByUsernameAndRoleCode(@NotNull String username, @NotNull String roleCode);

}
