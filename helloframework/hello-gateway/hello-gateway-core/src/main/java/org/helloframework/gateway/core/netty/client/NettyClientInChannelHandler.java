package org.helloframework.gateway.core.netty.client;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.helloframework.gateway.common.definition.Constants;
import org.helloframework.gateway.common.definition.apiservice.ApiServiceContext;
import org.helloframework.gateway.common.definition.base.MessageHandler;
import org.helloframework.gateway.common.definition.base.MethodInfo;
import org.helloframework.gateway.common.definition.base.ServerInfo;
import org.helloframework.gateway.common.netty.IdleException;
import org.helloframework.gateway.common.utils.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lanjian
 */
@ChannelHandler.Sharable
public class NettyClientInChannelHandler extends ChannelInboundHandlerAdapter {
    private final static Logger log = LoggerFactory.getLogger(NettyClientInChannelHandler.class);
    private MessageHandler messageHandler = null;
    private ServerInfo serverInfo = null;
    private Collection<MethodInfo> methods = null;
    private ApiServiceContext apiServiceContext = null;
    private ExecutorService executorService = null;

    public NettyClientInChannelHandler(ApiServiceContext apiServiceContext, ServerInfo serverInfo, MessageHandler messageHandler, Collection<MethodInfo> methods) {
        this.apiServiceContext = apiServiceContext;
        this.messageHandler = messageHandler;
        this.serverInfo = serverInfo;
        this.methods = methods;
        this.executorService = Executors.newFixedThreadPool(this.apiServiceContext.getWorkThreads(), new NamedThreadFactory(Constants.CLIENT_WORK_THREAD_NAME + serverInfo.toString(), true));
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.debug("数据解绑 serverInfo is {}", serverInfo.toString());
        apiServiceContext.getClientContext().unRegister(serverInfo, methods);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        byte[] message = (byte[]) msg;
        if (Arrays.equals(Constants.EXIT_INFO, message)) {
            log.debug("该节点下线");
            apiServiceContext.getClientContext().unRegister(serverInfo, methods);
            return;
        }
//        if (Arrays.equals(Constants.REMOVE_INFO, message)) {
//            log.debug("该节点异常下线");
//            apiServiceContext.getClientContext().unRegister(serverInfo, methods);
//            apiServiceContext.getClientContext().remove(serverInfo);
//            return;
//        }
        if (Arrays.equals(Constants.BACK_INFO, message)) {
            log.debug("收到回心跳.....");
            return;
        }
        channelReadHandler(message, ctx);
    }


    public void channelReadHandler(byte[] message, final ChannelHandlerContext ctx) throws Exception {
        executorService.execute(new NettyClientInvoker(message, messageHandler));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        if (cause instanceof IdleException) {
            log.error("exceptionCaught", cause);
            ctx.close();
        } else {
            log.warn("exceptionCaught", cause);
        }
    }
}
