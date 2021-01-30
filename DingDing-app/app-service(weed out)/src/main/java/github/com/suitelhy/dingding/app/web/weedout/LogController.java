package github.com.suitelhy.dingding.app.web.weedout;

import github.com.suitelhy.dingding.app.application.task.LogTask;
import github.com.suitelhy.dingding.app.domain.entity.Log;
import github.com.suitelhy.dingding.app.infrastructure.web.config.LogControllerConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * 日志记录 - 请求处理
 */
@Controller
@RequestMapping(value = "/log")
public class LogController {

    @Resource
    private LogTask logTask;

    @GetMapping(value = "/{userid}/log")
    public ModelAndView selectAll(@PathVariable("userid") String userid
            , @RequestParam(defaultValue = "1") Integer page) {
        ModelAndView view = new ModelAndView("log");

        List<Log> list = logTask.selectLogByUserid(userid
                , page
                , LogControllerConfig.PAGE_SIZE);
        Long count = logTask.selectCountByUserid(userid
                , LogControllerConfig.PAGE_SIZE);

        view.addObject("list", list);
        view.addObject("count", count);
        return view;
    }

}
