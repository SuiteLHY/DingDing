package github.com.suitelhy.dingding.sso.authorization.domain.service.impl;

import github.com.suitelhy.dingding.core.domain.service.security.RbacService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityUserService;
import github.com.suitelhy.dingding.core.infrastructure.web.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * RBAC 授权模型的基本实现
 *
 * @author Suite
 */
@Service("rbacService")
public class DingDingRbacServiceImpl
        implements RbacService {

    @Autowired
    private SecurityUserService securityUserService;

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
        if (principal instanceof SecurityUser) {
            //--- 读取用户有权限访问的所有 URL
            List<Map<String, Object>> urlInfoList = securityUserService.selectUrlPathByUsername(((SecurityUser) principal).getUsername());
            for (Map<String, Object> urlInfo : urlInfoList) {
                if (antPathMatcher.match((String) urlInfo.get("url"), request.getRequestURI())) {
                    hasPermission = true;
                    break;
                }
            }
        }

        return hasPermission;
    }

}
