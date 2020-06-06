package github.com.suitelhy.dingding.core.infrastructure.domain.util;

import github.com.suitelhy.dingding.core.infrastructure.util.RegexUtil;

import javax.validation.constraints.NotNull;

/**
 * SQL 工具类
 */
public class SqlUtil {

    public static class Regex {

        /**
         * 校验字段名称格式
         *
         * @param sql
         * @return
         * @Description 使用正则表达式校验
         */
        public static boolean validateSQL(@NotNull String sql) {
            return RegexUtil.getPattern("^[A-Za-z0-9.]*.$").matcher(sql).matches();
        }

        private Regex() {
        }

    }

}
