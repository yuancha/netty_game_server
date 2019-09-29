package router.clazz;
 
import com.google.protobuf.MessageLite;
import net.sf.cglib.reflect.FastMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import router.ActionHandler;

/**
 * 通讯接口Action处理信息
 */
public class ActionClassHandler extends ActionHandler {
	private static final Logger log = LoggerFactory.getLogger(ActionClassHandler.class);
	/** 模块的处理类实例 */
	private Object action;
	/** 模块的处理方法定义 */
	private FastMethod method;
	
	ActionClassHandler(int command, Object action,FastMethod method) {
		super();
		this.command = command;
		this.action = action;
		this.method = method;
		
	}
	ActionClassHandler(int command, String pushId, Object action, FastMethod method) {
		super();
		this.command = command;
		this.pushId = pushId;
		this.action = action;
		this.method = method;
	}
	
	@Override
	public void executeAction(Object[] params) {
//		Integer msgId = null;
//		MessageLite req = null;
//		for (Object param : params) {
//			if (param instanceof MessageLite) {
//				//TODO 获得消息id
//				req =(MessageLite) param;
//				Integer tmpMsgId = 100;
//				if (tmpMsgId != null && tmpMsgId != 0) {
//					msgId = tmpMsgId;
//					break;
//				}
//			}
//		}
		try {// 运行该Action处理
			method.invoke(action, params);
		} catch (Exception e) {
			log.error("action execute exception pkt_{}", e);
		}
	}
	
	public Object getAction() {
		return action;
	}
	public void setAction(Object action) {
		this.action = action;
	}
	public FastMethod getMethod() {
		return method;
	}
	public void setMethod(FastMethod method) {
		this.method = method;
	}
	
}
