import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ServerMain {
    public static final int SERVER_PORT=9000;
    public static void main(String[] args) {
        Server server=new Server(SERVER_PORT);
        server.start();

    }

}

