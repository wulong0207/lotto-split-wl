package com.hhly.lottosplit.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;


/**
 * @author Bruce Liu
 * @create on: 2016-5-11  下午05:25:04
 * @describe : used it to get/set Properties' value
 */
/**
 * @desc 获取配置文件中的属性值
 * @author Bruce
 * @date 2017年1月19日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public class PropertyUtil {
	private static Properties prop = null;
	
	public static final Logger LOGGER = LoggerFactory.getLogger(PropertyUtil.class);
	
	private static final String SYS_PROPERTIES_PATH = "application.properties";

	public static String getConfigValue(String key,Object ... arguments) {
		prop = getPropertiesInstance();
		try {
			if (prop != null) {
				String msg = MessageFormat.format(new String(prop.getProperty(key)),arguments);
				return msg;
			} else {
				return "";
			}
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
		}
		return "";
	}
	
	
	
	public static String getConfigValue(String key) {
		prop = getPropertiesInstance();
		try {
			if (prop != null) {
				String msg = new String(prop.getProperty(key));
				return msg;
			} else {
				return "";
			}
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
		}
		return "";
	}
	

	public static void setConfigValue(String key, String value) {
		prop = getPropertiesInstance();
		if (prop != null) {
			prop.setProperty(key, value);
		}
		
	}
	
	/**
	 * @return
	 * 加载系统配置文件
	 */
	private static Properties getPropertiesInstance() {
		if (prop == null) {
			prop = new Properties();
		} else {
			return prop;
		}
		try {
//			prop.load(PropertyUtil.class.getClassLoader().getResourceAsStream(SYS_PROPERTIES_PATH));
			prop.load(new InputStreamReader(PropertyUtil.class.getClassLoader().getResourceAsStream(SYS_PROPERTIES_PATH), "UTF-8"));
		} catch (IOException ex) {
			LOGGER.error(ex.getMessage());
			return null;
		}
		return prop;
	}
	/**
	 * 读取配置文件值
	 * @param fileName 文件名
	 * @param key
	 * @return 
	 */
	public static String getPropertyValue(String fileName,String key){
		try {
			String value = PropertiesLoaderUtils.loadAllProperties(fileName).getProperty(key);
			if("".equals(value) || value == null){
				return "";
			}
			return new String(value.getBytes("ISO-8859-1"), "UTF-8");
		} catch (IOException e) {
			LOGGER.error("读取配置文件错误", e);
			return null;
		}
	}
}
