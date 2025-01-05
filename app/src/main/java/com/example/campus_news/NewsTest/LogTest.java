package com.example.campus_news.NewsTest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campus_news.R;
import com.example.campus_news.StringEntity.BannerDataInfo;
import com.example.campus_news.StringEntity.UserInfo;
import com.example.campus_news.Utils.DBUtils;
import com.youth.banner.Banner;
/*import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;*/

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class LogTest extends AppCompatActivity {
    private static final int REQUEST_CODE_TAKE = 1;
    private static final int REQUEST_CODE_CHOOSE = 0;
    private TextView tv_data;
    private Button btn_get_data; //声明组件
    private Button btn_get_all;
    private Button insert;
    private Handler mainHandler;
    private ImageView image_show;
    private Uri image_Uri;
    private Banner banner;
    private List<BannerDataInfo> mBannerDataInfo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_test);
        btn_get_data = findViewById(R.id.btn_get_data);
        btn_get_all = findViewById(R.id.btn_get_all);
        tv_data = findViewById(R.id.tv_data);
        insert = findViewById(R.id.insert);
        image_show = findViewById(R.id.image_show);

       // banner = findViewById(R.id.banner);

        setListener();

        /*mBannerDataInfo.add(new BannerDataInfo(R.drawable.lunbo1, "第一"));
        mBannerDataInfo.add(new BannerDataInfo(R.drawable.lunbo2, "第二"));
        mBannerDataInfo.add(new BannerDataInfo(R.drawable.lunbo3, "第三"));*/

        /*banner.setAdapter(new BannerImageAdapter<BannerDataInfo>(mBannerDataInfo) {
            @Override
            public void onBindView(BannerImageHolder holder, BannerDataInfo data, int position, int size) {
                holder.imageView.setImageResource(data.getImage());
            }


        });*/

    }

    private void setListener() {
        // 按钮点击事件
        btn_get_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建一个线程来连接数据库并获取数据库中对应表的数据
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 调用数据库工具类DBUtils的getInfoByName方法获取数据库表中数据
                        HashMap<String, Object> map = DBUtils.getInfoByName("Sam");
                        Message message = handler.obtainMessage();
                        if (map != null) {
                            String s = "";
                            for (String key : map.keySet()) {
                                s += key + ":" + map.get(key) + "\n";
                            }
                            message.what = 0x12;
                            message.obj = s;
                        } else {
                            message.what = 0x11;
                            message.obj = "查询结果为空";
                        }
                        // 发消息通知主线程更新UI
                        handler.sendMessage(message);
                    }
                }).start();

            }
        });
        btn_get_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建一个线程来连接数据库并获取数据库中对应表的数据
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 调用数据库工具类DBUtils的getInfoByName方法获取数据库表中数据
                        HashMap<String, String> map = DBUtils.getAllInfo("18093538247");
                        Message message = handler.obtainMessage();
                        if (map != null) {
                            String s = "";
                            for (String key : map.keySet()) {
                                s += key + ":" + map.get(key) + "\n";
                            }
                            message.what = 0x12;
                            message.obj = s;
                        } else {
                            message.what = 0x11;
                            message.obj = "查询结果为空";
                        }
                        // 发消息通知主线程更新UI
                        handler.sendMessage(message);
                    }
                }).start();

            }
        });
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // createUserInfo("13909354546", "张三", "123456", "男", "2024年1月27日", "33@x");
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
               /* try {
                    takePhoto();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }*/
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    doTake();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else {
                Toast.makeText(this, "你没有权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void takePhoto() throws IOException {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            //拍照
            doTake();
        }else {
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
    }

    private void doTake() throws IOException {
        File imageTemp = new File(Arrays.toString(getExternalCacheDirs()), "imageOut.jpeg");
        if (imageTemp.exists()) {
            imageTemp.delete();
        }
        try {
            imageTemp.createNewFile();
        }catch (IOException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT > 24) {
            image_Uri = FileProvider.getUriForFile(this, "com.example.campus_news.fileprovider", imageTemp);
        }else {
            image_Uri = Uri.fromFile(imageTemp);
        }
        Intent intent = new Intent();
        intent.setAction("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_Uri);
        startActivityForResult(intent, REQUEST_CODE_TAKE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_TAKE){
            if (resultCode == RESULT_OK) {
                //获取拍摄照片
                try {
                    InputStream inputStream = getContentResolver().openInputStream(image_Uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageBytes = baos.toByteArray();
                    String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                   image_show.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 0x11:
                    String s = (String) msg.obj;
                    tv_data.setText(s);
                    break;
                case 0x12:
                    String ss = (String) msg.obj;
                    tv_data.setText(ss);
                    break;
            }
            return true;
        }
    });
    public void createUserInfo(String u_phone, String u_name, String u_password, String u_sex, String u_birthday, String u_email) {
        UserInfo item = new UserInfo();
        item.setPhone(u_phone);
        item.setU_name(u_name);
        item.setU_password(u_password);
        item.setU_sex(u_sex);
        item.setU_birthday(u_birthday);
        item.setU_email(u_email);
        Log.i("112","1");
        mainHandler = new Handler(getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int Row = DBUtils.insertUserInfo(item);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setResult(1);
                    }
                });
            }
        }).start();
    }
}