package github.com.suitelhy.dingding.core.infrastructure.domain.model;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 实体设计模板
 *
 * @param <ID>      Entity 的唯一标识 (Identify) 的类型
 */
public interface EntityModel<ID> extends Serializable {

    /**
     * 唯一标识 <- Entity 对象
     *
     * @return The unique identify of the entity.
     */
    @NotNull
    ID id();

    /**
     * 判断是否相同
     *
     * @param obj
     * @return
     * @Description <code>equals(Object obj)</code> 应该根据 <code>equals(EntityModel entity)</code> 的实现来重写.
     */
    boolean equals(Object obj);

    /**
     * 判断是否相同 <- Entity 对象
     *
     * @param entity 实体对象
     * @return
     * @Description 默认按照 Entity 设计实现, 不应该被重写
     */
    default boolean equals(@Nullable EntityModel entity) {
        return EntityModel.equals(this, entity);
    }

    /**
     * 判断是否相同 <- Entity 对象
     *
     * @param entity
     * @param obj
     * @param <T>
     * @return
     * @Description Entity模板提供的实现
     */
    static <T extends EntityModel> boolean equals(@Nullable T entity, @Nullable Object obj) {
        return (null != entity && null != entity.id() && !entity.isEmpty()
                && obj instanceof EntityModel && null != ((EntityModel) obj).id() && !((EntityModel) obj).isEmpty())
                && entity.id().equals(((EntityModel) obj).id());
    }

    /**
     * 计算哈希值
     *
     * @return
     * @Description 如果重写了 <code>equals(Object obj)</code>, 则必须根据 <code>equals(Object obj)</code>
     * -> 的实现来重写 <code>hashCode()</code>.
     */
    int hashCode();

    /**
     * 计算哈希值 <- Entity 对象
     *
     * @param entity
     * @param <T>
     * @return 可为 null, 此时 <tt>entity</tt> 或 <tt>entity.id()</tt> 为 null
     * -> , 或者 <tt>entity.isEmpty()</tt> 为 true.
     * @Description Entity模板提供的实现
     */
    @Nullable
    static <T extends EntityModel> Integer hashCode(@Nullable T entity) {
        return (null != entity && null != entity.id() && !entity.isEmpty())
                ? entity.id().hashCode()
                : null;
    }

    /**
     * 是否无效
     *
     * @return
     * @Description 保证 Entity 的基本业务实现中的合法性.
     * @Design 基础实现: <tt>EntityModel.isEmpty(this) || !isLegal() || isPersistence() -> not false</tt>
     */
    boolean isEmpty();

    /**
     * 是否无效 <- Entity 对象
     *
     * @param entity
     * @param <T>
     * @return
     * @Description Entity模板提供的实现 -> 仅检验 <tt><method>id()</method> -> nonNull</tt>.
     */
    static <T extends EntityModel> boolean isEmpty(@Nullable T entity) {
        return null == entity
                || (entity.id() instanceof EntityModel
                ? (((EntityModel) entity.id()).isEmpty())
                : null == entity.id());
    }

    /**
     * 是否符合业务要求 <- Entity 对象
     *
     * @return
     * @Description 需要实现类实现该接口
     */
    boolean isEntityLegal();

    /**
     * 是否已持久化 <- Entity 对象
     *
     * @return 可为 null, 此时未实现该接口.
     */
    @Nullable
    Boolean isEntityPersistence();

    /**
     * 转换为字符串
     *
     * @return
     */
    @NotNull
    String toString();

    /**
     * 转换为字符串 <- Entity 对象
     *
     * @param entity
     * @param <T>
     * @return
     * @Description Entity模板提供的实现
     */
    @NotNull
    static <T extends EntityModel> String toString(@NotNull T entity) {
        return ((entity.id() instanceof EntityModel)
                ? (!((EntityModel) entity.id()).isEmpty())
                : null != entity.id())
                ? "{" + entity.id() + "}"
                : "{}";
    }

}
