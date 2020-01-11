package github.com.suitelhy.webchat.infrastructure.domain.model;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 实体设计模板
 * @param <ID> - Entity 的唯一标识 (Identify) 的类型
 */
public interface EntityModel<ID> extends Serializable {

    /**
     * Entity 对象的唯一标识
     * @return The unique identify of the entity.
     */
    ID id();

    /**
     * 判断是否相同
     * @Description <code>equals(Object obj)</code> 应该根据 <code>equals(EntityModel entity)</code> 的实现来重写.
     * @param obj
     * @return
     */
    boolean equals(Object obj);

    /**
     * 判断是否相同
     * @Description 默认按照 Entity 设计实现, 不应该被重写
     * @param entity 实体对象
     * @return
     */
    default boolean equals(@Nullable EntityModel entity) {
        return EntityModel.equals(this, entity);
    }

    /**
     * 判断是否相同
     *
     * @Description Entity模板提供的实现
     * @param entity
     * @param obj
     * @param <T>
     * @return
     */
    static <T extends EntityModel> boolean equals(@Nullable T entity, @Nullable Object obj) {
        return (null != entity && null != entity.id() && !entity.isEmpty()
                && obj instanceof EntityModel && null != ((EntityModel) obj).id() && !((EntityModel) obj).isEmpty())
                ? entity.id().equals(((EntityModel) obj).id())
                : false;
    }

    /**
     * 计算哈希值
     * @Description 如果重写了 <code>equals(Object obj)</code>, 则必须根据 <code>equals(Object obj)</code>
     *-> 的实现重写 <code>hashCode()</code>.
     * @return
     */
    int hashCode();

    /**
     * 计算哈希值
     *
     * @Description Entity模板提供的实现
     * @param entity
     * @param <T>
     * @return 可为 null, 此时 <tt>entity</tt> 或 <tt>entity.id()</tt> 为 null
     *-> , 或者 <tt>entity.isEmpty()</tt> 为 true.
     */
    static <T extends EntityModel> Integer hashCode(@Nullable T entity) {
        return (null != entity && null != entity.id() && !entity.isEmpty())
                ? entity.id().hashCode()
                : null;
    }

    /**
     * 是否无效
     * @Description <tt>EntityModel.isEmpty(this) || !isLegal() || isPersistence() -> not false</tt>
     * @return
     */
    boolean isEmpty();

    /**
     * 是否无效
     *
     * @Description Entity模板提供的实现 -> 仅检验 <tt><method>id()</method> -> nonNull</tt>.
     * @param entity
     * @param <T>
     * @return
     */
    static <T extends EntityModel> boolean isEmpty(@Nullable T entity) {
        return null == entity
                || (entity.id() instanceof EntityModel
                        ? (((EntityModel) entity.id()).isEmpty())
                        : null == entity.id());
    }

    /**
     * 是否符合业务要求
     * @Description 需要实现类实现该接口
     * @return
     */
    boolean isLegal();

    /**
     * 是否已持久化
     * @return 可为 null, 此时未实现该接口.
     */
    @Nullable
    Boolean isPersistence();

    /**
     * 转换为字符串
     * @return
     */
    String toString();

    /**
     * 转换为字符串
     *
     * @Description Entity模板提供的实现
     * @param entity
     * @param <T>
     * @return
     */
    static <T extends EntityModel> String toString(@NotNull T entity) {
        return ((entity.id() instanceof EntityModel)
                    ? (!((EntityModel) entity.id()).isEmpty())
                    : null != entity.id())
                ? "{" + entity.id() + "}"
                : "{}";
    }

}
