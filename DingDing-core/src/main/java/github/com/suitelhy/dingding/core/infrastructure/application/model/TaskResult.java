package github.com.suitelhy.dingding.core.infrastructure.application.model;

import github.com.suitelhy.dingding.core.infrastructure.config.springdata.attribute.converter.VoAttributeConverter;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.VoModel;
import github.com.suitelhy.dingding.core.infrastructure.model.AbstractResult;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * 任务层 (Task) -> 操作结果
 *
 * @param <_DATA> [操作结果 - 相关数据]的类型
 * @Design 任务层 (Task) 的 {@link AbstractResult} 默认实现类.
 * @see AbstractResult
 * @see github.com.suitelhy.dingding.core.application.task
 */
public class TaskResult<_DATA>
        extends AbstractResult<TaskResult.Vo.StatusVo, _DATA, Map> {

    public interface Vo<VO extends Enum<VO> & VoModel<VO, V, _DESCRIPTION>, V extends Number, _DESCRIPTION>
            extends VoModel<VO, V, _DESCRIPTION> {

        /**
         * 操作结果 - 状态
         *
         * @Design VO
         */
        enum StatusVo
                implements VoModel<StatusVo, Integer, String> {
            /**
             * 此次操作失败
             *
             * @Description 泛用的说明类型。
             */
            FAILURE(0
                    , "此次操作失败"
                    , "此次操作失败。"),
            /**
             * 此次操作不符合业务要求
             *
             * @Description 详细的说明类型。
             */
            FAILURE_BUSINESS(1
                    , "此次操作不符合业务要求"
                    , "此次操作不符合业务要求。"),
            /**
             * [外部输入的参数或其相关联的信息]不符合业务要求
             *
             * @Description 详细的说明类型。
             */
            FAILURE_INPUT_PARAMETER(2
                    , "[外部输入的参数或其相关联的信息]不符合业务要求"
                    , "此次操作失败，原因是[外部输入的参数或其相关联的信息]不符合业务要求。"),
            /**
             * 此次操作成功
             *
             * @Description 泛用的说明类型。
             */
            SUCCESS(100
                    , "此次操作成功"
                    , "此次操作成功。");

            /**
             * 为持久化类型转换器提供支持
             */
            @javax.persistence.Converter(autoApply = true)
            public static class Converter
                    extends VoAttributeConverter<StatusVo, Integer, String> {

                // (单例模式 - 登记式)
                private static class Factory {
                    private static final @NotNull
                    Converter SINGLETON = new Converter();
                }

                private Converter() {
                    super(StatusVo.class);
                }

                public static @NotNull
                Converter getInstance() {
                    return Converter.Factory.SINGLETON;
                }

            }

            /**
             * 状态 - 编码
             */
            public final @NotNull
            Integer code;

            /**
             * 状态 - 名称
             */
            public final @NotNull
            String name;

            /**
             * 状态 - 描述
             */
            public final @NotNull
            String description;

            /**
             * (Constructor)
             *
             * @param code        [状态 - 编码]
             * @param name        [状态 - 名称]
             * @param description [状态 - 描述]
             */
            StatusVo(@NotNull Integer code, @NotNull String name, @NotNull String description)
                    throws IllegalArgumentException {
                if (null == code) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                            , "[状态 - 编码]"
                            , code
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == name) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                            , "[状态 - 名称]"
                            , code
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == description) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                            , "[状态 - 描述]"
                            , code
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
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
            public @NotNull
            String displayName() {
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
            @Override
            public @NotNull
            Converter voAttributeConverter() {
                return Converter.getInstance();
            }

        }

        /**
         * 额外拓展 <- 属性
         *
         * @Description 相关属性配置定义.
         * @Design VO
         */
        enum ExtraPropertyVo
                implements VoModel<ExtraPropertyVo, Integer, String> {
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
                    private static final Converter SINGLETON = new Converter();
                }

                private Converter() {
                    super(ExtraPropertyVo.class);
                }

                public static @NotNull
                Converter getInstance() {
                    return Converter.Factory.SINGLETON;
                }

            }

            /**
             * 属性 - 编码
             */
            public final @NotNull
            Integer code;

            /**
             * 属性 - 名称
             */
            public final @NotNull
            String name;

            /**
             * 属性 - 描述
             */
            public final @NotNull
            String description;

            /**
             * 属性 -> Key 名称
             */
            public final @NotNull
            String key;

            /**
             * 属性 -> Value 类型
             */
            public final @NotNull
            Class valueClass;

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
                    throws IllegalArgumentException {
                if (null == code) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                            , "[状态 - 编码]"
                            , code
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == name) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                            , "[状态 - 名称]"
                            , code
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == description) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                            , "[状态 - 描述]"
                            , code
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == key) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                            , "[属性 -> Key 名称]"
                            , code
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == valueClass) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                            , "[属性 -> Value 类型]"
                            , code
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
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
            public @NotNull
            String displayName() {
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
            public @NotNull
            String toString() {
                return VoModel.toString(this);
            }

            /**
             * 提供类型转换器
             *
             * @Design 为持久化类型转换功能提供支持.
             */
            @Override
            public @NotNull
            Converter voAttributeConverter() {
                return Converter.getInstance();
            }

        }

    }

    /**
     * {@link TaskResult} 的工厂类
     *
     * @Design {@link TaskResultFactoryModel}
     */
    public enum Factory
            implements TaskResultFactoryModel<TaskResult> {
        DEFAULT;

//        /**
//         * 创建
//         *
//         * @param statusVo  [操作结果 - 状态]
//         *
//         * @return {@link TaskResult}
//         */
//        @NotNull
//        public TaskResult create(@NotNull TaskResult.Vo.StatusVo statusVo) {
//            return new TaskResult(statusVo, null, null);
//        }
//
//        /**
//         * 创建
//         *
//         * @param statusVo  [操作结果 - 状态]
//         * @param message   [操作结果 - 响应的消息]
//         *
//         * @return {@link TaskResult}
//         */
//        @NotNull
//        public TaskResult create(@NotNull TaskResult.Vo.StatusVo statusVo, @Nullable String message) {
//            return new TaskResult(statusVo, message, null);
//        }

        /**
         * 创建
         *
         * @param statusVo [操作结果 - 状态]
         * @param data     [操作结果 - 相关数据]
         * @param <_DATA>
         * @return {@link TaskResult}
         */
        public <_DATA> @NotNull TaskResult<_DATA> create(@NotNull TaskResult.Vo.StatusVo statusVo, @NotNull _DATA data) {
            if (null == statusVo) {
                //-- 非法参数: [操作结果 - 状态]
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                        , "[操作结果 - 状态]"
                        , statusVo
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            if (null == data) {
                //-- 非法参数: [操作结果 - 相关数据]
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                        , "[操作结果 - 相关数据]"
                        , data
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            return new TaskResult<>(statusVo, null, data);
        }

        /**
         * 创建
         *
         * @param statusVo [操作结果 - 状态]
         * @param message  [操作结果 - 响应的消息]
         * @param data     [操作结果 - 相关数据]
         * @param <_DATA>
         * @return {@link TaskResult}
         */
        public <_DATA> @NotNull TaskResult<_DATA> create(@NotNull TaskResult.Vo.StatusVo statusVo, @Nullable String message, @NotNull _DATA data) {
            if (null == statusVo) {
                //-- 非法参数: [操作结果 - 状态]
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "[操作结果 - 状态]"
                        , statusVo
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            if (null == data) {
                //-- 非法参数: [操作结果 - 相关数据]
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "[操作结果 - 相关数据]"
                        , data
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            return new TaskResult<>(statusVo, message, data);
        }
    }

    /**
     * 操作结果 - 相关数据
     *
     * @Description {@link super#data}
     * @Design {@link super#data}
     */
    public final @NotNull
    _DATA data = super.data;

    /**
     * 操作结果 - 响应的消息
     */
    public final @NotNull
    String message;

    /**
     * 额外拓展
     *
     * @Description {@link super#extra}
     */
    public final @NotNull
    Map<String, Object> extra = super.extra;

    /**
     * (Constructor)
     *
     * @param state   [操作结果 - 状态]
     * @param message [操作结果 - 响应的消息]
     * @param data    [操作结果 - 相关数据]
     */
    protected TaskResult(@NotNull TaskResult.Vo.StatusVo state, @Nullable String message, @NotNull _DATA data) {
        super(state, data, new HashMap<String, Object>(1));

        this.message = (null == message)
                ? state.name
                : message;
    }

    /**
     * 操作结果 -> 判断是否失败
     *
     * @return 判断结果
     * @Description 操作流程未按照预期执行, 或业务结果不符合预期.
     */
    public boolean isFailure() {
        switch (this.status) {
            case FAILURE:
            case FAILURE_BUSINESS:
                return true;
        }

        return false;
    }

    /**
     * 操作结果 -> 判断是否成功
     *
     * @return 判断结果
     * @Description 操作流程完全按照预期执行, 且业务结果完全符合预期.
     */
    public boolean isSuccess() {
        switch (this.status) {
            case SUCCESS:
                return true;
        }

        return false;
    }

    @Override
    public @NotNull
    String toJSONString() {
        return String.format("{status:%s,message:\"%s\",data:%s,extra:%s}"
                , status
                , message
                , new StringBuilder(data instanceof String ? "\"" : "").append(data).append(data instanceof String ? '\"' : "")
                , !isSuccess() ? extra : "{}");
    }

    @Override
    public @NotNull
    String toString() {
        return String.format("%s{status=%s, message=\"%s\", data=%s, extra=%s}"
                , getClass().getSimpleName()
                , status
                , message
                , new StringBuilder(data instanceof String ? "\"" : "").append(data).append(data instanceof String ? '\"' : "")
                , !isSuccess() ? extra : "{}");
    }

}
