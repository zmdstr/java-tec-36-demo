package concurrence;

/**
 * @Author: zhenyang
 * @Date: 2019/8/18 3:55 PM
 * @Description
 */

public class ThreadTest {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("hello world");
        String name = Thread.currentThread().getName();
        long id = Thread.currentThread().getId();
        System.out.println("name:" + name + " id:" + id);
        Thread.sleep(1000 * 60 * 60L);
    }
}