package ru.khizriev.storage.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.khizriev.storage.client.interf.CallBack;

public class CommandInboundHandler extends ChannelInboundHandlerAdapter {

    private CallBack onMessageReceivedCallBack;

    public CommandInboundHandler(CallBack onMessageReceivedCallBack) {
        this.onMessageReceivedCallBack = onMessageReceivedCallBack;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (onMessageReceivedCallBack != null) {
            onMessageReceivedCallBack.callBack(msg);
        }
    }
}
