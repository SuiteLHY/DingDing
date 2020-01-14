package github.com.suitelhy.webchat.web;

import github.com.suitelhy.webchat.application.task.LogTask;
import github.com.suitelhy.webchat.application.task.UserTask;
import github.com.suitelhy.webchat.domain.entity.User;
import github.com.suitelhy.webchat.domain.vo.AccountVo;
import github.com.suitelhy.webchat.infrastructure.web.util.CommonDate;
import github.com.suitelhy.webchat.infrastructure.web.util.LogUtil;
import github.com.suitelhy.webchat.infrastructure.web.util.NetUtil;
import github.com.suitelhy.webchat.infrastructure.web.util.WordDefined;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
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

    @Resource
    private UserTask userTask;

    @Resource
    private LogTask logTask;

    /**
     * 登入 - 页面
     * @return
     */
    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    /**
     * 登入 - 操作
     * @param userid
     * @param password
     * @param session
     * @param attributes
     * @param defined
     * @param date
     * @param logUtil
     * @param netUtil
     * @param request
     * @return
     */
    @PostMapping(value = "/login")
    public String login(String userid
            , String password
            , HttpSession session
            , RedirectAttributes attributes
            , WordDefined defined
            , CommonDate date
            , LogUtil logUtil
            , NetUtil netUtil
            , HttpServletRequest request) {
        final String ip = netUtil.getIpAddress(request);
    	final User user = userTask.selectUserByUserid(userid);
        if (null == user) {
            attributes.addFlashAttribute("error", defined.LOGIN_USERID_ERROR);
            return "redirect:/user/login";
        }
        if (!user.getPassword().equals(password)) {
            attributes.addFlashAttribute("error", defined.LOGIN_PASSWORD_ERROR);
            return "redirect:/user/login";
        }
        if (user.getStatus() != AccountVo.Status.NORMAL) {
            attributes.addFlashAttribute("error", defined.LOGIN_USERID_DISABLED);
            return "redirect:/user/login";
        }
        ServletContext application = request.getServletContext();
        if (null == application.getAttribute("online")) {
            List<String> list = new ArrayList<>();
            list.add(userid);
            application.setAttribute("online", list);
        } else {
            List<String> list = (List<String>) application.getAttribute("online");
            if (list.contains(userid)) {
                attributes.addFlashAttribute("error","已在线");
                return "redirect:/user/login";
            }
        }
        logTask.insert(logUtil.setLog(userid
                , date.getTime24()
                , defined.LOG_TYPE_LOGIN
                , defined.LOG_DETAIL_USER_LOGIN
                , ip));

        session.setAttribute("userid", userid);
        session.setAttribute("login_status", true);

        user.setLasttime(date.getTime24());
        user.setIp(ip);
        userTask.update(user);

        attributes.addFlashAttribute("message", defined.LOGIN_SUCCESS);
        return "redirect:/chat";
    }

    /**
     * 登出 - 操作 & 页面
     * @param session
     * @param attributes
     * @param defined
     * @return
     */
    @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(HttpSession session
            , RedirectAttributes attributes
            , WordDefined defined) {
        session.removeAttribute("userid");
        session.removeAttribute("login_status");

        attributes.addFlashAttribute("message"
                , defined.LOGOUT_SUCCESS);
        return "redirect:/user/login";
    }

}
