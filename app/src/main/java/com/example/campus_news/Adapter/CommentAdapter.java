package com.example.campus_news.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus_news.StringEntity.CommentInfo;
import com.example.campus_news.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyHolder>{
    private List<CommentInfo> mCommentInfo = new ArrayList<>();
    public CommentAdapter(List<CommentInfo> list) {
        this.mCommentInfo = list;
    }
    @NonNull
    @Override
    public CommentAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.MyHolder holder, int position) {
        CommentInfo commentInfo = mCommentInfo.get(position);
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
        if (commentInfo != null) {
            holder.commentUser.setText(commentInfo.getName());
            holder.commentDate.setText(time.format(commentInfo.getDate()));
            holder.commentContent.setText(commentInfo.getContent());
        }

    }

    @Override
    public int getItemCount() {
        return mCommentInfo.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView commentUser, commentDate, commentContent;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            //初始化控件
            commentUser = itemView.findViewById(R.id.comment_user);
            commentDate = itemView.findViewById(R.id.comment_date);
            commentContent = itemView.findViewById(R.id.comment_content);
        }
    }
}
