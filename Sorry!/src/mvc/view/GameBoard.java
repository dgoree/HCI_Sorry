package mvc.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import agents.Player;
import gameItems.Token;
import mvc.controller.Controller;
import mvc.model.GameSystem;
import mvc.model.Listener;
import spaces.Space;
import spaces.TerminalSpace;
import utilities.Color;

public class GameBoard extends JPanel implements Listener
{	
	private static final int NUM_SPACES_PER_SIDE = 16; 	//The number of spaces along a side of the square game board
	private final int SIDE_LENGTH = 40;
	private final java.awt.Color NAVY = new java.awt.Color(0, 0, 128);
	private final int BORDER_WEIGHT = 2;
	private final GameSystem gameSystem;
	private final Controller controller;
	
	/**
	 * Constructs the game board
	 * @throws IOException 
	 */
	public GameBoard(GameSystem gameSystem, Controller controller) throws IOException 
	{
		this.gameSystem = gameSystem;
		this.controller = controller;
		gameSystem.addListener(this);
		setLayout(new GridBagLayout());
		
		initialize(gameSystem, controller);
	}
	
	/**
	 * Initializes the visual elements of the game board, including spaces and
	 * the START, safety zone, and HOME for each of the four colors 
	 * @throws IOException 
	 */
	private void initialize(final GameSystem gameSystem, final Controller controller) throws IOException 
	{	
		UUID[] startIDs = gameSystem.getStartIDs();
		UUID[] safeStartIDs = gameSystem.getSafeZoneStartSpaces();
		
		//Yellow
		initializeStart(startIDs, Color.YELLOW, 3, 1); //upper-left corner (3, 1)
		initializeHome(safeStartIDs, Color.YELLOW, 1, 6); //upper-left corner (1, 6)
		initializeLowerLevelSafetyZone(java.awt.Color.YELLOW, 2, 1, 2, 5);
		initializeSafetyZone(safeStartIDs, Color.YELLOW, 2, 1, 2, 5); //(2, 1) to (2, 5)

		//Green
		initializeStart(startIDs, Color.GREEN, 12, 3); //upper-left corner (12, 3)
		initializeHome(safeStartIDs, Color.GREEN, 7, 1); //upper-left corner (7, 1)
		initializeLowerLevelSafetyZone(java.awt.Color.GREEN, 14, 2, 10, 2);
		initializeSafetyZone(safeStartIDs, Color.GREEN, 14, 2, 10, 2); //(14, 2) to (10, 2)
		
		//Red
		initializeStart(startIDs, Color.RED, 10, 12); //upper-left corner (10, 12)
		initializeHome(safeStartIDs, Color.RED, 12, 7); //upper-left corner (12, 7)
		initializeLowerLevelSafetyZone(java.awt.Color.RED, 13, 14, 13, 10);
		initializeSafetyZone(safeStartIDs, Color.RED, 13, 14, 13, 10); //(13, 14) to (13, 10)
		
		//Blue
		initializeStart(startIDs, Color.BLUE, 1, 10); //upper-left corner (1, 10)
		initializeHome(safeStartIDs, Color.BLUE, 6, 12); //upper-left corner (6, 12)
		initializeLowerLevelSafetyZone(java.awt.Color.BLUE, 1, 13, 5, 13);
		initializeSafetyZone(safeStartIDs, Color.BLUE, 1, 13, 5, 13); //(1, 13) to (5, 13)
		
		initializeSlides();
		initializeLowerLayerSpaces();
		initializeSpaces(gameSystem.getSpaceIDs());
		initializeCardHolder(gameSystem, controller);//Maria's addition
	}
	
	/**
	 * Initialize slide images
	 */
	private void initializeSlides()
	{
		//Yellow Slide (4 spaces)
		int spaces = 4;
		{
			Space slide = new Space(null, null);
			slide.setPreferredSize(new Dimension(Space.SIDE_LENGTH * spaces, Space.SIDE_LENGTH));
			slide.setOpaque(false);
			
			ImageIcon icon = new ImageIcon("yellowSlide4.png");
			slide.setIcon(icon);
			slide.setIcon(new ImageIcon(icon.getImage().getScaledInstance(Space.SIDE_LENGTH * spaces, Space.SIDE_LENGTH, Image.SCALE_FAST)));
			slide.setHorizontalAlignment(JLabel.CENTER);
			slide.setVerticalAlignment(JLabel.CENTER);
			
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridwidth = 4;
			add(slide, c);
		}
		
		//Yellow slide (5 spaces)
		spaces = 5;
		{
			Space slide = new Space(null, null);
			slide.setPreferredSize(new Dimension(Space.SIDE_LENGTH * spaces, Space.SIDE_LENGTH));
			slide.setOpaque(false);
			
			ImageIcon icon = new ImageIcon("yellowSlide5.png");
			slide.setIcon(icon);
			slide.setIcon(new ImageIcon(icon.getImage().getScaledInstance(Space.SIDE_LENGTH * spaces, Space.SIDE_LENGTH, Image.SCALE_FAST)));
			slide.setHorizontalAlignment(JLabel.CENTER);
			slide.setVerticalAlignment(JLabel.CENTER);
			
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 9;
			c.gridy = 0;
			c.gridwidth = 5;
			add(slide, c);
		}
		
		//Green slide (4 spaces)
		spaces = 4;
		{
			Space slide = new Space(null, null);
			slide.setPreferredSize(new Dimension(Space.SIDE_LENGTH, Space.SIDE_LENGTH * spaces));
			slide.setOpaque(false);
			
			ImageIcon icon = new ImageIcon("greenSlide4.png");
			slide.setIcon(icon);
			slide.setIcon(new ImageIcon(icon.getImage().getScaledInstance(Space.SIDE_LENGTH, Space.SIDE_LENGTH * spaces, Image.SCALE_FAST)));
			slide.setHorizontalAlignment(JLabel.CENTER);
			slide.setVerticalAlignment(JLabel.CENTER);
			
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 15;
			c.gridy = 1;
			c.gridheight = 4;
			add(slide, c);
		}
		
		//Green slide (5 spaces)
		spaces = 5;
		{
			Space slide = new Space(null, null);
			slide.setPreferredSize(new Dimension(Space.SIDE_LENGTH, Space.SIDE_LENGTH * spaces));
			slide.setOpaque(false);
			
			ImageIcon icon = new ImageIcon("greenSlide5.png");
			slide.setIcon(icon);
			slide.setIcon(new ImageIcon(icon.getImage().getScaledInstance(Space.SIDE_LENGTH, Space.SIDE_LENGTH * spaces, Image.SCALE_FAST)));
			slide.setHorizontalAlignment(JLabel.CENTER);
			slide.setVerticalAlignment(JLabel.CENTER);
			
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 15;
			c.gridy = 9;
			c.gridheight = 5;
			add(slide, c);
		}
		
		//Red Slide (4 spaces)
		spaces = 4;
		{
			Space slide = new Space(null, null);
			slide.setPreferredSize(new Dimension(Space.SIDE_LENGTH * spaces, Space.SIDE_LENGTH));
			slide.setOpaque(false);
			
			ImageIcon icon = new ImageIcon("redSlide4.png");
			slide.setIcon(icon);
			slide.setIcon(new ImageIcon(icon.getImage().getScaledInstance(Space.SIDE_LENGTH * spaces, Space.SIDE_LENGTH, Image.SCALE_FAST)));
			slide.setHorizontalAlignment(JLabel.CENTER);
			slide.setVerticalAlignment(JLabel.CENTER);
			
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 11;
			c.gridy = 15;
			c.gridwidth = 4;
			add(slide, c);
		}
		
		//Red slide (5 spaces)
		spaces = 5;
		{
			Space slide = new Space(null, null);
			slide.setPreferredSize(new Dimension(Space.SIDE_LENGTH * spaces, Space.SIDE_LENGTH));
			slide.setOpaque(false);
			
			ImageIcon icon = new ImageIcon("redSlide5.png");
			slide.setIcon(icon);
			slide.setIcon(new ImageIcon(icon.getImage().getScaledInstance(Space.SIDE_LENGTH * spaces, Space.SIDE_LENGTH, Image.SCALE_FAST)));
			slide.setHorizontalAlignment(JLabel.CENTER);
			slide.setVerticalAlignment(JLabel.CENTER);
			
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 2;
			c.gridy = 15;
			c.gridwidth = 5;
			add(slide, c);
		}
		
		//Blue slide (4 spaces)
		spaces = 4;
		{
			Space slide = new Space(null, null);
			slide.setPreferredSize(new Dimension(Space.SIDE_LENGTH, Space.SIDE_LENGTH * spaces));
			slide.setOpaque(false);
			
			ImageIcon icon = new ImageIcon("blueSlide4.png");
			slide.setIcon(icon);
			slide.setIcon(new ImageIcon(icon.getImage().getScaledInstance(Space.SIDE_LENGTH, Space.SIDE_LENGTH * spaces, Image.SCALE_FAST)));
			slide.setHorizontalAlignment(JLabel.CENTER);
			slide.setVerticalAlignment(JLabel.CENTER);
			
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 11;
			c.gridheight = 4;
			add(slide, c);
		}
		
		//Blue slide (5 spaces)
		spaces = 5;
		{
			Space slide = new Space(null, null);
			slide.setPreferredSize(new Dimension(Space.SIDE_LENGTH, Space.SIDE_LENGTH * spaces));
			slide.setOpaque(false);
			
			ImageIcon icon = new ImageIcon("blueSlide5.png");
			slide.setIcon(icon);
			slide.setIcon(new ImageIcon(icon.getImage().getScaledInstance(Space.SIDE_LENGTH, Space.SIDE_LENGTH * spaces, Image.SCALE_FAST)));
			slide.setHorizontalAlignment(JLabel.CENTER);
			slide.setVerticalAlignment(JLabel.CENTER);
			
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 5;
			add(slide, c);
		}
	}
	
	/**
	 * Initializes the white spaces along the perimeter of the game board
	 */
	private void initializeLowerLayerSpaces()
	{	
		//Top row
		for (int i = 0; i < (NUM_SPACES_PER_SIDE - 1); ++i)
		{
			JLabel label = new JLabel();
			label.setMinimumSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
			label.setPreferredSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
			label.setMaximumSize(new Dimension((3 * SIDE_LENGTH), (3 * SIDE_LENGTH)));
			label.setBorder(new LineBorder(NAVY, BORDER_WEIGHT));
			label.setBackground(java.awt.Color.WHITE);
			label.setOpaque(true);
			
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = i;
			c.gridy = 0;
			
			add(label, c);
		}
		
		//Right column
		for (int i = 0; i < (NUM_SPACES_PER_SIDE - 1); ++i)
		{
			JLabel label = new JLabel();
			label.setMinimumSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
			label.setPreferredSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
			label.setMaximumSize(new Dimension((3 * SIDE_LENGTH), (3 * SIDE_LENGTH)));
			label.setBorder(new LineBorder(NAVY, BORDER_WEIGHT));
			label.setBackground(java.awt.Color.WHITE);
			label.setOpaque(true);
			
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = (NUM_SPACES_PER_SIDE - 1);
			c.gridy = i;
			
			add(label, c);
		}
		
		//Bottom row
		for (int i = (NUM_SPACES_PER_SIDE - 1); i > 0 ; --i)
		{
			JLabel label = new JLabel();
			label.setMinimumSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
			label.setPreferredSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
			label.setMaximumSize(new Dimension((3 * SIDE_LENGTH), (3 * SIDE_LENGTH)));
			label.setBorder(new LineBorder(NAVY, BORDER_WEIGHT));
			label.setBackground(java.awt.Color.WHITE);
			label.setOpaque(true);
			
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = i;
			c.gridy = (NUM_SPACES_PER_SIDE - 1);
			
			add(label, c);
		}
		
		//Left column
		for (int i = (NUM_SPACES_PER_SIDE - 1); i > 0 ; --i)
		{
			JLabel label = new JLabel();
			label.setMinimumSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
			label.setPreferredSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
			label.setMaximumSize(new Dimension((3 * SIDE_LENGTH), (3 * SIDE_LENGTH)));
			label.setBorder(new LineBorder(NAVY, BORDER_WEIGHT));
			label.setBackground(java.awt.Color.WHITE);
			label.setOpaque(true);
			
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = i;
			
			add(label, c);
		}
		
		//Extra start spaces
		//Yellow
		JLabel extraSpace1 = new JLabel();
		extraSpace1.setMinimumSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
		extraSpace1.setPreferredSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
		extraSpace1.setMaximumSize(new Dimension((3 * SIDE_LENGTH), (3 * SIDE_LENGTH)));
		extraSpace1.setBackground(NAVY);
		extraSpace1.setOpaque(true);
		
		GridBagConstraints c1 = new GridBagConstraints();
		c1.gridx = 4;
		c1.gridy = 1;
		add(extraSpace1, c1);
	
		//Green
		JLabel extraSpace2 = new JLabel();
		extraSpace2.setMinimumSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
		extraSpace2.setPreferredSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
		extraSpace2.setMaximumSize(new Dimension((3 * SIDE_LENGTH), (3 * SIDE_LENGTH)));
		extraSpace2.setBackground(NAVY);
		extraSpace2.setOpaque(true);
		
		GridBagConstraints c2 = new GridBagConstraints();
		c2.gridx = 14;
		c2.gridy = 4;
		add(extraSpace2, c2);
	
		//Red
		JLabel extraSpace3 = new JLabel();
		extraSpace3.setMinimumSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
		extraSpace3.setPreferredSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
		extraSpace3.setMaximumSize(new Dimension((3 * SIDE_LENGTH), (3 * SIDE_LENGTH)));
		extraSpace3.setBackground(NAVY);
		extraSpace3.setOpaque(true);
		
		GridBagConstraints c3 = new GridBagConstraints();
		c3.gridx = 11;
		c3.gridy = 14;
		add(extraSpace3, c3);
		
		//Blue
		JLabel extraSpace4 = new JLabel();
		extraSpace4.setMinimumSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
		extraSpace4.setPreferredSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
		extraSpace4.setMaximumSize(new Dimension((3 * SIDE_LENGTH), (3 * SIDE_LENGTH)));
		extraSpace4.setBackground(NAVY);
		extraSpace4.setOpaque(true);
		
		GridBagConstraints c4 = new GridBagConstraints();
		c4.gridx = 1;
		c4.gridy = 11;
		add(extraSpace4, c4);	
	}
	
	/**
	 * Initializes the transparent spaces connected with backend along the perimeter of the game board
	 */
	private void initializeSpaces(UUID[] spaces)
	{	
		int uuid = -1;
		//Top row
		for (int i = 0; i < (NUM_SPACES_PER_SIDE - 1); ++i)
		{
			uuid++;//TODO remember how ++i vs i++ works and combine with line below.
			Space space = gameSystem.getSpace(spaces[uuid]);
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = i;
			c.gridy = 0;

			space.addMouseListener(controller); //TODO: Can this be done in the Space class?
			add(space, c);
		}
		
		//Right column
		for (int i = 0; i < (NUM_SPACES_PER_SIDE - 1); ++i)
		{
			uuid++;//TODO remember how ++i vs i++ works and combine with line below.
			Space space = gameSystem.getSpace(spaces[uuid]);
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = (NUM_SPACES_PER_SIDE - 1);
			c.gridy = i;
			
			space.addMouseListener(controller); //TODO: Can this be done in the Space class?
			add(space, c);
		}
		
		//Bottom row
		for (int i = (NUM_SPACES_PER_SIDE - 1); i > 0 ; --i)
		{
			uuid++;//TODO remember how ++i vs i++ works and combine with line below.
			Space space = gameSystem.getSpace(spaces[uuid]);
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = i;
			c.gridy = (NUM_SPACES_PER_SIDE - 1);
			
			space.addMouseListener(controller); //TODO: Can this be done in the Space class?
			add(space, c);
		}
		
		//Left column
		for (int i = (NUM_SPACES_PER_SIDE - 1); i > 0 ; --i)
		{
			uuid++;//TODO remember how ++i vs i++ works and combine with line below.
			Space space = gameSystem.getSpace(spaces[uuid]);
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = i;
			
			space.addMouseListener(controller); //TODO: Can this be done in the Space class?
			add(space, c);
		}
	}
	
	/**
	 * Initializes the center piece that contains either a button to draw a card or a card.
	 * Maria's addition to Ryan's code
	 * @throws IOException 
	 */
	private void initializeCardHolder(GameSystem gameSystem, Controller controller) throws IOException
	{
		CardHolder holder = new CardHolder(gameSystem, controller);
		
		//set size
		holder.setPreferredSize(new Dimension(160, 240));
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = 4;
		c.gridheight = 6;
		c.gridx = 6;
		c.gridy = 5;
		
		add(holder, c);
	}
	
	/**
	 * Initializes a "START" space
	 * 
	 * @param color The color of the "START" space
	 * @param gridX	The x-coordinate of the upper-left corner of the "START" space
	 * @param gridY The y-coordinate of the upper-left corner of the "START" space
	 */
	private void initializeStart(UUID[] startSpaces, Color color, int gridX, int gridY)
	{
		//Set "START" size and location
		int startLength = Space.SIDE_LENGTH * 3;
		
		GridBagConstraints c = new GridBagConstraints();
		//3x3 space with the area of 9 normal game board spaces
		c.gridwidth = 3; //FIXME: Use constant?
		c.gridheight = 3; //FIXME: Use constant?
		c.gridx = gridX;
		c.gridy = gridY;
		
		UUID id = null;
		for(int i = 0; i<startSpaces.length; i++) {
			Space space = gameSystem.getSpace(startSpaces[i]);
			if(color == space.getColor())
			{
				id = startSpaces[i];
				break;
			}
		}
		//TODO error handle id = null. Shouldn't come up though.
		TerminalSpace start = (TerminalSpace) gameSystem.getSpace(id);
		start.setPreferredSize(new Dimension(startLength, startLength));
		start.addMouseListener(controller); //TODO: Can this be done in the Space class?
		add(start, c);
		
		//Set lower layer "START"
		JLabel startLabel = new JLabel();
		startLabel.setPreferredSize(new Dimension(startLength, startLength));
		ImageIcon icon = new ImageIcon("images/TerminalImages/" + color + "_Start.png");
		startLabel.setIcon(icon);
		startLabel.setHorizontalAlignment(JLabel.CENTER);
		startLabel.setVerticalAlignment(JLabel.CENTER);
		add(startLabel, c);	
	}
	
	private void initializeLowerLevelSafetyZone(java.awt.Color color, int x1, int y1, int x2, int y2)
	{
		JLabel extraSpace = new JLabel();
		extraSpace.setMinimumSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
		extraSpace.setPreferredSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
		extraSpace.setMaximumSize(new Dimension((3 * SIDE_LENGTH), (3 * SIDE_LENGTH)));
		extraSpace.setBackground(NAVY);
		extraSpace.setOpaque(true);
		
		//Vertical safety zone
		if (x1 == x2)
		{
			if (y1 < y2)
			{
				int i;
				//For each safety zone space
				for (i = y1; i <= y2; ++i)
				{	
					JLabel safeSpace = new JLabel();
					safeSpace.setMinimumSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
					safeSpace.setPreferredSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
					safeSpace.setMaximumSize(new Dimension((3 * SIDE_LENGTH), (3 * SIDE_LENGTH)));
					safeSpace.setBorder(new LineBorder(NAVY, BORDER_WEIGHT));
					safeSpace.setBackground(color);
					safeSpace.setOpaque(true);

					//Set location
					GridBagConstraints c = new GridBagConstraints();
					c.gridx = x1;
					c.gridy = i;

					add(safeSpace, c);
				}
				GridBagConstraints c1 = new GridBagConstraints();
				c1.gridx = x1;
				c1.gridy = i;
				add(extraSpace, c1);
			}
			else
			{
				int i;
				//For each safety zone space
				for (i = y1; i >= y2; --i)
				{	
					JLabel safeSpace = new JLabel();
					safeSpace.setMinimumSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
					safeSpace.setPreferredSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
					safeSpace.setMaximumSize(new Dimension((3 * SIDE_LENGTH), (3 * SIDE_LENGTH)));
					safeSpace.setBorder(new LineBorder(NAVY, BORDER_WEIGHT));
					safeSpace.setBackground(color);
					safeSpace.setOpaque(true);
	
					//Set location
					GridBagConstraints c = new GridBagConstraints();
					c.gridx = x1;
					c.gridy = i;
					
					add(safeSpace, c);
				}
				GridBagConstraints c1 = new GridBagConstraints();
				c1.gridx = x1;
				c1.gridy = i;
				add(extraSpace, c1);
			}
		}
		//Horizontal safety zone
		else if (y1 == y2)
		{
			if (x1 < x2)
			{
				int i;
				//For each safety zone space
				for (i = x1; i <= x2; ++i)
				{				
					JLabel safeSpace = new JLabel();
					safeSpace.setMinimumSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
					safeSpace.setPreferredSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
					safeSpace.setMaximumSize(new Dimension((3 * SIDE_LENGTH), (3 * SIDE_LENGTH)));
					safeSpace.setBorder(new LineBorder(NAVY, BORDER_WEIGHT));
					safeSpace.setBackground(color);
					safeSpace.setOpaque(true);
					
					//Set location
					GridBagConstraints c = new GridBagConstraints();
					c.gridx = i;
					c.gridy = y1;

					add(safeSpace, c);
				}
				GridBagConstraints c1 = new GridBagConstraints();
				c1.gridx = i;
				c1.gridy = y1;
				add(extraSpace, c1);
			}
			else
			{
				int i;
				//For each safety zone space
				for (i = x1; i >= x2; --i)
				{				
					JLabel safeSpace = new JLabel();
					safeSpace.setMinimumSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
					safeSpace.setPreferredSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
					safeSpace.setMaximumSize(new Dimension((3 * SIDE_LENGTH), (3 * SIDE_LENGTH)));
					safeSpace.setBorder(new LineBorder(NAVY, BORDER_WEIGHT));
					safeSpace.setBackground(color);
					safeSpace.setOpaque(true);
					
					//Set location
					GridBagConstraints c = new GridBagConstraints();
					c.gridx = i;
					c.gridy = y1;

					add(safeSpace, c);
				}
				GridBagConstraints c1 = new GridBagConstraints();
				c1.gridx = i;
				c1.gridy = y1;
				add(extraSpace, c1);
			}
		}
	}
	
	/**
	 * Initializes the spaces of a safety zone
	 * 
	 * @param color The color of the safety zone
	 * @param x1 The x-coordinate of the first safety zone space
	 * @param y1 The y-coordinate of the first safety zone space
	 * @param x2 The x-coordinate of the last safety zone space
	 * @param y2 The y-coordinate of the last safety zone space
	 */
	private void initializeSafetyZone(UUID[] safeStartSpaces, Color color, int x1, int y1, int x2, int y2)
	{
		UUID id = null;
		for(int i = 0; i<safeStartSpaces.length; i++) {
			Space space = gameSystem.getSpace(safeStartSpaces[i]);
			if(color == space.getColor())
			{
				id = safeStartSpaces[i];
				break;
			}
		}

		//TODO error handle id = null. Shouldn't come up though.
		Space safe = gameSystem.getSpace(id);
		
		//Vertical safety zone
		if (x1 == x2)
		{
			if (y1 < y2)
			{
				//For each safety zone space
				for (int i = y1; i <= y2; ++i)
				{	
					//Set location
					GridBagConstraints c = new GridBagConstraints();
					c.gridx = x1;
					c.gridy = i;
					
					safe.addMouseListener(controller); //TODO: Can this be done in the Space class?
					add(safe, c);
					if(safe.getSafeNextID() != null)
						safe = gameSystem.getSpace(safe.getSafeNextID());
				}
			}
			else
			{
				//For each safety zone space
				for (int i = y1; i >= y2; --i)
				{	
					//Set location
					GridBagConstraints c = new GridBagConstraints();
					c.gridx = x1;
					c.gridy = i;
					
					safe.addMouseListener(controller); //TODO: Can this be done in the Space class?
					add(safe, c);
					if(safe.getSafeNextID() != null)
						safe = gameSystem.getSpace(safe.getSafeNextID());
				}
			}

		}
		
		//Horizontal safety zone
		else if (y1 == y2)
		{
			if (x1 < x2)
			{
				//For each safety zone space
				for (int i = x1; i <= x2; ++i)
				{									
					//Set location
					GridBagConstraints c = new GridBagConstraints();
					c.gridx = i;
					c.gridy = y1;
					
					safe.addMouseListener(controller); //TODO: Can this be done in the Space class?
					add(safe, c);
					if(safe.getSafeNextID() != null)
						safe = gameSystem.getSpace(safe.getSafeNextID());
				}	
			}
			else
			{
				//For each safety zone space
				for (int i = x1; i >= x2; --i)
				{									
					//Set location
					GridBagConstraints c = new GridBagConstraints();
					c.gridx = i;
					c.gridy = y1;
					
					safe.addMouseListener(controller); //TODO: Can this be done in the Space class?
					add(safe, c);
					if(safe.getSafeNextID() != null)
						safe = gameSystem.getSpace(safe.getSafeNextID());
				}
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
	private void initializeHome(UUID[] safeStart, Color color, int gridX, int gridY)
	{
		//Set "HOME" size and location
		int homeLength = Space.SIDE_LENGTH * 3;
		
		GridBagConstraints c = new GridBagConstraints();
		int homeGridLength = 3;
		c.gridwidth = homeGridLength; //FIXME: make static?
		c.gridheight = homeGridLength; //FIXME: make static?
		c.gridx = gridX;
		c.gridy = gridY;
		
		//Set top layer transparent "HOME"
		UUID safeID = null;
		for(int i = 0; i<safeStart.length; i++) {
			Space space = gameSystem.getSpace(safeStart[i]);
			if(color == space.getColor())
			{
				safeID = safeStart[i];
				break;
			}
		}
		
		UUID id = gameSystem.getSpacesAway(gameSystem.getSpace(safeID), 5);
		//TODO error handle id = null. Shouldn't come up though.
		TerminalSpace home = (TerminalSpace) gameSystem.getSpace(id);
		home.setPreferredSize(new Dimension(homeLength, homeLength));
		home.addMouseListener(controller); //TODO: Can this be done in the Space class?
		add(home, c);
		
		//Set lower layer "HOME"
		JLabel homeLabel = new JLabel();
		homeLabel.setPreferredSize(new Dimension(homeLength, homeLength));
		ImageIcon icon = new ImageIcon("images/TerminalImages/" + color + "_Home.png");
		homeLabel.setIcon(icon);
		homeLabel.setHorizontalAlignment(JLabel.CENTER);
		homeLabel.setVerticalAlignment(JLabel.CENTER);
		add(homeLabel, c);	
	}
	
	/**
	 * Display each player's four tokens on the game board
	 */
	private void displayTokens()
	{
		ArrayList<Player> players = gameSystem.getPlayers();
		
		for(Player player : players)
		{
			Color color = player.getColor();
			Token tokens[] = player.getTokens();
			
			for(Token token : tokens)
			{
				Space space = gameSystem.getSpace(token.getSpaceID());
				
				ImageIcon icon = new ImageIcon("images/TokenImages/" + color + "_Token.png");
				space.setIcon(icon);
				space.setHorizontalAlignment(JLabel.CENTER);
				space.setVerticalAlignment(JLabel.CENTER);
			}	
		}	
	}
	
	// Update text showing numbers of tokens in start and home
	private void updateTokenCounts() {
		int[] tokensInStart = gameSystem.getTokensInStart();
		int[] tokensInHome = gameSystem.getTokensInHome();
		UUID[] startIDs = gameSystem.getStartIDs();
		UUID[] homeIDs = gameSystem.getHomeIDs();
		
		for(int player=0; player<4; player++) {
			if(tokensInStart[player] == 0) {
				gameSystem.getSpace(startIDs[player]).setText("");
			}
			else {
				gameSystem.getSpace(startIDs[player]).setText("x"+tokensInStart[player]);
			}
			if(tokensInHome[player] == 0) {
				gameSystem.getSpace(homeIDs[player]).setText("");
			}
			else {
				gameSystem.getSpace(homeIDs[player]).setText("x"+tokensInHome[player]);
			}
		}
	}

	@Override
	public void updated() 
	{
		displayTokens();
		updateTokenCounts();
	}
}
