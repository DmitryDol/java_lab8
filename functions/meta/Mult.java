package functions.meta;

import functions.Function;

public class Mult implements Function {
    private Function func1, func2;
    public Mult(Function func1, Function func2){
        this.func1 = func1;
        this.func2 = func2;
    }
    @Override
    public double getLeftDomainBorder() {
        return Math.max(func1.getLeftDomainBorder(),
                func2.getLeftDomainBorder());
    }

    @Override
    public double getRightDomainBorder() {
        return Math.min(func1.getRightDomainBorder(),
                func2.getRightDomainBorder());
    }

    @Override
    public double getFunctionValue(double x) {
        return func1.getFunctionValue(x) * func2.getFunctionValue(x);
    }
}
