import java.util.ArrayList;
import java.util.UUID;


public class TerminalSpace extends Space{

	private ArrayList<Token> tokens;
	private TerminalType type;

	//fully customizable
	public TerminalSpace(Space previous, Space next, Space slideTo, Space safeNext, Color color, TerminalType type) {
		super(previous, next, slideTo, safeNext, color, true);
		this.tokens = new ArrayList<Token>();
		this.type = type;
	}
	
	public TerminalSpace deepCopy() {
		TerminalSpace copy = new TerminalSpace(this.previous, this.next, this.slideTo, this.safeNext, this.color, this.type);
		copy.setTokens(this.tokens);
		return copy;
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
