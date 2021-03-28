package github.com.suitelhy.dingding.core.infrastructure.web.model;

import github.com.suitelhy.dingding.core.infrastructure.application.model.TaskResult;
import github.com.suitelhy.dingding.core.infrastructure.config.springdata.attribute.converter.VoAttributeConverter;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.VoModel;
import github.com.suitelhy.dingding.core.infrastructure.web.vo.HTTP;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * Web层 (用户界面层) -> 操作结果
 *
 * @Design {@link AbstractWebResult} 默认实现.
 * · Web API 的设计: {@link github.com.suitelhy.dingding.core.infrastructure.web}
 *
 * @param <_DATA> [操作结果 - 相关数据]的类型
 *
 * @see AbstractWebResult
 * @see HTTP.StatusVo
 */
public class WebResult<_DATA>
        extends AbstractWebResult<WebResult.Vo.StatusVo, _DATA, Map> {

    public interface Vo<VO extends Enum<VO> & VoModel<VO, V, _DESCRIPTION>, V extends Number, _DESCRIPTION>
            extends VoModel<VO, V, _DESCRIPTION> {

        /**
         * 操作结果 - 状态
         *
         * @Design {@link VoModel}
         *
         * @see HTTP.StatusVo
         */
        enum StatusVo
                implements VoModel<StatusVo, Integer, String> {
            /**
             * 此次操作失败
             *
             * @Description 泛用的说明类型.
             */
            FAILURE(0
                    , "此次操作失败"
                    , "此次操作失败。"),
            /**
             * 此次操作不符合业务要求
             *
             * @Description 详细的说明类型.
             */
            FAILURE_BUSINESS(1
                    , "此次操作不符合业务要求"
                    , "此次操作不符合业务要求。"),
            /**
             * 客户端输入不符合业务要求
             *
             * @Description 详细的说明类型.
             */
            FAILURE_INPUT_PARAMETER(2
                    , "客户端输入不符合要求"
                    , "客户端输入的数据不合法或不符合业务要求。"),
            /**
             * 此次操作成功
             *
             * @Description 泛用的说明类型.
             */
            SUCCESS(100
                    , "此次操作成功"
                    , "此次操作成功。泛用的说明类型。");

            /**
             * 为持久化类型转换器提供支持
             */
            @javax.persistence.Converter(autoApply = true)
            public static class Converter
                    extends VoAttributeConverter<StatusVo, Integer, String> {

                // (单例模式 - 登记式)
                private static class Factory {
                    private static final Converter SINGLETON = new Converter();
                }

                private Converter() {
                    super(StatusVo.class);
                }

                public static @NotNull Converter getInstance() {
                    return Factory.SINGLETON;
                }

            }

            /**
             * 状态 - 编码
             */
            public final @NotNull Integer code;

            /**
             * 状态 - 名称
             */
            public final @NotNull String name;

            /**
             * 状态 - 描述
             */
            public final @NotNull String description;

            /**
             * (Constructor)
             *
             * @param code        [状态 - 编码]
             * @param name        [状态 - 名称]
             * @param description [状态 - 描述]
             */
            StatusVo(@NotNull Integer code, @NotNull String name, @NotNull String description)
                    throws IllegalArgumentException
            {
                if (null == code) {
                    throw new IllegalArgumentException("非法参数: [状态 - 编码]");
                }
                if (null == name) {
                    throw new IllegalArgumentException("非法参数: [状态 - 名称]");
                }
                if (null == description) {
                    throw new IllegalArgumentException("非法参数: [状态 - 描述]");
                }

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
            public @NotNull String displayName() {
                return this.name;
            }

            /**
             * 等效比较
             *
             * @param value {@link Integer}
             *
             * @return 比较结果
             *
             * @see this#equals(Object)
             */
            public boolean equals(@Nullable Integer value) {
                return equalsValue(value);
            }

            @Override
            public @NotNull String toString() {
                return VoModel.toString(this);
            }

            /**
             * 提供类型转换器
             *
             * @Design 为持久化类型转换功能提供支持.
             *
             * @return {@link Converter}
             */
            @Override
            public @NotNull Converter voAttributeConverter() {
                return Converter.getInstance();
            }

        }

        /**
         * 操作结果 - 错误信息
         *
         * @Design {@link VoModel}
         *
         * @see HTTP.StatusVo
         */
        enum ErrorVo
                implements VoModel<ErrorVo, Integer, String> {
            /**
             * 未知错误
             *
             * @Description 泛用的说明类型.
             */
            UNKNOWN(0
                    , "未知错误"
                    , "未知类型"
                    , "未知的或未定义的错误。"),
            /**
             * 一般错误
             *
             * @Description 泛用的说明类型.
             */
            GENERAL_ERROR(1
                    , "一般错误"
                    , "通用类型"
                    , "通用类型的错误。"),
            /**
             * 客户端请求错误
             *
             * @Description 较为具体的说明类型.
             */
            BAD_REQUEST(400
                    , "客户端请求错误"
                    , "客户端错误"
                    , HTTP.StatusVo.BAD_REQUEST.description),
            /**
             * 服务端未知的错误
             *
             * @Description 泛用的说明类型.
             */
            INTERNAL_SERVER_ERROR(500
                    , "服务端未知的错误"
                    , "服务端错误"
                    , HTTP.StatusVo.INTERNAL_SERVER_ERROR.description);

            /**
             * 为持久化类型转换器提供支持
             */
            @javax.persistence.Converter(autoApply = true)
            public static class Converter
                    extends VoAttributeConverter<ErrorVo, Integer, String> {

                // (单例模式 - 登记式)
                private static class Factory {
                    private static final Converter SINGLETON = new Converter();
                }

                private Converter() {
                    super(ErrorVo.class);
                }

                @NotNull
                public static Converter getInstance() {
                    return Factory.SINGLETON;
                }

            }

            /**
             * 错误信息 - 编码
             */
            public final @NotNull Integer code;

            /**
             * 错误信息 - 名称
             */
            public final @NotNull String name;

            /**
             * 错误信息 - 类型
             */
            public final @NotNull String type;

            /**
             * 错误信息 - 描述
             */
            public final @NotNull String description;

            /**
             * (Constructor)
             *
             * @param code        [错误信息 - 编码]
             * @param name        [错误信息 - 名称]
             * @param type        [错误信息 - 类型]
             * @param description [错误信息 - 描述]
             */
            ErrorVo(@NotNull Integer code, @NotNull String name, @NotNull String type
                    , @NotNull String description)
                    throws IllegalArgumentException
            {
                if (null == code) {
                    throw new IllegalArgumentException("非法参数: [错误信息 - 编码]");
                }
                if (null == name) {
                    throw new IllegalArgumentException("非法参数: [错误信息 - 名称]");
                }
                if (null == type) {
                    throw new IllegalArgumentException("非法参数: [错误信息 - 类型]");
                }
                if (null == description) {
                    throw new IllegalArgumentException("非法参数: [错误信息 - 描述]");
                }

                this.code = code;
                this.name = name;
                this.type = type;
                this.description = description;
            }

            /**
             * VO 的值
             *
             * @return {@link Integer}
             */
            @Override
            public @NotNull Integer value() {
                return this.code;
            }

            /**
             * 详细信息
             *
             * @return {@link String}
             */
            @Override
            public @NotNull String description() {
                return this.description;
            }

            /**
             * (展示)名称
             *
             * @return {@link String}
             */
            @Override
            public @NotNull String displayName() {
                return this.name;
            }

            /**
             * 等效比较
             *
             * @param value {@link Integer}
             *
             * @return 比较结果
             *
             * @see this#equals(Object)
             */
            public boolean equals(@Nullable Integer value) {
                return equalsValue(value);
            }

            @Override
            public @NotNull String toString() {
                return VoModel.toString(this);
            }

            /**
             * 提供类型转换器
             *
             * @Design 为持久化类型转换功能提供支持.
             *
             * @return {@link Converter}
             */
            @Override
            public @NotNull Converter voAttributeConverter() {
                return Converter.getInstance();
            }

        }

        /**
         * 额外拓展 <- 属性
         *
         * @Description 相关属性配置定义.
         *
         * @Design {@link VoModel}
         */
        enum ExtraPropertyVo
                implements VoModel<ExtraPropertyVo, Integer, String> {
            /**
             * 异常信息
             *
             * @Description 此次操作中出现的异常的相关信息。
             */
            EXCEPTION(1
                    , "异常信息"
                    , "此次操作中出现的异常的相关信息。"
                    , "exception"
                    , String.class);

            /**
             * 为持久化类型转换器提供支持
             */
            @javax.persistence.Converter(autoApply = true)
            public static class Converter
                    extends VoAttributeConverter<ExtraPropertyVo, Integer, String> {

                // (单例模式 - 登记式)
                private static class Factory {
                    private static final @NotNull Converter SINGLETON = new Converter();
                }

                private Converter() {
                    super(ExtraPropertyVo.class);
                }

                public static @NotNull Converter getInstance() {
                    return Factory.SINGLETON;
                }

            }

            /**
             * 属性 - 编码
             */
            public final @NotNull Integer code;

            /**
             * 属性 - 名称
             */
            public final @NotNull String name;

            /**
             * 属性 - 描述
             */
            public final @NotNull String description;

            /**
             * 属性 -> Key 名称
             */
            public final @NotNull String key;

            /**
             * 属性 -> Value 类型
             */
            public final @NotNull Class valueClass;

            /**
             * (Constructor)
             *
             * @param code        [属性 - 编码]
             * @param name        [属性 - 名称]
             * @param description [属性 - 描述]
             * @param key         [属性 -> Key 名称]
             * @param valueClass  [属性 -> Value 类型]
             */
            ExtraPropertyVo(@NotNull Integer code, @NotNull String name, @NotNull String description
                    , @NotNull String key, @NotNull Class valueClass)
                    throws IllegalArgumentException
            {
                if (null == code) {
                    throw new IllegalArgumentException("非法参数: [状态 - 编码]");
                }
                if (null == name) {
                    throw new IllegalArgumentException("非法参数: [状态 - 名称]");
                }
                if (null == description) {
                    throw new IllegalArgumentException("非法参数: [状态 - 描述]");
                }
                if (null == key) {
                    throw new IllegalArgumentException("非法参数: [属性 -> Key 名称]");
                }
                if (null == valueClass) {
                    throw new IllegalArgumentException("非法参数: [属性 -> Value 类型]");
                }

                this.code = code;
                this.name = name;
                this.description = description;
                this.key = key;
                this.valueClass = valueClass;
            }

            /**
             * VO 的值
             *
             * @return {@link Integer}
             */
            @Override
            public Integer value() {
                return this.code;
            }

            /**
             * 详细信息
             *
             * @return {@link String}
             */
            @Override
            public @NotNull String description() {
                return this.description;
            }

            /**
             * (展示)名称
             *
             * @return {@link String}
             */
            @Override
            public @NotNull String displayName() {
                return this.name;
            }

            /**
             * 等效比较
             *
             * @param value {@link Integer}
             *
             * @return 比较结果
             *
             * @see this#equals(Object)
             */
            public boolean equals(@Nullable Integer value) {
                return equalsValue(value);
            }

            /**
             * 转换为字符串
             *
             * @return 转换结果
             */
            @Override
            public @NotNull String toString() {
                return VoModel.toString(this);
            }

            /**
             * 提供类型转换器
             *
             * @Design 为持久化类型转换功能提供支持.
             *
             * @return {@link Converter}
             */
            @Override
            public @NotNull Converter voAttributeConverter() {
                return Converter.getInstance();
            }

        }

    }

    /**
     * {@link WebResult} 的工厂类
     *
     * @Design {@link WebResultFactoryModel}
     */
    public enum Factory
            implements WebResultFactoryModel<WebResult<?>> {
        DEFAULT;

        /**
         * 创建
         *
         * @param taskResult {@link TaskResult}
         *
         * @return {@link WebResult}
         */
        public @NotNull <_DATA> WebResult<_DATA> create(@NotNull TaskResult<_DATA> taskResult) {
            final WebResult<_DATA> result;
            if (taskResult.isSuccess()) {
                //--- 预期内成功的情况
                result = new WebResult<>(Vo.StatusVo.SUCCESS
                        , null
                        , taskResult.data
                        , ""
                        , null
                        , null);
            } else if (taskResult.isFailure()) {
                //--- 预期内失败的情况
                switch (taskResult.status) {
                    case FAILURE:
                        result = new WebResult<>(Vo.StatusVo.FAILURE
                                , null
                                , taskResult.data
                                , ""
                                , Vo.ErrorVo.GENERAL_ERROR
                                , "");
                        break;
                    case FAILURE_BUSINESS:
                        result = new WebResult<>(Vo.StatusVo.FAILURE_BUSINESS
                                , null
                                , taskResult.data
                                , ""
                                , Vo.ErrorVo.GENERAL_ERROR
                                , "");
                        break;
                    case FAILURE_INPUT_PARAMETER:
                        result = new WebResult<>(Vo.StatusVo.FAILURE_INPUT_PARAMETER
                                , null
                                , taskResult.data
                                , ""
                                , Vo.ErrorVo.BAD_REQUEST
                                , "");
                        break;
                    default:
                        result = new WebResult<>(WebResult.Vo.StatusVo.FAILURE
                                , null
                                , taskResult.data
                                , ""
                                , Vo.ErrorVo.UNKNOWN
                                , "");
                }
            } else {
                //--- 预期外的情况
                result = new WebResult<>(WebResult.Vo.StatusVo.FAILURE
                        , null
                        , taskResult.data
                        , ""
                        , Vo.ErrorVo.UNKNOWN
                        , "");
            }

            return result;
        }

        /**
         * 创建
         *
         * @param statusVo  [操作结果 - 状态]
         * @param message   [操作结果 - 响应的消息]
         * @param data      [操作结果 - 相关数据]
         * @param requestId [请求 ID]
         * @param <_DATA>   {@link _DATA}
         *
         * @return {@link WebResult}
         */
        public @NotNull <_DATA> WebResult<_DATA> create(@NotNull WebResult.Vo.StatusVo statusVo
                , @Nullable String message
                , @NotNull _DATA data
                , @NotNull String requestId)
        {
            return new WebResult<>(statusVo, message, data
                    , requestId, null, null);
        }

        /**
         * 创建 (失败状态)
         *
         * @param statusVo     [操作结果 - 状态]
         * @param message      [操作结果 - 响应的消息]
         * @param data         [操作结果 - 相关数据]
         * @param requestId    [请求 ID]
         * @param errorVo      [操作结果 - 错误信息]
         * @param errorTraceId [操作结果 - 错误信息 <- 跟踪 ID]
         * @param <_DATA>      {@link _DATA}
         *
         * @return {@link WebResult}
         */
        public @NotNull <_DATA> WebResult<_DATA> createFailure(@NotNull Vo.StatusVo statusVo
                , @Nullable String message
                , @NotNull _DATA data
                , @NotNull String requestId
                , @NotNull Vo.ErrorVo errorVo
                , @NotNull String errorTraceId)
                throws IllegalArgumentException
        {
            if (null == errorVo) {
                throw new IllegalArgumentException("非法参数: [操作结果 - 错误信息]!");
            }
            if (null == errorTraceId) {
                throw new IllegalArgumentException("非法参数: [操作结果 - 错误信息 <- 跟踪 ID]!");
            }

            return new WebResult<>(statusVo, message, data
                    , requestId, errorVo, errorTraceId);
        }

        /**
         * 创建 (预期外状态)
         *
         * @param message      [操作结果 - 响应的消息]
         * @param data         [操作结果 - 相关数据]
         * @param requestId    [请求 ID]
         * @param errorTraceId [操作结果 - 错误信息 <- 跟踪 ID]
         * @param <_DATA>      {@link _DATA}
         *
         * @return {@link WebResult}
         */
        @NotNull
        public <_DATA> WebResult<_DATA> createUnknown(@Nullable String message
                , @NotNull _DATA data
                , @NotNull String requestId
                , @NotNull String errorTraceId)
                throws IllegalArgumentException
        {
            if (null == errorTraceId) {
                throw new IllegalArgumentException("非法参数: [操作结果 - 错误信息 <- 跟踪 ID]!");
            }

            return new WebResult<>(Vo.StatusVo.FAILURE, message, data
                    , requestId, Vo.ErrorVo.UNKNOWN, errorTraceId);
        }

        /**
         * 获取缺省对象
         *
         * @return 非 {@literal null}.
         */
        @Override
        public @NotNull WebResult<?> createDefault() {
            return new WebResult<>(Vo.StatusVo.FAILURE
                    , null
                    , new Object()
                    , ""
                    , Vo.ErrorVo.INTERNAL_SERVER_ERROR
                    , "");
        }

    }

    /**
     * 操作结果 - 相关数据
     *
     * @Description {@link super#data}
     */
    public final @Nullable _DATA data = super.data;

    /**
     * 操作结果 - 响应的消息
     */
    public final @NotNull String message;

//    /**
//     * 额外拓展
//     *
//     * @Description {@link super#extra}
//     */
//    @NotNull
//    public final Map<String, Object> extra = super.extra;

    /**
     * 错误信息集合
     *
     * @Description 在 {@link this#isSuccess()} 的结果不为 {@literal true} 的情况下才使用.
     */
    protected final @NotNull List<Map<String, Object>> errors = new ArrayList<>(0);

    /**
     * (Constructor)
     *
     * @param statusVo     [操作结果 - 状态]
     * @param message      [操作结果 - 响应的消息]
     * @param data         [操作结果 - 相关数据]
     * @param requestId    [请求 ID]
     * @param errorVo      [操作结果 - 错误信息]
     * @param errorTraceId [操作结果 - 错误信息 <- 跟踪 ID]
     */
    protected WebResult(@NotNull WebResult.Vo.StatusVo statusVo, @Nullable String message, @NotNull _DATA data
            , @NotNull String requestId, @Nullable Vo.ErrorVo errorVo, @Nullable String errorTraceId) {
        super(statusVo, data, new HashMap<String, Object>(1) {
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
                        Iterator<Entry<String, Object>> i = entrySet().iterator();
                        if (! i.hasNext()) {
                            return "{}";
                        }

                        StringBuilder sb = new StringBuilder();
                        sb.append('{');
                        for (; ; ) {
                            Entry<String, Object> e = i.next();
                            String key = e.getKey();
                            Object value = e.getValue();
                            sb.append('\"').append(key).append('\"');
                            sb.append(':');
                            sb.append(value instanceof String ? '\"' : "").append(value).append(value instanceof String ? '\"' : "");
                            if (! i.hasNext()) {
                                return sb.append('}').toString();
                            }
                            sb.append(',').append(' ');
                        }
                    }
                }
                , requestId);

        this.message = (null == message) ? statusVo.name : message;

        if (! this.isSuccess()) {
            final @NotNull Map<String, Object> error = new HashMap<String, Object>(5) {
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
                    Iterator<Entry<String, Object>> i = entrySet().iterator();
                    if (!i.hasNext()) {
                        return "{}";
                    }

                    StringBuilder sb = new StringBuilder();
                    sb.append('{');
                    for (; ; ) {
                        Entry<String, Object> e = i.next();
                        String key = e.getKey();
                        Object value = e.getValue();
                        sb.append('\"').append(key).append('\"');
                        sb.append(':');
                        sb.append(value instanceof String ? '\"' : "").append(value).append(value instanceof String ? '\"' : "");
                        if (! i.hasNext()) {
                            return sb.append('}').toString();
                        }
                        sb.append(',').append(' ');
                    }
                }
            };

            error.put("code", (null != errorVo) ? errorVo.code : Vo.ErrorVo.UNKNOWN.code);
            error.put("name", (null != errorVo) ? errorVo.name : Vo.ErrorVo.UNKNOWN.name);
            error.put("type", (null != errorVo) ? errorVo.type : Vo.ErrorVo.UNKNOWN.type);
            error.put("description", (null != errorVo) ? errorVo.description : Vo.ErrorVo.UNKNOWN.description);
            error.put("trace_id", (null != errorTraceId) ? errorTraceId : "");

            this.errors.add(error);
        }

    }

    /**
     * 判断是否为[预期内失败的情况]
     *
     * @Description · 预期内不成功的情况, 此时程序有对应的处理方案.
     * · 【补充说明】该方法的返回值为 {@literal true} 的情况下, 代表着当前执行结果({@link AbstractWebResult}的实现)属于[预期内失败的情况];
     * 同时 {@link this#isSuccess()} 的返回值必定为 {@literal false}.
     * · 【补充说明】该方法的返回值为 {@code false} 的情况下, 代表着当前执行结果({@link AbstractWebResult}的实现)不属于[预期内失败的情况];
     * 同时 {@link this#isSuccess()} 的返回值不受影响 (可能为 {@literal true} 也可能为 {@literal false}).
     *
     * @return 判断结果
     *
     * @see this#isSuccess()
     */
    @Override
    boolean isFailure() {
        if (isSuccess()) {
            return false;
        }

        switch (this.status) {
            case FAILURE_BUSINESS:
                return true;
        }

        return false;
    }

    /**
     * 判断是否为[预期内成功的情况]
     *
     * @Description · 不要求所有关联的业务都能及时响应[成功的状态], 但一定要保证[当前所执行的业务操作]在预期范围内且[能够保证在预期时间内达到预期成功的结果].
     * · 【补充说明】该方法的返回值为 {@literal true} 的情况下, 代表着当前执行结果({@link AbstractWebResult}的实现)属于[预期内成功的情况];
     * 同时 {@link this#isFailure()} 的返回值必定为 {@literal false}.
     * · 【补充说明】该方法的返回值为 {@literal false} 的情况下, 代表着当前执行结果({@link AbstractWebResult}的实现)不属于[预期内成功的情况];
     * 同时 {@link this#isFailure()} 的返回值不受影响 (可能为 {@literal true} 也可能为 {@literal false}).
     *
     * @return 判断结果
     */
    @Override
    boolean isSuccess() {
        switch (this.status) {
            case SUCCESS:
                return true;
        }

        return false;
    }

    /**
     * 转换为 JSON 格式的字符串
     *
     * @return 当前对象的 JSON 格式的字符串表示
     */
    public @NotNull
    String toJSONString() {
        StringBuilder resultSB = new StringBuilder()
                .append('{')
                .append("\"requestInfo\":").append(this.requestInfo)
                .append(", \"status\":\"").append(this.status.voAttributeConverter().convertToDatabaseColumn(this.status)).append('\"')
                .append(", \"message\":\"").append(this.message).append("\"");

        if (this.isSuccess()) {
            resultSB.append(", \"data\":").append(this.data instanceof String ? '\"' : "").append(this.data).append(this.data instanceof String ? '\"' : "");
        } else {
            resultSB.append(", \"errors\":").append(this.errors);
        }

        if (! this.extra.isEmpty()) {
            resultSB.append(", \"extra\":\"").append(this.extra).append('\"');
        }

        resultSB.append('}');
        return resultSB.toString();
    }

    /**
     * 转换为字符串
     *
     * @return 当前对象的字符串表示
     *
     * @see this#toJSONString()
     */
    @Override
    public @NotNull String toString() {
        return this.toJSONString();
    }

}
