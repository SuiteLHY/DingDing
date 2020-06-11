package github.com.suitelhy.dingding.core.infrastructure.domain.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;

public abstract class AbstractAggregateModel<A extends AbstractAggregateModel<A, ROOT>, ROOT extends EntityModel<?>>
        implements AggregateModel<A, ROOT> {

    @Autowired
    protected transient ObjectMapper objectMapper;

    @NotNull
    protected final transient Class<A> clazz;

    @NotNull
    protected final ROOT root;

    /**
     * (构造器)
     *
     * @param clazz
     * @param root
     */
    protected AbstractAggregateModel(@NotNull Class<A> clazz, @NotNull ROOT root) {
        if (null == clazz) {
            //-- 非法输入: 具体类型
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 具体类型"));
        }
        if (null == root || root.isEmpty()) {
            //-- 非法输入: 根节点
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 根节点"));
        }

        this.clazz = clazz;
        this.root = root;
    }

    /**
     * 根节点
     *
     * @Description <method>root()</method> 是唯一标识 (之一).
     *
     * @return
     */
    @NotNull
    @Override
    public ROOT root() {
        return this.root;
    }

    /**
     * 等效比较
     *
     * @Description <method>equals(Object obj)</method>
     *->    应该根据 <method>equals(A aggregate)</method> 的实现来重写.
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(@NotNull Object obj) {
        if (obj instanceof AbstractAggregateModel
                && obj.getClass().isAssignableFrom(this.clazz)) {
            return equals((A) obj);
        }
        return super.equals(obj);
    }

    /**
     * 等效比较
     *
     * @param aggregate
     * @return
     */
    @Override
    public abstract boolean equals(@NotNull A aggregate);

    /**
     * 计算哈希值
     *
     * @Description 如果重写了 <method>equals(Object obj)</method>, 则必须根据
     *->    <method>equals(Object obj)</method> 的实现来重写 <method>hashCode()</method>.
     *
     * @return
     */
    @Override
    public abstract int hashCode();

    /**
     * 转换为字符串
     *
     * @Description 建议使用 <Object>objectMapper</Object>
     *
     * @return
     */
    @NotNull
    @Override
    public String toString() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "{" + this.root() + "}";
    }

}
