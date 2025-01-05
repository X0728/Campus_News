package com.example.campus_news.News;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.campus_news.Adapter.NewsListAdapter;
import com.example.campus_news.NewsTest.ListViewAdapter;
import com.example.campus_news.R;
import com.example.campus_news.StringEntity.NewsInfo;

import java.util.ArrayList;
import java.util.List;


public class TabNewsFragment extends Fragment {
    private List<String> items;
    private ListView listView;

    private static final String ARG_PARAM = "title";
    private String title;
    private View view;
    private NewsListAdapter mNewsListAdapter;
    private List<NewsInfo> mNewsInfo = new ArrayList<>();
    private RecyclerView recyclerView;

    public TabNewsFragment() {
        // Required empty public constructor
    }


    public static TabNewsFragment newInstance(String param) {
        TabNewsFragment fragment = new TabNewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_news, container, false);
        //listView = view.findViewById(R.id.listview);
        listView.setAdapter(new ListViewAdapter(getActivity(), items));

        recyclerView = view.findViewById(R.id.listview);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化适配器
        mNewsListAdapter = new NewsListAdapter(mNewsInfo);
        recyclerView.setAdapter(mNewsListAdapter);
    }


}