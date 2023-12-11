package threads;

import functions.Function;
import functions.Functions;
import functions.basic.Log;

public class SimpleGenerator implements Runnable{
    private final Task task;
    public SimpleGenerator(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        for (int i = 0; i < task.taskAmount; ++i){
            synchronized (task){
                task.functionToIntegrate = new Log(1 + Math.random() * 9);
                task.leftX = Math.random() * 100;
                task.rightX = 100 + Math.random() * 100;
                task.deltaX = Math.random();

                System.out.println("Source " + task.leftX + " " +
                        task.rightX + " " + task.deltaX + "  generator");
                //Thread.sleep(100);
            }
        }
    }

}
