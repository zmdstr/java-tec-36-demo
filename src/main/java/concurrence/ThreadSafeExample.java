package concurrence;


/**
 * @Author: zhenyang
 * @Date: 2019/3/2 上午8:59
 * @Description 第15讲 | synchronized和ReentrantLock有什么区别呢？例子
 */

public class ThreadSafeExample {
    /**
     * 共享、可修改状态
     */
    public int sharedState;

    public void unsafeAction() {
        while (sharedState < 100000) {
            // 1.线程不安全
            int former = sharedState++;
            int later = sharedState;
            if (former != later - 1) {
                System.out.printf(
                        "Observed data race, former is " +
                                former + ",latter is " + later);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadSafeExample example = new ThreadSafeExample();
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