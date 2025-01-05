package com.example.campus_news.Adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus_news.News.NewsContentActivity;
import com.example.campus_news.Utils.ImageUtil;
import com.example.campus_news.R;
import com.example.campus_news.StringEntity.NewsInfo;
/*import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;*/

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.MyHolder> {
    private List<NewsInfo> mNewsInfo = new ArrayList<>();
    private int itemCountToRefresh = 10;
    public NewsListAdapter(List<NewsInfo> list) {
        this.mNewsInfo = list;
    }

    @NonNull
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //加载布局文件
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        NewsInfo newsInfo = mNewsInfo.get(position);
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
        if (newsInfo != null) {
            String title, u_name, picture, date, label;
            title = newsInfo.getTitle();
            u_name = newsInfo.getUser();
            date = time.format(newsInfo.getTime());
            picture = newsInfo.getPicture();
            label = newsInfo.getLabel();
            holder.text.setText(title);
            holder.u_name.setText(u_name);
            holder.information.setText(date);
            holder.label.setText(label);
            holder.tv_browse.setText(String.valueOf(newsInfo.getBrowse()));
            holder.tv_comments.setText(String.valueOf(newsInfo.getComments()));
            if (newsInfo.getHead() != null) {
                Bitmap bitmap = ImageUtil.base64ToImage(newsInfo.getHead());
                holder.u_picture.setImageBitmap(bitmap);
            }else {
                holder.u_picture.setImageResource(R.drawable.campus_new_log);
            }
            if (picture != null) {
                Bitmap bitmap = ImageUtil.base64ToImage(picture);
                holder.image.setImageBitmap(bitmap);
            }else {
                holder.image.setImageResource(R.drawable.tentative2);
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NewsInfo newsInfo = mNewsInfo.get(position);
                    //将点击的item的id传入新activity
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
    //=====对外方法，用于分页添加数据
    @Override
    public int getItemCount() {
        return mNewsInfo.size();
    }

    // 刷新指定范围内的items
    public void refreshItems(int startPosition) {
        int endPosition = Math.min(startPosition + itemCountToRefresh, getItemCount()) - 1;
        for (int i = startPosition; i <= endPosition; i++) {
            // 更新items中的数据
           // mNewsInfo.set(i, );
        }
        notifyItemRangeChanged(startPosition, endPosition - startPosition + 1);
    }

    static class MyHolder extends RecyclerView.ViewHolder{
        TextView text, u_name, information, label, tv_comments, tv_browse;
        ImageView image, u_picture;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            //初始化控件
            text =itemView.findViewById(R.id.text);
            image =itemView.findViewById(R.id.image);
            u_name = itemView.findViewById(R.id.u_name);
            u_picture = itemView.findViewById(R.id.u_picture);
            information = itemView.findViewById(R.id.information);
            label = itemView.findViewById(R.id.label);
            tv_browse = itemView.findViewById(R.id.tv_browse);
            tv_comments = itemView.findViewById(R.id.tv_comments);
        }
    }
}
