package com.example.campus_news.News;

import static android.os.Looper.getMainLooper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.campus_news.Adapter.MyCollectionsAdapter;
import com.example.campus_news.Adapter.MyPublishAdapter;
import com.example.campus_news.R;
import com.example.campus_news.StringEntity.NewsInfo;
import com.example.campus_news.Utils.CollectionSp;
import com.example.campus_news.Utils.DBUtils;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CollectionsActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private ImageView back;
    private List<NewsInfo> list = new ArrayList<>();
    private Handler mainHandler;
    private MyCollectionsAdapter myCollectionsAdapter;
    private SmartRefreshLayout smartRefreshLayout;
    private CollectionSp collectionSp;
    private Context context;

    private String[] newsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);

        recyclerView = findViewById(R.id.recycleView);
        back = findViewById(R.id.back);
        smartRefreshLayout = findViewById(R.id.refresh_parent);
        context = getApplicationContext();
        collectionSp = new CollectionSp(context);


        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(CollectionsActivity.this));
        smartRefreshLayout.setHeaderTriggerRate(0.5F);      //将刷新触发距离缩短至一半
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //刷新前要清空原先的数据
                list.clear();
                CollectionsFill();
                smartRefreshLayout.finishRefresh(1000);
            }
        });
        back.setOnClickListener(this);
        CollectionsFill();
    }

    private void CollectionsFill() {
        newsId = collectionSp.getCollections();
        mainHandler = new Handler(getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                list = DBUtils.getCollections(newsId);
                mainHandler.post(() -> {
                    myCollectionsAdapter = new MyCollectionsAdapter(list);
                    recyclerView.setAdapter(myCollectionsAdapter);
                    myCollectionsAdapter.setOnItemClickListener(new MyCollectionsAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(String id) {
                            deleteNewsDialog(id);
                        } //根据id执行删除新闻条目
                        @Override
                        public void onItemLongClick() {

                        }
                    });
                });
            }
        }).start();
    }
    public void deleteNewsDialog(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.alert)
                .setTitle("取消该收藏？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteNews(id);
                    }
                }).show();
    }
    //删除收藏
    private void deleteNews(String id) {
        collectionSp.remove(id);
        Toast.makeText(this, "已取消收藏!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back) {
            finish();
        }
    }
}