package com.glaurung.batMap.vo;

import lombok.Data;

import java.io.Serializable;


@Data
public class Area implements Serializable {

    private static final long serialVersionUID = 5397970358054415742L;
    private String name;

    public Area( String name ) {
        this.name = name;
    }
}
