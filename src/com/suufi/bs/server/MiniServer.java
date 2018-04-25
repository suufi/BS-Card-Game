package com.suufi.bs.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class MiniServer extends Thread {

	private Socket socket = null;
	private GUI gui;
	private String playerName = "";

	public MiniServer(Socket socket, GUI gui) {

		super("MiniServer");
		this.socket = socket;
		this.gui = gui;
	}

	public void run() {
		// Receive message from client
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String data;
			while ((data = br.readLine()) != null) {
				if (data.contains("name")) {
					String[] parts = data.split(" ");
					this.playerName = parts[1];
				}
				gui.log("Message from " + playerName + ": " + data);
			}
		} catch (IOException error) {
			System.out.println(error);
		}
	}
}