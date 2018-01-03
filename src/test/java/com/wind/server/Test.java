package com.wind.server;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    public static void main(String[] args) throws IOException {

        Pattern pattern = Pattern.compile("(.*?);;(.*?);;(.*?);;(.*?);;(.*?)@@(.*?)@@(.*)");

        String s = "crimson guild;;$apr1$dF!!_X#W$.kJanwjLtTrWFuyDwDOzA.;;south;;1;;south,north,west,east@@short1@@long1";

        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            System.out.println("all\t" + matcher.group());
            System.out.println("areaName\t" + matcher.group(1));
            System.out.println("roomUID\t" + matcher.group(2));
            System.out.println("exittUsed\t" + matcher.group(3));
            System.out.println("indoors\t" + matcher.group(4));
            System.out.println("exits\t" + matcher.group(5));
            System.out.println("shortDesc\t" + matcher.group(6));
            System.out.println("longDesc\t" + matcher.group(7));
        }

        File directory = new File("");//设定为当前文件夹

            System.out.println(directory.getCanonicalPath());//获取标准的路径
            System.out.println(directory.getAbsolutePath());//获取绝对路径


    }
}
