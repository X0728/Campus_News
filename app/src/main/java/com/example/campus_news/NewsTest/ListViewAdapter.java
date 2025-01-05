package com.example.campus_news.NewsTest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.campus_news.R;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {
    private List<String> data;
    private LayoutInflater inflater;
    public ListViewAdapter(Context context, List<String> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null){
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.item, viewGroup, false);
            viewHolder.text = view.findViewById(R.id.text);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.text.setText(data.get(i));
        return view;
    }
    private class ViewHolder{
        ImageView image;
        private TextView text;

    }
}
