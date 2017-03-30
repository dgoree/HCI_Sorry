package mvc.model;
import gameItems.Card;
import gameItems.Deck;
import gameItems.Token;
import spaces.Space;
import spaces.TerminalSpace;
import utilities.Color;
import utilities.TerminalType;

import java.util.ArrayList;
import java.util.UUID;

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
	
	//should be called if the user chooses New Game from the menu
	public void playGame() {
		setupGame();
		
		//play until someone wins
		boolean gameOver = false;
		while(!gameOver) {
			gameOver = takeTurn();
		}
	}
	
	public void setupGame() {
		this.turn = 0;
		this.stock = new Deck(true);
		this.discard = new Deck(false);
		setupPlayers();
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	//setup names and place all tokens in respective starts
	public void setupPlayers() {
		Token[][] tokens = new Token[4][4]; //player/color, token number
		
		for(int player=0;player<4;player++) {
			for(int token=0;token<4;token++) {
				tokens[player][token] = new Token(getStart(Color.values()[player]), Color.values()[player]);
			}
			this.players.add(new Player(playerNames[player], Color.values()[player], tokens[player]));
		}
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
	
	//get the start space for a given color
	public TerminalSpace getStart(Color c) {
		//TODO
		return new TerminalSpace();
	}
	
	//return true if the game is over
	public boolean takeTurn() {
		//draw a card
		Card thisCard = stock.remove(0);
		discard.add(thisCard);
		if(stock.isEmpty()) {
			stock = discard;
			stock.shuffle();
			discard.clear();
		}
		
		//calculate all available move options
		for(int token=0;token<4;token++) {
			players.get(turn).getMoves(players, thisCard.getNumber());
		}
		
		//TODO: display options and move the token of the player's choosing
		
		//end game, or go to next turn
		if(checkGameOver(turn)) {
			//TODO: display some kind of congratulatory message
			return true;
		}
		else if(++turn >= 4) {
			turn=0;
		}
		return false;
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
	
	//the game is over if all of this player's tokens are in home
	public boolean checkGameOver(int player) {
		boolean gameOver = true;
		Token t;
		for(int token=0;token<4;token++) {
			t = players.get(player).getTokens()[token]; 
			gameOver = t.inHome();
		}
		return gameOver;
	}
}
