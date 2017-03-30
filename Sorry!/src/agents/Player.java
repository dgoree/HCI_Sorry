package agents;
import gameItems.Token;
import utilities.Color;


public class Player {
	private String name;
	private Color color;
	private Token[] tokens;
	
	public Player(String name, Color color, Token[] tokens) {
		this.name = name;
		this.color = color;
		this.tokens = tokens;
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
