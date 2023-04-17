package org.helloframework.gateway.core.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.helloframework.gateway.common.definition.Constants;
import org.helloframework.gateway.common.definition.apiservice.ApiServiceContext;
import org.helloframework.gateway.common.definition.base.Endpoint;
import org.helloframework.gateway.common.definition.registry.RegistryFactory;
import org.helloframework.gateway.common.netty.ApiGWChannelHandler;
import org.helloframework.gateway.common.utils.NamedThreadFactory;
import org.helloframework.gateway.common.utils.NetUtils;
import org.helloframework.gateway.common.utils.URL;
import org.helloframework.gateway.config.ProviderConfig;
import org.helloframework.gateway.registry.zk.ZookeeperRegistryFactory;
import org.helloframework.gateway.registry.zk.zkclient.ZkclientZookeeperTransporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * Created by lanjian
 */
public class NettyServer extends ProviderConfig implements Endpoint, InitializingBean, ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(NettyServer.class);
    private static final ApiServiceContext apiServiceContext = ApiServiceContext.newInstance();
    private EventLoopGroup parentGroup = null;
    private EventLoopGroup childGroup = null;
    private ChannelFuture channelFuture = null;
    private InetSocketAddress inetSocketAddress = null;
    private Channel channel = null;


    public void afterPropertiesSet() throws Exception {
        try {
            ready();
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                public void run() {
                    log.info("shutdowning.....");
                    shutdown();
                }
            }));
        } catch (Throwable t) {
            shutdown();
            log.error(ExceptionUtils.getStackTrace(t));
            throw new RuntimeException("启动网关失败", t);
        }
    }


    public void bind() {
        try {
            // 配置NIO线程组
            parentGroup = new NioEventLoopGroup(getThreads());// 连接线程
            childGroup = new NioEventLoopGroup(getThreads());// 处理线程组
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parentGroup, childGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            bootstrap.childHandler(new ApiGWChannelHandler() {
                public List firstChannel() {
                    return Arrays.asList(new IdleStateHandler(Constants.SERVER_READ_IDLE_TIME, Constants.SERVER_WRITE_IDLE_TIME, Constants.SERVER_ALL_IDLE_TIME, TimeUnit.SECONDS), new NettyServerIdleStateTrigger());
                }

                public List lastChannel() {
                    return Arrays.asList(new NettyServerInChannelHandler(apiServiceContext));
                }
            });
            final CountDownLatch latch = new CountDownLatch(1);
            // 绑定端口，同步等待成功
            channelFuture = bootstrap.bind(inetSocketAddress).sync();
            //future对象
            channelFuture.addListener((ChannelFutureListener) channelFuture -> {
                boolean succeed = channelFuture.isSuccess();
                if (succeed) {
                    NettyServer.this.channel = channelFuture.channel();
                    latch.countDown();
                    log.debug("启动成功");
                } else {
                    log.debug("启动失败");
                }
            });
        } catch (Throwable ex) {
            log.error(ExceptionUtils.getStackTrace(ex));
            throw new RuntimeException("bind "+inetSocketAddress.toString()+" error shutdown", ex);
        }
    }

    public void shutdown() {
        apiServiceContext.shutdown();
        //关闭
        if (channel != null) {
            channel.close();
        }
        if (parentGroup != null) {
            parentGroup.shutdownGracefully();
        }
        if (childGroup != null) {
            childGroup.shutdownGracefully();
        }
    }

    public void ready() {
        if (getRegistryConfig() == null) {
            throw new IllegalArgumentException("getRegistryConfig is null");
        }
        inetSocketAddress = new InetSocketAddress(NetUtils.ANYHOST, getPort());
        final RegistryFactory registryFactory = new ZookeeperRegistryFactory(new ZkclientZookeeperTransporter());
        apiServiceContext.setRegistryFactory(registryFactory);
        final URL url = getRegistryConfig().getUrl();
        apiServiceContext.setRegistryUrl(url);
        apiServiceContext.setHostname(NetUtils.getLocalHost());
        apiServiceContext.setPort(getPort());
        apiServiceContext.setProtocol(getProtocol());
        apiServiceContext.setExecutorService(Executors.newFixedThreadPool(getWorkThreads(), new NamedThreadFactory(Constants.SERVER_WORK_THREAD_NAME, true)));
        apiServiceContext.setWorkThreads(getWorkThreads());
        apiServiceContext.setThreads(getThreads());
        apiServiceContext.setName(getApplicationConfig().getName());
        apiServiceContext.setMax(getMax());
        apiServiceContext.setWeight(getWeight());
        apiServiceContext.serverReady(() -> bind());
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        apiServiceContext.setApplicationContext(applicationContext);
    }
}
