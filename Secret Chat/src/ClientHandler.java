import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

//implements runnable interface & instances excecute via separate thread
public class ClientHandler implements Runnable {

    // using arraylist to keep track of client instances 
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    // buffered to read and write data/messages
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUserName;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            // socket output stream - using character stream wrapped around byte stream to
            // send words/characters/string
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUserName = bufferedReader.readLine();
            // passes client username to arraylist of users
            clientHandlers.add(this);
            //notify all clients of new user 
            broadcastMessage(" SERVER: " + clientUserName + " has joined");

        } catch (IOException e) {
            closeAll(socket, bufferedReader, bufferedWriter);

        }
    }

    
    @Override
    // everything thats run on a separate thread
    public void run() {
        String clientMessage;
        // temp infinite while loop to keep reading and writing messages while running
        while (socket.isConnected()) {
            try {
                // running on separate thread so that rest of application doesnt stop while
                // waiting for data to be read
                clientMessage = bufferedReader.readLine();
                broadcastMessage(clientMessage);

            } catch (IOException e) {
                // broadcastMessage("SERVER: " + clientUserName + " has left");
                removeClientHandler();
                closeAll(socket, bufferedReader, bufferedWriter);
                break;
            }
        }

    }

    public void broadcastMessage(String messagetoSend) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                clientHandler.bufferedWriter.write(messagetoSend);
                clientHandler.bufferedWriter.newLine();
                // manually flushing - prevents app from waiting until buffer is full
                clientHandler.bufferedWriter.flush();

            } catch (IOException e) {
                closeAll(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeClientHandler() {
        // removes current client from array list
        try {
            broadcastMessage("SERVER: " + clientUserName + " has left");
            clientHandlers.remove(this);
        } catch (Exception e) {
            // TODO: handle exception
        }
       
        
    }

    public void closeAll(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
        try {
            // check to see if null to avoid null pointer exception
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
