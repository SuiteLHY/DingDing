package github.com.suitelhy.dingding.app.web.netty.demo.base;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * HelloServer
 *
 * @Description 实现对客户端请求的响应.
 */
public class HelloServer {

    public static void main(String[] args) throws InterruptedException {
        //--- 定义线程组
        // 主线程组 -> 用于接收并分配客户端的请求
        EventLoopGroup firstEventLoopGroup = new NioEventLoopGroup();
        // 从线程组 -> 用于处理 (主线程组分配的) 客户端的请求
        EventLoopGroup secondEventLoopGroup = new NioEventLoopGroup();
        //---//
        try {
            // Netty 服务启动对象
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(firstEventLoopGroup, secondEventLoopGroup) // 设置主从线程组
                    .channel(NioServerSocketChannel.class) // 设置 NIO 双向通道
                    .childHandler(new HelloServerInitializer())/* 从线程组 <- 子处理器 */;
            // 通道控制器
            ChannelFuture channelFuture = serverBootstrap.bind(8888) // 设置端口号
                    .sync()/* 启动方式: 同步 */;
            // 通道控制器 -> 监听设置
            channelFuture.channel()
                    .closeFuture() // 监听 -> 通道关闭动作
                    .sync()/* 监听方式: 同步 */;
        } catch (InterruptedException e) {
            throw e;
        } finally {
            //--- 关闭线程组
            if (firstEventLoopGroup.shutdownGracefully().isSuccess()) {
                secondEventLoopGroup.shutdownGracefully();
            } else {
                secondEventLoopGroup.shutdownGracefully();
                firstEventLoopGroup.shutdownGracefully();
            }
        }

    }

}
