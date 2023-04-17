package org.helloframework.gateway.core.netty.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.helloframework.gateway.common.netty.IdleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lanjian
 */
@ChannelHandler.Sharable
public class NettyServerIdleStateTrigger extends ChannelInboundHandlerAdapter {
    private final static Logger log = LoggerFactory.getLogger(NettyServerIdleStateTrigger.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                log.debug("WRITER_IDLE");
                throw new IdleException("WRITER_IDLE exception");
            }
            if (state == IdleState.READER_IDLE) {
                log.debug("READER_IDLE");
                throw new IdleException("READER_IDLE exception");
            }
        }
    }
}
