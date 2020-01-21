package github.com.suitelhy.dingding.infrastructure.domain.vo;

import github.com.suitelhy.dingding.infrastructure.config.springdata.attribute.converter.VoAttributeConverter;
import github.com.suitelhy.dingding.infrastructure.domain.model.VoModel;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

/**
 * 用户附件 -> VO
 * @param <VO>
 * @param <V>
 * @param <_DESCRIPTION>
 */
public interface UserAttachVo<VO extends Enum & VoModel<VO, V, _DESCRIPTION>, V extends Number, _DESCRIPTION>
        extends VoModel<VO, V, _DESCRIPTION> {

    enum AttachType implements UserAttachVo<AttachType, Integer, String> {
        FACE_IMAGE(1, "头像", "用户头像")
        , QR_CODE(2, "二维码", "用户二维码");

        public final int code;

        public final String name;

        private final String description;

        AttachType(@NotNull int code, @NotNull String name, @Nullable String description) {
            this.code = code;
            this.name = name;
            this.description = description;
        }

        /**
         * 为持久化类型转换器提供支持
         */
        @javax.persistence.Converter(autoApply = true)
        public static class Converter
                extends VoAttributeConverter<AttachType, Integer, String> {

            public Converter() {
                super(AttachType.class);
            }

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
        public @NotNull String showName() {
            return this.name;
        }

    }

}
