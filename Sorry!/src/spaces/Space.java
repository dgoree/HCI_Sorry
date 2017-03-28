package spaces;
import java.util.UUID;

import utilities.Color;


public class Space {
	protected Space previous;
	protected Space next;
	protected Space slideTo;
	protected Space safeNext;
	protected Color color;
	protected boolean isSafe;
	protected UUID id;
	
	//common space
	public Space(Space previous, Space next) {
		this.previous = previous.deepCopy();
		this.next = next.deepCopy();
		this.slideTo = null;
		this.safeNext = null;
		this.color = null;
		this.isSafe = false;
		this.id = UUID.randomUUID();
	}
	
	//slide space
	public Space(Space previous, Space next, Space slideTo, Color color) {
		this.previous = previous.deepCopy();
		this.next = next.deepCopy();
		this.slideTo = slideTo.deepCopy();
		this.safeNext = null;
		this.color = color;
		this.isSafe = false;
		this.id = UUID.randomUUID();
	}
	
	//fully customizable
	public Space(Space previous, Space next, Space slideTo, Space safeNext, Color color, boolean isSafe) {
		this.previous = previous.deepCopy();
		this.next = next.deepCopy();
		this.slideTo = slideTo.deepCopy();
		this.safeNext = safeNext.deepCopy();
		this.color = color;
		this.isSafe = isSafe;
		this.id = UUID.randomUUID();
	}
	
	public Space deepCopy() {
		Space copy = new Space(this.previous, this.next, this.slideTo, this.safeNext, this.color, this.isSafe);
		copy.setId(this.id);
		return copy;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Space)
			return this.id.equals(((Space)o).id);
		return false;
	}

	public Space getPrevious() {
		return previous;
	}

	public void setPrevious(Space previous) {
		this.previous = previous.deepCopy();
	}

	public Space getNext() {
		return next;
	}

	public void setNext(Space next) {
		this.next = next.deepCopy();
	}

	public Space getSlideTo() {
		return slideTo;
	}

	public void setSlideTo(Space slideTo) {
		this.slideTo = slideTo.deepCopy();
	}

	public Space getSafeNext() {
		return safeNext;
	}

	public void setSafeNext(Space safeNext) {
		this.safeNext = safeNext.deepCopy();
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean isSafe() {
		return isSafe;
	}

	public void setSafe(boolean isSafe) {
		this.isSafe = isSafe;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
	
	
	
	
}
