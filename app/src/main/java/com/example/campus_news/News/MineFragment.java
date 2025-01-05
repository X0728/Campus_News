package com.example.campus_news.News;

import static android.app.Activity.RESULT_OK;
import static android.os.Looper.getMainLooper;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campus_news.Utils.DBUtils;
import com.example.campus_news.Utils.ImageUtil;
import com.example.campus_news.MainActivity;
import com.example.campus_news.R;
import com.example.campus_news.SetActivity;
import com.example.campus_news.StringEntity.UserInfo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;


public class MineFragment extends Fragment implements View.OnClickListener {
    private int choice = -1;
    private Button user_image;
    private ImageView image_show;
    private static final int SELECT_IMAGE = 1;
    private ImageView user_name;
    private ImageView user_phone;
    private ImageView user_birthday;
    private ImageView user_email;
    private ImageView user_sex;
    private ImageView collection;
    private TextView name_show;
    private TextView sex_show;
    private TextView phone_show;
    private TextView birthday_show;
    private TextView email_show;
    private Handler mainHandler;
    private String name, phone, sex, birthday, email, O_phone;
    private String u_name, u_phone, u_sex, u_birthday, u_email;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        // Inflate the layout for this fragment
        user_image = view.findViewById(R.id.user_image);
        image_show = view.findViewById(R.id.image_show);
        user_name =  view.findViewById(R.id.user_name);
        user_phone = view.findViewById(R.id.user_phone);
        user_birthday = view.findViewById(R.id.user_birthday);
        user_email = view.findViewById(R.id.user_email);
        user_sex = view.findViewById(R.id.user_sex);
        name_show = view.findViewById(R.id.name_show);
        sex_show = view.findViewById(R.id.sex_show);
        phone_show = view.findViewById(R.id.phone_show);
        birthday_show = view.findViewById(R.id.birthday_show);
        email_show = view.findViewById(R.id.email_show);
        collection = view.findViewById(R.id.collection);
        view.findViewById(R.id.setting).setOnClickListener(this);

        user_image.setOnClickListener(this);
        user_name.setOnClickListener(this);
        user_sex.setOnClickListener(this);
        user_phone.setOnClickListener(this);
        user_birthday.setOnClickListener(this);
        user_email.setOnClickListener(this);
        collection.setOnClickListener(this);
        initInfo();
        return view;
    }

    //获取登录页面的手机号
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        u_phone = ((MainActivity)context).toValue();
    }

    //初始化数据
    private void initInfo() {
        mainHandler = new Handler(getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = DBUtils.getAllInfo(u_phone);
                if (map != null) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            u_name = map.get("name").toString();
                            O_phone = map.get("phone").toString();
                            u_sex = map.get("sex").toString();
                            u_birthday = map.get("birthday").toString();
                            u_email = map.get("email").toString();
                            name_show.setText(u_name);
                            phone_show.setText(O_phone);
                            sex_show.setText(u_sex);
                            birthday_show.setText(u_birthday);
                            email_show.setText(u_email);
                            if (map.get("picture") != null){
                                String picture = map.get("picture").toString();
                                Bitmap bitmap = ImageUtil.base64ToImage(picture);
                                image_show.setImageBitmap(bitmap);
                            }
                        }
                    });
                } else {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "加载错误！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.user_image){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, SELECT_IMAGE);
        } else if (v.getId() == R.id.user_name) {
            showEditDialog();
        } else if (v.getId() == R.id.user_sex) {
            showRadioDialog();
        } else if (v.getId() == R.id.user_phone) {
            showEditPhoneDialog();
        } else if (v.getId() == R.id.user_birthday) {
            showTime();
        } else if (v.getId() == R.id.user_email) {
            Log.i("111","22");
            showEmailDialog();
        } else if (v.getId() == R.id.setting) {
            Log.i("111","22");
            startActivity(new Intent(getActivity(), SetActivity.class));
        } else if (v.getId() == R.id.collection) {
            startActivity(new Intent(getActivity(), CollectionsActivity.class));
        }
    }
    //更换头像
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            try {
                Uri selectedImageUri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                String u_picture = ImageUtil.imageToBase64(bitmap);
                UserInfo item = new UserInfo();
                item.setU_picture(u_picture);
                item.setPhone(u_phone);
                mainHandler = new Handler(getMainLooper());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int Row = DBUtils.picture(item);
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
                image_show.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showEditDialog() {
        final EditText inputServer = new EditText(getActivity());
        inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});//设置最多只能输入50个字符
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());//构建对话框，一个对话框，上面有输入框了，然后就还要有取消和确认键
        builder.setIcon(R.drawable.user_name).setView(inputServer)
                .setTitle("请输入新昵称：")
                .setNegativeButton("取消", null);//设置取消键
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {//设置确认键，且对确认键进行监听
            public void onClick(DialogInterface dialog, int which) {
                name = inputServer.getText().toString();//点击确认后获取输入框的内容
                if (name != null && !name.isEmpty()) {//如果内容不为空，这个判断是为了防止空指针
                    modifyInfo(O_phone, name, u_sex, u_birthday, u_email, O_phone);
                    name_show.setText(name);
                } else {
                    Toast.makeText(getActivity(), "签名为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();
    }

    private void showRadioDialog() {
        final String[] item = { "男", "女" };
        AlertDialog.Builder singleDialog = new AlertDialog.Builder(getActivity());
        singleDialog.setIcon(R.drawable.sex);
        singleDialog.setTitle("请选择");
        singleDialog.setSingleChoiceItems(item, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                choice = which;
            }
        });
        singleDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sex = item[choice];
                modifyInfo(O_phone, u_name, sex, u_birthday, u_email, O_phone);
                sex_show.setText(sex);
                Toast.makeText(getActivity(), "你选择了：" + sex, Toast.LENGTH_SHORT).show();
            }
        });
        singleDialog.show();
    }
    private void showEditPhoneDialog() {
        final EditText inputServer = new EditText(getActivity());
        inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});//设置最多只能输入11个字符
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());//构建对话框，一个对话框，上面有输入框了，然后就还要有取消和确认键
        builder.setIcon(R.drawable.phone).setView(inputServer)
                .setTitle("请输入新手机号：")
                .setNegativeButton("取消", null);//设置取消键
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {//设置确认键，且对确认键进行监听
            public void onClick(DialogInterface dialog, int which) {
                phone = inputServer.getText().toString();//点击确认后获取输入框的内容
                if (phone != null && !phone.isEmpty()) {//如果内容不为空，这个判断是为了防止空指针
                    modifyInfo(phone, u_name, u_sex, u_birthday, u_email, O_phone);
                    phone_show.setText(phone);
                } else {
                    Toast.makeText(getActivity(), "签名为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();
    }
    private void showEmailDialog() {
        final EditText inputServer = new EditText(getActivity());
        inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});//设置最多只能输入50个字符
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());//构建对话框，一个对话框，上面有输入框了，然后就还要有取消和确认键
        builder.setIcon(R.drawable.email).setView(inputServer)
                .setTitle("请输入邮箱号：")
                .setNegativeButton("取消", null);//设置取消键
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {//设置确认键，且对确认键进行监听
            public void onClick(DialogInterface dialog, int which) {
                email = inputServer.getText().toString();//点击确认后获取输入框的内容
                if (email != null && !email.isEmpty()) {//如果内容不为空，这个判断是为了防止空指针
                    modifyInfo(O_phone, u_name, u_sex, u_birthday, email, O_phone);
                    phone_show.setText(email);
                } else {
                    Toast.makeText(getActivity(), "签名为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();
    }
    private void showTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dateDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                birthday = year + "年" + (month+1) + "月" + dayOfMonth + "日";
                birthday_show.setText(birthday);
                //Toast.makeText(getActivity(), year + "年" + (month+1) + "月" + dayOfMonth + "日", Toast.LENGTH_SHORT).show();
            }
        }, year, month, dayOfMonth);
        dateDialog.show();
    }
    //修改信息
    private void modifyInfo(String phone, String name, String sex, String birthday, String email, String O_phone) {
        UserInfo item = new UserInfo();
        item.setPhone(phone);
        item.setU_name(name);
        item.setU_sex(sex);
        item.setU_birthday(birthday);
        item.setU_email(email);
        item.setO_phone(O_phone);
        mainHandler = new Handler(getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int Row = DBUtils.modifyInfo(item);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //setResult(1);
                    }
                });
            }
        }).start();
    }
}