package github.com.suitelhy.dingding.core.domain.repository.security;

import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRoleResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色 - 资源
 *
 * @Description 角色 - 资源 关联关系 -> 基础交互业务接口.
 */
public interface SecurityRoleResourceRepository
        extends JpaRepository<SecurityRoleResource, Long> {

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
     * @param roleCode      角色编码
     * @param resourceCode  资源编码
     * @return
     */
    boolean existsByRoleCodeAndResourceCode(String roleCode, String resourceCode);

    /**
     * 查询所有
     *
     * @param resourceCode  资源编码
     * @return
     */
    Page<SecurityRoleResource> findAllByResourceCode(String resourceCode, Pageable pageable);

    /**
     * 查询所有
     *
     * @param rodeCode      角色编码
     * @return
     */
    Page<SecurityRoleResource> findAllByRoleCode(String rodeCode, Pageable pageable);

    //===== Insert Data =====//

    /**
     * 新增/更新日志记录
     *
     * @param entity
     * @return
     */
    @Override
    SecurityRoleResource saveAndFlush(SecurityRoleResource entity);

    //===== Delete Data =====//

    /**
     * 删除
     *
     * @param id    数据ID
     */
    @Override
    @Transactional
    void deleteById(Long id);

    /**
     * 删除
     *
     * @param resourceCode  资源编码
     * @return
     */
    @Modifying
    @Transactional
    long removeByResourceCode(String resourceCode);

    /**
     * 删除
     *
     * @param roleCode  角色编码
     * @return
     */
    @Modifying
    @Transactional
    long removeByRoleCode(String roleCode);

    /**
     * 删除
     *
     * @param roleCode      角色编码
     * @param resourceCode  资源编码
     * @return
     */
    @Modifying
    @Transactional
    long removeByRoleCodeAndResourceCode(String roleCode, String resourceCode);

}
