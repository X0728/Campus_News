package com.example.campus_news;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.DatePicker;

import com.example.campus_news.StringEntity.UserInfo;
import com.example.campus_news.Utils.DBUtils;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public class  CreateCountActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private EditText register_user, u_name, u_email, register_password, again_password, et_captcha;
    private Button btn_get_captcha;
    private TextView submit, tv_birthday;
    private ImageView back, im_birthday;
    private RadioGroup sex;
    private String year, month, day, captcha, u_sex, birthday;
    private Handler mainHandler;
    private RadioButton man, female;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_count);

        register_user = findViewById(R.id.register_user);       //手机号
        btn_get_captcha = findViewById(R.id.btn_get_captcha);       //获取验证码
        et_captcha = findViewById(R.id.et_captcha);     //验证码
        submit = findViewById(R.id.save_user);      //提交
        back = findViewById(R.id.back);     //返回按钮
        u_name = findViewById(R.id.u_name);     //昵称
        sex = findViewById(R.id.sex);       //性别
        man = findViewById(R.id.rb_man);    //男
        female = findViewById(R.id.rb_female);      //女
        u_email = findViewById(R.id.u_email);       //邮箱
        register_password = findViewById(R.id.register_password);       //密码
        again_password = findViewById(R.id.again_password);
        tv_birthday = findViewById(R.id.tv_birthday);       //生日显示
        im_birthday = findViewById(R.id.im_birthday);       //生日选择

        submit.setOnClickListener(this);
        btn_get_captcha.setOnClickListener(this);
        im_birthday.setOnClickListener(this);
        back.setOnClickListener(this);
        sex.setOnCheckedChangeListener(this);

        //获取当前时间
        SimpleDateFormat format1 = new SimpleDateFormat("YYYY");
        SimpleDateFormat format2 = new SimpleDateFormat("MM");
        SimpleDateFormat format3 = new SimpleDateFormat("dd");
        format1.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        format2.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        format3.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        Date curDate = new Date(System.currentTimeMillis());
        year = format1.format(curDate);
        month = format2.format(curDate);
        day = format3.format(curDate);
    }


    @Override
    public void onClick(View view) {
        String phone = register_user.getText().toString();
        String u_captcha = et_captcha.getText().toString();
        String name = u_name.getText().toString();
        String email = u_email.getText().toString();
        String first_pwd = register_password.getText().toString();
        String second_pwd = again_password.getText().toString();
        if (view.getId() == R.id.im_birthday) {
            DateListener();
        }
        if (view.getId() == R.id.back) {
            finish();
        }
        if (view.getId() == R.id.btn_get_captcha) {
            if (phone.length() < 11) {
                Toast.makeText(this, "请输入正确手机号!", Toast.LENGTH_SHORT).show();
            }else {verifyCaptcha();}
        }
        if (view.getId() == R.id.save_user) {
            if (TextUtils.isEmpty(phone) || 
                    TextUtils.isEmpty(u_captcha) || 
                    TextUtils.isEmpty(name) || 
                    TextUtils.isEmpty(email) || 
                    TextUtils.isEmpty(first_pwd) || 
                    TextUtils.isEmpty(second_pwd)) {
                Toast.makeText(this, "请将信息填写完整!", Toast.LENGTH_SHORT).show();
            }else if(!u_captcha.equals(captcha)) {
                Toast.makeText(this, "验证码错误!", Toast.LENGTH_SHORT).show();
            } else if (!isValidEmail(email)) {
                Toast.makeText(this, "请输入正确邮箱!", Toast.LENGTH_SHORT).show();
            } else if (!first_pwd.equals(second_pwd)) {
                Toast.makeText(this, "两次密码不一致!", Toast.LENGTH_SHORT).show();
            }else {
                //createAccountDt(phone, name);
                createUserInfo(phone, name, first_pwd, u_sex, birthday, email);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("注册成功");
                builder.setMessage("手机号" + phone +",注册成功!")
                        .setPositiveButton("确认并返回登录", (dialogInterface, i) -> {
                            /*Intent intent = new Intent();
                            intent.putExtra("phone", phone);
                            setResult(Activity.RESULT_OK, intent);*/
                            finish();
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
               // Toast.makeText(this, "注册成功!", Toast.LENGTH_LONG).show();
            }
        }
    }
    @SuppressLint("ResourceType")
    private void DateListener() {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month += 1;
                birthday = year + "年"+ month+ "月" + day + "日";
                tv_birthday.setText(birthday);
            }
        };
        //显示日期选择框
        new DatePickerDialog(CreateCountActivity.this, 2, onDateSetListener, Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day)).show();
    }
    //获取验证码
    private void verifyCaptcha() {
        captcha = String.format("%6d", new Random().nextInt(999999));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请记住验证码");
        builder.setMessage("手机号" + register_user.getText().toString()+",本次验证码:"+ captcha +",请输入验证码")
                .setPositiveButton("确认", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    //性别选择
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        if (checkedId == R.id.rb_man) {
            u_sex = "男";
        } else if (checkedId == R.id.rb_female) {
            u_sex = "女";
        }
    }
    //邮箱正则式验证
    public static boolean isValidEmail(String email) {
        // 正则表达式用于校验邮箱格式
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(regex);
    }
    //数据库操作
    public void createAccountDt(String u_phone, String u_name) {
        UserInfo item = new UserInfo();
        item.setPhone(u_phone);
        item.setU_name(u_name);
        mainHandler = new Handler(getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int Row = DBUtils.createAccount(item);
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

    //数据库插入用户信息表
    public void createUserInfo(String u_phone, String u_name, String u_password, String u_sex, String u_birthday, String u_email) {
        UserInfo item = new UserInfo();
        item.setPhone(u_phone);
        item.setU_name(u_name);
        item.setU_password(u_password);
        item.setU_sex(u_sex);
        item.setU_birthday(u_birthday);
        item.setU_email(u_email);
        Log.i("112","1");
        Log.i("112",u_email);
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