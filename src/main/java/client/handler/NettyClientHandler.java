package client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyClientHandler extends SimpleChannelInboundHandler<String> {


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().writeAndFlush("channelRegistered");
        System.out.println("0");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive");
        ctx.channel().writeAndFlush("my name is 张三");
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println(s.trim());
    }
}
