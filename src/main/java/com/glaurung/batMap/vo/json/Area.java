package com.glaurung.batMap.vo.json;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

@Data
public class Area implements Serializable {

    private static final long serialVersionUID = 5397970358054415742L;
    private String name;

    public Area( String name ) {
        this.name = name;
    }

    //转换类型
    public com.glaurung.batMap.vo.Area xfer(){
        String json = JSON.toJSONString(this);
        return JSON.parseObject(json, com.glaurung.batMap.vo.Area.class);
    }
}
