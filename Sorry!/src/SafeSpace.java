import java.util.ArrayList;
import java.util.UUID;


public class SafeSpace extends Space{

	private ArrayList<Token> tokens;
	private TerminalSpace type;

	//fully customizable
	public SafeSpace(Space previous, Space next, Space slideTo, Space safeNext, Color color, boolean isSafe, TerminalSpace type) {
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

	public TerminalSpace getType() {
		return type;
	}

	public void setType(TerminalSpace type) {
		this.type = type;
	}
	
	
	

}
