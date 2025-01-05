package com.example.campus_news;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.campus_news.News.MainFragment;
import com.example.campus_news.News.MineFragment;
import com.example.campus_news.News.MyPublishFragment;
import com.example.campus_news.News.PublishFragment;
import com.example.campus_news.News.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private MainFragment mainFragment;
    private SearchFragment searchFragment;
    private PublishFragment publishFragment;
    private MineFragment mineFragment;
    private MyPublishFragment myPublishFragment;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.buttonNavigation);

        //默认首页选中
        Intent intent = getIntent();
        phone = intent.getStringExtra("u_phone");
        selectedFragment(0);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.main){
                    selectedFragment(0);
                } else if (item.getItemId() == R.id.search) {
                    selectedFragment(1);
                } else if (item.getItemId() == R.id.publish) {
                    selectedFragment(2);
                }else if (item.getItemId() == R.id.myPublish){
                    selectedFragment(3);
                }
                else {
                    selectedFragment(4);
                }
                return true;
            }
        });
    }

    private void selectedFragment(int position) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideFragment(fragmentTransaction);
        if (position == 0) {
            if (mainFragment == null) {
                mainFragment = new MainFragment();
                fragmentTransaction.add(R.id.content,mainFragment);
            }else {
                fragmentTransaction.show(mainFragment);
            }
        } else if (position == 1) {
            if (searchFragment == null) {
                searchFragment = new SearchFragment();
                fragmentTransaction.add(R.id.content, searchFragment);
            }else {
                fragmentTransaction.show(searchFragment);
            }
        } else if (position == 2) {
            if (publishFragment == null) {
                publishFragment = new PublishFragment();
                fragmentTransaction.add(R.id.content, publishFragment);
            }else {
                fragmentTransaction.show(publishFragment);
            }
        }else if (position ==3) {
            if (myPublishFragment == null) {
                myPublishFragment = new MyPublishFragment();
                fragmentTransaction.add(R.id.content, myPublishFragment);
            } else {
                fragmentTransaction.show(myPublishFragment);
            }
        }else if (position ==4) {
            if (mineFragment == null) {
                mineFragment = new MineFragment();
                fragmentTransaction.add(R.id.content, mineFragment);
            }else {
                fragmentTransaction.show(mineFragment);
            }

        }
        //提交
        fragmentTransaction.commit();
    }
    public  String toValue(){
        return  phone;
    }
    private void hideFragment(FragmentTransaction fragmentTransaction) {
        if (mainFragment != null) {
            fragmentTransaction.hide(mainFragment);
        }
        if (searchFragment != null) {
            fragmentTransaction.hide(searchFragment);
        }
        if (publishFragment != null) {
            fragmentTransaction.hide(publishFragment);
        }
        if (mineFragment != null) {
            fragmentTransaction.hide(mineFragment);
        }
        if (myPublishFragment != null) {
            fragmentTransaction.hide(myPublishFragment);
        }

    }
}