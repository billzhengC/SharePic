import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;


public class sharePicReceiver extends JFrame implements Runnable {

	public final static int SOCKET_PORT = 8888; // port number
	public final static String SERVER = "127.0.0.1"; // localhost
	public final static File FILE_TO_RECEIVED = new File(
			"/Users/Bill/source-downloaded.jpg");
	BufferedImage imgReceived;
	boolean ReceivedFlag = false;
	JScrollPane picPane;

	public final static int FILE_SIZE = 6022386;

	private static void createAndShowGui() {
		sharePicReceiver frame = new sharePicReceiver();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public sharePicReceiver() {
		setTitle("ReceivePic"); // set title
		int frameWidth = 300, frameHeight = 500;
		setSize(frameWidth, frameHeight);
		setLayout(new BorderLayout());
		picPane = new JScrollPane();
		picPane.setPreferredSize(new Dimension(frameWidth, frameHeight));
		picPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add(picPane);
		setVisible(true);

	}

	public static void main(String[] args) throws IOException {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGui();
			}
		});
		(new Thread(new sharePicReceiver())).start();
	}

	void displayImgInPane(BufferedImage img) {
		ImageIcon imgIcon = new ImageIcon(img);
		picPane.setViewportView(new JLabel(imgIcon));
		System.out.println("Display img");
	}

	int acceptFileOption(File file, int fileSize) {
		String fileMessage = "Do you want to receive" + file + "?"; 
		int n = JOptionPane.showOptionDialog( // get user's choice of exit or
												// restart
				this, // use this JPanel
				fileMessage, // set message
				"New File Transfer", // set title
				JOptionPane.YES_NO_OPTION, // set it to yes/no option
				JOptionPane.PLAIN_MESSAGE, // use plain message
				null, // no icon
				null, // use default button msg
				null); // no default choice
		return n;
	}

	void tryReceiveFile() throws IOException {
		int bytesRead;
		int current = 0;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		Socket sock = null;
		try {
			sock = new Socket(SERVER, SOCKET_PORT);
			System.out.println("Connecting...");
			BufferedImage img= ImageIO.read(ImageIO.createImageInputStream(sock.getInputStream()));
			// receive file
			int n = acceptFileOption(FILE_TO_RECEIVED,current);
			boolean displayFlag = true;
			switch (n) {
			case JOptionPane.YES_OPTION:
				displayFlag = true;
				break;
			}
			if (displayFlag) {
//				BufferedImage img=ImageIO.read(ImageIO.createImageInputStream(is));
				//imgReceived = ImageIO.read(FILE_TO_RECEIVED);
				displayImgInPane(img);
				ReceivedFlag = true;
			}
		} finally {
			if (fos != null)
				fos.close();
			if (bos != null)
				bos.close();
			if (sock != null)
				sock.close();
		}
	}

	@Override
	public void run() {
		if (!ReceivedFlag) {
			try {
				tryReceiveFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}