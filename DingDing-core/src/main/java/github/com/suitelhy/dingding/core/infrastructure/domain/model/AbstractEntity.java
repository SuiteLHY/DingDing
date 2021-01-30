package github.com.suitelhy.dingding.core.infrastructure.domain.model;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

/**
 * 实体设计模板的标准实现
 *
 * @param <ID> Entity 的唯一标识 (Identify) 的类型
 * @Design 实体设计模板 {@link EntityModel} 的标准实现.
 * @Solution · 持久化映射字段及其 getter 方法 (通常情况下) 不应该使用原始类型. <- {@link <a href="https://stackoverflow.com/questions/56497893/org-springframework-aop-aopinvocationexception-null-return-value-from-advice-do">
 * ->    java - org.springframework.aop.AopInvocationException: Null return value from advice does not match primitive return type for: public abstract char - Stack Overflow</a>}
 * @see EntityModel
 */
public abstract class AbstractEntity<ID>
        implements EntityModel<ID> {

    //===== EntityModel =====//

    /**
     * 唯一标识 <- Entity 对象
     *
     * @return The unique identify of the entity.
     * @see this#validateId(ID)
     */
    /*@NotNull*/
    @Override
    public abstract ID id();

    /**
     * 判断是否相同
     *
     * @param obj {@link Object}
     * @return 判断结果
     * @Description {@link this#equals(Object)} 应该根据 <code>equals(EntityModel entity)</code> 的实现来重写.
     */
    public boolean equals(Object obj) {
        return obj instanceof EntityModel
                && equals((EntityModel<?>) obj);
    }

    /**
     * 计算哈希值
     *
     * @return 哈希值
     * @Description 如果重写了 <method>equals(Object obj)</method>, 则必须根据 <method>equals(Object obj)</method>
     * 的实现重写 {@link this#hashCode()}.
     * · 注意: 避免无限递归调用 {@link this#hashCode()}.
     * @see EntityModel#hashCode(EntityModel)
     */
    @Override
    public int hashCode() {
        if (null == this.id() || this.isEmpty()) {
            //--- 【注意】避免无限递归调用 {@link this#hashCode()}
            return super.hashCode();
        }
        return EntityModel.hashCode(this);
    }

    /**
     * 是否无效
     *
     * @return 判断结果
     * @Description 保证 Entity 的基本业务实现中的合法性.
     * @Design 基础实现: {@link this#id()} <- <tt>校验不通过 || 数据合法性 || 未持久化</tt>;
     * -> <tt>EntityModel.isEmpty(this) || !isLegal() || isPersistence() -> not false</tt>.
     */
    @Override
    public boolean isEmpty() {
        return (EntityModel.isEmpty(this) || !this.validateId(this.id()))
                || !this.isEntityLegal()
                || Boolean.FALSE.equals(this.isEntityPersistence());
    }

    /**
     * 是否符合数据合法性要求 <- Entity 对象
     *
     * @return 判断结果
     * @Description 只保证 Entity 的数据合法性, 不保证 Entity 的业务实现中的合法性.
     */
    @Override
    public abstract boolean isEntityLegal();

    /**
     * 是否已持久化 <- Entity 对象
     *
     * @return {@link Boolean}  判断结果; 可为 {@code null}, 此时未实现该接口.
     */
    @Nullable
    @Override
    public Boolean isEntityPersistence() {
        return null;
    }

    /**
     * 转换为字符串 <- Entity 对象
     *
     * @return 转换结果
     * @see EntityModel#toString(EntityModel)
     */
    @Override
    public String toString() {
        return String.format("%s%s"
                , getClass().getSimpleName()
                , EntityModel.toString(this));
    }

    /**
     * 校验 Entity - ID
     *
     * @param id {@link this#id()}
     * @return 判断结果
     * @Design <abstractClass>AbstractEntityModel</abstractClass>提供的模板设计.
     * @see this#id()
     */
    protected abstract boolean validateId(@NotNull ID id);

}
