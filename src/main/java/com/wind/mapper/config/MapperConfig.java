package com.wind.mapper.config;

import com.wind.mapper.common.Tool;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MapperConfig {

    public static String protocol_end = "@@";

    /**
     * 真正的文件存储位置在这个目录下的 conf\batMapAreas 内
     */
    public static String baseDir;

    /**
     * 主程序窗口的初始宽、高
     */
    public static int mainWidth;
    public static int mainHeight;

    public static int mainFont;

    /**
     * socket监听的窗口
     */
    public static int port = 8081;

    public static void load() throws IOException {
        //todo
        InputStream in = ClassLoader.getSystemResourceAsStream("config.properties");
        Properties p = new Properties();
        p.load(in);

        baseDir=p.getProperty("baseDir");
        mainWidth = Integer.valueOf(p.getProperty("mainWidth"));
        mainHeight = Integer.valueOf(p.getProperty("mainHeight"));
        mainFont = Integer.valueOf(p.getProperty("mainFont"));
        port = Integer.valueOf(p.getProperty("port"));

        System.out.println("configs: ");
        System.out.println("baseDir="+baseDir);
        System.out.println("port="+port);
        System.out.println("mainWidth="+mainWidth);
        System.out.println("mainHeight="+mainHeight);
        System.out.println("mainFont="+mainFont);

        //创建地图文件的目录 真正的文件存储位置在baseDir目录下的 conf\batMapAreas 内
        File dir = new File(baseDir);

        if(!dir.exists()) {
            dir.mkdir();
        }

    }
}
