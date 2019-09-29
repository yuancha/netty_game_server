package session;

import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLiteOrBuilder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameIOSession {

    private static final Logger log = LoggerFactory.getLogger(GameIOSession.class);
    public static final AttributeKey<GameIOSession> sessionKey = AttributeKey.newInstance("game_session");
    private ChannelHandlerContext ctx;
    private int sessionId;
    private String remoteip;
    private String token;

    public GameIOSession(ChannelHandlerContext ctx, int sessionId) {
        this.ctx = ctx;
        this.sessionId = sessionId;
        Attribute<GameIOSession> attr = this.ctx.channel().attr(sessionKey);
        attr.set(this);

        try {
            remoteip=((NioSocketChannel)(ctx.channel())).remoteAddress().getAddress().getHostAddress();
            log.debug("GameIOSession remoteip : {}", remoteip);
        } catch (Exception e) {
            log.error("new GameIOSession error, " , e);
        }
    }

    public static GameIOSession getGameIOSession(ChannelHandlerContext ctx) {
        Attribute<GameIOSession> attr = ctx.channel().attr(sessionKey);
        return attr.get();
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void sendMsg(MessageLiteOrBuilder msg) {
        if (ctx != null) {
            //msgRecordManager.addMsgRecord((MessageLite) msg);
            ctx.write(msg, ctx.voidPromise());
        }
    }

    public void flushMsg() {
        if (ctx != null) {
            ctx.flush();
        }
    }

    public boolean sendMsgAndFlush(MessageLiteOrBuilder msg) {
        if (ctx != null) {
            if (ctx.channel().isWritable()) {
                ctx.writeAndFlush(msg);
                //msgRecordManager.addMsgRecord((MessageLite) msg);
                return true;
            } else {
                log.error("NO_WRITABLE " + this);
            }
        }
        return false;
    }


















    public int getSessionId() {
        return sessionId;
    }

    public String getRemoteip() {
        return remoteip;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
