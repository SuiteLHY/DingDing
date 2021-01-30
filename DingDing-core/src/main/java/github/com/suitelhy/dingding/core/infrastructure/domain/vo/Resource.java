package github.com.suitelhy.dingding.core.infrastructure.domain.vo;

import github.com.suitelhy.dingding.core.infrastructure.config.springdata.attribute.converter.VoAttributeConverter;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.VoModel;

import javax.validation.constraints.NotNull;

/**
 * VO - 资源属性
 */
public interface Resource<VO extends Enum<VO> & VoModel<VO, V, _DESCRIPTION>, V extends Number, _DESCRIPTION>
        extends VoModel<VO, V, _DESCRIPTION> {

    /**
     * 资源类型
     *
     * @Description 例如: 菜单, 按钮.
     */
    enum TypeVo
            implements VoModel<TypeVo, Integer, String> {
        BASE_INFO(0, "基础信息"), MENU(1, "菜单"), BUTTON(2, "按钮");

        /**
         * 为持久化类型转换器提供支持
         */
        @javax.persistence.Converter(autoApply = true)
        public static class Converter
                extends VoAttributeConverter<TypeVo, /*Byte*/Integer, String> {

            /**
             * @Design (单例模式 - 登记式)
             */
            private static class Factory {
                private static final Converter SINGLETON = new Converter();
            }

            private Converter() {
                super(TypeVo.class);
            }

            @NotNull
            public static Converter getInstance() {
                return Factory.SINGLETON;
            }

        }

        public final Integer code;

        public final @NotNull
        String name;

        TypeVo(Integer code, @NotNull String name) {
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
        public @NotNull
        String toString() {
            return VoModel.toString(this);
        }

        /**
         * 提供类型转换器
         *
         * @Design 为持久化类型转换功能提供支持.
         */
        @Override
        @SuppressWarnings("unchecked")
        public @NotNull
        Converter voAttributeConverter() {
            return Converter.getInstance();
        }

    }

}
