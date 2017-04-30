package gameItems;
import java.util.ArrayList;
import java.util.UUID;

import utilities.Color;


public class Token {
	private UUID spaceID;
	private Color color;
	private ArrayList<UUID> moves;
	
	public Token(UUID spaceID, Color color) {
		this.spaceID = spaceID;
		this.color = color;	
	}

	public UUID getSpaceID() {
		return spaceID;
	}

	public void setSpaceID(UUID spaceID) {
		this.spaceID = spaceID;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public ArrayList<UUID> getMoves() {
		return moves;
	}
	
	public void setMoves(ArrayList<UUID> moves) {
		this.moves = moves;
	}
	
	public void addMove(UUID move) {
		this.moves.add(move);
	}
	
	public void addMoves(ArrayList<UUID> newMoves) {
		this.moves.addAll(newMoves);
	}
	
}
