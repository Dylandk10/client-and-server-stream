import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ServerThread extends Thread {
	private Socket socket;
	private int clientNumber;
	PrintWriter out;
	//UsersArray<ServerThread> usersArray = new UsersArray<ServerThread>();
	public ServerThread(Socket socket, int clientNumber) {
		this.socket = socket;
		this.clientNumber = clientNumber;
		System.out.println("Client connected " + this.clientNumber + " connected Users");
		MainServer.log("Client connected " + this.clientNumber + " connected users \n");
		//UsersArray.push(this);
	}
	public void run() {
		try {
			 BufferedReader inBuffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			 out = new PrintWriter(socket.getOutputStream(), true);
			 out.println("Welcome to K3lly client");
			 while(socket.isConnected()) {
				 String input = inBuffer.readLine();
				 if(input == null || input.equals("exit")) {
					 break;
				 }
				 MainServer.log(input + "\n");
				 if(input != null) {
					 out.println(input + "\n");
				 }
				 out.flush();
			 }
			 //wait after init non-blocking
			 try {
				out.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} try {
			socket.close();
		
		} catch (IOException e) {
			MainServer.log("Couldn't close a socket, what's going on? \n");
        }
            MainServer.log("Connection with client# " + clientNumber + ": socket closed \n");
	}
	public void getCommandFromServer(String message) {
		//closes socket to client and shuts down client
		if(message.contains("close socket")) {
			try {
				MainServer.log("socket closed and client: " + this.clientNumber);
				this.socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
