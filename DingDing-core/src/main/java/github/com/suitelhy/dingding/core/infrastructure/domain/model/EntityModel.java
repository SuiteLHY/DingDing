package github.com.suitelhy.dingding.core.infrastructure.domain.model;

import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Arrays;

/**
 * 实体设计模板
 *
 * @param <ID> Entity 的唯一标识 (Identify) 的类型
 * @see Serializable
 */
public interface EntityModel<ID>
        extends Serializable {

    /**
     * 唯一标识 <- Entity 对象
     *
     * @return The unique identify of the entity.
     */
    /*@NotNull*/
    ID id();

//    /**
//     * 判断是否相同
//     *
//     * @Description <code>equals(Object obj)</code> 应该根据 <code>equals(EntityModel entity)</code> 的实现来重写.
//     *
//     * @Issue 违反约束声明规则, 弃用.
//     *-> {@code javax.validation.ConstraintDeclarationException: HV000151: A method overriding another method must not redefine the parameter constraint configuration, but method EntityModel#equals(Object) redefines the configuration of Object#equals(Object).}
//     *
//     * @param obj   {@link Object}
//     *
//     * @return 判断结果
//     */
//    boolean equals(@NotNull Object obj);

    /**
     * 判断是否相同 <- [{@link EntityModel} 实例]
     *
     * @param entity 实体对象, 必须合法且可未持久化    {@link EntityModel}
     * @return 判断结果
     * @Description 默认按照 {@link EntityModel} 设计实现, 不应该被重写.
     * @see EntityModel#equals(EntityModel, Object)
     */
    default boolean equals(EntityModel</*ID*/?> entity) {
        return EntityModel.equals(this, entity);
    }

    /**
     * 判断是否相同 <- Entity 对象
     *
     * @param entity 必须合法且允许未持久化. {@param <T>}
     * @param obj    必须合法且允许未持久化. {@link Object}
     * @param <T>    {@link EntityModel}
     * @return 判断结果
     * @Description {@link EntityModel} 提供的实现.
     */
    static <T extends EntityModel<?>> boolean equals(@Nullable T entity, @Nullable Object obj) {
        /*return (null != entity && null != entity.id() && !entity.isEmpty()
                && obj instanceof EntityModel && null != ((EntityModel) obj).id() && !((EntityModel) obj).isEmpty())
                && entity.id().equals(((EntityModel) obj).id());*/
        if ((null == entity || null == entity.id() || /*entity.isEmpty()*/!entity.isEntityLegal())
                || (!(obj instanceof EntityModel) || null == ((EntityModel<?>) obj).id() || /*((EntityModel) obj).isEmpty())*/!((EntityModel) obj).isEntityLegal())) {
            return false;
        }

        if (entity.id().getClass().isArray()
                && ((EntityModel<?>) obj).id().getClass().isArray()) {
            if (entity.id() instanceof Object[]
                    && ((EntityModel<?>) obj).id() instanceof Object[]) {
                return Arrays.deepEquals((Object[]) entity.id(), (Object[]) ((EntityModel<?>) obj).id());
            }
        }

        return entity.id().equals(((EntityModel<?>) obj).id());
    }

    /**
     * 计算哈希值
     *
     * @return 哈希值
     * @Description 如果重写了 {@link this#equals(Object)}, 则必须根据 {@link this#equals(Object)} 的实现来重写 {@link this#hashCode()}.
     */
    int hashCode();

    /**
     * 计算哈希值 <- [{@link EntityModel} 实例]
     *
     * @param entity {@param <T>}
     * @param <T>    {@link EntityModel}
     * @return 哈希值  {@link ObjectUtils#nullSafeHashCode}
     * @Description {@link EntityModel} 提供的实现.
     * · 【注意】避免无限递归调用 {@link this#hashCode()}.
     */
    static <T extends EntityModel<?>> /*Integer*/int hashCode(@Nullable T entity) {
        if (null == entity) {
            return ObjectUtils.nullSafeHashCode((Object) null);
        }
        if (null == entity.id() || entity.isEmpty()) {
            // 【注意】避免无限递归调用 <method>hashCode()</method>
            return /*ObjectUtils.nullSafeHashCode(entity)*/((Object) entity).hashCode();
        }

        // (new)
        if (entity.id().getClass().isArray()) {
            if (entity.id() instanceof Object[]) {
                return ObjectUtils.nullSafeHashCode((Object[]) entity.id());
            }
        }

        return entity.id().hashCode();
    }

    /**
     * 是否无效
     *
     * @return 判断结果
     * @Description 保证 [{@link EntityModel} 实例] 的基本业务实现中的合法性.
     * @Design 基础实现: {@code EntityModel.isEmpty(this) || !isLegal() || isPersistence()} -> <tt>not false</tt>
     */
    boolean isEmpty();

    /**
     * 是否无效 <- {@link EntityModel} 实例
     *
     * @param entity {@param <T>}
     * @param <T>    {@link EntityModel}
     * @return 判断结果
     * @Description {@link EntityModel} 提供的实现 -> 仅检验 {@link this#id()} -> 非 {@code null}.
     */
    static <T extends EntityModel<?>> boolean isEmpty(@Nullable T entity) {
        if (null == entity) {
            return true;
        }
        return entity.id() instanceof EntityModel
                ? (((EntityModel<?>) entity.id()).isEmpty())
                : null == entity.id();
    }

    /**
     * 是否符合业务要求 <- {@link EntityModel} 实例
     *
     * @return 判断结果
     * @Description 需要实现类实现该接口.
     */
    boolean isEntityLegal();

    /**
     * 是否已持久化 <- {@link EntityModel} 实例
     *
     * @return 判断结果.
     * · 可为 {@code null}, 此时未实现该接口.
     */
    @Nullable
    Boolean isEntityPersistence();

    /**
     * 转换为字符串
     *
     * @return 转换结果
     */
    @NotNull
    String toString();

    /**
     * 转换为字符串 <- Entity 对象
     *
     * @param entity {@param <T>}
     * @param <T>    {@link EntityModel}
     * @return 转换结果
     * @Description {@link EntityModel} 提供的实现
     */
    @NotNull
    static <T extends EntityModel<T_ID>, T_ID> String toString(@NotNull T entity) {
        /*return ((entity.id() instanceof EntityModel)
                ? (!((EntityModel) entity.id()).isEmpty())
                : null != entity.id())
                ? "{" + entity.id() + "}"
                : "{}";*/
        if (null == entity || null == entity.id()) {
            return "{}";
        }

        T_ID entityId = entity.id();
        if (entityId instanceof EntityModel) {
            if (((EntityModel<?>) entityId).isEmpty()) {
                return "{}";
            }
        }
        if (entityId.getClass().isArray()) {
            assert entityId instanceof Object[];
            return String.format("{%s}", Arrays.toString((Object[]) entityId));
        }
        return String.format("{%s}", entityId);
    }

}
