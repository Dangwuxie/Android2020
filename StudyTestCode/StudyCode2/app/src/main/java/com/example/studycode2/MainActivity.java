package com.example.studycode2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView mImage;
    private TextView mText;
    private int numLength;
    private int index;
    private String[] title;
    private int[] images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initData() {
        title = new String[]{"第1张图片","第2张图片","第3张图片","第4张图片","第5张图片"};
        images = new int[]{R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e};
        //此处两个初始化设置两个第一个title和image。刚进入app时的初始化界面
        mImage.setImageResource(images[0]);
        mText.setText(title[0]);
        //准备后面点击事件，切换image和title
        numLength = title.length;
        index = 0;
    }

    private void initView() {
        mImage = findViewById(R.id.iv_show);
        mText = findViewById(R.id.tv_show);
        //按钮设置点击事件
        findViewById(R.id.btn_previous).setOnClickListener(this);
        findViewById(R.id.btn_next).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        //点击之后判断是哪个按钮
        switch (view.getId()){
            case R.id.btn_previous:
                if (index == 0){
                    index = numLength - index;
                }else {
                    index = numLength-- ;
                }
                break;
            case R.id.btn_next:
                if (index == 4){
                    index = 0;
                }else {
                    index ++;
                }
                break;
        }
        //全局index变了之后，这时候就需要更新图片和title了。
        updateImageAndTitle();
    }

    private void updateImageAndTitle() {
        mText.setText(title[index]);
        mImage.setImageResource(images[index]);
    }


}





