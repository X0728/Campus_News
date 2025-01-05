package com.example.campus_news.News;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.example.campus_news.Adapter.ProclamationAdapter;
import com.example.campus_news.Utils.DBUtils;
import com.example.campus_news.R;
import com.example.campus_news.StringEntity.ProclamationData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Proclamation extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private ProclamationAdapter proclamationAdapter;
    private Handler mainHandler;
    private List<ProclamationData> mProclamation = new ArrayList<>();
    private List<ProclamationData> list = new ArrayList<>();
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proclamation);

        recyclerView = findViewById(R.id.recycleView);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);

        ProclamationFill();

    }
    //获得公告条目
    @SuppressLint("NotConstructor")
    private void ProclamationFill() {
        mainHandler = new Handler(getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    list = DBUtils.getProclamation();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                mainHandler.post(() -> {
                    if (list != null) {
                        mProclamation.addAll(list);
                    }
                    proclamationAdapter = new ProclamationAdapter(mProclamation);
                    recyclerView.setAdapter(proclamationAdapter);
                });
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back) {
            finish();
        }
    }
}