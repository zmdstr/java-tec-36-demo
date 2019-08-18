package concurrence;

/**
 * @Author: zhenyang
 * @Date: 2019/3/2 上午8:59
 * @Description
 */

public class ThreadSafeExample1 {
    public int sharedState;

    public void unsafeAction() {
        while (sharedState < 100000) {
            // synchronized 测试线程安全
            int former;
            int later;
            synchronized (this) {
                former = sharedState++;
                later = sharedState;
            }

            if (former != later - 1) {
                System.out.printf(
                        "Observed data race, former is " +
                                former + ",latter is " + later);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadSafeExample1 example = new ThreadSafeExample1();
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