package github.com.suitelhy.dingding.sso.authorization.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import github.com.suitelhy.dingding.core.infrastructure.web.AbstractSecurityUser;
import github.com.suitelhy.dingding.core.infrastructure.web.model.DingDingResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * DingDing - 注销成功处理器
 *
 * @Description 项目自定义处理器.
 *
 * @see LogoutSuccessHandler
 */
@Component
@Slf4j
public class DingDingLogoutSuccessHandler
        implements LogoutSuccessHandler {

    @Autowired
    private ObjectMapper toJSONString;

    /**
     * @Description 成功退出时调用.
     *
     * @param request
     * @param response
     * @param authentication
     *
     * @see LogoutSuccessHandler#onLogoutSuccess(HttpServletRequest, HttpServletResponse, Authentication)
     *
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request
            , HttpServletResponse response
            , Authentication authentication)
            throws IOException, ServletException {

        final AbstractSecurityUser user = (null == authentication)
                ? null
                : (AbstractSecurityUser) authentication.getPrincipal();

        //===== 设计: 返回 JSON 格式数据
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(
                toJSONString.writeValueAsString(new DingDingResponse("用户" + user + "退出成功"))
        );
    }

}
