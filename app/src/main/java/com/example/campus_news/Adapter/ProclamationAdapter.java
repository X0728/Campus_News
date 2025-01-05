package com.example.campus_news.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus_news.News.ProclamationContent;
import com.example.campus_news.R;
import com.example.campus_news.StringEntity.ProclamationData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ProclamationAdapter extends RecyclerView.Adapter<ProclamationAdapter.MyHolder>{
    private List<ProclamationData> mProclamationData = new ArrayList<>();
    public ProclamationAdapter(List<ProclamationData> list) {
        this.mProclamationData = list;
    }
    @NonNull
    @Override
    public ProclamationAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.proclamation_item, null);
        return new MyHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProclamationAdapter.MyHolder holder, int position) {
        ProclamationData proclamationData = mProclamationData.get(position);
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
        if (proclamationData != null) {
            String title, date;
            title = proclamationData.getTitle();
            date = time.format(proclamationData.getDate());
            holder.title.setText("\u00A0\u00A0" + title);
            holder.date.setText("\u00A0\u00A0" + date);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProclamationData proclamationData = mProclamationData.get(position);
                    String id;
                    id = String.valueOf(proclamationData.getId());
                    Intent intent = new Intent(view.getContext(), ProclamationContent.class);
                    intent.putExtra("id", id);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mProclamationData.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder {
        private TextView title, date;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            //初始化控件
            title = itemView.findViewById(R.id.proclamation_title);
            date = itemView.findViewById(R.id.proclamation_date);
        }
    }
}
