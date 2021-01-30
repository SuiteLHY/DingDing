package github.com.suitelhy.dingding.core.infrastructure.domain.util;

import github.com.suitelhy.dingding.core.infrastructure.domain.model.VoModel;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

/**
 * VO 层 - 业务辅助工具
 */
public final class VoUtil {

    @Nullable
    public /*static*/ <VO extends Enum<VO> & VoModel<VO, V, _DESCRIPTION>, V extends Number, _DESCRIPTION>
    VO getVoByName(@NotNull Class<VO> voClass, @NotNull String name) {
        for (VO each : voClass.getEnumConstants()) {
            if (each.name().equals(name)) {
                return each;
            }
        }
        return null;
    }

    @Nullable
    public /*static*/ <VO extends Enum<VO> & VoModel<VO, V, _DESCRIPTION>, V extends Number, _DESCRIPTION>
    VO getVoByValue(@NotNull Class<VO> voClass, @Nullable V value) {
        for (VO each : voClass.getEnumConstants()) {
            if (each.equalsValue(value)) {
                return each;
            }
        }
        return null;
    }

    /**
     * @Design (单例模式 - 登记式)
     */
    private static class Factory {
        private static final VoUtil SINGLETON = new VoUtil();
    }

    private VoUtil() {
    }

    @NotNull
    public static VoUtil getInstance() {
        return VoUtil.Factory.SINGLETON;
    }

}
