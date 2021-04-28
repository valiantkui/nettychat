package client;

import client.handler.NettyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class NettyClient {

    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new StringDecoder());
                            socketChannel.pipeline().addLast(new StringEncoder());
                            socketChannel.pipeline().addLast(new NettyClientHandler());
                        }
                    });

            System.out.println("netty client start");

            //启动客户端去连接服务器端
            ChannelFuture cf = bootstrap.connect("127.0.0.1", 9000).sync();
            Scanner scan = new Scanner(System.in);
            while (scan.hasNextLine()){
                String s = scan.nextLine();

                cf.channel().writeAndFlush(s);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }

    }
}
