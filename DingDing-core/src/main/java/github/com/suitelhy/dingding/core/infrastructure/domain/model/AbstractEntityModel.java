package github.com.suitelhy.dingding.core.infrastructure.domain.model;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

public abstract class AbstractEntityModel<ID>
        implements EntityModel<ID> {

    //===== EntityModel =====//

    /**
     * 唯一标识 <- Entity 对象
     *
     * @return The unique identify of the entity.
     */
    @NotNull
    @Override
    public abstract ID id();

    /**
     * 判断是否相同
     *
     * @param obj
     * @return
     * @Description <code>equals(Object obj)</code> 应该根据 <code>equals(EntityModel entity)</code> 的实现来重写.
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof EntityModel && equals((EntityModel) obj);
    }

    /**
     * 计算哈希值
     *
     * @return
     * @Description 如果重写了 <method>equals(Object obj)</method>, 则必须根据 <method>equals(Object obj)</method>
     * -> 的实现重写 <method>hashCode()</method>.
     */
    @Override
    public int hashCode() {
        return EntityModel.hashCode(this);
    }

    /**
     * 是否无效
     *
     * @return
     * @Description 保证 Entity 的基本业务实现中的合法性.
     * @Design 基础实现: <tt>id() 校验不通过 || 数据合法性 || 未持久化</tt>;
     * -> <tt>EntityModel.isEmpty(this) || !isLegal() || isPersistence() -> not false</tt>.
     */
    @Override
    public boolean isEmpty() {
        return (EntityModel.isEmpty(this) || !this.validateId(this.id()))
                || !this.isEntityLegal()
                || !Boolean.FALSE.equals(this.isEntityPersistence());
    }

    /**
     * 是否符合数据合法性要求 <- Entity 对象
     *
     * @return
     * @Description 只保证 Entity 的数据合法性, 不保证 Entity 的业务实现中的合法性.
     */
    @Override
    public abstract boolean isEntityLegal();

    /**
     * 是否已持久化 <- Entity 对象
     *
     * @return 可为 null, 此时未实现该接口.
     */
    @Override
    public @Nullable
    Boolean isEntityPersistence() {
        return null;
    }

    /**
     * 转换为字符串 <- Entity 对象
     *
     * @return
     */
    @Override
    public String toString() {
        return EntityModel.toString(this);
    }

    /**
     * 校验 Entity - ID
     *
     * @param id <method>id()</method>
     * @return
     * @Description <abstractClass>AbstractEntityModel</abstractClass>提供的模板设计.
     */
    protected abstract boolean validateId(@NotNull ID id);

}
