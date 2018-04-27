package com.suufi.bs.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

public class Server {

	private GUI gui;

	private static final int portNumber = 9009;

	ServerSocket serverSocket = null;
	boolean listeningSocket = true;
	int playerCount = 0;
	ArrayList<Socket> playerSockets = new ArrayList<>();
	int currentPlayerIndex;

	public Server(GUI gui) {
		this.gui = gui;
	}

	public void start() throws IOException {
		if (serverSocket == null) {
			try {
				// starting server
				gui.log("Server starting on port " + portNumber);
				serverSocket = new ServerSocket(portNumber);
				gui.log("Server is now live @ " + InetAddress.getLocalHost().getHostAddress() + ":"
						+ serverSocket.getLocalPort());
				// client connecting
				gui.log("Waiting for clients to connect");
			} catch (Exception e) {
				gui.log(e.toString());
			}
			// while the socket is listening
			while (listeningSocket) {
				if (playerCount < 4) {
					// accept all incoming socket connections as a clientSocket
					Socket clientSocket = serverSocket.accept();
					gui.log("Client has connected.");

					// create a "MiniServer" off of the clientSocket and start it
					MiniServer mini = new MiniServer(clientSocket, gui);
					mini.start();

					// send message to the client
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
					bw.write("Welcome to BS! The game needs 4 players to start.");
					bw.newLine();
					bw.flush();

					playerCount++;
					playerSockets.add(clientSocket);
				} else {
					// accept all incoming socket connections as a clientSocket
					Socket clientSocket = serverSocket.accept();
					gui.log("Extraneous client has connected.");

					// tell client only 4 per server
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
					bw.write("Welcome to BS! The game only permits 4 people per server. Sorry!");
					bw.newLine();
					bw.flush();

					clientSocket.close();
				}
			}
		} else {
			listeningSocket = true;
			gui.log("Server started");
		}

	}

	public void stop() throws IOException {
		if (listeningSocket == true) {
			listeningSocket = false;
		} else {
			gui.log("Can't stop what's not running!");
		}
	}

	public void startGame() throws IOException {
		int startingPlayerIndex = 0;

		Deck deck = new Deck();
		deck.initialize();

		for (Socket player : playerSockets) {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(player.getOutputStream()));

			ArrayList<String> cards = deck.drawCards(deck.length() / playerCount);
			System.out.println(cards.toString());

			for (String card : cards) {
				if (card == "1s") {
					startingPlayerIndex = playerSockets.indexOf(player);
				}
				gui.log("dealing" + card);
				bw.write("deal " + card);
				bw.newLine();
			}

			bw.write("delt");
			bw.newLine();
			bw.flush();
		}

		Collections.swap(playerSockets, startingPlayerIndex, 0);

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(playerSockets.get(0).getOutputStream()));
		bw.write("turn 1");
		bw.newLine();
		bw.flush();
		
		currentPlayerIndex = 0;
	}
	
	public void play() throws IOException {
		
	}
	
	public void turnNext() throws IOException {
		if (currentPlayerIndex < playerCount) currentPlayerIndex++;
		
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(playerSockets.get(currentPlayerIndex).getOutputStream()));
		bw.write("turn");
		// TODO: make it so that it displays to the player the current card as it is a required argument of turn
		bw.newLine();
		bw.flush();		
		
	}
}
