import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class sharePicClient extends JFrame{

  public final static int SOCKET_PORT = 8888;      
  public final static String SERVER = "127.0.0.1";  // localhost
  public final static String File_JPG = "/Users/Bill/source.jpg";  
  public final static int FILE_SIZE = 6022386; 
  public static void main (String [] args ) throws IOException {
	  	sharePicClient frame = new sharePicClient();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setSize(new Dimension(300,500));
		Socket sock = new Socket(SERVER, SOCKET_PORT);
//	    byte[] receivingbytearray = new byte[FILE_SIZE];
//	    InputStream is = sock.getInputStream();
//	    FileOutputStream fos = new FileOutputStream(FILE_TO_RECEIVED);
//	    BufferedOutputStream bos = new BufferedOutputStream(fos);
		BufferedImage img= ImageIO.read(ImageIO.createImageInputStream(sock.getInputStream()));
		frame.add(new JLabel(new ImageIcon(img)),BorderLayout.CENTER);
		frame.setVisible(true);
		sock.close();

	    
//	    int receivedBytes = is.read(receivingbytearray, 0, receivingbytearray.length);
//	    int current = receivedBytes;
//	    do {
//	    	receivedBytes = is.read(receivingbytearray, current, (receivingbytearray.length-current));
//	    	if(receivedBytes >= 0) current = current + receivedBytes;
//	    } while(receivedBytes > -1);
//	    bos.write(receivingbytearray, 0 , current);
//	    bos.flush();
//	    bos.close();
//	    sock.close();
  }

}