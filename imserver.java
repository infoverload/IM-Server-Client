
import java.net.*;
import java.io.*;
import java.util.Vector;

public class imserver {

	public static void main(String[] args) throws Exception {

		int portNum;       // to hold port number
		int arrayPos = 0;  // to hold array position
		int userCount = 0; // to hold user count

		// expects a port number to listen to
		if (args.length != 1) {
			System.out.println("Please give sufficient arguments.");
			System.exit(0);
		}

		System.out.println("Connecting to port " + args[0] + " ...");

		// open a listening socket (server socket)
		portNum = Integer.parseInt(args[0]); // cast string to int
		ServerSocket serverSocket = new ServerSocket(portNum);
		serverSocket.setSoTimeout(500);

		System.out.println("Connected to port " + args[0]);

		Vector<Socket> clientVector = new Vector<Socket>(); // vector of client sockets

		/** Enter into an infinite loop, waiting for new connections
	   	  * and passing data between existing connections
	   	  */
		while (true) { // infinite loop
			try {
				Socket clientSocket = new Socket(); // create client socket for each client
				clientSocket = serverSocket.accept(); // accept any incoming connections

				// give any clients that are connecting an ID and store in vector
				String id = "" + arrayPos;
				arrayPos++;
				userCount++;
				PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true); //to write to client
				output.println(id); // send a message to client in the form of an ID string
				clientVector.addElement(clientSocket); // store in vector

				// CREATE PRESENCE: user logs on
				for (int k = 0; k < clientVector.size(); k++) {

					if (k == (arrayPos - 1)) // don't alert the user they are online themselves
						continue;

					if (clientVector.elementAt(k).isClosed() == false) {
						PrintWriter temp; // to write to each client
						temp = new PrintWriter(clientVector.elementAt(k).getOutputStream(), true);
						temp.println("< User " + id + " has logged in; " + userCount + " users currently logged in >");
					}

				}

				System.out.println("There are currently " + userCount + " users");

			} catch (SocketTimeoutException e){}

			for (int i = 0; i < clientVector.size(); i++) {

				Socket clientSocket = clientVector.elementAt(i); // to hold each client socket

				if (clientSocket.isClosed())
					continue;

				PrintWriter output; //to write to client
				output = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader input; //to read from client
				input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

				String inputLine; // to hold user input

				// must accept messages from one client to another client
				// each message received should be sent only to intended recipient client based on ID
				while (input.ready()) {
					inputLine = input.readLine();
					if (inputLine.equals("QUIT")) {

						// disconnect -> close sockets
						userCount--;
						System.out.println("There are currently " + userCount + " users");
						output.close();
						input.close();
		           	        	clientSocket.close();

						// CREATE PRESENCE: user logs out
						for (int n = 0; n < clientVector.size(); n++) {
							if (clientVector.elementAt(n).isClosed() == false) {
								PrintWriter temp; // to write to each client
								temp = new PrintWriter(clientVector.elementAt(n).getOutputStream(), true);
								temp.println("< User " + i + " has logged off; " + userCount
										      + " users currently logged in >");
							}
						}
						break;
					}
					else if (inputLine.length() > 0) {
						try {
							int destination = Integer.parseInt("" + inputLine.charAt(0)); // to hold receiver ID
							if (destination >= clientVector.size()) { // check to see if user exists
								//write error msg back to sender
								output.println("User " + destination + " is not available. :(");
							}
							for (int j = 0; j < clientVector.size(); j++) {
								if (destination == j) {
									Socket outSocket = clientVector.elementAt(j);
									if (outSocket.isClosed()) { // check to see if user is still logged on
										//write error msg back to sender
										output.println("User " + j + " is not available. :(");
										continue;
									}
									PrintWriter pw; // to write to specific client
									pw = new PrintWriter(outSocket.getOutputStream(), true);
									// Echo input to output
									pw.println("Message from user " + i + ":" + inputLine.substring(1));
								}
							}

						} catch (NumberFormatException e){}
					}

				} // while

			} // for

		} // infinite while

	} // main

}
