package github.com.suitelhy.dingding.sso.server.infrastructure.web;

import javax.validation.constraints.NotNull;

/**
 * 项目定制 CORS 校验设计实现
 *
 * @author Suite
 */
public class DingDingCorsProcessor {

    /**
     * 正则校验器
     *
     * @author Suite
     */
    public static class Regex {

        // (单例模式 - 登记式)
        private static class Factory {
            private static final github.com.suitelhy.dingding.core.infrastructure.web.cors.RegexCorsProcessor SINGLETON = new github.com.suitelhy.dingding.core.infrastructure.web.cors.RegexCorsProcessor();
        }

        @NotNull
        public static github.com.suitelhy.dingding.core.infrastructure.web.cors.RegexCorsProcessor getInstance() {
            return Factory.SINGLETON;
        }

        private Regex() {}

    }

    private DingDingCorsProcessor() {}

}
