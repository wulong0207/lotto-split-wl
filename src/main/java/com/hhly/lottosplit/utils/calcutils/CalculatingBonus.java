package com.hhly.lottosplit.utils.calcutils;

import java.math.BigDecimal;

/**
 * 
 * @ClassName: CalculatingBonus 
 * @Description: 奖金计算工具类(四舍六入五成双)
 * @author wuLong
 * @date 2017年3月6日 下午3:48:57 
 *
 */
public class CalculatingBonus {

	/**
	 * 对于位数很多的近似数，当有效位数确定后，其后面多余的数字应该舍去，只保留有效数字最末一位，这种修约（舍入）规则是“四舍六入五成双”，
	 * 也即“4舍6入5凑偶”这里“四”是指≤4 时舍去，"六"是指≥6时进上，
	 * "五"指的是根据5后面的数字来定，当5后有数时，舍5入1；
	 * 当5后无有效数字时，需要分两种情况来讲：①5前为奇数，舍5入1；②5前为偶数，舍5不进。（0是偶数）
	 * mod  保留几位有效小数。 
	 * big 需要处理的数值
	 * @return
	 */
	public static double cauScale(int mod,BigDecimal big){
        //四舍六入
        if(mod<=0 || big.compareTo(BigDecimal.valueOf(0))<=0){
            return big.doubleValue();
        }
        String mathstr = String.valueOf(big.doubleValue()).toString();
        int dian = mathstr.indexOf(".");
        if(dian>0 && mathstr.length()- dian-1 >mod){
            String base = mathstr.substring(0,dian);
            String adress = mathstr.substring(dian+1,mathstr.length());
            if(adress.length()<=mod){
                base = base+"."+adress;
            }else if(adress.length()>=mod+1){
                int v = Integer.valueOf(adress.substring(mod,mod+1));//精确位小数后一位
                int v1 = Integer.valueOf(adress.substring(mod-1,mod));//精确位小数。
                int m =0 ;//是否需要进位。
                if(v>=6){ //精确位后大于等于6，精确位进一
                    m++;
                }else if(v<=4){//精确位后小于等于4，精确位后舍弃
                }else if(v==5 && v1%2==0){//精确位后为5时，精确位前为偶时，精确位后一位舍弃。
                }else if(v==5 && v1%2==1){//精确位后为5时，精确位前为奇时，精确位进一
                    m++;
                }
                String s =adress.substring(0,mod-1);
                base = base+"."+s+v1;
                if(m>0){
                    big = BigDecimal.valueOf(Double.valueOf(base)).add(BigDecimal.valueOf(Math.pow(0.1, mod)));
                }else{
                    big = BigDecimal.valueOf(Double.valueOf(base));
                }
            }
        } 
        return big.doubleValue();
    }
}
