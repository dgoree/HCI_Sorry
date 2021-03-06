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
import java.util.List;
import java.util.UUID;

import agents.Player;


public class GameSystem {
	private String[] playerNames = {"Caleb", "Daniel", "Maria", "Ryan"}; //user configurable
	private ArrayList<Player> players = new ArrayList<Player>();
	private int turn;
	private Deck stock;
	private Deck discard;
	private UUID[] startSpaces = new UUID[4];
	private UUID[] safeZoneStartSpaces = new UUID[4];
	private UUID[] homeSpaces = new UUID[4];
	private HashMap<UUID, Space> hashMap = new HashMap<UUID, Space>();
	private Card thisCard;
	private boolean showCard;
	private boolean gameInProgress;
	private boolean noPossibleMoves = false;
	private boolean changeNameMenu = false;
	private boolean secondSevenMove = false;
	private int sevenMovesRemaining = 0;
	private List<Listener> listeners = new ArrayList<Listener>();
		
	public GameSystem() {
		this.gameInProgress = false;
		buildGameBoard();
	}
	
	public void buildGameBoard() {
		//create start spaces
		TerminalSpace ts;
		for(int i=0;i<4;i++) {
			ts = new TerminalSpace(null, null, Color.values()[i], TerminalType.START); 
			startSpaces[i] = ts.getId();
			hashMap.put(ts.getId(), ts);
		}
		
		//create and link 60 perimeter squares with no properties
		Space firstSpace = new Space(null,null);
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
		Space firstSafeRed = new Space(null, null, Color.RED);
		hashMap.put(firstSafeRed.getId(), firstSafeRed);
		Space currentSafe = addSafeSpace(firstSafeRed.getId(), null, Color.RED);
		for(int i=0;i<3;i++) {
			currentSafe = addSafeSpace(currentSafe.getId(), null, Color.RED);
		}
		TerminalSpace homeRed = new TerminalSpace(currentSafe.getId(), null, Color.RED, TerminalType.HOME);
		currentSafe.setSafeNextID(homeRed.getId());
		hashMap.put(homeRed.getId(), homeRed);
		homeSpaces[0] = homeRed.getId();
		
		//blue
		Space firstSafeBlue = new Space(null, null, Color.BLUE);
		hashMap.put(firstSafeBlue.getId(), firstSafeBlue);
		currentSafe = addSafeSpace(firstSafeBlue.getId(), null, Color.BLUE);
		for(int i=0;i<3;i++) {
			currentSafe = addSafeSpace(currentSafe.getId(), null, Color.BLUE);
		}
		TerminalSpace homeBlue = new TerminalSpace(currentSafe.getId(), null, Color.BLUE, TerminalType.HOME);
		currentSafe.setSafeNextID(homeBlue.getId());
		hashMap.put(homeBlue.getId(), homeBlue);
		homeSpaces[1] = homeBlue.getId();
		
		//yellow
		Space firstSafeYellow = new Space(null, null, Color.YELLOW);
		hashMap.put(firstSafeYellow.getId(), firstSafeYellow);
		currentSafe = addSafeSpace(firstSafeYellow.getId(), null, Color.YELLOW);
		for(int i=0;i<3;i++) {
			currentSafe = addSafeSpace(currentSafe.getId(), null, Color.YELLOW);
		}
		TerminalSpace homeYellow = new TerminalSpace(currentSafe.getId(), null, Color.YELLOW, TerminalType.HOME);
		currentSafe.setSafeNextID(homeYellow.getId());
		hashMap.put(homeYellow.getId(), homeYellow);
		homeSpaces[2] = homeYellow.getId();
		
		//green
		Space firstSafeGreen = new Space(null, null, Color.GREEN);
		hashMap.put(firstSafeGreen.getId(), firstSafeGreen);
		currentSafe = addSafeSpace(firstSafeGreen.getId(), null, Color.GREEN);
		for(int i=0;i<3;i++) {
			currentSafe = addSafeSpace(currentSafe.getId(), null, Color.GREEN);
		}
		TerminalSpace homeGreen = new TerminalSpace(currentSafe.getId(), null, Color.GREEN, TerminalType.HOME);
		currentSafe.setSafeNextID(homeGreen.getId());
		hashMap.put(homeGreen.getId(), homeGreen);
		homeSpaces[3] = homeGreen.getId();
		
		safeZoneStartSpaces[0] = firstSafeRed.getId();
		safeZoneStartSpaces[1] = firstSafeBlue.getId();
		safeZoneStartSpaces[2] = firstSafeYellow.getId();
		safeZoneStartSpaces[3] = firstSafeGreen.getId();
		
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
			hashMap.get(startSpaces[color]).setNextID(currentSpace.getId());
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
		 Space newSpace = new Space(prev, next);
		 hashMap.put(newSpace.getId(), newSpace);
		 if(prev != null) hashMap.get(prev).setNextID(newSpace.getId());
		 if(next != null) hashMap.get(next).setPreviousID(newSpace.getId());
		 return newSpace;
	}
	
	public Space addSafeSpace(UUID prev, UUID next, Color color) {
		 Space newSpace = new Space(prev, next, color);
		 hashMap.put(newSpace.getId(), newSpace);
		 if(prev != null) hashMap.get(prev).setSafeNextID(newSpace.getId());
		 if(next != null) hashMap.get(next).setPreviousID(newSpace.getId());
		 return newSpace;
	}
	
	public void newGame() {
		this.turn = 0;
		this.stock = new Deck(true);
		this.discard = new Deck(false);
		setupPlayers();
		gameInProgress = true;
		notifyListeners();
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	//setup names and place all tokens in respective starts
	public void setupPlayers() {
		players.clear(); //all data needs to be erased in case this is not the first game
		Token[][] tokens = new Token[4][4]; //player/color, token number
		Player p;
		
		for(int player=0;player<4;player++) {
			for(int token=0;token<4;token++) {
				tokens[player][token] = new Token(startSpaces[player], Color.values()[player]);
			}
			p = new Player(playerNames[player], Color.values()[player], tokens[player], startSpaces[player], hashMap);
			players.add(p);
		}
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
	
	
	//Return true if there is an available move
	public boolean drawCard() {
		//draw a card and manage the deck
		thisCard = stock.remove(0);
		//Maria made a couple changes here to make the card number more easily accessible to the view.
		//I made thisCard a class variable with a getter.
		showCard = true;
		notifyListeners();//This is Maria's addition.  We might want it further down in the method, 
							//but I stuck it here for now.

		discard.add(thisCard);
		if(stock.isEmpty()) {
			stock = new Deck(true);
			discard.clear();
		}
		
		//calculate all available move options
		players.get(turn).calcMoves(players, thisCard.getNumber(), secondSevenMove);
		
		//determine if there are any valid moves
		ArrayList<UUID> moveOptions;
		for(int token=0;token<4;token++) {
			moveOptions = players.get(turn).getTokens()[token].getMoves();
			
			if(!moveOptions.isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	//move a token to a new space. Return the ID of the final destination (will change if slides happen)
	public void moveToken(Token token, UUID destination) {
		//handle swaps
		if(thisCard.getNumber() == 11) {
			if(hashMap.get(destination).getOccupant(players) != null) {
				hashMap.get(destination).getOccupant(players).setSpaceID(token.getSpaceID());
			}
		}
		//handle sevens - determine if the player will be moving a second token
		if(thisCard.getNumber() == 7) {
			if(secondSevenMove) {
				secondSevenMove = false;
			}
			else {
				UUID temp = token.getSpaceID();
				int moveCount = 0;
				while(temp != destination) {
					//first try to go to a safeNext
					if(hashMap.get(temp).getSafeNextID() != null && hashMap.get(hashMap.get(temp).getSafeNextID()).getDefaultColor() == Color.values()[turn]) {
						temp = hashMap.get(temp).getSafeNextID();
					}
					//go to an ordinary next is safeNext is invalid
					else {
						temp = hashMap.get(temp).getNextID();
					}
					moveCount++;
				}
				if(moveCount != 7) {
					secondSevenMove = true;
					sevenMovesRemaining = 7 - moveCount;
				}
			}
		}
		
		
		//remove any players occupying destination space
		evict(destination);
		hashMap.get(destination).setIcon(null); 
		//perform slide if necessary, changing destination
		UUID slideToID = hashMap.get(destination).getSlideToID(); 
		if((slideToID != null) && (hashMap.get(destination).getColor() != Color.values()[turn])) {
			while(destination != slideToID) {
				//remove tokens and their icons from the slide
				destination = hashMap.get(destination).getNextID();
				evict(destination);
				hashMap.get(destination).setIcon(null);
			}
		}
		//move token to proper space
		token.setSpaceID(destination);
		
		notifyListeners();
		if(secondSevenMove) { 
			players.get(turn).calcMoves(players, sevenMovesRemaining, secondSevenMove);
		}
		else checkGameOver();
	}
	
	//remove any tokens occupying the space with this id.
	//Note: logic in calcMoves() should prevent a player from landing on his own pawn, except on slides.
	public void evict(UUID id) {
		for(int player=0; player<4; player++) {
			for(int tok=0; tok<4; tok++) {
				//bump back to start if occupied and not in home
				if((players.get(player).getTokens()[tok].getSpaceID() == id) && !inHome(players.get(player).getTokens()[tok])) {
					players.get(player).getTokens()[tok].setSpaceID(startSpaces[player]);
				}
			}
		}
	}
	
	//creates an array of all the UUIDs used (except start spaces)
	//indices 0-59: IDs of perimeter squares, starting at top left corner and going clockwise
	//indices 60-83: IDs of safe zones, from fork to home, in this order: Y, G, R, B
	public UUID[] getSpaceIDs() {
		UUID[] ids = new UUID[84];
		UUID id = startSpaces[1]; //blue start space
		//iterate to to left corner
		for(int i=0; i<12; i++) {
			id = hashMap.get(id).getNextID();
		}
		//add 60 perimeter IDs to the array
		for(int i=0; i<60; i++) {
			ids[i] = id;
			id = hashMap.get(id).getNextID();
		}
		//add safe zone IDs to the array
		int[] safeZoneOrder = {2,3,0,1}; //Y,G,R,B
		int index = 60;
		for(int i:safeZoneOrder) {
			id = safeZoneStartSpaces[i];
			ids[index++] = id;
			for(int j=0; j<5; j++) {
				id = hashMap.get(id).getSafeNextID();
				ids[index++] = id;
			}
		}
		return ids;
	}
	
	public UUID[] getStartIDs() {
		return startSpaces;
	}
	
	public UUID[] getSafeZoneStartSpaces() {
		return safeZoneStartSpaces;
	}
	
	public UUID[] getHomeIDs() {
		return homeSpaces;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}
	
	public void advanceTurn() 
	{
		setShowCard(false);
		//go to next player's turn unless a 2 was played
		if(thisCard.getNumber() != 2) {
			turn = (turn + 1) % 4;
		}
		notifyListeners();
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
	public void checkGameOver() {
		Token t;
		for(int token=0;token<4;token++) {
			t = players.get(turn).getTokens()[token]; 
			if(!inHome(t)) {
				return;
			}
		}
		//if(debug_s < 2)
		//System.out.println("P" + turn + " Start: " + debug_s);
		//if(debug_h > 0) System.out.println("P" + turn + " Home: " + debug_h);
		gameInProgress = false;
	}
	
	public boolean inHome(Token t) {
		return ((hashMap.get(t.getSpaceID()) instanceof TerminalSpace) && (((TerminalSpace)hashMap.get(t.getSpaceID())).getType() == TerminalType.HOME));		
	}
	
	public boolean inStart(Token t) {
		return ((hashMap.get(t.getSpaceID()) instanceof TerminalSpace) && (((TerminalSpace)hashMap.get(t.getSpaceID())).getType() == TerminalType.START));		
	}
	
	public int getCardNum()
	{
		return thisCard.getNumber();
	}
	
	//This method tells the view whether to show a button or to show the card
	public boolean getShowCard()
	{
		return showCard;
	}
	
	//Ryan
	public void setShowCard(boolean showCard)
	{
		this.showCard = showCard;
		notifyListeners();
	}
	
	public void setSecondSevenMove(boolean ssm) {
		secondSevenMove = ssm;
	}
	
	public boolean isSecondSevenMove() {
		return secondSevenMove;
	}
	
	public boolean isGameInProgress()
	{
		return gameInProgress;
	}
	
	public Space getSpace(UUID id)
	{
		return hashMap.get(id);
	}
	
	//TODO test me
	public int getDistance(Space space1, Space space2) {
		if(space1.equals(space2))
			return 0;		
		return 1 + getDistance(getSpace(space1.getNextID()), space2);
	}
	

	//TODO test me
	public UUID getSpacesAway(Space space, int distance) {
		if(distance == 0)
			return space.getId();
		else if(getSpace(space.getNextID()) == null) {
			if(getSpace(space.getSafeNextID()) == null) {
				return null;
			}
			else if(distance == 1)
				return space.getSafeNextID();
			else
				return getSpacesAway(getSpace(space.getSafeNextID()), distance-1);
		}
		else if(distance == 1) {
			return space.getNextID();
		}
		else
			return  getSpacesAway(getSpace(space.getNextID()), distance-1);
	}
	
	public HashMap<UUID, Space> getHashMap() {
		return hashMap;
	}
	
	//Ryan
	public boolean getNoPossibleMoves()
	{
		return noPossibleMoves;
	}
	
	//Ryan
	public void setNoPossibleMoves(boolean noPossibleMoves)
	{
		this.noPossibleMoves = noPossibleMoves;
		notifyListeners();
	}
	
	//Maria
	public String getPlayerName()
	{
		return playerNames[turn];
	}
	
	//Maria
	public String getPlayerName(int playerNum)
	{
		return playerNames[playerNum];
	}
	
	//Maria
	public void setPlayerNames(String name, int playerNum)
	{
		playerNames[playerNum] = name;
	}
	
	//Maria
	public void setChangeNameMenu()
	{
		changeNameMenu = true;
	}
	
	//Maria
	public boolean getChangeNameMenu()
	{
		boolean answer = changeNameMenu;
		changeNameMenu = false;
		return answer;
	}
	
	public int[] getTokensInStart() {
		int[] array = new int[4];
		int numStart = 0;
		for(int player = 0; player<4; player++) {
			numStart = 0;
			for(int token=0; token<4; token++) {
				if(inStart(players.get(player).getTokens()[token])) numStart++;
			}
			array[player] = numStart;
		}
		return array;
	}
	
	public int[] getTokensInHome() {
		int[] array = new int[4];
		int numHome = 0;
		for(int player = 0; player<4; player++) {
			numHome = 0;
			for(int token=0; token<4; token++) {
				if(inHome(players.get(player).getTokens()[token])) numHome++;
			}
			array[player] = numHome;
		}
		return array;
	}

	public void addListener(final Listener listener)
	{
		listeners.add(listener);
	}
	
	//Tells view to update
	public void notifyListeners()
	{
		for(final Listener listener:listeners)
		{
			listener.updated();
		}
	}
}