package agents;
import java.util.ArrayList;
import java.util.Arrays;
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
	private boolean override_7s = false; //override for findSimpleMoves() when used for 7s
	private UUID[] predictedTokenIDs;
	
	public Player(String name, Color color, Token[] tokens, UUID startSpace, HashMap<UUID, Space> hashMap) {
		this.name = name;
		this.color = color;
		this.tokens = tokens;
		this.startSpace = startSpace;
		this.hashMap = hashMap;
	}
	
	//if secondSevenMove is true, this is the second part of a 2-pawn split and only simple forward moves are allowed
	public void calcMoves(ArrayList<Player> players, int cardNumber, boolean secondSevenMove) {
		
		//sevens have to be handled specially
		if(cardNumber == 7) {
			//any one token can move seven
			for(Token t: tokens) {
				t.setMoves(findSimpleMoves(t, 7, true));
			}
			//two tokens can split the seven spaces
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
				else t.setMoves(new ArrayList<UUID>());
				break;
			case 1:
				//move forward 1
				t.setMoves(findSimpleMoves(t,cardNumber,true));
				//move from start unless part of a split move
				if(!secondSevenMove && t.getSpaceID() == startSpace) {
					t.addMove(hashMap.get(t.getSpaceID()).getNextID());
				}
				break;
			case 2:
				//move forward 2
				t.setMoves(findSimpleMoves(t,cardNumber,true));
				//move from start unless part of a split move
				if(!secondSevenMove && t.getSpaceID() == startSpace) {
					t.addMove(hashMap.get(t.getSpaceID()).getNextID());
				}
				break;
			case 3:
				//move 3
				t.setMoves(findSimpleMoves(t,cardNumber,true));
				break;
			case 4:
				//back 4, unless part of a split
				if(!secondSevenMove) {
					t.setMoves(findSimpleMoves(t,cardNumber,false));
				}
				else {
					t.setMoves(findSimpleMoves(t,cardNumber,true));
				}
				break;
			case 5:
				//move 5
				t.setMoves(findSimpleMoves(t,cardNumber,true));
				break;
			case 6:
				//move 6; only happens as part of a split 7
				t.setMoves(findSimpleMoves(t,cardNumber,true));
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
		
		//create moveOptions
		
		//special scenario for sevens
		if(override_7s) {
			if(count == numMoves &&
			   space != null &&
			   (!Arrays.asList(predictedTokenIDs).contains(space.getId()))) {
				moveOptions.add(space.getId());
			}
		}
		
		//determine if end result is a legal destination: must not have reached a null space and
		//the space must not contain another of this player's tokens
		else if(count == numMoves &&
		   space != null &&
		   (!findMyTokens().contains(space.getId()))) {
			moveOptions.add(space.getId());
		}
		return moveOptions;
	}
	
	//calculate all possible move combinations of exactly two tokens totaling 7 spaces
	public void findSplitMoves() {
		ArrayList<UUID> t1_moves;
		ArrayList<UUID> t2_moves;
		for(int token1=0; token1<=3; token1++) {
			for(int token2=0; token2<=3; token2++) {
				if(token1 == token2) continue;
				for(int n=1; n<=6; n++) {
					//If token1 can move n spaces and token2 can move 7-n spaces, these moves are valid.
					t1_moves = findSimpleMoves(tokens[token1],n,true);
					if(!t1_moves.isEmpty()) {
						predictToken(token1, n);
						override_7s = true;
						t2_moves = findSimpleMoves(tokens[token2],7-n,true);
						override_7s = false;
						if(!t2_moves.isEmpty()) {
							tokens[token1].addMoves(t1_moves);
						}
					}
				}
			}
		}
	}
	
	//get IDs of locations of all this player's tokens, except those in home
	public ArrayList<UUID> findMyTokens() {
		ArrayList<UUID> tokenIDs = new ArrayList<UUID>();
		for(Token t: tokens) {
			if(!((hashMap.get(t.getSpaceID()) instanceof TerminalSpace) && (((TerminalSpace)hashMap.get(t.getSpaceID())).getType() == TerminalType.HOME))) {
				tokenIDs.add(t.getSpaceID());
			}
		}
		return tokenIDs;
	}
	
	//update IDs of locations where this player's tokens have been predicted to be, based on 7s calculations
	public void predictToken(int predictedToken, int numSpaces) {
		predictedTokenIDs = new UUID[4];
		int i=0;
		for(Token t: tokens) {
			predictedTokenIDs[i++] = t.getSpaceID();
		}
		UUID id = predictedTokenIDs[predictedToken];
		
		//token can't move if in start or home
		if(hashMap.get(id) instanceof TerminalSpace) return;
		
		//make prediction: move this token the proper number of spaces
		for(int n=0; n<numSpaces; n++) {
			//first try to go to a safeNext
			if(hashMap.get(id).getSafeNextID() != null && hashMap.get(hashMap.get(id).getSafeNextID()).getDefaultColor() == color) {
				id = hashMap.get(id).getSafeNextID();
			}
			//go to an ordinary next is safeNext is invalid
			else {
				id = hashMap.get(id).getNextID();
			}
		}
		
		//update prediction
		predictedTokenIDs[predictedToken] = id;
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
	
	public int numTokensInStart() {
		int numTokens = 0;
		for(Token t: tokens) {
			if((hashMap.get(t.getSpaceID()) instanceof TerminalSpace) && (((TerminalSpace)hashMap.get(t.getSpaceID())).getType() == TerminalType.START)) {
				numTokens++;	
			}
		}
		return numTokens;
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
