package router.clazz;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于定义请求-响应类通讯接口子模块<br/>
 * 提供一个int值，指定子模块的命令编号，如：1<br/>
 * 将与主模块命令组合成完整的请求命令，如：与主模块命令100组合为100001
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestModule {
	/** 子模块的命令编号，如：1 */
	int value();
	/** 需要在线才能执行该请求 */
	boolean needOnline() default true;
}
