import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * Creates a multi-threaded server
 */

public class Server {
	
	public static void main(String args[]) {
		new Server(3940).listen();
	}
	
	// Instance variables
	public static final int MAX_CONNECTIONS = 5;
	public Connection[] connections;
	private ServerSocket server;
	private BufferedReader in;
	private PrintWriter out;
	private Socket socket;
	
	// Constructor
	public Server (int port) {
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		connections = new Connection[MAX_CONNECTIONS];
	}
	
	// Adds new connections
	public void listen() {
		while (true) {
			socket = null;
			
			// Clients connecting
			System.out.println("Waiting for client connection...");
			try {
				socket = server.accept();
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
			} catch (IOException e) {
				e.printStackTrace();
			} 
			System.out.println("Client connected");
			
			// Checking if client is open and bound
			int i;
			if (socket != null && socket.isBound()) {
				for (i = 0; i < connections.length; i++) {
					if (connections[i] == null) {
						connections[i] = new Connection(socket, this, i);
						connections[i].start();
						break;
					}
				}
				if (i == MAX_CONNECTIONS) {
					out.println("Maximum number of connections reached, could not join.");
					try {
						in.close();
						out.close();
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
}
