package org.helloframework.gateway.core.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.helloframework.gateway.common.definition.Constants;
import org.helloframework.gateway.common.definition.apiservice.ApiServiceContext;
import org.helloframework.gateway.common.definition.base.Client;
import org.helloframework.gateway.common.definition.base.MessageHandler;
import org.helloframework.gateway.common.definition.base.MethodInfo;
import org.helloframework.gateway.common.definition.base.ServerInfo;
import org.helloframework.gateway.common.exception.ConnectedException;
import org.helloframework.gateway.common.netty.ApiGWChannelHandler;
import org.helloframework.gateway.common.utils.StringUtils;
import org.helloframework.gateway.core.exception.MessageSendException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by lanjian
 */
public class NettyClient implements Client {
    private final static Logger log = LoggerFactory.getLogger(NettyClient.class);
    private final static ReentrantLock lock = new ReentrantLock();
    private ChannelFuture channelFuture;
    private EventLoopGroup workerGroup;
    private String host;
    private Integer port;
    private ServerInfo serverInfo;
    private Channel channel;
//    private Set<MethodInfo> methods = new HashSet<>();
    private Map<String, MethodInfo> methodInfos = new HashMap<>();
    private MessageHandler messageHandler;
    private ApiServiceContext apiServiceContext;
    private String sessionid = UUID.randomUUID().toString();
    private Integer fail = 0;

    public NettyClient(ServerInfo serverInfo, MessageHandler messageHandler, ApiServiceContext apiServiceContext) {
        this.host = serverInfo.getHost();
        this.port = serverInfo.getPort();
        this.serverInfo = serverInfo;
        this.messageHandler = messageHandler;
        this.apiServiceContext = apiServiceContext;
        if (StringUtils.isBlank(host)) {
            throw new IllegalArgumentException("host is null");
        }
        if (port == null || port <= 0) {
            throw new IllegalArgumentException("port must >0");
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NettyClient that = (NettyClient) o;

        return serverInfo.equals(that.serverInfo);
    }

    @Override
    public int hashCode() {
        return serverInfo.hashCode();
    }

    public boolean isConnected() {
        return channel == null;
    }

    public MessageHandler findMessageHandler() {
        return messageHandler;
    }

    public void addAllMethods(Collection<MethodInfo> methods) {
//        this.methods.clear();
//        this.methods.addAll(methods);
        methodInfos.clear();
        for (MethodInfo methodInfo : methods) {
            methodInfos.put(methodInfo.getName(), methodInfo);
        }
    }

    public Collection<MethodInfo> getMethods() {
        return methodInfos.values();
    }

    @Override
    public MethodInfo findMethod(String name) {
        return methodInfos.get(name);
    }

    public int fail() {
        return fail;
    }

    public ServerInfo serverInfo() {
        return serverInfo;
    }

    public void send(final Object message) {
        try {
            if (channel == null) {
                throw new ConnectedException("Failed to Connected fail");
            }
            channel.write(message);
            channel.flush();
        } catch (Exception e) {
            throw new MessageSendException("Failed to send message", e);
        }
    }

    public void connect() {
        lock.lock();
        try {
            if (apiServiceContext.getClientContext().containsServerInfo(serverInfo)) {
                return;
            }
            if (channel != null && channel.isActive()) {
                return;
            }
            final CountDownLatch latch = new CountDownLatch(1);
            final Bootstrap b = new Bootstrap();
            workerGroup = new NioEventLoopGroup(apiServiceContext.getThreads());
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, Constants.DEFAULT_CONNECT_TIMEOUT);
            b.option(ChannelOption.TCP_NODELAY, true);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            b.handler(new ApiGWChannelHandler() {
                public List firstChannel() {
                    return Arrays.asList(new IdleStateHandler(Constants.CLIENT_READ_IDLE_TIME, Constants.CLIENT_WRITE_IDLE_TIME, Constants.CLIENT_ALL_IDLE_TIME, TimeUnit.SECONDS), new NettyClientIdleStateTrigger());
                }

                @Override
                public List lastChannel() {
                    return Arrays.asList(new NettyClientInChannelHandler(apiServiceContext, serverInfo, messageHandler, methodInfos.values()));
                }

            });
            channelFuture = b.connect(host, port).sync();
            //future对象
            channelFuture.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    boolean succeed = channelFuture.isSuccess();
                    if (succeed) {
                        NettyClient.this.channel = channelFuture.channel();
                        NettyClient.this.channel.writeAndFlush(Constants.PING_INFO);
                        NettyClient.this.fail = 0;
                        latch.countDown();
                        log.debug("{} {}:{} 连接成功", sessionid, NettyClient.this.host, NettyClient.this.port);
                        apiServiceContext.getClientContext().register(serverInfo, NettyClient.this);
                    } else {
                        log.debug("{} {}:{} 连接失败", sessionid, NettyClient.this.host, NettyClient.this.port);
                    }
                }
            });
            boolean success = latch.await(Constants.DEFAULT_REGISTRY_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
            if (!success) {
                throw new ConnectedException("Failed to Connected time out");
            }
        } catch (Exception ex) {
            fail++;
            throw new ConnectedException("Failed to Connected fail", ex);
        } finally {
            lock.unlock();
        }
    }

//    /**
//     * 重连尝试n次 每次延迟5秒钟 一共重试1分钟
//     */
//    public void reconnect() {
//        try {
//            log.warn("{}:{}尝试进行重新链接,第{}次尝试!延迟5秒", NettyClient.this.host, NettyClient.this.port, RETRY_COUNT);
//            Thread.sleep(Constants.RETRY_CONNECT_TIME);
//            connect();
//        } catch (Exception ex) {
//            log.error("reconnect", ex);
//        }
//    }


    public void shutdown() {
        try {
            if (channel != null) {
                channel.close();
                channel = null;
                channelFuture = null;
            }
            if (workerGroup != null) {
                workerGroup.shutdownGracefully();
                workerGroup = null;
            }
        } catch (Exception ex) {
            log.error("shutdown", ex);
        }
    }
}
