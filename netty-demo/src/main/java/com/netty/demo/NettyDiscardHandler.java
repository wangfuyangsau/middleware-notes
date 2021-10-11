package com.netty.demo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyDiscardHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf  in = (ByteBuf)msg;
        System.out.println("receive msg");
        while(in.isReadable()){
            System.out.print((char)in.readByte());
        }
    }
}
