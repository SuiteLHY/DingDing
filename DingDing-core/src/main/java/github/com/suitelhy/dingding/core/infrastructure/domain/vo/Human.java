package github.com.suitelhy.dingding.core.infrastructure.domain.vo;

import github.com.suitelhy.dingding.core.infrastructure.config.springdata.attribute.converter.VoAttributeConverter;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.VoModel;

import javax.validation.constraints.NotNull;

/**
 * VO - 人类特性
 */
public interface Human<VO extends Enum<VO> & VoModel<VO, V, _DESCRIPTION>, V extends Number, _DESCRIPTION>
        extends VoModel<VO, V, _DESCRIPTION> {

    /**
     * 性别
     *
     * @Description Hibernate 的默认策略仅支持数据库的数字类型映射到 java 的 Integer, 而不包括 Byte 和 Short.
     * ->    org.hibernate.HibernateException: Unknown wrap conversion requested: [B to java.lang.Byte
     * @Reference <a href="https://stackoverflow.com/questions/26347443/attributeconverter-fails-after-migration-from-glassfish-4-to-wildfly-8-1">
     * ->    AttributeConverter fails after migration from glassfish 4 to wildfly 8.1</a>
     */
    enum SexVo
            implements VoModel<SexVo, Integer, String> {
        UNKNOWN(null, "未知"),
        FEMALE(0, "女"),
        MALE(1, "男");

        /**
         * 为持久化类型转换器提供支持
         */
        @javax.persistence.Converter(autoApply = true)
        public static class Converter
                extends VoAttributeConverter<SexVo, /*Byte*/Integer, String> {

            /**
             * @Design (单例模式 - 登记式)
             */
            private static class Factory {
                private static final Converter SINGLETON = new Converter();
            }

            private Converter() {
                super(SexVo.class);
            }

            @NotNull
            public static Converter getInstance() {
                return Factory.SINGLETON;
            }

        }

        public final Integer code;

        public final String name;

        SexVo(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        /**
         * VO 的值
         *
         * @return
         */
        @Override
        public Integer value() {
            return this.code;
        }

        /**
         * VO 的 (展示)名称
         *
         * @return
         */
        @Override
        public @NotNull
        String displayName() {
            return this.name;
        }

        @Override
        public String toString() {
            return VoModel.toString(this);
        }

        /**
         * 提供类型转换器
         *
         * @Design 为持久化类型转换功能提供支持.
         */
        @Override
        @NotNull
        @SuppressWarnings("unchecked")
        public Converter voAttributeConverter() {
            return Converter.getInstance();
        }

    }

}
