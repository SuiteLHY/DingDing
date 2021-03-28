package github.com.suitelhy.dingding.core.infrastructure.util;

import javax.validation.constraints.NotNull;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 线程工具类
 *
 * @Description 线程工具.
 */
public class ThreadUtil {

    /**
     * @Design (单例模式 - 登记式)
     */
    private static class Factory {
        private static final ThreadUtil SINGLETON = new ThreadUtil();
    }

    private ThreadUtil() {
    }

    public static @NotNull ThreadUtil getInstance() {
        return Factory.SINGLETON;
    }

    public boolean shutdownThreadPool(@NotNull ScheduledExecutorService service) {
        if (null == service) {
            return false;
        }

        if (! service.isShutdown()) {
            service.shutdown();
        }
        return service.isShutdown();
    }

}
