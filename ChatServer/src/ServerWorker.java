import java.io.*;
import java.net.Socket;
import java.util.Date;

public class ServerWorker extends Thread{

    private final Socket socket;

    public ServerWorker(Socket socket){
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
        InputStream inputStream = socket.getInputStream();
        BufferedReader reader= new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
        OutputStream outputStream=socket.getOutputStream();
        OutputStreamWriter writer=new OutputStreamWriter(outputStream,"UTF-8");
        for (int i=0;i<10;i++){
            String a= "Time now is ê "+new Date()+"\n\r";
            writer.write(a);
            //outputStream.write((a).getBytes());
            Thread.sleep(1000);
        }
        socket.close();
    }
}
