package github.com.suitelhy.dingding.core.infrastructure.domain.model;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * [Entity 对象 <- 基础业务接口] -> 只读操作业务
 *
 * @Description 确保对数据库事务的支持.
 *
 * @author Suite
 */
@Transactional(isolation = Isolation.READ_COMMITTED
        , propagation = Propagation.SUPPORTS
        , readOnly = true
        , rollbackFor = Exception.class
        , timeout = 10)
public interface EntityReadService
        extends EntityService {
}
