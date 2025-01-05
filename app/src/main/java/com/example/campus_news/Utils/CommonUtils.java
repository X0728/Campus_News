package com.example.campus_news.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

//自定义工具类
public class CommonUtils {
    /**
     * 获取当前时间的字符串
     * @return "yyyy-MM-dd HH:mm:ss" 格式的时间字符串
     */
    public static String getDateStrFromNow(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }
    /**
     * 从日期时间中获取时间字符串
     * @param dt 日期时间
     * @return "yyyy-MM-dd HH:mm:ss" 格式的时间字符串
     */
    public static String getStrFromDate(Date dt){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(dt);
    }
}
