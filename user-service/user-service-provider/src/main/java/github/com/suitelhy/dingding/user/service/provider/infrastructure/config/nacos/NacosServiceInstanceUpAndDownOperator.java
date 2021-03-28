//package github.com.suitelhy.dingding.user.service.provider.infrastructure.config.nacos;
//
//import com.alibaba.cloud.nacos.registry.NacosRegistration;
//import com.alibaba.cloud.nacos.registry.NacosServiceRegistry;
//import github.com.suitelhy.dingding.core.infrastructure.util.Toolbox;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import javax.validation.constraints.NotNull;
//import java.io.Closeable;
//import java.io.IOException;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.ScheduledThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
///**
// * Nacos 服务实例 -> 上下线控制器
// *
// * @Description
// * · 解决 {@linkplain <a href="https://github.com/alibaba/spring-cloud-alibaba/issues/1805">springCloudAlibaba+dubbo+nacos环境下，重启服务提供者或先启动服务消费者后启动服务提供者的情况下，消费者有时候会出现找不到服务的问题及解决方案（In the spring cloud Alibaba + Dubbo + Nacos environment, when the service provider is restarted or the service consumer is started first and then the service provider, sometimes the consumer can not find the service when calling） · Issue #1805 · alibaba/spring-cloud-alibaba</a> SpringCloudAlibaba + Dubbo + Nacos 的部分版本环境下，消费者有时候会出现找不到生产者服务的问题及解决方案}
// */
//@Component
//@Slf4j
//public class NacosServiceInstanceUpAndDownOperator
//        implements ApplicationRunner, Closeable {
//
//    /**
//     * Nacos 服务实例上线
//     */
//    private static final @NotNull String OPERATOR_UP = "UP";
//
//    /**
//     * Nacos 服务实例下线
//     */
//    private static final @NotNull String OPERATOR_DOWN = "DOWN";
//
//    @Autowired
//    NacosServiceRegistry nacosServiceRegistry;
//
//    @Autowired
//    NacosRegistration nacosRegistration;
//
//    private ScheduledExecutorService executorService;
//
//    @PostConstruct
//    public void init() {
//        int poolSize = 1;
//        this.executorService = new ScheduledThreadPoolExecutor(poolSize, runnable -> {
//            Thread thread = new Thread(runnable);
//            thread.setDaemon(true);
//            thread.setName("NacosServiceInstanceUpAndDownOperator");
//            return thread;
//        });
//    }
//
//    @Override
//    public void run(ApplicationArguments args)
//            throws Exception
//    {
//        long delay_down = 5000L;    // 下线任务延迟
//        long delay_up = 10000L;     // 上线任务延迟
//
//        this.executorService.schedule(new InstanceDownAndUpTask(nacosServiceRegistry, nacosRegistration, OPERATOR_DOWN)
//                , delay_down
//                , TimeUnit.MILLISECONDS);
//        this.executorService.schedule(new InstanceDownAndUpTask(nacosServiceRegistry, nacosRegistration, OPERATOR_UP)
//                , delay_up
//                , TimeUnit.MILLISECONDS);
//    }
//
//    /**
//     * Closes this stream and releases any system resources associated
//     * with it. If the stream is already closed then invoking this
//     * method has no effect.
//     *
//     * <p> As noted in {@link AutoCloseable#close()}, cases where the
//     * close may fail require careful attention. It is strongly advised
//     * to relinquish the underlying resources and to internally
//     * <em>mark</em> the {@code Closeable} as closed, prior to throwing
//     * the {@code IOException}.
//     *
//     * @throws IOException if an I/O error occurs
//     */
//    @Override
//    public void close()
//            throws IOException
//    {
//        Toolbox.ThreadUtil.getInstance().shutdownThreadPool(NacosServiceInstanceUpAndDownOperator.this.executorService);
//    }
//
//    /**
//     * 服务实例上下线任务
//     */
//    class InstanceDownAndUpTask
//            implements Runnable
//    {
//
//        private final NacosServiceRegistry nacosServiceRegistry;
//
//        private final NacosRegistration nacosRegistration;
//
//        // 更新服务实例的状态 ：UP 、DOWN
//        private final String nacosServiceInstanceOperator;
//
//        InstanceDownAndUpTask(@NotNull NacosServiceRegistry nacosServiceRegistry, @NotNull NacosRegistration nacosRegistration, @NotNull String nacosServiceInstanceOperator) {
//            this.nacosServiceRegistry = nacosServiceRegistry;
//            this.nacosRegistration = nacosRegistration;
//            this.nacosServiceInstanceOperator = nacosServiceInstanceOperator;
//        }
//
//        @Override
//        public void run() {
//            log.info("===更新nacos服务实例的状态to：{}===start=", nacosServiceInstanceOperator);
//            this.nacosServiceRegistry.setStatus(nacosRegistration, nacosServiceInstanceOperator);
//            log.info("===更新nacos服务实例的状态to：{}===end=", nacosServiceInstanceOperator);
//
//            // 上线后，关闭线程池
//            if (NacosServiceInstanceUpAndDownOperator.OPERATOR_UP.equals(nacosServiceInstanceOperator)) {
//                Toolbox.ThreadUtil.getInstance().shutdownThreadPool(NacosServiceInstanceUpAndDownOperator.this.executorService);
//            }
//        }
//
//    }
//
//}
