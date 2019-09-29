package router;

/**
 * 通讯接口Action处理类信息
 */
public abstract class ActionHandler {
	/** 请求/推送消息的命令 */
	protected int command;
	/** 推送消息处理Action的标识 */
	protected String pushId;
	/** 执行该Action处理时，须作在线或登录验证 */
	protected boolean needOnline = true;
	
	/**
	 * 执行Action数据通讯处理
	 * @param params(session[0],Packet)
	 */
	public abstract void executeAction(Object[] params);
	
	
	public int getCommand() {
		return command;
	}
	public void setCommand(int command) {
		this.command = command;
	}
	public String getPushId() {
		return pushId;
	}
	public void setPushId(String pushId) {
		this.pushId = pushId;
	}
	public boolean isNeedOnline() {
		return needOnline;
	}
	public void setNeedOnline(boolean needOnline) {
		this.needOnline = needOnline;
	}
}
