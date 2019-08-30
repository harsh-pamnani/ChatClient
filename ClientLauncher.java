import java.io.IOException;
import java.util.Scanner;

// ClientLuancher class file will contain main method to launch the client.
public class ClientLauncher {
	public static void main(String[] args) {
		// Define "constants" to be used throughout the program.
		final int NEGATIVE_VALUE = -1111;

		// Initializing variables for host and port.
		String host = "";
		int port = NEGATIVE_VALUE;

		// Parse command line arguments:
		// -h sets a host name
		// -p sets a port number
		int i = 0;
		while (i < args.length) {
			if (args[i].equals("-p")) {
				port = Integer.parseInt(args[i + 1]);
				i++;
			} else if (args[i].equals("-h")) {
				host = args[i + 1];
				i++;
			}
			i++;
		}

		// If there are no command line arguments provided,
		// then setting port as 28148 and host as "hector.cs.dal.ca".
		if (port == NEGATIVE_VALUE) {
			port = 28148;
		}
		if (host.equals("")) {
			host = "hector.cs.dal.ca";
		}

		// Creating new client instant and scanner object
		Client client1 = new Client();
		Scanner sc = new Scanner(System.in);

		// Send AUTH command to server for authentication of the user.
		String cookie = "";
		try {
			// Setting the cookie name which come from AUTH command.
			cookie = client1.issueAuthCommand(host, port);
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		// In case if he provides wrong authentication information, loop to ask user for re-entering the authorization details.
		while (cookie.equals("")) {
			System.out.println("Do you want to continue? Enter Y/N.");
			String ch = sc.next();

			// Loop to check the input should be only Y or N.
			while (!(ch.equalsIgnoreCase("Y") || ch.equalsIgnoreCase("N"))) {
				System.out.println("=====================================");
				System.out.println("Bad input");
				// Asking the user whether he wants to continue or not.
				System.out.println("Do you want to continue? Enter Y/N.");
				ch = sc.next();
			}

			// If the user provides Y, send AUTH command again.
			// Else, set cookie as NEGATIVE_VALUE.
			if (ch.equalsIgnoreCase("Y")) {
				try {
					cookie = client1.issueAuthCommand(host, port);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				cookie = NEGATIVE_VALUE + "";
			}
		}
		
		// Allow the user to proceed further only if there is some cookie value.
		// If the cookie is "" or NEGATIVE_VALUE, then the user will not be allowed to proceed further.
		if (!(cookie.equals("") || cookie.equals(NEGATIVE_VALUE + ""))) {
			String roomEntered = "";
			try {
				// Send Enter command to Server, and store the entered room in a string.
				roomEntered = client1.issueEnterCommand(host, port, cookie);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			// In case if he provides wrong room information, loop to ask user for re-entering another room.
			while (roomEntered.equals("")) {
				System.out.println("Do you want to continue? Enter Y/N.");
				String ch = sc.next();

				// Loop to check the input should be only Y or N.
				while (!(ch.equalsIgnoreCase("Y") || ch.equalsIgnoreCase("N"))) {
					System.out.println("====================================");
					System.out.println("Bad input");
					// Asking the user whether he wants to continue or not.
					System.out.println("Do you want to continue? Enter Y/N.");
					ch = sc.next();
				}

				// If the user provides Y, send ENTER command again.
				// Else, set roomEntered as NEGATIVE_VALUE.
				if (ch.equalsIgnoreCase("Y")) {
					try {
						roomEntered = client1.issueEnterCommand(host, port, cookie);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					roomEntered = NEGATIVE_VALUE + "";
				}
			}
			
			// Allow the user to proceed further only if user is entered in any room.
			// If the room is "" or NEGATIVE_VALUE, then the user will not be allowed to proceed further.
			if (!(roomEntered.equals("") || roomEntered.equals(NEGATIVE_VALUE + ""))) {
				String ch = "Y";
				while (ch.equalsIgnoreCase("Y")) {
					// Initializing the user choice to null.
					String choice = NEGATIVE_VALUE + "";
					
					// Loop to ask the user again for choices.
					while (!(choice.equalsIgnoreCase("1") || choice.equalsIgnoreCase("2")
							|| choice.equalsIgnoreCase("3") || choice.equalsIgnoreCase("4")
							|| choice.equalsIgnoreCase("5")) || choice.equalsIgnoreCase(NEGATIVE_VALUE + "")) {
						
						// Providing 5 options to the user.
						System.out.println("==================================================");
						System.out.println("What do you want to do?");
						System.out.println("1. SAY - Post information in the room");
						System.out.println("2. NOOP - No operation, just get the room messages");
						System.out.println("3. ENTER - Enter another room");
						System.out.println("4. EXIT - Exit the current room");
						System.out.println("5. BYE - Deregister from the server");
						choice = sc.next();
						
						// Saying "Bad Input", if user enters some other value than "1", "2", "3", "4", or "5".
						if (!(choice.equalsIgnoreCase("1") || choice.equalsIgnoreCase("2")
								|| choice.equalsIgnoreCase("3") || choice.equalsIgnoreCase("4")
								|| choice.equalsIgnoreCase("5"))) {
							System.out.println("Bad Input.");
						}
					}
					
					// If users selects 1, then sending SAY command to the server.
					if (choice.equals("1")) {
						try {
							client1.issueSayCommand(host, port, roomEntered, cookie);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} 
					// If users selects 2, then sending NOOP command to the server.
					else if (choice.equals("2")) {
						try {
							client1.issueNoopCommand(host, port, roomEntered, cookie);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} 
					// If users selects 3, then sending ENTER command to the server.
					else if (choice.equals("3")) {
						try {
							roomEntered = client1.issueEnterCommand(host, port, cookie);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} 
					// If users selects 4, then sending EXIT command to the server.
					else if (choice.equals("4")) {
						try {
							client1.issueExitCommand(host, port, roomEntered, cookie);
						} catch (IOException e) {
							e.printStackTrace();
						}
						roomEntered = "";
					} 
					// If users selects 5, then sending BYE command to the server.
					else if (choice.equals("5")) {
						try {
							client1.issueByeCommand(host, port, cookie);
						} catch (IOException e) {
							e.printStackTrace();
						}
						// If the user wants to deregister from the server,
						// then he will not be provided with any more options and the client program will close.
						System.exit(1);
					}
					
					// Asking the user whether he wants to continue or not.
					System.out.println("====================================");
					System.out.println("Do you want to continue? Enter Y/N.");

					ch = sc.next();

					// Loop to check the input should be only Y or N.
					while (!(ch.equalsIgnoreCase("Y") || ch.equalsIgnoreCase("N"))) {
						System.out.println("====================================");
						System.out.println("Bad input");
						// Asking the user whether he wants to continue or not.
						System.out.println("Do you want to continue? Enter Y/N.");
						ch = sc.next();
					}
				}
			}
		}
	}
}
