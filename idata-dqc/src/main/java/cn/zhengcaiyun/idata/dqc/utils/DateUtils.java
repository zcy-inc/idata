package cn.zhengcaiyun.idata.dqc.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {
    /**
     *
     * @param date
     * @param formate yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String formateDate(Date date, String formate) {
        if (date == null) {
            return "";
        }
        try {
            LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
            DateTimeFormatter format = DateTimeFormatter.ofPattern(formate);
            return ldt.format(format);
        } catch (DateTimeException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public static Date convertDate(String text, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            return formatter.parse(text);
        } catch (ParseException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }


}
