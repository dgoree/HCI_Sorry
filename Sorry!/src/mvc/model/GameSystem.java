package mvc.model;
import gameItems.Deck;
import gameItems.Token;
import spaces.TerminalSpace;
import utilities.Color;

import java.util.ArrayList;

import agents.Player;


public class GameSystem {
	private String[] playerNames = {"Caleb", "Daniel", "Maria", "Ryan"}; //user configurable
	private ArrayList<Player> players;
	private int turn;
	private Deck stock;
	private Deck discard;
	
	public GameSystem() {
		buildGameBoard();
		playGame();
	}
	
	public void buildGameBoard() {
		//TODO
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public void setupPlayers() {
		Token[][] tokens = new Token[4][4]; //player/color, token number
		
		for(int color=0;color<4;color++) {
			for(int token=0;token<4;token++) {
				tokens[color][token] = new Token(getHome(Color.values()[color]), Color.values()[color]);
			}
		}
		
		for(int player=0;player<4;player++) {
			this.players.add(new Player(playerNames[player], Color.values()[player], tokens[player]));		
		}
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
	
	//get the home space for a given color
	public TerminalSpace getHome(Color c) {
		//TODO
		return new TerminalSpace();
	}
	
	//TODO
	public void playGame() {
		setupGame();
	}
	
	public void setupGame() {
		this.turn = 0;
		this.stock = new Deck(true);
		this.discard = new Deck(false);
		setupPlayers();
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
