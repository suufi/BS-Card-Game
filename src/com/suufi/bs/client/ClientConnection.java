package com.suufi.bs.client;
import java.io.*;
import java.net.*;
import java.util.*;

public class ClientConnection extends Thread {
	private Socket socket;
	private BufferedReader in; // what comes from the server
	private PrintWriter out;   // what is sent to the server
	private Client client;
	private boolean isTurn;
	
	public ClientConnection(String host) throws IOException {
		this.socket = new Socket(host, 9009);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
		client = new Client(this);
	    start();
	}
	
	public void run() {
		try {
			while (true) {
				String line = in.readLine();
				client.log(line);
				
				String[] data = line.split(" ");
				
				if (data[0].equals("deal")) {
					data = data[1].split("(?<=\\d)(?=\\D)");
					
					System.out.println(data[0]);
					System.out.println(data[1]);
					
					String cardId = data[0] + data[1] + ".gif";
					client.log(cardId);
					client.initHand(cardId);
				}
				
				else if (data[0].equals("delt")) {
					client.updateHandSize();
					client.displayCards();
				}
				
				else if (data[0].equals("name")) {
					client.setName(data[1]);
					client.log(data[1]);
				}
				
				else if (data[0].equals("pile")) {
					client.updatePileSize(data[1]);
				}
				
				else if (data[0].equals("turn")) {
					isTurn = true;
					client.getSubmitButton().setEnabled(true);
					
					if (data[1].equals("1")) {
						client.showDialog("It's your turn. You're Aces.");
					} else if (data[1].equals("11")) {
						client.showDialog("It's your turn. You're Jacks.");
					} else if (data[1].equals("12")) {
						client.showDialog("It's your turn. You're Queens.");
					} else if (data[1].equals("13")) {
						client.showDialog("It's your turn. You're Kings.");
					} else {
						client.showDialog("It's your turn. You're " + data[1] + "s.");
					}
				}
				
				else if (data[0].equals("bsfl")) {
					client.showDialog("BS called incorrectly. Discard pile given to " + data[1] + ".");
				}
				
				else if (data[0].equals("bsco")) {
					client.showDialog("BS called correctly. Discard pile given to " + data[1] + ".");
				}
				
				else if (data[0].equals("winr")) {
					client.showDialog("Congratulations! You won! Click OK to close.");
			        System.exit(0);
				}
				
				else if (data[0].equals("wins")) {
					client.showDialog(data[1] + " Wins!");
			         System.exit(0);
				}
				
				// plyd Mohamed 11 3
				else if (data[0].equals("plyd")) {
					boolean plural = Integer.parseInt(data[2]) > 1;
					
					if (data[2].equals("1")) {
						client.showDialog((plural) ? "There were " + data[3] + " aces played by " + data[1] +"." : "There was " + data[3] + " ace played by " + data[1] +".");
					} else if (data[2].equals("11")) {
						client.showDialog((plural) ? "There were " + data[3] + " jacks played by " + data[1] +"." : "There was " + data[3] + " jack played by " + data[1] +".");
					} else if (data[2].equals("12")) {
						client.showDialog((plural) ? "There were " + data[3] + " queens played by " + data[1] +"." : "There was " + data[3] + " queen played by " + data[1] +".");
					} else if (data[2].equals("13")) {
						client.showDialog((plural) ? "There were " + data[3] + " kings played by " + data[1] +"." : "There was " + data[3] + " king played by " + data[1] +".");
					} else {
						client.showDialog((plural) ? "There were " + data[3] + " " + data[2] + "s played by " + data[1] +"." : "There was " + data[3] + " " + data[2] + " played by " + data[1] +".");
					}
				}
				
			}
		} catch(IOException error) {
			System.out.print(error);
		}
	}
	
	public boolean checkIfTurn() {
		return isTurn;
	}
	
	public void sendBS() {
		out.println("bsbs");
	}

	public void setUsername(String username) {
	    out.println("name "+username);
	}
	
	public void submit(ArrayList<ClickableCard> submittedCards) {
	    String totalList = "";
	    for(int i = 0; i<submittedCards.size(); i++)
	    {
	      totalList = totalList + submittedCards.get(i).getCardValue() + " " + submittedCards.get(i).getSuit()+ " ";
	    }
	    client.updateHandSize();
	    client.log(totalList);
	    out.println("play " + totalList);
	    client.clearSubmitted();
	}
}
