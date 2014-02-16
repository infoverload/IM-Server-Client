/** 
  * Name: imclient.java                 
  * Course: CS3357            
  * Author: Daisy Tsang      
  * Description: Instant messaging client 
  */

import java.net.*;
import java.io.*; 

public class imclient {
	
	public static void main(String[] args) throws Exception {
		
		int portNum; // to hold port number
		String idNum; // to hold user ID 
				
		// expects address of server and port of server
		if (args.length != 2) {
		
			System.out.println("Please give sufficient arguments.");
			System.exit(0);
			
		}

		System.out.println(""); 
		System.out.println("Connecting to server at address " + args[0] + " on port " + args[1] + " ...");

		// connect to server
		portNum = Integer.parseInt(args[1]); // cast string to int
		Socket socket = new Socket(args[0], portNum); 

		System.out.println("Connected to server at address " + args[0] + " on port " + args[1]);
				 
		BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in)); // to read from keyboard
		BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream())); // to read from server
		PrintWriter serverOutput = new PrintWriter(socket.getOutputStream(), true); // to write to server
		String inputLine = ""; // to hold user input
		
		// display program info for the user
		System.out.println(""); 
		System.out.println("*****************************************************************");
		System.out.println("                    Welcome to IMclient!");
		System.out.println("Type the ID of the user you wish to talk to before every message.");
		System.out.println("            Type 'QUIT' at any time to exit program.");
		System.out.println("*****************************************************************");
		System.out.println(""); 
		
		// get user ID from server
		idNum = serverInput.readLine(); 
		System.out.println("You have been assigned the ID: " + idNum);
		
		// loop until user types quit
		while (true) {

			if (userInput.ready()) {
			
				inputLine = userInput.readLine(); // read user input
				
			}
			else {
			
				Thread.sleep(100);
				inputLine = "";
				
			}

			// read in messages from the keyboard and relay them to the server
			serverOutput.println(inputLine); 
		
			if (inputLine.equals("QUIT")) {
				// disconnect! -> close sockets
				System.out.println("IMclient: Goodbye!");      				
       			userInput.close(); 
       			serverInput.close(); 
				serverOutput.close();
				System.exit(1); 
				
			}
			else if (serverInput.ready()) {
				// read in messages from the server and relay them to the screen
				System.out.println(serverInput.readLine()); 
				
			}

		} // infinite while
        
	} // main

} 