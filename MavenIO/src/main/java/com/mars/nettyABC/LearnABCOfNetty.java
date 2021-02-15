package com.mars.nettyABC;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;

public class LearnABCOfNetty {

    @Test
    public void whatIsEventLoopGroup() throws IOException {
        /*
        NioEventLoopGroup 是一个 Executor、Selector
         */
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(2);
        nioEventLoopGroup.execute(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    System.out.println("task 1");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        nioEventLoopGroup.execute(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    System.out.println("task 2");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        System.in.read();
    }

    @Test
    public void originNettyClient() throws Exception {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup(1);
        NioSocketChannel socketChannel = new NioSocketChannel();
        // epoll_ctl
        eventLoopGroup.register(socketChannel);

        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new MyInHandler());

        ChannelFuture connect = socketChannel.connect(new InetSocketAddress("127.0.0.1", 9090));
        ChannelFuture sync = connect.sync();

        ByteBuf buf = Unpooled.copiedBuffer("hello server".getBytes());
        ChannelFuture send = socketChannel.writeAndFlush(buf);
        send.sync();

        sync.channel().closeFuture().sync();
    }

    @Test
    public void originNettyServer() throws Exception {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup(1);
        NioServerSocketChannel serverSocketChannel = new NioServerSocketChannel();
        // epoll_ctl
        eventLoopGroup.register(serverSocketChannel);
        ChannelPipeline pipeline = serverSocketChannel.pipeline();
        pipeline.addLast(new MyAcceptHandler(eventLoopGroup, new ChannelInit()));

        ChannelFuture bind = serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 9090));
        bind.sync().channel().closeFuture().sync();
    }

    @Test
    public void nettyClient() throws Exception {
        NioEventLoopGroup loopGroup = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture connect = bootstrap.group(loopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new MyInHandler());
                    }
                })
                .connect(new InetSocketAddress("127.0.0.1", 9090));
        Channel channel = connect.sync().channel();
        ByteBuf buf = Unpooled.copiedBuffer("hello server".getBytes());
        ChannelFuture send = channel.writeAndFlush(buf);
        send.sync();

        channel.closeFuture().sync();
    }

    @Test
    public void nettyServer() throws Exception {
        NioEventLoopGroup loopGroup = new NioEventLoopGroup(1);
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        ChannelFuture bind = serverBootstrap.group(loopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new MyInHandler());
                    }
                })
                .bind(new InetSocketAddress("127.0.0.1", 9090));
        bind.sync().channel().closeFuture().sync();
    }

    private class MyInHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            System.out.println("Client register...");
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("Client active...");
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf byteBuf = (ByteBuf) msg;
            CharSequence charSequence = byteBuf.getCharSequence(0, byteBuf.readableBytes(), CharsetUtil.UTF_8);
            System.out.println("Get data: " + charSequence);
            ctx.writeAndFlush(byteBuf);
        }
    }

    private class MyAcceptHandler extends ChannelInboundHandlerAdapter {

        private final NioEventLoopGroup loopGroup;
        private final ChannelHandler handler;

        public MyAcceptHandler(NioEventLoopGroup loopGroup, ChannelHandler handler) {
            this.loopGroup = loopGroup;
            this.handler = handler;
        }

        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            System.out.println("Server register...");
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("Server active...");
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            SocketChannel socketChannel = (SocketChannel) msg;
            ChannelPipeline pipeline = socketChannel.pipeline();
            pipeline.addLast(handler); // 需要一个 @Sharable 的 handler
            loopGroup.register(socketChannel);
        }
    }

    /**
     * 前置 handler，不涉及业务，不含有私有属性
     */
    @ChannelHandler.Sharable
    private class ChannelInit extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            Channel channel = ctx.channel();
            ChannelPipeline pipeline = channel.pipeline();
            pipeline.addLast(new MyInHandler());
            pipeline.remove(this);
        }
    }
}
