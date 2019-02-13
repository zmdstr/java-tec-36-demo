import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author: zhenyang
 * @Date: 2019/2/13 下午7:46
 * @Description
 */

public class NIOServer extends Thread {
    @Override
    public void run() {
        try (Selector selector = Selector.open();
             ServerSocketChannel serverSocket = ServerSocketChannel.open()) {
            serverSocket.bind(new InetSocketAddress(InetAddress.getLocalHost(), 9988));
            serverSocket.configureBlocking(false);
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    sayHelloWorld((ServerSocketChannel) key.channel());
                    iterator.remove();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sayHelloWorld(ServerSocketChannel server) {
        try (SocketChannel client = server.accept()) {
            client.write(Charset.defaultCharset().encode("hello world"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        NIOServer nioServer = new NIOServer();
        nioServer.start();

        try (Socket client = new Socket(InetAddress.getLocalHost(), 9988)) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            bufferedReader.lines().forEach(s -> System.out.println(s));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}