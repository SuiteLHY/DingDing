package github.com.suitelhy.dingding.core.infrastructure.domain.vo.config;

import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityRoleService;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.security.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * VO 初始持久化
 *
 * @Description 配置类.
 *
 * @Reference <a href="https://www.javatt.com/p/28644">Spring boot初始化Mongo数据库(将.json文件持久化到Mongo数据库) - Java天堂</a>
 *
 * @see github.com.suitelhy.dingding.core.infrastructure.domain.vo
 */
@Component
public class InitPersistence
        implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private SecurityRoleService securityRoleService;

    /**
     * 是否执行初始持久化
     *
     * @Description 配置属性.
     *
     * @Reference <a href="https://www.choupangxia.com/2019/12/25/springboot-spring-value/">
     *->     SpringBoot之Spring@Value属性注入使用详解 – 程序新视界</a>
     */
    @Value("${dingding.domain.vo.init_persistence:}")
    private Boolean initPersistence;

    /**
     * Handle an application event.
     *
     * @Description 对 Spring boot 容器加载完成的事件进行处理.
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!Boolean.TRUE.equals(initPersistence)) {
            return;
        }

        // 安全模块 VO -> 角色
        for (Security.RoleVo each : Security.RoleVo.class.getEnumConstants()) {
            if (!securityRoleService.existsByCode(each.name())) {
                securityRoleService.insert(SecurityRole.Factory.ROLE.create(each.name()
                        , each.name
                        , each.description));
            }
        }
    }

}
