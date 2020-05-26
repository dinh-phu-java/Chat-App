import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ServerMain {
    public static final int SERVER_PORT=9000;
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket =new ServerSocket(SERVER_PORT);
            while(true){
                System.out.println("Wating for connection!");
                Socket socket= serverSocket.accept();
                System.out.println("Access Success full with: "+socket.getRemoteSocketAddress());
                ServerWorker worker = new ServerWorker(socket);
                worker.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

