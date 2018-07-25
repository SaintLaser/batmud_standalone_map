package com.wind.server;

import com.alibaba.fastjson.JSON;
import com.glaurung.batMap.vo.json.Room;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class TestTranfer {

    @Test
    public void TestXfer(){
        Room room = new Room();
        room.setId("aaa");
        room.setPath("pppp");

        Set<String> exitSet = new HashSet<String>();
        exitSet.add("n");
        exitSet.add("s");
        room.setExits(exitSet);
        room.setEt(exitSet);

        com.glaurung.batMap.vo.Room room2 = room.xfer();

        System.out.println(JSON.toJSONString(room));
        System.out.println(JSON.toJSONString(room2));
        System.out.println(JSON.toJSONString(room2.getExits()));


        Assert.assertEquals(room.getId(),room2.getId());

        a1 a = new a1();
        a.setName("a1");
        Set<String> al = new HashSet<String>();
        al.add("1");
        al.add("2");
        a.setExits(al);

        System.out.println("a1 "+ JSON.toJSONString(a));
        a2 aa = JSON.parseObject(JSON.toJSONString(a),a2.class);
        aa.setPath("path");
        System.out.println("a2 "+JSON.toJSONString(aa));
    }
}

@Data
class a1{
    private String name;
    private Set<String> exits;
}


@Data
class a2{
    private String name;
    private Set<String> exits = new HashSet<String>();
    private String path;
}
