package com.example.campus_news.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus_news.News.NewsContentActivity;
import com.example.campus_news.R;
import com.example.campus_news.StringEntity.NewsInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


//我的动态适配器
public class MyPublishAdapter extends RecyclerView.Adapter<MyPublishAdapter.MyHolder>{
    private List<NewsInfo> mNewsInfo = new ArrayList<>();
    public MyPublishAdapter(List<NewsInfo> list) {
        this.mNewsInfo = list;
    }
    private OnItemClickListener mOnItemClickListener;

    @NonNull
    public MyPublishAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //加载布局文件
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypublish_item, null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPublishAdapter.MyHolder holder, int position) {
        NewsInfo newsInfo = mNewsInfo.get(position);
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
        if (newsInfo != null) {
            String title, date, label;
            title = newsInfo.getTitle();
            date = time.format(newsInfo.getTime());
            label = newsInfo.getLabel();
            holder.title.setText("\u00A0\u00A0" + title);
            holder.date.setText(date);
            holder.label.setText(label);
        }
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewsInfo newsInfo = mNewsInfo.get(position);
                //将点击的item的id传入新activity
                String id;
                id = String.valueOf(newsInfo.getId());
                Intent intent = new Intent(view.getContext(), NewsContentActivity.class);
                intent.putExtra("id", id);
                view.getContext().startActivity(intent);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            //根据点击确定新闻条目id
            public void onClick(View view) {
                String id;
                id = String.valueOf(newsInfo.getId());
                if (null != mOnItemClickListener) {
                    mOnItemClickListener.onItemClick(id);
                }
            }
        });
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        public void onItemClick(String id);
        public void onItemLongClick();
    }

    @Override
    public int getItemCount() {
        return mNewsInfo.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder{
        TextView title, date, label;
        ImageView delete;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            //初始化控件
            title = itemView.findViewById(R.id.publish_title);
            date = itemView.findViewById(R.id.date);
            label = itemView.findViewById(R.id.publish_label);
            delete = itemView.findViewById(R.id.delete);
        }
    }


}
