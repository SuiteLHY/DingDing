package github.com.suitelhy.dingding.app.web.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.stereotype.Component;

/**
 * WebSocket Server
 *
 * @Description 基于 Netty 的 WebSocket Server.
 */
@Component
public class WebSocketServer {

    //--- 定义线程组
    // 主线程组 -> 用于接收并分配客户端的请求
    private final EventLoopGroup firstEventLoopGroup;
    // 从线程组 -> 用于处理 (主线程组分配的) 客户端的请求
    private final EventLoopGroup secondEventLoopGroup;
    //---//

    // Netty 服务启动对象
    private final ServerBootstrap server;

    // 通道控制器
    private ChannelFuture channelFuture;

    //===== 工厂实现单例 =====//
    private static class Factory {
        private static final WebSocketServer INSTANCE = new WebSocketServer();
    }

    public static WebSocketServer getInstance() {
        return Factory.INSTANCE;
    }

    /**
     * Constructor
     */
    private WebSocketServer() {
        this.firstEventLoopGroup = new NioEventLoopGroup();
        this.secondEventLoopGroup = new NioEventLoopGroup();

        this.server = new ServerBootstrap()
                .group(firstEventLoopGroup, secondEventLoopGroup) // 设置主从线程组
                .channel(NioServerSocketChannel.class) // 设置 NIO 双向通道
                .childHandler(new WebSocketServerInitializer())/* 从线程组 <- 子处理器 */;
    }
    //==========//

    /**
     * 服务器 <- 启动
     */
    public void start() {
        this.channelFuture = server.bind(8088);
        System.out.println("===== WebSocket Server 启动完毕 =====");
    }

}
