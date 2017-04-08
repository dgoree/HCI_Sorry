package mvc.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import spaces.Space;
import spaces.TerminalSpace;
import utilities.TerminalType;

public class GameBoard extends JPanel
{	
	//The number of spaces along a side of the square game board
	private static final int NUM_SPACES_PER_SIDE = 16;
	
	/**
	 * Constructs the game board
	 */
	public GameBoard() 
	{
		setLayout(new GridBagLayout());
		
		initialize();	
	}
	
	/**
	 * Initializes the visual elements of the game board, including spaces and
	 * the START, safety zone, and HOME for each of the four colors 
	 */
	private void initialize() 
	{	
		initializeSpaces();

		//Yellow
		initializeStart(Color.YELLOW, 3, 1); //upper-left corner (3, 1)
		initializeSafetyZone(Color.YELLOW, 2, 1, 2, 5); //(2, 1) to (2, 5)
		initializeHome(Color.YELLOW, 1, 6); //upper-left corner (1, 6)
		
		//Green
		initializeStart(Color.GREEN, 12, 3); //upper-left corner (12, 3)
		initializeSafetyZone(Color.GREEN, 10, 2, 14, 2); //(10, 2) to (14, 2)
		initializeHome(Color.GREEN, 7, 1); //upper-left corner (7, 1)
		
		//Red
		initializeStart(Color.RED, 10, 12); //upper-left corner (10, 12)
		initializeSafetyZone(Color.RED, 13, 10, 13, 14); //(13, 10) to (13, 14)
		initializeHome(Color.RED, 12, 7); //upper-left corner (12, 7)
		
		//Blue
		initializeStart(Color.BLUE, 1, 10); //upper-left corner (1, 10)
		initializeSafetyZone(Color.BLUE, 1, 13, 5, 13); //(1, 13) to (5, 13)
		initializeHome(Color.BLUE, 6, 12); //upper-left corner (6, 12)
	}
	
	/**
	 * Initializes the spaces along the perimeter of the game board
	 */
	private void initializeSpaces()
	{
		//Top row
		for (int i = 0; i < (NUM_SPACES_PER_SIDE - 1); ++i)
		{
			Space space = new Space(null, null);
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = i;
			c.gridy = 0;
			add(space, c);
		}
		
		//Right column
		for (int i = 0; i < (NUM_SPACES_PER_SIDE - 1); ++i)
		{
			Space space = new Space(null, null);
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = (NUM_SPACES_PER_SIDE - 1);
			c.gridy = i;
			add(space, c);
		}
		
		//Bottom row
		for (int i = (NUM_SPACES_PER_SIDE - 1); i > 0 ; --i)
		{
			Space space = new Space(null, null);
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = i;
			c.gridy = (NUM_SPACES_PER_SIDE - 1);
			add(space, c);
		}
		
		//Left column
		for (int i = (NUM_SPACES_PER_SIDE - 1); i > 0 ; --i)
		{
			Space space = new Space(null, null);
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = i;
			add(space, c);
		}
	}
	
	/**
	 * Initializes a "START" space
	 * 
	 * @param color The color of the "START" space
	 * @param gridX	The x-coordinate of the upper-left corner of the "START" space
	 * @param gridY The y-coordinate of the upper-left corner of the "START" space
	 */
	private void initializeStart(Color color, int gridX, int gridY)
	{
		TerminalSpace start = new TerminalSpace(null, null, null, TerminalType.START);
		
		//Set "START" size and color
		start.setPreferredSize(new Dimension(120, 120)); //FIXME: Use constant?
		start.setBackground(color);
		start.setOpaque(true);
		
		//Set "START" location
		//TODO: Padding
		GridBagConstraints c = new GridBagConstraints();
		//3x3 space with the area of 9 normal game board spaces
		c.gridwidth = 3; //FIXME: Use constant?
		c.gridheight = 3; //FIXME: Use constant?
		c.gridx = gridX;
		c.gridy = gridY;
		
		add(start, c);
	}
	
	/**
	 * Initializes the spaces of a safety zone
	 * 
	 * @param color The color of the safety zone
	 * @param x1 The x-coordinate of the first (numerically lowest coordinate) safety zone space
	 * @param y1 The y-coordinate of the first (numerically lowest coordinate) safety zone space
	 * @param x2 The x-coordinate of the last (numerically highest coordinate) safety zone space
	 * @param y2 The y-coordinate of the last (numerically highest coordinate) safety zone space
	 */
	private void initializeSafetyZone(Color color, int x1, int y1, int x2, int y2)
	{
		//Vertical safety zone
		if (x1 == x2)
		{
			//For each safety zone space
			for (int i = y1; i <= y2; ++i)
			{
				Space safe = new Space(null, null);
				
				//Set color
				safe.setBackground(color);
				safe.setOpaque(true);
				
				//Set location
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = x1;
				c.gridy = i;
				
				add(safe, c);
			}
		}
		
		//Horizontal safety zone
		else if (y1 == y2)
		{
			//For each safety zone space
			for (int i = x1; i <= x2; ++i)
			{
				Space safe = new Space(null, null);
				
				//Set color
				safe.setBackground(color);
				safe.setOpaque(true);
				
				//Set location
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = i;
				c.gridy = y1;
				
				add(safe, c);
			}	
		}
	}
	
	/**
	 * Initializes a "HOME" space
	 * 
	 * @param color The color of the "HOME" space
	 * @param gridX The x-coordinate of the upper-left corner of the "HOME" space
	 * @param gridY The y-coordinate of the upper-left corner of the "HOME" space
	 */
	private void initializeHome(Color color, int gridX, int gridY)
	{
		TerminalSpace home = new TerminalSpace(null, null, null, TerminalType.HOME);
		
		//Set "HOME" size and color
		home.setPreferredSize(new Dimension(120, 120)); //FIXME: Use constant?
		home.setBackground(color);
		home.setOpaque(true);
		
		//Set "HOME" location
		//FIXME: Padding
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = 3; //FIXME: Use constant?
		c.gridheight = 3; //FIXME: Use constant?
		c.gridx = gridX;
		c.gridy = gridY;
		
		add(home, c);	
	}
}
