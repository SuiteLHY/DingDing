package github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo;

import github.com.suitelhy.dingding.core.infrastructure.config.springdata.attribute.converter.VoAttributeConverter;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.VoModel;

import javax.validation.constraints.NotNull;

/**
 * {@linkplain "https://dubbo.apache.org/" Dubbo}相关配置
 *
 * @Description 主要是策略选择.
 *
 * @param <VO>
 * @param <V>
 * @param <_DESCRIPTION>
 *
 * @see VoModel
 */
public interface Dubbo<VO extends Enum<VO> & VoModel<VO, V, _DESCRIPTION>, V extends Number, _DESCRIPTION>
        extends VoModel<VO, V, _DESCRIPTION> {

    /**
     * 策略
     *
     * @param <VO>
     * @param <V>
     * @param <_DESCRIPTION>
     */
    interface Strategy<VO extends Enum<VO> & VoModel<VO, V, _DESCRIPTION>, V extends Number, _DESCRIPTION>
            extends VoModel<VO, V, _DESCRIPTION>
    {

        /**
         * 集群容错策略
         */
        enum ClusterVo
                implements Strategy<ClusterVo, Integer, String> {
            /**
             * 策略 -> 失败自动恢复
             *
             * @Description
             * · 失败自动恢复，后台记录失败请求，定时重发。
             * · 通常用于消息通知操作。
             */
            FAIL_BACK(1
                    , "失败自动恢复"
                    , "失败自动恢复，后台记录失败请求，定时重发。通常用于消息通知操作。"
                    , "failback"),
            /**
             * 策略 -> 快速失败
             *
             * @Description
             * · 快速失败，只发起一次调用，失败立即报错。
             * · 通常用于非幂等性的写操作，比如新增记录。
             */
            FAIL_FAST(2
                    , "快速失败"
                    , "快速失败，只发起一次调用，失败立即报错。通常用于非幂等性的写操作，比如新增记录。"
                    , "failfast"),
            /**
             * 策略 -> 失败自动切换
             *
             * @Description
             * · 失败自动切换，当出现失败，重试其它服务器。通常用于读操作，但重试会带来更长延迟。
             * · 可通过{@code retries="2"}来设置重试次数(不含第一次)。
             */
            FAILOVER(3
                    , "失败自动切换"
                    , "失败自动切换，当出现失败，重试其它服务器。通常用于读操作，但重试会带来更长延迟。可通过 retries=\"2\" 来设置重试次数(不含第一次)。"
                    , "failover"),
            /**
             * 策略 -> 失败安全
             *
             * @Description
             * · 失败安全，出现异常时，直接忽略。
             * · 通常用于写入审计日志等操作。
             */
            FAILSAFE(4
                    , "失败安全"
                    , "失败安全，出现异常时，直接忽略。通常用于写入审计日志等操作。"
                    , "failsafe"),
            /**
             * 策略 -> 并行调用
             *
             * @Description
             * · 并行调用多个实例，只要一个成功就返回。
             */
            FORKING(5
                    , "并行调用"
                    , "并行调用多个实例，只要一个成功就返回。"
                    , "forking");

            /**
             * 为持久化类型转换器提供支持
             *
             * @see VoAttributeConverter
             */
            @javax.persistence.Converter(autoApply = true)
            public static class Converter
                    extends VoAttributeConverter<ClusterVo, Integer, String> {

                /**
                 * @Design (单例模式 - 登记式)
                 */
                private static class Factory {
                    private static final Converter SINGLETON = new Converter();
                }

                private Converter() {
                    super(ClusterVo.class);
                }

                public static @NotNull Converter getInstance() {
                    return Converter.Factory.SINGLETON;
                }

            }

            public final int code;

            public final @NotNull String name;

            public final @NotNull String description;

            /**
             * {@linkplain <a href="https://dubbo.apache.org/"/> Dubbo} 的 {@code Cluster Strategy} 配置项
             */
            public final @NotNull String cluster;

            /**
             * (Constructor)
             *
             * @param code
             * @param name
             * @param description
             * @param cluster       {@linkplain this#cluster 策略配置项}
             *
             * @throws IllegalArgumentException 非法的构造参数
             */
            ClusterVo(int code, @NotNull String name, @NotNull String description
                    , @NotNull String cluster)
                    throws IllegalArgumentException
            {
                if (null == name) {
                    throw new IllegalArgumentException(String.format("非法的构造参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                            , "name"
                            , name
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == description) {
                    throw new IllegalArgumentException(String.format("非法的构造参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                            , "description"
                            , description
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == cluster) {
                    throw new IllegalArgumentException(String.format("非法的构造参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                            , "[策略配置项]"
                            , cluster
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }

                this.code = code;
                this.name = name;
                this.description = description;
                this.cluster = cluster;
            }

            /**
             * VO 的值
             *
             * @Description Unique attribute.
             *
             * @return 可为 {@code null}, 此时[VO的值]为 {@code null} (而不是缺失设置).
             */
            @Override
            public Integer value() {
                return this.code;
            }

            /**
             * VO 的详细信息
             *
             * @return 不为 {@code null}
             */
            @Override
            public String description() {
                return this.description;
            }

            /**
             * VO 的 (展示)名称
             *
             * @return 不为 {@code null}
             */
            @Override
            public @NotNull String displayName() {
                return this.name;
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

}
