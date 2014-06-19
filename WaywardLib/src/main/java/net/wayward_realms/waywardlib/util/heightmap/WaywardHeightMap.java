package net.wayward_realms.waywardlib.util.heightmap;

import org.bukkit.Location;

import java.util.Arrays;
import java.util.Vector;

public class WaywardHeightMap {

    private int max;
    private Vector<WaywardHeightMapPoint> points;

    @SuppressWarnings("unchecked")
    WaywardHeightMap(WaywardHeightMapPoint... inputPoints){
        int testMax = 0;
        this.points = (Vector) Arrays.asList(inputPoints);
        //Find maximum point, in preparation for normalization.
        for(WaywardHeightMapPoint point: points){
            points.add(point);
            if(point.getValue() > testMax){
                testMax = point.getValue();
            }
        }
        this.max = testMax;
        //Normalize, and then smooth/flatten out the heights.
        for(WaywardHeightMapPoint point: points){
            point.setValue((point.getValue()/max)^2);
        }
    }

    public int getheight(Location location){
        int testY = location.getBlockY();
        int testX = location.getBlockX();
        int out = 0;
        for(WaywardHeightMapPoint point : points){
            int thisHeight =  ((point.getValue()^2) - ( ((testX - point.getX())^2) + ((testY - point.getY())^2)));
            if(thisHeight > out){
                out = thisHeight;
            }
        }
        return out * max;
    }

    public boolean addpoint(WaywardHeightMapPoint point){
        this.points.add(point);
        return this.points.contains(point);
    }

    public Vector<WaywardHeightMapPoint> getPoints(){
        return points;
    }
}
