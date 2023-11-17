import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.PrintWriter;
public class Gui extends JFrame{
   
    private final JTextArea chatArea;
    private final JTextField messageField;

    public Gui(){
        JFrame frame = new JFrame("secret chat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);

        chatArea = new JTextArea();
        chatArea.setEditable(false);

        messageField = new JTextField();

        JButton sendButton = new JButton("send");

        setLayout(new BorderLayout());
        add(new JScrollPane(chatArea), BorderLayout.CENTER);
        add(messageField, BorderLayout.CENTER);
        add(sendButton, BorderLayout.EAST);

        



    }
}