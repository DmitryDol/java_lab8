package threads;

import functions.Function;

import java.util.concurrent.Semaphore;

public class Task {
    public Function functionToIntegrate;
    public double leftX;
    public double rightX;
    public double deltaX;
    public int taskAmount;
}
