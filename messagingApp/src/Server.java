import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server {
    static final int PORT = 4444;
    public static ArrayList<ClientHandler> list = new ArrayList<ClientHandler>();
    ServerSocket server;
    public Server() throws IOException{
        ServerSocket server = new ServerSocket(PORT);
        
        while(true){
            Socket s = null;

            try{
                s = server.accept();
                System.out.println("A new client has connected.");
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                System.out.println("Assigning new thread for this client");

                ClientHandler t = new ClientHandler(s, dis, dos);
                list.add(t);
                t.start();
            } catch(Exception e){
                s.close();
                e.printStackTrace();
            }
        }
        
    }

    public void close(){
        for(ClientHandler handler : list){
            handler.close();
        }
        if(server != null && !server.isClosed()){
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    
}
