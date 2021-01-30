package github.com.suitelhy.dingding.app.web.netty.demo.base;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * Pipeline - Handler (自定义操作类)
 *
 * @Description 自定义操作类; 相当于【入站】(Inbound).
 * @Api <a href="https://netty.io/4.1/api/io/netty/channel/ChannelInboundHandler.html">
 * ->     ChannelInboundHandler（Netty API参考（4.1.45.Final））</a>
 */
public class CustomHandler
        extends SimpleChannelInboundHandler<HttpObject> {

    /**
     * Channel -> 注册动作 -> 动作
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("===== Channel -> 注册动作 -> 动作 =====");
        super.channelRegistered(ctx);
        System.out.println("==========");
    }

    /**
     * Channel -> 销毁动作 -> 动作
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("===== Channel -> 销毁动作 -> 动作 =====");
        super.channelUnregistered(ctx);
        System.out.println("==========");
    }

    /**
     * Channel -> 活跃状态 -> 动作
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("===== Channel -> 活跃状态 -> 动作 =====");
        super.channelActive(ctx);
        System.out.println("==========");
    }

    /**
     * Channel -> 不活跃状态 -> 动作
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("===== Channel -> 不活跃状态 -> 动作 =====");
        super.channelInactive(ctx);
        System.out.println("==========");
    }

    /**
     * Channel -> 读取完毕 -> 动作
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("===== Channel -> 读取完毕 -> 动作 =====");
        super.channelReadComplete(ctx);
        System.out.println("==========");
    }

    /**
     * Channel -> 用户事件触发 -> 动作
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println("===== Channel -> 用户事件触发 -> 动作 =====");
        super.userEventTriggered(ctx, evt);
        System.out.println("==========");
    }

    /**
     * Channel - 可写性状态 -> 被更改 -> 动作
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        System.out.println("===== Channel - 可写性状态 -> 被更改 -> 动作 =====");
        super.channelWritabilityChanged(ctx);
        System.out.println("==========");
    }

    /**
     * Channel -> 捕获到异常 -> 动作
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("===== Channel -> 捕获到异常 -> 动作 =====");
        super.exceptionCaught(ctx, cause);
        System.out.println("==========");
    }

    /**
     * Channel -> 操作类添加 -> 动作
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("===== Channel -> 操作类添加 -> 动作 =====");
        super.handlerAdded(ctx);
        System.out.println("==========");
    }

    /**
     * Channel -> 操作类移除 -> 动作
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("===== Channel -> 操作类移除 -> 动作 =====");
        super.handlerRemoved(ctx);
        System.out.println("==========");
    }

    /**
     * Channel -> 从对等方读取消息时 -> 动作
     *
     * @param context
     * @param httpObject
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext context, HttpObject httpObject)
            throws Exception {
        System.out.println("===== Channel -> 从对等方读取消息时 -> 动作 =====");

        Channel channel = context.channel();

        if (httpObject instanceof HttpRequest) {
            //===== HTTP Request 响应
            // 远程地址
            System.out.println(channel.remoteAddress());
            //--- 设置 HTTP Response ---//
            // 消息体内容
            ByteBuf content = Unpooled.copiedBuffer("Hello Netty!", CharsetUtil.UTF_8);
            // 初始化 HTTP Response
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1
                    , HttpResponseStatus.OK
                    , content);
            // 设置 HTTP Header
            response.headers().set(HttpHeaderNames.CONTENT_TYPE
                    , "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH
                    , content.readableBytes());
            //---//
            // 缓冲区 -> 写入并刷新 <- HTTP Response
            context.writeAndFlush(response);
        }

        System.out.println("==========");
    }

}
