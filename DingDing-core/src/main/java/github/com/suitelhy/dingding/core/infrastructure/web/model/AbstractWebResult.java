package github.com.suitelhy.dingding.core.infrastructure.web.model;

import github.com.suitelhy.dingding.core.infrastructure.domain.model.VoModel;
import github.com.suitelhy.dingding.core.infrastructure.model.AbstractResult;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 操作结果
 *
 * @param <_STATUS> {@link VoModel}
 * @param <_DATA>
 * @param <_EXTRA>
 * @Description Web API 对应的通用模型.
 * @Design Web层 (用户界面层) 的 {@link AbstractResult} 实现.
 * @see AbstractResult
 */
public abstract class AbstractWebResult<_STATUS extends VoModel, _DATA, _EXTRA>
        extends AbstractResult<_STATUS, _DATA, _EXTRA> {

    /**
     * 请求的相关信息
     *
     * @Design · 数据结构:
     * {
     * id: [请求 ID]
     * }
     */
    protected final @NotNull
    Map<String, String> requestInfo = new HashMap<String, String>(1) {
        /**
         * 转换为字符串
         *
         * @return 转换结果
         *
         * @see super#toString()
         */
        @Override
        public @NotNull
        String toString() {
            Iterator<Entry<String, String>> i = entrySet().iterator();
            if (!i.hasNext()) {
                return "{}";
            }

            StringBuilder sb = new StringBuilder();
            sb.append('{');
            for (; ; ) {
                Entry<String, String> e = i.next();
                String key = e.getKey();
                String value = e.getValue();
                sb.append('\"').append(key).append('\"');
                sb.append(':');
                sb.append('\"').append(value).append('\"');
                if (!i.hasNext()) {
                    return sb.append('}').toString();
                }
                sb.append(',').append(' ');
            }
        }
    };

    /**
     * (Constructor)
     *
     * @param status    [操作结果 - 状态]
     * @param data      [操作结果 - 相关数据]
     * @param extra     [额外拓展]
     * @param requestId [请求 ID]
     */
    protected AbstractWebResult(@NotNull _STATUS status, @NotNull _DATA data, @NotNull _EXTRA extra
            , @NotNull String requestId)
            throws IllegalArgumentException {
        super(status, data, extra);

        if (null == requestId) {
            throw new IllegalArgumentException("非法参数: [请求 ID]");
        }

        this.requestInfo.put("id", requestId);
    }

    /**
     * 判断是否为[预期内失败的情况]
     *
     * @return 判断结果
     * @Description · 预期内不成功的情况, 此时程序有对应的处理方案.
     * · 【补充说明】该方法的返回值为 {@code true} 的情况下, 代表着当前执行结果({@link AbstractWebResult}的实现)属于[预期内失败的情况];
     * 同时 {@link this#isSuccess()} 的返回值必定为 {@code false}.
     * · 【补充说明】该方法的返回值为 {@code false} 的情况下, 代表着当前执行结果({@link AbstractWebResult}的实现)不属于[预期内失败的情况];
     * 同时 {@link this#isSuccess()} 的返回值不受影响 (可能为 {@code true} 也可能为 {@code false}).
     * @see this#isSuccess()
     */
    abstract boolean isFailure();

    /**
     * 判断是否为[预期内成功的情况]
     *
     * @return 判断结果
     * @Description · 不要求所有关联的业务都能及时响应[成功的状态], 但一定要保证[当前所执行的业务操作]在预期范围内且[能够保证在预期时间内达到预期成功的结果].
     * · 【补充说明】该方法的返回值为 {@code true} 的情况下, 代表着当前执行结果({@link AbstractWebResult}的实现)属于[预期内成功的情况];
     * 同时 {@link this#isFailure()} 的返回值必定为 {@code false}.
     * · 【补充说明】该方法的返回值为 {@code false} 的情况下, 代表着当前执行结果({@link AbstractWebResult}的实现)不属于[预期内成功的情况];
     * 同时 {@link this#isFailure()} 的返回值不受影响 (可能为 {@code true} 也可能为 {@code false}).
     */
    abstract boolean isSuccess();

    /**
     * 转换为字符串
     *
     * @return 当前对象的字符串表示
     */
    @Override
    abstract public @NotNull
    String toString();

}
