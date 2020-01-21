package github.com.suitelhy.dingding.infrastructure.domain.model;

/**
 * (停用) Entity 对象持久化接口
 * @Update 换用 Spring Data Commons 提供的 <interface>org.springframework.data.repository.Repository</interface>.
 * @param <T> Entity 类型
 * @param <I> Entity 对象的 <method>id()</method> 返回值类型
 */
public interface EntityRepository<T extends EntityModel, I> {

    /**
     * 获取 Entity 对象
     * @param id    Entity 对象的 <method>id()</method>
     * @return
     */
    T find(I id);

}
