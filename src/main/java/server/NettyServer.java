package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import server.handler.NettyServerHandler;

public class NettyServer {
    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast("decoder",new StringDecoder());
                            socketChannel.pipeline().addLast("encoder",new StringEncoder());
                            socketChannel.pipeline().addLast(new NettyServerHandler());
                        }
                    });

            System.out.println("netty server start。。");

            ChannelFuture cf = bootstrap.bind(9000).sync();

            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
