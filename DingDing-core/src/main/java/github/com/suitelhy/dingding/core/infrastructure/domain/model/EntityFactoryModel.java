package github.com.suitelhy.dingding.core.infrastructure.domain.model;

import javax.validation.constraints.NotNull;

/**
 * Entity 工厂接口
 *
 * @param <T> - {@link EntityModel} 的实现类
 */
public interface EntityFactoryModel<T extends EntityModel<?>> {

    /**
     * 获取空对象
     *
     * @return 非 {@code null}.
     */
    @NotNull
    T createDefault();

}
