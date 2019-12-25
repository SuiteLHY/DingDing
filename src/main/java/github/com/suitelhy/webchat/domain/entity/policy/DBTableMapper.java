package github.com.suitelhy.webchat.domain.entity.policy;

import javax.validation.constraints.NotNull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DBTableMapper {

    /**
     * 驼峰命名转下划线
     * @param fieldName - POJO 的字段名
     * @return <code>fieldName</code> 对应的数据库表字段名称
     */
    public static String camelNamesUnderlined(@NotNull String fieldName) {
        StringBuffer columnNameSB = new StringBuffer();
        Matcher matcher = Pattern.compile("[A-Z]").matcher(fieldName);
        while (matcher.find()) {
            //--- (分组匹配)
            String each = matcher.group();
            matcher.appendReplacement(columnNameSB, "_" + each.toLowerCase());
        }
        matcher.appendTail(columnNameSB);
        if (columnNameSB.charAt(0) == '_') {
            columnNameSB.delete(0, 1);
        }
        return columnNameSB.toString();
    }

}
