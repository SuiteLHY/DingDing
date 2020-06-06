package github.com.suitelhy.dingding.core.domain.service.security;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * RBAC 模型 - Service 接口
 *
 * @Description 基于 RBAC 数据模型设计的 Service 接口.
 */
public interface RbacService {

    boolean hasPermission(HttpServletRequest request, Authentication authentication);

}
