package github.com.suitelhy.dingding.app.infrastructure.domain.model;

import org.springframework.lang.Nullable;

/**
 * Entity 工厂接口
 * @param <T> - Entity 类对象
 */
public interface EntityFactory<T extends EntityModel<?>> {

    /**
     * 获取 Entity 实例
     *
     * @return
     */
    @Nullable
    default T create() {
        return null;
    }

}
