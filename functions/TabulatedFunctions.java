package functions;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class TabulatedFunctions {
    private static TabulatedFunctionFactory factory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory tabulatedFunctionFactory){
        TabulatedFunctions.factory = tabulatedFunctionFactory;
    }
    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points){
        return factory.createTabulatedFunction(points);
    }
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount){
        return factory.createTabulatedFunction(leftX, rightX, pointsCount);
    }
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values){
        return factory.createTabulatedFunction(leftX, rightX, values);
    }
    public static TabulatedFunction createTabulatedFunction(Class tabulatedFunctionType, double leftX, double rightX, int pointsCount)
            throws IllegalArgumentException{
        if (tabulatedFunctionType != ArrayTabulatedFunction.class &&
                tabulatedFunctionType != LinkedListTabulatedFunction.class)
            throw new IllegalArgumentException();
        try{
            Constructor<TabulatedFunction> constructor = tabulatedFunctionType.getDeclaredConstructor(double.class,
                                                                                            double.class, int.class);
            return constructor.newInstance(leftX, rightX, pointsCount);
        } catch (Exception e){
            throw new IllegalArgumentException(e);
        }
    }
    public static TabulatedFunction createTabulatedFunction(Class tabulatedFunctionType, double leftX, double rightX, double[] values)
            throws IllegalArgumentException {
        if (tabulatedFunctionType != ArrayTabulatedFunction.class &&
                tabulatedFunctionType != LinkedListTabulatedFunction.class)
            throw new IllegalArgumentException();
        try{
            Constructor<TabulatedFunction> constructor = tabulatedFunctionType.getDeclaredConstructor(double.class,
                    double.class, double[].class);
            return constructor.newInstance(leftX, rightX, values);
        }catch (Exception e){
            throw new IllegalArgumentException(e);
        }
    }
    public static TabulatedFunction createTabulatedFunction(Class tabulatedFunctionType, FunctionPoint[] points)
            throws IllegalArgumentException {
        if (tabulatedFunctionType != ArrayTabulatedFunction.class &&
                tabulatedFunctionType != LinkedListTabulatedFunction.class)
            throw new IllegalArgumentException();
        try {
            Constructor<TabulatedFunction> constructor = tabulatedFunctionType.getDeclaredConstructor(FunctionPoint[].class);
            return constructor.newInstance((Object) points);
        }catch (Exception e){
            throw new IllegalArgumentException(e);
        }
    }
    public static TabulatedFunction tabulate(Class type, Function function, double leftX, double rightX, int pointsCount) throws IllegalArgumentException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder())
            throw new IllegalArgumentException();
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX)/ (pointsCount - 1);
        for (int i = 0; i < pointsCount; ++i)
            points[i] = new FunctionPoint(leftX + i*step, function.getFunctionValue(leftX + i*step));
        return createTabulatedFunction(type, points);
    }
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) throws IllegalArgumentException{
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder())
            throw new IllegalArgumentException();
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX)/ (pointsCount - 1);
        for (int i = 0; i < pointsCount; ++i)
            points[i] = new FunctionPoint(leftX + i*step, function.getFunctionValue(leftX + i*step));
        return factory.createTabulatedFunction(points);
    }
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(out);
        int pointsCount = function.getPointsCount();
        dataOut.writeInt(pointsCount);
        for(int i = 0; i < pointsCount; ++i){
            dataOut.writeDouble(function.getPointX(i));
            dataOut.writeDouble(function.getPointY(i));
        }
        dataOut.flush();
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dataInput = new DataInputStream(in);

        int pointCount =  dataInput.readInt();
        FunctionPoint[] points = new FunctionPoint[pointCount];
        for (int i = 0; i < pointCount; ++i){
            double x = dataInput.readDouble();
            double y = dataInput.readDouble();
            points[i] = new FunctionPoint(x, y);
        }
        dataInput.close();
        return factory.createTabulatedFunction(points);
    }
    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        int pointCount = function.getPointsCount();
        out.write(pointCount + " ");
        for(int i = 0; i < pointCount; ++i){
            out.write(function.getPointX(i) + " " + function.getPointY(i) + " ");
        }
    }
    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(in);
        tokenizer.nextToken();
        FunctionPoint[] points = new FunctionPoint[(int)tokenizer.nval+1];
        for(int i = 0; i < points.length; ++i){
            tokenizer.nextToken();
            double x = tokenizer.nval;
            tokenizer.nextToken();
            points[i] = new FunctionPoint(x, tokenizer.nval);
        }
        return factory.createTabulatedFunction(points);
    }

}
