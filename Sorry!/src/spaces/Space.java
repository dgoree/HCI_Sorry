package spaces;
import java.awt.Dimension;
import java.util.UUID;

import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import utilities.Color;


public class Space extends JLabel
{
	protected UUID previousID;
	protected UUID nextID;
	protected UUID slideToID;
	protected UUID safeNextID;
	protected UUID startPreviousID;
	protected Color color;
	protected boolean isSafe;
	protected UUID id;
	
	//Constants needed for GUI
	public static final int SIDE_LENGTH = 40;
	private static final java.awt.Color NAVY = new java.awt.Color(0, 0, 128);
	private static final int BORDER_WEIGHT = 2;

	//common space
	public Space(UUID previous, UUID next) {
		this.previousID = previous;
		this.nextID = next;
		this.slideToID = null;
		this.safeNextID = null;
		this.startPreviousID = null;
		this.color = null;
		this.isSafe = false;
		this.id = UUID.randomUUID();
		
		initVisualProperties();
	}

	//safe space
	public Space(UUID previous, UUID safeNext, Color color) {
		this.previousID = previous;
		this.slideToID = null;
		this.safeNextID = safeNext;
		this.startPreviousID = null;
		this.color = color;
		this.isSafe = true;
		this.id = UUID.randomUUID();
		
		initVisualProperties();
	}

	/**
	 * Set size, border, and fill of common spaces
	 */
	private void initVisualProperties()
	{
		//Set size
		setMinimumSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
		setPreferredSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
		setMaximumSize(new Dimension((3 * SIDE_LENGTH), (3 * SIDE_LENGTH)));

		//Set border
		setBorder(new LineBorder(NAVY, BORDER_WEIGHT));
		
		//Set fill
		setBackground(java.awt.Color.WHITE);
		setOpaque(true);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Space)
			return this.id.equals(((Space)o).id);
		return false;
	}
	
	public UUID getPreviousID() {
		return previousID;
	}

	public void setPreviousID(UUID previousID) {
		this.previousID = previousID;
	}

	public UUID getNextID() {
		return nextID;
	}

	public void setNextID(UUID nextID) {
		this.nextID = nextID;
	}

	public UUID getSlideToID() {
		return slideToID;
	}

	public void setSlideToID(UUID slideToID) {
		this.slideToID = slideToID;
	}

	public UUID getSafeNextID() {
		return safeNextID;
	}

	public void setSafeNextID(UUID safeNextID) {
		this.safeNextID = safeNextID;
	}
	
	public UUID getStartPreviousID() {
		return this.startPreviousID;
	}
	
	public void setStartPreviousID(UUID startPreviousID) {
		this.startPreviousID = startPreviousID;
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
