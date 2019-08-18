package concurrence;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author: zhenyang
 * @Date: 2019/8/18 4:53 PM
 * @Description
 */

public class DeadLockTool extends Thread {
    private String first;
    private String second;

    public DeadLockTool(String name, String first, String second) {
        super(name);
        this.first = first;
        this.second = second;
    }

    @Override
    public void run() {
        synchronized (first) {
            System.out.println(this.getName() + " obtained: " + first);
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (second) {
                System.out.println(this.getName() + " obtain: " + second);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadMXBean mbean = ManagementFactory.getThreadMXBean();
        Runnable dlCheck = new Runnable() {
            @Override
            public void run() {
                long[] threadIds = mbean.findDeadlockedThreads();
                if (threadIds != null) {
                    ThreadInfo[] threadInfos = mbean.getThreadInfo(threadIds);
                    System.out.println("Detected deadlock threads:");
                    for (ThreadInfo threadInfo : threadInfos) {
                        System.out.println(threadInfo.getThreadName());
                    }
                }
            }
        };
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(dlCheck,5L,10L,TimeUnit.SECONDS);
        String lockA = "lockA";
        String lockB = "lockB";
        DeadLockTool t1 = new DeadLockTool("Thread1", lockA, lockB);
        DeadLockTool t2 = new DeadLockTool("Thread2", lockB, lockA);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}