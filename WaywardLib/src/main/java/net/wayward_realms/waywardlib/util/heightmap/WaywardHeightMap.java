package net.wayward_realms.waywardlib.util.heightmap;

import org.bukkit.Location;

import java.util.Arrays;
import java.util.Vector;

public class WaywardHeightMap {

    private int scale;
    private Vector<WaywardHeightMapPoint> points;

    private int max;

    @SuppressWarnings("unchecked")
    WaywardHeightMap(int scale, WaywardHeightMapPoint... inputPoints){
        this.scale = scale;
        int testMax = 0;
        this.points = (Vector) Arrays.asList(inputPoints);
        //Find maximum point, in preparation for normalization.
        for(WaywardHeightMapPoint point: points){
            points.add(point);
            if(point.getValue() > testMax){
                testMax = point.getValue();
                point.setValue(point.getValue());
            }
        }
        this.max = testMax;
        //Normalize, and then smooth/flatten out the heights.
        for(WaywardHeightMapPoint point: points){
            point.setValue((point.getValue()/(max))*(point.getValue()/(max)));
        }
    }

    public int getheight(Location location){
        int testY = location.getBlockY();
        int testX = location.getBlockX();
        int out = 0;
        for(WaywardHeightMapPoint point : points){
            int thisHeight =  ((point.getValue() * point.getValue() * scale * scale) - ( ((testX - point.getX())*(testX - point.getX())) + ((testY - point.getY())*(testY - point.getY()))));
            if(thisHeight > out){
                out = thisHeight;
            }
        }
        return (out/(scale*scale)) * max;
    }

    public boolean addpoint(WaywardHeightMapPoint point){
        this.points.add(point);
        return this.points.contains(point);
    }

    public Vector<WaywardHeightMapPoint> getPoints(){
        return points;
    }
}
