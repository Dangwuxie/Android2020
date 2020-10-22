package com.example.soctest3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    static String charset = "GBK";
    private TextView text1;
    Socket socket = null;
    private InputStream inputStream;
    private OutputStream outputStream;
    private String inputMSG = "";
    private Boolean normal = false;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == 101){
                String data = msg.getData().getString("data");
                Log.i("tag2","传过来的数据"+data);
                text1.setText(data);
            }
            return false;
        }
    });
    private EditText text2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }

    private void initUI() {
        findViewById(R.id.btn_receive).setOnClickListener(this);
        findViewById(R.id.btn_send).setOnClickListener(this);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        //TCPClient();
    }

    private void TCPClient() {
        Log.i("tag","刷新新线程");
        new Thread(){
            @Override
            public void run() {
                if (socket == null){
                    try {
                        Thread.sleep(1000);
                        socket = new Socket("192.168.100.106",8080);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (socket == null || socket.isClosed()){
                        Log.i("tag","连接失败");

                    }else {
                        //拿到输入流
                        Log.i("tag","连接成功");
                        try {
                            inputStream = socket.getInputStream();
                            outputStream = socket.getOutputStream();
                            outputStream.write(inputMSG.getBytes("GBK"));
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        readIO(true);
                    }
                }else {
                    if (socket == null || socket.isClosed()){
                        Log.i("tag","连接结束");
                    }else {
                        try {
                            inputStream = socket.getInputStream();
                            outputStream = socket.getOutputStream();
                            outputStream.write(inputMSG.getBytes("GBK"));
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        readIO(true);
                    }
                }
            }
        }.start();
    }



    private void readIO(final boolean normal) {
        final boolean Normal = normal;
        new Thread(){
            private String data;
            @Override
            public void run() {
                byte[] buffer = new byte[256];
                int num = 0;
                while(Normal){
                    if(Normal){
                        if(num<60000) {
                            num = num+1;
                        }else{
                            try{
                                //发送一个紧急信息
                                socket.sendUrgentData(0xFF);//心跳包
                            }catch(IOException e) {
//                                    ErrStop("服务器断开了.......");
                                e.printStackTrace();
                                break;
                            }
                            num=0;
                        }

                        if(socket==null || socket.isClosed()){
//                                ErrStop("中途断开了.......");
                        }else{
                            data = "";
                            try{
                                final int len = inputStream.read(buffer);
                                int j = len;
                                //读取完的话len=-1；
                                if(j != -1){
//                                    for(;j<buffer.length;j++){
//                                        buffer[j]=0;
//                                    }
                                    try{
                                        data = new String(buffer,0,j,"GBK");
                                        System.out.println(data);
                                        Log.i("kan",data);
                                        Message message = new Message();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("data",data);
                                        message.setData(bundle);
                                        message.what = 101;
                                        handler.sendMessage(message);
                                    }catch (UnsupportedEncodingException e){
                                        e.printStackTrace();
                                    }
                                    if (data != null && data != ""){
//                                            ShowData(data);
                                    }
                                }
                            }catch(IOException e){
                                e.printStackTrace();
                            }
                        }
                    }else{
                        break;
                    }
                }

            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        inputMSG = text2.getText().toString();
        TCPClient();
    }

}