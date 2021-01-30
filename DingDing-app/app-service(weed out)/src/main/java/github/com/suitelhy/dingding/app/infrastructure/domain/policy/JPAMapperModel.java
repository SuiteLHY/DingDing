package github.com.suitelhy.dingding.app.infrastructure.domain.policy;

import github.com.suitelhy.dingding.app.infrastructure.domain.annotation.AnnotationMethodDefaults;
import github.com.suitelhy.dingding.app.infrastructure.domain.annotation.SuiteColumn;
import github.com.suitelhy.dingding.app.infrastructure.domain.annotation.SuiteTable;
import github.com.suitelhy.dingding.app.infrastructure.domain.model.EntityModel;
import github.com.suitelhy.dingding.app.infrastructure.domain.util.EntityUtil;
import github.com.suitelhy.dingding.app.infrastructure.domain.util.SqlUtil;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.*;

///**
// * 注解属性缺省值类（单例）
// */
//final class AnnotationMethodDefaults {
//
//    private static class DefaultsFactory {
//        public static final AnnotationMethodDefaults INSTANCE = new AnnotationMethodDefaults();
//    }
//
//    public static AnnotationMethodDefaults getInstance() {
//        return DefaultsFactory.INSTANCE;
//    }
//
//    String suiteColumnValue;
//
//    String repositoryValue;
//
//    private AnnotationMethodDefaults() {
//        try {
//            this.suiteColumnValue = (String) SuiteColumn.class.getMethod("value").getDefaultValue();
//            this.repositoryValue = (String) Repository.class.getMethod("value").getDefaultValue();
//        } catch (NoSuchMethodException e) {
//            throw new RuntimeException("注解的指定方法不存在, 装配方法缺省值出错!", e);
//        }
//    }
//
//}

/**
 * ORM框架 - JPA实现 - Entity 基本映射业务
 */
public class JPAMapperModel<E extends EntityModel> {

    //===== static basic properties and methods =====//

    /**
     * 获取数据库表字段名
     *
     * @param entityClazz - Entity 的类对象
     * @param <T>
     * @return
     */
    public static <T extends EntityModel> String[] getColumns(@NotNull Class<T> entityClazz) {
        List<String> resultList = new ArrayList<>(0);
        Field[] fields = entityClazz.getDeclaredFields();
        if (fields.length > 0) {
            for (Field field : fields) {
                String columnValue = getColumn(field);
                if (null == columnValue) continue;
                resultList.add(columnValue);
            }
        }
        return resultList.toArray(new String[0]);
    }

    /**
     * 获取数据库表字段名
     *
     * @param entity - Entity
     * @param <T>
     * @return
     */
    public static <T extends EntityModel> String[] getColumns(@NotNull T entity) {
        return getColumns(entity.getClass());
    }

    /**
     * 获取数据库表字段名
     *
     * @param entityClazz
     * @param fieldNames  - 指定的(Entity对象的)字段名集合
     * @param <T>
     * @return
     */
    public static <T extends EntityModel> String[] getColumns(@NotNull Class<T> entityClazz
            , @NotNull String[] fieldNames) {
        List<String> resultList = new ArrayList<>(0);
        Field[] fields = entityClazz.getDeclaredFields();
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
     * 获取数据库表字段名
     *
     * @param entity
     * @param fieldNames - 指定的(Entity对象的)字段名集合
     * @param <T>
     * @return
     */
    public static <T extends EntityModel> String[] getColumns(@NotNull T entity
            , @NotNull String[] fieldNames) {
        return getColumns(entity.getClass(), fieldNames);
    }

    // 屏蔽不安全设计
//    /**
//     * 获取 {数据库表字段名 - Entity属性值} 的映射集合
//     * @param entity
//     * @param <T>
//     * @return [{key=<b>数据库表字段名</b>, value=<b>Entity属性值</b>}, ...]
//     */
//    public static <T extends EntityModel> Map<String, Object> getColumnsMap(@NotNull T entity) {
//        Map<String, Object> result = new LinkedHashMap<>(0);
//        Field[] fields = entity.getClass().getDeclaredFields();
//        if (fields.length > 0) {
//            for (Field field : fields) {
//                String columnName = getColumn(field);
//                if (null == columnName) continue;
//                Object fieldValue = EntityUtil.invokeMethod(entity
//                        , field.getName()
//                        , null
//                        , null);
//                result.put(columnName, fieldValue);
//            }
//        }
//        return result;
//    }

    /**
     * 获取用于生成 SQL 的 {数据库表字段名 - Entity 属性名称SQL表示} 映射集合
     *
     * @param entityClazz
     * @param <T>
     * @return [{<tt>数据库表字段名</tt>=<tt>Entity属性名称SQL表示</tt>}, ...]
     * @Description 非公开方法.
     */
    protected static <T extends EntityModel> Map<String, String> getColumnsMapForSQL(@NotNull Class<T> entityClazz) {
        Map<String, String> result = new LinkedHashMap<>(0);
        Field[] fields = entityClazz.getDeclaredFields();
        if (fields.length > 0) {
            for (Field field : fields) {
                String columnName = getColumn(field);
                if (null == columnName) continue;
                String columnValue = field.getName();
//                Object fieldValue = EntityUtil.invokeMethod(entity
//                        , field.getName()
//                        , null
//                        , null);
                result.put(columnName, (null != columnValue)
                        ? ("#{" + columnValue + "}")
                        : null);
            }
        }
        return result;
    }

    /**
     * 获取用于生成 SQL 的 {数据库表字段名 - Entity 属性名称SQL表示} 映射集合
     *
     * @param entityClazz
     * @param prefixSql
     * @param <T>
     * @return
     * @Description Entity 属性名称SQL表示中将会注入合法的SQL语句 <param>prefixSql</param> 作为前缀
     */
    protected static <T extends EntityModel> Map<String, String> getColumnsMapForSQL(@NotNull Class<T> entityClazz
            , @NotNull String prefixSql) {
        if (null == prefixSql
                || !SqlUtil.Regex.validateSQL(prefixSql)) {
            throw new RuntimeException("异常的 SQL 注入", new IllegalArgumentException());
        }
        Map<String, String> result = new LinkedHashMap<>(0);
        Field[] fields = entityClazz.getDeclaredFields();
        if (fields.length > 0) {
            for (Field field : fields) {
                String columnName = getColumn(field);
                if (null == columnName) continue;
                String columnValue = field.getName();
                result.put(columnName, (null != columnValue)
                        ? ("#{" + prefixSql + columnValue + "}")
                        : null);
            }
        }
        return result;
    }

    /**
     * 获取用于生成 SQL 的 {数据库表字段名 - Entity 属性名称SQL表示} 映射集合
     *
     * @param entity
     * @param <T>
     * @return [{<tt>数据库表字段名</tt>=<tt>Entity属性名称SQL表示</tt>}, ...]
     */
    public static <T extends EntityModel> Map<String, String> getColumnsMapForSQL(@NotNull T entity) {
        return getColumnsMapForSQL(entity.getClass());
    }

    /**
     * 获取用于生成 SQL 的 {数据库表字段名 - Entity 属性名称SQL表示} 映射集合
     *
     * @param entity
     * @param prefixSql
     * @param <T>
     * @return
     * @Description Entity 属性名称SQL表示中将会注入合法的SQL语句 <param>prefixSql</param> 作为前缀
     */
    public static <T extends EntityModel> Map<String, String> getColumnsMapForSQL(@NotNull T entity
            , @Nullable String prefixSql) {
        return getColumnsMapForSQL(entity.getClass(), prefixSql);
    }

    /**
     * 获取 <code>Field</code> 对象对应的数据库表字段名
     *
     * @param field
     * @param <T>
     * @return 可为 null, 此时 <param>field</param> 没有映射到数据库表字段
     */
    private static <T extends EntityModel> String getColumn(@NotNull Field field) {
        final String result;
        SuiteColumn fieldColumn = null;
        try {
            //注意: 可能为 null
            fieldColumn = field.getAnnotation(SuiteColumn.class);
        } catch (NullPointerException e) {
            // 此时 <param>field</param> 没有映射到数据库表字段
        } finally {
            if (null == fieldColumn) {
                return null;
            }
        }
        // 注解中的方法的缺省值和注入值都不能为 null (IDE编译验证), 可放心使用
        String columnValue = fieldColumn.value();
        String defaultColumnValue = AnnotationMethodDefaults.getInstance().suiteColumnValue;
        result = defaultColumnValue.equals(columnValue)
                ? JPARules.camelCaseNameToUnderlined(field.getName())
                : columnValue;
        return result;
    }

    /**
     * 获取 Entity 指定属性对应的数据库表字段名
     *
     * @param entityClazz -> Entity 的类对象
     * @param fieldName   -> Entity 的指定属性名称
     * @param <T>         -> Entity 类型
     * @return 可为 null, 此时 Entity类对象 的指定属性不存在, 或者没有映射到数据库表字段
     */
    public static <T extends EntityModel> String getColumn(@NotNull Class<T> entityClazz
            , @NotNull String fieldName) {
        try {
            return getColumn(entityClazz./*getField*/getDeclaredField(fieldName));
        } catch (NoSuchFieldException e) {
            //--- Entity类对象中没有匹配的字段
            System.err.println("Entity类对象中没有找到名称为【"
                    + (null != fieldName ? fieldName : "")
                    + "】且配置SQL映射的字段!");
            return null;
        }
    }

    /**
     * 获取 Entity 指定属性对应的SQL表示
     *
     * @param entityClazz -> Entity 的类对象
     * @param fieldName   -> Entity 的指定属性名称
     * @param <T>         -> Entity 类型
     * @return 可为 null, 此时 Entity对象 的指定属性不存在, 或者没有映射到数据库表字段
     */
    public static <T extends EntityModel> String getColumnValueForSQL(@NotNull Class<T> entityClazz
            , @NotNull String fieldName) {
        try {
            Field field = entityClazz.getDeclaredField(fieldName);
            boolean columnFlag = (null != getColumn(field));
            if (columnFlag) {
                return "#{" + field.getName() + "}";
            }
        } catch (NoSuchFieldException e) {
            //--- Entity类对象中没有匹配的字段
            System.err.println("Entity类对象中没有找到名称为【"
                    + (null != fieldName ? fieldName : "")
                    + "】且配置SQL映射的字段!");
        }
        return null;
    }

    /**
     * 获取数据库表名
     *
     * @param entityClazz -> Entity类对象
     * @param <T>
     * @return
     */
    public static <T extends EntityModel> String getTableName(@NotNull Class<T> entityClazz) {
        final String result;
        String tableValue = entityClazz.getAnnotation(SuiteTable.class).value();
        String defaultValue = AnnotationMethodDefaults.getInstance().suiteTableValue;
        result = defaultValue.equals(tableValue)
                ? JPARules.camelCaseNameToUnderlined(entityClazz.getSimpleName())
                : tableValue;
        return result;
    }

    public static String toValueForSQL(@NotNull String fieldName) {
        return EntityUtil.Regex.validateFieldName(fieldName)
                ? ("#{" + fieldName + "}")
                : " IS NULL ";
    }

    //===== non-static basic properties and methods =====//
    public final Class<E> entityClazz;

    protected JPAMapperModel(Class<E> entityClazz) {
        this.entityClazz = entityClazz;
    }

    public String getColumn(@NotNull String fieldName) {
        return getColumn(this.entityClazz, fieldName);
    }

    public String getColumnValueForSQL(@NotNull String fieldName) {
        return getColumnValueForSQL(this.entityClazz, fieldName);
    }

    public String[] getColumns() {
        return getColumns(this.entityClazz);
    }

    public Map<String, String> getColumnsMapForSQL() {
        return getColumnsMapForSQL(this.entityClazz);
    }

    public String getTableName() {
        return getTableName(this.entityClazz);
    }

}
