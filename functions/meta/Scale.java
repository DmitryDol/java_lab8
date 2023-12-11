package functions.meta;

import functions.Function;

public class Scale implements Function {
    Function func;
    double xScale, yScale;
    public Scale(Function func, double xScale, double yScale){
        this.func = func;
        this.xScale = xScale;
        this.yScale = yScale;
    }
    @Override
    public double getLeftDomainBorder() {
        if (xScale > 0)
            return func.getLeftDomainBorder() * xScale;
        return func.getRightDomainBorder() * xScale;
    }

    @Override
    public double getRightDomainBorder() {
        if (xScale > 0)
            return func.getRightDomainBorder() * xScale;
        return func.getLeftDomainBorder() * xScale;
    }

    @Override
    public double getFunctionValue(double x) {
        return func.getFunctionValue(x * xScale) * yScale;
    }
}
