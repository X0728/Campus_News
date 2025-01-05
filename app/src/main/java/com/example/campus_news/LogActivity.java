package com.example.campus_news;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campus_news.Utils.DBUtils;
import com.example.campus_news.Utils.RememberPwdSp;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class LogActivity extends AppCompatActivity implements View.OnClickListener {
    private Handler mainHandler;
    private TextView account, btn_pwd, tv_forget,tv_create;
    private Button btn_log;
    private CheckBox re_pwd;    //记住密码
    private String pwd;
    private ActivityResultLauncher<Intent> register;
    private RememberPwdSp rememberPwdSp;
    private Map<String, String> remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        account = findViewById(R.id.btn_account);
        btn_pwd = findViewById(R.id.btn_pwd);
        btn_log = findViewById(R.id.btn_Log);
        tv_forget = findViewById(R.id.tv_forget); //忘记密码
        tv_create = findViewById(R.id.tv_create); //创建账号
        re_pwd = findViewById(R.id.cb_remember_pwd);    //记住密码

        btn_log.setOnClickListener(this);
        tv_forget.setOnClickListener(this);
        tv_create.setOnClickListener(this);
        rememberPwdSp = new RememberPwdSp(LogActivity.this);
        remember = rememberPwdSp.read();
        if (remember != null) {
            account.setText(rememberPwdSp.read().get("account"));
            btn_pwd.setText(rememberPwdSp.read().get("pwd"));
            if (!account.getText().toString().isEmpty()) {
                re_pwd.setChecked(true);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_Log){
            logIn();
        } else if (v.getId() == R.id.tv_forget) {
            Intent intent = new Intent(this, ForgetPasswordActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.tv_create) {
            Intent intent = new Intent(this, CreateCountActivity.class);
            startActivity(intent);
        }
    }

    public void logIn() {
        mainHandler = new Handler(getMainLooper());
        if (account.getText().length() != 11) {
            Toast.makeText(LogActivity.this, "请输入正确手机号！", Toast.LENGTH_SHORT).show();
        } else if (btn_pwd.getText().length() < 1) {
            Toast.makeText(LogActivity.this, "请输入密码！", Toast.LENGTH_SHORT).show();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HashMap<String, String> map = DBUtils.getLogInfo(account.getText().toString());
                    if (map != null) {
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (map.get("password").equals(btn_pwd.getText().toString())) {
                                    if (re_pwd.isChecked()) {
                                        rememberPwdSp.save(account.getText().toString(), btn_pwd.getText().toString());
                                    } else {
                                        rememberPwdSp.remove();
                                    }
                                    Intent intent = new Intent(LogActivity.this, MainActivity.class);
                                    intent.putExtra("u_phone", account.getText().toString());
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(LogActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LogActivity.this, "未查询到账号！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).start();
        }
    }
}
