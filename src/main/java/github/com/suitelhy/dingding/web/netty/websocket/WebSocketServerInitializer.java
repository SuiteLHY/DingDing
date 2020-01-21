package github.com.suitelhy.dingding.web.netty.websocket;

import github.com.suitelhy.dingding.web.netty.websocket.handler.ChatHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * WebSocket Server - 从线程组 <- 子处理器
 * @Description socket channel 初始化器
 */
public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        // 获取管道 (ChannelPipeline)
        ChannelPipeline pipeline = channel.pipeline();

        //===== WebSocket <- HTTP 支持 =====//
        // 添加 Handler: HTTP 解码器
        pipeline.addLast("HttpDecoder", new HttpServerCodec());
        // 添加 Handler: 大数据流 <- 写入操作支持
        pipeline.addLast(new ChunkedWriteHandler());
        // 添加 Handler: HTTP 操作对象的聚合
        //-> {@Api <a href="https://netty.io/4.1/api/io/netty/handler/codec/http/HttpObjectAggregator.html">
        //->        HttpObjectAggregator（Netty API参考（4.1.45.Final））</a>}
        //-> TIPS: 大多数 Netty 服务必需
        pipeline.addLast(new HttpObjectAggregator(1024 * 64));

        //===== WebSocket <- 服务器处理 =====//
        /**
         * <class>WebSocketServerProtocolHandler</class>
         *
         * @Api <a href="https://netty.io/4.0/api/io/netty/handler/codec/http/websocketx/WebSocketServerProtocolHandler.html">
         *->     WebSocketServerProtocolHandler（Netty API参考（4.0.56.Final））</a>
         *
         * @Description 负责处理 WebSocket 握手动作 (Handshaking) 和 控制帧 (close、ping、pong).
         *
         * @Tips 对于 WebSocket 来说, 所有数据都是以 frames 进行传输的, 不同数据类型对应不同的 frames.
         */
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"/* websocketPath, 路由地址 */));
        // 添加 Handler: 文本消息处理器
        pipeline.addLast(new ChatHandler());
        //==========//
    }

}
