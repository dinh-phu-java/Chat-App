import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread{
    private int serverPort;
    private ArrayList<ServerWorker> workerList=new ArrayList<>();

    public Server(int port){
        this.serverPort=port;
    }

    @Override
    public void run(){
        try {
            ServerSocket serverSocket =new ServerSocket(serverPort);
            while(true){
                System.out.println("Wating for connection!");
                Socket clientSocket= serverSocket.accept();
                System.out.println("Access Success full with: "+clientSocket.getRemoteSocketAddress());
                ServerWorker worker = new ServerWorker(this,clientSocket);
                this.workerList.add(worker);
                worker.start();
                System.out.println("Client Number: "+workerList.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ServerWorker> getWorkerList(){
        return this.workerList;
    }
}
