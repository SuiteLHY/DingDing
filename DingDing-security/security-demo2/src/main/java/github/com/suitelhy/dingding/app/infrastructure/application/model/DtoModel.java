package github.com.suitelhy.dingding.app.infrastructure.application.model;

import github.com.suitelhy.dingding.app.infrastructure.domain.model.EntityModel;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

public interface DtoModel<_ENTITY extends EntityModel<ID>, ID>
        extends EntityModel<ID> {

    /**
     * 唯一标识 <- DTO 对象
     *
     * @return The unique identify of the DTO.
     */
    @NotNull
    _ENTITY dtoId();

    /**
     * 唯一标识 <- Entity 对象
     *
     * @return The unique identify of the <tt>dtoId()</tt>.
     */
    @Override
    default ID id() {
        return dtoId().id();
    }

    /**
     * 判断是否相同
     * @Description <code>equals(Object obj)</code> 应该根据 <code>equals(EntityModel entity)</code> 的实现来重写.
     * @param obj
     * @return
     */
    @Override
    boolean equals(Object obj);

    /**
     * 判断是否相同 <- Entity 对象
     *
     * @Description 默认按照 Entity 设计实现, 不应该被重写
     * @param entity 实体对象
     * @return
     */
    @Override
    default boolean equals(@Nullable EntityModel entity) {
        return EntityModel.equals(this, entity);
    }

    /**
     * 计算哈希值
     * @Description 如果重写了 <code>equals(Object obj)</code>, 则必须根据 <code>equals(Object obj)</code>
     *-> 的实现来重写 <code>hashCode()</code>.
     * @return
     */
    int hashCode();

    /**
     * 是否符合业务要求 <- DTO 对象
     * @return
     */
    boolean isDtoLegal();

    /**
     * 是否无效 <- DTO 对象
     *
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
     * @Description <interface>DtoModel</interface> 提供的默认实现.
     *-> <tt>EntityModel.isEmpty(this) || !isLegal() || !isDtoLegal()</tt>
     * @param dto
     * @return
     */
    static boolean isEmpty(@NotNull DtoModel<?, ?> dto) {
        return EntityModel.isEmpty(dto)
                || !dto.isEntityLegal()
                || !dto.isDtoLegal();
    }

    /**
     * 是否已持久化 <- Entity 对象
     *
     * @return <code>null</code>, DTO 对象不实现该接口.
     */
    @Nullable
    @Override
    default Boolean isEntityPersistence() {
        return null;
    }

}
