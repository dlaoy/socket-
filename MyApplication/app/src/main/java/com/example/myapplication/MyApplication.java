package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MyApplication extends AppCompatActivity {
    Button btn1;
    TextView tv;
    private DatagramSocket socket;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myapplicationi);

        btn1 = findViewById(R.id.testBtn);
        tv = findViewById(R.id.helloWorldTextView);



        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv.setText("新文字");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // 创建DatagramSocket
                            socket = new DatagramSocket();

                            // 设置socket属性
                            socket.setBroadcast(true);

                            String message = "This is broadcast message!";
                            byte[] buffer = message.getBytes();

                            InetAddress address = InetAddress.getByName("255.255.255.255");
                            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 10130);

                            // 发送广播消息
                            socket.send(packet);

                            // 关闭socket
                            socket.close();
                            System.out.println("准备接收消息");
                            // 接收单播消息
                            receiveUnicastMessage();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });



    }

    private void myreceive() throws IOException {
        //1.获取 datagramSocket 实例,并监听某个端口
        DatagramSocket socketwwww = new DatagramSocket(10131);
        //2.创建一个 udp 的数据包
        byte[] buf = new byte[512];
        DatagramPacket packet = new DatagramPacket(buf,buf.length);
        //3.开始阻塞获取udp数据包
        socketwwww.receive(packet);

        //拿到发送端的一些信息
        String ip = packet.getAddress().getHostAddress();
        int port = packet.getPort();
        int length = packet.getLength();

        String msg = new String(buf,0,length);
        System.out.println("客户端: "+ip+"\tport: "+port+"\t信息: "+msg);
    }

    private void receiveUnicastMessage() throws IOException{
        DatagramSocket receiveSocket = new DatagramSocket(10131);
        System.out.println("接收消息2");
        byte[] receiveData = new byte[1024];
        System.out.println("接收消息3");
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        System.out.println("接收消息4");
        // 接收单播消息
        receiveSocket.receive(receivePacket);
        System.out.println("接收消息5");
        final String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
        final InetAddress senderAddress = receivePacket.getAddress();
        final int senderPort = receivePacket.getPort();
        System.out.println("hello");
        // 在UI线程更新UI
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 处理接收到的单播消息
                String displayText = "Received from " + senderAddress.toString() + ":" + senderPort + ": " + receivedMessage;
                System.out.println(displayText);
                tv.setText(displayText);
            }
        });

        // 关闭接收socket
        receiveSocket.close();
    }
}
