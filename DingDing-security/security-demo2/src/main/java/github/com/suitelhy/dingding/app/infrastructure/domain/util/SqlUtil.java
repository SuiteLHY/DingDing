package github.com.suitelhy.dingding.app.infrastructure.domain.util;

import github.com.suitelhy.dingding.app.infrastructure.util.RegexUtil;

import javax.validation.constraints.NotNull;

/**
 * SQL 工具类
 */
public class SqlUtil {

    public static class Regex {

        /**
         * 校验字段名称格式
         * @Description 使用正则表达式校验
         * @param sql
         * @return
         */
        public static boolean validateSQL(@NotNull String sql) {
            return RegexUtil.getPattern("^[A-Za-z0-9.]*.$").matcher(sql).matches();
        }

        private Regex() {}

    }

}
