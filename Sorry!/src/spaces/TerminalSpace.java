package spaces;
import gameItems.Token;

import java.util.ArrayList;
import java.util.UUID;

import utilities.Color;
import utilities.TerminalType;


public class TerminalSpace extends Space{

	private ArrayList<Token> tokens;
	private TerminalType type;
	
	
	//empty constructor
	public TerminalSpace() {
		super(null,null);
	}
	
	//fully customizable
	public TerminalSpace(UUID previous, UUID next, Color color, TerminalType type) {
		super(previous, next, color);
		this.tokens = new ArrayList<Token>();
		this.type = type;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof TerminalSpace)
			return this.id.equals(((TerminalSpace) o).id);
		return false;
	}
	
	public boolean add(Token token) {
		return this.tokens.add(token);
	}

	public ArrayList<Token> getTokens() {
		return tokens;
	}

	public void setTokens(ArrayList<Token> tokens) {
		this.tokens = tokens;
	}

	public TerminalType getType() {
		return type;
	}

	public void setType(TerminalType type) {
		this.type = type;
	}
	
	@Override
	public boolean isSafe() {
		return true;
	}
	
	
	

}
