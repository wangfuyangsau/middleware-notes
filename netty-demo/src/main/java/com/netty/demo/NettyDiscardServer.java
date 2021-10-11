package com.netty.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyDiscardServer {
    private final int serverPort;
    ServerBootstrap b = new ServerBootstrap();
    public NettyDiscardServer(int port) {
        this.serverPort = port;
    }
    public void runServer(){
            //创建反应器线程组
            EventLoopGroup bossLoopGroup = new NioEventLoopGroup(1);
            EventLoopGroup workerLoopGroup = new NioEventLoopGroup();
        try {

            //设置反应器线程组
            b.group(bossLoopGroup,workerLoopGroup);
            //nio通道
            b.channel(NioServerSocketChannel.class);
            b.localAddress(serverPort);
            //设置通道的参数
            b.option(ChannelOption.SO_KEEPALIVE,true);
            b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            //装配子通道流水线
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                //有连接到达时会创建一个通道
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                   //添加handler处理器
                    socketChannel.pipeline().addLast(new NettyDiscardHandler());
                }
            });
            //同步阻塞方法知道绑定成功
            ChannelFuture channelFuture = b.bind().sync();
            System.out.println("服务启动成功");
            //阻塞直到通道关闭
            ChannelFuture  closeFuture = channelFuture.channel().closeFuture();
            closeFuture.sync();
            System.out.println("连接关闭");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("释放资源");
            workerLoopGroup.shutdownGracefully();
            bossLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new NettyDiscardServer(9000).runServer();
    }
}
