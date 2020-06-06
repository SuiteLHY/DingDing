package github.com.suitelhy.dingding.sso.authorization.domain.service.impl;

import github.com.suitelhy.dingding.core.domain.service.security.RbacService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * RBAC 授权模型的基本实现
 *
 * @author Suite
 */
@Service("rbacService")
public class DingDingRbacServiceImpl
        implements RbacService {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     *
     *
     * @param request
     * @param authentication
     * @return
     */
    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        boolean hasPermission = false;

        Object principal = authentication.getPrincipal();
        /*if (principal instanceof User) {
            //--- 读取用户有权限访问的所有 URL
            Set<String> urls = ((User) principal).getUrls();
            for (String url : urls) {
                if (antPathMatcher.match(url, request.getRequestURI())) {
                    hasPermission = true;
                    break;
                }
            }
        }*/

        return hasPermission;
    }

}
