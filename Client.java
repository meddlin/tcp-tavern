import java.net.*;
import java.io.*;

public class Client {

	public static void main(String[] args)
	{
		String host = args[0];
		String heloHost = "";
		String port = args[1];
		String userInput = null;
		String responseLine = "";
		String temp = "";
		Socket smtpSocket = null;
		BufferedReader input = null;
		BufferedReader stdIn = null;
		PrintWriter out = null;
		
		try{
			//System.out.println("HOSTNAME TEST: " + InetAddress.getLocalHost().getHostName());
			heloHost = InetAddress.getLocalHost().getHostName();
		}catch (UnknownHostException e){
			System.out.println("error getting hostname");
		}
		
		
		System.out.println("Connect using......DPRP Protocol -- Client ver. 13.37");
		
		try{
			smtpSocket = new Socket(host, Integer.parseInt(port));
			input = new BufferedReader(new InputStreamReader(smtpSocket.getInputStream()));
			out = new PrintWriter(smtpSocket.getOutputStream(), true);
			stdIn = new BufferedReader(new InputStreamReader(System.in));

		}catch (UnknownHostException e) {
			System.err.println("Don't know about host: hostname");
		}catch (IOException e){
			System.err.println("Couldn't get I/O for the connection to: hostname");
		}
		
		if ((smtpSocket != null) && (out != null) && (input != null)){
			try{
				responseLine = input.readLine();
				System.out.println(responseLine);
				
				/*while(smtpSocket != null){
					userInput = stdIn.readLine();
					if(userInput.compareTo("BYE") == 0){
						out.println(userInput);
						break;
					}else{
						out.println(userInput);
						responseLine = input.readLine();
						System.out.println(responseLine);
					}
				}*/
				
				System.out.println();
				System.out.println("Sending HELO " + heloHost);
				out.println("HELO " + heloHost);
				responseLine = input.readLine();
				System.out.println(responseLine);
				System.out.println();
				
				System.out.println("Sending REQTIME");
				out.println("REQTIME");
				responseLine = input.readLine();
				System.out.println(responseLine);
				System.out.println();
				
				System.out.println("Sending REQDATE");
				out.println("REQDATE");
				responseLine = input.readLine();
				System.out.println(responseLine);
				System.out.println();
				
				System.out.println("Sending ECHO blahblahblah");
				out.println("ECHO blahblahblahblah");
				responseLine = input.readLine();
				System.out.println(responseLine);
				System.out.println();
				
				System.out.println("Sending REQIP");
				out.println("REQIP");
				responseLine = input.readLine();
				System.out.println(responseLine);
				System.out.println();
				
				System.out.println("Sending BYE");
				out.println("BYE");
				responseLine = input.readLine();
				System.out.println(responseLine);
				System.out.println();
				
				//clean up
				out.close();
				input.close();
				smtpSocket.close();
			}catch (UnknownHostException e){
				System.err.println("Trying to connect to unknown host: " + e);
			}catch(IOException e){
				System.err.println("IOException: " + e);
			}//end try
		}//end if
	}
}




/*while((userInput = stdIn.readLine()) != null){
out.println(userInput); //sends user input to server
responseLine = input.readLine(); //grabs response from server

while( (temp = input.readLine()) != null ){
	responseLine = responseLine + temp;
}

System.out.println(responseLine);
}*/
/*while(stdIn != null){
userInput = stdIn.readLine();
out.println(userInput);
while( (temp = input.readLine()) != null ){
	responseLine = responseLine + temp;
}
}*/