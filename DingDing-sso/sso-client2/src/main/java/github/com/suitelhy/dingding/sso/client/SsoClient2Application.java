/**
 * 
 */
package github.com.suitelhy.dingding.sso.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhailiang
 *
 */
@SpringBootApplication
@RestController
/*@EnableOAuth2Sso // 废弃注解, 不可用*/
@EnableOAuth2Client
@EnableResourceServer
public class SsoClient2Application {
	
	@GetMapping("/user")
	public Authentication user(Authentication user) {
		return user;
	}

	public static void main(String[] args) {
		SpringApplication.run(SsoClient2Application.class, args);
	}
	
}
