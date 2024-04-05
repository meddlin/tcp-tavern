import java.net.*;
import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.text.DateFormat;


public class ServerController {

	Boolean greeting = false;
	Boolean ECHOFLAG = false;
	Socket clientSocket = null;
	BufferedReader is = null;
	PrintWriter os = null;
	Scanner parseScanner = null;
	

	public ServerController(Socket clientSocket, BufferedReader is, PrintWriter os) {
		this.clientSocket = clientSocket;
		this.is = is;
		this.os = os;
	}
	
	public void announce(){
		os.println("200: DPRP (Dread Pirate Roberts Protocol) version 13.37 ready");
	}
	
	public void answerRequest(String input){
		String toPrint = "";
		
		//if(!greeting && (input.compareTo("HELO") == 0) ){
		if( !greeting && (processHELO(input)) ){
			greeting = true;
			toPrint = "210: server heard you and is ready for commands";
			
		//}else if(greeting && (input.compareTo("HELO") == 0) ){
		}else if( greeting && (processHELO(input)) ){
			toPrint = "211: you've already said HELO";
			
		}else if(greeting){
			if(input.compareTo("BYE") == 0){				
				try{
					//would have to introduce threads in order to be able to reconnect after closing the connection
					os.println("600: See ya later!");
					os.close();
					is.close();
					clientSocket.close();
				}catch(IOException e){
					System.out.println("BYE Error: " + e);
				}
			}else{
				toPrint = processCommand(input);
			}
			
			
		}else{
			toPrint = "000: you need to say HELO first";
		}
		
		os.println(toPrint);
	}//end answerRequest()
	
	public Boolean processHELO(String input){
		Boolean welcome = false;
		parseScanner = new Scanner(input);
		int argCounter = 0;
		String tempStr = "";
		
		while(parseScanner.hasNext()){
			if(argCounter == 0){
				tempStr = parseScanner.next();
				if( !(tempStr.compareTo("HELO") == 0) ){
					break;
				}
				argCounter++;
			}else if(argCounter == 1){
				welcome = true;
				argCounter++;
				break;
			}
		}
		
		return welcome;
	}
	
	public String processCommand(String command){
		parseScanner = new Scanner(command);
		int argCounter = 0;
		String forEcho = "";
		String tempStr = "";
		String answer = "";
		
		//parsing the user input
		while(parseScanner.hasNext()){
			if(argCounter == 0){
				tempStr = parseScanner.next();
				//forEcho = forEcho + tempStr;
				if(!(tempStr.compareTo("ECHO") == 0)){
					break;
				}
				tempStr = "";
				argCounter++;
			}else if(argCounter == 1){
				tempStr = parseScanner.next();
				forEcho = forEcho + tempStr;
				//otherwise, ECHO would come through here
				ECHOFLAG = true;
				tempStr = "";
				argCounter++;
			}
		}
		//System.out.println("scanner: forEcho = " + forEcho);
		
		if(command.compareTo("NANANA") == 0){ //the bat signal!
			answer = "Status: NANANANA...BATMAN!";
			
		}else if( (command.compareTo("HELP") == 0) || (command.compareTo("help") == 0) ){
			answer = "SRP-shell version 13.37" + "\r\n" + "Commands: \r\nREQTIME\r\nREQDATE\r\nECHO\r\nREQIP\r\n";
			
		}else if((command.compareTo("REQTIME") == 0) || (command.compareTo("reqtime") == 0)){
			answer = getTime();
			
		}else if((command.compareTo("REQDATE") == 0) || (command.compareTo("reqdate") == 0)){
			answer = getDate();
			
		}else if((command.compareTo("REQDT") == 0) || (command.compareTo("reqdt") == 0)){
			answer = "235: " + getTime() + " " + getDate();
			
		//}else if((command.compareTo("ECHO") == 0) || (command.compareTo("echo") == 0)){
		}else if(ECHOFLAG){
			 /* Status 240: "echo" the string the client originally sent
			 * Status 540: server is having problems with the ECHO command
			 */
			answer = "240: " + " " + forEcho;
			ECHOFLAG = false;
			
		}else if((command.compareTo("REQIP") == 0) || (command.compareTo("reqip") == 0)){
			 /* Status 250: server prints the client's IP address
			 * Status 550: server is having problems getting the client's IP
			 */
			answer = "250:  IP--" + clientSocket.getInetAddress().toString() + " Port--" + clientSocket.getPort();
			
		}else{
			answer = "999: Request not recognized";
		}
		
		return answer;
	}//end processCommand()
	
	public String getTime(){
		/* REQTIME -- request time
		 * Status 220: server's current time in ISO8601 format (hh:mm:ss)
		 * Status 520: server is having problems getting the current time
		 */
		String request = "";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		if((cal != null) && (sdf != null)){
			cal.getTime(); //not sure if this call is needed!!
			request = "220: " + (sdf.format(cal.getTime()));
		}else{
			request = "520: server is having problems getting the current time";
		}
		return request;
	}
	
	public String getDate(){
		/* Status 230: server's current date in ISO8601 format (YYYY-MM-DD)
		 * Status 530: server is having problems getting the current date
		 */
		String request = "";
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		Date d = new Date();
		if((df != null) && (d != null) ){
			request = "230: " + (df.format(d));
		}else{
			request = "530: server is having problems getting the current date";
		}
		return request;
	}
	
	public void printToClient(String input){
		System.out.println("printToClient() input=" + input);
		os.println("printToClient: " + input);
	}
}//end CLASS: ServerController
