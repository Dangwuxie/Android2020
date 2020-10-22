package com.example.studycode1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //调用父类的onCreate方法,初始化
        super.onCreate(savedInstanceState);
        //调用;layout布局文件
        setContentView(R.layout.activity_main);

    }
}