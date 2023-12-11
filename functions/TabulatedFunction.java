package functions;


public interface TabulatedFunction extends Function, Cloneable, Iterable<FunctionPoint> {

    public Object clone() throws CloneNotSupportedException;

    /**
     * @return amount of points
     */
    public int getPointsCount();

    /**
     * @param index from 0 to points count
     * @return class FunctionPoint
     */
    public FunctionPoint getPoint(int index);

    /**
     * @param index of point in this.points
     * @param point class Function point with new x and y values
     */
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException;

    /**
     * @return x coordinate of this.points[index]
     */
    public double getPointX(int index);

    /**
     * set x value for this.points[index]
     * @param x new x
     */
    public void setPointX(int index, double x) throws InappropriateFunctionPointException;

    /**
     * @return y coordinate of this.points[index]
     */
    public double getPointY(int index);

    /**
     * set y value for this.points[index]
     * @param y new y
     */
    public void setPointY(int index, double y);

    /**
     * delete point in specific index
     * @param index index of deleting point from this.points
     */
    public void deletePoint(int index);

    /**
     * add point to function
     * @param point object of FunctionPoint class with x and y coordinates
     */
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException;
}
