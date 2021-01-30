package github.com.suitelhy.dingding.core.domain.entity.security;

import github.com.suitelhy.dingding.core.infrastructure.domain.util.EntityUtil;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.AbstractEntity;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityFactoryModel;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityValidator;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.security.Security;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * （安全认证）用户 ←→ 角色
 *
 * @Description [（安全认证）用户 ←→ 角色]关联关系.
 */
@Entity
@Table(name = "SECURITY_USER_ROLE")
public class SecurityUserRole
        extends AbstractEntity</*Long*/Object[]> {

    /**
     * ID
     *
     * @Description 数据 ID.
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(length = 64)
    private Long id;

    /**
     * 用户名称
     *
     * @Description 业务唯一设计的一部分.
     */
    @Column(name = "username", nullable = false, length = 30)
    private String username;

    /**
     * 角色编码
     *
     * @Description 业务唯一设计的一部分.
     */
    @Column(name = "role_code", nullable = false, length = 20)
    private String roleCode;

    // 数据更新时间 (由数据库管理)
    @Column(name = "data_time")
    @Transient
    private LocalDateTime dataTime;

    //===== Entity Model =====//

    @Override
    public @NotNull /*Long*/Object[] id() {
        return new Object[]{
                this.username
                , this.roleCode
        };
    }

    /**
     * 是否无效
     *
     * @return
     * @Description 保证 User 的基本业务实现中的合法性.
     */
    @Override
    public boolean isEmpty() {
        return !Validator.USER_ROLE.id(this.id)
                || !this.isEntityLegal();
    }

    /**
     * 是否符合基础数据合法性要求
     *
     * @return
     * @Description 只保证数据合法, 不保证业务实现中的合法性.
     */
    @Override
    public boolean isEntityLegal() {
        return Validator.USER_ROLE.username(this.username)
                && Validator.USER_ROLE.roleCode(this.roleCode);
    }

    /**
     * 校验 Entity - ID
     *
     * @param entityId {@link this#id()}
     * @return
     * @Description <abstractClass>AbstractEntityModel</abstractClass> 提供的模板设计.
     */
    @Override
    protected boolean validateId(@NotNull /*Long*/Object[] entityId) {
        return Validator.USER_ROLE.entity_id(entityId);
    }

    /**
     * 等效比较
     *
     * @param user {@link SecurityUser}
     * @param role {@link SecurityRole}
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
     * @Description 各个属性的基础校验(注意 : 此校验 ≠ 完全校验).
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
    public SecurityUserRole() {
    }

    //===== entity factory =====//

    /**
     * (构造器)
     *
     * @param id       数据 ID
     * @param username 用户名
     * @param roleCode 角色编码
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
                //-- 非法输入: [数据 ID]
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "[数据 ID]"
                        , id
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
        }
        if (!Validator.USER_ROLE.username(username)) {
            //-- 非法输入: 用户名
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "用户名"
                    , username
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (!Validator.USER_ROLE.roleCode(roleCode)) {
            //-- 非法输入: 角色编码
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "角色编码"
                    , roleCode
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        // 数据ID
        this.setId(id);
        // 用户名
        this.setUsername(username);
        // 角色编码
        this.setRoleCode(roleCode);
    }

    public enum Factory
            implements EntityFactoryModel<SecurityUserRole> {
        USER_ROLE;

        /**
         * 创建
         *
         * @param username 用户名
         * @param roleCode 角色编码
         * @throws IllegalArgumentException
         */
        public @NotNull
        SecurityUserRole create(@NotNull String username, @NotNull String roleCode)
                throws IllegalArgumentException {
            return new SecurityUserRole(null, username, roleCode);
        }

        /**
         * 创建
         *
         * @param user （安全认证）用户
         * @param role （安全认证）角色
         * @throws IllegalArgumentException
         */
        public @NotNull
        SecurityUserRole create(@NotNull SecurityUser user, @NotNull SecurityRole role)
                throws IllegalArgumentException {
            if (null == user || user.isEmpty()) {
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                        , "[（安全认证）用户]"
                        , user
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            if (null == role || role.isEmpty()) {
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                        , "[（安全认证）角色]"
                        , role
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            return new SecurityUserRole(null, user.getUsername(), role.getCode());
        }

        /**
         * 创建
         *
         * @param user   [（安全认证）用户]
         * @param roleVo [（安全认证）角色]
         * @throws IllegalArgumentException
         */
        public @NotNull
        SecurityUserRole create(@NotNull SecurityUser user, @NotNull Security.RoleVo roleVo)
                throws IllegalArgumentException {
            if (null == user || user.isEmpty()) {
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                        , "[（安全认证）用户]"
                        , user
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            if (null == roleVo) {
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                        , "[（安全认证）角色]"
                        , roleVo
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            return new SecurityUserRole(null, user.getUsername(), roleVo.name());
        }

        /**
         * 更新
         *
         * @param id       数据 ID
         * @param username 用户名
         * @param roleCode 角色编码
         * @throws IllegalArgumentException
         */
        public @NotNull
        SecurityUserRole update(@NotNull Long id
                , @NotNull String username
                , @Nullable String roleCode)
                throws IllegalArgumentException {
            if (!Validator.USER_ROLE.id(id)) {
                //-- 非法输入: [数据 ID]
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "[数据 ID]"
                        , id
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            return new SecurityUserRole(id, username, roleCode);
        }

        /**
         * 获取空对象
         *
         * @return 非 {@code null}.
         */
        @Override
        public @NotNull
        SecurityUserRole createDefault() {
            return new SecurityUserRole();
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
