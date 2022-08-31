package cn.aixuxi.ledger.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 日期工具类
 *
 * @author ruozhuliufeng
 */
public class DateUtil {
    /**
     * 获取当前系统时间最近12月的年月(含当月)
     */
    public static List<String> getLatest12Month() {
        List<String> latest12Months = new ArrayList<>(12);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1); //要先+1,才能把本月的算进去
        for (int i = 0; i < 12; i++) {
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1); //逐次往前推1个月
            String lastMonth = cal.get(Calendar.YEAR) + "-"+fillZero(cal.get(Calendar.MONTH) + 1);
            latest12Months.add(lastMonth);
        }
        return latest12Months;
    }

    /**
     * 格式化月份
     */
    public static String fillZero(int i) {
        String month = "";
        if (i < 10) {
            month = "0" + i;
        } else {
            month = String.valueOf(i);
        }
        return month;
    }
}
