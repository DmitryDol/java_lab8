package functions;

import java.io.Serializable;

public class FunctionPoint implements Serializable, Cloneable {
    private double x,y;

    @Override
    public String toString(){
        return "(" + this.x + ", " + this.y + ")";
    }
    @Override
    public boolean equals(Object o){
        return (o instanceof FunctionPoint) && (((FunctionPoint)o).x == this.x)
                && (((FunctionPoint) o).y == this.y) &&
                (this.hashCode() == ((FunctionPoint)o).hashCode());
    }
    @Override
    public int hashCode(){
        int result = 17;
        long lx = Double.doubleToLongBits(this.x);
        long ly = Double.doubleToLongBits(this.y);
        result = 31*result + (int)( (int)(lx) ^ (lx >> 32));
        result = 31*result + (int)( (int)(ly) ^ (ly >> 32));
        return result;
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        FunctionPoint clone = (FunctionPoint) super.clone();
        clone.x = this.x;
        clone.y = this.y;
        return clone;
    }



    /**
     * create point with coordinates x, y
     * @param x abscissa of point
     * @param y ordinate of point
     */
    public FunctionPoint(double x, double y){
        this.x = x;
        this.y = y;
    }

    /**
     * copy constructor of FunctionPoint
     * @param point point that coordinates need to copy
     */
    public FunctionPoint(FunctionPoint point){
        this.x = point.x;
        this.y = point.y;
    }
    FunctionPoint(){
        this.x = 0;
        this.y = 0;
    }

    /**
     * @return x coordinate of point
     */
    public double getX() {
        return this.x;
    }

    /**
     * @return y coordinate of point
     */
    public double getY() {
        return this.y;
    }

    /**
     * set new coordinate for point using coordinates from another point
     * @param point point concluding new coordinate
     */
    public void setPoint(FunctionPoint point){
        this.x = point.getX();
        this.y = point.getY();
    }

    /**
     * set new x
     * @param x new x coordinate
     */
    public void setX(double x){
        this.x = x;
    }

    /**
     * set new y
     * @param y new y coordinate
     */
    public void setY(double y){
        this.y = y;
    }
}
