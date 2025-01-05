package com.example.campus_news.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus_news.News.NewsContentActivity;
import com.example.campus_news.R;
import com.example.campus_news.StringEntity.NewsInfo;

import java.util.ArrayList;
import java.util.List;
//搜索结果适配器
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.MyHolder>{
    private List<NewsInfo> mNewsInfo = new ArrayList<>();
    public SearchResultAdapter(List<NewsInfo> list) {
        this.mNewsInfo = list;
    }
    @NonNull
    @Override
    public SearchResultAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result, null);
        return new MyHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SearchResultAdapter.MyHolder holder, int position) {
        NewsInfo newsInfo = mNewsInfo.get(position);
        if (newsInfo != null) {
            holder.publishUser.setText(newsInfo.getUser());
            holder.publishTitle.setText("\u00A0\u00A0"+newsInfo.getTitle());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NewsInfo newsInfo = mNewsInfo.get(position);
                    String id, phone;
                    id = String.valueOf(newsInfo.getId());
                    phone = newsInfo.getPhone();
                    Intent intent = new Intent(view.getContext(), NewsContentActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("phone", phone);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mNewsInfo.size();
    }
    public class MyHolder extends RecyclerView.ViewHolder {
        TextView publishUser, publishTitle;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            //初始化控件
            publishUser = itemView.findViewById(R.id.publish_name);
            publishTitle = itemView.findViewById(R.id.search_title);
        }
    }
}
