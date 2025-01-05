package com.example.campus_news.NewsTest;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.campus_news.R;
import com.example.campus_news.SetActivity;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MyAPPActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_CHOOSE = 1;
    private int choice = -1;
    private Button user_image;
    private ImageView image_show;
    private static final int SELECT_IMAGE = 1;
    private ImageView user_name;
    private ImageView user_phone;
    private ImageView user_birthday;
    private ImageView user_email;
    private ImageView user_sex;
    private TextView name_show;
    private TextView sex_show;
    private TextView phone_show;
    private TextView birthday_show;
    private TextView email_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appactivity);
        user_image = findViewById(R.id.user_image);
        image_show = findViewById(R.id.image_show);
        user_name = findViewById(R.id.user_name);
        user_phone = findViewById(R.id.user_phone);
        user_birthday = findViewById(R.id.user_birthday);
        user_email = findViewById(R.id.user_email);
        user_sex = findViewById(R.id.user_sex);
        name_show = findViewById(R.id.name_show);
        sex_show = findViewById(R.id.sex_show);
        phone_show = findViewById(R.id.phone_show);
        birthday_show = findViewById(R.id.birthday_show);
        email_show = findViewById(R.id.email_show);
        findViewById(R.id.setting).setOnClickListener(this);
        
        user_image.setOnClickListener(this);
        user_name.setOnClickListener(this);
        user_sex.setOnClickListener(this);
        user_phone.setOnClickListener(this);
        user_birthday.setOnClickListener(this);
        user_email.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.user_image){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, SELECT_IMAGE);
            /*Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);*/
            //choosePhoto();
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
            startActivity(new Intent(this, SetActivity.class));
        }

    }
    //@SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            try {
                Uri selectedImageUri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                image_show.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /*if (requestCode == REQUEST_CODE_CHOOSE) {
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
            }
            String path = null;
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                   path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                }
                cursor.close();
            }
            if (path != null){
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                image_show.setImageBitmap(bitmap);
            }
        }*/
        /*if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                Log.i("path", picturePath);
                cursor.close();
                Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                image_show.setImageBitmap(bitmap);
            }
        }*/

    }
    //打开相册
    private void choosePhoto() {
        //判断是否具有权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent("android.intent.action.GET_CONTENT");
            intent.setType("image/*");
            startActivityForResult(intent,REQUEST_CODE_CHOOSE);
        }else {
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    private void showEditDialog() {
        final EditText inputServer = new EditText(this);
        inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});//设置最多只能输入50个字符
        AlertDialog.Builder builder = new AlertDialog.Builder(this);//构建对话框，一个对话框，上面有输入框了，然后就还要有取消和确认键
        builder.setIcon(R.drawable.user_name).setView(inputServer)
                .setTitle("请输入新昵称：")
                .setNegativeButton("取消", null);//设置取消键
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {//设置确认键，且对确认键进行监听
            public void onClick(DialogInterface dialog, int which) {
                String sign = inputServer.getText().toString();//点击确认后获取输入框的内容
                if (sign != null && !sign.isEmpty()) {//如果内容不为空，这个判断是为了防止空指针
                    name_show.setText(sign);
                    /*Map<String, String> params = new HashMap<>();
                    params.put("nickname", sign);
                    OkHttp.create(MyAPPActivity.this).updateSelfInfo(params).enqueue((Call, httpRes) -> {//发送okhttp请求，将修改的信息上传到服务器
                        if (httpRes.isSuccessful()) {
                            Log.v("updateSelfInfo", httpRes.toString());

                        }
                    });*/
                } else {
                    Toast.makeText(MyAPPActivity.this, "签名为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();
    }
    private void showRadioDialog() {
        final String[] item = { "男", "女" };
        AlertDialog.Builder singleDialog = new AlertDialog.Builder(this);
        singleDialog.setIcon(R.drawable.sex);
        singleDialog.setTitle("单选 AlertDialog");
        singleDialog.setSingleChoiceItems(item, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                choice = which;
            }
        });
        singleDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sex_show.setText(item[choice]);
                Toast.makeText(MyAPPActivity.this, "你选择了：" + item[choice], Toast.LENGTH_SHORT).show();
            }
        });
        singleDialog.show();
    }
    private void showEditPhoneDialog() {
        final EditText inputServer = new EditText(this);
        inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});//设置最多只能输入50个字符
        AlertDialog.Builder builder = new AlertDialog.Builder(this);//构建对话框，一个对话框，上面有输入框了，然后就还要有取消和确认键
        builder.setIcon(R.drawable.phone).setView(inputServer)
                .setTitle("请输入新手机号：")
                .setNegativeButton("取消", null);//设置取消键
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {//设置确认键，且对确认键进行监听
            public void onClick(DialogInterface dialog, int which) {
                String sign = inputServer.getText().toString();//点击确认后获取输入框的内容
                if (sign != null && !sign.isEmpty()) {//如果内容不为空，这个判断是为了防止空指针
                    phone_show.setText(sign);
                    /*Map<String, String> params = new HashMap<>();
                    params.put("nickname", sign);
                    OkHttp.create(MyAPPActivity.this).updateSelfInfo(params).enqueue((Call, httpRes) -> {//发送okhttp请求，将修改的信息上传到服务器
                        if (httpRes.isSuccessful()) {
                            Log.v("updateSelfInfo", httpRes.toString());

                        }
                    });*/
                } else {
                    Toast.makeText(MyAPPActivity.this, "签名为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();
    }
    private void showEmailDialog() {
        final EditText inputServer = new EditText(this);
        inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});//设置最多只能输入50个字符
        AlertDialog.Builder builder = new AlertDialog.Builder(this);//构建对话框，一个对话框，上面有输入框了，然后就还要有取消和确认键
        builder.setIcon(R.drawable.email).setView(inputServer)
                .setTitle("请输入邮箱号：")
                .setNegativeButton("取消", null);//设置取消键
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {//设置确认键，且对确认键进行监听
            public void onClick(DialogInterface dialog, int which) {
                String sign = inputServer.getText().toString();//点击确认后获取输入框的内容
                if (sign != null && !sign.isEmpty()) {//如果内容不为空，这个判断是为了防止空指针
                    phone_show.setText(sign);
                    /*Map<String, String> params = new HashMap<>();
                    params.put("nickname", sign);
                    OkHttp.create(MyAPPActivity.this).updateSelfInfo(params).enqueue((Call, httpRes) -> {//发送okhttp请求，将修改的信息上传到服务器
                        if (httpRes.isSuccessful()) {
                            Log.v("updateSelfInfo", httpRes.toString());

                        }
                    });*/
                } else {
                    Toast.makeText(MyAPPActivity.this, "签名为空", Toast.LENGTH_SHORT).show();
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

        DatePickerDialog dateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                birthday_show.setText(year + "-" + (month+1) + "-" + dayOfMonth);
                Toast.makeText(MyAPPActivity.this, year + "年" + (month+1) + "月" + dayOfMonth + "日", Toast.LENGTH_SHORT).show();
            }
        }, year, month, dayOfMonth);
        dateDialog.show();
    }

}