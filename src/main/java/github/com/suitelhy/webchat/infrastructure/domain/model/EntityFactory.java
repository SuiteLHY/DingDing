package github.com.suitelhy.webchat.infrastructure.domain.model;

/**
 * Entity 工厂接口
 * @param <T> - Entity 类对象
 */
public interface EntityFactory<T extends EntityModel> {

    /**
     * 获取 Entity 实例
     * @return
     */
    T create();

}
