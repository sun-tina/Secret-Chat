import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//server will listen to clients to connect & create new thread
public class Server{

    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void startServer(){
        try{
            //loop to keep server running while socket open -> stops when closed
            while(!serverSocket.isClosed()){
                //halts here until client connects and socket object is returned
                Socket socket = serverSocket.accept();
                System.out.println("new client connected");
                //handles client communication
                ClientHandler ClientHandler = new ClientHandler(socket);
                
                //to spawn new threads relevant to new clients
                Thread thread = new Thread(ClientHandler);
                thread.start();

            }
        }catch(IOException e){


        }
    }

    public void closeServerSocket(){
        try{
            if(serverSocket != null){
                serverSocket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }

   }

    //main
    public static void main (String[] args) throws IOException{
        //server to listen on port number
        ServerSocket serverSocket = new ServerSocket(5000);
        Server server = new Server(serverSocket);
        //run server / keep it constantly running
        server.startServer();



    }
}