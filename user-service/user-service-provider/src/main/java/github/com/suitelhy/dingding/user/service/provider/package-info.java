/**
 * 用户基础业务-服务提供端
 *
 * @Solution
 * · {@linkplain <a href="https://zhuanlan.zhihu.com/p/83404546">dubbo使用过程中遇到的一些坑 - 知乎</a> dubbo默认采用Hessian方式序列化，但是不能较好的适应Java的泛型，建议把序列化方式改为 kryo}
 *
 * @see <a href="https://mercyblitz.github.io/2019/04/26/Dubbo-Spring-Cloud-%E9%87%8D%E5%A1%91%E5%BE%AE%E6%9C%8D%E5%8A%A1%E6%B2%BB%E7%90%86/">Dubbo spring cloud 重塑微服务治理 - 小马哥的技术博客</a>
 * @see <a href="https://nacos.io/zh-cn/docs/quick-start-spring-cloud.html">Nacos Spring Cloud 快速开始</a>
 */
package github.com.suitelhy.dingding.user.service.provider;