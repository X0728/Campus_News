package com.example.campus_news.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class RememberPwdSp {
    private Context mContext;

    public RememberPwdSp() {
    }

    public RememberPwdSp(Context mContext) {
        this.mContext = mContext;
    }



    //定义一个保存数据的方法
    public void save(String account, String pwd) {
        SharedPreferences sp = mContext.getSharedPreferences("myPwd", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("account", account);
        editor.putString("pwd", pwd);
        editor.apply();
    }
    //定义一个读取SP文件的方法
    public Map<String, String> read() {
        Map<String, String> data = new HashMap<String, String>();
        SharedPreferences sp = mContext.getSharedPreferences("myPwd", Context.MODE_PRIVATE);
        data.put("account", sp.getString("account", ""));
        data.put("pwd", sp.getString("pwd", ""));
        return data;
    }
    public void remove() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("myPwd", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
