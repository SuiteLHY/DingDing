package github.com.suitelhy.webchat.domain.vo;

import github.com.suitelhy.webchat.infrastructure.config.springdata.attribute.converter.VoAttributeConverter;
import github.com.suitelhy.webchat.infrastructure.domain.model.VoModel;
import org.springframework.lang.Nullable;

/**
 * 账户特性
 *
 */
public interface AccountVo<VO extends Enum, V extends Number>
        extends VoModel<VO, V> {

    /**
     * 账户 - 状态
     */
    enum Status implements AccountVo<Status, Integer> {
        DESTRUCTION(0, "注销", "该账户已经被注销, 逻辑上已被删除")
        , NORMAL(1, "正常", "该账户正常")
        , ABNORMAL(2, "异常", "该账户异常, 很可能出现了严重的问题, 应该被禁用");

        /**
         * 为持久化类型转换器提供支持
         */
        @javax.persistence.Converter(autoApply = true)
        public static class Converter
                extends VoAttributeConverter<Status, Integer> {

            public Converter() {
                super(Status.class);
            }

        }

        public final Integer code;

        public final String name;

        public final String description;

        Status(Integer code, String name, String description) {
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
         * 备注: <method>equals(Object)</method>
         * @param value
         * @return
         */
        public boolean equals(@Nullable Integer value) {
            return equalsValue(value);
        }

        @Override
        public String toString() {
            return name()
                    + "{code=" + this.code
                    + ", name=" + this.name
                    + ", description=" + this.description
                    + "}";
        }

    }

}
