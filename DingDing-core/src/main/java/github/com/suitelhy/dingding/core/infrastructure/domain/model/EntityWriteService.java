package github.com.suitelhy.dingding.core.infrastructure.domain.model;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * [Entity 对象 <- 基础业务接口] -> 带写入操作业务
 *
 * @Description 确保对数据库事务的支持.
 *
 * @author Suite
 */
@Transactional(isolation = Isolation.SERIALIZABLE
        , propagation = Propagation.REQUIRED
        , readOnly = false
        , rollbackFor = Exception.class
        , timeout = 15)
public interface EntityWriteService
        extends EntityService {
}
