FILES: imserver.java
	   imclient.java
	   
AUTHOR: Daisy Tsang

*********************************************************************************************************

*DESCRIPTION*

The instant messaging server, upon starting up, will open a listening socket(server socket).
It will then enter into an infinite loop, waiting for new connections and passing data between
existing connections. Note: use ctrl-C to kill a process that is in an infinite loop.

The instant messaging client, upon starting up, will connect to the server. It will read in messages
from the keyboard and relay them to the server. It will also read in messages from the server and 
relay them to the screen.

In addition, I have:
1. enabled the server to send back error messages to the client
	- if one user tried to send a message to another user who is not logged in
2. enabled the server to keep track of how many users are logged in
3. created "presence" information 
	- server announces when people log on
	- server announces when people log off 
	- server announces current number of users logged in

*********************************************************************************************************

*HOW TO RUN CODE*

To compile, type: 
> javac *.java

To run imserver.java, type:
> java imserver [port number]

To run imclient.java type:
> java imclient [server address] [server port number]

The program should run according to the protocol outlined below. 

***********************************************************************************************************

*PROTOCOL*

A client is in one of three states: 
- CONNECTING
- CONNECTED
- DISCONNECTING

When a client first connects to the server, it is in the CONNECTING state.
CONNECTING
The server sends a message to the client of the form:
        ID
where:
 - ID is a positive integer represented as a string. It indicates the client's "ID" on the server.
The client is considered in the CONNECTED state.

CONNECTED
The connection is idle until the server sends a message to the client (a)
or the client sends a message to the server (b or c).

a. The server sends a message to the client of the form:
        ID " " MSG
where:
 - ID is a positive integer represented as a string. It indicates the ID of the sender of the message.
 - between the ID and MSG is a space character
 - MSG is a string (ASCII text) of ANY length. It is terminated by a null character, but not a newline.

b. The client sends a message to the server of the form:
        ID " " MSG
formatted as described above in (a). In this case, ID refers to the RECEIVER of the message (not the sender).

c. The client sends a message to the server of the form:
        QUIT
where QUIT is literally the ASCII string "QUIT".
The client is considered in the DISCONNECTING state.

DISCONNECTING
No communication takes place in the DISCONNECTING state. 
The server and client are free to close their sockets.

*************************************************************************************************************