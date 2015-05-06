import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.*;

public class sharePicSender extends JFrame implements Runnable {

	public final int SOCKET_PORT = 8888; // port number
	//public File fileToSend = new File("/Users/Bill/source.jpg"); // file to transfer
	File fileToSend;
	boolean startFlag = true;
	JButton startListeningButton;
	JButton chooseFileButton;
	JLabel infoLabel;
	ServerSocket servsock;

	sharePicSender() {
		setTitle("SendPic"); // set title
		int frameWidth = 400, frameHeight = 300;
		Dimension buttonDimension = new Dimension(frameWidth ,
				frameHeight / 2);
		setSize(frameWidth, frameHeight);
		setLayout(new BorderLayout());
		// create two buttons and a info label
		startListeningButton = new JButton("Start Listening");
		startListeningButton.addActionListener(new startListeningListener());
		chooseFileButton = new JButton("Choose File");
		chooseFileButton.addActionListener(new chooseFileListener());
		// set info label's alignment, font and color
		infoLabel = new JLabel("HelloWorld", SwingConstants.CENTER);
		infoLabel.setFont(new Font("Serif", Font.PLAIN, 30));
		infoLabel.setForeground(Color.BLUE);
		// set the size of button/lable
		startListeningButton.setPreferredSize(buttonDimension);
		chooseFileButton.setPreferredSize(buttonDimension);
		infoLabel.setPreferredSize(new Dimension(frameWidth, frameHeight / 2));
		// add them to frame
		add(startListeningButton, BorderLayout.WEST);
		//add(chooseFileButton, BorderLayout.EAST);
		add(infoLabel, BorderLayout.SOUTH);
	}

	// create GUI
	private static void createAndShowGui() {
		sharePicSender frame = new sharePicSender();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	class startListeningListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// start socket listening
			startFlag = true;
			(new Thread(new sharePicSender())).start();
			infoLabel.setText("Start Listening at port" + SOCKET_PORT);
		}
	}

	class chooseFileListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(sharePicSender.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					fileToSend = fc.getSelectedFile();
					infoLabel.setText("Opening: " + fileToSend.getName() + ".");
				}
		}
	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGui();
			}
		});
	}
	synchronized void sendPic() throws IOException {
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(sharePicSender.this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			fileToSend = fc.getSelectedFile();
			infoLabel.setText("Opening: " + fileToSend.getName() + ".");
		}
		ServerSocket servsock = new ServerSocket(SOCKET_PORT);
		if (startFlag) {
			Socket server = servsock.accept();
			
		    BufferedImage bimg=ImageIO.read(fileToSend);
		    OutputStream os = server.getOutputStream();
			ImageIO.write(bimg,"jpg",os);
			os.close();
			servsock.close();
			//System.exit(0);
		}
	}
	
	public void run() {
		try {
			sendPic();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}