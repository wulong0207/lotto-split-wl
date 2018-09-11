package com.hhly.lottosplit.utils.calcutils;

import java.util.ArrayList;
import java.util.List;

public class Percontion {

	private int total = 0;
	private ArrayList<String> arrangeList = new ArrayList<String>();

	private void swap(String list[], int k, int i) {
		String c3 = list[k];
		list[k] = list[i];
		list[i] = c3;
	}

	public void perm(String list[], int k, int m) {
		if (k > m) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i <= m; i++) {
				sb.append(list[i]).append(",");
			}
			if (sb.length() > 0) {
				sb.setLength(sb.length() - 1);
			}
			arrangeList.add(sb.toString());
			total++;
		} else {
			for (int i = k; i <= m; i++) {
				swap(list, k, i);
				perm(list, k + 1, m);
				swap(list, k, i);
			}
		}
	}
	
	/**
	 * @author wuLong
	 * @version 1.0
	 * @2010-8-22
	 * @description
	 * @path ecp888_4-->com.ecp888.utils.impl-->Percontion.java
	 * @param list 要生排列的字符串数组如 {"a","b","c"}
	 * @param k  启始位置 0
	 * @param m  结束位置 2 list.length
	 * @param result 
	 * @return 
	 * StringBuffer a,b,c|a,c,b|b,a,c|b,c,a|c,b,a|c,a,b
	 */
	public StringBuffer perm(String list[], int k, int m,StringBuffer result) {
		if (k > m) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i <= m; i++) {
				sb.append(list[i]).append(",");
			}
			if (sb.length() > 0 && sb.toString().endsWith(",")) {
				sb.setLength(sb.length() - 1);
			}
			result.append(sb.toString()).append("|");//arrangeList.add(sb.toString());
			total++;
			
		} else {
			for (int i = k; i <= m; i++) {
				swap(list, k, i);
				result = perm(list, k + 1, m,result);
				swap(list, k, i);
			}
		}
		//修改多去掉一位 wuLong 2010-9-2
		if(result.length()>0 && result.toString().endsWith(","))
			result.setLength(result.length()-1);
				
		return result;
	}
	 
	 
	 
	
	public int getTotal(){
		return this.total;
	}
	
	public int getPercontionCount(int n,int m) {
		int c = 1;
		for (int i=n-m; i<n; c*=++i);
		return c;
	}

	@SuppressWarnings("rawtypes")
	public ArrayList getPerconData() {
		return arrangeList;
	}

	@SuppressWarnings("rawtypes")
	public static void main(String args[]) {
//		long a = System.currentTimeMillis();
		String list[] = { "1", "2", "3", "4", "5" };
        BaseLottery base = new BaseLottery();
		List alist = base.getPercontionData(list,5);
		//List blist = base.getPercontionData(new String[] { "A", "A", "A","A","A","A" }, 5);
 
		////log.info(alist.size()+"  "+blist.size());
		
		Percontion ts = new Percontion();
		for(int i=0;i<alist.size();i++){
			String s = alist.get(i).toString();
			//log.info("--"+s);
			String[] tmp  = s.split(",");
			ts.perm(tmp, 0,2);
			for (int ii = 0; ii < ts.getPerconData().size(); ii++) {
				//log.info("++"+ts.getPerconData().get(ii));
			}
			
		}
		//log.info("combine 5:3-->"+base.getCombineCount(5, 3)+"  "+alist.size());
		//log.info("5:3-->"+ts.getPercontionCount(5, 3));
	}

}
