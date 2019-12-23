/**
 * @Creater zhangruifeng
 * Aug 20, 2008
 * @Version 1.0
 **/
package com.gpdi.operatingunit.utils;

import org.apache.commons.beanutils.ConversionException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {

    public static final String yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final SimpleDateFormat format4 = new SimpleDateFormat("yyyy-MM");

    private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat(
            "yyyy.MM.dd HH:mm:ss");

    private static ThreadLocal<Date> _tlNow = new ThreadLocal<Date>();

    /* @update zzl
     * @describe 添加更方便的调用接口
     * @time 2008-9-16 19:30:27
     */
    public static String toString(Date date) {
        return toString(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String toStringToMin(Date date) {
        return format3.format(date);
    }

    public static String toString(Date date, String format) {
        if (date == null) {
            return "";
        }
        return dateTimeToStr(date, format);
    }

    public static String toDateString(Date date) {
        if (date == null) {
            return "";
        }
        return dateTimeToStr(date, "yyyy-MM-dd");
    }

    /**
     * 日期时间转字符串
     *
     * @param date              DATE型
     * @param dateTimeFormatStr 格式
     * @return 示例：当前日期：2007年07月01日12时14分25秒。 dateTimeToStr(new Date(),"yyyy-MM-dd
     * HH:mm:ss"); 返回：2007-07-01 12:14:25
     */
    public static String dateTimeToStr(Date date, String dateTimeFormatStr) {
        String rsStr = null;
        if (dateTimeFormatStr != null) {
            SimpleDateFormat df = new SimpleDateFormat(dateTimeFormatStr);
            rsStr = df.format(date);
        } else {
            rsStr = dateTimeFormat.format(date);
        }
        return rsStr;
    }

    /**
     * 字符串转日期时间
     *
     * @param dateStr           字符串
     * @param dateTimeFormatStr 格式
     * @return 示例：当前日期：2007年07月01日12时14分25秒。 dateTimeToStr("2007-07-01
     * 12:14:25","yyyy-MM-dd HH:mm:ss"); 返回：Date
     */
    public static Date strToDateTime(String dateStr, String dateTimeFormatStr) {
        Date rsDate = null;
        if (dateTimeFormatStr != null) {
            SimpleDateFormat df = new SimpleDateFormat(dateTimeFormatStr);
            try {
                rsDate = df.parse(dateStr);
            } catch (Exception e) {
                rsDate = new Date();
                e.printStackTrace();
            }
        } else {
            try {
                rsDate = dateTimeFormat.parse(dateStr);
            } catch (Exception e) {
                rsDate = new Date();
                e.printStackTrace();
            }
        }
        return rsDate;
    }

    public static Date addDay(Date date, int dayCount) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.DAY_OF_MONTH, dayCount);
        return gc.getTime();
    }

//    public static int addDay(int date, int dayCount) {
//        Date date1 = addDay(fromIntDate(date), dayCount);
//        return toIntDate(date1);
//    }

    public static int addDay(int date, int dayCount) {
        int year = date / 10000;
        int month = (date / 100) % 100;
        int day = date % 100;
        Calendar cal = new GregorianCalendar(year, month - 1, day);
        cal.add(Calendar.DAY_OF_MONTH, dayCount);
        return cal.get(Calendar.YEAR) * 10000 +
                (cal.get(Calendar.MONTH) + 1) * 100 +
                cal.get(Calendar.DAY_OF_MONTH);
    }

    public static Date addMinute(Date date, int minuteCount) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minuteCount);
        return cal.getTime();
    }

    public static Date addSecond(Date date, int secondCount) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.SECOND, secondCount);
        return cal.getTime();
    }

    public static Date addHour(Date date, int hourCount) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, hourCount);
        return cal.getTime();
    }

    /**
     * by linjiekai
     * 增加几个小时
     *
     * @param hour
     * @return
     */
    public static Date addHour(int hour) {
        Calendar calendar = Calendar.getInstance();
        Date myDate = new Date();
        calendar.setTime(myDate);
        calendar.add(calendar.HOUR_OF_DAY, hour);
        return calendar.getTime();
    }

    /**
     * by linjiekai
     * 增加几分钟
     *
     * @param minute 分钟
     * @return
     */
    public static Date addMinute(int minute) {
        Calendar calendar = Calendar.getInstance();
        Date myDate = new Date();
        calendar.setTime(myDate);
        calendar.add(calendar.MINUTE, minute);
        return calendar.getTime();
    }

    /* @update zzl
     * @describe 获取星期几（０为星期天，１为星期一）
     * @time 2008-9-22 2:21:23
     */
    public static Integer getWeekNo(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /* @update zzl
     * @describe 从分钟转为时间(hh:mm)
     * @time 2008-9-22 12:26
     */
    public static String minuteToTime(Integer minute) {
        if (minute == null) {
            return "";
        }
        return String.format("%02d:%02d",
                (minute - minute % 60) / 60, minute % 60);
    }

    /* @update zzl
     * @describe 获取当前分钟数(0点算起)
     * @time 2008-9-23
     */
    public static Integer getMinute(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
    }

    /* @update zzl
     * @describe 截掉Date的时间部分
     * @time 2008-9-24 9:52
     */
    public static Date trunc(Date date) {
        if (date == null) {
            return null;
        }
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Integer dayDiff(Date startDate, Date endDate) {
        long msDiff = trunc(endDate).getTime() - trunc(startDate).getTime();
        return (int) (msDiff / (1000 * 60 * 60 * 24));
    }

    public static int monthDiff(Date startDate, Date endDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        int m1 = startCal.get(Calendar.YEAR) * 12 + startCal.get(Calendar.MONTH);
        int m2 = endCal.get(Calendar.YEAR) * 12 + endCal.get(Calendar.MONTH);
        return m2 - m1;
    }

    public static Date deriveDate(Date date, int hour, int minute, int second) {
        return deriveDate(date, null, hour, minute, second);
    }

    public static Date deriveDate(Date date, Integer day, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (day != null) {
            calendar.set(Calendar.DAY_OF_MONTH, day);
        }
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static boolean isBetween(Date stDt, Date endDt, Date date, boolean ignoreTime) {
        if (ignoreTime) {
            stDt = trunc(stDt);
            endDt = trunc(endDt);
            date = trunc(date);
        }
        if (stDt == null && endDt == null) {
            return false;
        }
        if (stDt != null) {
            return date.getTime() >= stDt.getTime();
        }
        if (endDt != null) {
            return date.getTime() <= endDt.getTime();
        }
        return date.getTime() >= stDt.getTime() && date.getTime() <= endDt.getTime();
    }

    //zzl
    public static Date addMonth(Date date, int monthCount) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.MONTH, monthCount);
        return cal.getTime();
    }

    public static int addMonth(int monthId, int monthCount) {
        Date date = fromIntDate(monthId * 100 + 1);
        Date date1 = addMonth(date, monthCount);
        return getMonthId(date1);
    }

    //liuc
    public static Date addYear(Date date, int yearCount) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.YEAR, yearCount);
        return cal.getTime();
    }

    public static Integer getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    /*
     * @return 返回月份（基数为1！！）
     */
    public static Integer getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    //
    public static Date date(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Date) {
            return (Date) obj;
        }
        if (obj instanceof String) {
            return convert((String) obj);
        }
        if (obj instanceof Long) {
            return new Date((Long) (obj));
        }
        return null;
    }

    public static Date convert(String str) {
        try {
            return format2.parse((String) str);
        } catch (Exception e) {
        }
        try {
            return format3.parse((String) str);
        } catch (Exception e) {
        }
        try {
            return format.parse((String) str);
        } catch (Exception e) {
        }
        try {
            return format4.parse((String) str);
        } catch (Exception e) {
            throw new ConversionException("Bad date format: " + str, e);
        }
    }

    public static Date getCurrentDate() {
        return new Date();
    }

    public static int toIntDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR) * 10000 +
                (cal.get(Calendar.MONTH) + 1) * 100 +
                cal.get(Calendar.DAY_OF_MONTH);
    }

    public static Integer toYearMonth(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR) * 100 +
                (cal.get(Calendar.MONTH) + 1);
    }

    public static Date newDate(int y, int m, int d) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, y);
        cal.set(Calendar.MONTH, m - 1);
        cal.set(Calendar.DAY_OF_MONTH, d);
        return cal.getTime();
    }

    /**
     * 取得当月天数
     */
    public static int getCurrentMonthDays() {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天  
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天  
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 得到指定月的天数
     */
    public static int getMonthDays(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天  
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天  
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 获取下月天数
     */
    public static int getNextMonthDays() {
        Calendar a = Calendar.getInstance();
        a.add(Calendar.MONTH, 1);
        a.set(Calendar.DATE, 1);//把日期设置为下月第一天
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 获取当月的当天
     */
    public static int getDayOfMonth() {
        Calendar a = Calendar.getInstance();
        int day = a.get(Calendar.DATE);
        return day;
    }

    public static int getDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DATE);
        return day;
    }

    /**
     * 获取当月的第一天
     */
    public static Date getFirstDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        return cal.getTime();
    }

    /**
     * 获取当月的最后一天
     */
    public static Date getLastDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    /**
     * 获取下月的第一天
     */
    public static Date getFirstDayOfNextMonth() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        return cal.getTime();
    }

    /**
     * 获取下月的最后一天
     */
    public static Date getLastDayOfNextMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);     // 设置当前月的1号
        cal.add(Calendar.MONDAY, 2);   // 加两个月，变为下下月的1号
        cal.add(Calendar.DATE, -1);    // 减一天，变为下月的最后一天
        return cal.getTime();
    }

    /**
     * 获取当月的第一天是星期几
     */
    public static  int getFirstDayWeekOfMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int day = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return day;
    }

    /**
     * 获取下月的第一天是星期几
     */
    public static  int getFirstDayWeekOfNextMonth() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int day = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return day;
    }

    public static Date parse(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        return convert(str);
    }

    public static Date today() {
        return trunc(new Date());
    }

    public static Date getToday() {
        return trunc(new Date());
    }

    public static Date now() {
        return new Date();
    }

    public static Date fromIntDate(Integer nDate) {
        if(nDate == null) {
            return null;
        }
        int year = nDate / 10000;
        int month = (nDate / 100) % 100;
        int day = nDate % 100;
        Calendar cal = new GregorianCalendar(year, month - 1, day);
        return cal.getTime();
    }

    public static Date getMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime();
    }

    public static String getWeekDay(int day) {
        Date date = fromIntDate(day);
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        int index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        String[] weekDays = new String[] {
                "周日", "周一", "周二", "周三", "周四", "周五", "周六"
        };
        return weekDays[index];
    }

    public static int getIntToday() {
        return toIntDate(today());
    }

    public static int getMonthId(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR) * 100 + cal.get(Calendar.MONTH) + 1;
    }

    public static int getMonthId() {
//        return getIntToday() / 100;
        return getMonthId(new Date());
    }

    public static int getLastMonthId() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        month -= 1;
        if(month < 1) {
            month = 12;
            year -= 1;
        }
        return year * 100 + month;
    }

    public static String formatMonth(int monthId) {
        return (monthId / 100) + "年" + (monthId % 100) + "月";
    }

    public static String formatDate(Integer date) {
        if(date == null) {
            return null;
        }
        int nDate = date;
        int year = nDate / 10000;
        int month = (nDate / 100) % 100;
        int day = nDate % 100;
        return year + "-" +
                (month < 10? "0": "") + month + "-" +
                (day < 10? "0": "") + day;
    }

    public static int getSundayOfWeek(int date) {
        int year = date / 10000;
        int month = (date / 100) % 100;
        int day = date % 100;
        Calendar cal = new GregorianCalendar(year, month - 1, day);
        int weekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if(weekDay == 0) {
            weekDay = 7;
        }
        int dayDiff = 7 - weekDay;
        if(dayDiff == 0) {
            return date;
        }
        else {
            cal.add(Calendar.DATE, dayDiff);
            return cal.get(Calendar.YEAR) * 10000 +
                    (cal.get(Calendar.MONTH) + 1) * 100 +
                    cal.get(Calendar.DAY_OF_MONTH);
        }
    }

    public static int getFirstDayOfMonth(int date) {
        return (date / 100) * 100 + 1;
    }

    public static int getIntTime() { //to minute
        Calendar cal = GregorianCalendar.getInstance();
        int h = cal.get(Calendar.HOUR_OF_DAY);
        int n = cal.get(Calendar.MINUTE);
        return h * 100 + n;
    }

    public static int minuteDiff(Date startDate, Date endDate) {
        long msDiff = endDate.getTime() - startDate.getTime();
        return (int) (msDiff / (1000 * 60));
    }

    public static int secondDiff(Date startDate, Date endDate) {
        long msDiff = endDate.getTime() - startDate.getTime();
        return (int) (msDiff / 1000);
    }

    public static int yearDiff(Date startDate, Date endDate) {
        Calendar a = Calendar.getInstance();
        a.setTime(startDate);

        Calendar b = Calendar.getInstance();
        b.setTime(endDate);

        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
        int aMonth = a.get(Calendar.MONTH);
        int bMonth = b.get(Calendar.MONTH);
        if (aMonth > bMonth || (aMonth == bMonth &&
                        a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
            --diff;
        }
        return diff;
    }

    public static int getAge(Date birthDate) {
        Calendar calBirth = new GregorianCalendar();
        calBirth.setTime(birthDate);

        Calendar calToday = new GregorianCalendar();

        int age = calToday.get(Calendar.YEAR) - calBirth.get(Calendar.YEAR);
        int m = calToday.get(Calendar.MONTH) - calBirth.get(Calendar.MONTH);
        if (m < 0 || (m == 0 &&
                calToday.get(Calendar.DAY_OF_MONTH) < calBirth.get(Calendar.DAY_OF_MONTH))) {
            --age;
        }
        return age;
    }

    public static int getIntYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return toIntDate(cal.getTime());
    }

    public static String toStrYm(Integer month) {
        Date date = DateUtils.fromIntDate(month * 100 + 1);
        return DateUtils.toString(date, "yyyy-MM");
    }
}
