package github.com.suitelhy.dingding.sso.infrastructure.domain.util;

import github.com.suitelhy.dingding.sso.infrastructure.domain.model.VoModel;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

/**
 * VO 层 - 业务辅助工具
 *
 */
public final class VoUtil {

    @Nullable
    public static <VO extends Enum & VoModel<VO, V, _DESCRIPTION>, V extends Number, _DESCRIPTION>
    VO getVoByName(@NotNull Class<VO> voClass, @NotNull String name) {
        for (VO each : voClass.getEnumConstants()) {
            if (each.name().equals(name)) {
                return each;
            }
        }
        return null;
    }

    @Nullable
    public static <VO extends Enum & VoModel<VO, V, _DESCRIPTION>, V extends Number, _DESCRIPTION>
    VO getVoByValue(@NotNull Class<VO> voClass, @Nullable V value) {
        for (VO each : voClass.getEnumConstants()) {
            if (each.equalsValue(value)) {
                return each;
            }
        }
        return null;
    }

}
