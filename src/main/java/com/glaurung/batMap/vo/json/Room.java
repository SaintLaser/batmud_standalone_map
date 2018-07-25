package com.glaurung.batMap.vo.json;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.awt.Color;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
public class Room implements Serializable {
    private static final long serialVersionUID = 123;

    private String id;
    private String shortDesc;
    private String longDesc;
    private boolean areaEntrance = false;
    private boolean current = false;
    private boolean drawn = false;
    //所属区域
    private Area area;
    private boolean picked = false;
    private Set<String> exits = new HashSet<String>();
    private Set<String> et = new HashSet<String>();
    private boolean indoors;
    private String notes;
    private Color color = null;

    //
    private String path;


    public Room(){}

    public Room(String shortDesc, String id ) {
        this.shortDesc = shortDesc;
        this.id = id;
    }

    public Room( String shortDesc, String id, Area area ) {
        this.shortDesc = shortDesc;
        this.id = id;
        this.area = area;
    }

    public Room( String id, Area area ) {
        this.id = id;
        this.area = area;
    }

    public String toString() {
        return this.shortDesc;
    }

    public boolean equals( Object o ) {
        if (o instanceof Room) {
            if (this.id.equals( ( (Room) o ).getId() ))
                return true;
        }
        return false;

    }

    public void addExits( Collection<String> outExits ) {
        this.exits.addAll( outExits );
    }

    public void addExit( String exit ) {
        this.exits.add( exit );
    }

    public com.glaurung.batMap.vo.Room xfer(){
        String json = JSON.toJSONString(this);
        com.glaurung.batMap.vo.Room roomNew =  JSON.parseObject(json, com.glaurung.batMap.vo.Room.class);
        return roomNew;
    }
}
