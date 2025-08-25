
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.swing.*;
import java.net.*;

public class appPanel extends JPanel implements ActionListener, KeyListener {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    public static boolean running;
    int counter = 0;
    JTextField textField;
    JButton send;
    JPanel input;
    JScrollPane messagesPane;
    JPanel panelInPanel;
    BorderLayout borderlayout = new BorderLayout();
    InetAddress ip;
    Socket s;
    DataInputStream dis;
    DataOutputStream dos;

    appPanel() {
        running = true;
        this.setLayout(borderlayout);
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setFocusable(true);
        Font font = new Font("Comic Sans", Font.PLAIN, 30);
        input = new JPanel();
        input.setLayout(new FlowLayout());

        textField = new JTextField();
        textField.setPreferredSize(new Dimension(400, 100));
        textField.setFont(font);
        textField.setFocusable(true);
        textField.addKeyListener(this);
        input.add(textField);

        send = new JButton("Send!");
        send.setPreferredSize(new Dimension(100, 100));
        send.addActionListener(this);
        input.add(send);

        this.add(input, BorderLayout.SOUTH);

        panelInPanel = new JPanel();
        panelInPanel.setLayout(new BoxLayout(panelInPanel, BoxLayout.PAGE_AXIS));
        messagesPane = new JScrollPane(panelInPanel);
        this.add(messagesPane, BorderLayout.CENTER);

        try {
            ip = InetAddress.getByName("localhost");
            s = new Socket(ip, Server.PORT);

            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());

            new Thread(() -> {
                while (running) {
                    try {
                        String msg = dis.readUTF();
                        SwingUtilities.invokeLater(() -> recieveMessage(msg));
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == send) {
            sendMessage();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Unused
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            sendMessage();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Unused
    }

    public void sendMessage() {
        Font font2 = new Font("Comic Sans", Font.PLAIN, 20);
        Font font3 = new Font("Comic Sans", Font.PLAIN, 10);
        counter++;
        JLabel message = new JLabel();
        JLabel sentTime = new JLabel("Sent: " + LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        JPanel messageNSentTime = new JPanel();
        JPanel bubble = new JPanel();
        bubble.setLayout(new FlowLayout(FlowLayout.RIGHT));
        messageNSentTime.setLayout(new BoxLayout(messageNSentTime, BoxLayout.PAGE_AXIS));
        messageNSentTime.setAlignmentX(Component.RIGHT_ALIGNMENT);
        messageNSentTime.add(message);
        messageNSentTime.add(sentTime);
        sentTime.setFont(font3);
        message.setFont(font2);
        message.setText(textField.getText());
        System.out.println(message.getText());
        messageNSentTime.setBorder(BorderFactory.createLineBorder(Color.blue, 2));
        bubble.add(messageNSentTime);
        panelInPanel.add(bubble);
        textField.setText("");
        panelInPanel.revalidate();
        panelInPanel.repaint();
        try {
            String tosend = message.getText();
            dos.writeUTF(tosend);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void recieveMessage(String messageRecieved) {
        Font font2 = new Font("Comic Sans", Font.PLAIN, 20);
        Font font3 = new Font("Comic Sans", Font.PLAIN, 10);
        counter++;
        JLabel message = new JLabel();
        JLabel sentTime = new JLabel("Sent: " + LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        sentTime.setFont(font3);
        message.setFont(font2);
        try {
            message.setText(messageRecieved);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JPanel messageNSentTime = new JPanel();
        JPanel bubble = new JPanel();
        bubble.setLayout(new FlowLayout(FlowLayout.LEFT));
        messageNSentTime.setLayout(new BoxLayout(messageNSentTime, BoxLayout.PAGE_AXIS));
        messageNSentTime.setAlignmentX(Component.LEFT_ALIGNMENT);
        messageNSentTime.add(message);
        messageNSentTime.add(sentTime);
        messageNSentTime.setBorder(BorderFactory.createLineBorder(Color.green, 2));
        bubble.add(messageNSentTime);
        panelInPanel.add(bubble);
        panelInPanel.revalidate();
        panelInPanel.repaint();
    }
}
