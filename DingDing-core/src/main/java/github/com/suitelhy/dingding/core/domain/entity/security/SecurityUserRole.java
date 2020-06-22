package github.com.suitelhy.dingding.core.domain.entity.security;

import github.com.suitelhy.dingding.core.infrastructure.domain.model.AbstractEntityModel;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityFactory;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityValidator;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.EntityUtil;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 用户 - 角色
 *
 * @Description 用户 - 角色 关联关系.
 */
@Entity
@Table(name = "SECURITY_USER_ROLE")
public class SecurityUserRole
        extends AbstractEntityModel</*Long*/Object[]> {

    /**
     * ID
     *
     * @Description 数据 ID.
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    /**
     * 用户名称
     *
     * @Description 业务唯一设计的一部分.
     */
    @Column(name = "username", nullable = false)
    private String username;

    /**
     * 角色编码
     *
     * @Description 业务唯一设计的一部分.
     */
    @Column(name = "role_code", nullable = false)
    private String roleCode;

    // 数据更新时间 (由数据库管理)
    @Column(name = "data_time")
    @Transient
    private LocalDateTime dataTime;

    //===== Entity Model =====//

    @Override
    public @NotNull /*Long*/Object[] id() {
        return new Object[] {
                this.username
                , this.roleCode
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
        return !Validator.USER_ROLE.id(this.id)
                || !this.isEntityLegal();
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
     * @Description <abstractClass>AbstractEntityModel</abstractClass> 提供的模板设计.
     *
     * @param entityId      {@link this#id()}
     * @return
     */
    @Override
    protected boolean validateId(@NotNull /*Long*/Object[] entityId) {
        return Validator.USER_ROLE.entity_id(entityId);
    }

    /**
     * 等效比较
     *
     * @param user      {@link SecurityUser}
     * @param role      {@link SecurityRole}
     * @return
     */
    public boolean equals(@NotNull SecurityUser user, @NotNull SecurityRole role) {
        if ((null == user || user.isEmpty())
                || (null == role || role.isEmpty())) {
            return false;
        }

        return !this.isEmpty()
                && Arrays.deepEquals(this.id(), new Object[]{user.getUsername(), role.getCode()});
    }

    //===== Entity Validator =====//

    /**
     * 角色 - 属性校验器
     *
     * @Description 各个属性的基础校验(注意: 此校验 ≠ 完全校验).
     */
    public enum Validator
            implements EntityValidator<SecurityUserRole, /*Long*/Object[]> {
        USER_ROLE;

        @Override
        public boolean validateId(@NotNull SecurityUserRole entity) {
            return null != entity.id()
                    && entity_id(entity.id());
        }

        @Override
        public boolean entity_id(@NotNull /*Long*/Object[] entityId) {
            return null != entityId
                    && entityId.length == 2
                    && this.username((String) entityId[0])
                    && this.roleCode((String) entityId[1]);
        }

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
    private SecurityUserRole(@Nullable Long id
            , @NotNull String username
            , @NotNull String roleCode)
            throws IllegalArgumentException {
        if (null == id) {
            //--- 添加功能
        } else {
            //--- 更新功能
            if (!Validator.USER_ROLE.id(id)) {
                //-- 非法输入: 数据ID
                throw new IllegalArgumentException(this.getClass().getSimpleName()
                        .concat(" -> 非法输入: 数据ID"));
            }
        }
        if (!Validator.USER_ROLE.username(username)) {
            //-- 非法输入: 用户名
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 用户名"));
        }
        if (!Validator.USER_ROLE.roleCode(roleCode)) {
            //-- 非法输入: 角色编码
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 角色编码"));
        }

        // 数据ID
        this.setId(id);
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
                //-- 非法输入: 数据 ID
                throw new IllegalArgumentException(this.getClass().getSimpleName()
                        .concat(" -> 非法输入: 数据 ID"));
            }

            return new SecurityUserRole(id, username, roleCode);
        }

    }

    //===== getter & setter =====//

    @Nullable
    public Long getId() {
        return id;
    }

    private boolean setId(@NotNull Long id) {
        if (Validator.USER_ROLE.id(id)) {
            this.id = id;
            return true;
        }
        return false;
    }

    @NotNull
    public String getUsername() {
        return username;
    }

    private boolean setUsername(@NotNull String username) {
        if (Validator.USER_ROLE.username(username)) {
            this.username = username;
            return true;
        }
        return false;
    }

    @NotNull
    public String getRoleCode() {
        return roleCode;
    }

    private boolean setRoleCode(@NotNull String roleCode) {
        if (Validator.USER_ROLE.roleCode(roleCode)) {
            this.roleCode = roleCode;
            return true;
        }
        return false;
    }

}
