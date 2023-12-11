package functions;

import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayTabulatedFunction implements TabulatedFunction, Serializable, Cloneable {
    private FunctionPoint[] points;
    private int end;


    @Override
    public Iterator<FunctionPoint> iterator() {
        return new Iterator<>() {
            private int currentIndex = 0;
            @Override
            public boolean hasNext(){
                return currentIndex <= end;
            }

            @Override
            public FunctionPoint next(){
                if (!hasNext())
                    throw new NoSuchElementException();
                return points[currentIndex++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public String toString(){
        if (end == 0)
            return "{}";
        StringBuilder str = new StringBuilder("{" + points[0].toString());
        for (int i = 1; i < end + 1; ++i)
            str.append(", ").append(points[i].toString());
        return str.append("}").toString();
    }

    @Override
    public boolean equals(Object o){
        if (((o.getClass() != LinkedListTabulatedFunction.class) && (o.getClass() != ArrayTabulatedFunction.class) )||
                ((TabulatedFunction) o).getPointsCount() != this.getPointsCount())
            return false;

        if (this.hashCode() != o.hashCode())
            return false;

        if (o instanceof ArrayTabulatedFunction) {
            for (int i = 0; i < this.getPointsCount(); ++i)
                if (!this.points[i].equals(((ArrayTabulatedFunction) o).points[i]))
                    return false;
        }
        else {
            for (int i = 0; i < this.getPointsCount(); ++i)
                if (!this.getPoint(i).equals(((LinkedListTabulatedFunction) o).getPoint(i)))
                    return false;
        }
        return true;
    }
    @Override
    public int hashCode(){
        int result = 0;
        for(int i = 0; i < this.end + 1; ++i){
            result ^= this.points[i].hashCode();
        }
        return result ^ (end+1);
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        ArrayTabulatedFunction cloned = (ArrayTabulatedFunction) super.clone();
        cloned.points = new FunctionPoint[this.points.length];
        for (int i = 0; i < this.end + 1; ++i)
            cloned.points[i] = (FunctionPoint) this.points[i].clone();
        return cloned;
    }

    /*@Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(end);
        for (FunctionPoint point : this.points) {
            if (point != null) {
                out.writeObject(point.getX());
                out.writeObject(point.getY());
            }
        }
    }*/

    /*@Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.end = (int)in.readObject();
        int size = (int)Math.pow(2, ((int)(Math.log10(end + 1)/
                Math.log10(2)) + 1));
        this.points = new FunctionPoint[size];
        for(int i = 0; i < end; ++i)
            this.points[i] = new FunctionPoint(in.read(), in.read());

    }*/

    public ArrayTabulatedFunction(){}

    public ArrayTabulatedFunction(ArrayTabulatedFunction tab){
        this.end = tab.end;
        this.points = new FunctionPoint[tab.points.length];
        System.arraycopy(tab.points, 0, this.points, 0, tab.points.length);
    }
    boolean isDisorderedByXArray(FunctionPoint[] points){
        for (int i = 1; i < points.length; ++i)
            if (points[i-1].getX() > points[i].getX())
                return true;
        return false;
    }
    public ArrayTabulatedFunction(FunctionPoint[] points) throws IllegalArgumentException{
        if (points.length < 2 || isDisorderedByXArray(points))
            throw new IllegalArgumentException();
        end = points.length - 1;
        int size = (int)Math.pow(2, ((int)(Math.log10(points.length)/
                Math.log10(2)) + 1));
        this.points = new FunctionPoint[size];
        for (int i = 0; i < points.length; ++i)
            this.points[i] = new FunctionPoint(points[i]);

    }

    /**
     * Constructor for function with all y`s equals to 0
     * @param leftX left domain border of function 
     * @param rightX right domain border of function
     * @param pointsCount amount of points where function is defined
     */
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX || pointsCount < 2)
            throw new IllegalArgumentException();

        end = pointsCount - 1;
        double step = (rightX - leftX)/ (pointsCount - 1);
        int size = (int)Math.pow(2, ((int)(Math.log10(pointsCount)/
                Math.log10(2)) + 1));
        this.points = new FunctionPoint[size];
        for (int i = 0; i < pointsCount; ++i)
            this.points[i] = new FunctionPoint(leftX + i*step,0);

    }

    /**
     * 
     * @param leftX left domain border of function 
     * @param rightX right domain border of function
     * @param values array of Y values of function
     */
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX || values.length < 2)
            throw new IllegalArgumentException();

        end = values.length - 1;
        double step = (rightX - leftX)/ (values.length - 1);
        int size = (int)Math.pow(2, ((int)(Math.log10(values.length)/
                Math.log10(2)) + 1));
        this.points = new FunctionPoint[size];
        for (int i = 0; i < values.length; ++i)
            this.points[i] = new FunctionPoint(leftX + i*step, values[i]);
    }

    public double getLeftDomainBorder() {
        return this.points[0].getX();
    }
    public double getRightDomainBorder() {
        return this.points[end].getX();
    }

    /**
     * in case where searchingX isn`t in this.points return index where need to enter point with searchingX coordinates
     * @return index of point with x equals to searchingX in this.points
     */
    private int binarySearch(double searchingX){
        int left = -1;
        int right = this.end + 1;
        while (left + 1 < right){
            int middle = (left + right) / 2;
            if (this.points[middle].getX() < searchingX)
                left = middle;
            else
                right = middle;
        }
        return right;
    }

    /**
     * find value of function in point x
     * @param x must be in domain
     * @return value of f(x)
     */
    public double getFunctionValue(double x) {
        if (x < this.getLeftDomainBorder() || x > this.getRightDomainBorder())
            return Double.NaN;

        int i = binarySearch(x);
        if (x == this.points[i].getX())
            return this.points[i].getY();
        FunctionPoint point1 = this.points[i-1];
        FunctionPoint point2 = this.points[i];
        return (point2.getY() - point1.getY()) * (x - point1.getX()) /
                (point2.getX() - point1.getX()) + point1.getY();
    }

    /**
     * @return amount of points
     */
    public int getPointsCount() {
        return this.end + 1;
    }

    /**
     * @param index from 0 to points count
     * @return class FunctionPoint
     */
    public FunctionPoint getPoint(int index){
        if (index > end || index < 0)
            throw new FunctionPointIndexOutOfBoundsException();
        return this.points[index];
    }

    /**
     * @param index of point in this.points
     * @param point class Function point with new x and y values
     */
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index > end || index < 0)
            throw new FunctionPointIndexOutOfBoundsException();

        if ((index != 0)&&(point.getX() <= this.points[index-1].getX()) ||
                (index <= end) && (point.getX() >= this.points[index+1].getX()))
            throw new InappropriateFunctionPointException();
        this.points[index].setPoint(point);
    }

    /**
     * @return x coordinate of this.points[index]
     */
    public double getPointX(int index){
        if (index > end || index < 0)
            throw new FunctionPointIndexOutOfBoundsException();
        return this.points[index].getX();
    }

    /**
     * set x value for this.points[index]
     * @param x new x
     */
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (index > end || index < 0)
            throw new FunctionPointIndexOutOfBoundsException();

        if ((x <= this.points[index-1].getX()) || (x >= this.points[index+1].getX()))
            throw new InappropriateFunctionPointException();
        this.points[index].setX(x);
    }

    /**
     * @return y coordinate of this.points[index]
     */
    public double getPointY(int index){
        if (index > end || index < 0)
            throw new FunctionPointIndexOutOfBoundsException();
        return this.points[index].getY();
    }

    /**
     * set y value for this.points[index]
     * @param y new y
     */
    public void setPointY(int index, double y){
        if (index > end || index < 0)
            throw new FunctionPointIndexOutOfBoundsException();
        this.points[index].setY(y);
    }

    /**
     * delete point in specific index
     * @param index index of deleting point from this.points
     */
    public void deletePoint(int index){
        if (end < 3)
            throw new IllegalStateException();
        if (index > end || index < 0)
            throw new FunctionPointIndexOutOfBoundsException();
        if (end <= this.points.length / 4){
            FunctionPoint[] old = this.points;
            this.points = new FunctionPoint[old.length/2];
            System.arraycopy(old, 0, this.points, 0,end + 1);
        }
        System.arraycopy(this.points, index+1, this.points, index, end - index + 1);
        --this.end;
    }

    /**
     * add point to function
     * @param point object of FunctionPoint class with x and y coordinates
     */
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        int ind = binarySearch(point.getX());
        if (ind < this.end && this.points[ind].getX() == point.getX()){
            throw new InappropriateFunctionPointException();
        }
        if (this.end == this.points.length - 1) {
            FunctionPoint[] old = this.points;
            this.points = new FunctionPoint[2*old.length];
            System.arraycopy(old,0, this.points,0, old.length);
        }
        if (ind != this.end + 1) {

            System.arraycopy(this.points, ind, this.points, ind+1, end - ind + 1);
        }
        this.points[ind] = new FunctionPoint(point);
        this.end += 1;
    }

    public static class ArrayTabulatedFunctionFactory implements TabulatedFunctionFactory{

        @Override
        public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
            return new ArrayTabulatedFunction(points);
        }

        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
            return new ArrayTabulatedFunction(leftX, rightX, pointsCount);
        }

        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
            return new ArrayTabulatedFunction(leftX, rightX, values);
        }
    }

}
