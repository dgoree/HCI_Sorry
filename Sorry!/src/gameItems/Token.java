package gameItems;
import java.util.ArrayList;

import spaces.Space;
import utilities.Color;


public class Token {
	private Space space;
	private Color color;
	
	public Token(Space space, Color color) {
		this.space = space.deepCopy();
		this.color = color;
	}
	
	public ArrayList<Space> getMoves(Card card) {
		//TODO
		return null;
	}

	public Space getSpace() {
		return space;
	}

	public void setSpace(Space space) {
		this.space = space.deepCopy();
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	
}
