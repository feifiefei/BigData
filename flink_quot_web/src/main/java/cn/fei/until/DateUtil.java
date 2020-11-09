package cn.fei.until;

import cn.fei.constant.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 日期工具类
 */
public class DateUtil {

    public static String getCurDate() {
        SimpleDateFormat si = new SimpleDateFormat(Constants.YYYYMMDD);
        String date = si.format(new Date());
        return date;
    }

    public static String getLineCurDate() {
        SimpleDateFormat si = new SimpleDateFormat(Constants.YYYY_MM_DD);
        String date = si.format(new Date());
        return date;
    }

    public static long getCurDateMs(String date) {
        SimpleDateFormat si = new SimpleDateFormat(Constants.YYYYMMDDHHMMSS);
        long time = 0;
        try {
            time = si.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String getCurDateMsTime(String date) {
        SimpleDateFormat si = new SimpleDateFormat(Constants.YYYYMMDDHHMMSS);
        String formatDate = null;
        try {
            Date parseDate = si.parse(date);
            formatDate = si.format(parseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatDate;
    }

    public static String getSysDateFromStringTime(String date) throws ParseException {

        SimpleDateFormat sf = new SimpleDateFormat(Constants.YYYYMMDDHHMMSS2);
        Date time = sf.parse(date);
        SimpleDateFormat sf2 = new SimpleDateFormat(Constants.YYYYMMDDHHMMSS);
        return sf2.format(time).trim();
    }

    public static Map<String, Long> getCurMinTime() {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        System.out.println(cal.getTime());
        long startMin = cal.getTime().getTime();

        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        System.out.println(cal.getTime());
        long endMin = cal.getTime().getTime();

        HashMap<String, Long> map = new HashMap<>();
        map.put("startMinTime", startMin);
        map.put("endMinTime", endMin);
        return map;
    }

    /**
     * 秒级行情最新查询起止时间
     * 样例：startSecTime=20200804170700, endSecTime=20200804170759
     */
    public static Map<String, String> getCurSecTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.MM);
        Date date = new Date();
        String minStr = simpleDateFormat.format(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MINUTE, Integer.valueOf(minStr));
        cal.set(Calendar.SECOND, 0);
        Date time = cal.getTime();
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(Constants.YYYYMMDDHHMMSS);
        String startMin = simpleDateFormat2.format(time);

        cal.set(Calendar.SECOND, 59);
        time = cal.getTime();
        String endMin = simpleDateFormat2.format(time);

        HashMap<String, String> map = new HashMap<>();
        map.put("startSecTime", startMin);
        map.put("endSecTime", endMin);
        return map;
    }

    public static String getHHmmss(String date) {

        SimpleDateFormat sf = new SimpleDateFormat(Constants.HHMMSS);
        String format = sf.format(new Date(Long.valueOf(date)));
        return format;
    }

    /**
     * Long型日期格式化
     */
    public static String formatLongTime(Long time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.YYYYMMDDHHMMSS2);
        String date = simpleDateFormat.format(time);
        return date;
    }

    /**
     * 秒级行情日期格式化
     */
    public static String formatSecTime(Long time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.HHMMSS);
        String date = simpleDateFormat.format(time);
        return date;
    }


    public static void main(String[] args) throws Exception {

        System.out.println(getCurSecTime());

    }
}
