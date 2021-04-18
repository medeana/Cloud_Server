package com.khizriev.chat.server;

import com.khizriev.chat.client.Command;
import com.khizriev.chat.server.Exception.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;



public class MainHandler extends SimpleChannelInboundHandler<String> {

    private static final List<Channel> channels = new ArrayList<>();
    private  String clientName;
    private static int newClientIndex = 1;

    private boolean needFeedBack;
    private UserDAO userDAO;
    private Path userRoot;
    private static final Path ROOT_SERVER = Paths.get("");
    private Path currentDir;


    public MainHandler(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Клиент подключился " + ctx);
        needFeedBack = true;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ch, String s) throws Exception {
        System.out.println("Получено сообщение:" + s);
        String out = String.format("[%s]: %s\n", clientName, s);
        for(Channel c: channels){
            c.writeAndFlush(out);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Command) {
            Command cmd = (Command) msg;
            switch (cmd.getType()) {
                case AUTH: {
                    try {
                        AuthInfo authInfo = new AuthInfo(cmd.getArgs());

                        if (userDAO.checkUser(authInfo.getName(), authInfo.getPass())) {
                            userRoot = ROOT_SERVER.resolve(authInfo.getName());
                            if (!Files.exists(userRoot)) {
                                Files.createDirectory(userRoot);
                            }
                            currentDir = Paths.get(userRoot.toString());

                            ctx.channel().writeAndFlush(Command.generate(Command.CommandType.AUTH_OK, currentDir.toString()));
                        } else {
                            ctx.channel().writeAndFlush(Command.generate(Command.CommandType.ERROR, "Не правильный логин, пароль."));
                        }
                    } catch (UserLoginException
                            | UserPassException
                            | DBConnectException
                            | ReadResultSetException e) {
                        ctx.channel().writeAndFlush(Command.generate(Command.CommandType.ERROR, e.getMessage()));
                    }
                    break;
                }

                case REGISTER: {
                    try {
                        userDAO.registerUser(cmd.getArgs()[0], cmd.getArgs()[1]);
                        userRoot = ROOT_SERVER.resolve(cmd.getArgs()[0]);
                        if (!Files.exists(userRoot)) {
                            Files.createDirectory(userRoot);
                        }
                        currentDir = Paths.get(userRoot.toString());
                        ctx.channel().writeAndFlush(Command.generate(Command.CommandType.AUTH_OK, currentDir.toString()));
                    } catch (UserExistsException e) {
                        ctx.channel().writeAndFlush(Command.generate(Command.CommandType.ERROR, e.getMessage()));
                    }
                    break;
                }
            }
        }
    }
}
