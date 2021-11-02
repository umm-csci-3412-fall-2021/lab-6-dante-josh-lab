package echoserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class EchoClient {
    public static final int PORT_NUMBER = 6013;

    public static void main(String[] args) throws IOException, InterruptedException {
        EchoClient client = new EchoClient();
        client.start();
    }

    private void start() throws IOException, InterruptedException {
        Socket socket = new Socket("localhost", PORT_NUMBER);
        InputStream socketInputStream = socket.getInputStream();
        OutputStream socketOutputStream = socket.getOutputStream();


	// Create instance of thread class
        UserInputToServer userInput = new UserInputToServer(socket, socketOutputStream);

	// Create instance of other thread class
        ReceiveInputFromServer serverOutput = new ReceiveInputFromServer(socket, socketInputStream);

	// Create threads
        Thread userInputThread = new Thread(userInput);
        Thread serverOutputThread = new Thread(serverOutput);

	// Start threads and then join them 
        userInputThread.start();
        serverOutputThread.start();
        userInputThread.join();
        serverOutputThread.join();
    }

    public class UserInputToServer implements Runnable {
        Socket socket;
        OutputStream socketOutputStream;


        public UserInputToServer(Socket socket, OutputStream socketOutputStream) {
            this.socket = socket;
            this.socketOutputStream = socketOutputStream;
        }

        @Override
        public void run() {
            try {
                byte[] buffer = new byte[4096];
                int bytesRead;

                // Read data in from user input
                while ((bytesRead = System.in.read(buffer)) != -1) {

                    // Send data to server
                    socketOutputStream.write(buffer, 0, bytesRead);
                    socketOutputStream.flush();
                }

		// Shut down output when we are finished
                socket.shutdownOutput();
            } catch (Exception e) {
                System.out.println(e);
            }

        }
    }

    public class ReceiveInputFromServer implements Runnable {
        Socket socket;
        InputStream socketInputStream;

        public ReceiveInputFromServer(Socket socket, InputStream socketInputStream) {
            this.socket = socket;
            this.socketInputStream = socketInputStream;
        }

        @Override
        public void run() {
            try {
                int bytesRead;

                // Read data from server response
                while ((bytesRead = socketInputStream.read()) != -1) {

		    // Write information received from server to standard output
                    System.out.write(bytesRead);
                    System.out.flush();
                }

		// Close socket connection
                socket.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

    }
}
