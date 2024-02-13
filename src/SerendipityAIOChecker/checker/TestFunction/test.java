package SerendipityAIOChecker.checker.TestFunction;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class test {
    public static void main(String[] args) {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        ExecutorService executor = Executors.newFixedThreadPool(600); // 假设线程池大小为2

        // 启动循环
        new Thread(() -> {
            while (true) {
                try {
                    String combo = queue.take();
                    executor.execute(() -> {
                            System.out.println("Processed: " + combo);
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();

        // 向队列添加元素
        for (int i = 1; i <= 1000; i++) {
            String element = "Element " + i;
            queue.add(element);
            System.out.println("Added to queue: " + element);
        }
    }
}
