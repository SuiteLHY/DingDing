package github.com.suitelhy.dingding.core.infrastructure.model;

import github.com.suitelhy.dingding.core.infrastructure.domain.model.VoModel;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

/**
 * 操作结果
 *
 * @Description 通用模型.
 *
 * @param <_STATUS>  {@link VoModel}
 * @param <_DATA>
 * @param <_EXTRA>
 */
public abstract class AbstractResult<_STATUS extends VoModel, _DATA, _EXTRA> {

    /**
     * 操作结果 - 状态
     *
     * @Design VO
     */
    public final @NotNull _STATUS status;

    /**
     * 操作结果 - 相关数据
     *
     * @Design 不可为 {@code null}.
     * · 【设计原则】若没有合适的缺省值, 则应该选择合适的 {@link java.util.Collection} 实现类型作为 {@link _DATA}.
     */
    /*public*/protected final @NotNull _DATA data;

    /**
     * 额外拓展
     */
    /*public*/protected final @NotNull _EXTRA extra;

    /**
     * (Constructor)
     *
     * @param status    [操作结果 - 状态]
     * @param data      [操作结果 - 相关数据]
     * @param extra     [额外拓展]
     */
    protected AbstractResult(@NotNull _STATUS status, @NotNull _DATA data, @NotNull _EXTRA extra)
            throws IllegalArgumentException
    {
        if (null == status) {
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "[操作结果 - 状态]"
                    , status
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == data) {
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "[操作结果 - 相关数据]"
                    , data
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == extra) {
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "[额外拓展]"
                    , extra
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        this.status = status;
        this.data = data;
        this.extra = extra;
    }

    public @NotNull String toJSONString() {
        return String.format("{status:%s,data:%s,extra:%s}"
                , status
                , new StringBuilder(data instanceof String ? "\"" : "").append(data).append(data instanceof String ? '\"' : "")
                , new StringBuilder(extra instanceof String ? "\"" : "").append(extra).append(extra instanceof String ? '\"' : ""));
    }

    @Override
    public @NotNull String toString() {
        return String.format("%s{status=%s, data=%s, extra=%s}"
                , getClass().getSimpleName()
                , status
                , data
                , extra);
    }

}
