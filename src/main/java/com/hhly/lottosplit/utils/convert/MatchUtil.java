package com.hhly.lottosplit.utils.convert;

import java.math.BigDecimal;

/**
 * 计算工具类
 *
 * @author jiangwei
 * @version 1.0
 * @desc
 * @date 2017年5月25日
 * @company 益彩网络科技公司
 */
public class MatchUtil {
    /**
     * 排列组合计算方式（Cnm）
     *
     * @param n 下标
     * @param m 上表
     * @return
     * @author jiangwei
     * @Version 1.0
     * @CreatDate 2017年5月25日 下午4:29:53
     */
    public static int pac(int n, int m) {
        if (m > n) {
            return 0;
        }
        if (m <= 0 || n <= 0) {
            return 0;
        }
        int up = 1;
        int down = 1;
        for (int i = 0; i < m; i++) {
            up *= (n - i);
            down *= (i + 1);
        }
        return up / down;
    }

    /**
     * @param value        double数据.
     * @param scale        精度位数(保留的小数位数).
     * @param roundingMode 精度取值方式.(如截取，四舍五入等)
     * @return 精度计算后的数据.
     * @Desc 对double数据进行取精度.
     */
    public static double round(double value, int scale, int roundingMode) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(scale, roundingMode);
        return bd.doubleValue();
    }

    /**
     * @param d1 double数据1
     * @param d2 double数据2
     * @return 浮点相加，精度计算后的数据.
     * @Desc 对double数据进行相加并取精度.
     */
    public static double sum(double d1, double d2) {
        try {
            BigDecimal bd1 = new BigDecimal(Double.toString(d1));
            BigDecimal bd2 = new BigDecimal(Double.toString(d2));
            return bd1.add(bd2).doubleValue();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * @param d1 double数据1
     * @param d2 double数据2
     * @return 浮点相减，精度计算后的数据.
     * @Desc 对double数据进行相减并取精度.
     */
    public static double sub(double d1, double d2) {
        try {
            BigDecimal bd1 = new BigDecimal(Double.toString(d1));
            BigDecimal bd2 = new BigDecimal(Double.toString(d2));
            return bd1.subtract(bd2).doubleValue();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * @param d1 double数据1
     * @param d2 double数据2
     * @return 浮点相乘，精度计算后的数据.
     * @Desc 对double数据进行相乘并取精度.
     */
    public static double mul(double d1, double d2) {
        try {
            BigDecimal bd1 = new BigDecimal(Double.toString(d1));
            BigDecimal bd2 = new BigDecimal(Double.toString(d2));
            return bd1.multiply(bd2).doubleValue();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * @param d1 double数据1
     * @param d2 double数据2
     * @return 浮点相除，精度计算后的数据.
     * @Desc 对double数据进行相除并取精度.
     */
    public static double div(double d1, double d2, int scale) {
        try {
            BigDecimal bd1 = new BigDecimal(Double.toString(d1));
            BigDecimal bd2 = new BigDecimal(Double.toString(d2));
            return bd1.divide(bd2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * @param d1           double数据1
     * @param d2           double数据2
     * @param scale        保留小数位
     * @param roundingMode 取舍模式
     * @return 浮点相除，精度计算后的数据.
     * @desc TODO
     * @author huangb
     * @date 2017年3月9日
     */
    public static BigDecimal div(double d1, double d2, int scale, int roundingMode) {
        try {
            BigDecimal bd1 = new BigDecimal(Double.toString(d1));
            BigDecimal bd2 = new BigDecimal(Double.toString(d2));
            return bd1.divide(bd2, scale, roundingMode);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    /**
     * @param d1 double数据1
     * @param d2 double数据2
     * @return 浮点数比较的返回结果
     * @Desc 浮点数取精度比较
     */
    public static int compareTo(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.compareTo(bd2);
    }

    public static void main(String[] args) {
        System.out.println(pac(7, 5) * 99 * 2);
    }
}
