package github.com.suitelhy.dingding.web.netty.websocket.handler;

import github.com.suitelhy.dingding.infrastructure.util.CalendarController;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 文本消息处理器
 *
 * @Description 处理 WebSocket 传输用的文本消息.
 *
 * @Tips <class>TextWebSocketFrame</class>是 WebSocket 通信中专门处理文本的类; frame 是消息的载体.
 */
public class ChatHandler
        extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    /**
     * 客户端 Channel 组 <- 存放和管理容器
     *
     * @Api <a href="https://netty.io/4.1/api/io/netty/channel/group/ChannelGroup.html">
     *->     ChannelGroup（Netty API参考（4.1.45.Final））</a>
     *
     * @Description <interface>ChannelGroup</interface>
     */
    private static final ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * Channel -> 从对等方读取消息时 -> 动作
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg)
            throws Exception {
        // 客户端发送来的消息
        String content = msg.text();
        System.out.println("=== 接收到数据: " + content);

        for (Channel channel : clients) {
            channel.writeAndFlush(new TextWebSocketFrame("[" + new CalendarController()
                    + "]接收到消息: " + content));
        }
        /*//↑ 等效写法; 语法糖, 业务确定后需要简化代码时可用.
        clients.writeAndFlush(new TextWebSocketFrame("[" + new CalendarController()
                + "]接收到消息: " + content));*/
    }

    /**
     * Channel -> 操作类添加 -> 动作
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx)
            throws Exception {
        clients.add(ctx.channel());
    }

    /**
     * Channel -> 操作类移除 -> 动作
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx)
            throws Exception {
        //TIPS: <interface: ChannelGroup>client</interface: ChannelGroup> 会自动移除注销状态的 Channel 实例.
        Channel channel = ctx.channel();
        if (null != clients.find(channel.id())) {
            clients.remove(channel);
        }
    }

}
