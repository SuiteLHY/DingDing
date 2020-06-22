package github.com.suitelhy.dingding.core.domain.entity.security;

import github.com.suitelhy.dingding.core.infrastructure.domain.model.AbstractEntityModel;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityFactory;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityValidator;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.EntityUtil;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.security.Security;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * 角色
 *
 * @Description 角色表.
 */
@Entity
@Table(name = "SECURITY_ROLE")
public class SecurityRole
        extends AbstractEntityModel<String> {

//    static {
//        //--- (使用静态块) 初始化相关数据
//        for (Security.RoleVo vo : Security.RoleVo.class.getEnumConstants()) {
//            //-- (编译器会自动调用枚举的构造器)
//        }
//    }

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
     * 角色描述
     *
     * @Description 可为空.
     */
    @Column
    @Nullable
    private String description;

    /**
     * 角色名称
     */
    @Column(nullable = false)
    private String name;

    //===== Entity Model =====//
    @Override
    public @NotNull String id() {
        return this.code;
    }

    /**
     * 是否无效
     *
     * @Description 保证 Entity 的基本业务实现中的合法性.
     * @return
     */
    @Override
    public boolean isEmpty() {
        return !Validator.ROLE.id(this.id)
                || !this.isEntityLegal();
    }

    /**
     * 是否符合基础数据合法性要求
     *
     * @Description 只保证 Entity 的数据合法, 不保证 Entity 的业务实现中的合法性.
     * @return
     */
    @Override
    public boolean isEntityLegal() {
        return Validator.ROLE.code(this.code)
                && Validator.ROLE.name(this.name)
                && Validator.ROLE.description(this.description);
    }

    /**
     * 校验 Entity - ID
     *
     * @Description <abstractClass>AbstractEntityModel</abstractClass>提供的模板设计.
     * @param id <method>id()</method>
     * @return
     */
    @Override
    protected boolean validateId(@NotNull String id) {
        return Validator.ROLE.entity_id(id);
    }

    //===== Entity Validator =====//

    /**
     * 角色 - 属性校验器
     *
     * @Description 各个属性的基础校验(注意: 此校验 ≠ 完全校验).
     */
    public enum Validator
            implements EntityValidator<SecurityRole, String> {
        ROLE;

        @Override
        public boolean validateId(@NotNull SecurityRole entity) {
            return null != entity.id() && entity_id(entity.id());
        }

        @Override
        public boolean entity_id(@NotNull String entityId) {
            return this.code(entityId);
        }

        public boolean id(@NotNull Long id) {
            return null != id
                    && EntityUtil.Regex.validateId(Long.toString(id));
        }

        public boolean code(@NotNull String code) {
            Map<String, Object> param_rule = new HashMap<>(1);
            param_rule.put("maxLength", 20);
            return EntityUtil.Regex.GeneralRule.englishPhrases_Number(code, param_rule);
        }

        public boolean description(@Nullable String description) {
            return null == description || description.length() < 201;
        }

        public boolean name(@NotNull String name) {
            Map<String, Object> param_rule = new HashMap<>(1);
            param_rule.put("maxLength", 20);
            return EntityUtil.Regex.GeneralRule.chinese_EnglishPhrases_Number(name, param_rule);
        }

    }

    //===== base constructor =====//

    /**
     * 仅用于持久化注入
     */
    public SecurityRole() {}

    //===== entity factory =====//

    /**
     * 创建/更新
     *
     * @param id            数据 ID
     * @param code          角色编码
     * @param name          角色名称
     * @param description   角色描述
     * @throws IllegalArgumentException
     */
    private SecurityRole(@NotNull Long id
            , @NotNull String code
            , @NotNull String name
            , @Nullable String description)
            throws IllegalArgumentException {
        if (null == id) {
            //--- 添加功能
        } else {
            //--- 更新功能
            if (!Validator.ROLE.id(id)) {
                //-- 非法输入: 数据ID
                throw new IllegalArgumentException(this.getClass().getSimpleName()
                        .concat(" -> 非法输入: 数据ID"));
            }
        }
        if (!Validator.ROLE.code(code)) {
            //-- 非法输入: 角色编码
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 角色编码"));
        }
        if (!Validator.ROLE.name(name)) {
            //-- 非法输入: 角色名称
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 角色名称"));
        }
        if (!Validator.ROLE.description(description)) {
            //-- 非法输入: 角色描述
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 角色描述"));
        }

        // 数据ID
        this.setId(id);
        // 角色编码
        this.setCode(code);
        // 角色名称
        this.setName(name);
        // 角色描述
        this.setDescription(description);
    }

    public enum Factory
            implements EntityFactory<SecurityRole> {
        ROLE;

        /**
         * 创建
         *
         * @param code          角色编码
         * @param name          角色名称
         * @param description   角色描述
         * @throws IllegalArgumentException
         */
        public SecurityRole create(@NotNull String code
                , @NotNull String name
                , @Nullable String description)
                throws IllegalArgumentException {
            return new SecurityRole(null, code, name, description);
        }

        /**
         * 创建
         *
         * @param roleVo {@link github.com.suitelhy.dingding.core.infrastructure.domain.vo.security.Security.RoleVo}
         * @throws IllegalArgumentException
         */
        public SecurityRole create(@NotNull Security.RoleVo roleVo)
                throws IllegalArgumentException {
            return new SecurityRole(null, roleVo.name(), roleVo.name
                    , roleVo.description);
        }

        /**
         * 更新
         *
         * @param id            数据 ID
         * @param code          角色编码
         * @param name          角色名称
         * @param description   角色描述
         * @throws IllegalArgumentException 此时 <param>id</param> 非法
         * @return 可为 null, 此时输入参数非法
         */
        public SecurityRole update(@NotNull Long id
                , @NotNull String code
                , @NotNull String name
                , @Nullable String description)
                throws IllegalArgumentException {
            if (!Validator.ROLE.id(id)) {
                throw new IllegalArgumentException("非法输入: 数据 ID");
            }
            return new SecurityRole(id, code, name, description);
        }

    }

    //===== getter & setter =====//

    @Nullable
    public Long getId() {
        return id;
    }

    private boolean setId(@NotNull Long id) {
        if (Validator.ROLE.id(id)) {
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
        if (Validator.ROLE.code(code)) {
            this.code = code;
            return true;
        }
        return false;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public boolean setDescription(@Nullable String description) {
        if (Validator.ROLE.description(description)) {
            this.description = description;
            return true;
        }
        return false;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public boolean setName(@NotNull String name) {
        if (Validator.ROLE.name(name)) {
            this.name = name;
            return true;
        }
        return false;
    }

}
