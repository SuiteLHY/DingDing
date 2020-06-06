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
 * 角色 - 资源
 *
 * @Description 角色 - 资源 关联关系.
 */
@Entity
@Table(name = "SECURITY_RESOURCE_URL")
public class RoleResource
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
     * @Description 单个.
     */
    @Column(name = "role_code", nullable = false)
    private String roleCode;

    /**
     * 资源编码
     *
     * @Description 单个.
     */
    @Column(name = "resource_code", nullable = false)
    private String resourceCode;

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
    protected boolean validateId(@NotNull Long id) {
        return Validator.ROLE_RESOURCE.id(id);
    }

    @Override
    public String toString() {
        return roleCode;
    }

    //===== Entity Validator =====//

    /**
     * 角色 - 属性校验器
     *
     * @Description 各个属性的基础校验(注意: 此校验 ≠ 完全校验).
     */
    public enum Validator
            implements EntityValidator<RoleResource, Long> {
        ROLE_RESOURCE;

        @Override
        public boolean validateId(@NotNull RoleResource entity) {
            return null != entity.id()
                    && id(entity.id());
        }

        @Override
        public boolean id(@NotNull Long id) {
            return null != id
                    && EntityUtil.Regex.validateId(Long.toString(id));
        }

        public boolean roleCode(@NotNull String roleCode) {
            return Role.Validator.ROLE.code(roleCode);
        }

        public boolean resourceCode(@NotNull String resourceCode) {
            return Resource.Validator.RESOURCE.code(resourceCode);
        }

    }

    //===== base constructor =====//

    /**
     * 仅用于持久化注入
     */
    public RoleResource() {}

    //===== entity factory =====//

    /**
     * (构造器)
     *
     * @param id            数据 ID
     * @param roleCode      角色编码
     * @param resourceCode  资源编码
     * @throws IllegalArgumentException
     */
    private RoleResource(@NotNull Long id
            , @NotNull String roleCode
            , @Nullable String resourceCode)
            throws IllegalArgumentException {
        if (null == id) {
            //--- 添加功能
        } else {
            //--- 更新功能
            if (!Validator.ROLE_RESOURCE.id(id)) {
                //-- 非法输入: 数据ID
                throw new IllegalArgumentException(this.getClass().getSimpleName()
                        + " -> 非法输入: 数据ID");
            }
        }
        if (!Validator.ROLE_RESOURCE.roleCode(roleCode)) {
            //-- 非法输入: 角色编码
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 角色编码");
        }
        if (!Validator.ROLE_RESOURCE.resourceCode(resourceCode)) {
            //-- 非法输入: 资源编码
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 资源编码");
        }

        // 数据ID
        this.id = id;
        // 角色编码
        this.setRoleCode(roleCode);
        // 资源编码
        this.setResourceCode(resourceCode);
    }

    public enum Factory
            implements EntityFactory<RoleResource> {
        ROLE_RESOURCE;

        /**
         * 创建
         *
         * @param roleCode          角色编码
         * @param resourceCode      资源编码
         * @throws IllegalArgumentException
         */
        public RoleResource create(@NotNull String roleCode, @Nullable String resourceCode)
                throws IllegalArgumentException {
            return new RoleResource(null, roleCode, resourceCode);
        }

        /**
         * 更新
         *
         * @param id            数据 ID
         * @param roleCode      角色编码
         * @param resourceCode  资源编码
         * @throws IllegalArgumentException
         */
        public RoleResource update(@NotNull Long id
                , @NotNull String roleCode
                , @Nullable String resourceCode)
                throws IllegalArgumentException {
            if (!Validator.ROLE_RESOURCE.id(id)) {
                throw new IllegalArgumentException("非法输入: 数据 ID");
            }
            return new RoleResource(id, roleCode, resourceCode);
        }

    }

    //===== getter & setter =====//

    public Long getId() {
        return id;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public boolean setRoleCode(String roleCode) {
        if (Validator.ROLE_RESOURCE.roleCode(roleCode)) {
            this.roleCode = roleCode;
            return true;
        }
        return false;
    }

    public String getResourceCode() {
        return resourceCode;
    }

    public boolean setResourceCode(String resourceCode) {
        if (Validator.ROLE_RESOURCE.resourceCode(resourceCode)) {
            this.resourceCode = resourceCode;
            return true;
        }
        return false;
    }

}
