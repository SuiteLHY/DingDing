package github.com.suitelhy.dingding.core.infrastructure.domain.model;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 聚合设计模板
 *
 * @Description root() 是唯一标识 (之一).
 */
public interface AggregateModel<A extends AggregateModel<A, ROOT>, ROOT extends EntityModel<?>>
        extends Serializable {

    /**
     * 根节点
     *
     * @return
     * @Description <method>root()</method> 是唯一标识 (之一).
     */
    @NotNull
    ROOT root();

    /**
     * 等效比较
     *
     * @param obj
     * @return
     * @Description <method>equals(Object obj)</method>
     * ->    应该根据 <method>equals(A aggregate)</method> 的实现来重写.
     */
    boolean equals(Object obj);

    /**
     * 等效比较
     *
     * @param aggregate
     * @return
     */
    boolean equals(@NotNull A aggregate);

    /**
     * 根节点 <- 等效比较
     *
     * @param root
     * @return
     */
    default boolean equalsRoot(@NotNull ROOT root) {
        return null != root
                && root().equals(root);
    }

    /**
     * 根节点 <- 等效比较
     *
     * @param aggregate
     * @return
     */
    default boolean equalsRoot(@NotNull A aggregate) {
        return null != aggregate
                && equalsRoot(aggregate.root());
    }

    /**
     * 计算哈希值
     *
     * @return
     * @Description 如果重写了 <method>equals(Object obj)</method>, 则必须根据
     * ->    <method>equals(Object obj)</method> 的实现来重写 <method>hashCode()</method>.
     */
    int hashCode();

    /**
     * 判断有效性
     *
     * @return
     * @Description 缺省则根据 <method>root()</method> 来判断.
     */
    default boolean isEmpty() {
        return root().isEmpty();
    }

    /**
     * 转换为字符串
     *
     * @return
     */
    @NotNull
    String toString();

}
