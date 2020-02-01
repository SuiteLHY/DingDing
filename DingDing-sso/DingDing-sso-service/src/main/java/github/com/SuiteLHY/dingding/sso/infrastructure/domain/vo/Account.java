package github.com.suitelhy.dingding.sso.infrastructure.domain.vo;

import github.com.suitelhy.dingding.sso.infrastructure.config.springdata.attribute.converter.VoAttributeConverter;
import github.com.suitelhy.dingding.sso.infrastructure.domain.model.VoModel;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

/**
 * 账户特性
 *
 */
public interface Account<VO extends Enum & VoModel<VO, V, _DESCRIPTION>, V extends Number, _DESCRIPTION>
        extends VoModel<VO, V, _DESCRIPTION> {

    /**
     * 账户 - 状态
     */
    enum StatusVo implements Account<StatusVo, Integer, String> {
        DESTRUCTION(0, "注销", "该账户已经被注销, 业务逻辑上已被删除")
        , NORMAL(1, "正常", "该账户正常")
        , ABNORMAL(2, "异常", "该账户异常, 很可能出现了严重的问题, 应该被禁用");

        /**
         * 为持久化类型转换器提供支持
         */
        @javax.persistence.Converter(autoApply = true)
        public static class Converter
                extends VoAttributeConverter<StatusVo, Integer, String> {

            public Converter() {
                super(StatusVo.class);
            }

        }

        public final Integer code;

        @NotNull
        public final String name;

        public final String description;

        StatusVo(Integer code, String name, String description) {
            this.code = code;
            this.name = name;
            this.description = description;
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
         * 详细信息
         *
         * @return
         */
        @Override
        public String description() {
            return this.description;
        }

        /**
         * (展示)名称
         *
         * @return
         */
        @Override
        public String showName() {
            return this.name;
        }

        /**
         * 备注: <method>equals(Object)</method>
         * @param value
         * @return
         */
        public boolean equals(@Nullable Integer value) {
            return equalsValue(value);
        }

        @Override
        public String toString() {
            return VoModel.toString(this);
        }

    }

}
