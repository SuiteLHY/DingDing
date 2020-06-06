package github.com.suitelhy.dingding.core.domain.entity.security;

import github.com.suitelhy.dingding.core.infrastructure.domain.model.AbstractEntityModel;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityFactory;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityValidator;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.EntityUtil;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 角色
 *
 * @Description 角色表.
 */
@Entity
@Table(name = "SECURITY_ROLE")
public class Role
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
     * 角色编码
     *
     * @Description 业务唯一.
     */
    @Column(nullable = false, unique = true)
    private String code;

    /**
     * 角色名称
     */
    @Column(nullable = false)
    private String name;

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
        return Validator.ROLE.code(this.code)
                && Validator.ROLE.name(this.name);
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
        return Validator.ROLE.id(id);
    }

    //===== Entity Validator =====//

    /**
     * 角色 - 属性校验器
     *
     * @Description 各个属性的基础校验(注意: 此校验 ≠ 完全校验).
     */
    public enum Validator
            implements EntityValidator<Role, Long> {
        ROLE;

        @Override
        public boolean validateId(@NotNull Role entity) {
            return null != entity.id() && id(entity.id());
        }

        @Override
        public boolean id(@NotNull Long id) {
            return null != id
                    && EntityUtil.Regex.validateId(Long.toString(id));
        }

        public boolean code(@NotNull String code) {
            return EntityUtil.Regex.GeneralRule.englishPhrases(code, null, 20);
        }

        public boolean name(@NotNull String name) {
            return EntityUtil.Regex.GeneralRule.englishPhrases(name, null, 20);
        }

    }

    //===== base constructor =====//

    /**
     * 仅用于持久化注入
     */
    public Role() {}

    //===== entity factory =====//

    /**
     * 创建/更新
     *
     * @param id            数据 ID
     * @param code          角色编码
     * @param name          角色名称
     * @param resourceCodes   资源 Code (集合)
     * @throws IllegalArgumentException
     */
    private Role(@NotNull Long id
            , @NotNull String code
            , @NotNull String name
            , @Nullable String resourceCodes)
            throws IllegalArgumentException {
        if (null == id) {
            //--- 添加功能
        } else {
            //--- 更新功能
            if (!Validator.ROLE.id(id)) {
                //-- 非法输入: 数据ID
                throw new IllegalArgumentException(this.getClass().getSimpleName()
                        + " -> 非法输入: 数据ID");
            }
        }
        if (!Validator.ROLE.code(code)) {
            //-- 非法输入: 角色编码
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 角色编码");
        }
        if (!Validator.ROLE.name(name)) {
            //-- 非法输入: 角色名称
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 角色名称");
        }
        // 数据ID
        this.setId(id);
        // 角色编码
        this.setCode(code);
        // 角色名称
        this.setName(name);
    }

    public enum Factory
            implements EntityFactory<Role> {
        ROLE;

        /**
         * 创建
         *
         * @param code          角色编码
         * @param name          角色名称
         * @param resourceCodes   资源 Code (集合)
         * @throws IllegalArgumentException
         */
        public Role create(@NotNull String code
                , @NotNull String name
                , @Nullable String resourceCodes)
                throws IllegalArgumentException {
            return new Role(null, code, name, resourceCodes);
        }

        /**
         * 更新
         *
         * @param id            数据 ID
         * @param code          角色编码
         * @param name          角色名称
         * @param resourceCodes   资源 Code (集合)
         * @throws IllegalArgumentException 此时 <param>id</param> 非法
         * @return 可为 null, 此时输入参数非法
         */
        public Role update(@NotNull Long id
                , @NotNull String code
                , @NotNull String name
                , @Nullable String resourceCodes)
                throws IllegalArgumentException {
            if (!Validator.ROLE.id(id)) {
                throw new IllegalArgumentException("非法输入: 数据 ID");
            }
            return new Role(id, code, name, resourceCodes);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
