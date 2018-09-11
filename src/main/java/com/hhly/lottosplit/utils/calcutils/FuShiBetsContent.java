package com.hhly.lottosplit.utils.calcutils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class FuShiBetsContent implements Serializable {
	/**
	 * /**
	 * 
	 * <pre>
	 * 几串几(除几串一外，包含几串几关)
	 * 如 PasswayMap.put("4-5", "4-3,1-4");
	 * 表示 4-5 包括 4个3串1关,1个4串1关
	 * </pre>
	 */
	public final static Map<String, String> PasswayMap = new HashMap<String, String>();
	public final static Map<String, String> DayMap = new HashMap<String, String>();
	public final static Map<String, String> spfMap = new HashMap<String, String>();
	public final static Map<String, String> rqspfMap = new HashMap<String, String>();
	public final static Map<String, String> sumMap = new HashMap<String, String>();
	public final static Map<String, String> bfMap = new HashMap<String, String>();
	public final static Map<String, String> bqcMap = new HashMap<String, String>();
	public final static Map<String, String> spfMap1 = new HashMap<String, String>();
	public final static Map<String, String> rqspfMap2 = new HashMap<String, String>();
	public final static Map<String, String> bqcConvertMap = new HashMap<String, String>();
	/** 快捷投注map，胜或负对应上盘，让胜或让负对应下盘 */
	public final static Map<String, String> qbetMap = new HashMap<String, String>();

	public final static Map<String, String> sfMap = new HashMap<String, String>();
	public final static Map<String, String> rfsfMap = new HashMap<String, String>();
	public final static Map<String, String> sfcMap = new HashMap<String, String>();
	public final static Map<String, String> dxfMap = new HashMap<String, String>();
	/**
	 * 北单串关组合
	 */
	public final static Map<String, int[]> bdPassWayMapArr = new HashMap<String, int[]>();
	/**
	 * <pre>
	 * 几串几 包含子串数组
	 * 如  passWayMapArr.put("4-5", new int[] { 3, 4 });
	 * 表示 4-5 包括了 3串1关  和 4串1关
	 * </pre>
	 */
	public final static Map<String, int[]> passWayMapArr = new HashMap<String, int[]>();
	static {
		PasswayMap.put("2-1", "1-2");
		PasswayMap.put("3-1", "1-3");
		PasswayMap.put("3-3", "3-2");
		PasswayMap.put("3-4", "3-2,1-3");
		PasswayMap.put("4-1", "1-4");
		PasswayMap.put("4-4", "4-3");
		PasswayMap.put("4-5", "4-3,1-4");
		PasswayMap.put("4-6", "6-2");
		PasswayMap.put("4-11", "6-2,4-3,1-4");
		PasswayMap.put("5-1", "1-5");
		PasswayMap.put("5-5", "5-4");
		PasswayMap.put("5-6", "5-4,1-5");
		PasswayMap.put("5-10", "10-2");
		PasswayMap.put("5-16", "10-3,5-4,1-5");
		PasswayMap.put("5-20", "10-2,10-3");
		PasswayMap.put("5-26", "10-2,10-3,5-4,1-5");
		PasswayMap.put("6-1", "1-6");
		PasswayMap.put("6-6", "6-5");
		PasswayMap.put("6-7", "6-5,1-6");
		PasswayMap.put("6-15", "15-2");
		PasswayMap.put("6-20", "20-3");
		PasswayMap.put("6-22", "15-4,6-5,1-6");
		PasswayMap.put("6-35", "15-2,20-3");
		PasswayMap.put("6-42", "20-3,15-4,6-5,1-6");
		PasswayMap.put("6-50", "15-2,20-3,15-4");
		PasswayMap.put("6-57", "15-2,20-3,15-4,6-5,1-6");
		PasswayMap.put("7-1", "1-7");
		PasswayMap.put("7-7", "7-6");
		PasswayMap.put("7-8", "7-6,1-7");
		PasswayMap.put("7-21", "21-5");
		PasswayMap.put("7-35", "35-4");
		PasswayMap.put("7-120", "21-2,35-3,35-4,21-5,7-6,1-7");
		PasswayMap.put("8-1", "1-8");
		PasswayMap.put("8-8", "8-7");
		PasswayMap.put("8-9", "8-7,1-8");
		PasswayMap.put("8-28", "28-6");
		PasswayMap.put("8-56", "56-5");
		PasswayMap.put("8-70", "70-4");
		PasswayMap.put("8-247", "28-2,56-3,70-4,56-5,28-6,8-7,1-8");
		PasswayMap.put("8-255", "8-1,28-2,56-3,70-4,56-5,28-6,8-7,1-8");

		passWayMapArr.put("1-1", new int[] { 1 });
		passWayMapArr.put("2-1", new int[] { 2 });
		passWayMapArr.put("3-1", new int[] { 3 });
		passWayMapArr.put("3-3", new int[] { 2 });
		passWayMapArr.put("3-4", new int[] { 2, 3 });
		passWayMapArr.put("4-1", new int[] { 4 });
		passWayMapArr.put("4-4", new int[] { 3 });
		passWayMapArr.put("4-5", new int[] { 3, 4 });
		passWayMapArr.put("4-6", new int[] { 2 });
		passWayMapArr.put("4-11", new int[] { 2, 3, 4 });
		passWayMapArr.put("5-1", new int[] { 5 });
		passWayMapArr.put("5-5", new int[] { 4 });
		passWayMapArr.put("5-6", new int[] { 4, 5 });
		passWayMapArr.put("5-10", new int[] { 2 });
		passWayMapArr.put("5-16", new int[] { 3, 4, 5 });
		passWayMapArr.put("5-20", new int[] { 2, 3 });
		passWayMapArr.put("5-26", new int[] { 2, 3, 4, 5 });
		passWayMapArr.put("6-1", new int[] { 6 });
		passWayMapArr.put("6-6", new int[] { 5 });
		passWayMapArr.put("6-7", new int[] { 5, 6 });
		passWayMapArr.put("6-15", new int[] { 2 });
		passWayMapArr.put("6-20", new int[] { 3 });
		passWayMapArr.put("6-22", new int[] { 4, 5, 6 });
		passWayMapArr.put("6-35", new int[] { 2, 3 });
		passWayMapArr.put("6-42", new int[] { 3, 4, 5, 6 });
		passWayMapArr.put("6-50", new int[] { 2, 3, 4 });
		passWayMapArr.put("6-57", new int[] { 2, 3, 4, 5, 6 });
		passWayMapArr.put("7-1", new int[] { 7 });
		passWayMapArr.put("7-7", new int[] { 6 });
		passWayMapArr.put("7-8", new int[] { 6, 7 });
		passWayMapArr.put("7-21", new int[] { 5 });
		passWayMapArr.put("7-35", new int[] { 4 });
		passWayMapArr.put("7-120", new int[] { 2, 3, 4, 5, 6, 7 });
		passWayMapArr.put("8-1", new int[] { 8 });
		passWayMapArr.put("8-8", new int[] { 7 });
		passWayMapArr.put("8-9", new int[] { 7, 8 });
		passWayMapArr.put("8-28", new int[] { 6 });
		passWayMapArr.put("8-56", new int[] { 5 });
		passWayMapArr.put("8-70", new int[] { 4 });
		passWayMapArr.put("8-247", new int[] { 2, 3, 4, 5, 6, 7, 8 });
		
		
		bdPassWayMapArr.put("1-1", new int[] { 1 });
		bdPassWayMapArr.put("2-1", new int[] { 2 });
		bdPassWayMapArr.put("2-3", new int[] { 1,2 });
		bdPassWayMapArr.put("3-1", new int[] { 3 });
		bdPassWayMapArr.put("3-4", new int[] { 2, 3 });
		bdPassWayMapArr.put("3-7", new int[] { 1, 2, 3 });
		bdPassWayMapArr.put("4-1", new int[] { 4 });
		bdPassWayMapArr.put("4-5", new int[] { 3, 4 });
		bdPassWayMapArr.put("4-11", new int[] { 2, 3, 4 });
		bdPassWayMapArr.put("4-15", new int[] { 1, 2, 3, 4 });
		bdPassWayMapArr.put("5-1", new int[] { 5 });
		bdPassWayMapArr.put("5-6", new int[] { 4, 5 });
		bdPassWayMapArr.put("5-16", new int[] { 3, 4, 5 });
		bdPassWayMapArr.put("5-26", new int[] { 2, 3, 4, 5 });
		bdPassWayMapArr.put("5-31", new int[] { 1, 2, 3, 4, 5 });
		bdPassWayMapArr.put("6-1", new int[] { 6 });
		bdPassWayMapArr.put("6-7", new int[] { 5, 6 });
		bdPassWayMapArr.put("6-22", new int[] { 4, 5, 6 });
		bdPassWayMapArr.put("6-42", new int[] { 3, 4, 5, 6 });
		bdPassWayMapArr.put("6-57", new int[] { 2, 3, 4, 5, 6 });
		bdPassWayMapArr.put("6-63", new int[] { 1, 2, 3, 4, 5, 6 });
		bdPassWayMapArr.put("7-1", new int[] { 7 });
		bdPassWayMapArr.put("8-1", new int[] { 8 });
		bdPassWayMapArr.put("9-1", new int[] { 9 });
		bdPassWayMapArr.put("10-1", new int[] { 10 });
		bdPassWayMapArr.put("11-1", new int[] { 11 });
		bdPassWayMapArr.put("12-1", new int[] { 12 });
		bdPassWayMapArr.put("13-1", new int[] { 13 });
		bdPassWayMapArr.put("14-1", new int[] { 14 });
		bdPassWayMapArr.put("15-1", new int[] { 15 });

		DayMap.put("1", "周一");
		DayMap.put("2", "周二");
		DayMap.put("3", "周三");
		DayMap.put("4", "周四");
		DayMap.put("5", "周五");
		DayMap.put("6", "周六");
		DayMap.put("7", "周日");
		// 胜平负
		spfMap.put("3", "胜");
		spfMap.put("1", "平");
		spfMap.put("0", "负");
		// 让球胜平负
		rqspfMap.put("3", "让胜");
		rqspfMap.put("1", "让平");
		rqspfMap.put("0", "让负");
		// 奖金优让球胜平负
		rqspfMap2.put("rq_3", "让胜");
		rqspfMap2.put("rq_1", "让平");
		rqspfMap2.put("rq_0", "让负");
		// 奖金优化胜平负
		spfMap1.put("胜", "S");
		spfMap1.put("平", "P");
		spfMap1.put("负", "F");
		// 总进球数
		sumMap.put("0", "0");
		sumMap.put("1", "1");
		sumMap.put("2", "2");
		sumMap.put("3", "3");
		sumMap.put("4", "4");
		sumMap.put("5", "5");
		sumMap.put("6", "6");
		sumMap.put("7", "7+");
		// 比分
		bfMap.put("1-0", "1:0");
		bfMap.put("2-0", "2:0");
		bfMap.put("2-1", "2:1");
		bfMap.put("3-0", "3:0");
		bfMap.put("3-1", "3:1");
		bfMap.put("3-2", "3:2");
		bfMap.put("4-0", "4:0");
		bfMap.put("4-1", "4:1");
		bfMap.put("4-2", "4:2");
		bfMap.put("5-0", "5:0");
		bfMap.put("5-1", "5:1");
		bfMap.put("5-2", "5:2");
		bfMap.put("9-0", "胜其他");
		bfMap.put("0-0", "0:0");
		bfMap.put("1-1", "1:1");
		bfMap.put("2-2", "2:2");
		bfMap.put("3-3", "3:3");
		bfMap.put("9-9", "平其他");
		bfMap.put("0-1", "0:1");
		bfMap.put("0-2", "0:2");
		bfMap.put("1-2", "1:2");
		bfMap.put("0-3", "0:3");
		bfMap.put("1-3", "1:3");
		bfMap.put("2-3", "2:3");
		bfMap.put("0-4", "0:4");
		bfMap.put("1-4", "1:4");
		bfMap.put("2-4", "2:4");
		bfMap.put("0-5", "0:5");
		bfMap.put("1-5", "1:5");
		bfMap.put("2-5", "2:5");
		bfMap.put("0-9", "负其他");
		// 半全场
		bqcMap.put("3-3", "胜-胜");
		bqcMap.put("3-1", "胜-平");
		bqcMap.put("3-0", "胜-负");
		bqcMap.put("1-3", "平-胜");
		bqcMap.put("1-1", "平-平");
		bqcMap.put("1-0", "平-负");
		bqcMap.put("0-3", "负-胜");
		bqcMap.put("0-1", "负-平");
		bqcMap.put("0-0", "负-负");

		bqcConvertMap.put("33", "3-3");
		bqcConvertMap.put("31", "3-1");
		bqcConvertMap.put("30", "3-0");
		bqcConvertMap.put("13", "1-3");
		bqcConvertMap.put("11", "1-1");
		bqcConvertMap.put("10", "1-0");
		bqcConvertMap.put("03", "0-3");
		bqcConvertMap.put("01", "0-1");
		bqcConvertMap.put("00", "0-0");

		qbetMap.put("胜", "上盘");
		qbetMap.put("负", "上盘");
		qbetMap.put("让胜", "下盘");
		qbetMap.put("让负", "下盘");

		sfMap.put("3", "主胜");
		sfMap.put("0", "客胜");

		rfsfMap.put("3", "让分主胜");
		rfsfMap.put("0", "让分客胜");

		sfcMap.put("01", "主胜1-5");
		sfcMap.put("02", "主胜6-10");
		sfcMap.put("03", "主胜11-15");
		sfcMap.put("04", "主胜16-20");
		sfcMap.put("05", "主胜21-25");
		sfcMap.put("06", "主胜26+");
		sfcMap.put("11", "客胜1-5");
		sfcMap.put("12", "客胜6-10");
		sfcMap.put("13", "客胜11-15");
		sfcMap.put("14", "客胜16-20");
		sfcMap.put("15", "客胜21-25");
		sfcMap.put("16", "客胜26+");

		dxfMap.put("99", "大分");
		dxfMap.put("00", "小分");
	}
}
