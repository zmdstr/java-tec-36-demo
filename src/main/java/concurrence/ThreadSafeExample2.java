package concurrence;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: zhenyang
 * @Date: 2019/3/2 上午8:59
 * @Description
 */

public class ThreadSafeExample2 {
    public int sharedState;
    /**
     * 注意 reentrantLock 定义位置，在方法内不生效
     */
    private ReentrantLock lock = new ReentrantLock();

    public void unsafeAction() {
        while (sharedState < 100000) {
            int former;
            int later;
            lock.lock();
            try {
                former = sharedState++;
                later = sharedState;
            } finally {
                lock.unlock();
            }

            if (former != later - 1) {
                System.out.printf(
                        "Observed data race, former is " +
                                former + ",latter is " + later);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadSafeExample2 example = new ThreadSafeExample2();
        Thread threadA = new Thread() {
            @Override
            public void run() {
                example.unsafeAction();
            }
        };
        Thread threadB = new Thread() {
            @Override
            public void run() {
                example.unsafeAction();
            }
        };

        threadA.start();
        threadB.start();
        // 当前线程等待子线程终止
        threadA.join();
        threadB.join();
    }
}