package mvc.model;
import gameItems.Deck;

import java.util.ArrayList;

import agents.Player;


public class GameSystem {
	private ArrayList<Player> players;
	private int turn;
	private Deck stock;
	private Deck discard;
	
	public GameSystem(ArrayList<Player> players) {
		this.players = players;
		this.turn = 0;
		this.stock = new Deck();
		//TODO populate stock
		this.discard = new Deck();
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public Deck getStock() {
		return stock;
	}

	public void setStock(Deck stock) {
		this.stock = stock;
	}

	public Deck getDiscard() {
		return discard;
	}

	public void setDiscard(Deck discard) {
		this.discard = discard;
	}
	
	
}
