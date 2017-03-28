package agents;
import gameItems.Token;
import utilities.Color;


public class Player {
	private Color color;
	private Token[] tokens;
	
	public Player(Color color, Token[] tokens) {
		this.color = color;
		this.tokens = tokens;
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
