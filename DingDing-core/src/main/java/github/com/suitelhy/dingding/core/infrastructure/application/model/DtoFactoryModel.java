package github.com.suitelhy.dingding.core.infrastructure.application.model;

import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityFactoryModel;

import javax.validation.constraints.NotNull;

/**
 * {@link DtoModel} 工厂接口
 *
 * @param <_DTO> {@link DtoModel} 的实现类
 */
public interface DtoFactoryModel<_DTO extends DtoModel<?, ID>, ID>
        extends EntityFactoryModel<_DTO> {

    /**
     * 获取空对象
     *
     * @return 非 {@code null}.
     */
    @NotNull
    _DTO createDefault();

}
