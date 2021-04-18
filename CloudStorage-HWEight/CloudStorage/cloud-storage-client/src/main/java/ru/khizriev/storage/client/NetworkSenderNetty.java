package ru.khizriev.storage.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.khizriev.storage.client.handler.CommandInboundHandler;
import ru.khizriev.storage.client.helper.Command;
import ru.khizriev.storage.client.helper.FileWrapper;
import ru.khizriev.storage.client.interf.CallBack;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class NetworkSenderNetty {

    private static final Logger LOGGER = LogManager.getLogger(NetworkSenderNetty.class.getName());

    private SocketChannel channel;

    public NetworkSenderNetty(CallBack onMessageReceivedCallBack) {
        Thread t = new Thread(() -> {
            EventLoopGroup workerGroup = new NioEventLoopGroup();

            try {
                Bootstrap bsb = new Bootstrap();
                bsb.group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                channel = socketChannel;
                                socketChannel.pipeline().addLast(
                                        new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                        new ObjectEncoder(),
                                        new CommandInboundHandler(onMessageReceivedCallBack)

                                );
                            }
                        });
                ChannelFuture future = bsb.connect("localhost", 8189).sync();
                LOGGER.log(Level.INFO, "Channel connected");
                future.channel().closeFuture().sync();
            } catch (Exception e) {
                LOGGER.log(Level.ERROR, e);
            } finally {
                workerGroup.shutdownGracefully();
                LOGGER.log(Level.INFO, "PipeLine group close.");
            }
        });
        t.setDaemon(true);
        t.start();
        LOGGER.log(Level.INFO, "Service Start");
    }

    public void sendCommand(Command command) {
        try {
            channel.writeAndFlush(command).sync();
        } catch (InterruptedException e) {
            LOGGER.log(Level.ERROR, e);
        }
    }

    public void sendFile(Path filePath, Command.CommandType type) {
        try {
            if (Files.size(filePath) == 0) {
                sendCommand(Command.generate(Command.CommandType.TOUCH, filePath.getFileName().toString()));
                return;
            }
            FileInputStream fis = new FileInputStream(filePath.toString());
            int read;
            int part = 1;
            FileWrapper fw = new FileWrapper(filePath, type, false);

            while ((read = fis.read(fw.getBuffer())) != -1) {
                fw.setCurrentPart(part);
                fw.setReadByte(read);
                channel.writeAndFlush(fw).sync();
                part++;
            }
            fis.close();
            if (type == Command.CommandType.MOVE) {
                Files.delete(filePath);
                LOGGER.log(Level.INFO, "Delete after send complete: " + fw.toString());
            }
        } catch (IOException | InterruptedException e) {
            LOGGER.log(Level.ERROR, e);
        }

    }

    public void close() {
        channel.close();
        LOGGER.log(Level.INFO, "Channel close");
    }
}
