# ChatClient

A chat client is developed using socket programming, where the client can send various commands to the server.
- This client is able to send 6 commands to the server: AUTH, ENTER, SAY, NOOP, EXIT, BYE.
-- AUTH will allow the user to log-in to the server.
-- ENTER will allow the user to enter a specified room.
-- EXIT will allow the user to exit a specified room.
-- SAY will allow the user to send messages in the room
-- NOOP will allow the user to get the new messages in the room, without sending anything to the server.
-- BYE will allow the user to de-register from the server.
- Following 2 java files have been developed as a solution to the given problem:
- Client.java: This class has issueAuthCommand(), issueEnterCommand(), issueNoopCommand(), issueByeCommand(), issueSayCommand(), and issueExitCommand() methods, to send AUTH, ENTER, SAY, NOOP, EXIT, BYE commands respectively to the server.
- ClientLauncher.java: This class has methods to create the table “donations” and insert the records from the given text file to this table.
