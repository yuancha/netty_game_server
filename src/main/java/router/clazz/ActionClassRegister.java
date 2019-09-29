package router.clazz;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Iterator;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import router.ActionRegister;
import util.ScanUtil;

/**
 * Action处理类注册器
 */
public class ActionClassRegister extends ActionRegister {
	private static final Logger log = LoggerFactory.getLogger(ActionClassRegister.class);
	
	/*
	 * @param initParam 参数1：使用了Module注解的Action类包路径，如：action
	 * 					参数2：是否使用spring初始化Action类（可选，默认为false）
	 */
	@Override
	public void init(String actionPackagePath) {
		try {
			Iterator it = ScanUtil.scan(actionPackagePath, Module.class).iterator();

			while (it.hasNext()) {
				Class clazz = (Class) it.next();
				Object obj = clazz.newInstance();
				Module ann = (Module) clazz.getAnnotation(Module.class);
				log.debug("Action Register--->Action:[{}]", clazz.getName());
				if (ann == null || ann.value() <= 0) {
					throw new Exception("通讯模块初始化失败，模块编号未指定，" + clazz.getName());
				}
				int moduleId = ann.value();
				
				FastClass cglibBeanClass = FastClass.create(clazz);
				
				Method[] mList = clazz.getMethods();
				for (Method m : mList) {
					RequestModule reqMAnn = m.getAnnotation(RequestModule.class);
					PushModule pushMAnn = m.getAnnotation(PushModule.class);
					if (reqMAnn != null && reqMAnn.value() > 0) {// 是请求类Action
						int command = reqMAnn.value();//对应proto生成的MessageID里面的枚举变量
						
						ActionClassHandler hasHandler = (ActionClassHandler) commandActionMap.get(command);
						if (hasHandler != null) {
							throw new Exception(MessageFormat.format(
									"通讯模块初始化失败，模块<{0}, {1}, {2}>与<{3}, {4}, {5}>重复", 
									command, clazz.getName(), m.getName(),
									hasHandler.getCommand(), hasHandler.getAction().toString(), hasHandler.getMethod().getName() 
									));
						}
						FastMethod fastM = cglibBeanClass.getMethod(m);
						hasHandler = new ActionClassHandler(command, obj, fastM);
						hasHandler.setNeedOnline(reqMAnn.needOnline());
						log.debug("Action Register--->OpCode:[{}]",command);
						commandActionMap.put(command, hasHandler);
					} else if (pushMAnn != null && pushMAnn.value() > 0 && !pushMAnn.pushId().equals("")) {// 是推送类Action
						int subModuleId = pushMAnn.value();
						int command = moduleId * MODULE_BASE + subModuleId;
						String pushId = pushMAnn.pushId();

						ActionClassHandler hasHandler = (ActionClassHandler) commandActionMap.get(pushId);
						if (hasHandler != null) {
							throw new Exception(MessageFormat.format(
									"推送模块初始化失败，模块<{0}, {1}, {2}>与<{3}, {4}, {5}>重复", 
									command, clazz.getName(), m.getName(),
									hasHandler.getCommand(), hasHandler.getAction().toString(), hasHandler.getMethod().getName() 
									));
						}
						hasHandler = (ActionClassHandler) pushActionMap.get(pushId);
						if (hasHandler != null) {
							throw new Exception(MessageFormat.format(
									"推送模块初始化失败，模块<{0}, {1}, {2}>与<{3}, {4}, {5}>重复", 
									pushId, clazz.getName(), m.getName(),
									hasHandler.getPushId(), hasHandler.getAction().toString(), hasHandler.getMethod().getName() 
									));
						}
						FastMethod fastM = cglibBeanClass.getMethod(m);
						hasHandler = new ActionClassHandler(command, pushId, obj, fastM);
						hasHandler.setNeedOnline(pushMAnn.needOnline());
						commandActionMap.put(command, hasHandler);
						pushActionMap.put(pushId, hasHandler);
					}
				}
			}
			log.info("Action注册完毕!");
		} catch (Exception e) {
			log.error("Action注册时异常!",e);			
			e.printStackTrace();
		}
	}

}
