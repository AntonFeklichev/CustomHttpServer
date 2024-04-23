package customhttpserver;

import java.io.*;
import java.net.Socket;

public class RunnableClientSocket implements Runnable {
    private final Socket clientSocket;
    public RunnableClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    @Override
    public void run() {

        try {
            System.out.println("Получено соединение от " + clientSocket.getInetAddress());

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String line;
            StringBuilder requestBuilder = new StringBuilder();

            while ((line = in.readLine()) != null && !line.isEmpty()) {
                requestBuilder.append(line).append("\r\n");
            }

            String contentLengthHeader = "Content-Length: ";
            int contentLength = 0;
            String request = requestBuilder.toString();
            String body = "";
            if (request.contains(contentLengthHeader)) {
                int start = request.indexOf(contentLengthHeader) + contentLengthHeader.length();
                int end = request.indexOf("\r\n", start);
                contentLength = Integer.parseInt(request.substring(start, end).trim());
                char[] bodyFromReader = new char[contentLength];
                in.read(bodyFromReader, 0, contentLength);
                body = new String(bodyFromReader);
            }

            OutputStream out = clientSocket.getOutputStream();

            PrintWriter pout = new PrintWriter(out);

            pout.println("HTTP/1.1 200 OK");
            pout.println("Content-Type: text/plain");
            pout.println("Connection: close");
            pout.println("");
            pout.println("Tnx for request\r\n");
            pout.println(body);

            pout.flush();

            in.close();

            pout.close();

        } catch (IOException e) {
            System.out.println("RunnableClientSocket exception");
        }

    }
}
