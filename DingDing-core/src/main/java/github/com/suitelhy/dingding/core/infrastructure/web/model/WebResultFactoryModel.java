package github.com.suitelhy.dingding.core.infrastructure.web.model;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

/**
 * {@link R} 的工厂类接口
 *
 * @param <R>   {@link WebResult}
 */
public interface WebResultFactoryModel<R extends WebResult<?>> {

//    /**
//     * 获取{@link R}实例
//     *
//     * @return 可为 {@link null}.
//     */
//    default @Nullable R create() {
//        return null;
//    }

    /**
     * 获取缺省对象
     *
     * @return 非 {@code null}.
     */
    @NotNull R createDefault();

}
