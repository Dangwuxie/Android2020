package com.example.tcptest1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.format.Time;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.StringBuffer;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static TextView tv1;
    static   TextView tv2;
    static  TextView tv3;
    static  TextView tv4;
    static  TextView tv5_1;
    static  TextView tv5_2;
    static  TextView tv6_1;
    static TextView tv6_2;
    static  TextView tv7_1;
    static  TextView tv7_2;
    static  TextView tv8_1;
    static TextView tv8_2;
    static  TextView tv9_1;
    static  TextView tv9_2;
    static TextView tv10_1;
    static TextView tv10_2;
    static TextView tv10_3;
    static  TextView tv11_1;
    static  TextView tv11_2;
    static  TextView tv11_3;
    static TextView tv_server_receive_data;
    static TextView tv_local_ip;

    static  Button btn_local_start;
    static  Button btn_local_pause;
    static Button btn_server_start;
    static  Button btn_server_pause;
    static Button btn_get_local_ip;

    static TCPServer tcpServer;


    private List<String> dataList;
    static String charset = "GBK";
    private static Time t= new Time();
    static String ShowTextData = "";
    static TextView ShowText = null;
    static boolean isServer = false;

    static Handler handler1 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            setDataShow(msg);
            //if (msg.what == 102){
            //
            //}
            return false;
        }
    });
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
            ShowText.setText(Html.fromHtml(ShowTextData));
        } else{
            ShowTextData = ShowTextData + NewData;
            ShowText.setText(Html.fromHtml(ShowTextData));
        }

        int offset = ShowText.getLineCount()*ShowText.getLineHeight();
        if(offset>ShowText.getHeight()){
            ShowText.scrollTo(0,offset - ShowText.getHeight());
        }
        //AddColorText(time+":","1010bb",true);
        //AddColorText(data,"bb1010",true);
    }

    private static String GetHtmlText(String text,String color,boolean AddBr){
        text ="<font color='#"+ color +"'>" +  text + "</font>";
        if(AddBr){
            text = text + "<br>";
        }
        return text;
    }

    private static void setDataShow(Message msg) {
        Bundle msgData = msg.getData();
        String msgTvText1 = msgData.getString("tv1");
        String msgTvText2 = msgData.getString("tv2");
        String msgTvText3 = msgData.getString("tv3");
        String msgTvText4 = msgData.getString("tv4");

        String msgTvText5_1 = msgData.getString("tv5_1");
        String msgTvText5_2 = msgData.getString("tv5_2");
        String msgTvText6_1 = msgData.getString("tv6_1");
        String msgTvText6_2 = msgData.getString("tv6_2");

        String msgTvText7_1 = msgData.getString("tv7_1");
        String msgTvText7_2 = msgData.getString("tv7_2");
        String msgTvText8_1 = msgData.getString("tv8_1");
        String msgTvText8_2 = msgData.getString("tv8_2");
        String msgTvText9_1 = msgData.getString("tv9_1");
        String msgTvText9_2 = msgData.getString("tv9_2");

        String msgTvText10_1 = msgData.getString("tv10_1");
        String msgTvText10_2 = msgData.getString("tv10_2");
        String msgTvText10_3 = msgData.getString("tv10_3");
        String msgTvText11_1 = msgData.getString("tv11_1");
        String msgTvText11_2 = msgData.getString("tv11_2");
        String msgTvText11_3 = msgData.getString("tv11_3");

        tv1.setText(msgTvText1);
        tv2.setText(msgTvText2);
        tv3.setText(msgTvText3);
        tv4.setText(msgTvText4);

        tv5_1.setText(msgTvText5_1);
        tv5_2.setText(msgTvText5_2);
        tv6_1.setText(msgTvText6_1);
        tv6_2.setText(msgTvText6_2);

        tv7_1.setText(msgTvText7_1);
        tv7_2.setText(msgTvText7_2);
        tv8_1.setText(msgTvText8_1);
        tv8_2.setText(msgTvText8_2);
        tv9_1.setText(msgTvText9_1);
        tv9_2.setText(msgTvText9_2);

        tv10_1.setText(msgTvText10_1);
        tv10_2.setText(msgTvText10_2);
        tv10_3.setText(msgTvText10_3);
        tv11_1.setText(msgTvText11_1);
        tv11_2.setText(msgTvText11_2);
        tv11_3.setText(msgTvText11_3);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getTextViewEvent();
        initUI();

    }

    private void getTextViewEvent() {
        tv1 = findViewById(R.id.tv_data1);
        tv2 = findViewById(R.id.tv_data2);
        tv3 = findViewById(R.id.tv_data3);
        tv4 = findViewById(R.id.tv_data4);
        tv5_1 = findViewById(R.id.tv_data5_1);
        tv5_2 = findViewById(R.id.tv_data5_2);

        tv6_1 = findViewById(R.id.tv_data6_1);
        tv6_2 = findViewById(R.id.tv_data6_2);
        tv7_1 = findViewById(R.id.tv_data7_1);
        tv7_2 = findViewById(R.id.tv_data7_2);

        tv8_1 = findViewById(R.id.tv_data8_1);
        tv8_2 = findViewById(R.id.tv_data8_2);
        tv9_1 = findViewById(R.id.tv_data9_1);
        tv9_2 = findViewById(R.id.tv_data9_2);

        tv10_1 = findViewById(R.id.tv_data10_1);
        tv10_2 = findViewById(R.id.tv_data10_2);
        tv10_3 = findViewById(R.id.tv_data10_3);
        tv11_1 = findViewById(R.id.tv_data11_1);
        tv11_2 = findViewById(R.id.tv_data11_2);
        tv11_3 = findViewById(R.id.tv_data11_3);

        tv_server_receive_data = findViewById(R.id.tv_server_receive_data);
        ShowText = tv_server_receive_data;
        tv_server_receive_data.setMovementMethod(ScrollingMovementMethod.getInstance());

        tv_local_ip = findViewById(R.id.tv_local_ip);

        btn_local_start = findViewById(R.id.btn_local_start);
        btn_local_pause = findViewById(R.id.btn_local_pause);
        btn_server_start = findViewById(R.id.btn_server_start);
        btn_server_pause = findViewById(R.id.btn_server_pause);
        btn_get_local_ip = findViewById(R.id.btn_get_local_ip);
    }

    private void initUI() {
        btn_local_start.setOnClickListener(this);
        btn_local_pause.setOnClickListener(this);
        btn_server_start.setOnClickListener(this);
        btn_server_pause.setOnClickListener(this);
        btn_get_local_ip.setOnClickListener(this);
    }

    static void sendMessage(String[] sendMessageArray) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        System.out.println("数组的长度为"+sendMessageArray.length);
        bundle.putString("tv1",sendMessageArray[0]);
        bundle.putString("tv2",sendMessageArray[1]);
        bundle.putString("tv3",sendMessageArray[2]);
        bundle.putString("tv4",sendMessageArray[3]);

        bundle.putString("tv5_1",sendMessageArray[4]);
        bundle.putString("tv5_2",sendMessageArray[5]);
        bundle.putString("tv6_1",sendMessageArray[6]);
        bundle.putString("tv6_2",sendMessageArray[7]);

        bundle.putString("tv7_1",sendMessageArray[8]);
        bundle.putString("tv7_2",sendMessageArray[9]);
        bundle.putString("tv8_1",sendMessageArray[10]);
        bundle.putString("tv8_2",sendMessageArray[11]);
        bundle.putString("tv9_1",sendMessageArray[12]);
        bundle.putString("tv9_2",sendMessageArray[13]);

        bundle.putString("tv10_1",sendMessageArray[14]);
        bundle.putString("tv10_2",sendMessageArray[15]);
        bundle.putString("tv10_3",sendMessageArray[16]);
        bundle.putString("tv11_1",sendMessageArray[17]);
        bundle.putString("tv11_2",sendMessageArray[18]);
        bundle.putString("tv11_3",sendMessageArray[19]);

        message.setData(bundle);
        handler1.sendMessage(message);
    }

    private void readTxtFile() {
        ////win系统
        String fileName = "/res/raw/tcp_data.txt";
        try {
            InputStream inputStreamReader = getResources().openRawResource(R.raw.tcp_data);
            dataList = new ArrayList<>();
            byte[] receiveArray = new byte[1];
            int len = 0;
            StringBuffer result = new StringBuffer("");
            int count = 0;
            while((len = inputStreamReader.read(receiveArray)) != -1){
                count++;
                result.append(new String(receiveArray));

                if (receiveArray[0] == '\n'){
                    System.out.println("该换行了@！！！！！！！");
                    String temp = result.toString();
                    System.out.println("这是一条数据："+temp);
                    dataList.add(temp);
                    result = new StringBuffer("");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sendMessage(temp.split(","));
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_local_start:
                startReadTxtData();
                break;
            case R.id.btn_local_pause:
                pauseReadTxtData();
                break;
            case R.id.btn_server_start:
                startServerReceive();
                break;
            case R.id.btn_server_pause:
                pauseServerReceive();
                break;
            case R.id.btn_get_local_ip:
                getLocalIp();
                break;
        }
    }

    private void getLocalIp() {
        String localIp =  getLocalIpCore();
        //此处设置tv_local_ip的值
        tv_local_ip.setText("本地ip地址："+localIp);

    }

    private String getLocalIpCore() {
        //获取本地ip地址；
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        }catch (SocketException ex){
            ex.printStackTrace();
        }
        return null;
    }

    private void startServerReceive() {
        isServer = true;
        if (tcpServer == null){
            //启动服务端接收数据
            ShowErro("启动TCP服务端");
            tcpServer = new TCPServer();
            tcpServer.SetPort(8080);
        }else{
            ShowErro("新建一个用户接入口");
        }
        tcpServer.NewAClient();
    }

    private void pauseServerReceive() {
        //暂停服务端接收数据

    }

    private void startReadTxtData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                readTxtFile();
            }
        }).start();

    }

    private void pauseReadTxtData() {
        //暂停读取txt文件数据

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
                                    //此处再发送给主线程数据，
                                    System.out.println(Data);
                                    MainActivity.sendMessage(Data.split(","));
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


