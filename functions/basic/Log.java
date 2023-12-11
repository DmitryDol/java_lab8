package functions.basic;

import functions.Function;

public class Log implements Function {
    double base;
    public Log(double base){
        this.base = base;
    }
    @Override
    public double getLeftDomainBorder() {
        return Double.NEGATIVE_INFINITY;
    }

    @Override
    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public double getFunctionValue(double x) {
        return Math.log(x) / Math.log(this.base);
    }
}
