package com.hhly.lottosplit.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @auth lgs on
 * @date 2017/2/6.
 * @desc 对Object对象进行处理
 * @compay 益彩网络科技有限公司
 * @vsersion 1.0
 */
public class ObjectUtil extends ObjectUtils{
	
	
	
	/**
	 * 判断字符串为空
	 * @param string
	 * @return
	 */
	public static boolean isBlank(String string){
		return StringUtils.isBlank(string);
	}
	
	
	/**
	 * 判断LONG,空值
	 * @param l
	 * @return
	 * null/0 (true)
	 * 
	 */
	public static boolean isBlank(Long l){
		return null == l || l.equals(0L);
	}
	
	/**
	 * 判断Integer,空值
	 * @param l
	 * @return
	 * null/0 (true)
	 * 
	 */
	public static boolean isBlank(Integer i){
		return null == i || i.equals(0);
	}
	
	
	/**
	 * 判断Short,空值
	 * @param s
	 * @return
	 * null/0 (true)
	 * 
	 */
	public static boolean isBlank(Short s){
		return null == s || s.equals((short)0);
	}
	
	/**
	 * 判断Double,空值
	 * @param l
	 * @return
	 * null/0 (true)
	 * 
	 */
	public static boolean isBlank(Double d){
		return null == d || d.equals(0d);
	}
	
    /**
     * 查看Object是否为null
     * @param obj Java Object对象
     * @return 是null 返回true否则false
     */
    public static boolean isBlank(Object obj){
        return obj == null;
    }
	
	/**
	 *  判断数组是否为空
	 */
	public static boolean isBlank(Object[] array){
		return array == null || array.length == 0;
	}
	
	/**
	 *  判断List是否为空
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isBlank(List list){
		return list == null || list.size() == 0;
	}
	
	/**
	 *  判断Map是否为空
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isBlank(Map map){
		return map == null || map.size() == 0;
	}
	
	/**
	 *  判断Set是否为空
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isBlank(Set set){
		return set == null || set.size() == 0;
	}
	/**
	 * 判断是否为空
	 * @param obj
	 * @return
	 */
	public static boolean isNull(Object obj){
		return obj == null;
	}
	
	/**
	 * 判断是否为空
	 * @param obj
	 * @return
	 */
	public static boolean isNotNull(Object obj){
		return !isNull(obj);
	}
}
