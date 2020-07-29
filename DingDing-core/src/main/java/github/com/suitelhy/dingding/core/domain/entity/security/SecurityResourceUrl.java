package github.com.suitelhy.dingding.core.domain.entity.security;

import github.com.suitelhy.dingding.core.infrastructure.domain.model.AbstractEntityModel;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityFactory;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityValidator;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.EntityUtil;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 资源 - URL
 *
 * @Description 资源 - URL 关联关系.
 */
@Entity
@Table(name = "SECURITY_RESOURCE_URL")
public class SecurityResourceUrl
        extends AbstractEntityModel</*Long*/Object[]> {

    private static final long serialVersionUID = 1L;

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
     * 资源服务器 ID
     *
     * @Description 对应资源服务器的唯一标识 {clientId}.
     */
    @Column(name = "client_id", nullable = false)
    private String clientId;

    /**
     * 资源对应的 URL Method
     *
     * @Description 单个对象.
     */
    @Column(name = "url_method")
    private String urlMethod;

    /**
     * 资源对应的 URL (Path部分)
     *
     * @Description 单个 URL.
     */
    @Column(name = "url_path", nullable = false)
    private String urlPath;

    // 数据更新时间 (由数据库管理)
    @Column(name = "data_time")
    @Transient
    private LocalDateTime dataTime;

    //===== Entity Model =====//

    @Override
    public @NotNull /*Long*/Object[] id() {
        return new Object[] {
                this.code
                , this.clientId
                , this.urlPath
        };
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
                && Validator.RESOURCE_URL.clientId(this.clientId)
                && Validator.RESOURCE_URL.urlPath(this.urlPath);
    }

    /**
     * 校验 Entity - ID
     *
     * @Description <abstractClass>AbstractEntityModel</abstractClass>提供的模板设计.
     * @param id        {@link this#id()}
     * @return
     */
    @Override
    protected boolean validateId(@NotNull /*Long*/Object[] id) {
        return Validator.RESOURCE_URL.entity_id(id);
    }

    //===== Entity Validator =====//

    /**
     * 角色 - 属性校验器
     *
     * @Description 各个属性的基础校验(注意: 此校验 ≠ 完全校验).
     */
    public enum Validator
            implements EntityValidator<SecurityResourceUrl, /*Long*/Object[]> {
        RESOURCE_URL;

        @Override
        public boolean validateId(@NotNull SecurityResourceUrl entity) {
            return null != entity.id()
                    && entity_id(entity.id());
        }

        @Override
        public boolean entity_id(@NotNull /*Long*/Object[] entityId) {
            return null != entityId
                    && entityId.length == 3
                    && this.code((String) entityId[0])
                    && this.clientId((String) entityId[1])
                    && this.urlPath((String) entityId[2]);
        }

        public boolean id(@NotNull Long id) {
            return null != id
                    && EntityUtil.Regex.validateId(Long.toString(id));
        }

        public boolean code(@NotNull String code) {
            return SecurityResource.Validator.RESOURCE.code(code);
        }

        public boolean clientId(@NotNull String clientId) {
            Map<String, Object> param_rule = new HashMap<>(1);
            param_rule.put("maxLength", 50);
            return EntityUtil.Regex.GeneralRule.englishPhrases_Number(clientId, param_rule);
        }

        /**
         * URL 校验
         *
         * @param urlMethod     URL (Method部分)
         * @return 校验结果
         * @see github.com.suitelhy.dingding.core.infrastructure.domain.util.EntityUtil.Regex.GeneralRule#urlPath(String)
         */
        public boolean urlMethod(@Nullable String urlMethod) {
            return null == urlMethod
                    || EntityUtil.Regex.GeneralRule.englishPhrases(urlMethod, null, null);
        }

        /**
         * URL 校验
         *
         * @param urlPath       URL (Path部分)
         * @return 校验结果
         * @see github.com.suitelhy.dingding.core.infrastructure.domain.util.EntityUtil.Regex.GeneralRule#urlPath(String)
         */
        public boolean urlPath(@NotNull String urlPath) {
            return EntityUtil.Regex.GeneralRule.urlPath(urlPath);
        }

    }

    //===== base constructor =====//

    /**
     * 仅用于持久化注入
     */
    public SecurityResourceUrl() {}

    //===== entity factory =====//

    /**
     * (构造器)
     *
     * @param id            数据 ID
     * @param code          资源编码
     * @param clientId      资源服务器 ID
     * @param urlPath       资源对应的 URL (单个)
     * @throws IllegalArgumentException
     */
    private SecurityResourceUrl(@Nullable Long id
            , @NotNull String code
            , @NotNull String clientId
            , @NotNull String urlPath)
            throws IllegalArgumentException {
        if (null == id) {
            //--- 添加功能
        } else {
            //--- 更新功能
            if (!Validator.RESOURCE_URL.id(id)) {
                //-- 非法输入: 数据ID
                throw new IllegalArgumentException(this.getClass().getSimpleName()
                        .concat(" -> 非法输入: 数据ID"));
            }
        }
        if (!Validator.RESOURCE_URL.code(code)) {
            //-- 非法输入: 资源编码
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 资源编码"));
        }
        if (!Validator.RESOURCE_URL.clientId(clientId)) {
            //-- 非法输入: 资源服务器 ID
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 资源服务器 ID"));
        }
        if (!Validator.RESOURCE_URL.urlPath(urlPath)) {
            //-- 非法输入: 资源对应的URL
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 资源对应的URL"));
        }

        // 数据ID
        this.serId(id);
        // 资源编码
        this.setCode(code);
        // 资源服务器 ID
        this.setClientId(clientId);
        // 资源对应的 URL
        this.setUrlPath(urlPath);
    }

    public enum Factory
            implements EntityFactory<SecurityResourceUrl> {
        RESOURCE_URL;

        /**
         * 创建
         *
         * @param code          资源编码
         * @param clientId      资源服务器 ID
         * @param url           资源对应的 URL (单个)
         * @throws IllegalArgumentException
         */
        public SecurityResourceUrl create(@NotNull String code
                , @NotNull String clientId
                , @NotNull String url)
                throws IllegalArgumentException {
            return new SecurityResourceUrl(null, code, clientId, url);
        }

        /**
         * 更新
         *
         * @param id            数据 ID
         * @param code          资源编码
         * @param clientId      资源服务器 ID
         * @param url           资源对应的 URL (单个)
         * @throws IllegalArgumentException
         */
        public SecurityResourceUrl update(@NotNull Long id
                , @NotNull String code
                , @NotNull String clientId
                , @NotNull String url)
                throws IllegalArgumentException {
            if (!Validator.RESOURCE_URL.id(id)) {
                //-- 非法输入: 数据 ID
                throw new IllegalArgumentException(this.getClass().getSimpleName()
                        .concat(" -> 非法输入: 数据 ID"));
            }

            return new SecurityResourceUrl(id, code, clientId
                    , url);
        }

    }

    //===== getter & setter =====//

    /*@NotNull // 名称为"id"的属性, 在持久化之前会被根据getter方法进行校验; 在新增数据时, 作为ID的字段值由数据库管理填充, 所以此处不应该使用该注解*/
    @Nullable
    public Long getId() {
        return id;
    }

    private boolean serId(@NotNull Long id) {
        if (!Validator.RESOURCE_URL.id(id)) {
            this.id = id;
            return true;
        }
        return false;
    }

    @NotNull
    public String getCode() {
        return code;
    }

    private boolean setCode(@NotNull String code) {
        if (Validator.RESOURCE_URL.code(code)) {
            this.code = code;
            return true;
        }
        return false;
    }

    @NotNull
    public String getClientId() {
        return clientId;
    }

    public boolean setClientId(@NotNull String clientId) {
        if (Validator.RESOURCE_URL.clientId(clientId)) {
            this.clientId = clientId;
            return true;
        }
        return false;
    }

    public String getUrlMethod() {
        return urlMethod;
    }

    public boolean setUrlMethod(String urlMethod) {
        if (Validator.RESOURCE_URL.urlMethod(urlMethod)) {
            this.urlMethod = urlMethod;
            return true;
        }
        return false;
    }

    @NotNull
    public String getUrlPath() {
        return urlPath;
    }

    private boolean setUrlPath(@NotNull String urlPath) {
        if (Validator.RESOURCE_URL.urlPath(urlPath)) {
            this.urlPath = urlPath;
            return true;
        }
        return false;
    }

}
