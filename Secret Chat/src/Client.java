import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public Client(Socket socket, String username){
        try{
            this.socket = socket;
            this.username = username;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        }catch(IOException e){
            closeAll(socket, bufferedReader, bufferedWriter);
        }

    }
    public void sendMessage(){
        try{
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while(socket.isConnected()){
                String message = scanner.nextLine();
                bufferedWriter.write(username + ": " + message);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch(IOException e){
            closeAll(socket, bufferedReader, bufferedWriter);
        }
    }

    //waits for messages from broadcast and prints out for each thread
    public void listenMessage(){
        //new thread via runnable concurrency framework
        new Thread(new Runnable() {
            @Override
            //overriidedn and executed on separate thread
            public void run(){
                String groupMessage;

                while (socket.isConnected()){
                    try{
                        groupMessage = bufferedReader.readLine();
                        System.out.println(groupMessage);

                    }catch(IOException e){
                        closeAll(socket, bufferedReader, bufferedWriter);

                    }
                }

            }
            //to call start on the thread object
        }).start();
        }
    public void closeAll(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try{
            // check to see if null to avoid null pointer exception
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if (bufferedWriter!=null){
                bufferedWriter.close();
            }
            if (socket!= null){
                socket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws UnknownHostException, IOException{
        Gui gui = new Gui();
        gui.setVisible(true);
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter username: ");
        String username = scanner.nextLine();
        //connect to port server is listening on
        Socket socket = new Socket("localhost", 5000);
        Client client = new Client(socket, username);
        //separate threads for listen and send 
        //allows app to run at the same time so that it does not halt when waiting to listen/send
        client.listenMessage();
        client.sendMessage();


    }
}
    

