package github.com.suitelhy.dingding.core.infrastructure.domain.vo;

import github.com.suitelhy.dingding.core.infrastructure.config.springdata.attribute.converter.VoAttributeConverter;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.VoModel;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * 账户特性
 */
public interface Account<VO extends Enum<VO> & VoModel<VO, V, _DESCRIPTION>, V extends Number, _DESCRIPTION>
        extends VoModel<VO, V, _DESCRIPTION> {

    /**
     * 账户 - 状态
     */
    enum StatusVo
            implements VoModel<StatusVo, Integer, String> {
        DESTRUCTION(0
                , "注销"
                , "该账户已经被注销，业务逻辑上已被删除。"), NORMAL(1
                , "正常"
                , "该账户正常。"), ABNORMAL(2
                , "异常"
                , "该账户异常，很可能出现了严重的问题，被禁用于正常状态才被允许的业务操作。"), LOCKED(3
                , "锁定"
                , "该账户已被锁定，被禁用于所有业务操作，且该状态只能由具有足够权限的管理员账户设置和解除。");

        /**
         * 为持久化类型转换器提供支持
         */
        @javax.persistence.Converter(autoApply = true)
        public static class Converter
                extends VoAttributeConverter<StatusVo, Integer, String> {

            /**
             * @Design (单例模式 - 登记式)
             */
            private static class Factory {
                private static final Converter SINGLETON = new Converter();
            }

            private Converter() {
                super(StatusVo.class);
            }

            @NotNull
            public static Converter getInstance() {
                return Factory.SINGLETON;
            }

        }

        public final Integer code;

        @NotNull
        public final String name;

        public final String description;

        /**
         * 数据[可用]凭证
         *
         * @Description 用于判断[账户 - 状态]是否属于[账户可正常使用].
         */
        @NotNull
        public static final Set<StatusVo> DATA_AVAILABILITY_CERTIFICATE;

        static {
            DATA_AVAILABILITY_CERTIFICATE = new HashSet<>(1);
            DATA_AVAILABILITY_CERTIFICATE.add(NORMAL);
        }

        /**
         * 数据[存在]凭证
         *
         * @Description 用于判断[账户 - 状态]是否属于[账户存在].
         */
        @NotNull
        public static final Set<StatusVo> DATA_EXISTENCE_CERTIFICATE;

        static {
            DATA_EXISTENCE_CERTIFICATE = new HashSet<>(3);
            DATA_EXISTENCE_CERTIFICATE.add(NORMAL);
            DATA_EXISTENCE_CERTIFICATE.add(ABNORMAL);
            DATA_EXISTENCE_CERTIFICATE.add(LOCKED);
        }

        /**
         * 数据[不可用]凭证
         *
         * @Description 用于判断[账户 - 状态]是否属于[账户存在且不可正常使用].
         */
        @NotNull
        public static final Set<StatusVo> DATA_UNAVAILABILITY_CERTIFICATE;

        static {
            DATA_UNAVAILABILITY_CERTIFICATE = new HashSet<>(2);
            DATA_UNAVAILABILITY_CERTIFICATE.add(ABNORMAL);
            DATA_UNAVAILABILITY_CERTIFICATE.add(LOCKED);
        }

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
        public String displayName() {
            return this.name;
        }

        /**
         * 备注: <method>equals(Object)</method>
         *
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

        /**
         * 提供类型转换器
         *
         * @Design 为持久化类型转换功能提供支持.
         */
        @NotNull
        @Override
        @SuppressWarnings("unchecked")
        public Converter voAttributeConverter() {
            return Converter.getInstance();
        }

    }

}
