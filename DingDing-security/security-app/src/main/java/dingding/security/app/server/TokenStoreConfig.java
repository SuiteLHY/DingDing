/**
 *
 */
package dingding.security.app.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import dingding.security.core.properties.SecurityProperties;

/**
 * 令牌存储配置
 *
 * @author zhailiang
 * @Editor Suite
 *
 */
@Configuration
public class TokenStoreConfig {

    /**
     * 使用 redis 存储 token 的配置
     *
     * @Description 只有在 dingding.security.oauth2.tokenStore 配置为 redis 时生效.
     *
     * @author zhailiang
     *
     */
    @Configuration
    @ConditionalOnProperty(prefix = "dingding.security.oauth2"
            , name = "tokenStore"
            , havingValue = "redis")
    public static class RedisConfig {

        // Redis 连接工厂
        @Autowired
        private RedisConnectionFactory redisConnectionFactory;

        /**
         * @Description OAuth2令牌的持久性接口的基于 Redis 的实现.
         * @return
         */
        @Bean
        public TokenStore redisTokenStore() {
            return new RedisTokenStore(redisConnectionFactory);
        }

    }

    /**
     * JWT (Json Web Token) 策略
     *
     * @Description 使用 JWT 时的配置; 默认生效.
     *
     * @author zhailiang
     *
     */
    @Configuration
    @ConditionalOnProperty(prefix = "dingding.security.oauth2"/* 配置项前缀, 最后一个"."之前的内容 */
            , name = "tokenStore"/* 配置项名称, 最后一个"."之后的内容 */
            , havingValue = "jwt"/* 配置项的值 */
            , matchIfMissing = true)
    public static class JwtConfig {

        @Autowired
        private SecurityProperties securityProperties;

        /**
         * <interface>TokenStore</interface>
         *
         * @Description 管理 Token 存储.
         * @return
         */
        @Bean
        public TokenStore jwtTokenStore() {
            return new JwtTokenStore(jwtAccessTokenConverter());
        }

        /**
         * <class>JwtAccessTokenConverter</class>
         *
         * @Description 管理 Token 生成.
         * @return
         */
        @Bean
        public JwtAccessTokenConverter jwtAccessTokenConverter() {
            JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
            // 设置签名用的密钥
            converter.setSigningKey(securityProperties.getOauth2().getJwtSigningKey());
            return converter;
        }

        /**
         *
         * @return
         */
        @Bean
        @ConditionalOnBean(TokenEnhancer.class)
        public TokenEnhancer jwtTokenEnhancer() {
            return new TokenJwtEnhancer();
        }

    }

}
