package github.com.suitelhy.dingding.app.infrastructure.domain.annotation;

import java.lang.annotation.*;

/**
 * 注解: 数据库表字段名称
 */
// （可选的）类和方法的元注解在缺省情况下是不出现在 javadoc 中的。如果
//-> 使用 @Documented 修饰所定义的注解，则表示它可以出现在 javadoc 中。
@Documented
// 声明所定义的注解作用于字段（包括枚举常量）
// 定义注解时，@Target 可有可无。若有 @Target，则所定义的注解只能用于
//-> 它所指定的地方；若没有 @Target，则该注解可以用于任何地方。
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)// 指定元注解的类型属性
public @interface SuiteColumn {

    /**
     * 定义 - Entity 属性映射字段名称
     * @Description 缺省值为空字符串，此时使用 POJO 字段名，根据驼峰命名规范
     *-> 映射下划线的规则推测出对应的数据库表字段名称。
     * @return Entity 属性映射字段名称
     */
    String value() default "";

    /**
     * 描述 - Entity 属性映射字段是否可为空值
     * @Description 缺省值为 true, 此时 Entity 属性映射字段可为空.
     * @return
     */
    boolean nullable() default true;

}
