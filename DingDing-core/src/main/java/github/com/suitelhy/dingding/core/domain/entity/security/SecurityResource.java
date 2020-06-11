package github.com.suitelhy.dingding.core.domain.entity.security;

import github.com.suitelhy.dingding.core.infrastructure.domain.model.AbstractEntityModel;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityFactory;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityValidator;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.EntityUtil;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.VoUtil;
import io.swagger.models.auth.In;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * 资源类
 *
 * @Description
 */
@Entity
@Table(name = "SECURITY_RESOURCE")
public class SecurityResource
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
    @Column(name = "parent_code")
    private String parentCode;

    /**
     * 序号
     *
     * @Description 排序用.
     * @Solution
     *-> · 持久化映射字段及其 getter 方法 (通常情况下) 不应该使用原始类型. <- {@link <a href="https://stackoverflow.com/questions/56497893/org-springframework-aop-aopinvocationexception-null-return-value-from-advice-do">
     *->     java - org.springframework.aop.AopInvocationException: Null return value from advice does not match primitive return type for: public abstract char - Stack Overflow</a>}
     */
    @Column(nullable = false)
    private /*int*/Integer sort;

    /**
     * 资源类型
     *
     * @Description 非空.
     *
     * @see github.com.suitelhy.dingding.core.infrastructure.domain.vo.Resource.TypeVo
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
                && Validator.RESOURCE.type(this.type);
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
        return SecurityRole.Validator.ROLE.id(id);
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
            implements EntityValidator<SecurityResource, Long> {
        RESOURCE;

        @Override
        public boolean validateId(@NotNull SecurityResource entity) {
            return null != entity.id()
                    && id(entity.id());
        }

        @Override
        public boolean id(@NotNull Long id) {
            return null != id
                    && EntityUtil.Regex.validateId(Long.toString(id));
        }

        public boolean code(@NotNull String code) {
            Map<String, Object> param_rule = new HashMap<>(1);
            param_rule.put("maxLength", 20);
            return EntityUtil.Regex.GeneralRule.englishPhrases_Number(code, param_rule);
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

        public boolean parentCode(@NotNull SecurityResource entity) {
            return null != entity
                    && null != entity.getCode()
                    && Validator.RESOURCE.code(entity.getCode())
                    && Validator.RESOURCE.parentCode(entity.getParentCode())
                    && !entity.getCode().equals(entity.getParentCode());
        }

        public boolean sort(/*int*/@NotNull Integer sort) {
            return null != sort;
        }

        public boolean type(github.com.suitelhy.dingding.core.infrastructure.domain.vo.Resource.TypeVo type) {
            if (null == type) {
                return null != VoUtil.getVoByValue(github.com.suitelhy.dingding.core.infrastructure.domain.vo.Resource.TypeVo.class, null);
            }
            return true;
        }

        //===== 相对特殊的业务校验方法 =====//

        /**
         * 判断子节点
         *
         * @param parent
         * @param child
         * @return
         */
        public boolean isChildNode(@NotNull SecurityResource parent, @NotNull SecurityResource child) {
            if (null != parent && !parent.isEmpty()
                    && null != child && child.isEntityLegal()) {
                if (null != child.getParentCode()
                        && parent.getCode().equals(child.getParentCode())) {
                    if (null != parent.getParentCode()
                            && parent.getParentCode().equals(child.getCode())) {
                        return false;
                    }
                    return true;
                }
            }
            return false;
        }

    }

    //===== base constructor =====//

    /**
     * 仅用于持久化注入
     */
    public SecurityResource() {}

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
     * @param type          资源类型
     * @throws IllegalArgumentException
     */
    private SecurityResource(@NotNull Long id
            , @NotNull String code
            , @NotNull String icon
            , @NotNull String link
            , @NotNull String name
            , @Nullable String parentCode
            , @NotNull int sort
            , @NotNull github.com.suitelhy.dingding.core.infrastructure.domain.vo.Resource.TypeVo type)
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
        if (!Validator.RESOURCE.type(type)) {
            //-- 非法输入: 资源类型
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 资源类型");
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
        // 资源类型
        this.setType(type);

        if (!Validator.RESOURCE.parentCode(this)) {
            //-- 非法输入: 父节点资源编码
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 父节点资源编码");
        }
    }

    public enum Factory
            implements EntityFactory<SecurityResource> {
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
         * @param type          资源类型
         * @throws IllegalArgumentException
         */
        public SecurityResource create(@NotNull String code
                , String icon
                , String link
                , @NotNull String name
                , @Nullable String parentCode
                , @NotNull int sort
                , @NotNull github.com.suitelhy.dingding.core.infrastructure.domain.vo.Resource.TypeVo type)
                throws IllegalArgumentException {
            return new SecurityResource(null, code, icon
                    , link, name, parentCode
                    , sort, type);
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
         * @param type          资源类型
         * @throws IllegalArgumentException
         */
        public SecurityResource update(@NotNull Long id
                , @NotNull String code
                , String icon
                , String link
                , @NotNull String name
                , @Nullable String parentCode
                , @NotNull int sort
                , @NotNull github.com.suitelhy.dingding.core.infrastructure.domain.vo.Resource.TypeVo type)
                throws IllegalArgumentException {
            if (!Validator.RESOURCE.id(id)) {
                throw new IllegalArgumentException("非法输入: 数据 ID");
            }
            return new SecurityResource(id, code, icon
                    , link, name, parentCode
                    , sort, type);
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

    public boolean setParentCode(String parentCode) {
        if (Validator.RESOURCE.parentCode(parentCode)) {
            String temp = this.parentCode;

            this.parentCode = parentCode;

            if (Validator.RESOURCE.parentCode(this)) {
                return true;
            }
            this.parentCode = temp;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @NotNull
    public /*int*/Integer getSort() {
        return sort;
    }

    public boolean setSort(int sort) {
        if (Validator.RESOURCE.sort(sort)) {
            this.sort = sort;
            return true;
        }
        return false;
    }

    /*public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }*/

}
