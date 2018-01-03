package com.tcp;

import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * just a demo of basic socket server
 */
@Slf4j
public class NormalSocket {

    public static void main(String[] agrs) throws IOException {
        new NormalSocket().runServer();
    }


    public static int PORT = 8081;

    public void runServer() throws IOException {
        ServerSocket s = new ServerSocket(PORT);

        while (true) {
            Socket socket = s.accept();
            log.info("New connection: {}", socket);

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            String str;
            while ( (str = br.readLine()) != null) {

                if (str.equals("END")) {
                    break;
                }
                System.out.println("receive:" + str);
                pw.println("ok");
                pw.flush();
            }

            br.close();
            pw.close();
            socket.close();
        }
    }
}
