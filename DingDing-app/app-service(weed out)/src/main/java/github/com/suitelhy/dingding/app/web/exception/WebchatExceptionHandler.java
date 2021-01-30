package github.com.suitelhy.dingding.app.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 定制化异常信息处理器
 *
 * @Reference -> {@link <a href="https://github.com/SuiteLHY/SuiteLHY/blob/master/src/main/java/spring/example/SpringWebMVC/web/exception/MyExceptionHandler.java">SuiteLHY/MyExceptionHandler.java at master · SuiteLHY/SuiteLHY</a>}
 */
@Component
@Slf4j
public class WebchatExceptionHandler
        implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request
            , HttpServletResponse response
            , Object handler
            , Exception ex) {
        //--- 可以针对异常类型使用状态机
        /**
         * 获取异常对象的堆栈跟踪信息
         *
         * @Reference
         *-> {@link <a href="https://stackoverflow.com/questions/1149703/how-can-i-convert-a-stack-trace-to-a-string">【Stack Overflow】How can I convert a stack trace to a string?</a>}
         */
        StringWriter exStringWriter = new StringWriter();
        ex.printStackTrace(new PrintWriter(exStringWriter));

        request.setAttribute("ex", ex);
        request.setAttribute("exStackTrace", exStringWriter.toString());

        log.warn("异常已被处理", ex);

        return new ModelAndView("/exception/defaultExceptionPage");
    }

}
