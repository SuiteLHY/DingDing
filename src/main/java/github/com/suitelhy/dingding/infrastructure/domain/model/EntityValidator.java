package github.com.suitelhy.dingding.infrastructure.domain.model;

import github.com.suitelhy.dingding.infrastructure.domain.util.EntityUtil;

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



}
