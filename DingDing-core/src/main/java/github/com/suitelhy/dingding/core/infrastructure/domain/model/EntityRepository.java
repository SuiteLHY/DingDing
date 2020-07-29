package github.com.suitelhy.dingding.core.infrastructure.domain.model;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * Entity 对象 <- 持久化接口
 *
 * @Description  与 <interface>org.springframework.data.repository.Repository</interface> 并用.
 *
 */
@Transactional(isolation = Isolation.READ_COMMITTED
        , propagation = Propagation/*.REQUIRED*/.MANDATORY
        , timeout = 15)
public interface EntityRepository/*<T extends EntityModel, I>*/ {

//    /**
//     * 获取 Entity 对象
//     *
//     * @param id Entity 对象的 <method>id()</method>
//     * @return
//     */
//    T find(I id);

}
