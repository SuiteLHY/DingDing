package github.com.suitelhy.dingding.app.infrastructure.config;
//
//import java.lang.reflect.Method;
//
//import org.springframework.cache.annotation.CachingConfigurerSupport;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cache.interceptor.KeyGenerator;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * Redis 的 Cache 配置类
// *
// * @author Suite
// */
//@Configuration
//// @EnableCaching注解: 开启缓存
//@EnableCaching
//public class RedisConfig extends CachingConfigurerSupport {
//
//    /**
//     * ...
//     */
//    @Bean
//    public KeyGenerator keyGenerator() {
//        return new KeyGenerator() {
//            @Override
//            public Object generate(Object target, Method method, Object... params) {
//                StringBuilder sb = new StringBuilder();
//                sb.append(target.getClass().getName());
//                sb.append(method.getName());
//                for (Object obj : params) {
//                    sb.append(obj.toString());
//                }
//                return sb.toString();
//            }
//        };
//    }
//
//}
