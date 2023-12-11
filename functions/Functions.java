package functions;

import functions.meta.*;

public abstract class Functions {
    public static Function shift(Function f, double shiftX, double shiftY){
        return new Shift(f, shiftX, shiftY);
    }
    public static Function scale(Function f, double scaleX, double scaleY){
        return new Scale(f, scaleX, scaleY);
    }
    public static Function power(Function f1, double power){
        return new Power(f1, power);
    }
    public static Function sum(Function f1, Function f2){
        return new Sum(f1, f2);
    }
    public static Function mult(Function f1, Function f2){
        return new Mult(f1, f2);
    }
    public static Function composition(Function f1, Function f2){
        return new Composition(f1, f2);
    }

    private static double area(Function func, double leftX, double rightX){
        return (func.getFunctionValue(leftX) + func.getFunctionValue(rightX)) * (rightX - leftX) / 2;
    }
    synchronized public static double integrate(Function func, double leftX, double rightX, double deltaX) throws IllegalArgumentException {
        if (leftX < func.getLeftDomainBorder() || rightX > func.getRightDomainBorder())
            throw  new IllegalArgumentException();
        int i = 0;
        double area = 0;
        while (leftX + i * deltaX < rightX - deltaX)
            area += area(func, leftX + (i++) * deltaX, leftX + i * deltaX);
        if (leftX + i * deltaX < rightX)
            area += area(func, leftX + i * deltaX, rightX);
        return area;
    }
}
