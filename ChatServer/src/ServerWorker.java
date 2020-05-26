import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

public class ServerWorker extends Thread{

    private final Socket socket;
    private Server server;
    private OutputStream outputStream;
    private InputStream inputStream;
    private BufferedReader reader;

    public ServerWorker(Server server,Socket socket){
        this.server=server;
        this.socket=socket;
    }
    @Override
    public void run(){
        try {
            handleSocket();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void handleSocket() throws IOException, InterruptedException {
         this.inputStream = socket.getInputStream();
         this.reader= new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
         this.outputStream=socket.getOutputStream();
        String line;
        while( (line=reader.readLine())!=null ){
            String[] tokens= StringUtils.split(line);
            if(tokens != null && tokens.length>0){
                String cmd= tokens[0];
                System.out.println(line);
                outputStream.write((line+"\n\r").getBytes());
                if("quit".equalsIgnoreCase(cmd)){
                    break;
                }
                else{
                    ArrayList<ServerWorker> workers = this.server.getWorkerList();
                    for (ServerWorker worker : workers){
                        worker.outputStream.write((line+"\n\r").getBytes());
                    }
                }
            }

        }
        socket.close();
    }
}
