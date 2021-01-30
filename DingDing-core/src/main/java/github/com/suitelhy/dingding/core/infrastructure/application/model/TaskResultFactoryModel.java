package github.com.suitelhy.dingding.core.infrastructure.application.model;

import org.springframework.lang.Nullable;

/**
 * {@link R} 的工厂类接口
 *
 * @param <R> {@link TaskResult}
 */
public interface TaskResultFactoryModel<R extends TaskResult> {

    /**
     * 获取{@link R}实例
     *
     * @return 可为 {@link null}.
     */
    @Nullable
    default R create() {
        return null;
    }

}
