package github.com.suitelhy.dingding.sso.server.web.security.weedout;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@Slf4j
public class LogoutController {

//    @PostMapping("/logout")
//    public void logout(HttpServletRequest request
//            , HttpServletResponse response
//            , @AuthenticationPrincipal SecurityUser securityUser) {
//        // token can be revoked here if needed
//        new SecurityContextLogoutHandler().logout(request, null, null);
//        try {
//            //sending back to client app
//            response.sendRedirect(request.getHeader("referer"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


}
