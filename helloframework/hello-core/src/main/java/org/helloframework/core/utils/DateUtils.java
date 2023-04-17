package org.helloframework.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author lanjian
 */
public abstract class DateUtils {

    /**
     * 日期短格式yyyy-MM-dd
     */
    public static final String DATE_SHORT = "yyyy-MM-dd";

    /**
     * 24小时格式
     */
    public static final String DATE_MINUTES = "yyyy-MM-dd HH:mm:ss";

    /**
     * 12小时格式
     */
    public static final String DATE_MINUTES_12 = "yyyy-MM-dd hh-mm-ss";

    private static final String dateFormate_Now = "yyyyMMdd";

    private static final SimpleDateFormat dateformat = new SimpleDateFormat(DATE_MINUTES);

    private static final SimpleDateFormat dateformat_now = new SimpleDateFormat(dateFormate_Now);

    public static final String DATE_MINUTES_NO_SYMBOL = "yyyyMMddHHmmss";


//    public static final String ZERO_TIME = " 00:00:00";

    public static String getDateText(Date date) {
        if (date != null) {
            return dateformat.format(date);
        }
        return "";
    }

    public static Long zero() {
        Long time = System.currentTimeMillis(); // 当前时间的时间戳
        long zero = time / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();// 今天零点零分零秒的毫秒数
        return zero;
    }

    public static String getCurrentDateText() {
        Date date = new Date();
        return dateformat_now.format(date);
    }

    public static String dayMove(String date, int len) {
        return dateMove(date, len, Calendar.DATE, DATE_SHORT);
    }

    public static String dayMove(Date date, int len) {
        return dateMove(date, len, Calendar.DATE, DATE_SHORT);
    }

    public static String dayMove(Date date, int len, String pattern) {
        return dateMove(date, len, Calendar.DATE, pattern);
    }

    public static String dayMove(String date, int len, String pattern) {
        return dateMove(date, len, Calendar.DATE, pattern);
    }

    public static Date dayMoveNoFormat(Date date, int len) {
        return dateMoveToDate(date, len, Calendar.DATE);
    }

    public static String mouthMove(Date date, int len) {
        return dateMove(date, len, Calendar.MONTH, DATE_SHORT);
    }

    public static String mouthMove(String date, int len) {
        return dateMove(date, len, Calendar.MONTH, DATE_SHORT);
    }

    public static String mouthMove(Date date, int len, String pattern) {
        return dateMove(date, len, Calendar.MONTH, pattern);
    }

    public static String mouthMove(String date, int len, String pattern) {
        return dateMove(date, len, Calendar.MONTH, pattern);
    }

    public static String yearMove(String date, int len) {
        return dateMove(date, len, Calendar.YEAR, DATE_SHORT);
    }

    public static String houseMove(Date date, int len) {
        return dateMove(date, len, Calendar.HOUR, DATE_SHORT);
    }

    public static String yearMove(Date date, int len) {
        return dateMove(date, len, Calendar.YEAR, DATE_SHORT);
    }

    public static String yearMove(String date, int len, String pattern) {
        return dateMove(date, len, Calendar.YEAR, pattern);
    }

    public static String yearMove(Date date, int len, String pattern) {
        return dateMove(date, len, Calendar.YEAR, pattern);
    }


    private static String dateMove(String date, int len, int field, String pattern) {
        return dateMove(stringToDate(date, pattern), len, field, pattern);
    }


    private static String dateMove(Date date, int len, int field, String pattern) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(field, len);
        return dateToString(cal.getTime(), pattern);
    }


    public static int timeMove(Date date, int len, int field) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(field, len);
        return cal.get(field);
    }


    public static Date dateMoveToDate(Date date, int len, int field) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(field, len);
        return cal.getTime();
    }


    public static String time() {
        return dateToString(new Date(), DATE_MINUTES_12);
    }


    public static String time24() {
        return time24(new Date());
    }


    public static String time24(Date date) {
        return dateToString(date, DATE_MINUTES);
    }


    public static String nowDate() {
        return dateToString(new Date(), DATE_SHORT);
    }


    public static String nowDate(String p) {
        return dateToString(new Date(), p);
    }

    public static Long todayDateTime() {
        return stringToDate(dateToString(new Date(), DATE_SHORT)).getTime() / 1000;
    }

    public static Long dayDateTime(Date date) {
        return stringToDate(dateToString(date, DATE_SHORT)).getTime() / 1000;
    }

    public static Long nowDateTime() {
        return new Date().getTime() / 1000;
    }

    public static boolean isSameDate(Date date) {
        return dateToString(new Date()).equals(dateToString(date));
    }

    public static Date dateReducingTime(Date date, int minute) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, -minute);
        return calendar.getTime();
    }


    /**
     * 将当前时间减少几分钟
     *
     * @param minute
     * @return
     */
    public static Date currentDateReducingTime(int minute) {
        Calendar calendar = new GregorianCalendar();
        return dateReducingTime(calendar.getTime(), minute);
    }

    /**
     * @return String
     * @description -返回系统现在时间的毫秒数
     */
    public static String timeMilliseconds() {
        return String.valueOf(new Date().getTime());
    }

    /**
     * @param pattern -格式 <br>
     *                Date date -日期对象
     * @return String -日期字符串
     * @description 将日期对象date转化成格式pattern的日期字符串
     */
    public static String dateToString(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * @param date
     * @return String
     * @description 返回指定时间的字符串 (yyyy-MM-dd hh-mm-ss),12小时格式
     */
    public static String timeToString(Date date) {
        return dateToString(date, DATE_MINUTES_12);
    }

    public static String dateToString(Date date) {
        if (date != null) {
            return dateToString(date, DATE_SHORT);
        } else {
            return null;
        }
    }


    /**
     * @param dateStr -日期字符串 <br>
     *                String pattern -转化格式
     * @return Date -转化成功返回该格式的日期对象,失败返回null
     * @description -按格式pattern将字符串dateStr转化为日期
     */
    public static Date stringToDate(String dateStr, String pattern) {
        Date date = null;
        if (StringUtils.isNotBlank(dateStr)) {
            try {
                date = new SimpleDateFormat(pattern).parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    public static Date stringTo12Time(String timeStr) {
        return stringToDate(timeStr, DATE_MINUTES_12);
    }


    public static Date stringTo24Time(String timeStr) {
        return stringToDate(timeStr, DATE_MINUTES);
    }


    public static Date stringToDate(String dateStr) {
        return stringToDate(dateStr, DATE_SHORT);
    }


    public static String format(String dateString, String pattern) {
        String result = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            result = dateFormat.format(dateFormat.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static String format(String dateString) {
        return format(dateString, DATE_SHORT);
    }


    public static Date formatDate(Date date) {
        String dateString = dateToString(date);
        return stringToDate(dateString);
    }

    /**
     * 判断当前日期   > 传入的日期 返回true。
     * 判断当前日期   <= 传入的日期 返回false。
     *
     * @param vDate
     * @return
     */
    public static boolean isAfterDate(Date vDate) {
        Calendar nowDate = GregorianCalendar.getInstance();
        return nowDate.after(vDate);
    }

    /**
     * 判断日期的相隔天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int distDates(Date startDate, Date endDate) {
        long totalDate = 0;
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        Calendar endalendar = Calendar.getInstance();
        endalendar.setTime(endDate);
        long timestart = startCalendar.getTimeInMillis();
        long timeend = endalendar.getTimeInMillis();
        totalDate = Math.abs((timeend - timestart)) / (1000 * 60 * 60 * 24);
        return Long.valueOf(totalDate).intValue();
    }

    /**
     * 判断传入的日期与服务器当前日期的相隔天数
     *
     * @param vDate
     * @return
     */
    public static int distCurrentDates(Date vDate) {
        return distDates(new Date(), vDate);
    }

    /**
     * 判断日期相隔的时、分、秒
     */
    public static Long[] distTime(Date startDate, Date endDate) {
        long hour = 0;
        long min = 0;
        long sec = 0;
        if (startDate != null && endDate != null) {
            long time1 = startDate.getTime();
            long time2 = endDate.getTime();
            long diff;
            if (time1 < time2) {
                hour = 0L;
                min = 0L;
                sec = 0L;
            } else {
                diff = time1 - time2;
                hour = diff / (60 * 60 * 1000);
                min = ((diff / (60 * 1000)) - hour * 60);
                sec = (diff / 1000 - hour * 60 * 60 - min * 60);
            }
        }
        Long[] times = {hour, min, sec};
        return times;
    }

    /**
     * 判断日期相隔的天、时、分
     */
    public static Long[] distMinute(Date startDate, Date endDate) {
        long day = 0;
        long hour = 0;
        long min = 0;
        if (startDate != null && endDate != null) {
            long time1 = startDate.getTime();
            long time2 = endDate.getTime();
            long diff;
            if (time1 < time2) {
                day = 0L;
                hour = 0L;
                min = 0L;
            } else {
                diff = time1 - time2;
                day = diff / (1000 * 60 * 60 * 24);
                hour = (diff / (60 * 60 * 1000) - day * 24);
                min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            }
        }
        Long[] times = {day, hour, min};
        return times;
    }

    /**
     * 判断传入日期与服务器当期日期相隔的时、分、秒
     */
    public static Long[] distCurrentTime(Date vDate) {
        return distTime(vDate, new Date());
    }

    /**
     * 判断传入日期与服务器当期日期相隔的天、时、分
     */
    public static Long[] distCurrentMinute(Date vDate) {
        return distMinute(vDate, new Date());
    }

    /**
     * 返回当前日期的星期数
     * 星期天为1，星期一为：2星期二为3，以此类推星期天为7
     *
     * @return
     */
    public static int dayOfWeek() {
        Calendar nowDate = GregorianCalendar.getInstance();
        return nowDate.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取传入日期的星期
     *
     * @param date
     * @return
     */
    public static String dayWeek(Date date) {
        String[] weeks = {"sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayInweek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return weeks[dayInweek];
    }

    /**
     * 当前时间与传入时间的比较
     */
    public static boolean distTime(String startTime, String endTime) {
        Calendar nowTime = Calendar.getInstance();
        Calendar sTime = Calendar.getInstance();
        Calendar eTime = Calendar.getInstance();
        nowTime.set(Calendar.SECOND, 0);
        String[] sa = startTime.split(":");
        String[] ea = endTime.split(":");
        if (sa.length > 1 && ea.length > 1) {
            sTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(sa[0]));
            sTime.set(Calendar.MINUTE, Integer.valueOf(sa[1]));
            eTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(ea[0]));
            eTime.set(Calendar.MINUTE, Integer.valueOf(ea[1]));
            return nowTime.compareTo(sTime) > 0 && nowTime.compareTo(eTime) < 0;
        }
        return true;
    }


    public static Date lastWeekBegin() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -8);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        Calendar dayInWeek = (Calendar) calendar.clone();
        dayInWeek.add(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_WEEK) - dayOfWeek + 1);
        Date firstDayInWeek = dayInWeek.getTime();
        return formatDate(firstDayInWeek);
    }

    public static Date lastWeekEnd() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(lastWeekBegin());
        calendar.add(Calendar.DATE, 6);
        Date endDayInWeek = calendar.getTime();

        return formatDate(endDayInWeek);
    }

    /**
     * 返回date集合
     *
     * @param begin
     * @param end
     * @return
     */
    public static List<Date> days(String begin, String end) {
        int distDates = distDates(stringToDate(begin), stringToDate(end)) + 1;
        List<Date> days = new ArrayList<Date>();
        for (int i = 1; i <= distDates; i++) {
            days.add(dayMoveNoFormat(stringToDate(begin), distDates - i));
        }
        return days;
    }

    public static List<Date> days(Date begin, Date end) {
        int distDates = distDates(begin, end) + 1;
        List<Date> days = new ArrayList<Date>();
        for (int i = 1; i <= distDates; i++) {
            days.add(dayMoveNoFormat(begin, distDates - i));
        }
        return days;
    }

    public static List<Date> days(String dateStr) {
        Date date = stringToDate(dateStr);
        List<Date> days = new ArrayList<Date>();
        for (int i = 0; i < 2; i++) {
            days.add(DateUtils.dayMoveNoFormat(date, 0 - i));
        }
        return days;
    }

    public static Date minDays(int days) {
        DateTime dateTime = DateTime.now();
//        return new DateTime(dateTime.getYearOfCentury(),dateTime.getMonthOfYear(),dateTime.getMonthOfYear(),0,0,0).toDate();
        Date date = dateTime.minusDays(days).toDate();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        return date;
    }


    public static boolean belongToday(Date createAt) {
//        return new DateTime(dateTime.getYearOfCentury(),dateTime.getMonthOfYear(),dateTime.getMonthOfYear(),0,0,0).toDate();
        Date startdate = new Date();
        startdate.setHours(0);
        startdate.setMinutes(0);
        startdate.setSeconds(0);

        Date enddate = new Date();
        enddate.setHours(23);
        enddate.setMinutes(59);
        enddate.setSeconds(19);

        return createAt.after(startdate) && createAt.before(enddate);
    }

    public static Date nowPlusDays(int days) {
        return DateTime.now(DateTimeZone.getDefault()).plusDays(days).toDate();
    }

    public static Date datePlusDays(Date date, int days) {
        DateTime dateTime = new DateTime(date.getTime(), DateTimeZone.getDefault());
        return dateTime.plusDays(days).toDate();
    }

    public static void main(String[] a) {
//        System.out.println(DateUtil.minDays(7));
//        Date startdate = new Date();
//        startdate.setHours(21);
//        startdate.setMinutes(0);
//        startdate.setSeconds(0);
//        System.out.println(belongToday(startdate));
//
//        Date date = datePlusDays(new Date(), 3);
//        System.out.println(getDateText(date));
        System.out.println(getCurrentDateStartTime());
    }

    public static String getCurrentDateStartTime() {
        DateTime dateTime = new DateTime();
        int dateTimeYearOfCentury = dateTime.getYearOfCentury();
        int monthOfYear = dateTime.getMonthOfYear();
        int day = dateTime.getDayOfMonth();

        return (new StringBuffer(String.valueOf(dateTimeYearOfCentury)).append("-").append(monthOfYear).append("-").append(day).
                append(" ").append("00:00")).toString();
    }

    public static String getUnixTimeString(Long unixTime) {
        Date date = new Date();
        date.setTime(unixTime * 1000);
        return dateToString(date, DATE_MINUTES);
    }

    public static String getUnixTimeString(Long unixTime, String pattern) {
        Date date = new Date();
        date.setTime(unixTime * 1000);
        return dateToString(date, pattern);
    }

    public static String getUnixTimeString(Long unixTime, String pattern, String defaultValue) {
        if (unixTime == null || unixTime <= 0) {
            return defaultValue;
        }
        return getUnixTimeString(unixTime, pattern);
    }

    public static String labelTime(Long time) {
        DateTime labelTime = new DateTime(time * 1000);
        DateTime currentTime = new DateTime();
        if (currentTime.minusMinutes(1).isBefore(labelTime)) {
            return "刚刚";
        }
        if (currentTime.minusHours(1).isBefore(labelTime)) {
            return (currentTime.getMillis() - labelTime.getMillis()) / 60000 + "分钟前";
        }
        if (currentTime.getYear() == labelTime.getYear() && currentTime.getMonthOfYear() == labelTime.getMonthOfYear() && currentTime.getDayOfMonth() == labelTime.getDayOfMonth()) {
            return (currentTime.getHourOfDay() - labelTime.getHourOfDay()) + "小时前";
        }

        if (currentTime.getYear() == labelTime.getYear() && currentTime.getMonthOfYear() == labelTime.getMonthOfYear() && (currentTime.getDayOfMonth() - labelTime.getDayOfMonth()) == 1) {
            return "昨天" + formatDate(labelTime.toDate(), "HH:mm");
        }

        if (currentTime.getYear() == labelTime.getYear() && currentTime.getMonthOfYear() == labelTime.getMonthOfYear() && (currentTime.getDayOfMonth() - labelTime.getDayOfMonth()) == 2) {
            return "前天" + formatDate(labelTime.toDate(), "HH:mm");
        }
        return formatDate(labelTime.toDate(), "yyyy-MM-dd");
    }

    public static String formatDate(Date date, String pattern) {
        return dateToString(date, pattern);
    }

    public static Date parse(String createTime, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            return simpleDateFormat.parse(createTime);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String createUCTimeString(Date date) {
        DateTime dateTime = new DateTime(date);
        int year = dateTime.getYear();
        String month = dateTime.getMonthOfYear() > 9 ? String.valueOf(dateTime.getMonthOfYear()) : "0" + dateTime.getMonthOfYear();
        String day = dateTime.getDayOfMonth() > 9 ? String.valueOf(dateTime.getDayOfMonth()) : "0" + dateTime.getDayOfMonth();
        String hour = dateTime.getHourOfDay() > 9 ? String.valueOf(dateTime.getHourOfDay()) : "0" + dateTime.getHourOfDay();
        String minute = dateTime.getMinuteOfHour() > 9 ? String.valueOf(dateTime.getMinuteOfHour()) : "0" + dateTime.getMinuteOfHour();
        String second = dateTime.getSecondOfMinute() > 9 ? String.valueOf(dateTime.getSecondOfMinute()) : "0" + dateTime.getSecondOfMinute();
        return year + month + day + hour + minute + second;
    }

    public static long[] monthTimeArea(Date date) {
        long[] result = new long[2];
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        result[0] = cal.getTime().getTime() / 1000;
        cal.roll(Calendar.DAY_OF_MONTH, -1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        result[1] = cal.getTime().getTime() / 1000;
        return result;
    }

    public static String unixTimeToDateString(Long unixTime, String pattern) {
        Date date = new Date(unixTime * 1000);
        return dateToString(date, pattern);
    }

    /**
     * 昨天。
     */
    public static int yesterday() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.get(Calendar.YEAR) * 10000 + (cal.get(Calendar.MONTH) + 1) * 100 + cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 今天。
     */
    public static int today() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR) * 10000 + (cal.get(Calendar.MONTH) + 1) * 100 + cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 明天。
     */
    public static int tomorrow() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        return cal.get(Calendar.YEAR) * 10000 + (cal.get(Calendar.MONTH) + 1) * 100 + cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 后天。
     */
    public static int afterTomorrow() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 2);
        return cal.get(Calendar.YEAR) * 10000 + (cal.get(Calendar.MONTH) + 1) * 100 + cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 返回距离今天指定天数后的yyyyMMdd日期格式时间。
     */
    public static int ymdByDate(int offset) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, offset);
        return cal.get(Calendar.YEAR) * 10000 + (cal.get(Calendar.MONTH) + 1) * 100 + cal.get(Calendar.DAY_OF_MONTH);
    }

    public static int ymd(long ts) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(ts);
        return cal.get(Calendar.YEAR) * 10000 + (cal.get(Calendar.MONTH) + 1) * 100 + cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取日期字符串
     *
     * @param date
     * @param format
     * @return
     */
    public static String getDateString(Date date, String format) {
        if (null == date) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINA);
        return dateFormat.format(date);
    }
}
