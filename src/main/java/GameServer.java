import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import netty.codec.ProtocDecoder;
import netty.codec.ProtocEncoder;
import netty.codec.ProtocVarint32FrameDecoder;
import netty.codec.ProtocVarint32LengthFieldPrepender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import router.ActionRegister;
import router.MessageMapper;
import router.clazz.ActionClassRegister;

public class GameServer {
	private Logger logger = LoggerFactory.getLogger(GameServer.class);
	private int port;

	private MessageMapper msgMapper;
	private ActionRegister actionMapper;
	public void init(int port) {
	    this.port = port;
        msgMapper = new MessageMapper("proto.message");
        actionMapper = new ActionClassRegister();
        actionMapper.init("action");
    }

	private EventLoopGroup bossGroup;// 1线程
	private EventLoopGroup workerGroup;// cpu核数*2 个线程
	private EventLoopGroup logicGroup;// 业务执行线程
	private ServerBootstrap serverBootstrap;

	public void start() {
		bossGroup = new NioEventLoopGroup(1);// 1线程
		workerGroup = new NioEventLoopGroup();// cpu核数*2 个线程
		logicGroup = new DefaultEventLoopGroup(100);
		try {
			serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .option(ChannelOption.SO_REUSEADDR, true)
             .option(ChannelOption.SO_RCVBUF, 10240)
             .option(ChannelOption.SO_SNDBUF, 30480)
             .option(ChannelOption.TCP_NODELAY, true)
             .option(ChannelOption.SO_LINGER, 0)
             .option(ChannelOption.SO_BACKLOG, 1000)
             .childOption(ChannelOption.SO_KEEPALIVE, true)
             //.handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ChannelPipeline pipeline = ch.pipeline();
                     //pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                     pipeline.addLast(new ProtobufVarint32FrameDecoder());
                     pipeline.addLast(new ProtocDecoder(msgMapper));

                     pipeline.addLast(logicGroup,new ProtobufVarint32LengthFieldPrepender());
                     pipeline.addLast(logicGroup,new ProtocEncoder(msgMapper));
					 pipeline.addLast(logicGroup, new GameHandler(msgMapper, actionMapper));
                 }
             });
            // Start the server.
			ChannelFuture f = serverBootstrap.bind(port).sync();
			if (f.isSuccess()) {
				logger.info("GameServer start listen port:{}", port);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void stop() {
		// Shut down all event loops to terminate all threads.
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        logicGroup.shutdownGracefully();
	}

    public static void main(String[] args) {
        GameServer server = new GameServer();
        server.init(8000);
        server.start();
    }
}
