import java.util.UUID;


public class Space {
	private Space previous;
	private Space next;
	private Space slideTo;
	private Space safeNext;
	private Color color;
	private boolean isSafe;
	private UUID id;
	
	//common space
	public Space(Space previous, Space next) {
		this.previous = previous;
		this.next = next;
		this.slideTo = null;
		this.safeNext = null;
		this.color = null;
		this.isSafe = false;
		this.id = UUID.randomUUID();
	}
	
	//slide space
	public Space(Space previous, Space next, Space slideTo, Color color) {
		this.previous = previous;
		this.next = next;
		this.slideTo = slideTo;
		this.safeNext = null;
		this.color = color;
		this.isSafe = false;
		this.id = UUID.randomUUID();
	}
	
	//fully customizable
	public Space(Space previous, Space next, Space slideTo, Space safeNext, Color color, boolean isSafe) {
		this.previous = previous;
		this.next = next;
		this.slideTo = slideTo;
		this.safeNext = safeNext;
		this.color = color;
		this.isSafe = isSafe;
		this.id = UUID.randomUUID();
	}

	public Space getPrevious() {
		return previous;
	}

	public void setPrevious(Space previous) {
		this.previous = previous;
	}

	public Space getNext() {
		return next;
	}

	public void setNext(Space next) {
		this.next = next;
	}

	public Space getSlideTo() {
		return slideTo;
	}

	public void setSlideTo(Space slideTo) {
		this.slideTo = slideTo;
	}

	public Space getSafeNext() {
		return safeNext;
	}

	public void setSafeNext(Space safeNext) {
		this.safeNext = safeNext;
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
