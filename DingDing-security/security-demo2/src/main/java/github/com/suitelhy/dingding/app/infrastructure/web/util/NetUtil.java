package github.com.suitelhy.dingding.app.infrastructure.web.util;

import github.com.suitelhy.dingding.app.infrastructure.util.RegexUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

/**
 * 网络工具配置类
 */
enum NetUtilConfig {
    SINGLETON;
    /*//===== 工厂模式实现单例 =====//
    private NetUtilConfig() {}

    private static class Factory {
        static final NetUtilConfig INSTANCE = new NetUtilConfig();
    }

    static NetUtilConfig getInstance() {
        return Factory.INSTANCE;
    }*/

    //===== 配置项 =====//
    private final String[] PROXY_SERVER_REQUEST_HEADERS = {
            // Apache 服务代理
            "Proxy-Client-IP",
            // WebLogic 服务代理
            "WL-Proxy-Client-IP",
            // Squid 服务代理
            "X-Forwarded-For",
            // Nginx 服务代理
            "X-Real-IP",
            // 非标准服务代理 (注意: 安全性)
            "HTTP_CLIENT_IP",
            // HTTP 代理或负载均衡方式处理的请求
            // ("HTTP_*"的请求 Header 都很容易伪造, 生产环境下禁止读取)
            "HTTP_X_FORWARDED_IP",
            // 最后握手的IP, 作为缺省项; "REMOTE_ADDR" 不可以显式伪造, 目前是最安全的(..)
            /*"REMOTE_ADDR",*/
    };

    /**
     * 正则表达式 - 匹配 IP 地址
     *
     * @Reference <a href="https://www.jianshu.com/p/82886d77440c">正则表达式 - 匹配 IP 地址</a>
     * <a href="https://c.runoob.com/front-end/854">正则表达式在线测试 | 菜鸟工具</a>
     */
    private final String REGEX_IP = /*"((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}"*/
            "((?:(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d)\\.){3}(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d))";

    //===== getter =====//

    /**
     * 配置项集合 - 代理服务请求头
     * @return 可接受的代理服务请求头
     */
    String[] getProxyServerRequestHeaders() {
        return PROXY_SERVER_REQUEST_HEADERS;
    }

    /**
     * 配置项 - 正则表达式 - IP
     * @return 校验 IP 格式的正则表达式
     */
    String getRegexIp() {
        return REGEX_IP;
    }

}

/**
 * 网络工具
 */
public class NetUtil {

    // 配置
    private static final NetUtilConfig NET_UTIL_CONFIG = /*NetUtilConfig.getInstance()*/NetUtilConfig.SINGLETON;

    /**
     * 校验 IP 地址格式
     * @param ip
     * @return {<code>true</code> : <p>符合 IP 地址格式</p>
     *-> , <code>false</code> : <p>参数 ip 为空或不符合 IP 地址格式</p>}
     */
    public static boolean validateIpAddress(@NotNull String ip) {
        return null != ip
                && !ip.isEmpty()
                && RegexUtil.getPattern(NET_UTIL_CONFIG.getRegexIp()).matcher(ip).matches();
    }

    /**
     * 从 Request 中获取 IP 地址
     *
     * 相关资料:
     *-> <a href="https://juejin.im/post/5dde34bef265da060a52181c">
     *->     X-Forward-For 看破红尘，代理 IP 无所遁形 - 掘金</a>,
     * 相关 Python 爬虫资料:
     *-> <a href="https://juejin.im/post/5e05a58b6fb9a0164f2955b2">一线大厂在用的反爬虫手段，看我破！ - 掘金</a>
     *-> <a href="https://l1905.github.io/%E5%85%A5%E9%97%A8/2019/07/16/ip-proxy-01/">IP代理池理解 | 生活的自留地</a>
     *
     * @param request
     * @return
     */
    // 参考项目中, 此方法的实现过于草率(...), 于是上网查到
    //-> <a href="https://blog.csdn.net/hhhh222222/article/details/77878510">
    //->     根据HttpServletRequest获取用户真实IP地址_hhhh222222的博客-CSDN博客</a>
    // 总之, 黑猫白猫, 能干好活就是好猫, coding after thinking, 避免不必要的错误(...).
    public static String getIpAddress(@NotNull HttpServletRequest request) {
        String ipAddress = null;
        for (String requestHeader : NET_UTIL_CONFIG.getProxyServerRequestHeaders()) {
            ipAddress = request.getHeader(requestHeader);
            if (validateIpAddress(ipAddress)) {
                break;
            }
        }
        if (!validateIpAddress(ipAddress)) {
            //--- 有些网络通过多层代理，那么获取到的IP就会有多个, 其中获取到的IP串
            //-> 一般是通过逗号<code>,</code>分割开来, 且第一个IP为客户端的IP
            try {
                for (String eachIp : ipAddress.split(",")) {
                    if (validateIpAddress(eachIp)) {
                        ipAddress = eachIp;
                        break;
                    }
                }
                if (!validateIpAddress(ipAddress)) {
                    throw new NullPointerException();
                }
            } catch (Exception e) {
                //-- 获取不到 IP 地址的情况
                throw new RuntimeException(NetUtil.class.getSimpleName()
                        + " - "
                        + "getIpAddress(HttpServletRequest request)"
                        + " -> 从 Request 中获取 IP 地址出错!"
                        , e);
            }
        }
        if (!validateIpAddress(ipAddress)) {
            // 获取最后握手的IP, 作为缺省项
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

}
