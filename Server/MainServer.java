import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.List;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.xml.bind.Marshaller.Listener;

public class MainServer {
	static JFrame frame = new JFrame("K3LLY Server side");
	static JTextArea TA = new JTextArea(4000, 3000);
	static JPanel panel1 = null;
	static int clientNumber = 0;
	static ServerSocket listener;
	static ArrayList<ServerThread> currentUsers = new ArrayList<ServerThread>();
	public static void main(String[] args) throws IOException {
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    Container c = frame.getContentPane();
	    c.setBackground(Color.BLACK);
	    frame.setSize(600, 300);
	    frame.add(TA);
	    TA.setEditable(true);
	    TA.setBackground(Color.BLACK);
	    TA.setForeground(Color.GREEN);
	    TA.setCaretColor(Color.GREEN);
	    c.add(new JScrollPane(TA));
	    frame.setVisible(true);
	    log("Server Running... \n");
	    
	    //variables for server///
		System.out.println("Server Running");

		listener = new ServerSocket(9898);
	    //Gui for server side commands
	    TA.addKeyListener((KeyListener) new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					//calls method for checking each line for command
					try {
						try {
							commandCheck();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
			}
	    });
	    //main listener for client connections
		try {
			//pushes user/well thread to a list to access from array.
			currentUsers.add(new ServerThread(listener.accept(), ++clientNumber));
			while(true) {
				new ServerThread(listener.accept(), ++clientNumber).start();
			
			}
		} finally {
			listener.close();
		}
	}
	//checks dynamic commands running threw GUI and prints them
	public static void log(String message) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                TA.append(message + "\n");
                if(message.contains("socket closed")) {
                	--clientNumber;
                	TA.append("Current Users connected: " + clientNumber + "\n");
                } 
            }});
	}
	//checks commands typed directly into GUI
	public static void commandCheck() throws BadLocationException, IOException {
		int lines = TA.getLineCount() -1;
		int start = TA.getLineStartOffset(lines);
		int end = TA.getLineEndOffset(lines);
		String endLine = TA.getText().substring(start, end);
		
		if(endLine.equals("exit") ) {
			System.exit(0);
		} else if(endLine.contains("close socket")) {
			//int on where to split socket
			int socketToClose = -1;
			//turn list array to array;
			Object[] serverArray = currentUsers.toArray();
			log(currentUsers.size() + " Listsize");
			//slice number from array to call on array number and close socket
			String[] charLine = endLine.split(" ");
			String regex = "\\d+";
			for(int x = 0; x < charLine.length; x++) {
				if(charLine[x].matches(regex)) {
					log("System found integer");
					//minus 1 for the index of the array
					socketToClose = Integer.parseInt(charLine[x]);
				}
			}
			//call close socket on correct serverthread CHECKS ARRAY FOR INDEX HENCE THE SOCKET -1
			if(socketToClose != -1 && serverArray.length >= (socketToClose -1)) {
				//check socket length
				log("server Array legnth: " + serverArray.length + " ");
				//cast and call close socket on serverthread
				((ServerThread) serverArray[socketToClose -1]).getCommandFromServer("close socket");
				
				//slice user from currrent user array
				//need to rethink array slice for just a single slice
				serverArray = Arrays.copyOfRange(serverArray, 0, socketToClose);

			} else {
				log("Socket number " + socketToClose + " not found");
			}
			
			log("Arraysize: " + serverArray.length);
			
		}
	}

}