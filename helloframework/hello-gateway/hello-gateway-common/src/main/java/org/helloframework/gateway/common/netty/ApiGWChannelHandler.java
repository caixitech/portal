package org.helloframework.gateway.common.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lanjian
 * /**
 * 客户端的ChannelHandler集合，由子类实现，这样做的好处：
 * 继承这个接口的所有子类可以很方便地获取ChannelPipeline中的Handlers
 * 获取到handlers之后方便ChannelPipeline中的handler的初始化和在重连的时候也能很方便
 * 地获取所有的handlers
 */
public abstract class ApiGWChannelHandler extends ChannelInitializer<SocketChannel> {


    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(handlers());
    }

    public ChannelHandler[] handlers() {
        List<ChannelHandler> channelHandlers = new ArrayList<ChannelHandler>();
        channelHandlers.addAll(firstChannel());
        channelHandlers.add(new LengthFieldPrepender(4));
        channelHandlers.add(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
        channelHandlers.add(new ByteArrayDecoder());
        channelHandlers.add(new ByteArrayEncoder());
        channelHandlers.addAll(lastChannel());
        return channelHandlers.toArray(new ChannelHandler[]{});
    }

    public List lastChannel() {
        return new ArrayList();
    }

    public List firstChannel() {
        return new ArrayList();
    }

}
