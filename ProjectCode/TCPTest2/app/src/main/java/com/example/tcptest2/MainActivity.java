package com.example.tcptest2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.format.Time;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    static String charset = "GBK";
    private TextView text1;
    private static Time t= new Time();
    static String ShowTextData = "";
    static TextView ShowText = null;
    static TCPServer tcpServer = null;
    static boolean isServer = false;

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
        findViewById(R.id.btn_server_start).setOnClickListener(this);
    }

    private static String GetHtmlText(String text,String color,boolean AddBr){
        text ="<font color='#"+ color +"'>" +  text + "</font>";
        if(AddBr){
            text = text + "<br>";
        }
        return text;
    }
    //显示接收到的数据
    public static void ShowData(String data){
        t.setToNow(); // 取得系统时间。
        int year = t.year;
        int month = t.month;
        int date = t.monthDay;
        int hour = t.hour; // 0-23
        int minute = t.minute;
        int second = t.second;

        String time = year+"-"+month+"-" + date + "   " + hour + ":" + minute + ":" + second;
        int lineCount = ShowText.getLineCount();
        String NewData = GetHtmlText(time,"1010BB",true) + GetHtmlText(data,"000000",true);

        if(lineCount>80){
            ShowTextData = NewData;
            ShowText.setText(Html.fromHtml(ShowTextData));
        }else{
            ShowTextData = ShowTextData + NewData;
            ShowText.setText(Html.fromHtml(ShowTextData));
        }

        int offset=ShowText.getLineCount()*ShowText.getLineHeight();
        if(offset>ShowText.getHeight()){
            ShowText.scrollTo(0,offset - ShowText.getHeight());
        }
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
            ShowText.setText(Html.fromHtml(ShowTextData));
        }else{
            ShowTextData = ShowTextData + NewData;
            ShowText.setText(Html.fromHtml(ShowTextData));
        }
        int offset=ShowText.getLineCount()*ShowText.getLineHeight();
        if(offset>ShowText.getHeight()){
            ShowText.scrollTo(0,offset - ShowText.getHeight());
        }
    }
    //显示错误
    public static void ShowErro(String data){
        t.setToNow(); // 取得系统时间。
        int year = t.year;
        int month = t.month;
        int date = t.monthDay;
        int hour = t.hour; // 0-23
        int minute = t.minute;
        int second = t.second;
        String time = year+"-"+month+"-" + date + "   " + hour + ":" + minute + ":" + second;
        int lineCount = ShowText.getLineCount();
        String NewData = GetHtmlText(time,"1010BB",true) + GetHtmlText(data,"BB1010",true);
        if(lineCount>80){
            ShowTextData = NewData;
        }
        else{
            ShowTextData = ShowTextData + NewData;
        }
        ShowText.setText(Html.fromHtml(ShowTextData));

        int offset = ShowText.getLineCount()*ShowText.getLineHeight();
        if(offset>ShowText.getHeight()){
            ShowText.scrollTo(0,offset - ShowText.getHeight());
        }
        //AddColorText(time+":","1010bb",true);
        //AddColorText(data,"bb1010",true);
    }

    @Override
    public void onClick(View v) {
        isServer = true;
        if(tcpServer ==null){
            ShowErro("启动TCP服务端...");
            tcpServer = new TCPServer();
            tcpServer.SetPort(8080);
            tcpServer.NewAClient();
        }else{
            ShowErro("新建一个用户接入口");
            tcpServer.NewAClient();
        }
    }
}


//TCP服务端
class TCPServer {

    private boolean IsNormal;
    private boolean HasStart = false;
    private ServerSocket SSocket = null;
    private Socket[] socket={null,null,null};
    private boolean[] socketEnable={false,false,false};
    private int PORT=8080;
    private String SendData = "";
    private int WhoID = -1;

    public boolean IsIDInRun(int i){
        return socket[i]!=null && socketEnable[i];
    }

    public void SetPort(int i){
        PORT=i;
    }

    public boolean IsRun(){
        return IsNormal;
    }

    public void CloseServer(){
        IsNormal = false;
        for(int j=0;j<socket.length;j++){
            FoceCloseByID(j);
        }
        SSocket=null;
        HasStart=false;
    }

    public void SendMSGAllID(String data){
        for(int j=0;j<socket.length;j++){
            SendMSGByID(data,j);
        }
        ShowData2(data);
    }

    public void SendMSGByID(String data,int i){
        OutputStream os;
        if(IsIDInRun(i)){
            SendData = data;
            WhoID = i ;
            int AutoBreak = 10;
            while(true) {
                if(WhoID < 0 || SendData==null || SendData==""){break;}
                try{
                    Thread.sleep(30);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                if(AutoBreak<0){
                    break;
                }
                AutoBreak=AutoBreak-1;
            }

        }
    }

    public void CloseByID(final int i){
        socketEnable[i] = false;
    }

    public int NewAClient(){
        //每次使用后需要考虑是否再加一个来监听，多一个监听可以多连接一个客户端
        if(HasStart==false) {
            Start();
            HasStart = true;
        }
        int i = FindAEmptySocket();
        if(i>=0) {
            NewThread(i);
            //OutInfo("添加客户成功，客户id: " + String.valueOf(i));
        }else{
            ShowErr("客户数量达到最大值了.");
        }
        return i;
    }

    private void FoceCloseByID(final int i){
        socketEnable[i] = false;
        if (socket[i] != null){
            //socket.shutdownOutput();
            try {
                socket[i].close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket[i] = null;
    }

    private void Start(){
        try{
            SSocket = new ServerSocket(PORT);
            IsNormal = true;
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private int FindAEmptySocket(){
        for(int j=0;j<socket.length;j++){
            if(socket[j]==null || socketEnable[j]==false){
                return j;
            }
        }
        return -1;
    }

    private void NewThread(final int i){
        //不止接受一个客户端
        final OutputStream[] os = {null};
        final InputStream[] is = {null};
        socket[i] = null;//接受一个连接
        final boolean[] IsConect = {false};
        final byte[] buffer = new byte[1024];//创建接收缓冲区
        socketEnable[i]=true;
        new Thread() {
            public void run()
            {
                ShowErr("等待客户连接！");
                while (IsNormal && socketEnable[i]){
                    try{
                        socket[i] = SSocket.accept();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    try{
                        //System.out.println("客户端已连接，ip地址:" + socket[i].getInetAddress().getHostAddress() + "端口号:" + socket[i].getLocalPort());
                        IsConect[0] = true;
                        ShowErr("客户已连接！"+"<br>ip地址:" + socket[i].getInetAddress().getHostAddress() + "<br>端口号:" + socket[i].getLocalPort());
                        Thread.sleep(1000);
                    }catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                    try{
                        is[0] = socket[i].getInputStream();
                        os[0] = socket[i].getOutputStream();
                    }catch(IOException e) {
                        e.printStackTrace();
                    }
                    try{
                        os[0].write("Hello!!!".getBytes(MainActivity.charset));
                        OutThread(os[0],i);
                    }catch(IOException e) {
                        e.printStackTrace();
                    }
                    int num=0;
                    while(socketEnable[i] && IsConect[0]){
                        if(num<60000000){
                            num = num+1;
                        }else{
                            try{
                                socket[i].sendUrgentData(0xFF);//心跳包
                            }catch(IOException e){
                                //ErroStop("服务器断开了~(-_-)~");
                                ShowErr("客户断开了~(-_-)~");
                                e.printStackTrace();
                                break;
                            }
                            num=0;
                        }
                        String Data = "";
                        try{
                            int len = is[0].read(buffer);
                            int j = len;
                            if(j > 0){
                                for(;j<buffer.length;j++){
                                    buffer[j]=0;
                                }
                                try{
                                    Data = new String(buffer,MainActivity.charset);
                                }catch(UnsupportedEncodingException e){
                                    e.printStackTrace();
                                }
                                if(Data != null && Data != ""){
                                    OutInfo("IP:" + socket[i].getInetAddress().getHostAddress()  + "<br>内容:" + Data);
                                }
                            }
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    FoceCloseByID(i);
                    break;
                }
            }
        }.start();
    }

    private void OutThread(final OutputStream os, final int i){
        new Thread() {
            public void run()
            {
                while(socketEnable[i] && IsNormal){
                    if(WhoID == i && SendData!=null){
                        try{
                            os.write(SendData.getBytes(MainActivity.charset));
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        WhoID=-1;
                        SendData="";
                    }
                }
            }
        }.start();
    }

    //显示消息
    private void OutInfo(String data){
        MainActivity.ShowData(data);
    }

    public void ShowErr(String data)
    {
        MainActivity.ShowErro(data);
    }

    private void ShowData2(String data)
    {
        MainActivity.ShowData2(data);
    }

}




