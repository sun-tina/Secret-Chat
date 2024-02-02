import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//server will listen to clients to connect & create new thread
public class Server extends javax.swing.JFrame{

    private static ServerSocket serverSocket;

    public Server(ServerSocket serverSocket){
        Server.serverSocket = serverSocket;
    }
    public Server() {
        initComponents();
    }
    @SuppressWarnings("unchecked")                          
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        messageArea = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jTextField1.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        messageArea.setColumns(20);
        messageArea.setRows(5);
        //read only
        messageArea.setEditable(false);
        jScrollPane2.setViewportView(messageArea);

        jLabel1.setText("Server");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 606, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(89, Short.MAX_VALUE))
        );

        pack();
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
                messageArea.setText(messageArea.getText() + "\nnew client connected");
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
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run(){
                new Server().setVisible(true);
            }
        });
        //server to listen on port number
        ServerSocket serverSocket = new ServerSocket(5000);
        Server server = new Server(serverSocket);
        //run server keep it constantly running
        server.startServer();

    }
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private static javax.swing.JTextArea messageArea;
}