package functions;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static java.lang.Math.abs;

public class LinkedListTabulatedFunction implements TabulatedFunction, Externalizable, Cloneable {
    private int size;
    private static class FunctionNode{
        private FunctionPoint data;
        private FunctionNode next, prev;
        private FunctionNode(){
            this.data = null;
            this.next = this;
            this.prev = this;
        }
        private FunctionNode(FunctionPoint point){
            this.data = new FunctionPoint(point);
            this.next = this;
            this.prev = this;
        }
        public FunctionPoint getPoint(){
            return data;
        }

    }
    private final FunctionNode head;
    private FunctionNode lastUsed;
    private int lastUsedIndex;

    @Override
    public Iterator<FunctionPoint> iterator() {
        return new Iterator<>() {
            private FunctionNode current = head.next;
            @Override
            public boolean hasNext(){
                return current != head;
            }

            @Override
            public FunctionPoint next(){
                if (!hasNext())
                    throw new NoSuchElementException();
                FunctionPoint data = current.data;
                current = current.next;
                return data;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public String toString(){
        FunctionNode node = this.head.next;
        StringBuilder str = new StringBuilder("{" + node.data.toString());
        node = node.next;
        while(node != this.head){
            str.append(",").append(node.data);
            node = node.next;
        }
        return str.append("}").toString();
    }
    @Override
    public boolean equals(Object o){
        if (!(o instanceof TabulatedFunction) || (this.getPointsCount() != ((TabulatedFunction)o).getPointsCount()))
            return false;
        if (this.hashCode() != ((TabulatedFunction)o).hashCode())
            return false;
        for(int i = 0; i < this.getPointsCount(); ++i){
            if (this.getPoint(i).equals(((TabulatedFunction)o).getPoint(i)))
                return false;
        }
        return true;
    }
    @Override
    public int hashCode(){
        FunctionNode node = this.head.next;
        int result = 0;
        while (node != this.head){
            result ^= node.data.hashCode();
            node = node.next;
        }
        return result ^ size;
    }
    // TODO rewrite clone methode
    @Override
    public Object clone(){
        return new LinkedListTabulatedFunction(this);
    }

    /**
     * constructor that copy another LinkedListTabulatedFunction
     * @param tab LinkedListTabulatedFunction
     */
    public LinkedListTabulatedFunction(LinkedListTabulatedFunction tab) {
        this.size = tab.size;
        this.head = new FunctionNode();
        this.lastUsedIndex = tab.lastUsedIndex;
        this.lastUsed = tab.lastUsed;
        FunctionNode node = tab.head.next;
        while(node != tab.head){
            addNodeToTail(node.data);
            node = node.next;
        }
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(size);
        FunctionNode node = head.next;
        while (node != head){
            out.writeObject(node.data.getX());
            out.writeObject(node.data.getY());
            node = node.next;
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int sz = (int)in.readObject();
        for(int i = 0; i < sz; ++i){
            this.addNodeToTail(new FunctionPoint((double)in.readObject(), (double)in.readObject()));
        }
    }

    /**
     * create empty list (used in Externalizable)
     */
    public LinkedListTabulatedFunction(){
        this.size = 0;
        this.head = new FunctionNode();
    }

    private boolean isDisorderedByXArray(FunctionPoint[] points){
        for (int i = 1; i < points.length; ++i)
            if (points[i-1].getX() > points[i].getX())
                return true;
        return false;
    }
    public LinkedListTabulatedFunction(FunctionPoint[] points) throws IllegalArgumentException{
        if (points.length < 2 || isDisorderedByXArray(points))
            throw new IllegalArgumentException();
        this.head = new FunctionNode();
        this.size = 0;
        for (FunctionPoint point : points) this.addNodeToTail(point);

    }

    /**
     *  find node with index
     * @param index index of node in list
     * @return FunctionNode
     */
    private FunctionNode getNodeByIndex(int index){
        if (this.lastUsed != null && abs(index - this.lastUsedIndex) < abs(index + 1) ){
            for (int i = 0; i < index - this.lastUsedIndex; ++i)
                this.lastUsed = this.lastUsed.next;
            for (int i = 0; i < this.lastUsedIndex - index; ++i)
                this.lastUsed = this.lastUsed.prev;
        } else{
            this.lastUsed = this.head;
            if (this.size - index < index)
                for (int i = 0; i <= this.size - index; ++i)
                    this.lastUsed = this.lastUsed.prev;
            else
                for (int i = 0; i <= index; ++i)
                    this.lastUsed = this.lastUsed.next;
        }

        this.lastUsedIndex = index;
        return this.lastUsed;
    }

    /**
     * add empty node to tail of list
     * @return link to the added node
     */
    private FunctionNode addNodeToTail(){
        FunctionNode nd = new FunctionNode();
        ++this.size;
        nd.next = this.head;
        nd.prev = this.head.prev;
        this.head.prev = nd;
        nd.prev.next = nd;
        return nd;
    }

    /**
     * add empty node to the list
     * @param index where to add node
     * @return link to the added node
     */
    private FunctionNode addNodeByIndex(int index){
        FunctionNode nd = getNodeByIndex(index);
        FunctionNode new_node = new FunctionNode();
        new_node.next = nd;
        new_node.prev = nd.prev;
        nd.prev = new_node;
        new_node.prev.next = new_node;
        return new_node;
    }

    /**
     * delete node in specific index
     * @param index where to delete node
     * @return deleted node
     */
    private FunctionNode deleteNodeByIndex(int index){
        FunctionNode nd = this.getNodeByIndex(index);
        nd.next.prev = nd.prev;
        nd.prev.next = nd.next;
        nd.next = null;
        nd.prev = null;
        this.lastUsed = null;
        this.lastUsedIndex = -1;
        return nd;
    }

    /**
     * add filled node to the tail
     * @param point data in new node
     */
    private void addNodeToTail(FunctionPoint point){
        FunctionNode nd = this.addNodeToTail();
        nd.data = new FunctionPoint(point);
    }

    /**
     * constructor that creates list with size pointsCount. y coordinate for all points is equals to 0
     * @param leftX left domain border of function
     * @param rightX right domain border of function
     * @param pointsCount amount of points in tabulated function
     */
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) throws IllegalArgumentException{
        if (leftX >= rightX || pointsCount < 2)
            throw new IllegalArgumentException();

        this.head = new FunctionNode();
        this.size = 0;
        double step = (rightX - leftX)/ (pointsCount - 1);
        for (int i = 0; i < pointsCount; ++i){
            this.addNodeToTail(new FunctionPoint(leftX + i*step,0));
        }
    }

    /**
     * constructor that creates list with size values.length and y coordinates from values array
     * @param leftX left domain border of function
     * @param rightX right domain border of function
     * @param values y coordinates for each point
     */
    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) throws IllegalArgumentException{
        if (leftX >= rightX || values.length < 2)
            throw new IllegalArgumentException();

        this.head = new FunctionNode();
        this.size = 0;
        double step = (rightX - leftX)/ (values.length - 1);
        for (int i = 0; i < values.length; ++i)
            this.addNodeToTail(new FunctionPoint(leftX + i*step, values[i]));
    }

    public double getLeftDomainBorder() {
        return this.head.next.data.getX();
    }
    public double getRightDomainBorder() {
        return this.head.prev.data.getX();
    }

    private FunctionNode searchNodeByX(double x){
        FunctionNode nd = this.head.next;
        while (nd != this.head && nd.data.getX() < x)
            nd = nd.next;
        return nd;
    }
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder())
            return Double.NaN;

        FunctionNode point = searchNodeByX(x);
        FunctionPoint point1 = point.prev.data;
        FunctionPoint point2 = point.data;
        return (point2.getY() - point1.getY()) * (x - point1.getX()) /
                (point2.getX() - point1.getX()) + point1.getY();
    }
    public  int getPointsCount() {
        return this.size;
    }
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException{
        if (index >= this.size)
            throw new FunctionPointIndexOutOfBoundsException();
        return this.getNodeByIndex(index).data;
    }
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException, FunctionPointIndexOutOfBoundsException {
        if (index >= this.size)
            throw new FunctionPointIndexOutOfBoundsException();

        FunctionNode node = this.getNodeByIndex(index);
        if ((node.prev != this.head)&&(point.getX() <= node.prev.data.getX()) ||
                (point.getX() >= node.next.data.getX()) && (node.next != this.head))
            throw new InappropriateFunctionPointException();
        node.data.setPoint(point);
    }
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException{
        if (index >= this.size || index < 0)
            throw new FunctionPointIndexOutOfBoundsException();
        return this.getPoint(index).getX();
    }
    public void setPointX(int index, double x) throws InappropriateFunctionPointException, FunctionPointIndexOutOfBoundsException {
        if (index >= this.size || index < 0)
            throw new FunctionPointIndexOutOfBoundsException();
        FunctionNode node = this.getNodeByIndex(index);
        if ((x <= node.prev.data.getX()) || (x >= node.next.data.getX()))
            throw new InappropriateFunctionPointException();
        node.data.setX(x);
    }
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException{
        if (index >= this.size || index < 0)
            throw new FunctionPointIndexOutOfBoundsException();
        return this.getPoint(index).getY();
    }
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException{
        if (index >= this.size || index < 0)
            throw new FunctionPointIndexOutOfBoundsException();
        this.getPoint(index).setY(y);
    }

    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException{
        if (this.size < 3)
            throw new IllegalStateException();
        if (index >= this.size || index < 0)
            throw new FunctionPointIndexOutOfBoundsException();

        this.deleteNodeByIndex(index);
        --this.size;
    }
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        FunctionNode node = searchNodeByX(point.getX());
        if (node != this.head && node.data.getX() == point.getX()){
            throw new InappropriateFunctionPointException();
        }

        FunctionNode nw_node = new FunctionNode(point);
        nw_node.prev = node.prev;
        nw_node.next = node;
        node.prev = nw_node;
        nw_node.prev.next = nw_node;
        ++this.size;
    }

    public static class LinkedListTabulatedFunctionFactory implements TabulatedFunctionFactory{

        @Override
        public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
            return new LinkedListTabulatedFunction(points);
        }

        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
            return new LinkedListTabulatedFunction(leftX, rightX, pointsCount);
        }

        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
            return new LinkedListTabulatedFunction(leftX, rightX, values);
        }
    }
}

