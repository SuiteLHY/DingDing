package github.com.suitelhy.webchat.domain.entity.util;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 数据源 POJOs 操作辅助工具
 */
public final class EntityUtil {

    /**
     * 通过反射执行指定方法
     * @param entity
     * @param fieldName
     * @param parameterTypes
     * @param args
     * @param <T>
     * @return 所执行的方法的返回值 - 可为 null, 此时成功执行方法且方法返回值
     *-> 为 null, 或者方法执行失败且由 <code>invokeMethod</code> 方法处理
     */
    public static <T extends java.io.Serializable> Object invokeMethod(@NotNull T entity
            , @NotNull String fieldName
            , @Nullable Class<?>[] parameterTypes
            , @Nullable Object[] args) {
        Object result = null;
        Method method;
        try {
            method = entity.getClass().getMethod(fieldName, null);
            result = method.invoke(entity, null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("反射调用工具 - 通过反射执行指定方法出错", e);
        }
        return result;
    }

}
