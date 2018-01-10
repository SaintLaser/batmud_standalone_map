package com.wind.mapper.handler;

import com.glaurung.batMap.controller.MapperEngine;
import com.wind.mapper.common.Tool;
import com.wind.mapper.config.MapperConfig;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * just a demo
 */
public class SocketHandler implements IHandler{

    /**
     * set the mapperEngine
     *
     * @param mapperEngine
     */
    @Override
    public void setMapperEngine(MapperEngine mapperEngine) {
        this.mapperEngine = mapperEngine;
    }

    @Override
    public void beginHandle() {

        try {
            Socket socket = new Socket(MapperConfig.addr, MapperConfig.port);
            PrintWriter os = new PrintWriter(socket.getOutputStream());
            BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Tool.p("Create connection: ", socket);
            String readline = is.readLine();
            String roomInfo = "";
            while (readline!=null) {
                Tool.p("receive:" ,readline,readline.contains(MapperConfig.protocol_end));

                if(readline.contains(MapperConfig.protocol_end)){
                    //room info组装完毕
                    roomInfo = roomInfo + readline.replaceAll(MapperConfig.protocol_end,"");
                    handle(roomInfo);
                    roomInfo = "";

                    os.println("mapper data receive ok");
                    os.flush();

                }else{
                    roomInfo = roomInfo + readline + "\n";
                }

                readline = is.readLine();
            } //继续循环

            os.close(); //关闭Socket输出流
            is.close(); //关闭Socket输入流
            socket.close(); //关闭Socket

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public void beginHandle2() {
        try {

            ServerSocket s = new ServerSocket(MapperConfig.port);

            while (true) {
                Socket socket = s.accept();
                Tool.p("New connection: ", socket);

                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                String roomInfo="";
                String str;
                while ((str = br.readLine()) != null) {
                    Tool.p("receive:" ,str,str.contains(MapperConfig.protocol_end));

                    if(str==null || str.trim().equals("")){
                        continue;
                    }

                    if(str.contains(MapperConfig.protocol_end)){
                        //room info组装完毕
                        roomInfo = roomInfo + str.replaceAll(MapperConfig.protocol_end,"");
                        handle(roomInfo);
                        roomInfo = "";

                        pw.println("ok");
                        pw.flush();

                    }else{
                        roomInfo = roomInfo + str + "\n";
                    }
                }

                br.close();
                pw.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Pattern pattern = Pattern.compile("(.*?);;(.*?);;(.*?);;(.*?);;(.*?);;(.*?);;(.*);;",Pattern.DOTALL);

    /**
     * @param roomInfo 格式
     *  示例
     *
    crimson guild;;$apr1$dF!!_X#W$eA6mBDF.Y93f8W7tObZGP1;;west;;1;;Hallway east;;The hallway continues south-north here.  You catch the glint of light on steel
    from the eastern doorway.  Torchlight falls from sconces set high in the
    walls.
    ;;east,north,south;;@@


     *  规范
     *  [areaName];;[roomUID];;[exitUsed];;[indoors];;[shortDesc];;[longDesc];;[exits];;
     */
    public void handle(String roomInfo) {
        Tool.p("roominfo:",roomInfo);

        Matcher matcher = pattern.matcher(roomInfo);
        if(matcher.find()){
            String areaName = matcher.group(1);
            String roomUID = matcher.group(2);
            String exittUsed = matcher.group(3);
            boolean indoors = matcher.group(4).equals("1");
            String shortDesc = matcher.group(5);
            String longDesc = matcher.group(6);
            String exits = matcher.group(7);

            HashSet<String> exitsHash = new HashSet<String>();
            for(String exit : exits.split(",")){
                exitsHash.add(exit);
            }

            mapperEngine.moveToRoom(areaName, roomUID, exittUsed,indoors, shortDesc,longDesc,exitsHash);
        }else{
            Tool.p("not match!");
        }
    }

    private MapperEngine mapperEngine;
}
