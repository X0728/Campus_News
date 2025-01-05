package com.example.campus_news.News;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campus_news.Adapter.CommentAdapter;
import com.example.campus_news.StringEntity.CommentInfo;
import com.example.campus_news.StringEntity.NewsInfo;
import com.example.campus_news.Utils.DBUtils;
import com.example.campus_news.Utils.ImageUtil;
import com.example.campus_news.R;
import com.example.campus_news.Utils.CollectionSp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewsContentActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView back, picture, comment, share;
    private TextView title, information, label, content, tv_thumbs_up;
    private CheckBox thumbs_up, collection;
    private EditText et_comment;
    private String id, c_title, u_name, c_picture, c_date, c_content, c_label, c_thumbs_up;
    private Handler mainHandler;
    private RecyclerView recyclerView;
    private String phone;
    private List<CommentInfo> list = new ArrayList<>();
    private List<CommentInfo> mComments = new ArrayList<>();
    private CommentAdapter commentAdapter;    //recycle view适配器

    private Context mContext;
    private CollectionSp collectionSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);

        back = findViewById(R.id.back);
        picture = findViewById(R.id.picture);
        title = findViewById(R.id.title);
        information = findViewById(R.id.information);
        label = findViewById(R.id.label);
        content = findViewById(R.id.content);
        tv_thumbs_up = findViewById(R.id.tv_thumbs_up);     //点赞数量显示
        thumbs_up = findViewById(R.id.thumbs_up);           //点赞按钮
        collection = findViewById(R.id.collection);         //收藏按钮
        share = findViewById(R.id.share);       //分享按钮
        comment = findViewById(R.id.comment);   //评论按钮

        back.setOnClickListener(this);
        //btn_comment.setOnClickListener(this);
        comment.setOnClickListener(this);

        mContext = getApplicationContext();
        collectionSp = new CollectionSp(mContext);

        thumbs_up.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int thumbs_up_num = Integer.parseInt(tv_thumbs_up.getText().toString());
                if (b) {
                    tv_thumbs_up.setText(String.valueOf(thumbs_up_num+1));
                }else {
                    tv_thumbs_up.setText(String.valueOf(thumbs_up_num-1));
                }
            }
        });

        collection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    collectionSp.save(id);
                }else {
                    collectionSp.remove(id);
                }
            }
        });

        //得到上个页面item的id
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        phone = intent.getStringExtra("phone");
        NewsItemInfo();
        //CommentFill();
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back) {
            upDateThumbs(Integer.parseInt(tv_thumbs_up.getText().toString()));
            finish();
        }else if(view.getId() == R.id.comment) {
            Intent intent = new Intent(NewsContentActivity.this, CommentActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("phone", phone);
            startActivity(intent);
        }
    }
    //获取新闻内容
    private void NewsItemInfo() {
        mainHandler = new Handler(getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HashMap<String, String> map = DBUtils.getNewsItem(id);
                    if (map != null) {
                        mainHandler.post(() -> {
                            c_title = map.get("title");
                            u_name = map.get("u_name");
                            c_picture = map.get("picture");
                            c_date = map.get("date");
                            c_content = map.get("content");
                            c_label = map.get("label");
                            c_thumbs_up = map.get("browse");
                            if (c_picture != null) {
                                Bitmap bitmap = ImageUtil.base64ToImage(c_picture);
                                picture.setImageBitmap(bitmap);
                            }else {
                                picture.setImageDrawable(null);
                            }
                            title.setText(c_title);
                            information.setText(u_name + c_date + "编辑");
                            label.setText(c_label);
                            content.setText("  " + c_content);
                            tv_thumbs_up.setText(c_thumbs_up);
                        });
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
    //更新点赞数量
    public void upDateThumbs(int thumbs) {
        NewsInfo item = new NewsInfo();
        item.setId(Integer.parseInt(id));
        item.setBrowse(thumbs);
        mainHandler = new Handler(getMainLooper());
        new Thread(new Runnable() {
            int Row;
            @Override
            public void run() {
                try {
                    Row = DBUtils.upDateThumbs(item);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                mainHandler.post(() -> {
                    if (Row != 1) {
                        Toast.makeText(getApplicationContext(), "发生错误，稍后再试！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

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
                            Toast.makeText(NewsContentActivity.this, "评论成功！", Toast.LENGTH_SHORT).show();
                            et_comment.setText("");
                            et_comment.setFocusable(false);
                        }else {
                            Toast.makeText(NewsContentActivity.this, "评论失败，稍后再试！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
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
}