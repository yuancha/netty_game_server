import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import com.google.protobuf.MessageLite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import router.ActionHandler;
import router.ActionRegister;
import router.MessageMapper;
import session.GameIOSession;
import session.OnlineManager;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Handler implementation for the echo server.
 */
public class GameHandler extends ChannelInboundHandlerAdapter {

	private static final Logger log = LoggerFactory.getLogger(GameHandler.class);

	private static AtomicInteger sessincr = new AtomicInteger();
	private MessageMapper messageMapper;
	private ActionRegister actionMapper;
	
	public GameHandler(MessageMapper messageMapper, ActionRegister actionMapper) {
		super();
		this.messageMapper = messageMapper;
		this.actionMapper = actionMapper;
	}

	public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
		try {
			new GameIOSession(ctx, sessincr.incrementAndGet());
			Map<Integer, GameIOSession> sessMap = OnlineManager.getInstance().getSessMap();
			GameIOSession sess = GameIOSession.getGameIOSession(ctx);
			sessMap.put(sess.getSessionId(), sess);
			log.debug("init session, sid : {}", sess.getSessionId());
		} catch(Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		final GameIOSession session = GameIOSession.getGameIOSession(ctx);
		if (session != null) {
			OnlineManager.getInstance().getSessMap().remove(session.getSessionId());
			log.debug("session remove, sid : {}", session.getSessionId());
		}
	}

	@Override
	public void channelRead(final ChannelHandlerContext ctx, Object msg)
			throws Exception {
		if( msg instanceof MessageLite) {
			MessageLite msg0 = (MessageLite)msg;
			int msgType = messageMapper.getTypeByMsg(msg0);
			final ActionHandler handler = actionMapper.getRequestHandler(msgType);
			if (handler == null) {
				log.error("ActionHandler not find, msgType : {}", msgType);
				return;
			}
			final GameIOSession session = GameIOSession.getGameIOSession(ctx);
			if (session == null) {
				log.error("GameIOSession not find, msgType : {}", msgType);
				return;
			}
			Object[] params = {session, msg0};
			handler.executeAction(params);
		}
	}

}
