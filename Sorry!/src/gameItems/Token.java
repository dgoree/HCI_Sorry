package gameItems;
import spaces.Space;
import spaces.TerminalSpace;
import utilities.Color;
import utilities.TerminalType;


public class Token {
	private Space space;
	private Color color;
	
	public Token(Space space, Color color) {
		this.space = space.deepCopy();
		this.color = color;	
	}
	
	public boolean inStart() {
		return (getSpace() instanceof TerminalSpace && ((TerminalSpace) space).getType() == TerminalType.START);
	}
	
	public boolean inHome() {
		return (getSpace() instanceof TerminalSpace && ((TerminalSpace) space).getType() == TerminalType.HOME);
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
