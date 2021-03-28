package github.com.suitelhy.dingding.core.infrastructure.util;

import javax.validation.constraints.NotNull;

public enum StringUtil {
    DEFAULT;

    public <T> @NotNull String notNull(T obj) {
        return (null == obj) ? "" : obj.toString();
    }

}
