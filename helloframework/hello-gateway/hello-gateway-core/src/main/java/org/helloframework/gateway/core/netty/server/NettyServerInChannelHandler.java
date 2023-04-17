package org.helloframework.gateway.core.netty.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.helloframework.gateway.common.definition.Constants;
import org.helloframework.gateway.common.definition.apiservice.ApiServiceContext;
import org.helloframework.gateway.common.netty.IdleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Created by lanjian
 */
@ChannelHandler.Sharable
public class NettyServerInChannelHandler extends ChannelInboundHandlerAdapter {
    private final static Logger log = LoggerFactory.getLogger(NettyServerInChannelHandler.class);
    private ApiServiceContext apiServiceContext;


    public NettyServerInChannelHandler(ApiServiceContext apiServiceContext) {
        this.apiServiceContext = apiServiceContext;
    }


    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        byte[] message = (byte[]) msg;
        if (Arrays.equals(Constants.PING_INFO, message)) {
            log.debug("收到心跳，回心跳数据");
            ctx.channel().writeAndFlush(Constants.BACK_INFO);
            return;
        }
        if (apiServiceContext.isShutdown()) {
            log.debug("shutdown，退出节点");
            ctx.channel().writeAndFlush(Constants.EXIT_INFO);
        }
        channelReadHandler(message, ctx);
    }

    public void channelReadHandler(byte[] message, final ChannelHandlerContext ctx) throws Exception {
        apiServiceContext.getExecutorService().execute(new ServiceInvoker(ctx.channel(), message, apiServiceContext));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        if (cause instanceof IdleException) {
            //TODO 重新注册所有服务 利用订阅激活client端
            log.warn("exceptionCaught", cause);
            ctx.close();
//            apiServiceContext.registerAllApiService();
        } else {
            log.error("exceptionCaught", cause);
        }
    }
}
