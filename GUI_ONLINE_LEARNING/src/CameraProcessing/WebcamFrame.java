package CameraProcessing;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class WebcamFrame extends JFrame {
    private JPanel contentPane;
    private JLabel imageLabel;
    private boolean running;
    private static boolean isConnected;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                WebcamFrame frame = new WebcamFrame();
                frame.setVisible(true);
                if(isConnected){
                    System.out.println("Connected");
                }
                else{
                    System.out.println("Not connected");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public WebcamFrame() {
        isConnected = false;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        imageLabel = new JLabel("");
        contentPane.add(imageLabel, BorderLayout.CENTER);

        // Start a thread to listen for images from the socket
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(8485)) {
                Socket socket = serverSocket.accept();
                isConnected = true;
                System.out.println("Client connected");
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

                while (true) {
                    // Read the size of the image data
                    long len = dataInputStream.readLong();
                    byte[] data = new byte[(int) len];
                    dataInputStream.readFully(data);

                    // Convert the data to an image
                    InputStream in = new ByteArrayInputStream(data);
                    BufferedImage image = ImageIO.read(in);

                    // Update the label with the new image
                    SwingUtilities.invokeLater(() -> imageLabel.setIcon(new ImageIcon(image)));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    public boolean isConnected() {
        return isConnected;
    }

}