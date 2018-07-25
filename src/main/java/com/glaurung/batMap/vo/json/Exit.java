package com.glaurung.batMap.vo.json;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

@Data
public class Exit implements Serializable {


    private static final long serialVersionUID = 3983564665752135097L;
    public final String TELEPORT = "teleport";

    private String exit;
    //方向简称
    private String compassDir;
    private boolean currentExit;


    public Exit( String exit ) {
        this.exit = exit;
        this.compassDir = this.checkWhatExitIs( exit );
    }

    private String checkWhatExitIs( String exit ) {
        if (exit.equalsIgnoreCase( "n" ) || exit.equalsIgnoreCase( "north" ))
            return "n";
        if (exit.equalsIgnoreCase( "e" ) || exit.equalsIgnoreCase( "east" ))
            return "e";
        if (exit.equalsIgnoreCase( "s" ) || exit.equalsIgnoreCase( "south" ))
            return "s";
        if (exit.equalsIgnoreCase( "w" ) || exit.equalsIgnoreCase( "west" ))
            return "w";
        if (exit.equalsIgnoreCase( "ne" ) || exit.equalsIgnoreCase( "northeast" ))
            return "ne";
        if (exit.equalsIgnoreCase( "nw" ) || exit.equalsIgnoreCase( "northwest" ))
            return "nw";
        if (exit.equalsIgnoreCase( "se" ) || exit.equalsIgnoreCase( "southeast" ))
            return "se";
        if (exit.equalsIgnoreCase( "sw" ) || exit.equalsIgnoreCase( "southwest" ))
            return "sw";

        return null;
    }


    public String toString() {
        return this.exit;
    }

    public boolean equals( Object o ) {
        if (o instanceof Exit) {
            if (this.exit.equals( ( (Exit) o ).getExit() ))
                return true;
        }
        return false;

    }

    public com.glaurung.batMap.vo.Exit xfer(){
        String json = JSON.toJSONString(this);
        return JSON.parseObject(json, com.glaurung.batMap.vo.Exit.class);
    }

}
