import java.io.*;
import java.net.*;

public class ClientHandler extends Thread {
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;
    String messageRecieved;
    String toReturn;

    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos){
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run(){
        while(true){
            try{

                messageRecieved = dis.readUTF();
                for(int i = 0; i < Server.list.size(); i++){
                    if(Server.list.get(i) == this){
                        continue; // 
                    }
                    Server.list.get(i).dos.writeUTF(messageRecieved);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void close() {
        try{
            if(dis != null) dis.close();
            if(dos != null) dos.close();
            if(s != null && !s.isClosed()) s.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
}
