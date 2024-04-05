import java.net.*;
import java.io.*;

public class Server {

	public static void main(String[] args)
	{		
		ServerSocket serverSocket = null;
		String line;
		BufferedReader is;
		PrintWriter os;
		Socket clientSocket = null;
		int port = 1337;
		
		try{
			serverSocket = new ServerSocket(port);
			System.out.println("Server running...");
			System.out.println("Host name: " + InetAddress.getLocalHost().getHostName());
			System.out.println("Host IP: " + InetAddress.getLocalHost().getHostAddress());
			System.out.println("Port: " + port);
		}catch(IOException e){
			System.out.println(e);
		}
		
		try{
			clientSocket = serverSocket.accept();
			is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			os = new PrintWriter(clientSocket.getOutputStream(), true);
			ServerController servControl = new ServerController(clientSocket, is, os);
			servControl.announce();
			
			System.out.println("Connected and Listening...");
			
			while(true){
				line = is.readLine();
				/*System.out.println("line: " + line);
				System.out.println("os: " + os);
				System.out.println("is: " + is);
				System.out.println("clientSocket: " + clientSocket);*/
				servControl.answerRequest(line);
			}
		}catch(IOException e){
			System.out.println(e);
		}
	}//end main
}//end CLASS
