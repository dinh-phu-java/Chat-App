import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.JsonToStringStyleTest;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

public class ServerWorker extends Thread{

    private  Socket clientSocket;
    private Server server;
    private OutputStream outputStream;
    private InputStream inputStream;
    private BufferedReader reader;
    private String clientSession;

    public ServerWorker(Server server,Socket clientSocket){
        this.server=server;
        this.clientSocket=clientSocket;
    }
    public String getClientSession(){
        return this.clientSession;
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
         this.inputStream = clientSocket.getInputStream();
         this.reader= new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
         this.outputStream=clientSocket.getOutputStream();
        String line;
        while( (line=reader.readLine())!=null ){
            String[] tokens= StringUtils.split(line);
            if(tokens != null && tokens.length>0){
                String cmd= tokens[0];
                System.out.println(line);
                outputStream.write((line+"\n\r").getBytes());
                if("quit".equalsIgnoreCase(cmd)){
                    handleLogoff();
                    break;
                }else if("msg".equalsIgnoreCase(cmd)){
                    String[] tokensMsg= StringUtils.split(line,null,3);
                    handleMessage(tokensMsg);
                }
                else if("login".equalsIgnoreCase(cmd)){
                    handleLogin(tokens);
                }
                else if("sendall".equalsIgnoreCase(cmd)){
                    ArrayList<ServerWorker> workers = this.server.getWorkerList();
                    for (ServerWorker worker : workers){
                        worker.outputStream.write((line+"\n\r").getBytes());
                    }
                }else{
                    this.outputStream.write("Please Login\n\r".getBytes());
                }
            }
        }
        clientSocket.close();
    }

    public void handleMessage(String[] tokensMsg) throws IOException {
        if(tokensMsg.length>2 && tokensMsg!=null){
            String sendTo=tokensMsg[1];
            String bodyMsg=tokensMsg[2];

            ArrayList<ServerWorker> workers= this.server.getWorkerList();
            for (ServerWorker worker : workers){
                if(worker.getClientSession()!=null && this.clientSession!=null){
                    if(sendTo.equals(worker.getClientSession())){
                        worker.outputStream.write((bodyMsg+"\n\r").getBytes());
                    }
                }
            }
        }
    }

    public void handleLogoff() throws IOException {
        ArrayList<ServerWorker> workers = this.server.getWorkerList();
        workers.remove(this);
        System.out.println("Client Number : "+workers.size());

        String offline;
        for (ServerWorker worker : workers){
            offline="offline "+this.getClientSession()+"\n\r";
            worker.outputStream.write(offline.getBytes());
        }
    }

    public void handleLogin(String[] tokens) throws IOException {
        if(tokens.length == 3 && tokens!=null){
            String login= tokens[1];
            String password = tokens[2];

            if(("jim".equals(login) && "jim".equals(password)) || ("guest".equals(login) && "guest".equals(password)) || ("phu".equals(login) && "phu".equals(password))){
                this.clientSession=login;
                this.outputStream.write(("Login successful: "+login+"\n\r").getBytes());
                ArrayList<ServerWorker> workers= this.server.getWorkerList();

                String online="online "+login+"\n\r";
                for (ServerWorker worker : workers){
                    if(worker.clientSession!=null){
                       if(this.clientSession != worker.getClientSession()){
                           worker.outputStream.write(online.getBytes());
                       }
                    }
                }

                String onlineAll;
                for (ServerWorker worker : workers){
                    if(worker.clientSession!=null){
                        if(this.clientSession != worker.getClientSession()){
                            onlineAll="online "+worker.getClientSession()+"\n\r";
                            this.outputStream.write(onlineAll.getBytes());
                        }
                    }
                }

            }else{
                this.outputStream.write("Invalid user and password\n\r".getBytes());
            }

        }else{
            System.out.println("Login command is incorrect!");
        }
    }
}
