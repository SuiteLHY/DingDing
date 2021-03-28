package github.com.suitelhy.dingding.core.infrastructure.application.model;

import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityModel;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

public interface DtoModel<_ENTITY extends EntityModel<ID>, ID>
        extends EntityModel<ID> {

    /**
     * 转换为字符串
     *
     * @param dto {@link T}
     * @param <T> {@link DtoModel}
     *
     * @return 转换结果
     *
     * @throws IllegalArgumentException 此时 {@param dto} 为 {@code null}
     */
    static @NotNull
    <T extends DtoModel> String toString(@NotNull T dto)
            throws IllegalArgumentException {
        if (null == dto) {
            throw new IllegalArgumentException("非法参数: <param>dto</param> <- <code>null</code>");
        }

        return dto.toJSONString();
    }

    /**
     * 唯一标识 <- DTO 对象
     *
     * @Description 默认设计为不实现该接口, 返回值为 {@code null}.
     *
     * @return The unique identify of the DTO.
     */
    default @Nullable _ENTITY dtoId() {
        return null;
    }

    /**
     * 唯一标识 <- Entity 对象
     *
     * @return The unique identify of the <tt>dtoId()</tt>.
     */
    @Override
    default @NotNull ID id() {
        return null == dtoId() ? null : dtoId().id();
    }

    /**
     * 判断是否相同
     *
     * @Description {@link this#equals(Object)} 应该根据 {@link this#equals(EntityModel)} 的实现来重写.
     *
     * @param obj
     *
     * @return
     */
    @Override
    boolean equals(Object obj);

    /**
     * 判断是否相同 <- Entity 对象
     *
     * @Description 默认按照 Entity 设计实现, 不应该被重写
     *
     * @param entity 实体对象
     *
     * @return
     */
    @Override
    default boolean equals(@Nullable EntityModel entity) {
        return EntityModel.equals(this, entity);
    }

    /**
     * 计算哈希值
     *
     * @Description 如果重写了 {@link this#equals(Object)}, 则必须根据 {@link this#equals(Object)}
     * 的实现来重写 {@link this#hashCode()}.
     *
     * @return
     */
    int hashCode();

    /**
     * 是否符合业务要求 <- DTO 对象
     *
     * @return
     */
    boolean isDtoLegal();

    /**
     * 是否无效 <- DTO 对象
     *
     * @return
     */
    @Override
    default boolean isEmpty() {
        return DtoModel.isEmpty(this);
    }

    /**
     * 是否无效 <- DTO 对象
     *
     * @Description {@link DtoModel} 提供的默认实现.
     * {@code EntityModel.isEmpty(this) || ! isLegal() || ! isDtoLegal()}
     *
     * @param dto
     *
     * @return
     */
    static boolean isEmpty(@NotNull DtoModel<?, ?> dto) {
        return EntityModel.isEmpty(dto)
                || ! dto.isEntityLegal()
                || ! dto.isDtoLegal();
    }

    /**
     * 是否已持久化 <- Entity 对象
     *
     * @return {@literal null}, DTO 对象不实现该接口.
     */
    @Override
    default @Nullable Boolean isEntityPersistence() {
        return null;
    }

    /**
     * 转换为 JSON 格式的字符串
     *
     * @return 转换结果
     */
    default @NotNull String toJSONString() {
        return String.format("{\"_class_name\":\"%s\"}", getClass().getSimpleName());
    }

    /**
     * 转换为字符串
     *
     * @return 转换结果
     */
    @Override
    @NotNull String toString();

}
