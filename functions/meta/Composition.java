package functions.meta;

import functions.Function;

public class Composition implements Function {
    Function func1;
    Function func2;
    public Composition(Function func1, Function func2){
        this.func1 = func1;
        this.func2 = func2;
    }
    @Override
    public double getLeftDomainBorder() {
        return func2.getLeftDomainBorder();
    }

    @Override
    public double getRightDomainBorder() {
        return func2.getRightDomainBorder();
    }

    @Override
    public double getFunctionValue(double x) {
        return func2.getFunctionValue(func1.getFunctionValue(x));
    }
}
