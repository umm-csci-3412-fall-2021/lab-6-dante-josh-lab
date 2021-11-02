package echoserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {

    // REPLACE WITH PORT PROVIDED BY THE INSTRUCTOR
    public static final int PORT_NUMBER = 6013;
    public static void main(String[] args) throws IOException, InterruptedException {
        EchoServer server = new EchoServer();
        server.start();
    }

    private void start() throws IOException, InterruptedException {
        ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
        while (true) {
            Socket socket = serverSocket.accept();

            InputStream socketInputStream = socket.getInputStream();
            OutputStream socketOutputStream = socket.getOutputStream();

	    // Create instance of class that will act as thread code
            RetransmitData clientInput = new RetransmitData(socket, socketInputStream, socketOutputStream);
	    // Create thread
            Thread clientInputThread = new Thread(clientInput);

	    // Singe Thread Example
            clientInputThread.start();
        }
    }

    public class RetransmitData implements Runnable {
        Socket socket;
        InputStream socketInputStream;
        OutputStream socketOutputStream;

        public RetransmitData(Socket socket, InputStream socketInputStream, OutputStream socketOutputStream) {
            this.socket = socket;
            this.socketInputStream = socketInputStream;
            this.socketOutputStream = socketOutputStream;
        }

        @Override
        public void run() {
            try {
                byte[] buffer = new byte[4096];
                int bytesRead;

                //Read data sent by client socket
                while ((bytesRead = socketInputStream.read(buffer)) != -1) {
                    // Write data back to client
                    socketOutputStream.write(buffer, 0 , bytesRead);
                    socketOutputStream.flush();
                }
                
		// Close Socket Connection
                socket.close();
            }  catch(IOException ioe) {
                System.out.println("We caught an unexpected exception");
            }
        }
    }
}
