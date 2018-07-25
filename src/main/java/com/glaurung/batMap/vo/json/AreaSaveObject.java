package com.glaurung.batMap.vo.json;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import edu.uci.ics.jung.graph.SparseMultigraph;
import lombok.Data;

@Data
public class AreaSaveObject implements Serializable {


    private static final long serialVersionUID = - 787030872360880875L;

    private SparseMultigraph<Room, Exit> graph;
    private Map<Room, Point2D> locations;
    private Map<Room, LocalPoint2D> locationsPersistance;
    private String fileName;

    //将locations内容转移到locationsPersistance
    public void toPersistance(){
        if( locations == null ){
            return;
        }
        locationsPersistance = new HashMap<Room, LocalPoint2D>();
        for( Room room : locations.keySet()){
            Point2D point2D = locations.get(room);
            LocalPoint2D localPoint2D = LocalPoint2D.genLocalPoint2D(point2D);
            locationsPersistance.put(room,localPoint2D);
        }
    }

    //将locationsPersistance内容转移到locations
    public void toPoint2D(){
        if( locationsPersistance == null ){
            return;
        }

        locations = new HashMap<Room, Point2D>();
        for( Room room : locationsPersistance.keySet()){
            LocalPoint2D localPoint2D = locationsPersistance.get(room);
            Point2D point2D = LocalPoint2D.genPoint2D(localPoint2D);
            locations.put(room, point2D);
        }
    }

    public AreaSaveObject() {
        this.graph = new SparseMultigraph<Room, Exit>();
        this.locations = new HashMap<Room, Point2D>();
    }

    public com.glaurung.batMap.vo.AreaSaveObject xfer(){
        String json = JSON.toJSONString(this);
        com.glaurung.batMap.vo.AreaSaveObject areaSaveObject = JSON.parseObject(json, com.glaurung.batMap.vo.AreaSaveObject.class);


        return areaSaveObject;
    }

}
