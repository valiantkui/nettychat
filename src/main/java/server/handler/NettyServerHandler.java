package server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Iterator;

public class NettyServerHandler extends SimpleChannelInboundHandler<String> {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        ctx.writeAndFlush("hello client");
        channelGroup.writeAndFlush("[客户端] "+ channel.remoteAddress() + " 上线了 \n");
        channelGroup.add(channel);
        System.out.println("客户端 "+ channel.remoteAddress() + " 上线了");
    }


    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        Channel channel = ctx.channel();
        Iterator<Channel> iterator = channelGroup.iterator();
        while(iterator.hasNext()){
            Channel next = iterator.next();
            if(next.remoteAddress() != channel.remoteAddress()){
                next.writeAndFlush(channel.remoteAddress()+" : "+s);
            }
        }

        System.out.println(s);
    }


}
