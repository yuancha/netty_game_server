package util;

import com.google.protobuf.MessageLite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * proto类扫描工具类
 */
public class ProtoScanUtil {
	
	private static final Logger log = LoggerFactory.getLogger(ProtoScanUtil.class);
	private static Pattern msgClassPattern = Pattern.compile("\\d+$");
	
	/**
	 * 按类使用的注解扫描
	 * @param basePackagesStr 需要扫描的包，多个包使用“,”分隔
	 * @return
	 */
	public static Map<Integer, MessageLite> scan(String basePackagesStr){
		Map<Integer, MessageLite> classes = new HashMap<>();
		if (basePackagesStr == null) return classes;
		String[] basePackages = basePackagesStr.split(",");
		for (String basePackage : basePackages) {
			String packageName = basePackage;
			if (packageName.endsWith(".")) {
				packageName = packageName
						.substring(0, packageName.lastIndexOf('.'));
			}
			String packagePath = packageName.replace('.', '/');
			try {
				Enumeration<URL> list = ProtoScanUtil.class.getClassLoader().getResources(packagePath);
				while (list.hasMoreElements()) {
					URL url = list.nextElement();
					String protocol = url.getProtocol();
					if ("file".equals(protocol)) {
						String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
						classes.putAll(scanFiles(packageName, filePath));
					} else if ("jar".equals(protocol)) {
						//classes.addAll(scanJar(packageName, url, annClazz));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return classes;
	}

	public static void main(String[] args) {
		ProtoScanUtil.scan("proto.message");
	}


	/**
	 * 扫描并加载目录中的Class文件
	 * @param packagePath 要扫描的包，如：com.game.model
	 * @param filePath 要扫描的Class文件路径，与包参数对应，如：file://D:/Common/bin/com/game/model
	 * @return
	 */
	private static Map<Integer, MessageLite> scanFiles(String packagePath, String filePath) {
		Map<Integer, MessageLite> protos = new HashMap<>();
		File dir = new File(filePath);
		if (!dir.exists() || !dir.isDirectory()) {
			return null;
		}
		File[] dirfiles = dir.listFiles();
		for (File file : dirfiles) {
			if (file.isDirectory()) {
				protos.putAll(scanFiles(packagePath + "." + file.getName(), file.getAbsolutePath()));
			} else {
				if (!file.getPath().endsWith("class") || file.getPath().contains("$")) continue;
				String className = file.getName().substring(0, file.getName().length() - 6);
				Matcher match = msgClassPattern.matcher(className);
				if (!match.find()) { continue; }
				String msgType = match.group();
				className = packagePath + '.' + className;
				try {
					Class clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
					Method method = clazz.getMethod("getDefaultInstance");
					Object inst = method.invoke(clazz);
					MessageLite msgLite = (MessageLite) inst;
					protos.put(Integer.parseInt(msgType), msgLite);
				} catch (Exception e) {
					log.error("加载类<" + className + ">出错", e);
				}
			}
		}
		return protos;
	}
	
	/**
	 * 扫描并加载JAR中的Class文件
	 * @param packagePath 要扫描的包，如：com.game.model
	 * @param url 要扫描的Jar文件路径，如：jar:file://D:/GameServer/lib/Common.jar
	 * @param annClazz 扫描的Class需使用的Runtime注解
	 * @return
	 */
	private static Set<Class> scanJar(String packagePath, URL url, Class annClazz){
		Set<Class> classes = new LinkedHashSet<>();
		packagePath = packagePath.replace('.', '/');
		JarFile jar;
		try {
			jar = ((JarURLConnection) url.openConnection()).getJarFile();
			Enumeration<JarEntry> entries = jar.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				String name = entry.getName();
				if (!name.startsWith(packagePath) || entry.isDirectory()) {
					continue;
				}

				String className = name.replace('/', '.');
				className = className.substring(0, className.length() - 6);
				try {
					Class clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
					Annotation ann = clazz.getAnnotation(annClazz);
					if (ann != null) {
						classes.add(clazz);
					}
				} catch (Exception e) {
					log.error("从包<" + url + ">加载类<" + className + ">出错", e);
				}
			}
		} catch (IOException e) {
			log.error("读取包<" + url + ">出错", e);
		}
		return classes;
	}
}
