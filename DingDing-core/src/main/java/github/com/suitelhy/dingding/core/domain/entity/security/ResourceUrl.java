package github.com.suitelhy.dingding.core.domain.entity.security;

import github.com.suitelhy.dingding.core.infrastructure.domain.model.AbstractEntityModel;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityFactory;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityValidator;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.EntityUtil;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 资源 - URL
 *
 * @Description 资源 - URL 关联关系.
 */
@Entity
@Table(name = "SECURITY_RESOURCE_URL")
public class ResourceUrl
        extends AbstractEntityModel<Long> {

    /**
     * ID
     *
     * @Description 数据 ID.
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    /**
     * 资源编码
     *
     * @Description 业务唯一.
     */
    @Column(nullable = false)
    private String code;

    /**
     * 资源对应的 URL (单个)
     *
     * @Description 单个 URL.
     */
    @Column(nullable = false)
    private String url;

    // 数据更新时间 (由数据库管理)
    @Column(name = "data_time")
    @Transient
    private LocalDateTime dataTime;

    //===== Entity Model =====//

    @Override
    public @NotNull Long id() {
        return this.getId();
    }

    /**
     * 是否无效
     *
     * @Description 保证 User 的基本业务实现中的合法性.
     * @return
     */
    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    /**
     * 是否符合基础数据合法性要求
     *
     * @Description 只保证数据合法, 不保证业务实现中的合法性.
     * @return
     */
    @Override
    public boolean isEntityLegal() {
        return Validator.RESOURCE_URL.code(this.code)
                && Validator.RESOURCE_URL.url(this.url);
    }

    /**
     * 校验 Entity - ID
     *
     * @Description <abstractClass>AbstractEntityModel</abstractClass>提供的模板设计.
     * @param id <method>id()</method>
     * @return
     */
    @Override
    protected boolean validateId(@NotNull Long id) {
        return Validator.RESOURCE_URL.id(id);
    }

    @Override
    public String toString() {
        return code;
    }

    //===== Entity Validator =====//

    /**
     * 角色 - 属性校验器
     *
     * @Description 各个属性的基础校验(注意: 此校验 ≠ 完全校验).
     */
    public enum Validator
            implements EntityValidator<ResourceUrl, Long> {
        RESOURCE_URL;

        @Override
        public boolean validateId(@NotNull ResourceUrl entity) {
            return null != entity.id()
                    && id(entity.id());
        }

        @Override
        public boolean id(@NotNull Long id) {
            return null != id
                    && EntityUtil.Regex.validateId(Long.toString(id));
        }

        public boolean code(@NotNull String code) {
            return Resource.Validator.RESOURCE.code(code);
        }

        public boolean url(@NotNull String url) {
            return EntityUtil.Regex.GeneralRule.url(url);
        }

    }

    //===== base constructor =====//

    /**
     * 仅用于持久化注入
     */
    public ResourceUrl() {}

    //===== entity factory =====//

    /**
     * (构造器)
     *
     * @param id            数据 ID
     * @param code          资源编码
     * @param url           资源对应的 URL (单个)
     * @throws IllegalArgumentException
     */
    private ResourceUrl(@NotNull Long id
            , @NotNull String code
            , @Nullable String url)
            throws IllegalArgumentException {
        if (null == id) {
            //--- 添加功能
        } else {
            //--- 更新功能
            if (!Validator.RESOURCE_URL.id(id)) {
                //-- 非法输入: 数据ID
                throw new IllegalArgumentException(this.getClass().getSimpleName()
                        + " -> 非法输入: 数据ID");
            }
        }
        if (!Validator.RESOURCE_URL.code(code)) {
            //-- 非法输入: 资源编码
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 资源编码");
        }
        if (!Validator.RESOURCE_URL.url(url)) {
            //-- 非法输入: 资源对应的URL
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 资源对应的URL");
        }

        // 数据ID
        this.setId(id);
        // 资源编码
        this.setCode(code);
        // 资源对应的URL (单个)
        this.setUrl(url);
    }

    public enum Factory
            implements EntityFactory<ResourceUrl> {
        RESOURCE_URL;

        /**
         * 创建
         *
         * @param code          资源编码
         * @param urls          资源对应的 URL (单个)
         * @throws IllegalArgumentException
         */
        public ResourceUrl create(@NotNull String code, @Nullable String urls)
                throws IllegalArgumentException {
            return new ResourceUrl(null, code, urls);
        }

        /**
         * 更新
         *
         * @param id            数据 ID
         * @param code          资源编码
         * @param urls          资源对应的 URL (单个)
         * @throws IllegalArgumentException
         */
        public ResourceUrl update(@NotNull Long id
                , @NotNull String code
                , @Nullable String urls)
                throws IllegalArgumentException {
            if (!Validator.RESOURCE_URL.id(id)) {
                throw new IllegalArgumentException("非法输入: 数据 ID");
            }
            return new ResourceUrl(id, code, urls);
        }

    }

    //===== getter & setter =====//

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
