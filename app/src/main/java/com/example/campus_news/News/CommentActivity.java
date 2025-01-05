package com.example.campus_news.News;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.campus_news.Adapter.CommentAdapter;
import com.example.campus_news.StringEntity.CommentInfo;
import com.example.campus_news.Utils.DBUtils;
import com.example.campus_news.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//评论页面
public class CommentActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView back;
    private EditText et_comment;
    private Button btn_comment;
    private RecyclerView recyclerView;
    private Handler mainHandler;
    private List<CommentInfo> list = new ArrayList<>();
    private List<CommentInfo> mComments = new ArrayList<>();
    private CommentAdapter commentAdapter;    //recycle view适配器

    private String id, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        back = findViewById(R.id.back);     //返回按钮
        et_comment = findViewById(R.id.et_comment);     //评论编辑
        btn_comment = findViewById(R.id.btn_comment);       //评论提交
        recyclerView = findViewById(R.id.recycleView);      //评论列表

        back.setOnClickListener(this);
        btn_comment.setOnClickListener(this);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        phone = intent.getStringExtra("phone");

        CommentFill();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back){
            finish();
        } else if (view.getId() == R.id.btn_comment) {
            String commentContent = et_comment.getText().toString();
            if (TextUtils.isEmpty(commentContent)) {
                Toast.makeText(this, "内容为空！", Toast.LENGTH_SHORT).show();
            }else {
                createComment(phone, commentContent, id);
            }
        }
    }

    //获得数据库中全部评论条目
    private void CommentFill() {
        mainHandler = new Handler(getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    list = DBUtils.getComments(id);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                mainHandler.post(() -> {
                    if (list != null) {
                        mComments.addAll(list);
                    }
                    commentAdapter = new CommentAdapter(mComments);
                    recyclerView.setAdapter(commentAdapter);
                });
            }
        }).start();
    }

    //创建评论
    public void createComment(String phone, String content, String newsId) {
        CommentInfo item = new CommentInfo();
        item.setPhone(phone);
        item.setContent(content);
        item.setNewsId(newsId);
        mainHandler = new Handler(getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                int Row;
                try {
                    Row = DBUtils.createComment(item);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (Row == 1) {
                            Toast.makeText(CommentActivity.this, "评论成功！", Toast.LENGTH_SHORT).show();
                            et_comment.setText("");
                            et_comment.setFocusable(false);
                        }else {
                            Toast.makeText(CommentActivity.this, "评论失败，稍后再试！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }


}