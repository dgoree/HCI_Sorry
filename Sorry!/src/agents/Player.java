package agents;
import java.util.ArrayList;

import gameItems.Token;
import spaces.Space;
import utilities.Color;


public class Player {
	private String name;
	private Color color;
	private Token[] tokens;
	private Space startSpace;
	private ArrayList<Space> moves;
	
	public Player(String name, Color color, Token[] tokens, Space startSpace) {
		this.name = name;
		this.color = color;
		this.tokens = tokens;
		this.startSpace = startSpace;
	}
	
	public void getMoves(ArrayList<Player> players, int cardNumber) {
		moves = new ArrayList<Space>();
		
		switch(cardNumber) {
		case 0: //sorry card
			//requires a token in start
			if(tokenInStart()) {
				//move to any non-safe space containing an opponent's token
				moves = findOpponentTokens(players);
			}
			break;
		case 1:
			//move forward 1
			moves = findSimpleMoves(cardNumber,true);
			//move from start
			moves.add(startSpace.getNext());
			break;
		case 2:
			//move forward 2
			moves = findSimpleMoves(cardNumber,true);
			//move from start
			moves.add(startSpace.getNext());
			break;
		case 3:
			//move 3
			moves = findSimpleMoves(cardNumber,true);
			break;
		case 4:
			//back 4
			moves = findSimpleMoves(cardNumber,false);
			break;
		case 5:
			//move 5
			moves = findSimpleMoves(cardNumber,true);
			break;
		case 7:
			//move one or two tokens 7 spaces total - will require extra logic
			moves = findSplitMoves();
			break;
		case 8:
			//move 8
			moves = findSimpleMoves(cardNumber,true);
			break;
		case 10:
			//move 10
			moves = findSimpleMoves(cardNumber,true);
			//move back 1
			moves.addAll(0, findSimpleMoves(1, false));
			break;
		case 11:
			//swap with opponent
			moves = findOpponentTokens(players);
			//move 11
			moves.addAll(0,findSimpleMoves(cardNumber,true));
			break;
		case 12:
			//move 12
			moves = findSimpleMoves(cardNumber,true);
			break;
		}
	}
	
	public boolean tokenInStart() {
		for(Token t: tokens) {
			if(t.inStart()) return true;
		}
		return false;
	}
	
	//TODO
	public Space findStartSpace() {
		Space startSpace = new Space(null, null);
		return startSpace;
	}
	
	//direction: true = forwards, false = backwards
	public ArrayList<Space> findSimpleMoves(int numMoves, boolean direction) {
		Space space;
		ArrayList<Space> moveOptions = new ArrayList<Space>();
		//iterate through all tokens
		for(Token t:tokens) {
			space = t.getSpace();
			//forwards
			if(direction) {
				for(int moves=0;moves<numMoves;moves++) {
					if(space.getSafeNext() != null && space.getSafeNext().getColor() == color) {
						space = space.getSafeNext();
					}
					else {
						space = space.getNext();
					}
				}
			}
			//backwards
			else {
				for(int moves=0;moves<numMoves;moves++) {
					space = space.getPrevious();
				}
			}
			//determine if end result is a legal destination
			if(space != null && !findMyTokens().contains(space)) {
				moveOptions.add(space);
			}
		}
		return moveOptions;
	}
	
	public ArrayList<Space> findSplitMoves() {
		ArrayList<Space> moveOptions = new ArrayList<Space>();
		//TODO		
		return moveOptions;
	}
	
	public ArrayList<Space> findMyTokens() {
		ArrayList<Space> tokenLocs = new ArrayList<Space>();
		for(int token=0;token<4;token++) {
			tokenLocs.add(tokens[token].getSpace());
		}
		return tokenLocs;
	}
	
	public ArrayList<Space> findOpponentTokens(ArrayList<Player> players) {
		ArrayList<Space> tokenLocs = new ArrayList<Space>();
		Space tempSpace;
		for(int player=0;player<4;player++) {
			//don't look for tokens of the same color
			if(Color.values()[player] == color) continue;
			//find all non-safe token spaces
			for(int token=0;token<4;token++) {
				tempSpace = players.get(player).getTokens()[token].getSpace();
				if(!tempSpace.isSafe()) {
					tokenLocs.add(tempSpace);
				}
			}
		}
		return tokenLocs;
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
