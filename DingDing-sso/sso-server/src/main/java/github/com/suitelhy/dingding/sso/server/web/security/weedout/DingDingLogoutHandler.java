//package github.com.suitelhy.dingding.sso.server.web.security;
//
//import SecurityUser;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.logout.LogoutHandler;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * DingDing - 注销操作处理器
// *
// * @Description 项目自定义处理器.
// */
//@Component
//@Slf4j
//public class DingDingLogoutHandler
//        implements LogoutHandler {
//
//    /**
//     * Causes a logout to be completed. The method must complete successfully.
//     *
//     * @param request        the HTTP request
//     * @param response       the HTTP response
//     * @param authentication the current principal details
//     */
//    @Override
//    public void logout(HttpServletRequest request
//            , HttpServletResponse response
//            , Authentication authentication) {
//        SecurityUser user = (SecurityUser) authentication.getPrincipal();
//
//        if (null != user) {
//            System.err.println("===== username: " + user.getUsername());
//        }
//
//
//    }
//
//}
