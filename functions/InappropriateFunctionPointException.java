package functions;

/**
 * Attempting to add or change a function point inappropriately
 */
public class InappropriateFunctionPointException extends Exception{
    public InappropriateFunctionPointException(){
        super("Attempting to add or change a function point inappropriately");
    }
}
