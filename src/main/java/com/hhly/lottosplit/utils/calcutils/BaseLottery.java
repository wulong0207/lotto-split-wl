package com.hhly.lottosplit.utils.calcutils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
	
/**
 * 
 * @ClassName: BaseLottery 
 * @Description: 所有彩种验证基类
 * @author wuLong
 * @date 2017年3月6日 下午3:49:52 
 *
 */
@Scope("prototype")
@Component("baseLottery")
public class BaseLottery {
	@SuppressWarnings("rawtypes")
	List comArry = null;// 要组合或排列的数组
	int num = 0;// 组合每注号码个数.

	Combine combine = new Combine();// 组合类
	Percontion percon = new Percontion();// 排列类

	// 组合类
	/**
	 * 组合类
	 *
	 * @author wuLong
	 * @version 1.0
	 * @2010-6-3
	 * @description
	 * @path ecp888_2-->com.ecp888.utils.util-->BaseLottery.java
	 * @param arr
	 *            要组合元素列表
	 * @param num
	 *            一个组合中需要几个 元素 void
	 */
	@SuppressWarnings("rawtypes")
	public List getCombineData(String[] arr, int num) {
		combine.mn(arr, num);
		return combine.getCombineData();
	}

	public List<String[]> getCombineData4Arr(String[] arr, int num) {
		return combine.mn4Arr(arr, num);
	}

	/**
	 * 组合类
	 *
	 * @author wuLong
	 * @version 1.0
	 * @2010-6-3
	 * @description
	 * @path ecp888_2-->com.ecp888.utils.util-->BaseLottery.java
	 * @param m
	 *            基数
	 * @param n
	 *            几个元素组成一注
	 * @return 组合注数 int
	 */
	public int getCombineCount(int m, int n) {
		if (m < n)
			throw new IllegalArgumentException("Error   m   <   n");
		return combine.getCombineCount(n, m);

	}

	/**
	 * 排列类
	 *
	 * @author wuLong
	 * @version 1.0
	 * @2010-6-3
	 * @description
	 * @path ecp888_2-->com.ecp888.utils.util-->BaseLottery.java
	 * @param arr
	 * @param num
	 * @return List
	 */
	@SuppressWarnings("rawtypes")
	public List getPercontionData(String[] arr, int num) {
		combine.mn(arr, num);
		List alist = combine.getCombineData();
		List blist = new ArrayList();
		for (int i = 0; i < alist.size(); i++) {
			String[] s = alist.get(i).toString().split(",");
			percon.perm(s, 0, s.length - 1);
			List clist = percon.getPerconData();
			blist = mergeList(blist, clist);
		}
		return blist;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List mergeList(List srcList, List destList) {
		for (int i = 0, j = destList.size(); i < j; i++) {
			srcList.add(destList.get(i));
		}
		return srcList;
	}

	/**
	 * @author wuLong
	 * @version 1.0
	 * @2010-8-22
	 * @description
	 * @path ecp888_4-->com.ecp888.utils.impl-->BaseLottery.java
	 * @param arr
	 *            要排列的数组
	 * @param num
	 *            长度
	 * @return 全排列的字符串长度，号码以逗号隔开，注以|分隔 String 如 a,b,c|a,c,b|b,a,c
	 */
	@SuppressWarnings("rawtypes")
	public String getPercontionToStr(String[] arr, int num) {
		combine.mn(arr, num);
		List alist = combine.getCombineData();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < alist.size(); i++) {
			String[] s = alist.get(i).toString().split(",");
			StringBuffer sbb = new StringBuffer();
			sbb = percon.perm(s, 0, s.length - 1, sbb);
			sb.append(sbb).append("|");
		}
		// if(sb.length() > 0)
		while (sb.toString().endsWith("|"))
			sb.setLength(sb.length() - 1);
		return sb.toString();
	}

	/**
	 * 排列类
	 *
	 * @author wuLong
	 * @version 1.0
	 * @2010-6-3
	 * @description
	 * @path ecp888_2-->com.ecp888.utils.util-->BaseLottery.java
	 * @param m
	 *            基数
	 * @param n
	 *            几个元素一注
	 * @return 全排列组合几注 int
	 */
	public int getPercontionCount(int m, int n) {
		if (m < n)
			throw new IllegalArgumentException("Error   m   <   n");
		return percon.getPercontionCount(m, n);
	}

	// 数学计算类
	/**
	 * @author wuLong
	 * @version 1.0
	 * @2010-6-4
	 * @description
	 * @path ecp888_2-->com.ecp888.utils.impl-->BaseLottery.java
	 * @param n
	 *            要阶乘的基数
	 * @return 阶乘结果 int
	 */
	public long getMathFact(long n) {
		long c = 1;
		// return n <1 ?1:n *getMathFact(n-1);
		while (n > 1) {
			c *= n--;
		}
		return c;
	}

	/**
	 * 数组类
	 *
	 * @author wuLong
	 * @version 1.0
	 * @2010-6-4
	 * @description
	 * @path ecp888_2-->com.ecp888.utils.impl-->BaseLottery.java
	 * @param arr
	 *            需要循环累加的数组
	 * @return 数组元素之和 int
	 */
	public int getArrayAddItem(String[] arr) {
		if (null == arr || arr.length == 0)
			return 0;
		int sum = 0;
		for (String str : arr) {
			sum += Integer.parseInt(str);
		}
		return sum;
	}

	/**
	 * 数组类
	 *
	 * @author wuLong
	 * @version 1.0
	 * @2010-6-4
	 * @description
	 * @path ecp888_2-->com.ecp888.utils.impl-->BaseLottery.java
	 * @param arr
	 *            需要循环累加的数组
	 * @return 数组各元素长度之和 int
	 */
	public int getArrayAddItems(String[] arr) {
		if (null == arr || arr.length == 0)
			return 0;
		int sum = 0;
		for (String str : arr) {
			sum += str.length();
		}
		return sum;
	}

	/**
	 * 数组类
	 *
	 * @author wuLong
	 * @version 1.0
	 * @2010-6-4
	 * @description
	 * @path ecp888_2-->com.ecp888.utils.impl-->BaseLottery.java
	 * @param arr
	 *            需要循环累加的数组
	 * @return 数组元素之和 int
	 */
	public int getArrayAddItem(int[] arr) {
		if (null == arr || arr.length == 0)
			return 0;
		int sum = 0;
		for (int str : arr) {
			if (str == 0)
				continue;
			sum += str;
		}
		return sum;
	}

	/**
	 * 数组类
	 *
	 * @author wuLong
	 * @version 1.0
	 * @2010-6-4
	 * @description
	 * @path ecp888_2-->com.ecp888.utils.impl-->BaseLottery.java
	 * @param arr
	 *            需要循环累乘的数组
	 * @return 数组元素之积 int
	 */
	public int getArrayMultipItem(String[] arr) {
		if (null == arr || arr.length == 0)
			return 0;
		int sum = 1;
		for (String str : arr) {
			sum *= Integer.parseInt(str);
		}
		return sum;
	}

	/**
	 * 数组类
	 *
	 * @author wuLong
	 * @version 1.0
	 * @2010-6-4
	 * @description
	 * @path ecp888_2-->com.ecp888.utils.impl-->BaseLottery.java
	 * @param arr
	 *            需要循环累加的数组
	 * @return 数组各元素长度之积 int
	 */
	public int getArrayMultipItems(String[] arr) {
		if (null == arr || arr.length == 0)
			return 0;
		int sum = 1;
		for (String str : arr) {
			sum *= str.length();
		}
		return sum;
	}

	/**
	 * 数组类
	 *
	 * @author wuLong
	 * @version 1.0
	 * @2010-6-4
	 * @description
	 * @path ecp888_2-->com.ecp888.utils.impl-->BaseLottery.java
	 * @param arr
	 *            需要循环累加的数组
	 * @return 数组元素之积 int
	 */
	public int getArrayMultipItem(int[] arr) {
		if (null == arr || arr.length == 0)
			return 0;
		int sum = 1;
		for (int str : arr) {
			if (str == 0)
				continue;
			sum *= str;
		}
		return sum;
	}

	/**
	 * @author wuLong
	 * @version 1.0
	 * @2010-6-5
	 * @description
	 * @path ecp888_2-->com.ecp888.utils.impl-->BaseLottery.java
	 * @param arr
	 * @return String[] 遍历数组每个大小长度
	 */
	public String[] getArrayCols(String[] arr) {
		String[] tmp = new String[arr.length];
		int i = 0;
		for (String s : arr) {
			tmp[i++] = "" + s.length();
		}
		return tmp;
	}

	/**
	 * 待取得组合拆分的数组(先后顺序) 参数 str 形如:3,1,0|3,1的字符串 拆分后为3,3|3,1|1,3|1,1|0,3|0,1
	 *
	 * @param str
	 * @return
	 */
	public String getCombineArrToStr(String str) {
		String[] str0 = str.split("\\|");
		if (str0 == null || str0.length <= 0)
			return "";
		String[][] strArr = new String[str0.length][1];
		int i = 0;
		for (String s : str0) {
			strArr[i++] = s.split(",");
		}
		return getCombineArrToStr(strArr);
	}
	
	/**
	 * 2873:3,1,0/2893:3,1,0
	 * @return 
	 */
	public String getCombinneArrSp(String str){
		String[] str0 = str.split("/");
		if (str0 == null || str0.length <= 0){
			return "";
		}
		String[][] strArr = new String[str0.length][1];
		int i = 0;
		for (String s : str0) {
			String ss = ","+s.split(":")[0]+":";
			s = s.replace(",", ss);
			strArr[i++] = s.split(",");
		}
		return getCombineArrToStr(strArr);
	}
	/**
	 * 2873:3,1,0/2893:3,1,0
	 * @return 
	 */
	public String getCombinneArrSpWl(String str){
		String[] str0 = str.split("/");
		if (str0 == null || str0.length <= 0){
			return "";
		}
		String[][] strArr = new String[str0.length][1];
		int i = 0;
		for (String s : str0) {
			String ss = ","+s.split(":")[0]+":";
			s = s.replace(",", ss);
			strArr[i++] = s.split(",");
		}
		return getCombineArrToStr_wl(strArr);
	}

	/**
	 * 待取得组合拆分的数组(先后顺序) <br>
	 * 参数 str 形如:12,3,45,6的字符串 拆分后为 1,3,4,6|1,3,5,6|2,3,4,6|2,3,5,6
	 *
	 * @param str
	 *            字符串 12,3,45,6
	 * @return
	 */
	public String getCombineArrToStrParam(String str) {
		String[] str0 = str.split(",");
		if (str0 == null || str0.length <= 0)
			return "";
		String[][] strArr = new String[str0.length][0];
		int i = 0;
		for (String s : str0) {
			String[] tmp = new String[s.length()];
			for (int j = 0; j < s.length(); j++) {
				tmp[j] = "" + s.charAt(j);
			}
			strArr[i++] = tmp;
		}
		return getCombineArrToStr(strArr);
	}

	/**
	 * 参数 arr形如:["3","1","0"],["3","1"]数组列表
	 *
	 * 拆分后为3,3|3,1|1,3|1,1|0,3|0,1 待取得组合拆分的数组(先后顺序)
	 *
	 * @param arr
	 * @return
	 */
	public static String getCombineArrToStr(String[]... arr) {
		if (arr == null || arr.length <= 0)
			return "";
		List<String[]> list = new ArrayList<String[]>();
		for (String[] a : arr) {
			list.add(a);
		}
		StringBuffer sb = new StringBuffer();
		combineArr(list, arr[0], "", sb);
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	
	
	/**
	 * 参数 arr形如:["3","1","0"],["3","1"]数组列表
	 *
	 * 拆分后为3|3/3|1/1|3/1|1/0|3/0|1 待取得组合拆分的数组(先后顺序)
	 *
	 * @param arr
	 * @return
	 */
	public static String getCombineArrToStrabc(String[]... arr) {
		if (arr == null || arr.length <= 0)
			return "";
		List<String[]> list = new ArrayList<String[]>();
		for (String[] a : arr) {
			list.add(a);
		}
		StringBuffer sb = new StringBuffer();
		combineArrabc(list, arr[0], "", sb);
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	
	/**
	 * 参数 arr形如:["gameid:3,0","gameid:h11-15,h16-20"],["gameid:3","gameid:h11-15,h16-20"],["gameid:3"]数组列表
	 * 拆分后为gameid:3,0/gameid:h11-15,h16-20/gameid:3|gameid:3,0/gameid:3/gameid:3 待取得组合拆分的数组(先后顺序)
	 * 已竖线 分隔成   多串过关的每一个玩法组合
	 * @param arr
	 * @return
	 */
	public String getCombineArrToStr_s(String[]... arr) {
		if (arr == null || arr.length <= 0)
			return "";
		List<String[]> list = new ArrayList<String[]>();
		for (String[] a : arr) {
			list.add(a);
		}
		StringBuffer sb = new StringBuffer();
		combineArr_s(list, arr[0], "", sb);
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	
	/**
	 * 参数 arr形如:["gameid:3,0","gameid:h11-15,h16-20"],["gameid:3","gameid:h11-15,h16-20"],["gameid:3"]数组列表
	 * 拆分后为gameid:3,0/gameid:h11-15,h16-20/gameid:3$gameid:3,0/gameid:3/gameid:3 待取得组合拆分的数组(先后顺序)
	 * 已竖线 分隔成   多串过关的每一个玩法组合
	 * @param arr
	 * @return
	 */
	public String getCombineArrToStr_wl(String[]... arr) {
		if (arr == null || arr.length <= 0)
			return "";
		List<String[]> list = new ArrayList<String[]>();
		for (String[] a : arr) {
			list.add(a);
		}
		StringBuffer sb = new StringBuffer();
		combineArr_wl(list, arr[0], "", sb);
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	/**
	 * 参数 arr形如:["gameid:3,0","gameid:h11-15,h16-20"],["gameid:3","gameid:h11-15,h16-20"],["gameid:3"]数组列表
	 * 拆分后为gameid:3,0/gameid:h11-15,h16-20/gameid:3$gameid:3,0/gameid:3/gameid:3 待取得组合拆分的数组(先后顺序)
	 * 已竖线 分隔成   多串过关的每一个玩法组合
	 * @param arr
	 * @return
	 */
	public String getCombineArrToStr_plus(String[]... arr) {
		if (arr == null || arr.length <= 0)
			return "";
		List<String[]> list = new ArrayList<String[]>();
		for (String[] a : arr) {
			list.add(a);
		}
		StringBuffer sb = new StringBuffer();
		combineArr_plus(list, arr[0], "", sb);
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}


	/**
	 * 多数组组合问题
	 *
	 * @param list
	 * @param arr
	 * @param str
	 * @param sb
	 */
	private static void combineArr(List<String[]> list, String[] arr, String str,
			StringBuffer sb) {
		for (int i = 0; i < list.size(); i++) {
			// 取得当前的数组
			if (i == list.indexOf(arr)) {
				// 迭代数组
				for (String st : arr) {
					st = (str.equals("") ? "" : (str + ",")) + st;
					if (i < list.size() - 1) {
						combineArr(list, list.get(i + 1), st, sb);
					} else if (i == list.size() - 1) {
						sb.append(st + "|");
					}
				}
			}
		}
	}
	
	/**
	 * 多数组组合问题
	 *
	 * @param list
	 * @param arr
	 * @param str
	 * @param sb
	 */
	private static void combineArrabc(List<String[]> list, String[] arr, String str,
			StringBuffer sb) {
		for (int i = 0; i < list.size(); i++) {
			// 取得当前的数组
			if (i == list.indexOf(arr)) {
				// 迭代数组
				for (String st : arr) {
					st = (str.equals("") ? "" : (str + "|")) + st;
					if (i < list.size() - 1) {
						combineArrabc(list, list.get(i + 1), st, sb);
					} else if (i == list.size() - 1) {
						sb.append(st + "/");
					}
				}
			}
		}
	}
	
	/**
	 * 多数组组合问题
	 *
	 * @param list
	 * @param arr
	 * @param str
	 * @param sb
	 */
	private void combineArr_s(List<String[]> list, String[] arr, String str,
			StringBuffer sb) {
		for (int i = 0; i < list.size(); i++) {
			// 取得当前的数组
			if (i == list.indexOf(arr)) {
				// 迭代数组
				for (String st : arr) {
					st = (str.equals("") ? "" : (str + "/")) + st;
					if (i < list.size() - 1) {
						combineArr_s(list, list.get(i + 1), st, sb);
					} else if (i == list.size() - 1) {
						sb.append(st + "|");
					}
				}
			}
		}
	}
	
	/**
	 * 多数组组合问题
	 *
	 * @param list
	 * @param arr
	 * @param str
	 * @param sb
	 */
	private void combineArr_wl(List<String[]> list, String[] arr, String str,
			StringBuffer sb) {
		for (int i = 0; i < list.size(); i++) {
			// 取得当前的数组
			if (i == list.indexOf(arr)) {
				// 迭代数组
				for (String st : arr) {
					st = (str.equals("") ? "" : (str + "/")) + st;
					if (i < list.size() - 1) {
						combineArr_wl(list, list.get(i + 1), st, sb);
					} else if (i == list.size() - 1) {
						sb.append(st + "$");
					}
				}
			}
		}
	}

	/**
	 * 多数组组合问题
	 *
	 * @param list
	 * @param arr
	 * @param str
	 * @param sb
	 */
	private void combineArr_plus(List<String[]> list, String[] arr, String str,
							   StringBuffer sb) {
		for (int i = 0; i < list.size(); i++) {
			// 取得当前的数组
			if (i == list.indexOf(arr)) {
				// 迭代数组
				for (String st : arr) {
					st = (str.equals("") ? "" : (str + "+")) + st;
					if (i < list.size() - 1) {
						combineArr_plus(list, list.get(i + 1), st, sb);
					} else if (i == list.size() - 1) {
						sb.append(st + "$");
					}
				}
			}
		}
	}

	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	private static String[] permutation(String orginal) {
		ArrayList list = new ArrayList();
		if (orginal.length() == 1) {
			return new String[] { orginal };
		} else {
			for (int i = 0; i < orginal.length(); i++) {
				String s = orginal.charAt(i) + "";
				String result = "";
				String resultA = result + s;
				String leftS = orginal.substring(0, i)
						+ orginal.substring(i + 1, orginal.length());
				for (String element : permutation(leftS)) {
					result = resultA + element;
					list.add(result);
				}
			}
			return (String[]) list.toArray(new String[list.size()]);
		}
}
	public static void main(String[] args) {
		BaseLottery baseLottery = new BaseLottery();
		//B20130530301:3,0||h11-15,h16-20|/B20130525303:3||h11-15,h16-20|/B20130526301:3|||
		System.out.println(baseLottery.getCombinneArrSp("B20130530301:3,1,0/B20130525303:3/B20130526301:3,0"));
	}
}
