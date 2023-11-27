import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
public class Gui extends JFrame{
   
    private final JTextArea chatArea;
    private final JTextField messageField;

    public Gui(){
        JFrame frame = new JFrame("secret chat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);

        //read only
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        
        //type messages
        messageField = new JTextField();

        JButton sendButton = new JButton("send");

        //sets frame layout to a container
        setLayout(new BorderLayout());
        //scrollable chat in the center
        add(new JScrollPane(chatArea), BorderLayout.CENTER);
        add(messageField, BorderLayout.CENTER);
        add(sendButton, BorderLayout.EAST);


    }
    //adds message to chat area
    public void listMessage(String message){
        //swingutilities.invokelater for thread safety (ensures swing is executed sepcifically on EDT)
        SwingUtilities.invokeLater(() -> chatArea.append(message + "\n "));
    }

    public String getMessage(){
        //gets the entered message
        return messageField.getText();
    }

    public void clearMessage(){
        messageField.setText("");
    }
    
    //actionlistener for message field and send button
    public void setSendActionListener(ActionListener listener){
        //adds the actionlistener to the messagefield
        messageField.addActionListener(listener);
        //adds it to the send button
        ((JButton) getContentPane().getComponent(2)).addActionListener(listener);
    }
    //joptionpane swing framework to showmessagedialog
    public void showMessage(String message){
        JOptionPane.showMessageDialog(this, message); 

    }

    public static void main (String[] args){
        SwingUtilities.invokeLater(()->{
            Gui gui = new Gui();
            gui.setVisible(true);
        });
        
    }

}