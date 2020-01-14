package github.com.suitelhy.webchat.infrastructure.domain.util;

import github.com.suitelhy.webchat.infrastructure.domain.model.EntityModel;
import github.com.suitelhy.webchat.infrastructure.domain.policy.DBPolicy;
import github.com.suitelhy.webchat.infrastructure.util.RegexUtil;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Entity 层 - 业务辅助工具
 *
 */
public final class EntityUtil {

    public static class Regex {

        /**
         * 校验 -> Entity 属性名称
         * @Description 使用正则表达式校验
         * @param fieldName
         * @return
         */
        public static boolean validateFieldName(@Nullable String fieldName) {
                return null != fieldName
                        && RegexUtil.getPattern("[a-z][A-Za-z0-9]+").matcher(fieldName).matches();
        }

        /**
         * 校验 -> Entity 唯一标识 (ID)
         * @Description Entity - ID 校验通用规范. 至多32位的16(或以下)进制数字
         * @param id
         * @return
         */
        public static boolean validateId(@Nullable String id) {
            return null != id
                    && (RegexUtil.getPattern("^[a-zA-Z0-9]{1,32}$").matcher(id).matches()
                            || DBPolicy.MYSQL.validateUuid(id));
        }

        /**
         * 校验 -> 用户密码
         * @Description 用户密码规则: 以字母开头, 长度在6~18之间, 只能包含字母、数字和下划线.
         * @param password
         * @return
         */
        public static boolean validateUserPassword(@Nullable String password) {
            return null != password
                    && RegexUtil.getPattern("^[a-zA-Z]\\w{5,17}$").matcher(password).matches();
        }

        private Regex() {}

    }

    /**
     * 通过反射执行指定方法
     * @param entity
     * @param fieldName
     * @param parameterTypes
     * @param args
     * @param <T>
     * @return 所执行的方法的返回值 - 可为 null, 此时成功执行方法且方法返回值
     *-> 为 null, 或者方法执行失败且由 <code>invokeMethod</code> 方法处理异常.
     */
    public static <T extends EntityModel> Object invokeMethod(@NotNull T entity
            , @NotNull String fieldName
            , @Nullable Class<?>[] parameterTypes
            , @Nullable Object[] args) {
        Object result;
        Method method;
        try {
            method = entity.getClass().getMethod(fieldName, parameterTypes);
            result = method.invoke(entity, args);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("反射调用工具 - 通过反射执行指定方法出错", e);
        }
        return result;
    }

}
