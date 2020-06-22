package github.com.suitelhy.dingding.core.domain.service.security;

import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

/**
 * RBAC 模型 - Service 接口
 *
 * @Description 基于 RBAC 数据模型设计的 Service 接口.
 */
@Transactional(isolation = Isolation.READ_COMMITTED
        , propagation = Propagation.REQUIRED
        , readOnly = true
        , rollbackFor = Exception.class
        , timeout = 15)
public interface RbacService {

    boolean hasPermission(HttpServletRequest request, Authentication authentication);

}
