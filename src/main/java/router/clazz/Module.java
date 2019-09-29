package router.clazz;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于定义通讯接口主模块<br/>
 * 提供一个int值，指定主模块的命令编号，如：100<br/>
 * 主模块的命令编号将乘以基数后与子模块的命令编号组成完成命令，<br/>
 * 如：100 * 1000 + 1 = 100001 表示通讯接口处理模块的完整命令为100001
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Module {
	/** 主模块的命令编号，如：100 */
	int value();
}
