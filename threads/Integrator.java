package threads;

import functions.Functions;

import java.util.concurrent.Semaphore;

public class Integrator extends Thread{
    private Task task;
    private Sem sem;

    public Integrator(Task task, Sem sem){
        this.task = task;
        this.sem = sem;
    }


    @Override
    public void run() {
        try {
            for (int i = 0; i < task.taskAmount && !this.isInterrupted(); ++i) {
                double result;

                this.sem.beginRead();
                result = Functions.integrate(this.task.functionToIntegrate, this.task.leftX,
                                            this.task.rightX, this.task.deltaX);

                System.out.println("[" + i + "]Source " + this.task.leftX + " " +
                        this.task.rightX + " " + this.task.deltaX + "  integrator " + result);


                this.sem.endRead();
            }
        } catch (InterruptedException e) {
            this.sem.endRead();
        }finally {
            System.out.println("Thread Integrator was interrupted");
        }
    }
}
