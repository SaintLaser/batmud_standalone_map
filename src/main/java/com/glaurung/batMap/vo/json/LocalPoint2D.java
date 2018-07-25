package com.glaurung.batMap.vo.json;

import lombok.Data;

import java.awt.geom.Point2D;

@Data
public class LocalPoint2D{
    private double px;
    private double py;

    public static Point2D genPoint2D(LocalPoint2D localPoint2D){
        if( localPoint2D == null ){
            return null;
        }

        return new Point2D.Double(localPoint2D.getPx(),localPoint2D.getPy());
    }

    public static LocalPoint2D genLocalPoint2D(Point2D point2D){
        if( point2D == null ){
            return null;
        }

        LocalPoint2D localPoint2D = new LocalPoint2D();
        localPoint2D.setPx(point2D.getX());
        localPoint2D.setPy(point2D.getY());

        return localPoint2D;
    }
}
