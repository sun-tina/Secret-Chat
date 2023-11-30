import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;


public class Client extends javax.swing.JFrame{
    static Socket socket;
    static BufferedReader bufferedReader;
    static BufferedWriter bufferedWriter;
    static String username;

    public Client() {
        initComponents();
    }
    
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        readMessageArea = new javax.swing.JTextArea();
        send = new javax.swing.JButton();
        title = new javax.swing.JLabel();
        typeMessageArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        

        readMessageArea.setColumns(20);
        readMessageArea.setRows(5);
        readMessageArea.setFont(new Font("Arial", Font.PLAIN, 17));
        readMessageArea.setEditable(false);
        jScrollPane1.setViewportView(readMessageArea);

        send.setText("send");
        send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendActionPerformed(evt);
            }
        });

        title.setText("Chat");

        typeMessageArea.setText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(typeMessageArea, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(send, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(title)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 609, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(title)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(send, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(typeMessageArea, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        pack();
    }                  
    

    public Client(Socket socket, String username){
        try{
            Client.socket = socket;
            Client.username = username;
            Client.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Client.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        }catch(IOException e){
            closeAll(socket, bufferedReader, bufferedWriter);
        }

    }

    private void sendActionPerformed(java.awt.event.ActionEvent evt) {                                     
            sendMessage();
    }

    public void sendMessage(){
        try{
            // bufferedWriter.write(username);
            // bufferedWriter.newLine();
            // bufferedWriter.flush();
            
            Scanner scanner = new Scanner(typeMessageArea.getText());
            if (scanner.hasNext()){
                while(socket.isConnected()){
                String message = scanner.nextLine();
                bufferedWriter.write(" "  + username + ": " + message);
                // System.out.println(username);
                bufferedWriter.newLine();
                bufferedWriter.flush();
                typeMessageArea.setText("");
            }};
        
            
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
                        addTimedMessage(readMessageArea, groupMessage, 50000); 
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

    public void addTimedMessage(JTextArea readMessageArea, String message, int delay){
        SwingUtilities.invokeLater(()->{
            readMessageArea.setText(readMessageArea.getText() + "\n" + message);

            Timer timer = new Timer(delay, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e){
                    removeMessage(readMessageArea, message);
                }
            });
            timer.setRepeats(false);
            timer.start();
        });
    }
    
    public void removeMessage(JTextArea readMessageArea, String message){
        SwingUtilities.invokeLater(()->{
            String currentText = readMessageArea.getText();
            int startIndex = currentText.indexOf(message);
            if(startIndex != -1){
                int endIndex = startIndex + message.length();
                readMessageArea.setText(currentText.substring(0, startIndex) + currentText.substring(endIndex));
            }
        });
        
    }

    public static void main(String[] args) throws UnknownHostException, IOException{
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Client().setVisible(true);
            }
        });
        String username =  JOptionPane.showInputDialog(readMessageArea, "Enter username");
       
        // readMessageArea.setText(readMessageArea.getText() + "[Current User] " + username);
        // ip address(local host) & port server is listening on
        if(username !=null)
       { Socket socket = new Socket("127.0.0.1", 5000);
        Client client = new Client(socket, username);
        //separate threads for listen and send 
        //allows app to run at the same time so that it does not halt when waiting to listen/send
        client.listenMessage();};
        
        bufferedWriter.write(username);
        bufferedWriter.newLine();
        bufferedWriter.flush();
        // client.sendMessage();


    }
    private javax.swing.JScrollPane jScrollPane1;
    private static javax.swing.JTextArea readMessageArea;
    private javax.swing.JButton send;
    private javax.swing.JLabel title;
    private static javax.swing.JTextArea typeMessageArea;
}