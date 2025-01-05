package com.example.campus_news.News;

import static android.app.Activity.RESULT_OK;
import static android.os.Looper.getMainLooper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campus_news.StringEntity.NewsInfo;
import com.example.campus_news.Utils.DBUtils;
import com.example.campus_news.Utils.ImageUtil;
import com.example.campus_news.MainActivity;
import com.example.campus_news.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class PublishFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener, CompoundButton.OnCheckedChangeListener {
    private static final int SELECT_IMAGE = 1;      //请求码
    private  TextView et_title, et_content, tv_monitor1, tv_monitor2, tv_result;
    private ImageView image, publish;
    private Handler mainHandler;
    private String n_picture, phone, label_result;
    private Button customize;
    private CheckBox animal, travel, political, campus, find;
    private List<String> list = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_publish, container, false);
        et_title = view.findViewById(R.id.et_title);        //新闻标题
        et_content = view.findViewById(R.id.et_content);        //新闻内容
        image = view.findViewById(R.id.image);
        publish = view.findViewById(R.id.publish);      //新闻图片
        tv_monitor1 = view.findViewById(R.id.tv_monitor1);
        tv_monitor2 = view.findViewById(R.id.tv_monitor2);
        tv_result = view.findViewById(R.id.tv_result);
        customize = view.findViewById(R.id.customize);        //标签自定义按钮
        //标签复选框
        animal = view.findViewById(R.id.animal);
        travel = view.findViewById(R.id.travel);
        political = view.findViewById(R.id.political);
        campus = view.findViewById(R.id.campus);
       // find = view.findViewById(R.id.finding);


        image.setOnClickListener(this);
        image.setOnLongClickListener(this);
        tv_result.setOnLongClickListener(this);
        publish.setOnClickListener(this);
        customize.setOnClickListener(this);

        animal.setOnCheckedChangeListener(this);
        travel.setOnCheckedChangeListener(this);
        political.setOnCheckedChangeListener(this);
        campus.setOnCheckedChangeListener(this);
       // find.setOnCheckedChangeListener(this);

        //文本框监听
        et_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                int count = editable.length();
                tv_monitor1.setText("(" + count + "/30)");
                if (count >= 30) {
                    //et_title.setEnabled(false);
                    Toast.makeText(getActivity(), "已达最大长度", Toast.LENGTH_SHORT).show();
                }
            }
        });
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int count = editable.length();
                tv_monitor2.setText("(" + count + "/300)");
                if (count >= 300) {
                    //et_content.setEnabled(false);
                    Toast.makeText(getActivity(), "已达最大长度", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
    //获取当前登录的手机号
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        phone = ((MainActivity)context).toValue();
    }
    @Override
    public void onClick(View view) {
        String title = et_title.getText().toString();
        String content = et_content.getText().toString();
        String s = "";
        if (view.getId() == R.id.image) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, SELECT_IMAGE);
        } else if (view.getId() == R.id.customize) {
            showLabelDialog();
        } else if (view.getId() == R.id.publish) {
            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
                Toast.makeText(getActivity(), "请填写内容！", Toast.LENGTH_SHORT).show();
            } else if (list.size() > 0 || label_result.length() > 0) {
                if (list.size() > 0){
                    for (String item : list){
                        s = "#" + item;
                    }
                }
                if (!label_result.isEmpty() && label_result != null){
                    s = s + label_result;
                }
                createNewsInfo(title, content, phone, n_picture, s);
                Toast.makeText(getActivity(), "发布成功！", Toast.LENGTH_SHORT).show();
                //恢复原有状态
                et_title.setText("");
                et_content.setText("");
                tv_result.setText("");
                animal.setChecked(false);
                travel.setChecked(false);
                political.setChecked(false);
                campus.setChecked(false);
               // find.setChecked(false);
                image.setImageDrawable(null);
                image.setBackgroundResource(R.drawable.add_picture);
                list.clear();
            } else {
                Toast.makeText(getActivity(), "请选择标签！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //接受打开图库的结果
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            try {
                Uri selectedImageUri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                n_picture = ImageUtil.imageToBase64(bitmap);
                image.setBackground(null);
                image.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //插入新闻信息数据库
    public void createNewsInfo(String title, String content, String phone, String picture, String label) {
        NewsInfo item = new NewsInfo();
        item.setTitle(title);
        item.setContent(content);
        item.setUser(phone);
        item.setPicture(picture);
        item.setLabel(label);
        Log.i("112","1");
        mainHandler = new Handler(getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int Row = DBUtils.newsInfo(item);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        }).start();
    }

    //长按删除图片
    @Override
    public boolean onLongClick(View view) {
        if (view.getId() == R.id.image) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setIcon(R.drawable.alert)
                    .setTitle("删除该图片？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            image.setImageDrawable(null);
                            image.setBackgroundResource(R.drawable.add_picture);
                        }
                    }).show();
        } else if (view.getId() == R.id.tv_result) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setIcon(R.drawable.alert)
                    .setTitle("清空自定义标签？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            tv_result.setText("");
                        }
                    }).show();
        }
        return true;
    }
    //获取复选框内容
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            list.add(compoundButton.getText().toString().replace("#", ""));
        } else {
            list.remove(compoundButton.getText().toString());
        }
    }
    private void showLabelDialog() {
        final EditText inputServer = new EditText(getActivity());
        inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});//设置最多只能输入15个字符
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());//构建对话框，一个对话框，上面有输入框了，然后就还要有取消和确认键
        builder.setIcon(R.drawable.label).setView(inputServer)
                .setTitle("请输入便签(以#分割)：")
                .setNegativeButton("取消", null);//设置取消键
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {//设置确认键，且对确认键进行监听
            public void onClick(DialogInterface dialog, int which) {
                if (!inputServer.getText().toString().isEmpty()){
                    label_result = inputServer.getText().toString();//点击确认后获取输入框的内容
                }
                if (label_result != null && !label_result.isEmpty()) {//如果内容不为空，这个判断是为了防止空指针
                    tv_result.setText(label_result);
                } else {
                    Toast.makeText(getActivity(), "签名为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();
    }
}