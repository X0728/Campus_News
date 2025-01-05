package com.example.campus_news.News;

import static android.os.Looper.getMainLooper;

import android.content.Intent;
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
import android.widget.Toast;

import com.example.campus_news.Adapter.NewsListAdapter;
import com.example.campus_news.Utils.ImageUtil;
import com.example.campus_news.StringEntity.BannerDataInfo;
import com.example.campus_news.StringEntity.NewsInfo;
import com.example.campus_news.Utils.DBUtils;
import com.example.campus_news.R;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
/*import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;*/
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment implements View.OnClickListener {
    private RecyclerView recyclerView;
    private NewsListAdapter newsListAdapter;    //recycle view适配器
    private Handler mainHandler;
    private List<NewsInfo> mNewsInfoList = new ArrayList<>();
    private List<NewsInfo> list = new ArrayList<>();
    private List<BannerDataInfo> bannerList = new ArrayList<>();
    private Banner banner;      //轮播控件
    private List<Bitmap> mBannerDataInfo = new ArrayList<>();  //轮播图片
    private List<String> listTitle = new ArrayList<>(); //轮播标题
    private SmartRefreshLayout smartRefreshLayout;
    private ImageView menu, proclamation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        banner = view.findViewById(R.id.banner);
        recyclerView = view.findViewById(R.id.listview);
        smartRefreshLayout = view.findViewById(R.id.refresh_parent);
        menu = view.findViewById(R.id.menu);  //菜单按钮
        proclamation = view.findViewById(R.id.proclamation);    //公告按钮
        menu.setOnClickListener(this);
        proclamation.setOnClickListener(this);


        //头部刷新
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        smartRefreshLayout.setHeaderTriggerRate(0.5F);      //将刷新触发距离缩短至一半
        //尾部刷新样式
        //smartRefreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //刷新前要清空原先的数据
                mNewsInfoList.clear();
                NewsInfoFill();
                smartRefreshLayout.finishRefresh(1000);
            }
        });
        /*尾部刷新（暂时不用）
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                NewsInfoFill();
                //重新刷新列表控件的数据
                newsListAdapter.notifyDataSetChanged();
                smartRefreshLayout.finishLoadMore(2000);

            }
        });*/
        NewsInfoFill();     //加载新闻列表
        BannerFill();

        //轮播图
        /*mBannerDataInfo.add(R.drawable.item);
        mBannerDataInfo.add(R.drawable.speak);
        mBannerDataInfo.add(R.drawable.hezaho);
        listTitle.add("二十大顺利召开");
        listTitle.add("中共中央国务院举行春节团拜会 习近平发表讲话");
        listTitle.add("习近平同党外人士共迎新春");
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(mBannerDataInfo).setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE)
                .setBannerTitles(listTitle)
                .setDelayTime(3000)
                .setBannerAnimation(Transformer.Tablet).setImageLoader(new GlideImageLoader()).start();*/

        return view;
    }
    //获得数据库中全部新闻条目
    private void NewsInfoFill() {
        mainHandler = new Handler(getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    list = DBUtils.getAllNews();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                mainHandler.post(() -> {
                    if (list != null) {
                        mNewsInfoList.addAll(list);
                    }
                    newsListAdapter = new NewsListAdapter(mNewsInfoList);
                    recyclerView.setAdapter(newsListAdapter);
                });
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.menu) {
            new XPopup.Builder(getContext())
                    .atView(menu)  // 依附于所点击的View，内部会自动判断在上方或者下方显示
                    .asAttachList(new String[]{"找朋友", "扫码"},
                            new int[]{R.drawable.findfriend, R.drawable.sao},
                            new OnSelectListener() {
                                @Override
                                public void onSelect(int position, String text) {
                                    Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                                }
                            })
                    .show();
        } else if (view.getId() == R.id.proclamation) {
            Intent intent = new Intent(getActivity(), Proclamation.class);
            view.getContext().startActivity(intent);

        }
    }
    //获取轮播图
    private void BannerFill() {
        mainHandler = new Handler(getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    bannerList = DBUtils.Banner();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                mainHandler.post(() -> {
                    for (int i = 0; i < bannerList.size(); i++) {
                        mBannerDataInfo.add(ImageUtil.base64ToImage(bannerList.get(i).getImage()));
                        listTitle.add(bannerList.get(i).getTitle());
                    }
                    /*mBannerDataInfo.add(ImageUtil.base64ToImage(bannerList.get(1).getImage()));
                    listTitle.add(bannerList.get(1).getTitle());*/
                    banner.setImageLoader(new GlideImageLoader());
                    banner.setImages(mBannerDataInfo).setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE)
                            .setBannerTitles(listTitle)
                            .setDelayTime(3000)
                            .setBannerAnimation(Transformer.Tablet).setImageLoader(new GlideImageLoader()).start();
                    });
            }
        }).start();
    }
}