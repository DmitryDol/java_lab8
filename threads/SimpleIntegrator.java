package threads;

import functions.Functions;

public class SimpleIntegrator implements Runnable{
    final Task task;
    public SimpleIntegrator(Task task){
        this.task = task;
    }

    @Override
    public void run() {
        for (int i  = 0; i < task.taskAmount; ++i){
            double result;
            synchronized (task){

                result = Functions.integrate(task.functionToIntegrate, task.leftX, task.rightX, task.deltaX);

                System.out.println("Source " + task.leftX + " " +
                      task.rightX + " " + task.deltaX + "  integrator " + result);
            }
        }
    }
}
