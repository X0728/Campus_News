package com.example.campus_news;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

//设置页
public class SetActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        ImageView about = findViewById(R.id.about);
        ImageView reset_pwd = findViewById(R.id.reset_password);
        Button exit = findViewById(R.id.exit);
        ImageView back = findViewById(R.id.back);
        about.setOnClickListener(this);
        reset_pwd.setOnClickListener(this);
        exit.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.exit) {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setClass(this, LogActivity.class);
            startActivity(intent);
            finish();
        } else if (v.getId() == R.id.about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.back) {
            finish();
        }
    }
}