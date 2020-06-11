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
 * 用户 - 角色
 *
 * @Description 用户 - 角色 关联关系.
 */
@Entity
@Table(name = "SECURITY_USER_ROLE")
public class SecurityUserRole
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
     * 用户ID
     *
     * @Description 单个.
     */
    @Column(name = "username", nullable = false)
    private String username;

    /**
     * 角色编码
     *
     * @Description 单个.
     */
    @Column(name = "role_code", nullable = false)
    private String roleCode;

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
        return Validator.USER_ROLE.username(this.username)
                && Validator.USER_ROLE.roleCode(this.roleCode);
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
        return Validator.USER_ROLE.id(id);
    }

    @Override
    public String toString() {
        return Long.toString(id());
    }

    //===== Entity Validator =====//

    /**
     * 角色 - 属性校验器
     *
     * @Description 各个属性的基础校验(注意: 此校验 ≠ 完全校验).
     */
    public enum Validator
            implements EntityValidator<SecurityUserRole, Long> {
        USER_ROLE;

        @Override
        public boolean validateId(@NotNull SecurityUserRole entity) {
            return null != entity.id()
                    && id(entity.id());
        }

        @Override
        public boolean id(@NotNull Long id) {
            return null != id
                    && EntityUtil.Regex.validateId(Long.toString(id));
        }

        public boolean username(@NotNull String username) {
            return SecurityUser.Validator.USER.username(username);
        }

        public boolean roleCode(@NotNull String roleCode) {
            return SecurityRole.Validator.ROLE.code(roleCode);
        }

    }

    //===== base constructor =====//

    /**
     * 仅用于持久化注入
     */
    public SecurityUserRole() {}

    //===== entity factory =====//

    /**
     * (构造器)
     *
     * @param id            数据 ID
     * @param username      用户名
     * @param roleCode      角色编码
     * @throws IllegalArgumentException
     */
    private SecurityUserRole(@NotNull Long id
            , @NotNull String username
            , @Nullable String roleCode)
            throws IllegalArgumentException {
        if (null == id) {
            //--- 添加功能
        } else {
            //--- 更新功能
            if (!Validator.USER_ROLE.id(id)) {
                //-- 非法输入: 数据ID
                throw new IllegalArgumentException(this.getClass().getSimpleName()
                        + " -> 非法输入: 数据ID");
            }
        }
        if (!Validator.USER_ROLE.username(username)) {
            //-- 非法输入: 用户名
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 用户名");
        }
        if (!Validator.USER_ROLE.roleCode(roleCode)) {
            //-- 非法输入: 角色编码
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 角色编码");
        }

        // 数据ID
        this.id = id;
        // 用户名
        this.setUsername(username);
        // 角色编码
        this.setRoleCode(roleCode);
    }

    public enum Factory
            implements EntityFactory<SecurityUserRole> {
        USER_ROLE;

        /**
         * 创建
         *
         * @param username      用户名
         * @param roleCode      角色编码
         * @throws IllegalArgumentException
         */
        public SecurityUserRole create(@NotNull String username, @Nullable String roleCode)
                throws IllegalArgumentException {
            return new SecurityUserRole(null, username, roleCode);
        }

        /**
         * 更新
         *
         * @param id          数据 ID
         * @param username    用户名
         * @param roleCode    角色编码
         * @throws IllegalArgumentException
         */
        public SecurityUserRole update(@NotNull Long id
                , @NotNull String username
                , @Nullable String roleCode)
                throws IllegalArgumentException {
            if (!Validator.USER_ROLE.id(id)) {
                throw new IllegalArgumentException("非法输入: 数据 ID");
            }
            return new SecurityUserRole(id, username, roleCode);
        }

    }

    //===== getter & setter =====//

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public boolean setUsername(String username) {
        if (Validator.USER_ROLE.username(username)) {
            this.username = username;
            return true;
        }
        return false;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public boolean setRoleCode(String roleCode) {
        if (Validator.USER_ROLE.roleCode(roleCode)) {
            this.roleCode = roleCode;
            return true;
        }
        return false;
    }

}
