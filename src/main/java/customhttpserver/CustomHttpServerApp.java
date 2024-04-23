package customhttpserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomHttpServerApp {
    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        try (ServerSocket serverSocket = new ServerSocket(8080, 60)) {

            while (true) {

                Socket clientSocket = serverSocket.accept();

                Runnable runnableClientSocket = new RunnableClientSocket(clientSocket);

                executorService.execute(runnableClientSocket);

            }


        } catch (IOException e) {
            System.out.println("Port 8080 is unavailable");
        }


    }

}
