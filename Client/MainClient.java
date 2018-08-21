import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

//****CREDITS****//
//help from cs.lmu.edu for basic Jtext and socket methods...


public class MainClient {
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    private JFrame frame = new JFrame("K3LLY Client");
    private JTextField dataField = new JTextField(40);
    private JTextArea messageArea = new JTextArea(8, 60);

    public MainClient() {

        // Layout GUI
        messageArea.setEditable(false);
        frame.getContentPane().add(dataField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");

        // change to keylistener action listener was to open
        dataField.addKeyListener(new KeyListener() {

            @Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			           out.println(dataField.getText());
	                   String response = null;
	                try {
	                    response = in.readLine();
	                    if (response == null || response.equals("")) {
	                          return;
	                      } else if(response.equals("close client")) {
	                    	  System.exit(0);
	                      }
	                    	out.flush();
	                	} catch (IOException ex) {
	                       response = "Error: " + ex;
	                	}
	                messageArea.append(response + "\n");
	                dataField.selectAll();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
			}
       });
				
    }
    public void connectToServer() throws IOException {

        // Get the server address from a dialog box.
        String serverAddress = JOptionPane.showInputDialog(
            frame,
            "Enter IP Address of the Server:",
            "Welcome to the K3LLY Program",
            JOptionPane.QUESTION_MESSAGE);

        // Make connection and initialize streams
        socket = new Socket(serverAddress, 9898);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Consume the initial welcoming messages from the server
        //only reads init message
        int checkOpening = 0;
        while(socket.isConnected() && checkOpening <= 0) {
        	messageArea.append(in.readLine() + "\n");
        	System.out.println(in.readLine());
        	socket.setKeepAlive(true);
        	checkOpening++;
        }
    }

    /**
     * Runs the client application.
     */
    public static void main(String[] args) throws Exception {
        MainClient client = new MainClient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.pack();
        client.frame.setVisible(true);
        client.connectToServer();
    }
}
