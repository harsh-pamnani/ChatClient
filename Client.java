import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	// Define "constants" to be used throughout the program.
	private final String ALL_OK = "3901chat/1.0 200 ok";
	private final String BAD_ROOM = "Invalid room requested.";
	private final String BAD_AUTHENTICATION = "Invalid authentication information provided.";
	
	/**
	 * This method will send AUTH command to server, to authenticate the user.
	 * 
	 * @param host: Host name
	 * @param port: Port number
	 * @return : Return the cookie string for authenticated user.
	 *           If authentication is unsuccessful, then it will return empty string.
	 * @throws IOException: This method will throw IOException in case there are any error while reading the buffer reader.
	 */
	public String issueAuthCommand(String host, int port) throws IOException {
		Socket echoSocket = null;
		PrintWriter out = null;
		BufferedReader inputBuffer = null;
		String cookie = "";
		Scanner sc = new Scanner(System.in);

		try {
			// Tell Java to use IPv4 like the rest of the world.
			System.setProperty("java.net.preferIPv4Stack", "true");
			
			// Create client socket, output stream and input stream.
			echoSocket = new Socket(host, port);
			out = new PrintWriter(echoSocket.getOutputStream(), true);
			inputBuffer = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
		} catch (IOException e) {
			// Report error in case the socket can't be opened.
			System.err.println("Couldn't open socket for the connection.");
			System.exit(1);
		}

		// Ask the user for username and password.
		System.out.println("Enter your username");
		String username = sc.next();
		System.out.println("Enter your password");
		String password = sc.next();
		
		// Creating AUTH command and sending it to the server.
		String auth = "AUTH " + username + " 3901chat/1.0\r\n" + "Password: " + password + "\r\n" + "\r\n";		
		out.println(auth);

		// Reading response from server.
		String userAuthenticationReturn = inputBuffer.readLine();
		
		// Printing the output in Client according to the response from server.
		if (userAuthenticationReturn.equals(ALL_OK)) {
			// Setting the cookie string as cookie provided in server, if the user is authenticated.
			cookie = inputBuffer.readLine().substring(12);
			System.out.println("User authenticated.");
		} else {
			System.out.println(BAD_AUTHENTICATION);
		}
		
		// Closing client socket, output stream and input stream.
		out.close();
		inputBuffer.close();
		echoSocket.close();
		
		// Return the cookie string for authenticated user.
		// If authentication is unsuccessful, then it will return empty string.
		return cookie;
	}
	
	/**
	 * This method will send ENTER command to server, to enter in a specified room.
	 * 
	 * @param host: Host name
	 * @param port: Port number
	 * @param cookie: Cookie of the logged-in user
	 * @return : Returns the room name if user successfully entered in any room.
	 *           If entering in a room is unsuccessful, then it will return empty string.
	 * @throws IOException: This method will throw IOException in case there are any error while reading the buffer reader.
	 */
	public String issueEnterCommand(String host, int port, String cookie) throws IOException {
		String result = "";
		Socket echoSocket = null;
		PrintWriter out = null;
		BufferedReader inputBuffer = null;
		Scanner sc = new Scanner(System.in);

		try {
			// Tell Java to use IPv4 like the rest of the world.
			System.setProperty("java.net.preferIPv4Stack", "true");
			
			// Create client socket, output stream and input stream.
			echoSocket = new Socket(host, port);
			out = new PrintWriter(echoSocket.getOutputStream(), true);
			inputBuffer = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
		} catch (IOException e) {
			// Report error in case the socket can't be opened.
			System.err.println("Couldn't open socket for the connection.");
			System.exit(1);
		}
		
		// Ask the user for which room to enter.
		System.out.println("Room to enter? (Enter 1 or 2)");
		System.out.println("1. dal");
		System.out.println("2. 3901");

		String room = "";
		
		// Setting the room name according to the user choice.
		String choice = sc.next();
		if (choice.equals("1") || choice.equals("dal")) {
			room = "dal";
		} else if (choice.equals("2") || choice.equals("3901")) {
			room = "3901";
		}
		
		// Creating ENTER command and sending it to the server.
		String enter = "ENTER " + room + " 3901chat/1.0\r\n" + "Cookie: " + cookie + "\r\n" + "\r\n";
		out.println(enter);

		// Reading response from server.
		String roomEntered = inputBuffer.readLine();
		
		// Printing the output in Client according to the response from server.
		if (roomEntered.equals(ALL_OK)) {
			System.out.println("Room Entered.");
			// Setting the room string as room entered by the user.
			result = room;
		} else {
			System.out.println(BAD_ROOM);
		}
		
		// Closing client socket, output stream and input stream.
		out.close();
		inputBuffer.close();
		echoSocket.close();
		
		// Returns the room name if user successfully entered in any room.
		// If entering in a room is unsuccessful, then it will return empty string.
		return result;
	}
	
	/**
	 * This method will send NOOP command to server, to get the new messages in the room.
	 * 
	 * @param host: Host name
	 * @param port: Port number
	 * @param cookie: Cookie of the logged-in user
	 * @throws IOException: This method will throw IOException in case there are any error while reading the buffer reader.
	 */
	public void issueNoopCommand(String host, int port, String roomEntered, String cookie) throws IOException {
		Socket echoSocket = null;
		PrintWriter out = null;
		BufferedReader inputBuffer = null;
		Scanner sc = new Scanner(System.in);

		try {
			// Tell Java to use IPv4 like the rest of the world.
			System.setProperty("java.net.preferIPv4Stack", "true");
			
			// Create client socket, output stream and input stream.
			echoSocket = new Socket(host, port);
			out = new PrintWriter(echoSocket.getOutputStream(), true);
			inputBuffer = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
		} catch (IOException e) {
			// Report error in case the socket can't be opened.
			System.err.println("Couldn't open socket for the connection.");
			System.exit(1);
		}
		
		// Creating NOOP command and sending it to the server.
		String noop = "NOOP NOOP 3901chat/1.0\r\n" + "Cookie: " + cookie + "\r\n" + "\r\n";
		out.println(noop);
		
		// Reading response from server.
		String noopResponse = inputBuffer.readLine();

		// Printing the output in Client according to the response from server.
		if (noopResponse.equals(ALL_OK)) {
			String other = "";
			String newMessages = "";
			
			// Loop to store messages from other user.
			while (other != null) {
				other = inputBuffer.readLine();
				if (other != null) {
					if (!other.equals("")) {
						String parts[] = other.split(":");
						if (parts[0].equals("Content-Length")) {
							// If the content length is 0, then the loop will break as there are no new messages.
							if (parts[1].equals(" 0")) {
								break;
							} else {
								continue;
							}
						}
						// Attaching each line of response message to the newMessages string.
						newMessages += (other + "\n");
					}
				}
			}
			
			// Printing the new messages from other user.
			// If there are no messages, it will ""No new messages in the room."
			System.out.println("**************************************");
			if (!newMessages.equals("")) {
				System.out.println("Messages from other users in the room:");
				System.out.println(newMessages);
			} else {
				System.out.println("No new messages in the room.");
			}
			System.out.println("**************************************");
		} else {
			System.out.println("Noop failed");
		}
		
		// Closing client socket, output stream and input stream.
		out.close();
		inputBuffer.close();
		echoSocket.close();
	}
	
	/**
	 * This method will send BYE command to server, to deregister the user from server.
	 * 
	 * @param host: Host name
	 * @param port: Port number
	 * @param cookie: Cookie of the logged-in user
	 * @throws IOException: This method will throw IOException in case there are any error while reading the buffer reader.
	 */
	public void issueByeCommand(String host, int port, String cookie) throws IOException {
		Socket echoSocket = null;
		PrintWriter out = null;
		BufferedReader inputBuffer = null;
		Scanner sc = new Scanner(System.in);

		try {
			// Tell Java to use IPv4 like the rest of the world.
			System.setProperty("java.net.preferIPv4Stack", "true");
			
			// Create client socket, output stream and input stream.
			echoSocket = new Socket(host, port);
			out = new PrintWriter(echoSocket.getOutputStream(), true);
			inputBuffer = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
		} catch (IOException e) {
			// Report error in case the socket can't be opened.
			System.err.println("Couldn't open socket for the connection.");
			System.exit(1);
		}
		
		// Creating BYE command and sending it to the server.
		String bye = "BYE BYE 3901chat/1.0\r\n" + "Cookie: " + cookie + "\r\n" + "\r\n";
		out.println(bye);

		// Reading response from server.
		String byeResponse = inputBuffer.readLine();
		
		// Printing the output in Client according to the response from server.
		if (byeResponse.equals(ALL_OK)) {
			System.out.println("Deregistered successfully from the server.");
		} else {
			System.out.println("Not able to deregister from the server.");
		}
		
		// Closing client socket, output stream and input stream.
		out.close();
		inputBuffer.close();
		echoSocket.close();
	}
	
	/**
	 * This method will send SAY command to server, to send messages in the room.
	 * 
	 * @param host: Host name
	 * @param port: Port number
	 * @param roomEntered: Room name in which the user wants to send the messages.
	 * @param cookie: Cookie of the logged-in user
	 * @throws IOException: This method will throw IOException in case there are any error while reading the buffer reader.
	 */
	public void issueSayCommand(String host, int port, String roomEntered, String cookie) throws IOException {
		Socket echoSocket = null;
		PrintWriter out = null;
		BufferedReader inputBuffer = null;
		Scanner sc = new Scanner(System.in);

		try {
			// Tell Java to use IPv4 like the rest of the world.
			System.setProperty("java.net.preferIPv4Stack", "true");
			
			// Create client socket, output stream and input stream.
			echoSocket = new Socket(host, port);
			out = new PrintWriter(echoSocket.getOutputStream(), true);
			inputBuffer = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
		} catch (IOException e) {
			// Report error in case the socket can't be opened.
			System.err.println("Couldn't open socket for the connection.");
			System.exit(1);
		}
		
		// Ask the user for the message to be sent in the group.
		System.out.println("Enter the information you want to post in the room");
		String message = sc.next();
		
		// Loop to check whether the user is not sending empty messages.
		while (message.length() == 0) {
			System.out.println("Message length zero.");
			System.out.println("Please enter the message again.");
			message = sc.next();
		}
		
		// Creating SAY command and sending it to the server.
		String say = "SAY " + roomEntered + " 3901chat/1.0\r\n" + "Cookie: " + cookie + "\r\n" + "Content-Length: "
				+ message.length() + "\r\n" + "\r\n" + message;
		out.println(say);

		// Reading response from server.
		String sayMessageResponse = inputBuffer.readLine();
		
		// Printing the output in Client according to the response from server.
		if (sayMessageResponse.equals(ALL_OK)) {
			System.out.println("Message posted to the room.");
			String other = "";
			String newMessages = "";

			// Loop to store messages from other user.
			while (other != null) {
				other = inputBuffer.readLine();
				if (other != null) {
					if (!other.equals("")) {
						// Splitting the header line into an array.
						String parts[] = other.split(":");
						
						// Ignoring the "Content-Length", while creating newMessages string.
						if (!parts[0].equals("Content-Length")) {
							// Ignoring the messages from the same user as the logged-in user.
							if (!parts[0].equals(cookie.substring(3))) {
								// Attaching each line of response message to the newMessages string.
								newMessages += (other + "\n");
							}
						}
					}
				}
			}
			
			// Printing the new messages from other user.
			if (!newMessages.equals("")) {
				System.out.println("**************************************");
				System.out.println("Messages from other users in the room:");
				System.out.println(newMessages);
				System.out.println("**************************************");
			}
		} else {
			System.out.println(BAD_ROOM + " Message not posted the room.");
		}
		
		// Closing client socket, output stream and input stream.
		out.close();
		inputBuffer.close();
		echoSocket.close();
	}
	
	/**
	 * This method will send EXIT command to server, to exit from the specified room.
	 * 
	 * @param host: Host name
	 * @param port: Port number
	 * @param roomEntered: Room name in which the user wants to send the messages.
	 * @param cookie: Cookie of the logged-in user
	 * @throws IOException: This method will throw IOException in case there are any error while reading the buffer reader.
	 */
	public void issueExitCommand(String host, int port, String roomEntered, String cookie) throws IOException {
		Socket echoSocket = null;
		PrintWriter out = null;
		BufferedReader inputBuffer = null;
		Scanner sc = new Scanner(System.in);

		try {
			// Tell Java to use IPv4 like the rest of the world.
			System.setProperty("java.net.preferIPv4Stack", "true");
			
			// Create client socket, output stream and input stream.
			echoSocket = new Socket(host, port);
			out = new PrintWriter(echoSocket.getOutputStream(), true);
			inputBuffer = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
		} catch (IOException e) {
			// Report error in case the socket can't be opened.
			System.err.println("Couldn't open socket for the connection.");
			System.exit(1);
		}
		
		// Creating EXIT command and sending it to the server.
		String exit = "EXIT " + roomEntered + " 3901chat/1.0\r\n" + "Cookie: " + cookie + "\r\n" + "\r\n";
		out.println(exit);

		// Reading response from server.
		String roomExit = inputBuffer.readLine();
		
		// Printing the output in Client according to the response from server.
		if (roomExit.equals(ALL_OK)) {
			System.out.println("Room exit done.");
		} else {
			System.out.println(BAD_ROOM);
		}
		
		// Closing client socket, output stream and input stream.
		out.close();
		inputBuffer.close();
		echoSocket.close();
	}
}