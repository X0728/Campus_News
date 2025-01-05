package com.example.campus_news.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class CollectionSp {
    private Context mContext;

    public CollectionSp() {
    }

    public CollectionSp(Context mContext) {
        this.mContext = mContext;
    }


    //定义一个保存数据的方法
    public void save(String newsId) {
        SharedPreferences sp = mContext.getSharedPreferences("myCollections", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String id = sp.getString("newsId", "");
        StringBuilder stringBuilder = new StringBuilder(id);
        stringBuilder.append(newsId + ",");

        if (!id.contains(newsId + ",")){
            editor.putString("newsId", stringBuilder.toString());
            editor.apply();
        }
        Toast.makeText(mContext, "已收藏", Toast.LENGTH_SHORT).show();
    }

    //定义一个读取SP文件的方法
    public Map<String, String> read() {
        Map<String, String> data = new HashMap<String, String>();
        SharedPreferences sp = mContext.getSharedPreferences("myCollection", Context.MODE_PRIVATE);
        data.put("newsId", sp.getString("newsId", ""));
        return data;
    }

    public String[] getCollections() {
        // 获取搜索记录文件内容
        SharedPreferences sp = mContext.getSharedPreferences("myCollections", Context.MODE_PRIVATE);
        String history = sp.getString("newsId", "");
        // 用逗号分割内容返回数组
        return history.split(",");
    }

    public void remove(String id) {
        String old = id + ",";
        String newString = "";
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("myCollections", Context.MODE_PRIVATE);
        String history = sharedPreferences.getString("newsId", "");
        String history1 = history.replace(old, newString);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putString("newsId", history1);
        editor.apply();
        Toast.makeText(mContext, "已取消收藏", Toast.LENGTH_SHORT).show();
    }

    public String[] getHistoryList() {
        // 获取搜索记录文件内容
        SharedPreferences sp = mContext.getSharedPreferences("search_history", 0);
        String history = sp.getString("history", "");
        // 用逗号分割内容返回数组
        String[] history_arr = history.split(",");
        // 保留前50条数据
        if (history_arr.length > 50) {
            String[] newArrays = new String[50];
            System.arraycopy(history_arr, 0, newArrays, 0, 50);
        }
        return history_arr;
    }
}
