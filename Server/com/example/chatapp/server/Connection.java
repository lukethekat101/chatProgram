package com.example.chatapp.server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection extends Thread {

	private Socket socket;
	private Server server;
	private int id;
	private String username;
	private String ip;
	private BufferedReader in;
	public PrintWriter out;

	public Connection(Socket socket, Server server, int id) {
		this.socket = socket;
		this.server = server;
		this.id = id;
	}

	public void run() {

		// Initializing I/O streams
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Receiving IP Address
		try {
			out.println("GETIPADDRESS");
			ip = in.readLine();
		} catch (Exception e) {
		}

		// Receiving data
		try {

			do {
				out.println("GETUSERNAME");
				username = in.readLine();
			} while (username.length() >= 20);

			// Displays amount of people online
			int totalConnected = 0;
			synchronized (this) {
				for (int i = 0; i < Server.MAX_CONNECTIONS; i++) {
					if (server.connections[i] != null) {
						totalConnected++;
					}
				}
			}

			synchronized (this) {
				for (Connection i : server.connections) {
					if (i != null) {
						i.out.println(username + " has joined the server. There are now " + totalConnected + "/"
								+ Server.MAX_CONNECTIONS + " online.");
						i.out.println("AOIWJDIOAJWDIOAJWIODJAWIOD");
					}
				}
			}

			String data;
			while ((data = in.readLine()) != null) {
				System.out.println(ip + ": " + username + "> " + data);
				synchronized (this) {
					for (int i = 0; i < Server.MAX_CONNECTIONS; i++) {
						if (server.connections[i] != this && server.connections[i] != null) {
							server.connections[i].out.println(username + "> " + data);
						}
					}
				}
			}

		} catch (IOException e) {
		}

		synchronized (this) {
			server.connections[id] = null;
		}

		// Closing I/O streams
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
