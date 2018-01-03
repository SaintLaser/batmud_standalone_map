package com.wind.mapper.handler;

import com.glaurung.batMap.controller.MapperEngine;
import com.wind.mapper.common.Tool;
import com.wind.mapper.config.MapperConfig;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
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

            ServerSocket s = new ServerSocket(MapperConfig.port);

            while (true) {
                Socket socket = s.accept();
                Tool.p("New connection: ", socket);

                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                String str;
                while ((str = br.readLine()) != null) {
                    System.out.println("receive:" + str);
                    handle(str);
                    pw.println("ok");
                    pw.flush();
                }

                br.close();
                pw.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Pattern pattern = Pattern.compile("(.*?);;(.*?);;(.*?);;(.*?);;(.*?)@@(.*?)@@(.*)");

    /**
     * @param roomInfo 格式
     *  示例
     *  arelium;;$apr1$dF!!_X#W$dG7RGdqpERshxk5XnrLDN/;;west;;1;;east@@shortdesc@@longdesc
     *  crimson guild;;$apr1$dF!!_X#W$kXXkOct/6BkbBsksle6aS/;;north;;1;;south,west,east@@shortdesc@@longdesc
     *  规范
     *  [areaName];;[roomUID];;[exitUsed];;[indoors];;[exits1,exits2]@@[shortDesc]@@[longDesc]
     */
    public void handle(String roomInfo) {
        if(roomInfo==null || roomInfo.trim().equals("")){
            return;
        }

        Matcher matcher = pattern.matcher(roomInfo);
        if(matcher.find()){
            String areaName = matcher.group(1);
            String roomUID = matcher.group(2);
            String exittUsed = matcher.group(3);
            boolean indoors = matcher.group(4).equals("1");
            String exits = matcher.group(5);
            String shortDesc = matcher.group(6);
            String longDesc = matcher.group(7);

            HashSet<String> exitsHash = new HashSet<String>();
            for(String exit : exits.split(",")){
                exitsHash.add(exit);
            }

            mapperEngine.moveToRoom(areaName, roomUID, exittUsed,indoors, areaName+"\n" + shortDesc,longDesc,exitsHash);
        }
    }

    private MapperEngine mapperEngine;
}
