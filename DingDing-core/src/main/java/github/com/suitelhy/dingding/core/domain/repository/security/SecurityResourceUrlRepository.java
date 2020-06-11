package github.com.suitelhy.dingding.core.domain.repository.security;

import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResourceUrl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

/**
 * 资源 - URL
 *
 * @Description 资源 - URL 关联关系 -> 基础交互业务接口.
 */
public interface SecurityResourceUrlRepository
        extends JpaRepository<SecurityResourceUrl, Long> {

    //===== Select Data =====//

    /**
     * 查询总数
     *
     * @return
     */
    @Override
    long count();

    /**
     * 查询所有
     *
     * @param resourceCode  资源编码
     * @return
     */
    Page<SecurityResourceUrl> findAllByCode(@NotNull String resourceCode, Pageable pageable);

    /**
     * 查询所有
     *
     * @param urlPath       资源对应的 URL (Path部分)
     * @return
     */
    Page<SecurityResourceUrl> findAllByUrlPath(@NotNull String urlPath, Pageable pageable);

    //===== Insert Data =====//

    /**
     * 新增/更新日志记录
     *
     * @param entity
     * @return
     */
    @Override
    SecurityResourceUrl saveAndFlush(SecurityResourceUrl entity);

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
     * @param resourceCode  资源编码
     * @return
     */
    @Modifying
    @Transactional
    long removeByCode(@NotNull String resourceCode);

    /**
     * 删除
     *
     * @param urlPath       资源对应的 URL (Path部分)
     * @return
     */
    @Modifying
    @Transactional
    long removeByUrlPath(@NotNull String urlPath);

    /**
     * 删除
     *
     * @param resourceCode  资源编码
     * @param urlPath       资源对应的 URL (Path部分)
     * @return
     */
    @Modifying
    @Transactional
    long removeByCodeAndUrlPath(@NotNull String resourceCode, @NotNull String urlPath);

}
