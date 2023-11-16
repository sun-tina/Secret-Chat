import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

//implements runnable interface & instances excecute via separate thread
public class ClientHandler implements Runnable{

    //using array to keep track of clients to loop thru and send message to each
    //want array to belong to class not each object of class - static 
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    //buffered to read and write data/messages 
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUserName;

    
    public ClientHandler(Socket socket){
        try{
            this.socket = socket;
            //socket output stream - using character stream wrapped around byte stream to send words/characters/string
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()) );
            this.clientUserName = bufferedReader.readLine();
            //passes client username to arraylist of users
            clientHandlers.add(this);

            
        }catch(){

        }
    }


    @Override
    public void run() {
        
    }
    
}
