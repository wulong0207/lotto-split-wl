package com.hhly.lottosplit.utils.calcutils;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * 
 * @ClassName: Combine 
 * @Description: 组合类
 * @author wuLong
 * @date 2017年3月6日 下午3:48:41 
 *
 */
public class Combine {

	@SuppressWarnings("rawtypes")
	private List combList = new ArrayList();
	
	@SuppressWarnings("rawtypes")
	public List mn(String[] array, int n) {
		List alist = new ArrayList();
		alist = this.mn(array, n, alist);
		this.combList = alist;
		return alist;
	}
	
	public List<String[]> mn4Arr(String[] array, int m) {
		List<String[]> list=new ArrayList<String[]>();
		int n = array.length;
		if (n < m)
			throw new IllegalArgumentException("Error   n   <   m");
		BitSet bs = new BitSet(n);
		for (int i = 0; i < m; i++) {
			bs.set(i, true);
		}
		do {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < n; i++)
				if (bs.get(i)) {
					sb.append(array[i]).append(',');
				}
			if(sb.length()>0){//zw edit
				sb.setLength(sb.length() - 1);
			}
			list.add(sb.toString().split(","));
		} while (moveNext(bs, n));
		return list;
	}

	/**
	 * 从数组的n个元素中取m个元素的组合取法
	 * @param array
	 * @param m
	 * @param list
	 * @return["1,2,3","2,3,4"]
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List mn(String[] array,int m,List list){
		int n = array.length;
		if (n < m)
			throw new IllegalArgumentException("Error   n   <   m");
		BitSet bs = new BitSet(n);
		for (int i = 0; i < m; i++) {
			bs.set(i, true);
		}
		do {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < n; i++)
				if (bs.get(i)) {
					sb.append(array[i]).append('/');
				}
			if(sb.length()>0){//zw edit
				sb.setLength(sb.length() - 1);
			}
			list.add(sb.toString());
		} while (moveNext(bs, n));
		return list;
	}
	/**
	 * 1、start 第一个true片段的起始位，end截止位 2、把第一个true片段都置false
	 * 3、数组从0下标起始到以第一个true片段元素数量减一为下标的位置都置true 4、把第一个true片段end截止位置true
	 * 
	 * @param bs
	 *            数组是否显示的标志位
	 * @param n
	 *            数组长度
	 * @return boolean 是否还有其他组合
	 */
	private boolean moveNext(BitSet bs, int n) {
		int start = -1;
		while (start < n)
			if (bs.get(++start))
				break;
		if (start >= n)
			return false;

		int end = start;
		while (end < n)
			if (!bs.get(++end))
				break;
		if (end >= n)
			return false;
		for (int i = start; i < end; i++)
			bs.set(i, false);
		for (int i = 0; i < end - start - 1; i++)
			bs.set(i);
		bs.set(end);
		return true;
	}
	

	/**
	 * 输出生成的组合结果
	 * 
	 * @param array
	 *            数组
	 * @param bs
	 *            数组元素是否显示的标志位集合
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	private void printAll(String[] array, BitSet bs) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; i++)
			if (bs.get(i)) {
				sb.append(array[i]).append(',');
			}
		sb.setLength(sb.length() - 1);
		combList.add(sb.toString());
	}

	/**
	 * 获取组合后的数组
	 * **/
	@SuppressWarnings("rawtypes")
	public List getCombineData() {
		////log.info(combList.size());
		return combList;
	}

	/**
	 * 从n个元素中取出m个元素的组和数
	 * @param m
	 * @param n
	 * @return
	 */
	public int getCombineCount(int m,int n){
		if(m < 0 || n < 0 || n < m){return 0;}	//当m小于0时返回0 by:liaoyuding
		if(m==0||n==0)return 1;//当m为0或者n 为 0 时,返回 1 by :zw
		int n1=1, n2=1;
		for (int i=n,j=1; j<=m; n1*=i--,n2*=j++);
		return n1/n2;
	}
	

}
