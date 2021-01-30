package github.com.suitelhy.dingding.core.infrastructure.application.dto.security;

import github.com.suitelhy.dingding.core.infrastructure.application.model.DtoModel;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityFactoryModel;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.security.Security;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

/**
 * (安全) 角色
 *
 * @Description (安全) 角色 -> DTO.
 * @see SecurityRole
 */
public class RoleDto
        implements DtoModel<SecurityRole, String> {

    private static final long serialVersionUID = 1L;

    // ID
    protected Long id;

    // 角色编码
    protected String code;

    // 角色描述
    protected String description;

    // 角色名称
    protected String name;

    //===== DtoModel =====//

    @NotNull
    private final transient SecurityRole dtoId;

    /**
     * (Constructor)
     *
     * @Description 用于构造默认空对象.
     */
    protected RoleDto() {
        this.dtoId = SecurityRole.Factory.ROLE.createDefault();
    }

    /**
     * (Constructor)
     *
     * @param dtoId {@link SecurityRole}
     * @throws IllegalArgumentException 此时 {@param dtoId} 非法
     */
    protected RoleDto(@NotNull SecurityRole dtoId)
            throws IllegalArgumentException {
        if (null == dtoId || !dtoId.isEntityLegal()) {
            //-- 非法输入: <param>dtoId</param>
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: <param>dtoId</param>"));
        }

        this.dtoId = dtoId;

        this.id = this.dtoId.getId();
        this.code = this.dtoId.getCode();
        this.description = this.dtoId.getDescription();
        this.name = this.dtoId.getName();
    }

    /**
     * 唯一标识 <- Entity 对象
     *
     * @return The unique identify of the <tt>dtoId()</tt>.
     */
    @Override
    public String id() {
        return this.dtoId.id();
    }

    /**
     * 是否符合业务要求 <- DTO 对象
     *
     * @return
     */
    @Override
    public boolean isDtoLegal() {
        return isEntityLegal();
    }

    /**
     * 是否符合业务要求 <- Entity 对象
     *
     * @return {@link Boolean#TYPE}
     * @Description 需要实现类实现该接口
     */
    @Override
    public boolean isEntityLegal() {
        return dtoId.isEntityLegal();
    }

    /**
     * 转换为 JSON 格式的字符串
     *
     * @return 转换结果
     */
    @Override
    public @NotNull
    String toJSONString() {
        /*return String.format("%s{code=\"%s\", name=\"%s\", description=\"%s\"}"
                , getClass().getSimpleName()
                , (null != this.code) ? this.code : ""
                , (null != this.name) ? this.name : ""
                , (null != this.description) ? this.description : "");*/
        return String.format("{\"class_name\":\"%s\", \"code\":\"%s\", \"name\":\"%s\", \"description\":\"%s\"}"
                , getClass().getSimpleName()
                , (null != this.code) ? this.code : ""
                , (null != this.name) ? this.name : ""
                , (null != this.description) ? this.description : "");
    }

    @Override
    public @NotNull
    String toString() {
        return this.toJSONString();
    }

    //===== Factory =====//

    public enum Factory
            implements EntityFactoryModel<RoleDto> {
        ROLE_DTO;

        /**
         * 创建 DTO
         *
         * @param role {@link SecurityRole}
         * @return {@link RoleDto}
         */
        @NotNull
        public RoleDto create(SecurityRole role) {
            return new RoleDto(role);
        }

        /**
         * 创建 DTO
         *
         * @param code        角色编码
         * @param name        角色名称
         * @param description 角色描述
         * @return {@link RoleDto}
         */
        @NotNull
        public RoleDto create(@NotNull String code
                , @NotNull String name
                , @Nullable String description) {
            final @NotNull SecurityRole role = SecurityRole.Factory.ROLE.create(code
                    , name
                    , description);
            return new RoleDto(role);
        }

        /**
         * 创建 DTO
         *
         * @param roleVo {@link Security.RoleVo}
         * @return {@link RoleDto}
         */
        @NotNull
        public RoleDto create(@NotNull Security.RoleVo roleVo) {
            final @NotNull SecurityRole role = SecurityRole.Factory.ROLE.create(roleVo);
            return new RoleDto(role);
        }

        /**
         * 更新 DTO
         *
         * @param id          数据 ID
         * @param code        角色编码
         * @param name        角色名称
         * @param description 角色描述
         * @return {@link RoleDto}
         * @throws IllegalArgumentException 此时 {@param id} 非法
         */
        @NotNull
        public RoleDto update(@NotNull Long id
                , @NotNull String code
                , @NotNull String name
                , @Nullable String description)
                throws IllegalArgumentException {
            final @NotNull SecurityRole role = SecurityRole.Factory.ROLE.update(id
                    , code
                    , name
                    , description);
            return new RoleDto(role);
        }

        /**
         * 获取空对象
         *
         * @return 非 {@code null}.
         */
        @NotNull
        @Override
        public RoleDto createDefault() {
            return new RoleDto();
        }

    }

    //===== Getter And Setter =====//

    public Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
