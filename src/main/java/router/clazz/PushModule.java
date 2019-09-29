package router.clazz;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于定义推送类通讯接口子模块<br/>
 * 提供一个int值，指定子模块的命令编号，如：4<br/>
 * 将与主模块命令组合成完整的请求命令，如：与主模块命令100组合为100004
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PushModule {
	/** 子模块的命令编号，如：4 */
	int value();
	/** 指定一个唯一的推送ID，用于推送消息时查找处理类 */
	String pushId();
	/** 需要在线才能执行该请求 */
	boolean needOnline() default true;
}
