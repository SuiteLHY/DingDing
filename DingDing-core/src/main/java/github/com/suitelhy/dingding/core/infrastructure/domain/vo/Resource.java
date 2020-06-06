package github.com.suitelhy.dingding.core.infrastructure.domain.vo;

import github.com.suitelhy.dingding.core.infrastructure.config.springdata.attribute.converter.VoAttributeConverter;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.VoModel;

import javax.validation.constraints.NotNull;

/**
 * VO - 资源属性
 */
public interface Resource<VO extends Enum & VoModel<VO, V, _DESCRIPTION>, V extends Number, _DESCRIPTION>
        extends VoModel<VO, V, _DESCRIPTION> {

    /**
     * 资源类型
     *
     * @Description 例如: 菜单, 按钮.
     */
    enum TypeVo
            implements VoModel<TypeVo, Integer, String> {
        MENU(1, "菜单")
        , BUTTON(2, "按钮");

        /**
         * 为持久化类型转换器提供支持
         */
        @javax.persistence.Converter(autoApply = true)
        public static class Converter
                extends VoAttributeConverter<TypeVo, /*Byte*/Integer, String> {

            public Converter() {
                super(TypeVo.class);
            }

        }

        public final Integer code;

        public final String name;

        TypeVo(Integer code, String name) {
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
        public @NotNull String displayName() {
            return this.name;
        }

        @Override
        public String toString() {
            return VoModel.toString(this);
        }

    }

}
