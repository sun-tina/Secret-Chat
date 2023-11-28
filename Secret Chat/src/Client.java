import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


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
        // pass
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
        // Gui gui = new Gui();
        // gui.setVisible(true);
        // //send button action
        // gui.setSendActionListener(e -> {
        //     String message = gui.getMessage();
        //     if(!message.isEmpty()){
        //         gui.clearMessage();
        //     }
        // });
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Client().setVisible(true);
            }
        });
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter username: ");
        String username = scanner.nextLine();
        // ip address(local host) & port server is listening on
        Socket socket = new Socket("127.0.0.1", 5000);
        Client client = new Client(socket, username);
        //separate threads for listen and send 
        //allows app to run at the same time so that it does not halt when waiting to listen/send
        client.listenMessage();
        client.sendMessage();


    }
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea readMessageArea;
    private javax.swing.JButton send;
    private javax.swing.JLabel title;
    private javax.swing.JTextArea typeMessageArea;
}
    

