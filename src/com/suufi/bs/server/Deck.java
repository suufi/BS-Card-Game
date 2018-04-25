package com.suufi.bs.server;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
	ArrayList<String> deck = new ArrayList<String>();
	
	public Deck() {
		
	}
	
	public void initialize() {
		for (int suit = 1; suit < 5; suit++) {
			for (int val = 1; val < 14; val++) {
				String suitLetter = null;
				if (suit == 1) suitLetter = "c";
				if (suit == 2) suitLetter = "d";
				if (suit == 3) suitLetter = "h";
				if (suit == 4) suitLetter = "s";
				
				deck.add(val + suitLetter);
			}
		}
	}
	
	public void shuffle() {
		Collections.shuffle(deck);
	}
	
	public String getCard(int i) {
		String card = deck.get(i);
	
		return card;
	}

	public ArrayList<String> getCards() {
		return this.deck;
	}
	
	public int length() {
		return deck.size();
	}
	
	public String drawCard() {
		shuffle();
		
		int deckIndex = (int) (Math.random() * length());
		String card = deck.get(deckIndex);
		deck.remove(deckIndex);
		
		return card;
	}
	
	public String drawCard(String targetCard) {
		boolean hasCard = deck.contains(targetCard);
		if (hasCard == true) {
			for (String card : deck) {
				if (card.equals(targetCard)) {
					deck.remove(card);
					return card;
				}
			}
		}
		
		return null;
	}
	
	public String drawCard(int index) {
		String card = deck.get(index);
		deck.remove(card);
		return card;
	}
	
	public ArrayList<String> drawCards(int quantity) {
		ArrayList<String> drawingCards = new ArrayList<String>();
		
		for (int i = 0; i < quantity; i++) {
			String card = drawCard();
			deck.remove(card);
			drawingCards.add(card);
		}
		
		return drawingCards;
	}
	
	public ArrayList<String> drawCards(ArrayList<String> cards) {
		ArrayList<String> drawingCards = new ArrayList<String>();
		
		for (String card : deck) {
			if (cards.contains(card)) {
				deck.remove(card);
				drawingCards.add(card);
			}
		}
		
		return drawingCards;
	}
	
	public ArrayList<String> drawTop(int quantity) {
		ArrayList<String> drawingCards = new ArrayList<String>();
		
		for (int i = 0; i < quantity; i++) {
			drawingCards.add(deck.get(i));
			deck.remove(i);
		}
		
		return drawingCards;
		
	}
	
	public void addCard(String card) {
		deck.add(card);
	}
	
	public void addCards(ArrayList<String> cards) {
		deck.addAll(cards);
	}
	
	public void removeCard(String card) {
		deck.remove(card);
	}
	
	public void removeCards(ArrayList<String> cards) {
		deck.removeAll(cards);
	}
	
	public boolean contains(String card) {
		if (deck.contains(card)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean contains(ArrayList<String> cards) {
		if (deck.containsAll(cards)) {
			return true;
		} else {
			return false;
		}
	}

}