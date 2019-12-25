package github.com.suitelhy.webchat.domain.entity.policy;

import github.com.suitelhy.webchat.domain.entity.annotation.SuiteColumn;
import github.com.suitelhy.webchat.domain.entity.util.EntityUtil;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 注解属性缺省值类（单例）
 */
final class AnnotationMethodDefaults {

    private static class DefaultsFactory {
        public static final AnnotationMethodDefaults INSTANCE = new AnnotationMethodDefaults();
    }

    public static final AnnotationMethodDefaults getInstance() {
        return DefaultsFactory.INSTANCE;
    }

    String suiteColumnValue;

    String repositoryValue;

    private AnnotationMethodDefaults() {
        try {
            this.suiteColumnValue = (String) SuiteColumn.class.getMethod("value").getDefaultValue();
            this.repositoryValue = (String) Repository.class.getMethod("value").getDefaultValue();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("注解的指定方法不存在, 装配方法缺省值出错!", e);
        }
    }

}

/**
 * POJO 数据定义规则
 */
final class EntityRules {

    /**
     * 根据字段名获取对应的 Getter 方法名
     * @param field - 字段名
     * @return 可以为 null, 此时字段名不符合 POJO 属性命名规则
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

/**
 * POJO 映射数据库表策略
 *
 */
public class EntityMapperTable<E extends java.io.Serializable> {

    //===== static properties and methods =====//
    /**
     * 获取数据库表字段名
     * @param entityClazz - POJO 的类对象
     * @param <T>
     * @return
     */
    public static <T extends java.io.Serializable> String[] getColumns(@NotNull Class<T> entityClazz) {
        List<String> resultList = new ArrayList<>(0);
        Field[] fields = entityClazz.getFields();
        if (fields.length > 0) {
            for (Field field : fields) {
                String columnValue = getColumn(field);
                if (null == columnValue) continue;
                resultList.add(columnValue);
            }
        }
        return resultList.toArray(new String[0]);
    }

    public static <T extends java.io.Serializable> String[] getColumns(@NotNull Class<T> entityClazz
            , @NotNull String[] fieldNames) {
        List<String> resultList = new ArrayList<>(0);
        Field[] fields = entityClazz.getFields();
        if (fields.length > 0) {
            List<String> fieldNamesList = Arrays.asList(fieldNames);
            for (Field field : fields) {
                if (!fieldNamesList.contains(field.getName())) continue;
                String columnValue = getColumn(field);
                if (null == columnValue) continue;
                resultList.add(columnValue);
            }
        }
        return resultList.toArray(new String[0]);
    }

    /**
     * 获取 {数据库表字段名 - POJO 属性值} 的映射集合
     * @param entity
     * @param <T>
     * @return
     */
    public static <T extends java.io.Serializable> Map<String, Object> getColumnsMap(@NotNull T entity) {
        Map<String, Object> result = new LinkedHashMap<>(0);
        Field[] fields = entity.getClass().getFields();
        if (fields.length > 0) {
            for (Field field : fields) {
                String columnName = getColumn(field);
                if (null == columnName) continue;
                Object fieldValue = EntityUtil.invokeMethod(entity
                        , field.getName()
                        , null
                        , null);
                result.put(columnName, fieldValue);
            }
        }
        return result;
    }

    /**
     * 获取用于生成 SQL 的 {数据库表字段名 - POJO 属性值} 的映射集合
     * @param entity
     * @param <T>
     * @return
     */
    public static <T extends java.io.Serializable> Map<String, String> getColumnsMapForSQL(@NotNull T entity) {
        Map<String, String> result = new LinkedHashMap<>(0);
        Field[] fields = entity.getClass().getFields();
        if (fields.length > 0) {
            for (Field field : fields) {
                String columnName = getColumn(field);
                if (null == columnName) continue;
                Object fieldValue = EntityUtil.invokeMethod(entity
                        , field.getName()
                        , null
                        , null);
                result.put(columnName, (null != fieldValue)
                        ? String.valueOf(fieldValue)
                        : null);
            }
        }
        return result;
    }

    /**
     * 获取 <code>Field</code> 对象对应的数据库表字段名
     * @param field
     * @param <T>
     * @return 可为 null, 此时 <code>field</code> 没有映射到数据库表字段
     */
    private static <T extends java.io.Serializable> String getColumn(@NotNull Field field) {
        final String result;
        SuiteColumn fieldColumn;
        try {
            fieldColumn = field.getAnnotation(SuiteColumn.class);
        } catch (NullPointerException e) {
            // 此时 <code>field</code> 没有映射到数据库表字段
            return null;
        }
        // 注解中的方法的缺省值和注入值都不能为 null (IDE编译验证), 可放心使用
        String columnValue = fieldColumn.value();
        String defaultColumnValue = AnnotationMethodDefaults.getInstance().suiteColumnValue;
        result = defaultColumnValue.equals(columnValue)
                ? DBTableMapper.camelNamesUnderlined(field.getName())
                : columnValue;
        return result;
    }

    /**
     * 获取指定属性对应的数据库表字段名
     * @param entityClazz - POJO 的 <code>Class</code> 对象
     * @param fieldName - POJO 的指定属性名称
     * @param <T> - POJO 类型
     * @return 可为 null, 此时 POJO 的指定属性不存在, 或者没有映射到数据库表字段
     */
    public static <T extends java.io.Serializable> String getColumn(@NotNull Class<T> entityClazz
            , @NotNull String fieldName) {
        try {
            return getColumn(entityClazz.getClass().getField(fieldName));
        } catch (NoSuchFieldException e) {
            // POJO 中没有找到指定名称的字段
            return null;
        }
    }

    /**
     * 获取数据库表名
     * @param entityClazz - POJO 的类对象
     * @param <T>
     * @return
     */
    public static <T extends java.io.Serializable> String getTableName(@NotNull Class<T> entityClazz) {
        final String result;
        String tableValue = entityClazz.getAnnotation(Repository.class).value();
        String defaultValue = AnnotationMethodDefaults.getInstance().repositoryValue;
        result = defaultValue.equals(tableValue)
                ? DBTableMapper.camelNamesUnderlined(entityClazz.getSimpleName())
                : tableValue;
        return result;
    }

    //===== non-static properties and methods =====//
    public final Class<E> entityClazz;

    public EntityMapperTable(Class<E> entityClazz) {
        this.entityClazz = entityClazz;
    }

    public String[] getColumns() {
        return getColumns(this.entityClazz);
    }

    public String getColumnsStr() {
        StringBuilder resultSB = new StringBuilder();
        for (String each : getColumns()) {
            if (resultSB.length() > 0) {
                resultSB.append(',');
            }
            resultSB.append(each);
        }
        return resultSB.toString();
    }

    public String getColumn(@NotNull String fieldName) {
        return getColumn(this.entityClazz, fieldName);
    }

    public String getTableName() {
        return getTableName(this.entityClazz);
    }

}
