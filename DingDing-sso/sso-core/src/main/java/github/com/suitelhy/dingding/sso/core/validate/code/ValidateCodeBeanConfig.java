package github.com.suitelhy.dingding.sso.core.validate.code;

import github.com.suitelhy.dingding.sso.core.properties.SecurityProperties;
import github.com.suitelhy.dingding.sso.core.validate.code.image.ImageCodeGenerator;
import github.com.suitelhy.dingding.sso.core.validate.code.sms.DefaultSmsCodeSender;
import github.com.suitelhy.dingding.sso.core.validate.code.sms.SmsCodeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码相关的扩展点配置
 *
 * @Description 配置在这里的bean，业务系统都可以通过声明同类型或同名的bean来覆盖安全模块默认的配置。
 *
 * @author zhailiang
 *
 */
@Configuration
public class ValidateCodeBeanConfig {
	
	@Autowired
	private SecurityProperties securityProperties;
	
	/**
	 * 图片验证码图片生成器
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(name = "imageValidateCodeGenerator")
	public ValidateCodeGenerator imageValidateCodeGenerator() {
		ImageCodeGenerator codeGenerator = new ImageCodeGenerator();
		codeGenerator.setSecurityProperties(securityProperties);
		return codeGenerator;
	}
	
	/**
	 * 短信验证码发送器
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(SmsCodeSender.class)
	public SmsCodeSender smsCodeSender() {
		return new DefaultSmsCodeSender();
	}

}
