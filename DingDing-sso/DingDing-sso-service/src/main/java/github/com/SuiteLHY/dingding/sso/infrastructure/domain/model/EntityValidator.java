package github.com.suitelhy.dingding.sso.infrastructure.domain.model;

import github.com.suitelhy.dingding.sso.infrastructure.domain.util.EntityUtil;

import javax.validation.constraints.NotNull;

/**
 * 实体验证器模板
 *
 * @Description 实体验证器主要用于校验实体属性.
 *-> 实体验证器的存在是为了集中和规范对实体属性的校验操作.
 *-> 可以根据实际业务设计和项目开发情况进行取舍.
 * @param <E>
 */
public interface EntityValidator<E extends EntityModel<ID>, ID> {

    /**
     * 校验 Entity - ID
     *
     * @param entity
     * @return
     */
    default boolean validateId(@NotNull E entity) {
        return null != entity
                && null != entity.id()
                && /*EntityUtil.Regex.validateId(String.valueOf(entity.id()))*/id(entity.id());
    }

    /**
     * 校验 Entity - ID
     * @param id
     * @return
     */
    boolean id(@NotNull ID id);

    /**
     * 校验 Entity - ID
     * @Description 实体验证器模板提供的默认实现.
     * @param id
     * @return
     */
    static boolean id(@NotNull String id) {
        return EntityUtil.Regex.validateId(id);
    }

    //===== 实体验证器模板 -> 拓展业务封装 =====//
    /**
     * 实体验证器模板 -> 关联使用实体验证器模板的业务封装
     *
     * @Description 相当于外键校验;
     *-> 在枚举中构建的类无法使用泛型, 因为枚举实例的初始化是在运行期之前就保证完成的.
     *
     * @Demo
     * <code>
     * //===== 外键设计 (仅限也仅应该为 Entity 的唯一标识) (模块化实现) =====//
     * private final ForeignEntityValidator FOREIGN_VALIDATOR;
     *
     * <E extends EntityModel<ID>, ID> Validator(@NotNull Class<E> foreignEntityClazz
     *         , @NotNull EntityValidator<E, ID> foreignValidator) {
     *     this.FOREIGN_VALIDATOR = new ForeignEntityValidator(foreignEntityClazz, foreignValidator);
     * }
     * //==========//
     * </code>
     *
     */
    class ForeignEntityValidator/*<_FOREIGN_ENTITY_VALIDATOR extends Enum & EntityValidator<E, ID>, E extends EntityModel<ID>, ID>*/ {

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

}
