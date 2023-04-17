package org.helloframework.gateway.core.netty.client;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.helloframework.gateway.common.definition.Constants;
import org.helloframework.gateway.common.netty.IdleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lanjian
 */
@ChannelHandler.Sharable
public class NettyClientIdleStateTrigger extends ChannelInboundHandlerAdapter {
    private final static Logger log = LoggerFactory.getLogger(NettyClientIdleStateTrigger.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                log.warn("READER_IDLE");
                throw new IdleException("READER_IDLE exception");
            }
            if (state == IdleState.ALL_IDLE) {
                log.debug("ALL_IDLE 发送心跳");
                ctx.channel().writeAndFlush(Constants.PING_INFO);
            }
        }
    }
}
