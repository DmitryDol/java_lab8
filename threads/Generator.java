package threads;

import functions.basic.Log;


public class Generator extends Thread {
    private Task task;
    private Sem sem;
    public Generator(Task task, Sem sem){
        this.task = task;
        this.sem = sem;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < task.taskAmount && !this.isInterrupted(); ++i){

                this.sem.beginWrite();
                this.task.functionToIntegrate = new Log(1 + Math.random() * 9);
                this.task.leftX = Math.random() * 100;
                this.task.rightX = 100 + Math.random() * 100;
                this.task.deltaX = Math.random();

                System.out.println("[" + i + "]Source " + this.task.leftX + " " +
                        this.task.rightX + " " + this.task.deltaX + "  generator");


                this.sem.endWrite();
            }
        } catch (InterruptedException e) {
            this.sem.endWrite();
        }finally {
            System.out.println("Thread Generator was interrupted");
        }
    }

}
