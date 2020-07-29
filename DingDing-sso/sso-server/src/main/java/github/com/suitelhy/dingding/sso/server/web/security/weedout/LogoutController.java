package github.com.suitelhy.dingding.sso.server.web.security.weedout;

import github.com.suitelhy.dingding.core.infrastructure.web.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
