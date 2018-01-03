package com.wind.server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    public static void main(String[] args) {

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

    }
}
