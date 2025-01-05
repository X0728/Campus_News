package com.example.campus_news;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campus_news.StringEntity.UserInfo;
import com.example.campus_news.Utils.DBUtils;

import java.sql.SQLException;
import java.util.Random;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_account, et_pwd_first, et_pwd_second, et_captcha;
    private String captcha;
    private TextView test;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        findViewById(R.id.btn_submit).setOnClickListener(this);

        et_account = findViewById(R.id.et_account);     //手机号
        et_pwd_first = findViewById(R.id.et_pwd_first);     //新密码
        et_pwd_second = findViewById(R.id.et_pwd_second);
        et_captcha = findViewById(R.id.et_captcha);     //验证码
        Button btn_get_captcha = findViewById(R.id.btn_get_captcha);
        Button btn_submit = findViewById(R.id.btn_submit);

        btn_get_captcha.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_get_captcha) {
            if (et_account.getText().length() < 11){
                Toast.makeText(this, "请输入正确手机号!", Toast.LENGTH_SHORT).show();
            }else {verifyCaptcha();}
        }
        if (v.getId() == R.id.btn_submit) {
            if (et_account.getText().length() < 11){
                Toast.makeText(this, "请输入正确手机号!", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(et_pwd_first.getText().toString().trim()) ||TextUtils.isEmpty(et_pwd_second.getText().toString().trim())){
                Toast.makeText(this, "请输入密码!", Toast.LENGTH_SHORT).show();
            } else if (!et_pwd_first.getText().toString().equals(et_pwd_second.getText().toString())) {
                Toast.makeText(this, "两次密码不一致！", Toast.LENGTH_SHORT).show();
            } else if (!et_captcha.getText().toString().equals(captcha)) {
                Toast.makeText(this, "验证码错误!", Toast.LENGTH_SHORT).show();
            }else {
                editPassword(et_account.getText().toString(), et_pwd_first.getText().toString());
                reviseSuccess();
            }
        }
        if (v.getId() == R.id.back){
            finish();
        }
    }

    //获取验证码
    private void verifyCaptcha() {

        captcha = String.format("%6d", new Random().nextInt(999999));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请记住验证码");
        builder.setMessage("手机号" + et_account.getText().toString()+",本次验证码:"+ captcha +",请输入验证码");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    //修改成功提示
    private void reviseSuccess() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("修改密码成功");
        builder.setMessage("手机号" + et_account.getText().toString() + "密码修改成功!");
        builder.setPositiveButton("确认并返回登录", (dialogInterface, i) -> finish());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void editPassword(String u_phone, String u_password) {
        UserInfo item = new UserInfo();
        item.setPhone(u_phone);
        item.setU_password(u_password);
        Log.i("112","1");
        mainHandler = new Handler(getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int Row = DBUtils.editPassword(item);
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