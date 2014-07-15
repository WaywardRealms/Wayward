package net.wayward_realms.waywardlib.util.heightmap;

import org.bukkit.Location;

import java.util.Arrays;
import java.util.Vector;

public class HeightMap {

    private int scale;
    private Vector<HeightMapPoint> points;

    private int max;

    @SuppressWarnings("unchecked")
    HeightMap(int scale, HeightMapPoint... inputPoints){
        this.scale = scale;
        double testMax = 0;
        this.points = (Vector) Arrays.asList(inputPoints);
        //Find maximum point, in preparation for normalization.
        for(HeightMapPoint point: points){
            points.add(point);
            if(point.getValue() > testMax){
                testMax = point.getValue();
            }
        }
        this.max = (int)testMax;
        //Normalize, and then smooth/flatten out the heights.
        for(HeightMapPoint point: points){
            point.setValue((point.getValue()/(max))*(point.getValue()/(max)));
        }
    }

    public double getheight(Location location){
        int testY = location.getBlockY();
        int testX = location.getBlockX();
        double out = 0;
        for(HeightMapPoint point : points){
            double thisHeight =  ((point.getValue() * point.getValue() * scale * scale) - ( ((testX - point.getX())*(testX - point.getX())) + ((testY - point.getY())*(testY - point.getY()))));
            if(thisHeight > out){
                out = thisHeight;
            }
        }
        return (out/(scale*scale)) * max;
    }

    public boolean addpoint(HeightMapPoint point){
        this.points.add(point);
        return this.points.contains(point);
    }

    public Vector<HeightMapPoint> getPoints(){
        return points;
    }
}
