package mvc.model;
import gameItems.Card;
import gameItems.Deck;
import gameItems.Token;
import spaces.Space;
import spaces.TerminalSpace;
import utilities.Color;
import utilities.TerminalType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import agents.Player;


public class GameSystem {
	private String[] playerNames = {"Caleb", "Daniel", "Maria", "Ryan"}; //user configurable
	private ArrayList<Player> players = new ArrayList<Player>();
	private int turn;
	private Deck stock;
	private Deck discard;
	private UUID[] startSpaces = new UUID[4];
	private HashMap<UUID, Space> hashMap = new HashMap<UUID, Space>();
	
	public GameSystem() {
		buildGameBoard();
		//playGame(); //disabled for now because there are errors in findSimpleMoves()
	}
	
	public void buildGameBoard() {
		//create start spaces
		TerminalSpace ts;
		for(int i=0;i<4;i++) {
			ts = new TerminalSpace(null, null, Color.values()[i], TerminalType.START, true); 
			startSpaces[i] = ts.getId();
			hashMap.put(ts.getId(), ts);
		}
		
		//create and link 60 perimeter squares with no properties
		Space firstSpace = new Space(null,null, true);
		hashMap.put(firstSpace.getId(), firstSpace);
		Space currentSpace = addSpace(firstSpace.getId(), null);
		for(int i=0;i<58;i++) {
			currentSpace = addSpace(currentSpace.getId(), null);
		}
		//close the loop
		currentSpace.setNextID(firstSpace.getId());
		firstSpace.setPreviousID(currentSpace.getId());
		
		//create safe zones
		//red
		Space firstSafeRed = new Space(null, null, true);
		hashMap.put(firstSafeRed.getId(), firstSafeRed);
		Space currentSafe = addSafeSpace(firstSafeRed.getId(), null, Color.RED);
		for(int i=0;i<3;i++) {
			currentSafe = addSafeSpace(currentSafe.getId(), null, Color.RED);
		}
		TerminalSpace homeRed = new TerminalSpace(currentSafe, null, Color.RED, TerminalType.HOME);
		currentSafe.setSafeNextID(homeRed.getId());
		hashMap.put(homeRed.getId(), homeRed);
		
		//blue
		Space firstSafeBlue = new Space(null, null, true);
		hashMap.put(firstSafeBlue.getId(), firstSafeBlue);
		currentSafe = addSafeSpace(firstSafeBlue.getId(), null, Color.BLUE);
		for(int i=0;i<3;i++) {
			currentSafe = addSafeSpace(currentSafe.getId(), null, Color.BLUE);
		}
		TerminalSpace homeBlue = new TerminalSpace(currentSafe, null, Color.BLUE, TerminalType.HOME);
		currentSafe.setSafeNextID(homeBlue.getId());
		hashMap.put(homeBlue.getId(), homeBlue);
		
		//yellow
		Space firstSafeYellow = new Space(null, null, true);
		hashMap.put(firstSafeYellow.getId(), firstSafeYellow);
		currentSafe = addSafeSpace(firstSafeYellow.getId(), null, Color.YELLOW);
		for(int i=0;i<3;i++) {
			currentSafe = addSafeSpace(currentSafe.getId(), null, Color.YELLOW);
		}
		TerminalSpace homeYellow = new TerminalSpace(currentSafe, null, Color.YELLOW, TerminalType.HOME);
		currentSafe.setSafeNextID(homeYellow.getId());
		hashMap.put(homeYellow.getId(), homeYellow);
		
		//green
		Space firstSafeGreen = new Space(null, null, true);
		hashMap.put(firstSafeGreen.getId(), firstSafeGreen);
		currentSafe = addSafeSpace(firstSafeGreen.getId(), null, Color.GREEN);
		for(int i=0;i<3;i++) {
			currentSafe = addSafeSpace(currentSafe.getId(), null, Color.GREEN);
		}
		TerminalSpace homeGreen = new TerminalSpace(currentSafe, null, Color.GREEN, TerminalType.HOME);
		currentSafe.setSafeNextID(homeGreen.getId());
		hashMap.put(homeGreen.getId(), homeGreen);
		
		UUID[] safeZoneStartSpaces = {firstSafeRed.getId(), firstSafeBlue.getId(), firstSafeYellow.getId(), firstSafeGreen.getId()};
		
		//let firstSpace be the start of the red slide by red's home.
		//assign color, slide, and safeNext properties
		currentSpace = firstSpace;
		for(int color=0;color<4;color++) {
			//first slide
			currentSpace.setColor(Color.values()[color]);
			currentSpace.setSlideToID(hashMap.get(hashMap.get(hashMap.get(currentSpace.getNextID()).getNextID()).getNextID()).getId());
			//fork to safe zone
			currentSpace = hashMap.get(currentSpace.getNextID());
			currentSpace.setSafeNextID(safeZoneStartSpaces[color]);
			hashMap.get(safeZoneStartSpaces[color]).setPreviousID(currentSpace.getId());
			//coming out space
			currentSpace = hashMap.get(hashMap.get(currentSpace.getNextID()).getNextID());
			currentSpace.setStartPreviousID(startSpaces[color]);
			Space debug = hashMap.get(startSpaces[color]);
			hashMap.get(startSpaces[color]).setNextID(currentSpace.getId());
			debug = hashMap.get(startSpaces[color]);
			//second slide
			for(int i=0;i<5;i++) {
				currentSpace = hashMap.get(currentSpace.getNextID());
			}
			currentSpace.setColor(Color.values()[color]);
			currentSpace.setSlideToID(hashMap.get(hashMap.get(hashMap.get(hashMap.get(currentSpace.getNextID()).getNextID()).getNextID()).getNextID()).getId());
			//advance to next color
			for(int i=0;i<7;i++) {
				currentSpace = hashMap.get(currentSpace.getNextID());
			}
		}
	}
	
	public Space addSpace(UUID prev, UUID next) {
		 Space newSpace = new Space(prev, next, true);
		 hashMap.put(newSpace.getId(), newSpace);
		 if(prev != null) hashMap.get(prev).setNextID(newSpace.getId());
		 if(next != null) hashMap.get(next).setPreviousID(newSpace.getId());
		 return newSpace;
	}
	
	public Space addSpace(Space prev, Space next) {
		 Space newSpace = new Space(prev, next);
		 hashMap.put(newSpace.getId(), newSpace);
		 if(prev != null) prev.setNext(newSpace);
		 if(next != null) next.setPrevious(newSpace);
		 return newSpace;
	}
	
	public Space addSafeSpace(UUID prev, UUID next, Color color) {
		 Space newSpace = new Space(prev, next, color);
		 hashMap.put(newSpace.getId(), newSpace);
		 if(prev != null) hashMap.get(prev).setNextID(newSpace.getId());
		 if(next != null) hashMap.get(next).setPreviousID(newSpace.getId());
		 return newSpace;
	}
	
	public Space addSafeSpace(Space prev, Space next, Color color) {
		 Space newSpace = new Space(prev, next, color);
		 hashMap.put(newSpace.getId(), newSpace);
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
		Player p;
		
		for(int player=0;player<4;player++) {
			for(int token=0;token<4;token++) {
				tokens[player][token] = new Token(hashMap.get(startSpaces[player]), Color.values()[player]);
			}
			p = new Player(playerNames[player], Color.values()[player], tokens[player], hashMap.get(startSpaces[player]), hashMap);
			players.add(p);
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
