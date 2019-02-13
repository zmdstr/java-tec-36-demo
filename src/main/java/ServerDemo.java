import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: zhenyang
 * @Date: 2019/2/13 下午6:44
 * @Description
 */

public class ServerDemo extends Thread {
    private ServerSocket serverSocket;

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    @Override
    public void run() {
        try {
            // 0 表示自动绑定一个空闲端口
            serverSocket = new ServerSocket(0);
            ExecutorService executor = Executors.newFixedThreadPool(8);

            while (true) {
                // 阻塞等待客户端连接
                Socket socket = serverSocket.accept();
                RequestHandler requestHandler = new RequestHandler(socket);
                /**
                 * 当连接建立后，启动一个线程，负责回复客户端请求
                 * 每次创建一个线程，消耗内存，改为下面的使用线程池
                 */
                //requestHandler.start();
                /**
                 * 启用线程池
                 * 通过一个固定大小的线程池管理工作线程，避免频繁创建、销毁线程的开销
                 */
                executor.execute(requestHandler);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class RequestHandler extends Thread {
        private Socket socket;

        RequestHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (PrintWriter out = new PrintWriter(socket.getOutputStream())) {
                out.println("Hello world!");
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ServerDemo server = new ServerDemo();
        server.start();

        try (Socket client = new Socket(InetAddress.getLocalHost(), server.getPort())) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            bufferedReader.lines().forEach(s -> System.out.println(s));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
