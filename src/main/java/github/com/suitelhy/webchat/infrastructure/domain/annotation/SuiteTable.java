package github.com.suitelhy.webchat.infrastructure.domain.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SuiteTable {

    /**
     * 数据库表名
     * @return
     */
    String value() default "";

}
