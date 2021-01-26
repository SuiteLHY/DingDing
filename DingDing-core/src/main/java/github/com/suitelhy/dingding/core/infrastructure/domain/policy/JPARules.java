package github.com.suitelhy.dingding.core.infrastructure.domain.policy;

import github.com.suitelhy.dingding.core.infrastructure.util.RegexUtil;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.regex.Matcher;

/**
 * ORM框架 - JPA实现 - 基于JPA规范的基础逻辑操作
 */
public final class JPARules {

    /**
     * 驼峰命名风格转下划线风格
     *
     * @param fieldName - 字段名
     * @return
     */
    public static String camelCaseNameToUnderlined(@NotNull String fieldName) {
        StringBuffer columnNameSB = new StringBuffer();
        /*Matcher matcher = Pattern.compile("[A-Z]").matcher(fieldName);*/
        Matcher matcher = RegexUtil.getInstance().getPattern("[A-Z]").matcher(fieldName);
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

    /**
     * 根据字段名获取对应的 Getter 方法名
     *
     * @param field - 字段名
     * @return 可以为 null, 此时字段名不符合 JPA - POJO 属性命名规则
     */
    public static String getGetMethodName(@NotNull Field field) {
        StringBuffer getMethodNameSB = new StringBuffer();
        String fieldName = field.getName();
        if (fieldName.length() == 1) {
            return null;
        }
        if (field.getType() == Boolean.TYPE
                || field.getType() == Boolean.class) {
            getMethodNameSB.append("is");
        } else {
            getMethodNameSB.append("get");
        }
        char[] fieldNameArray = fieldName.toCharArray();
        fieldNameArray[0] = Character.isUpperCase(fieldNameArray[1])
                ? fieldNameArray[0]
                : Character.toUpperCase(fieldNameArray[0]);
        getMethodNameSB.append(fieldNameArray);
        return getMethodNameSB.toString();
    }

}
