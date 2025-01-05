package com.example.campus_news.News;

import static android.os.Looper.getMainLooper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campus_news.Adapter.MyPublishAdapter;
import com.example.campus_news.StringEntity.NewsInfo;
import com.example.campus_news.Utils.DBUtils;
import com.example.campus_news.Utils.ImageUtil;
import com.example.campus_news.MainActivity;
import com.example.campus_news.R;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//个人动态页面
public class MyPublishFragment extends Fragment {
    private RecyclerView recyclerView;
    private MyPublishAdapter myPublishAdapter;    //recycle view适配器
    private Handler mainHandler;
    private List<NewsInfo> mNewsInfoList = new ArrayList<>();
    private List<NewsInfo> list = new ArrayList<>();
    private SmartRefreshLayout smartRefreshLayout;
    private String phone;       //用户手机号
    private ImageView u_picture;
    private TextView u_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_publish, container, false);

        recyclerView = view.findViewById(R.id.recycleView);
        u_name = view.findViewById(R.id.u_name);
        u_picture = view.findViewById(R.id.u_picture);
        UserInfo();
        NewsInfoFill();     //加载用户动态列表
        smartRefreshLayout = view.findViewById(R.id.refresh_parent);
        //头部刷新
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        smartRefreshLayout.setHeaderTriggerRate(0.5F);      //将刷新触发距离缩短至一半
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //刷新前要清空原先的数据
                mNewsInfoList.clear();
                NewsInfoFill();
                smartRefreshLayout.finishRefresh(1000);
            }
        });

        return view;
    }
    //获取当前登录的手机号
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        phone = ((MainActivity)context).toValue();
    }
    private void UserInfo() {
        mainHandler = new Handler(getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String , String> map = DBUtils.getAllInfo(phone);
                mainHandler.post(() -> {
                    if (map != null) {
                        if (map.get("picture") != null) {
                            Bitmap bitmap = ImageUtil.base64ToImage(map.get("picture"));
                            u_picture.setImageBitmap(bitmap);
                        }
                        u_name.setText(map.get("name")+"的动态");
                    }
                });
            }
        }).start();
    }
    //填充用户动态列表
    private void NewsInfoFill() {
        mainHandler = new Handler(getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    list = DBUtils.getUserNews(phone);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                mainHandler.post(() -> {
                    if (list != null) {
                        mNewsInfoList.addAll(list);
                    }
                    myPublishAdapter = new MyPublishAdapter(mNewsInfoList);
                    recyclerView.setAdapter(myPublishAdapter);
                    myPublishAdapter.setOnItemClickListener(new MyPublishAdapter.OnItemClickListener() {
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
    //删除新闻提示框
    public void deleteNewsDialog(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.alert)
                .setTitle("删除该记录？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteNews(id);
                    }
                }).show();
    }
    //删除新闻
    private void deleteNews(String id) {
        mainHandler = new Handler(getMainLooper());
        new Thread(new Runnable() {
            int Row = 0;
            @Override
            public void run() {
                try {
                    Row = DBUtils.deleteNews(id);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                mainHandler.post(() -> {
                    if (Row == 1) {
                        Toast.makeText(getActivity(), "删除成功！", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(), "删除失败，请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }
}