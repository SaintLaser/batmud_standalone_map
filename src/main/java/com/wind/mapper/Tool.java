package com.wind.mapper;


public class Tool {

    /**
     * i dont want to use logback
     * so,just a simple test tool
     */
    public static void p(Object ...args){
        for( Object arg : args ){
            System.out.print( arg.toString() + ", ");
        }
        System.out.println();
    }
}
