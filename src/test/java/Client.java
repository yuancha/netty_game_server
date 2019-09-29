import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import netty.codec.ProtocDecoder;
import netty.codec.ProtocEncoder;
import netty.codec.ProtocVarint32FrameDecoder;
import netty.codec.ProtocVarint32LengthFieldPrepender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import router.MessageMapper;

import java.util.concurrent.TimeUnit;

public class Client {
    private static final Logger log = LoggerFactory.getLogger(Client.class);

    private int port;
    private String host;
    private Bootstrap bstrap;
    private EventLoopGroup group;
    private Channel channel;
    private ChannelFuture future;
    private MessageMapper msgMapper;

    public Client(int port, String host) {
        this.port = port;
        this.host = host;
        msgMapper = new MessageMapper("proto.message");
    }

    public void start() throws Exception {
        // 配置客户端NIO线程组
        group = new NioEventLoopGroup();
        bstrap = new Bootstrap();
        bstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        //配置Protobuf编码工具ProtobufVarint32LengthFieldPrepender与ProtobufEncoder
                        ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                        ch.pipeline().addLast(new ProtocDecoder(msgMapper));

                        ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                        ch.pipeline().addLast(new ProtocEncoder(msgMapper));
                        ch.pipeline().addLast(new ClientHandler(Client.this));
                    }
                });
        connect();
    }

    public void connect()  {
        if (null != channel && channel.isActive()) {
            return;
        }
        future = bstrap.connect(host, port);
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture f) throws Exception {
                if (f.isSuccess()) {
                    channel = future.channel();
                    log.debug("远程连接成功");
                }
//                else {
//                    log.error("failed to connect to server, port : {}, host : {}", port, host);
//                    f.channel().eventLoop().schedule(new Runnable() {
//                        @Override
//                        public void run() {
//                            log.debug("尝试重连");
//                            connect();
//                        }
//                    }, 2, TimeUnit.SECONDS);
//                }
            }
        });
//        try {
//
//            future.channel().closeFuture().sync();
//        } finally {
//            group.shutdownGracefully();
//        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8000;
        new Client(port, "127.0.0.1").start();
    }
}
