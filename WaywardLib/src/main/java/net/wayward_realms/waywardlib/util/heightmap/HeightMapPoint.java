package net.wayward_realms.waywardlib.util.heightmap;


public class HeightMapPoint {

    private int x;
    private int y;
    private double value;

    HeightMapPoint(int x, int y, int value){
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getValue() {
        return value;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
