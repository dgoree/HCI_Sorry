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
	private TerminalSpace[] startSpaces = new TerminalSpace[4];
	
	public GameSystem() {
		buildGameBoard();
		playGame();
	}
	
	public void buildGameBoard() {
		//create start spaces
		startSpaces[0] = new TerminalSpace(null, null, Color.RED, TerminalType.START);
		startSpaces[1] = new TerminalSpace(null, null, Color.BLUE, TerminalType.START);
		startSpaces[2] = new TerminalSpace(null, null, Color.YELLOW, TerminalType.START);
		startSpaces[3] = new TerminalSpace(null, null, Color.GREEN, TerminalType.START);
		
		//create and link 60 perimeter squares with no properties
		Space firstSpace = new Space(null,null);
		Space currentSpace = addSpace(firstSpace, null);
		for(int i=0;i<58;i++) {
			currentSpace = addSpace(currentSpace, null);
		}
		//close the loop
		currentSpace.setNext(firstSpace);
		firstSpace.setPrevious(currentSpace);
		
		//create safe zones
		//red
		Space firstSafeRed = new Space(null, null);
		Space currentSafe = addSafeSpace(firstSafeRed, null, Color.RED);
		for(int i=0;i<3;i++) {
			currentSafe = addSafeSpace(currentSafe, null, Color.RED);
		}
		TerminalSpace homeRed = new TerminalSpace(currentSafe, null, Color.RED, TerminalType.HOME);
		currentSafe.setSafeNext(homeRed);
		
		//blue
		Space firstSafeBlue = new Space(null, null);
		currentSafe = addSafeSpace(firstSafeBlue, null, Color.BLUE);
		for(int i=0;i<3;i++) {
			currentSafe = addSafeSpace(currentSafe, null, Color.BLUE);
		}
		TerminalSpace homeBlue = new TerminalSpace(currentSafe, null, Color.BLUE, TerminalType.HOME);
		currentSafe.setSafeNext(homeBlue);
		
		//yellow
		Space firstSafeYellow = new Space(null, null);
		currentSafe = addSafeSpace(firstSafeYellow, null, Color.YELLOW);
		for(int i=0;i<3;i++) {
			currentSafe = addSafeSpace(currentSafe, null, Color.YELLOW);
		}
		TerminalSpace homeYellow = new TerminalSpace(currentSafe, null, Color.YELLOW, TerminalType.HOME);
		currentSafe.setSafeNext(homeYellow);
		
		//green
		Space firstSafeGreen = new Space(null, null);
		currentSafe = addSafeSpace(firstSafeGreen, null, Color.GREEN);
		for(int i=0;i<3;i++) {
			currentSafe = addSafeSpace(currentSafe, null, Color.GREEN);
		}
		TerminalSpace homeGreen = new TerminalSpace(currentSafe, null, Color.GREEN, TerminalType.HOME);
		currentSafe.setSafeNext(homeGreen);
		
		Space[] safeZoneStartSpaces = {firstSafeRed, firstSafeBlue, firstSafeYellow, firstSafeGreen};
		
		//let firstSpace be the start of the red slide by red's home.
		//assign color, slide, and safeNext properties
		currentSpace = firstSpace;
		for(int color=0;color<4;color++) {
			//first slide
			currentSpace.setColor(Color.values()[color]);
			currentSpace.setSlideTo(currentSpace.getNext().getNext().getNext());
			//fork to safe zone
			currentSpace = currentSpace.getNext();
			currentSpace.setSafeNext(safeZoneStartSpaces[color]);
			safeZoneStartSpaces[color].setPrevious(currentSpace);
			//coming out space
			currentSpace = currentSpace.getNext().getNext();
			currentSpace.setStartPrevious(startSpaces[color]);
			startSpaces[color].setNext(currentSpace);
			//second slide
			currentSpace = currentSpace.getNext().getNext().getNext().getNext().getNext();
			currentSpace.setColor(Color.values()[color]);
			currentSpace.setSlideTo(currentSpace.getNext().getNext().getNext().getNext());
			//advance to next color
			for(int i=0;i<7;i++) {
				currentSpace = currentSpace.getNext();
			}
		}
	}
	
	public Space addSpace(Space prev, Space next) {
		 Space newSpace = new Space(prev, next);
		 if(prev != null) prev.setNext(newSpace);
		 if(next != null) next.setPrevious(newSpace);
		 return newSpace;
	}
	
	public Space addSafeSpace(Space prev, Space next, Color color) {
		 Space newSpace = new Space(prev, next, color);
		 if(prev != null) prev.setNext(newSpace);
		 if(next != null) next.setPrevious(newSpace);
		 return newSpace;
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
				//currently throws null pointer exception because findStartSpace() returns a blank space, baby
				tokens[player][token] = new Token(startSpaces[player], Color.values()[player]);
			}
			this.players.add(new Player(playerNames[player], Color.values()[player], tokens[player], startSpaces[player]));
		}
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
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
