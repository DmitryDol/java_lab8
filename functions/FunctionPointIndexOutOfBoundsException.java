package functions;

/**
 * Out of bounds in points array when accessing them by index
 */
public class FunctionPointIndexOutOfBoundsException extends IndexOutOfBoundsException{
    public FunctionPointIndexOutOfBoundsException(){
        super("Out of bounds in points array when accessing them by index");
    }
}
