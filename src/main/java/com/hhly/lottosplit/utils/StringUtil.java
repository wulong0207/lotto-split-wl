package com.hhly.lottosplit.utils;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 
 * @author HouXB
 *
 */
public class StringUtil extends StringUtils {
	
	/**
	 * @param str
	 *            字符串
	 * @return true/false
	 * @desc 是否为空白,包括null和""
	 */
	public static boolean isBlank(String str) {
		return str == null || str.trim().length() == 0;
	}
	
	/**
	 * @desc 不同类型转字符串(null返回空字符串)
	 * @author huangb
	 * @date 2017年1月20日
	 * @param o
	 * @return
	 */
	public static <T> String convertObjToStr(T o) {
		if (o == null) {
			return "";
		}
		return String.valueOf(o);
	}
	/**
	 * @param str
	 *            源字符串
	 * @param len
	 *            最终长度
	 * @return 返回长度为len的字符串
	 * @Desc 将字符串左边填充"0"，使字符串长度达到指定长度
	 */
	public static String padZeroLeft(String str, int len) {
		return padLeft(str, '0', len);
	}
	
	/**
	 * @param str
	 *            源字符串
	 * @param len
	 *            最终长度
	 * @return 返回长度为len的字符串
	 * @Desc 将字符串左边填充指定字符，使字符串长度达到指定长度
	 */
	public static String padLeft(String str, char padchar, int len) {
		str = isBlank(str) ? "" : str;
		for (int i = str.length(); i < len; i++) {
			str = padchar + str;
		}
		return str;
	}

	/**
	 * @param str
	 *            源字符串
	 * @param padchar
	 *            填充字符
	 * @param len
	 *            最终长度
	 * @return 返回长度为len的字符串
	 * @Desc 将字符串右边填充指定字符，使字符串长度达到指定长度
	 */
	public static String padRight(String str, char padchar, int len) {
		StringBuffer sb = new StringBuffer(isBlank(str) ? "" : str);
		for (int i = sb.length(); i < len; i++) {
			sb.append(padchar);
		}
		return sb.toString();
	}

	/**
	 * 将字符串中每个字符之间增加一个分割符号
	 * @param str 012345
	 * @param pad | or . or ,
	 * @return 0|1|2|3|4|5
	 */
	public static String padSplitChar(String str, String pad)
	{
		StringBuffer result = new StringBuffer("");
		int size = str.length();
		for (int i = 0; i < size; i++)
		{
			if (i > 0)
			{
				result.append(pad);
			}
			result.append(str.charAt(i));
		}
		return result.toString();
	}

	public static String join(String[] arr, String split)
	{
		StringBuffer result = new StringBuffer("");
		for (int i = 0; i < arr.length; i++)
		{
			if (i > 0)
				result.append(split);
			result.append(arr[i]);
		}
		return result.toString();
	}

	public static String[] splitArr(String string)
	{
		String[] result = new String[string.length()];
		for (int i = 0; i < string.length(); i++)
			result[i] = String.valueOf(string.charAt(i));
		return result;
	}

	public static String[] splitArr(String value, int len)
	{
		if (value == null || value.equals(""))
			return null;
		if (len < 0)
			len = 0;
		int idx = value.length() / len;
		if (value.length() % len != 0)
			idx = idx + 1;
		String[] result = new String[idx];
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < result.length; i++)
		{
			for (int j = 0; j < len; j++)
			{
				sb.append(String.valueOf(value.charAt(i * len + j)));
				if (i * len + j == value.length() - 1)
					break;
			}
			result[i] = sb.toString();
			sb.setLength(0);
		}
		return result;
	}

	public static String join(String[] arr)
	{
		StringBuffer result = new StringBuffer("");
		for (int i = 0; i < arr.length; i++)
		{
			result.append(arr[i]);
		}
		return result.toString();
	}

	public static String[] sort(String[] args)
	{
		int len = args.length;
		for (int i = 1; i < len; i++)
		{
			for (int j = 0; j < i; j++)
			{
				if (args[j].compareTo(args[i]) > 0)
				{
					String tmp = args[j];
					args[j] = args[i];
					args[i] = tmp;
				}
			}
		}
		return args;
	}

	public static String[] sortByNumber(String[] args)
	{
		return sortByNumber(args, false);
	}

	public static String[] sortByNumber(String[] args, boolean addZero)
	{
		int len = args.length;
		for (int i = 0; i < len; i++)
		{
			for (int j = i + 1; j < len; j++)
			{
				int x = Integer.parseInt(args[i]);
				int y = Integer.parseInt(args[j]);

				if (x > y)
				{
					String tmp = args[j];
					args[j] = args[i];
					args[i] = tmp;
				}
			}
			if (addZero)
			{
				int x = Integer.parseInt(args[i]);
				if (x < 10)
					args[i] = "0" + x;
			}
		}

		return args;
	}

	public static String[] padZeroNumberString(String[] strarr)
	{
		for (int i = 0; i < strarr.length; i++)
		{
			if (Integer.parseInt(strarr[i]) < 10)
				strarr[i] = "0" + strarr[i];
		}
		return strarr;
	}

	/**
	 * 给一串包含数字的字符串，各个小于10的数字前补0
	 * @param str
	 * @param splitstr
	 * @return
	 */
	public static String padZeroNumberString(String str, String splitstr)
	{
		String[] strarr = StringUtil.padZeroNumberString(str.split("[" + splitstr + "]"));
		return StringUtil.join(strarr, splitstr);
	}

	/**
	 * 清除一串包含数字的字符串中<10前面的0 01,02,10,20 结果：1,2,10,20
	 * @param str
	 * @param splitstr 字符串的分割符,如 ",","|"
	 * @return
	 */
	public static String clearZeroNumberString(String str, String splitstr)
	{
		if (splitstr == null || splitstr.equals(""))
			return str;
		String[] strarr = str.split("[" + splitstr + "]");
		StringBuffer sb = new StringBuffer();
		int x = 0;
		for (String number : strarr)
		{
			x = Integer.parseInt(number);
			sb.append("" + x).append(splitstr);

		}
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}

	/**
	 * 清除一串包含数字的字符串中<10前面的0 01,02,10,20|03,09 结果：1,2,10,20|3,9
	 * "|"為第split2,","為split1
	 * @param str
	 * @param split1 字符串的第二分割符,如 ",","|"
	 * @param split2 字符串的第一分割符,如 ",","|"
	 * @return
	 */
	public static String clearZeroNumberString(String str, String split1, String split2)
	{

		if (split2 == null || split1 == null || split1.equals(""))
			return str;
		if (split2.equals(""))
			return StringUtil.clearZeroNumberString(str, split1);
		StringBuffer sb = new StringBuffer();
		String[] strarr = str.split("[" + split2 + "]");
		for (String number : strarr)
		{
			sb.append(StringUtil.clearZeroNumberString(number, split1)).append(split2);
		}
		sb.setLength(sb.length() - 1);
		return sb.toString();

	}

	/**
	 * 
	 */
	public static String splitStr(String str, String split1, String split2, String playtype, String modeplay, int multiple)
	{
		if (str == null || str.equals(""))
			return null;
		String[] arr = str.split("[" + split1 + "]");
		if (arr.length < 2)
		{
			str = str + ":" + playtype + ":" + modeplay + ":" + multiple;
			return str;
		}
		StringBuffer sb = new StringBuffer();
		for (String number : arr)
		{
			sb.append(number).append(":").append(playtype).append(":").append(modeplay).append(":").append(multiple).append(split2);
		}
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}
	
	/**
	 * 返回小写字符串
	 * @param value
	 * @return
	 */
	public static String getStringLowerCase(String value){
		return value.trim().toLowerCase();
	}
	
	/**
	 * @desc   字符串拆成数字list
	 * @author Tony Wang
	 * @create 2017年3月24日
	 * @param str
	 * @param delimiters
	 * @return 
	 */
	public static List<Integer> toIntList(String str, String delimiters) {
		List<String> tokens = toStrList(str, delimiters);
		
		List<Integer> intList = new ArrayList<Integer>();
		for (String tmp : tokens) {
			intList.add(Integer.parseInt(tmp));
		}
		return intList;
	}
	/**
	 * @desc   字符串拆成list
	 * @author Tony Wang
	 * @create 2017年3月24日
	 * @param str
	 * @param delimiters
	 * @return 
	 */
	public static List<String> toStrList(String str, String delimiters) {
		if (isBlank(str)) {
			return Collections.emptyList();
		}
		StringTokenizer st = new StringTokenizer(str, delimiters);
		List<String> tokens = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			String token = st.nextToken().trim();
			if (token.length() > 0) {
				tokens.add(token);
			}
		}
		return tokens;
	}
	
	/**
	 * @desc   以逗号为分隔符,把字符串拆成数字list
	 * @author Tony Wang
	 * @create 2017年3月24日
	 * @param str
	 * @return 
	 */
	public static List<Integer> toIntList(String str) {
		return toIntList(str, ",");
	}
	
	/**  
	* 方法说明: 隐藏身份证、银行卡、手机号、邮箱的中间部分
	* @auth: xiongJinGang
	* @param code 需要隐藏的字符串
	* @param type 1、身份证；2、银行卡；3、手机号；4、邮箱；5、姓名;6、移动端银行卡显示
	* @time: 2017年4月18日 下午3:30:42
	* @return: String 
	*/
	public static String hideString(String code, Short type) {
        if(isBlank(code)) {
            return code;
        }
        String hideStr = "";
        StringBuffer stringBuffer = new StringBuffer();
        switch (type) {
            case 1:// 身份证号隐藏
                hideStr = stringBuffer.append(code.substring(0, 3)).append("*************").append(code.substring(code.length() - 2, code.length())).toString();
                break;
            case 2:// 银行卡号隐藏
                hideStr = stringBuffer.append(code.substring(0, 2)).append("*************").append(code.substring(code.length() - 4, code.length())).toString();
                break;
            case 3:// 手机号隐藏
                hideStr = stringBuffer.append(code.substring(0, 3)).append("*****").append(code.substring(code.length() - 2, code.length())).toString();
                break;
            case 4:// 邮箱隐藏
                int num = code.indexOf("@");
                if(num > 4) {
                    hideStr = stringBuffer.append(code.substring(0, 4)).append("****").append(code.substring(code.indexOf("@"), code.length())).toString();
                } else {
                    hideStr = stringBuffer.append(code.substring(0, code.indexOf("@"))).append("****").append(code.substring(code.indexOf("@"), code.length())).toString();
                }
                break;
            case 5:// 姓名隐藏
                hideStr = stringBuffer.append(code.substring(0, 1)).append("**").toString();
                break;
            case 6:
                //隐藏银行卡-移动显示
                hideStr = stringBuffer.append("***" ).append(code.substring(code.length() - 4, code.length())).toString();
                break;
			case 7:
				//隐藏昵称
				hideStr = stringBuffer.append(code.substring(0,2)).append("****").toString();
				break;
            default:
                hideStr = code;
                break;
        }
        return hideStr;
    }
	
	/**  
	* 方法说明: 只留卡号后4位
	* @auth: xiongJinGang
	* @param code
	* @time: 2017年4月19日 下午3:35:43
	* @return: String 
	*/
	public static String hideHeadString(String code) {
		if (isBlank(code)) {
			return code;
		}
		return new StringBuffer("***").append(code.substring(code.length() - 4, code.length())).toString();
	}
	
	/**  
	* 方法说明: 将中文逗号、顿号替换成英文逗号
	* @auth: xiongJinGang
	* @param param
	* @time: 2017年4月20日 下午6:12:59
	* @return: String 
	*/
	public static String replaceSign(String param) {
		if (isBlank(param)) {
			return param;
		}
		param = param.replaceAll("，", ",");
		param = param.replaceAll("、", ",");
		return param;
	}
	
	/**  
	* 方法说明: 去除参数左右空格
	* @auth: xiongJinGang
	* @param value
	* @time: 2017年4月20日 下午6:16:52
	* @return: String 
	*/
	public static String trimSpace(String value) {
		if (!ObjectUtil.isBlank(value)) {
			return value.trim();
		}
		return value;
	}
	
	/**  
	* 方法说明: 截取结尾的符号
	* @auth: xiongJinGang
	* @param value
	* @param symbol
	* @time: 2017年4月27日 下午6:41:53
	* @return: String 
	*/
	public static String interceptEndSymbol(String value, String symbol) {
		if (!ObjectUtil.isBlank(value) && !ObjectUtil.isBlank(symbol)) {
			if (value.endsWith(symbol))
				return value.substring(0, value.length() - 1);
		}
		return value;
	}
	
	
	/**
	 * 方法说明：字符串List转成字符串
	 * @author longguoyou
	 * @date 2017年4月28日
	 * @param list
	 * @return
	 */
	public static String listToString(List<String> list){
	   if(ObjectUtil.isBlank(list)){return "";}
	   StringBuilder result = new StringBuilder();
	   for(String string : list) {
	      result.append(string);
	   }
	   return result.toString();
	}
}
