package github.com.suitelhy.dingding.log.service.client.web;

import github.com.suitelhy.dingding.core.infrastructure.web.model.WebResult;
import github.com.suitelhy.dingding.log.service.client.application.task.LogTask;
import github.com.suitelhy.dingding.security.service.api.infrastructure.web.OAuth2AuthenticationInfo;
import github.com.suitelhy.dingding.security.service.api.infrastructure.web.util.OAuth2AuthenticationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

/**
 * 日志业务
 *
 * @Description 日志基础增删查改业务.
 */
@RequestMapping("/log")
@RestController
@Slf4j
public class LogController {

    @Autowired
    private LogTask logTask;

    /**
     * 查询用户对应的日志记录
     *
     * @param authentication
     * @param id
     *
     * @return
     */
    @GetMapping("/log/{id}")
    public String getLogDetail(@AuthenticationPrincipal OAuth2Authentication authentication
            , @PathVariable String id)
    {
        try {
            final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails userAuthenticationDetails = OAuth2AuthenticationUtil.getInstance().getDetails(authentication);

            return WebResult.Factory.DEFAULT
                    .create(logTask.selectLogById(id, userAuthenticationDetails))
                    .toString();
        } catch (Exception e) {
            log.error("LogController#getLogDetail", e);
            e.printStackTrace();
        }
        return WebResult.Factory.DEFAULT.createDefault().toString();
    }

    /**
     * 查询用户对应的日志记录列表
     *
     * @param authentication
     * @param pageCount
     * @param pageSize
     *
     * @return
     */
    @GetMapping("/logList")
    public String getLogList(@AuthenticationPrincipal OAuth2Authentication authentication
            , @RequestParam(name = "page_count") int pageCount
            , @RequestParam(name = "page_size") int pageSize)
    {
        try {
            final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails userAuthenticationDetails = OAuth2AuthenticationUtil.getInstance().getDetails(authentication);

            return WebResult.Factory.DEFAULT
                    .create(logTask.selectLogByUsername(userAuthenticationDetails.getUserName(), pageCount, pageSize, userAuthenticationDetails))
                    .toString();
        } catch (Exception e) {
            log.error("LogController#getLogList", e);
            e.printStackTrace();
        }
        return WebResult.Factory.DEFAULT.createDefault().toString();
    }

    /**
     * 查询[所有的日志记录]列表
     *
     * @Description 需要有足够的管理员权限.
     *
     * @param authentication
     * @param pageCount
     * @param pageSize
     *
     * @return
     */
    @GetMapping("/allLogList")
    public String getAllLogList(@AuthenticationPrincipal OAuth2Authentication authentication
            , @RequestParam(name = "page_count") int pageCount
            , @RequestParam(name = "page_size") int pageSize)
    {
        try {
            final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails userAuthenticationDetails = OAuth2AuthenticationUtil.getInstance().getDetails(authentication);

            return WebResult.Factory.DEFAULT
                    .create(logTask.selectAll(pageCount, pageSize, userAuthenticationDetails))
                    .toString();
        } catch (Exception e) {
            log.error("LogController#getAllLogList", e);
            e.printStackTrace();
        }
        return WebResult.Factory.DEFAULT.createDefault().toString();
    }

}
