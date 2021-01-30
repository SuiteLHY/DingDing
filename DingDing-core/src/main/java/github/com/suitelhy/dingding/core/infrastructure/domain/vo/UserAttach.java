package github.com.suitelhy.dingding.core.infrastructure.domain.vo;

import github.com.suitelhy.dingding.core.infrastructure.config.springdata.attribute.converter.VoAttributeConverter;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.VoModel;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

/**
 * 用户附件 -> VO
 *
 * @param <VO>
 * @param <V>
 * @param <_DESCRIPTION>
 */
public interface UserAttach<VO extends Enum<VO> & VoModel<VO, V, _DESCRIPTION>, V extends Number, _DESCRIPTION>
        extends VoModel<VO, V, _DESCRIPTION> {

    /**
     * 附件类型
     */
    enum AttachTypeVo
            implements VoModel<AttachTypeVo, Integer, String> {
        FACE_IMAGE(1
                , "头像"
                , "用户头像图片。"), QR_CODE(2
                , "二维码"
                , "用户二维码。");

        /**
         * 为持久化类型转换器提供支持
         */
        @javax.persistence.Converter(autoApply = true)
        public static class Converter
                extends VoAttributeConverter<AttachTypeVo, Integer, String> {

            /**
             * @Design (单例模式 - 登记式)
             */
            private static class Factory {
                private static final Converter SINGLETON = new Converter();
            }

            private Converter() {
                super(AttachTypeVo.class);
            }

            @NotNull
            public static Converter getInstance() {
                return Factory.SINGLETON;
            }

        }

        public final int code;

        public final String name;

        private final String description;

        AttachTypeVo(@NotNull int code, @NotNull String name, @Nullable String description) {
            this.code = code;
            this.name = name;
            this.description = description;
        }

        /**
         * Returns the name of this enum constant, as contained in the
         * declaration.  This method may be overridden, though it typically
         * isn't necessary or desirable.  An enum type should override this
         * method when a more "programmer-friendly" string form exists.
         *
         * @return the name of this enum constant
         */
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

        /**
         * VO 的值
         *
         * @return 可为 <code>null</code>
         * @Description Unique attribute.
         */
        @Override
        public Integer value() {
            return this.code;
        }

        /**
         * VO 的详细信息
         *
         * @return
         */
        @Nullable
        @Override
        public String description() {
            return this.description;
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

    }

}
