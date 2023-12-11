package functions;

public interface Function {
    /**
     * @return left domain border of tabulated function
     */
    public double getLeftDomainBorder();

    /**
     * @return right domain border of tabulated function
     */
    public double getRightDomainBorder();

    /**
     * find value of function in point x
     * @param x must be in domain
     * @return value of f(x)
     */
    public double getFunctionValue(double x);
}
