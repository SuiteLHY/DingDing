package github.com.suitelhy.dingding.core.domain.repository.security;

import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

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
        extends JpaRepository<SecurityResource, Long> {

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
    long countByParentCode(String parentCode);

    /**
     * 判断存在
     *
     * @param code
     * @param parentCode
     * @return
     */
    boolean existsByCodeAndParentCode(String code, String parentCode);

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
     * @param code
     * @return  {@link java.util.Optional}
     */
    Optional<SecurityResource> findByCode(String code);

    /**
     * 查询父节点资源编码对应的资源
     *
     * @param parentCode
     * @param pageable
     * @return {@link org.springframework.data.domain.Page}
     */
    Page<SecurityResource> findAllByParentCode(String parentCode, Pageable pageable);

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
                    + "where ru.code = :code ")
    List<Map<String, Object>> selectUrlByCode(@NotNull @Param("code") String code);

    //===== Insert Data =====//

    /**
     * 新增/更新日志记录
     *
     * @param resource
     * @return
     */
    @Override
    SecurityResource saveAndFlush(SecurityResource resource);

    //===== Delete Data =====//

    /**
     * 删除
     *
     * @param id 记录ID
     */
    @Override
    @Transactional
    void deleteById(@NotNull Long id);

    /**
     * 删除
     *
     * @param code
     */
    @Modifying
    @Transactional
    long removeByCode(@NotNull String code);

    /**
     * 删除
     *
     * @param code
     * @param parentCode
     * @return
     */
    @Modifying
    @Transactional
    long removeByCodeAndParentCode(@NotNull String code, @Nullable String parentCode);

}
