package com.example.autoclient1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.format.Time;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static Socket socket = null;
    InputStream inputStream = null;
    OutputStream outputStream = null;
    //读取txt文件之后每读取一行，就复制，发送；
    private String inputMSG = "";
    private TextView text1;
    private Button btn_start;
    private Button btn_send;
    private EditText et_server_ip;
    static String ShowTextData = "";

    private static Time t= new Time();
    static TextView ShowText = null;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == 101){
                String data = msg.getData().getString("data");
                Log.i("tag2","传过来的数据"+data);
                //text1.setText(data);
                ShowData2(data);
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();

    }

    private void initUI() {
        text1 = findViewById(R.id.text1);
        text1.setMovementMethod(ScrollingMovementMethod.getInstance());
        ShowText = text1;
        btn_start = findViewById(R.id.btn_linked_server_start);
        btn_send = findViewById(R.id.btn_send_data);
        et_server_ip = findViewById(R.id.et_server_ip);
        btn_start.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        //autoClientStart();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_linked_server_start:
                autoClientStart();
                break;
            case R.id.btn_send_data:
                autoSendData();
                break;
        }

    }

    private void autoSendData() {
       if (socket != null){
           try {
               outputStream = socket.getOutputStream();
               new Thread(new Runnable() {
                   @Override
                   public void run() {
                       redLocalTxt(outputStream);
                   }
               }).start();
           } catch (IOException e) {
               e.printStackTrace();
           }
           //readIO(true);
       }else {
           ShowData2("服务端未连接！");
       }
    }

    private void redLocalTxt(OutputStream outputStream) {
        try {
            String fileName = "/res/raw/tcp_data.txt";

            InputStream inputStreamReader = getResources().openRawResource(R.raw.tcp_data);
            byte[] receiveArray = new byte[1];
            int len = 0;
            StringBuffer result = new StringBuffer("");

            while((len = inputStreamReader.read(receiveArray)) != -1){
                result.append(new String(receiveArray));
                if (receiveArray[0] == '\n'){
                    System.out.println("该换行了@！！！！！！！");
                    String temp = result.toString();
                    System.out.println("这是一条数据："+temp);
                    result = new StringBuffer("");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ShowData2(temp);
                    //sendMessage(temp.split(","));
                    outputStream.write(temp.getBytes("GBK"));

                    outputStream.flush();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String GetHtmlText(String text,String color,boolean AddBr){
        text ="<font color='#"+ color +"'>" +  text + "</font>";
        if(AddBr){
            text = text + "<br>";
        }
        return text;
    }


    public static void ShowData2(String data){
        t.setToNow(); // 取得系统时间。
        int year = t.year;
        int month = t.month;
        int date = t.monthDay;
        int hour = t.hour; // 0-23
        int minute = t.minute;
        int second = t.second;
        String time = year+"-"+month+"-" + date + "   " + hour + ":" + minute + ":" + second;
        int lineCount = ShowText.getLineCount();
        String NewData = GetHtmlText(time,"1010BB",true) + GetHtmlText(data,"10BB10",true);

        if(lineCount>80){
            ShowTextData = NewData;
        }else{
            ShowTextData = ShowTextData + NewData;
        }
        ShowText.setText(Html.fromHtml(ShowTextData));
        int offset=ShowText.getLineCount()*ShowText.getLineHeight();
        if(offset>ShowText.getHeight()){
            ShowText.scrollTo(0,offset - ShowText.getHeight());
        }
    }

    private void autoClientStart(){
        Log.i("tag","刷新新线程");

        new Thread(){
            @Override
            public void run(){
                //if (socket == null){
                    try{
                        Thread.sleep(1000);
                        //String serverIp = et_server_ip.getText().toString();
                        //System.out.println("这是输入的ip地址"+serverIp);
                        socket = new Socket("192.168.100.107",8080);
                    }catch (IOException | InterruptedException e){
                        e.printStackTrace();
                    }

                    if (socket != null){
                        ShowData2("连接成功！");
                    }else {
                        ShowData2("连接失败！,请再次连接");
                    }
                    //if(socket == null || socket.isClosed()){
                    //    Toast.makeText(getApplicationContext(),"连接失败",Toast.LENGTH_LONG).show();
                    //}else{
                    //    //拿到输入流
                    //    Toast.makeText(getApplicationContext(),"连接成功",Toast.LENGTH_LONG).show();
                    //    try{
                    //        inputStream = socket.getInputStream();
                    //        outputStream = socket.getOutputStream();
                    //        outputStream.write(inputMSG.getBytes("GBK"));
                    //        ShowData2(inputMSG);
                    //        outputStream.flush();
                    //    }catch(IOException e){
                    //        e.printStackTrace();
                    //    }
                    //    readIO(true);
                    //}
            //    }else{
            //        if (socket == null || socket.isClosed()){
            //            Log.i("tag","连接结束");
            //        }else{
            //            try{
            //                inputStream = socket.getInputStream();
            //                outputStream = socket.getOutputStream();
            //                outputStream.write(inputMSG.getBytes("GBK"));
            //                ShowData2(inputMSG);
            //                outputStream.flush();
            //            }catch (IOException e){
            //                e.printStackTrace();
            //            }
            //            readIO(true);
            //        }
            //    }
            }
        }.start();
    }

    //private void readIO(final boolean normal){
    //    final boolean Normal = normal;
    //    new Thread(){
    //        private String data;
    //        @Override
    //        public void run(){
    //            byte[] buffer = new byte[256];
    //            int num = 0;
    //            while(Normal){
    //                if(Normal){
    //                    if(num<6000000){
    //                        num = num+1;
    //                    }else{
    //                        try{
    //                            //发送一个紧急信息
    //                            socket.sendUrgentData(0xFF);//心跳包
    //                        }catch(IOException e) {
    //                            e.printStackTrace();
    //                            break;
    //                        }
    //                        num=0;
    //                    }
    //                    if(socket==null || socket.isClosed()){
    //                        //                                ErrStop("中途断开了.......");
    //                    }else{
    //                        data = "";
    //                        try{
    //                            final int len = inputStream.read(buffer);
    //                            int j = len;
    //                            //读取完的话len=-1；
    //                            if(j != -1){
    //                                try{
    //                                    data = new String(buffer,0,j,"GBK");
    //                                    System.out.println(data);
    //                                    Log.i("kan",data);
    //                                    Message message = new Message();
    //                                    Bundle bundle = new Bundle();
    //                                    bundle.putString("data",data);
    //                                    message.setData(bundle);
    //                                    message.what = 101;
    //                                    handler.sendMessage(message);
    //                                }catch (UnsupportedEncodingException e){
    //                                    e.printStackTrace();
    //                                }
    //                                if (data != null && data != ""){
    //                                   ShowData2(data);
    //                                }
    //                            }
    //                        }catch(IOException e){
    //                            e.printStackTrace();
    //                        }
    //                    }
    //                }else{
    //                    break;
    //                }
    //            }
    //
    //        }
    //    }.start();
    //}


}