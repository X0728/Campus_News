package com.example.campus_news.News;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.campus_news.Adapter.SearchResultAdapter;
import com.example.campus_news.StringEntity.NewsInfo;
import com.example.campus_news.Utils.DBUtils;
import com.example.campus_news.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//搜索结果页
public class SearchResultActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView back, notFound;
    private RecyclerView recyclerView;
    private String keyWord;
    private Handler mainHandler;
    private List<NewsInfo> list = new ArrayList<>();
    private SearchResultAdapter searchResultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        back = findViewById(R.id.back);
        recyclerView = findViewById(R.id.recycleView);
        notFound = findViewById(R.id.notFound);

        back.setOnClickListener(this);

        Intent intent = getIntent();
        keyWord = intent.getStringExtra("key");

        SearchNews(keyWord);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back) {
            finish();
        }
    }
    private void SearchNews(String keyWord) {
        mainHandler = new Handler(getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    list = DBUtils.SearchNews(keyWord);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                mainHandler.post(() -> {
                    if (list.size() > 0) {
                        searchResultAdapter = new SearchResultAdapter(list);
                        recyclerView.setAdapter(searchResultAdapter);
                    }else {
                        Toast.makeText(SearchResultActivity.this, "未找到相关内容！", Toast.LENGTH_SHORT).show();
                        notFound.setImageResource(R.drawable.notfound);
                    }

                });
            }
        }).start();
    }
}