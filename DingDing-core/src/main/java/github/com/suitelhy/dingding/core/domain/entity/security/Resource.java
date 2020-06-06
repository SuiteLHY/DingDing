package github.com.suitelhy.dingding.core.domain.entity.security;

import github.com.suitelhy.dingding.core.infrastructure.domain.model.AbstractEntityModel;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityFactory;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityValidator;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.EntityUtil;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.VoUtil;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 资源类
 *
 * @Description
 */
@Entity
@Table(name = "SECURITY_RESOURCE")
public class Resource
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
    @Column(nullable = false, unique = true)
    private String code;

    /**
     * 图标
     */
    @Column
    private String icon;

    /**
     * 资源链接
     */
    @Column
    private String link;

    /**
     * 资源名称
     *
     * @Description 例如: xx菜单, xx按钮
     */
    @Column(nullable = false)
    private String name;

    /**
     * 父节点 <- 资源编码
     *
     * @Description 顶级节点可为空.
     */
    @Column
    private String parentCode;

    /**
     * 序号
     *
     * @Description 排序用.
     */
    @Column(nullable = false)
    private int sort;

    /**
     * 资源类型
     */
    @Column(nullable = false)
    private github.com.suitelhy.dingding.core.infrastructure.domain.vo.Resource.TypeVo type;

//    /**
//     * 资源对应的 URL (集合)
//     *
//     * @Description 多个 URL 之间以英文逗号分隔.
//     */
//    @Column(nullable = false)
//    private String urls;

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
     * @Description 只保证 User 的数据合法, 不保证 User 的业务实现中的合法性.
     * @return
     */
    @Override
    public boolean isEntityLegal() {
        return Validator.RESOURCE.code(this.code)
                && Validator.RESOURCE.icon(this.icon)
                && Validator.RESOURCE.link(this.link)
                && Validator.RESOURCE.name(this.name)
                && Validator.RESOURCE.parentCode(this)
                && Validator.RESOURCE.sort(this.sort)
                /*&& Validator.RESOURCE.urls(this.urls)*/;
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
        return Role.Validator.ROLE.id(id);
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
            implements EntityValidator<Resource, Long> {
        RESOURCE;

        @Override
        public boolean validateId(@NotNull Resource entity) {
            return null != entity.id()
                    && id(entity.id());
        }

        @Override
        public boolean id(@NotNull Long id) {
            return null != id
                    && EntityUtil.Regex.validateId(Long.toString(id));
        }

        public boolean code(@NotNull String code) {
            return EntityUtil.Regex.GeneralRule.englishPhrases(code, null, 20);
        }

        public boolean icon(String icon) {
            //--- 暂无
            return true;
        }

        public boolean link(String link) {
            //--- 暂无
            return true;
        }

        public boolean name(@NotNull String name) {
            return EntityUtil.Regex.GeneralRule.englishPhrases(name, null, 20);
        }

        public boolean parentCode(@Nullable String parentCode) {
            return null == parentCode || Validator.RESOURCE.code(parentCode);
        }

        public boolean parentCode(@NotNull Resource entity) {
            return null != entity
                    && null != entity.getCode()
                    && Validator.RESOURCE.code(entity.getCode())
                    && Validator.RESOURCE.parentCode(entity.getParentCode())
                    && !entity.getCode().equals(entity.getParentCode());
        }

        public boolean sort(int sort) {
            //--- 暂无
            return true;
        }

        public boolean type(github.com.suitelhy.dingding.core.infrastructure.domain.vo.Resource.TypeVo type) {
            if (null == type) {
                return null != VoUtil.getVoByValue(github.com.suitelhy.dingding.core.infrastructure.domain.vo.Resource.TypeVo.class, null);
            }
            return false;
        }

        public boolean urls(String urls) {
            //--- 暂无
            return true;
        }

    }

    //===== base constructor =====//

    /**
     * 仅用于持久化注入
     */
    public Resource() {}

    //===== entity factory =====//

    /**
     * (构造器)
     *
     * @param id            数据 ID
     * @param code          角色编码
     * @param icon          图标
     * @param link          资源链接
     * @param name          角色名称
     * @param parentCode    父节点 <- 资源编码
     * @param sort          序号
     * @param urls          资源对应的 URL (集合)
     * @throws IllegalArgumentException
     */
    private Resource(@NotNull Long id
            , @NotNull String code
            , @NotNull String icon
            , @NotNull String link
            , @NotNull String name
            , @Nullable String parentCode
            , @NotNull int sort
            , @Nullable String urls)
            throws IllegalArgumentException {
        if (null == id) {
            //--- 添加功能
        } else {
            //--- 更新功能
            if (!Validator.RESOURCE.id(id)) {
                //-- 非法输入: 数据ID
                throw new IllegalArgumentException(this.getClass().getSimpleName()
                        + " -> 非法输入: 数据ID");
            }
        }
        if (!Validator.RESOURCE.code(code)) {
            //-- 非法输入: 资源编码
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 资源编码");
        }
        if (!Validator.RESOURCE.icon(icon)) {
            //-- 非法输入: 图标
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 图标");
        }
        if (!Validator.RESOURCE.link(link)) {
            //-- 非法输入: 链接
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 链接");
        }
        if (!Validator.RESOURCE.name(name)) {
            //-- 非法输入: 资源名称
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 资源名称");
        }
        if (!Validator.RESOURCE.parentCode(parentCode)) {
            //-- 非法输入: 父节点资源编码
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 父节点资源编码");
        }
        if (!Validator.RESOURCE.sort(sort)) {
            //-- 非法输入: 序号
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 序号");
        }
        if (!Validator.RESOURCE.urls(urls)) {
            //-- 非法输入: 资源对应的URL
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 资源对应的URL");
        }

        // 数据ID
        this.setId(id);
        // 资源编码
        this.setCode(code);
        // 图标
        this.setIcon(icon);
        // 链接
        this.setLink(link);
        // 资源名称
        this.setName(name);
        // 资源编码
        this.setParentCode(parentCode);
        // 序号
        this.setSort(sort);
        /*// 资源对应的URL (集合)
        this.setUrls(urls);*/

        if (!Validator.RESOURCE.parentCode(this)) {
            //-- 非法输入: 父节点资源编码
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 父节点资源编码");
        }
    }

    public enum Factory
            implements EntityFactory<Resource> {
        RESOURCE;

        /**
         * 创建
         *
         * @param code          角色编码
         * @param icon          图标
         * @param link          资源链接
         * @param name          角色名称
         * @param parentCode    父节点 <- 资源编码
         * @param sort          序号
         * @param urls          资源对应的 URL (集合)
         * @throws IllegalArgumentException
         */
        public Resource create(@NotNull String code
                , String icon
                , String link
                , @NotNull String name
                , @Nullable String parentCode
                , @NotNull int sort
                , @Nullable String urls)
                throws IllegalArgumentException {
            return new Resource(null, code, icon
                    , link, name, parentCode
                    , sort, urls);
        }

        /**
         * 更新
         *
         * @param id            数据 ID
         * @param code          角色编码
         * @param icon          图标
         * @param link          资源链接
         * @param name          角色名称
         * @param parentCode    父节点 <- 资源编码
         * @param sort          序号
         * @param urls          资源对应的 URL (集合)
         * @throws IllegalArgumentException
         */
        public Resource update(@NotNull Long id
                , @NotNull String code
                , String icon
                , String link
                , @NotNull String name
                , @Nullable String parentCode
                , @NotNull int sort
                , @Nullable String urls)
                throws IllegalArgumentException {
            if (!Validator.RESOURCE.id(id)) {
                throw new IllegalArgumentException("非法输入: 数据 ID");
            }
            return new Resource(id, code, icon
                    , link, name, parentCode
                    , sort, urls);
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getParentCode() {
        return parentCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    @NotNull
    public github.com.suitelhy.dingding.core.infrastructure.domain.vo.Resource.TypeVo getType() {
        return type;
    }

    public boolean setType(github.com.suitelhy.dingding.core.infrastructure.domain.vo.Resource.TypeVo type) {
        if (Validator.RESOURCE.type(type)) {
            this.type = type;
            return true;
        }
        return false;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    /*public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }*/

}
