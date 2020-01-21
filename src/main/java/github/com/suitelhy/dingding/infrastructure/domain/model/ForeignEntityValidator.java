package github.com.suitelhy.dingding.infrastructure.domain.model;

import javax.validation.constraints.NotNull;

/**
 * 实体验证器模板 -> 关联使用实体验证器模板的业务封装
 *
 * @Description 相当于外键校验;
 *-> 在枚举中构建的类无法使用泛型, 因为枚举实例的初始化是在运行期之前就保证完成的.
 */
public class ForeignEntityValidator/*<_FOREIGN_ENTITY_VALIDATOR extends Enum & EntityValidator<E, ID>, E extends EntityModel<ID>, ID>*/ {

    public final Class<?> FOREIGN_ENTITY_CLAZZ;

    public final EntityValidator FOREIGN_VALIDATOR;

    public <E extends EntityModel<ID>, ID> ForeignEntityValidator(@NotNull Class<E> foreignEntityClazz
            , @NotNull EntityValidator<E, ID> foreignValidator)
            throws IllegalArgumentException {
        if (null == foreignEntityClazz) {
            throw new IllegalArgumentException("非法参数: <param>entityClazz</param>");
        }
        if (null == foreignValidator) {
            throw new IllegalArgumentException("非法参数: <param>foreignValidator</param>");
        }
        this.FOREIGN_ENTITY_CLAZZ = foreignEntityClazz;
        this.FOREIGN_VALIDATOR = foreignValidator;
    }

    public <E extends EntityModel<ID>, ID> boolean foreignId(@NotNull Class<E> foreignEntityClazz
            , @NotNull ID id) {
        if (null != foreignEntityClazz
                && null != id
                && FOREIGN_ENTITY_CLAZZ == foreignEntityClazz) {
            return FOREIGN_VALIDATOR.id(id);
        }
        return false;
    }

}
