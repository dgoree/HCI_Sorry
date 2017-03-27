import java.util.ArrayList;
import java.util.UUID;


public class TerminalSpace extends Space{

	private ArrayList<Token> tokens;
	private TerminalType type;

	//fully customizable
	public TerminalSpace(Space previous, Space next, Space slideTo, Space safeNext, Color color, boolean isSafe, TerminalType type) {
		super(previous, next, slideTo, safeNext, color, isSafe);
		this.tokens = new ArrayList<Token>();
		this.type = type;
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
