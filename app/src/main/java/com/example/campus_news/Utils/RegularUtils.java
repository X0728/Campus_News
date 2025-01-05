package com.example.campus_news.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularUtils {
    /**
     * 校验字符串是否含有表情
     * @param content
     * @return
     */
    public static boolean hasEmoji(String content){

        Pattern pattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]");
        Matcher matcher = pattern.matcher(content);
        if(matcher .find()){
            return true;
        }
        return false;
    }
}
