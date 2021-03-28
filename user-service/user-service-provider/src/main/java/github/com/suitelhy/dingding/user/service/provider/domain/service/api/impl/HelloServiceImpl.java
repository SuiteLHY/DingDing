package github.com.suitelhy.dingding.user.service.provider.domain.service.api.impl;

import github.com.suitelhy.dingding.user.service.api.domain.service.HelloService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotNull;

@Service(cluster = "failover")
public class HelloServiceImpl
        implements HelloService {

    @Value("${dubbo.protocol.name}")
    private String dubboName;

    @Override
    public @NotNull String sayHello(@NotNull String name) {
        return String.format("[%s]: Hello, %s!", dubboName, name);
    }

}
