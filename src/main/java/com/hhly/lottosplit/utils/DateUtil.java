package com.hhly.lottosplit.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 *
 * @Date 2016年12月8日
 *
 * @Desc 日期工具类
 */
public class DateUtil {

	/** 定义格式 **/
	public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATETIME_FORMAT_NO_SEC = "yyyy-MM-dd HH:mm";
	public static final String FORMAT_YYYY_MM = "yyyy-MM";
	public static final String FORMAT_YYYYMM = "yyyyMM";
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATE_FORMAT_NO_LINE = "yyyyMMdd";
	public static final String DATE_FORMAT_LINE = "yyyyMMdd HH:mm:ss";
	public static final String TIME = "HH:mm:ss";
	public static final String DATA_FORMAT_SIMPLE = "yyMMddHHmmss";
	public static final String DATE_FORMAT_NUM = "yyyyMMddHHmmss";
	public static final String DATE_MMM_D_YYYY_K_M_S_S = "MMM d, yyyy K:m:s a";
	public static final String FORMAT_CHINESE_DAY = "MM月dd日 HH:mm:ss";
	public static final String FORMAT_CHINESE_YYYYMM = "yyyy年MM月";

	/**
	 * @return 根据默认格式(yyyy-MM-dd HH:mm:ss)获取当前时间
	 */
	public static String getNow() {
		return new SimpleDateFormat(DEFAULT_FORMAT).format(new Date());
	}

	/**
	 * @param format
	 *            - 日期格式
	 * @return 根据参数日期格式获取当前时间
	 */
	public static String getNow(String format) {
		SimpleDateFormat sdf = null;
		if (null == format || "".equals(format)) {
			sdf = new SimpleDateFormat(DEFAULT_FORMAT);
		} else {
			sdf = new SimpleDateFormat(format);
		}
		return sdf.format(new Date());
	}

	/**
	 * @param str
	 *            字符型日期
	 * @return 根据默认格式(yyyy-MM-dd HH:mm:ss)获取日期
	 */
	public static Date convertStrToDate(String str) {
		return convertStrToDate(str, DEFAULT_FORMAT);
	}

	/**
	 * @param str
	 *            日期字符串
	 * @param format
	 *            格式
	 * @return 获取指定格式日期对象
	 * @Desc 获取指定格式日期对象
	 */
	public static Date convertStrToDate(String str, String format) {
		try {
			if (null == str || "".equals(str)) {
				return null;
			}
			return new SimpleDateFormat(format).parse(str);
		} catch (ParseException e) {
			return null;
		}
	}
	/**
	 * 日期格式转换
	 * @param str 日期格式
	 * @param format
	 * @return
	 * @throws NullPointerException(参数为空)，ServiceRuntimeException（转换异常）
	 */
	public static Date convertStrToDate2(String str, String format) {
		try {
			if (null == str || "".equals(str)) {
				throw new NullPointerException("日期参数为空");
			}
			return new SimpleDateFormat(format).parse(str);
		} catch (ParseException e) {
			throw new RuntimeException("日期转换异常："+e.getMessage());
		}
	}
	/**
	 * @param date
	 *            日期对象
	 * @return 默认格式的日期字符串
	 * @Desc 日期对象转为字符串
	 */
	public static String convertDateToStr(Date date) {
		return convertDateToStr(date, DEFAULT_FORMAT);
	}

	/**
	 * @param date
	 *            日期对象
	 * @param format
	 *            格式
	 * @return 指定格式的日期字符串
	 * @Desc 日期对象转为指定格式字符串
	 */
	public static String convertDateToStr(Date date, String format) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = null;
		if (null == format || "".equals(format)) {
			sdf = new SimpleDateFormat(DEFAULT_FORMAT);
		} else {
			sdf = new SimpleDateFormat(format);
		}
		return sdf.format(date);
	}
	
	/**  
	* 方法说明: 得到前后几天的日期，num为正整数，则是后几天，为负整数，则是前几天
	* @auth: xiongJinGang
	* @param num
	* @time: 2017年4月19日 下午2:56:04
	* @return: String 
	*/
	public static String getBeforeOrAfterDate(int num) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, num);
		return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
	}
	
	/**  
	* 方法说明: 得到前后几天的日期，num为正整数，则是后几天，为负整数，则是前几天
	* @auth: xiongJinGang
	* @param num
	* @param format
	* @time: 2017年4月26日 下午5:16:30
	* @return: String 
	*/
	public static String getBeforeOrAfterDate(int num,String format) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, num);
		return new SimpleDateFormat(format).format(cal.getTime());
	}

	/**
	 * @return 秒值
	 * @Desc 获取当前时间的秒值
	 */
	public static int getSecOfNow() {
		return getFieldVal(new Date(), Calendar.SECOND);
	}

	/**
	 * @param date
	 *            日期对象
	 * @return 秒值
	 * @Desc 获取指定日期的秒值
	 */
	public static int getSec(Date date) {
		return getFieldVal(date == null ? new Date() : date, Calendar.SECOND);
	}

	/**
	 * @return 分值
	 * @Desc 获取当前时间的分值
	 */
	public static int getMinOfNow() {
		return getFieldVal(new Date(), Calendar.MINUTE);
	}

	/**
	 * @param date
	 *            日期对象
	 * @return 分值
	 * @Desc 获取指定日期的分值
	 */
	public static int getMin(Date date) {
		return getFieldVal(date == null ? new Date() : date, Calendar.MINUTE);
	}
	
	/**
	 * @param date
	 * 			    日期对象
	 * @return 日值
	 * @Desc 获取指定日期的日值
	 */
	public static int getDay(Date date) {
		return getFieldVal(date == null ? new Date() : date, Calendar.DATE);
	}
	
	/**
	 * @return 小时
	 * @Desc 获取当前时间的小时值
	 */
	public static int getHourOfNow() {
		return getFieldVal(new Date(), Calendar.HOUR_OF_DAY);
	}
	
	/**
	 * @return 日值
	 * @Desc 获取当前时间的日值
	 */
	public static int getDayOfNow() {
		return getFieldVal(new Date(), Calendar.DATE);
	}

	/**
	 * @return 年值
	 * @Desc 获取当前时间的年值
	 */
	public static int getYearOfNow() {
		return getFieldVal(new Date(), Calendar.YEAR);
	}

	/**
	 * @param date
	 *            日期对象
	 * @return 年值
	 * @Desc 获取指定日期的年值
	 */
	public static int getYear(Date date) {
		return getFieldVal(date == null ? new Date() : date, Calendar.YEAR);
	}

	/**
	 * @param date
	 *            日期对象
	 * @param field
	 *            日期字段(eg：YEAR，MINUTE，SECOND，DAY_OF_WEEK 等)
	 * @return 指定日期的指定字段值
	 * @Desc 获取指定日期的指定字段值
	 */
	public static int getFieldVal(Date date, int field) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(field);
	}

	/**
	 * @param startDate
	 *            开始日期
	 * @param includeDays
	 *            包含的天集合,即需要哪几天的日期(只能指定SUNDAY, MONDAY, TUESDAY, WEDNESDAY,
	 *            THURSDAY, FRIDAY, SATURDAY对应的字段值)
	 * @param count
	 *            日期列表數量
	 * @return 日期列表,从指定的开始日期往后，获取一周中指定的那几天日期，组合一个日期集合返回
	 * @Desc 从指定的开始日期往后，获取一周中指定的几天日期的集合列表
	 */
	public static List<Date> getDaysOfWeek(Date startDate, Integer[] includeDays, int count) {
		return getDaysOfWeek(startDate, includeDays, count, null);
	}

	/**
	 * @param startDate
	 *            开始日期
	 * @param includeDays
	 *            包含的天集合,即需要哪几天的日期(只能指定SUNDAY, MONDAY, TUESDAY, WEDNESDAY,
	 *            THURSDAY, FRIDAY, SATURDAY对应的字段值)
	 * @param count
	 *            日期列表數量
	 * @param filterDays
	 *            需要过滤的天集合(即符合条件的天不能存在于该过滤列表中)
	 * @return 日期列表,从指定的开始日期往后，获取一周中指定的那几天日期，组合一个日期集合返回
	 * @Desc 从指定的开始日期往后，获取一周中指定的几天日期的集合列表
	 */
	public static List<Date> getDaysOfWeek(Date startDate, Integer[] includeDays, int count, List<Date> filterDays) {
		if (startDate == null || includeDays == null || includeDays.length == 0 || count == 0) {
			return new ArrayList<Date>();
		}
		List<Integer> dayList = Arrays.asList(includeDays);
		if (!(dayList.contains(Calendar.SUNDAY) || dayList.contains(Calendar.MONDAY)
				|| dayList.contains(Calendar.TUESDAY) || dayList.contains(Calendar.WEDNESDAY)
				|| dayList.contains(Calendar.THURSDAY) || dayList.contains(Calendar.FRIDAY)
				|| dayList.contains(Calendar.SATURDAY))) {
			return new ArrayList<Date>();
		}
		List<Date> target = new ArrayList<Date>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		while (true) {
			if (count == target.size()) {
				break;
			}
			// 如果是需要过滤的日期
			if (filterDays != null && !filterDays.isEmpty() && filterDays.contains(cal.getTime())) {
				cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 1);
				continue;
			}
			// 是否是符合的天
			int temp = cal.get(Calendar.DAY_OF_WEEK);
			if (dayList.contains(temp)) {
				target.add(cal.getTime());
			}
			cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 1);
		}
		return target;
	}

	/**
	 * @param startDate
	 *            开始日期
	 * @param includeDays
	 *            包含的天集合,即需要哪几天的日期(只能指定SUNDAY, MONDAY, TUESDAY, WEDNESDAY,
	 *            THURSDAY, FRIDAY, SATURDAY对应的字段值)
	 * @param count
	 *            日期列表數量
	 * @param filterStart
	 *            过滤日期范围：开始日期
	 * @param filterEnd
	 *            过滤日期范围：结束日期
	 * @return 日期列表,从指定的开始日期往后，获取一周中指定的那几天日期，组合一个日期集合返回
	 * @Desc 从指定的开始日期往后，获取一周中指定的几天日期的集合列表
	 */
	public static List<Date> getDaysOfWeek(Date startDate, Integer[] includeDays, int count, Date filterStart,
			Date filterEnd) {
		if (startDate == null || includeDays == null || includeDays.length == 0 || count == 0) {
			return new ArrayList<Date>();
		}
		List<Integer> dayList = Arrays.asList(includeDays);
		if (!(dayList.contains(Calendar.SUNDAY) || dayList.contains(Calendar.MONDAY)
				|| dayList.contains(Calendar.TUESDAY) || dayList.contains(Calendar.WEDNESDAY)
				|| dayList.contains(Calendar.THURSDAY) || dayList.contains(Calendar.FRIDAY)
				|| dayList.contains(Calendar.SATURDAY))) {
			return new ArrayList<Date>();
		}
		List<Date> target = new ArrayList<Date>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		while (true) {
			if (count == target.size()) {
				break;
			}
			// 如果是需要过滤的日期范围
			if (filterStart != null && filterEnd != null && cal.getTime().compareTo(filterStart) >= 0
					&& cal.getTime().compareTo(filterEnd) <= 0) {
				cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 1);
				continue;
			}
			// 是否是符合的天
			int temp = cal.get(Calendar.DAY_OF_WEEK);
			if (dayList.contains(temp)) {
				target.add(cal.getTime());
			}
			cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 1);
		}
		return target;
	}
	/**
	 * 计算日期是星期几
	 * @param date
	 * @return 1-7
	 */
	public static int dayForWeek(Date date) {
		  Calendar c = Calendar.getInstance();
		  c.setTime(date);
		  return dayForWeek(c);
	}
	/**
	 * 计算当前时间是星期几
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年3月3日 下午2:16:55
	 * @return
	 */
	public static int dayForWeek(){
		return dayForWeek(Calendar.getInstance());
	}
	/**
	 * 根据日期字符串获取周几
	 * @param dateStr yyyy-MM-dd HH:mm:ss
	 * @return
     */
	public static int dayForWeek(String dateStr, String format){
		Date date = convertStrToDate(dateStr,format);
		return dayForWeek(date);
	}
	
	/**
	 * 计算星期
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年3月3日 下午2:20:46
	 * @param c
	 * @return
	 */
	private static  int dayForWeek(Calendar c){
		  int dayForWeek = 0;
		  if(c.get(Calendar.DAY_OF_WEEK) == 1){
		   dayForWeek = 7;
		  }else{
		   dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		  }
		  return dayForWeek;
	}
	/**
	 * 获取日期前几天或者后几天的日期
	 * @param date 指定日期
	 * @param dayNum 指定天数 正数 1 为后一天，负数-1为前一天
	 * @return 日期
     */
	public static Date getDateDit(Date date ,int dayNum){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		date = calendar.getTime();
		return date;
	}
	
	/**
	 * 当前时间加多少秒后返回
	 * @param second
	 * @return
	 */
	public static Timestamp currentTimeAddSec(int second){		
		Calendar caln = Calendar.getInstance();
		caln.add(Calendar.SECOND, second);
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		ts.setTime(caln.getTimeInMillis());
		return ts;
	}
	
	/**
     * 输入的时间是否小于当前时间
     * @param t
     * @return
     */
	public static boolean isOver(String t){		
		if(t == null) return true;
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(DateUtil.strToTimestamp(t));		
		return (cal1.compareTo(cal2)) <= 0;
	}
	
	
	/**
     * 输入的时间是否小于当前时间
     * @param t
     * @return
     */
	public static boolean isOver(Timestamp t){		
		if(t == null) return true;
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(t);		
		return (cal1.compareTo(cal2)) <= 0;
	}
	
	
	/**
	 * 将字符串转换为时间戳
	 * @param time
	 * @param pattern
	 * @return 时间戳
	 */
	public static Timestamp strToTimestamp(String time){	
		  Timestamp ts = null;
	      try {
	    	  ts = Timestamp.valueOf(time);  
			  return ts;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ts;
	}
	/**
	 * 
	 * @author longguoyou
	
	 * @date 2017年2月9日 上午10:04:33
	
	 * @desc 比较两个日期类型时间大小
	 *
	 * @param sourceDate 
	 * @param targetDate 
	 * @return
	 */
	public static int compare(Date sourceDate, Date targetDate){
		DateFormat df = new SimpleDateFormat(DEFAULT_FORMAT);
		return compare(df.format(sourceDate),df.format(targetDate));
	}
	/**
	 * 
	 * @author longguoyou
	
	 * @date 2017年2月9日 上午9:51:08
	
	 * @desc 比较两个字符类型时间大小：<br>大于，返回1； 等于 ，返回0；小于，返回 -1
	 *
	 * @param sourceTime 比较时间
	 * @param targetTime 被比较时间
	 * @return
	 */
	public static int compare(String sourceTime, String targetTime){
		DateFormat df = new SimpleDateFormat(DEFAULT_FORMAT);
        try {
            Date dtSource = df.parse(sourceTime);
            Date dtTarget = df.parse(targetTime);
            if (dtSource.getTime() > dtTarget.getTime()) {
                return 1;
            } else if (dtSource.getTime() < dtTarget.getTime()) {
                return -1;
            } else {return 0;}
        } catch (Exception ex) {
        	throw new RuntimeException(ex);
        }
	}
	public static Date getMinDate(Date date1,Date date2){
	    if(date1==null){
	        return date2;
	    }else if(date2 == null){
	        return date1;
	    }else if(date1.getTime() < date2.getTime()){
	        return date1;
	    }
	    return date2;
	}
	
	/**  
	* 方法说明: 验证信用卡是否过期（overdueDate=0920的格式）
	* @auth: xiongjingang
	* @param overdueDate
	* @time: 2017年3月17日 上午9:26:48
	* @return: boolean 
	*/
	public static boolean validateCreditCard(String overdueDate) {
		boolean flag = false;
		try {
			String month = overdueDate.substring(0, 2);
			String year = "20" + overdueDate.substring(2, 4);
			String nowMonth = getNow(FORMAT_YYYY_MM);
			String validateYear = year +"-"+ month;
			flag = validateYear.compareTo(nowMonth)>=0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
    
    /**
     * 校验信用卡有效期是否过期
     * @param input 传入参数，格式如：1220
     * @return true:表示还未过期;false:表示过期
     */
	public static Boolean validateCredCardOver(String input) throws ParseException{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMyy");
        simpleDateFormat.setLenient(false);
        Date expiry = simpleDateFormat.parse(input);
        return expiry.after(new Date());
    }
	/**
	 * 
	 * @Description: 返回两个日期之间的毫秒数
	 * @param date1
	 * @param date2
	 * @return
	 * @author wuLong
	 * @date 2017年3月25日 下午2:24:57
	 */
	public static long getDifferenceTime(Date date1,Date date2){
		long d = date1.getTime() - date2.getTime();
		if(d<1){
			return 0;
		}
		return d;
	}

	/**
	 * 判断是否超过1年
	 * @param startDate
	 * @param endDate
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean judgmentDate(Date startDate, Date endDate) throws Exception {
		Calendar calendar = Calendar.getInstance();
		Date date = null;
		calendar.setTime(endDate);
		calendar.add(Calendar.YEAR, -1);
		date = calendar.getTime();
		if(date.compareTo(startDate)>0){
           return true;
		}else {
			return false;
		}
	}

	
}
