package agents;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import gameItems.Token;
import spaces.Space;
import spaces.TerminalSpace;
import utilities.Color;
import utilities.TerminalType;


public class Player {
	private String name;
	private Color color;
	private Token[] tokens;
	private UUID startSpace;
	private HashMap<UUID, Space> hashMap;
	
	public Player(String name, Color color, Token[] tokens, UUID startSpace, HashMap<UUID, Space> hashMap) {
		this.name = name;
		this.color = color;
		this.tokens = tokens;
		this.startSpace = startSpace;
		this.hashMap = hashMap;
	}
	
	public void calcMoves(ArrayList<Player> players, int cardNumber) {
		
		//sevens can't be handled individually
		if(cardNumber == 7) {
			findSplitMoves();
		}
		
		else for(Token t:tokens) {
			switch(cardNumber) {
			case 0: //sorry card
				//requires token to be in start
				if(t.getSpaceID() == startSpace) {
					//move to any non-safe space containing an opponent's token
					t.setMoves(findOpponentTokens(players));
				}
				break;
			case 1:
				//move forward 1
				t.setMoves(findSimpleMoves(t,cardNumber,true));
				//move from start
				if(t.getSpaceID() == startSpace) {
					t.addMove(hashMap.get(t.getSpaceID()).getNextID());
				}
				break;
			case 2:
				//move forward 2
				t.setMoves(findSimpleMoves(t,cardNumber,true));
				//move from start
				if(t.getSpaceID() == startSpace) {
					t.addMove(hashMap.get(t.getSpaceID()).getNextID());
				}
				break;
			case 3:
				//move 3
				t.setMoves(findSimpleMoves(t,cardNumber,true));
				break;
			case 4:
				//back 4
				t.setMoves(findSimpleMoves(t,cardNumber,false));
				break;
			case 5:
				//move 5
				t.setMoves(findSimpleMoves(t,cardNumber,true));
				break;
			case 8:
				//move 8
				t.setMoves(findSimpleMoves(t,cardNumber,true));
				break;
			case 10:
				//move 10
				t.setMoves(findSimpleMoves(t,cardNumber,true));
				//move back 1
				t.addMoves(findSimpleMoves(t, 1, false));
				break;
			case 11:
				//move 11
				t.setMoves(findSimpleMoves(t,cardNumber,true));
				//swap with opponent (requires token not to be in start or safety zone)
				if(t.getSpaceID() != startSpace && !hashMap.get(t.getSpaceID()).isSafe()) {
					t.addMoves(findOpponentTokens(players));
				}
				break;
			case 12:
				//move 12
				t.setMoves(findSimpleMoves(t,cardNumber,true));
				break;
			}
		}
	}
	
	//direction: true = forwards, false = backwards
	public ArrayList<UUID> findSimpleMoves(Token t, int numMoves, boolean direction) {
		Space space = hashMap.get(t.getSpaceID());
		ArrayList<UUID> moveOptions = new ArrayList<UUID>();
		if(space instanceof TerminalSpace) return moveOptions; //can't move if in start or home
		int count = 0;
		//forwards
		if(direction) {
			for(int moves=0;moves<numMoves;moves++) {
				if(space.getSafeNextID() != null && hashMap.get(space.getSafeNextID()).getColor() == color) {
					space = hashMap.get(space.getSafeNextID());
					count++;
				}
				else if(space.getNextID() != null) {
					space = hashMap.get(space.getNextID());
					count++;
				}
			}
		}
		//backwards
		else {
			for(int moves=0;moves<numMoves;moves++) {
				if(space.getPreviousID() != null) {
					space = hashMap.get(space.getPreviousID());
					count++;
				}
			}
		}
		//determine if end result is a legal destination: must not have reached a null space and either:
		//the space must not contain another of this player's tokens, or
		//the space must be the start of a slide of a different color
		if(count == numMoves &&
		   space != null &&
		   (!findMyTokens().contains(space.getId()))) { // || (space.getSlideToID() != null && space.getColor() != color))) {
			moveOptions.add(space.getId());
		}
		return moveOptions;
	}
	
	//TODO
	public void findSplitMoves() {
		for(Token t: tokens) {
			t.setMoves(new ArrayList<UUID>());
		}
	}
	
	//get IDs of locations of all this player's tokens, except those in home
	public ArrayList<UUID> findMyTokens() {
		ArrayList<UUID> tokenIDs = new ArrayList<UUID>();
		Token t;
		for(int token=0;token<4;token++) {
			t = tokens[token];
			if(!((hashMap.get(t.getSpaceID()) instanceof TerminalSpace) && (((TerminalSpace)hashMap.get(t.getSpaceID())).getType() == TerminalType.HOME))) {
				tokenIDs.add(t.getSpaceID());
			}
		}
		return tokenIDs;
	}
	
	public ArrayList<UUID> findOpponentTokens(ArrayList<Player> players) {
		ArrayList<UUID> tokenIDs = new ArrayList<UUID>();
		UUID tempID;
		for(int player=0;player<4;player++) {
			//don't look for tokens of the same color
			if(Color.values()[player] == color) continue;
			//find all non-safe token spaces
			for(int token=0;token<4;token++) {
				tempID = players.get(player).getTokens()[token].getSpaceID();
				if(!hashMap.get(tempID).isSafe()) {
					tokenIDs.add(tempID);
				}
			}
		}
		return tokenIDs;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Token[] getTokens() {
		return tokens;
	}

	public void setTokens(Token[] tokens) {
		this.tokens = tokens;
	}
}
