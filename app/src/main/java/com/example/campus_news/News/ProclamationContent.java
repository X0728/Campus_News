package com.example.campus_news.News;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.campus_news.Utils.DBUtils;
import com.example.campus_news.R;
import com.example.campus_news.StringEntity.ProclamationData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProclamationContent extends AppCompatActivity implements View.OnClickListener {
    private TextView title, information, content;
    private ImageView picture, back;
    private Handler mainHandler;
    private String id;
    private List<ProclamationData> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proclamation_content);

        title = findViewById(R.id.title);
        information = findViewById(R.id.information);
        content = findViewById(R.id.content);
        picture = findViewById(R.id.picture);
        back = findViewById(R.id.back);

        back.setOnClickListener(this);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        if (id != null) {
            ProclamationItem();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back) {
            finish();
        }
    }

    private void ProclamationItem() {
        mainHandler = new Handler(getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                     HashMap<String, String> map = DBUtils.ProclamationContent(id);
                    if (map != null) {
                        mainHandler.post(() -> {
                            String p_title = map.get("title");
                            String p_content = map.get("content");
                            String p_information = map.get("publisher") + map.get("date");
                            title.setText(p_title);
                            information.setText(p_information + "发布");
                            content.setText("  " + p_content);
                        });
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}