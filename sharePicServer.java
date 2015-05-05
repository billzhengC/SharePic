import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.*;

public class sharePicServer extends JFrame implements Runnable {

	public final int SOCKET_PORT = 8888; // port number
	public File fileToSend; // file to transfer
	boolean startFlag = false;
	JButton startListeningButton;
	JButton chooseFileButton;
	JLabel infoLabel;
	ServerSocket servsock;

	sharePicServer() {
		setTitle("SendPic"); // set title
		int frameWidth = 400, frameHeight = 300;
		Dimension buttonDimension = new Dimension(frameWidth / 2,
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
		add(chooseFileButton, BorderLayout.EAST);
		add(infoLabel, BorderLayout.SOUTH);
	}

	// create GUI
	private static void createAndShowGui() {
		sharePicServer frame = new sharePicServer();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	class startListeningListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// start socket listening
			(new Thread(new sharePicServer())).start();
			infoLabel.setText("Start Listening at port" + SOCKET_PORT);
		}
	}

	class chooseFileListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(sharePicServer.this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				fileToSend = fc.getSelectedFile();
				// This is where a real application would open the file.
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

	public void run() {
		try {
			servsock = new ServerSocket(SOCKET_PORT);
			while (true) {
				FileInputStream fis = null;
				BufferedInputStream bis = null;
				OutputStream os = null;
				Socket sock = null;

				int read = 0;
				int numRead = 0;
				while (startFlag) {
					infoLabel.setText("Waiting..");
					try {
						sock = servsock.accept();
						infoLabel.setText("Connection accepted: " + sock);
						// send file
						int fileLength = (int) fileToSend.length();
						byte[] byteToSendArray = new byte[fileLength];
						fis = new FileInputStream(fileToSend);
						bis = new BufferedInputStream(fis);
						while (read < fileLength
								&& (numRead = bis.read(byteToSendArray, read,
										fileLength - numRead)) >= 0)
							read += numRead;
						os = sock.getOutputStream();
						os.write(byteToSendArray, 0, fileLength);
						os.flush();
						infoLabel.setText(fileToSend + "("
										+ byteToSendArray.length + " bytes)"
										+ " Done.");
					} finally {
						if (bis != null)
							bis.close();
						if (os != null)
							os.close();
						if (sock != null)
							sock.close();
					}
				}
			}
		} catch (IOException e){
			e.printStackTrace();
		}
		finally {
			if (servsock != null)
				try {
					servsock.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
		}
	}

}