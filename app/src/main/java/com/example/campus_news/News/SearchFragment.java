package com.example.campus_news.News;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campus_news.R;
import com.example.campus_news.Utils.KeyBoardUtils;
import com.example.campus_news.Utils.RegularUtils;
import com.example.campus_news.Utils.SPUtils;
import com.example.campus_news.Utils.ZFlowLayout;


public class SearchFragment extends Fragment {
    ZFlowLayout historyFl;
    EditText autoSearch;
    ImageView search;
    TextView clear;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        historyFl = view.findViewById(R.id.history);
        autoSearch = view.findViewById(R.id.search_context);
        search = view.findViewById(R.id.search_news);
        clear = view.findViewById(R.id.clear_history);
        initHistory();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (KeyBoardUtils.isSoftShowing(getActivity())) {
                    KeyBoardUtils.hintKeyboard(getActivity());
                }
                String searchKey = autoSearch.getText().toString();
                if (!isNullorEmpty(searchKey)) {
                    if (RegularUtils.hasEmoji(autoSearch.getText().toString())) {
                        //含有非法字符串
                        Toast.makeText(getActivity(), "搜索内容不合法！", Toast.LENGTH_SHORT).show();
                    } else {
                        //搜索
                        String keyWord = autoSearch.getText().toString();
                        if (!isNullorEmpty(keyWord)) {
                            SPUtils.getInstance(getActivity()).save(autoSearch.getText().toString());
                            Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                            intent.putExtra("key", keyWord);
                            startActivity(intent);
                        }
                        initHistory();
                    }
                } else {
                    //搜索为空
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SPUtils.cleanHistory();
                initHistory();
            }
        });
        return view;
    }


    private boolean isNullorEmpty(String str) {
        return str == null || "".equals(str);
    }
    /**
     * 初始化 历史记录列表
     */
    private void initHistory() {

        final String[] data = SPUtils.getInstance(getActivity()).getHistoryList();
        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 10, 10, 10);

        historyFl.removeAllViews();
        for (int i = 0; i < data.length; i++) {
            if (isNullorEmpty(data[i])) {

                return;
            }
            //有数据往下走
            final int j = i;
            //添加分类块
            View paramItemView = getLayoutInflater().inflate(R.layout.adapter_search_keyword, null);
            TextView keyWordTv = paramItemView.findViewById(R.id.tv_content);
            keyWordTv.setText(data[j]);
            historyFl.addView(paramItemView, layoutParams);

            keyWordTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (KeyBoardUtils.isSoftShowing(getActivity())) {
                        KeyBoardUtils.hintKeyboard(getActivity());
                    }
                    autoSearch.setText(data[j]);
                    autoSearch.setSelection(data[j].length());//光标在最后
                    if (!isNullorEmpty(data[j])) {
                        SPUtils.getInstance(getActivity()).save(autoSearch.getText().toString());
                    }
                    //点击事件
                }
            });
        }
    }
}
