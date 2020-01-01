package github.com.suitelhy.webchat.infrastructure.domain.annotation;

/**
 * 注解属性缺省值类（单例）
 */
public final class AnnotationMethodDefaults {

    private static class DefaultsFactory {
        public static final AnnotationMethodDefaults INSTANCE = new AnnotationMethodDefaults();
    }

    public static AnnotationMethodDefaults getInstance() {
        return DefaultsFactory.INSTANCE;
    }

    public final String suiteColumnValue;

    public final String suiteTableValue;

    private AnnotationMethodDefaults() {
        try {
            this.suiteColumnValue = (String) SuiteColumn.class.getMethod("value").getDefaultValue();
            this.suiteTableValue = (String) SuiteTable.class.getMethod("value").getDefaultValue();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("注解的指定方法不存在, 装配方法缺省值出错!", e);
        }
    }

}