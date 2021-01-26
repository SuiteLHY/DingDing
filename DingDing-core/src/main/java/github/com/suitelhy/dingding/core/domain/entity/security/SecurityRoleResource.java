package github.com.suitelhy.dingding.core.domain.entity.security;

import github.com.suitelhy.dingding.core.infrastructure.domain.util.EntityUtil;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.AbstractEntity;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityFactoryModel;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityValidator;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * （安全认证）角色 ←→ 资源
 *
 * @Description [（安全认证）角色 ←→ 资源]关联关系.
 */
@Entity
@Table(name = "SECURITY_ROLE_RESOURCE")
public class SecurityRoleResource
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
     * 角色编码
     *
     * @Description 单个.
     */
    @Column(name = "role_code", nullable = false, length = 20)
    private String roleCode;

    /**
     * 资源编码
     *
     * @Description 单个.
     */
    @Column(name = "resource_code", nullable = false, length = 20)
    private String resourceCode;

    // 数据更新时间 (由数据库管理)
    @Column(name = "data_time")
    @Transient
    private LocalDateTime dataTime;

    //===== Entity Model =====//

    @Override
    public @NotNull /*Long*/Object[] id() {
        return /*this.getId()*/new Object[] {
                this.getRoleCode(), this.getResourceCode()
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
        return !Validator.ROLE_RESOURCE.id(this.id)
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
        return Validator.ROLE_RESOURCE.roleCode(this.roleCode)
                && Validator.ROLE_RESOURCE.resourceCode(this.resourceCode);
    }

    /**
     * 校验 Entity - ID
     *
     * @Description <abstractClass>AbstractEntityModel</abstractClass>提供的模板设计.
     * @param id <method>id()</method>
     * @return
     */
    @Override
    protected boolean validateId(@NotNull /*Long*/Object[] id) {
        return Validator.ROLE_RESOURCE.entity_id(id);
    }

    public boolean equals(@NotNull SecurityRole role, @NotNull SecurityResource resource) {
        if ((null == role || role.isEmpty())
                || (null == resource || resource.isEmpty())) {
            return false;
        }

        return !this.isEmpty()
                && Arrays.deepEquals(this.id(), new Object[]{role.getCode(), resource.getCode()});
    }

    //===== Entity Validator =====//

    /**
     * 角色 - 属性校验器
     *
     * @Description 各个属性的基础校验(注意: 此校验 ≠ 完全校验).
     */
    public enum Validator
            implements EntityValidator<SecurityRoleResource, /*Long*/Object[]> {
        ROLE_RESOURCE;

        @Override
        public boolean validateId(@NotNull SecurityRoleResource entity) {
            return null != entity.id()
                    && entity_id(entity.id());
        }

        @Override
        public boolean entity_id(@NotNull Object[] entityId) {
            if (null == entityId || entityId.length != 2) {
                return false;
            }

            return Validator.ROLE_RESOURCE.roleCode((String) entityId[0])
                    && Validator.ROLE_RESOURCE.resourceCode((String) entityId[1]);
        }

        public boolean id(@NotNull Long id) {
            return null != id
                    && EntityUtil.Regex.validateId(Long.toString(id));
        }

        public boolean roleCode(@NotNull String roleCode) {
            return SecurityRole.Validator.ROLE.code(roleCode)
                    && roleCode.length() < 21;
        }

        public boolean resourceCode(@NotNull String resourceCode) {
            return SecurityResource.Validator.RESOURCE.code(resourceCode)
                    && resourceCode.length() < 21;
        }

    }

    //===== base constructor =====//

    /**
     * (构造器)
     *
     * @Description 仅用于持久化注入.
     */
    public SecurityRoleResource() {}

    //===== entity factory =====//

    /**
     * (构造器)
     *
     * @param id            数据 ID
     * @param roleCode      角色编码
     * @param resourceCode  资源编码
     *
     * @throws IllegalArgumentException
     */
    private SecurityRoleResource(@NotNull Long id
            , @NotNull String roleCode
            , @NotNull String resourceCode)
            throws IllegalArgumentException
    {
        if (null == id) {
            //--- 添加功能
        } else {
            //--- 更新功能
            if (! Validator.ROLE_RESOURCE.id(id)) {
                //-- 非法输入: [数据 ID]
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "[数据 ID]"
                        , id
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
        }
        if (! Validator.ROLE_RESOURCE.roleCode(roleCode)) {
            //-- 非法输入: 角色编码
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "角色编码"
                    , roleCode
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (! Validator.ROLE_RESOURCE.resourceCode(resourceCode)) {
            //-- 非法输入: 资源编码
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "资源编码"
                    , resourceCode
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        // 数据ID
        this.setId(id);
        // 角色编码
        this.setRoleCode(roleCode);
        // 资源编码
        this.setResourceCode(resourceCode);
    }

    public enum Factory
            implements EntityFactoryModel<SecurityRoleResource> {
        ROLE_RESOURCE;

        /**
         * 创建
         *
         * @param roleCode          角色编码
         * @param resourceCode      资源编码
         *
         * @throws IllegalArgumentException
         */
        public @NotNull SecurityRoleResource create(@NotNull String roleCode, @NotNull String resourceCode)
                throws IllegalArgumentException
        {
            return new SecurityRoleResource(null, roleCode, resourceCode);
        }

        /**
         * 创建
         *
         * @param role      （安全认证）角色
         * @param resource  （安全认证）资源
         *
         * @throws IllegalArgumentException
         */
        public @NotNull SecurityRoleResource create(@NotNull SecurityRole role, @NotNull SecurityResource resource)
                throws IllegalArgumentException
        {
            if (null == role || role.isEmpty()) {
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                        , "[（安全认证）角色]"
                        , role
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            if (null == resource || resource.isEmpty()) {
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                        , "[（安全认证）资源]"
                        , resource
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            return new SecurityRoleResource(null, role.getCode(), resource.getCode());
        }

        /**
         * 更新
         *
         * @param id            数据 ID
         * @param roleCode      角色编码
         * @param resourceCode  资源编码
         *
         * @throws IllegalArgumentException
         */
        public @NotNull SecurityRoleResource update(@NotNull Long id
                , @NotNull String roleCode
                , @Nullable String resourceCode)
                throws IllegalArgumentException
        {
            if (!Validator.ROLE_RESOURCE.id(id)) {
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "[数据 ID]"
                        , id
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            return new SecurityRoleResource(id, roleCode, resourceCode);
        }

        /**
         * 获取空对象
         *
         * @return 非 {@code null}.
         */
        @Override
        public @NotNull SecurityRoleResource createDefault() {
            return new SecurityRoleResource();
        }

    }

    //===== getter & setter =====//

    @Nullable
    public Long getId() {
        return id;
    }

    private boolean setId(@NotNull Long id) {
        if (Validator.ROLE_RESOURCE.id(id)) {
            this.id = id;
            return true;
        }
        return false;
    }

    @NotNull
    public String getRoleCode() {
        return roleCode;
    }

    public boolean setRoleCode(@NotNull String roleCode) {
        if (Validator.ROLE_RESOURCE.roleCode(roleCode)) {
            this.roleCode = roleCode;
            return true;
        }
        return false;
    }

    @NotNull
    public String getResourceCode() {
        return resourceCode;
    }

    public boolean setResourceCode(@NotNull String resourceCode) {
        if (Validator.ROLE_RESOURCE.resourceCode(resourceCode)) {
            this.resourceCode = resourceCode;
            return true;
        }
        return false;
    }

}
