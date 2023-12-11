package functions.meta;

import functions.Function;

public class Power implements Function {
    Function expBase;
    double pow;
    public Power(Function expBase, double pow){
        this.expBase = expBase;
        this.pow = pow;
    }
    @Override
    public double getLeftDomainBorder() {
        return expBase.getLeftDomainBorder();
    }

    @Override
    public double getRightDomainBorder() {
        return expBase.getRightDomainBorder();
    }

    @Override
    public double getFunctionValue(double x) {
        return Math.pow(expBase.getFunctionValue(x), pow);
    }
}
