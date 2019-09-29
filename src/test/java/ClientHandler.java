import com.google.protobuf.MessageLite;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proto.message.CSLogin1000;
import proto.message.HeartBeat1;
import proto.message.SCLogin1001;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(ClientHandler.class);

    private Client client;

    public ClientHandler(Client client) {
        this.client = client;
    }

    /**
     * channel建立之后,向服务端发送消息
     * 需要注意的是这里写入的消息是完整的UserInfo.User对象
     * 因为后续会被工具ProtobufVarint32LengthFieldPrepender与ProtobufEncoder进行编码处理
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        //channel建立之后,向服务端发送消息,需要注意的是这里写入的消息是完整的UserInfo.User对象
        //因为后续会被工具ProtobufVarint32LengthFieldPrepender与ProtobufEncoder进行编码处理
        CSLogin1000 user = CSLogin1000.newBuilder()
                .setAccount("yuanc")
                .setPassword("123456").build();

        HeartBeat1.Builder hb = HeartBeat1.newBuilder();

        ctx.writeAndFlush(user);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent)evt;

        switch (event.state()){
            case READER_IDLE:
                break;
            case WRITER_IDLE: {
                log.debug("客户端写空闲");
                //ctx.write(LoginMsg.HeartBeat.getDefaultInstance());
            }  break;
            case ALL_IDLE:
                break;
            default: break;
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof MessageLite) {
            log.debug("收到返回消息");
            SCLogin1001 msg0 = (SCLogin1001)msg;
            System.out.println(msg0.getStatus());
            System.out.println(msg0.getMessage());
        }
    }
}
