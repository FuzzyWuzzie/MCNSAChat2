package com.mcnsa.mcnsachat2.net;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.bukkit.entity.Player;

import com.mcnsa.mcnsachat2.MCNSAChat2;
import com.mcnsa.mcnsachat2.util.ColourHandler;

public class NetworkManager {
	private MCNSAChat2 plugin;
	private String host;
	private Integer port;
	private Socket connection;
	private DataOutputStream outStream;
	private NetworkListener listener;
	
	public NetworkManager(MCNSAChat2 instance, String _host, Integer _port) {
		plugin = instance;
		host = _host;
		port = _port;
	}
	
	public boolean connect() {
		// connect to the server
		try {
			// open up the connection
			connection = new Socket(host, port);
			plugin.log("Connected to " + host + " on port " + port);
			
			// start the listening thread
			listener = new NetworkListener(this, connection);
			listener.start();
			
			// setup the output writing stream
			outStream = new DataOutputStream(connection.getOutputStream());
			
			// and broadcast to the server
			Player[] players = plugin.getServer().getOnlinePlayers();
			for(int i = 0; i < players.length; i++) {
				ColourHandler.sendMessage(players[i], "&2This universe is now connected to the chat server!");
			}
		}
		catch (Exception e) {
			plugin.error("error connecting to the host: " + e.getMessage());
			return false;
		}
		return true;
	}
	
	public void sendMessage(String message) {
		try {
			// and write a message
			plugin.debug("writing '" + message + "' to connection...");
			outStream.writeBytes(message + "\n");
			// flush to ensure it gets sent
			outStream.flush();
		}
		catch (IOException e) {
			plugin.error("error sending chat: " + e.getMessage());
			// TODO: disconnect?
			disconnect();
		}
	}
	
	public void receiveMessage(String message) {
		// parse the message first
		String[] parts = message.split(":", 4);
		// part 0: world
		// part 1: channel
		// part 2: formatted name
		// part 3: message
		
		// make sure we receieved a valid message
		if(parts.length != 4) {
			plugin.debug("receieved bad message: " + message);
			return;
		}
		plugin.debug("received message: " + message);
		
		// TODO: actual chat
		// for now, just announce to all players on the server
		Player[] players = plugin.getServer().getOnlinePlayers();
		for(int i = 0; i < players.length; i++) {
			ColourHandler.sendMessage(players[i], parts[2] + ": " + parts[3]);
		}
	}
	
	public void disconnect() {
		// TODO: more disconnect stuff
		try {
			connection.close();
		}
		catch (IOException e) {
			plugin.error("error disconnecting from the host: " + e.getMessage());
		}
		
		// broadcast to the server
		Player[] players = plugin.getServer().getOnlinePlayers();
		for(int i = 0; i < players.length; i++) {
			ColourHandler.sendMessage(players[i], "&4This universe has lost its to the chat server!");
		}
	}
}