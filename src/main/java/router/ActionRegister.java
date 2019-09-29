package router;

import java.util.HashMap;
import java.util.Map;

import router.clazz.ActionClassRegister;

/**
 * 通讯接口Action处理器注册/管理类
 */
public abstract class ActionRegister {
	protected static final int MODULE_BASE = 1000;
	private static final ActionClassRegister classRegister = new ActionClassRegister();

	protected Map<Integer, ActionHandler> commandActionMap = new HashMap<>();
	protected Map<String, ActionHandler> pushActionMap = new HashMap<String, ActionHandler>();
	 
	/**
	 * 获取默认Action注册/管理类
	 * @return
	 */
	public static ActionRegister getRegister() {
		return classRegister;
	}
	
	/**
	 * <pre>
	 * 初始化，应用程序启动时调用
	 * Class方式：参数1：使用了Module注解的Action类包路径，如：com.ssgn.testgame.action
	 * 	参数2：是否使用spring初始化Action类（可选，默认为false）
	 * Script方式：3个参数：脚本文件路径、脚本可用扩展函数类路径、脚本类型(后缀名，可选参数)
	 * </pre>
	 */
	public abstract void init(String actionPackagePath);
	
	/**
	 * 通过命令编号查找通讯请求服务
	 * @param command 命令编号，如：100001
	 * @return
	 */
	public ActionHandler getRequestHandler(int command) {
		return commandActionMap.get(command);
	}
	
	/**
	 * 通过推送ID查找消息推送服务
	 * @param pushId 推送ID，如：pushRole
	 * @return
	 */
	public ActionHandler getPushHandler(String pushId){
		return pushActionMap.get(pushId);
	}
}
