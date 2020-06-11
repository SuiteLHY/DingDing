package github.com.suitelhy.dingding.core.domain.entity.security;

import github.com.suitelhy.dingding.core.infrastructure.domain.model.AbstractEntityModel;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityFactory;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityValidator;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.EntityUtil;

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
            implements EntityValidator<SecurityRole, Long> {
        ROLE;

        @Override
        public boolean validateId(@NotNull SecurityRole entity) {
            return null != entity.id() && id(entity.id());
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
     * @throws IllegalArgumentException
     */
    private SecurityRole(@NotNull Long id
            , @NotNull String code
            , @NotNull String name)
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
        // 数据ID
        this.setId(id);
        // 角色编码
        this.setCode(code);
        // 角色名称
        this.setName(name);
    }

    public enum Factory
            implements EntityFactory<SecurityRole> {
        ROLE;

        /**
         * 创建
         *
         * @param code          角色编码
         * @param name          角色名称
         * @throws IllegalArgumentException
         */
        public SecurityRole create(@NotNull String code, @NotNull String name)
                throws IllegalArgumentException {
            return new SecurityRole(null, code, name);
        }

        /**
         * 更新
         *
         * @param id            数据 ID
         * @param code          角色编码
         * @param name          角色名称
         * @throws IllegalArgumentException 此时 <param>id</param> 非法
         * @return 可为 null, 此时输入参数非法
         */
        public SecurityRole update(@NotNull Long id
                , @NotNull String code
                , @NotNull String name)
                throws IllegalArgumentException {
            if (!Validator.ROLE.id(id)) {
                throw new IllegalArgumentException("非法输入: 数据 ID");
            }
            return new SecurityRole(id, code, name);
        }

    }

    //===== getter & setter =====//

    public Long getId() {
        return id;
    }

    public boolean setId(@NotNull Long id) {
        if (Validator.ROLE.id(id)) {
            this.id = id;
            return true;
        }
        return false;
    }

    public String getCode() {
        return code;
    }

    public boolean setCode(String code) {
        if (Validator.ROLE.code(code)) {
            this.code = code;
            return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public boolean setName(String name) {
        if (Validator.ROLE.name(name)) {
            this.name = name;
            return true;
        }
        return false;
    }

}
