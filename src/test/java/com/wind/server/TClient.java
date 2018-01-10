package com.wind.server;

import java.io.*;
import java.net.*;


public class TClient {

    public static void main(String args[]) {

        try {

            Socket socket = new Socket("127.0.0.1", 4700);

            BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));

            PrintWriter os = new PrintWriter(socket.getOutputStream());

//由Socket对象得到输出流，并构造PrintWriter对象

            BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));

//由Socket对象得到输入流，并构造相应的BufferedReader对象

            String readline = sin.readLine(); //从系统标准输入读入一字符串

            while (!readline.equals("bye")) {

                os.println(readline);
                os.flush();

                System.out.println("Client:" + readline);
                System.out.println("Server:" + is.readLine());

                readline = sin.readLine(); //从系统标准输入读入一字符串
            } //继续循环

            os.close(); //关闭Socket输出流
            is.close(); //关闭Socket输入流
            socket.close(); //关闭Socket
        } catch (Exception e) {
            System.out.println("Error" + e); //出错，则打印出错信息

        }

    }

}
