package github.com.suitelhy.dingding.app.web.weedout;

import github.com.suitelhy.dingding.app.application.task.LogTask;
import github.com.suitelhy.dingding.app.application.task.UserTask;
import github.com.suitelhy.dingding.app.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.app.infrastructure.application.dto.UserDto;
import github.com.suitelhy.dingding.app.infrastructure.util.CalendarController;
import github.com.suitelhy.dingding.app.infrastructure.web.util.LogUtil;
import github.com.suitelhy.dingding.app.infrastructure.web.util.NetUtil;
import github.com.suitelhy.dingding.app.infrastructure.web.util.WordDefined;
import github.com.suitelhy.dingding.app.infrastructure.domain.vo.Account;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户登录登出 - Web请求控制器
 */
@Controller
@RequestMapping(value = "/user")
public class LoginController {

    @Autowired
    private UserTask userTask;

    @Autowired
    private LogTask logTask;

    /**
     * 登入 - 页面
     *
     * @return
     */
    @ApiOperation(value = "用户登录页面")
    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    /**
     * 登入 - 操作
     *
     * @param username
     * @param password
     * @param session
     * @param attributes
     * @param defined
     * @param logUtil
     * @param request
     * @return
     */
    @ApiOperation(value = "用户登录操作")
    @PostMapping(value = "/login")
    public String login(@AuthenticationPrincipal SecurityUser currentUser
            , String username
            , String password
            , HttpSession session
            , RedirectAttributes attributes
            , WordDefined defined
            , LogUtil logUtil
            , HttpServletRequest request) {
        final String ip = NetUtil.getIpAddress(request);
        /*final UserDto user = userTask.selectUserByUsername(username);*/
        final UserDto user = UserDto.Factory.USER_DTO.create(currentUser);
        if (null == user || user.isEmpty()) {
            attributes.addFlashAttribute("error", defined.LOGIN_USERID_ERROR);
            // 重定向 -> 登入 - 页面
            return "redirect:/user/login";
        }
        if (null == user.equalsPassword(password) || !user.equalsPassword(password)) {
            attributes.addFlashAttribute("error", defined.LOGIN_PASSWORD_ERROR);
            // 重定向 -> 登入 - 页面
            return "redirect:/user/login";
        }
        if (!Account.StatusVo.NORMAL.showName().equals(user.getStatus())) {
            attributes.addFlashAttribute("error", defined.LOGIN_USERID_DISABLED);
            // 重定向 -> 登入 - 页面
            return "redirect:/user/login";
        }

        ServletContext servletContext = request.getServletContext();
        if (null == servletContext.getAttribute("online")) {
            List<String> list = new ArrayList<>();
            list.add(username);
            servletContext.setAttribute("online", list);
        } else {
            List<String> list = (List<String>) servletContext.getAttribute("online");
            if (list.contains(username)) {
                attributes.addFlashAttribute("error", "已在线");
                // 重定向 -> 登入 - 页面
                return "redirect:/user/login";
            }
        }

        logTask.insert(logUtil.setLog(user.id()
                , new CalendarController().toString()
                , defined.LOG_TYPE_LOGIN
                , defined.LOG_DETAIL_USER_LOGIN
                , ip));

        session.setAttribute("userid", username);
        session.setAttribute("login_status", true);

        userTask.update(user
                , currentUser.getPassword()
                , ip
                , new CalendarController().toString());

        attributes.addFlashAttribute("message", defined.LOGIN_SUCCESS);
        // 重定向 -> 视频主页(暂未开放)
        return "redirect:/chat";
    }

    /**
     * 登出 - 操作
     *
     * @param session
     * @param attributes
     * @param defined
     * @return
     */
    @ApiOperation(value = "用户登出操作")
    @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(HttpSession session
            , RedirectAttributes attributes
            , WordDefined defined) {
        session.removeAttribute("userid");
        session.removeAttribute("login_status");

        attributes.addFlashAttribute("message"
                , defined.LOGOUT_SUCCESS);
        // 重定向 -> 登入 - 页面
        return "redirect:/user/login";
    }

}
