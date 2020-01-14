package github.com.suitelhy.webchat.infrastructure.domain.util;

import github.com.suitelhy.webchat.infrastructure.domain.model.VoModel;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

/**
 * VO 层 - 业务辅助工具
 *
 */
public final class VoUtil {

    @Nullable
    public static <VO extends Enum & VoModel<VO, V>, V extends Number>
    VO getVoByValue(@NotNull Class<VO> voClass, @Nullable V value) {
        for (VO each : voClass.getEnumConstants()) {
            if (each.equalsValue(value)) {
                return each;
            }
        }
        return null;
    }

}
