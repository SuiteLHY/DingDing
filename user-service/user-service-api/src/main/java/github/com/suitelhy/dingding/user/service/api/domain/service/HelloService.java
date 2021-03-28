package github.com.suitelhy.dingding.user.service.api.domain.service;

import javax.validation.constraints.NotNull;

/**
 * Hello ~
 *
 * @Description 练习用的服务接口.
 */
public interface HelloService {

    @NotNull String sayHello(@NotNull String name);

}
