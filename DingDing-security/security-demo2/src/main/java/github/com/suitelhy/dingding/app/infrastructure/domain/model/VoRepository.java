package github.com.suitelhy.dingding.app.infrastructure.domain.model;

import java.util.List;

/**
 * VO 对象持久化接口
 *
 * @param <T> VO 类型
 * @param <V> VO 对象的 <method>value()</method> 返回值类型
 */
public interface VoRepository<T extends VoModel, V> {

    /**
     * 获取 VO 对象
     *
     * @param value VO 对象的 <method>value()</method>
     * @return
     */
    List<T> find(V value);

}
