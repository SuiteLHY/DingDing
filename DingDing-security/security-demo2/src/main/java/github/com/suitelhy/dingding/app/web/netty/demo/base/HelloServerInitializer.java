package github.com.suitelhy.dingding.app.web.netty.demo.base;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 从线程组 <- 子处理器
 *
 * @Description socket channel 初始化器
 */
public class HelloServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        // 获取管道
        ChannelPipeline pipeline = channel.pipeline();
        // 添加 Handler: HTTP 解码器
        pipeline.addLast("HttpDecoder", new HttpServerCodec());
        // 添加 Handler: 自定义响应消息
        pipeline.addLast("customHandler", new CustomHandler());
    }

}
