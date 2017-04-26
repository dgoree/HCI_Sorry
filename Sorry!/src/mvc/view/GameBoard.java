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
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import agents.Player;
import gameItems.Token;
import mvc.controller.Controller;
import mvc.model.GameSystem;
import mvc.model.Listener;
import spaces.Space;
import spaces.TerminalSpace;
import utilities.Color;
import utilities.TerminalType;

public class GameBoard extends JPanel implements Listener
{	
	//The number of spaces along a side of the square game board
	private static final int NUM_SPACES_PER_SIDE = 16;
	private final GameSystem gameSystem;
	private final Controller controller;
	private final int[] tokensInStart = new int[4]; //indexed by player
	private final int[] tokensInHome = new int[4]; //indexed by player
	
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
		initializeSlides();
		initializeSpaces(gameSystem.getSpaceIDs());
		initializeCardHolder(gameSystem, controller);//Maria's addition

		UUID[] startIDs = gameSystem.getStartIDs();
		UUID[] safeStartIDs = gameSystem.getSafeZoneStartSpaces();
		//Yellow
		initializeStart(startIDs, Color.YELLOW, 3, 1); //upper-left corner (3, 1)
		initializeSafetyZone(safeStartIDs, Color.YELLOW, 2, 1, 2, 5); //(2, 1) to (2, 5)
		initializeHome(safeStartIDs, Color.YELLOW, 1, 6); //upper-left corner (1, 6)
		
		//Green
		initializeStart(startIDs, Color.GREEN, 12, 3); //upper-left corner (12, 3)
		initializeSafetyZone(safeStartIDs, Color.GREEN, 10, 2, 14, 2); //(10, 2) to (14, 2)
		initializeHome(safeStartIDs, Color.GREEN, 7, 1); //upper-left corner (7, 1)
		
		//Red
		initializeStart(startIDs, Color.RED, 10, 12); //upper-left corner (10, 12)
		initializeSafetyZone(safeStartIDs, Color.RED, 13, 10, 13, 14); //(13, 10) to (13, 14)
		initializeHome(safeStartIDs, Color.RED, 12, 7); //upper-left corner (12, 7)
		
		//Blue
		initializeStart(startIDs, Color.BLUE, 1, 10); //upper-left corner (1, 10)
		initializeSafetyZone(safeStartIDs, Color.BLUE, 1, 13, 5, 13); //(1, 13) to (5, 13)
		initializeHome(safeStartIDs, Color.BLUE, 6, 12); //upper-left corner (6, 12)
		
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
	 * Initializes the spaces along the perimeter of the game board
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
		
		//Set "START" size and color
		int startLength = Space.SIDE_LENGTH * 3; //FIXME: make static?
		start.setPreferredSize(new Dimension(startLength, startLength));
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
		
		start.addMouseListener(controller); //TODO: Can this be done in the Space class?
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
			//For each safety zone space
			for (int i = y1; i <= y2; ++i)
			{	
				//Set color
				safe.setBackground(color);
				safe.setOpaque(true);

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
		
		//Horizontal safety zone
		else if (y1 == y2)
		{
			//For each safety zone space
			for (int i = x1; i <= x2; ++i)
			{				
				//Set color
				safe.setBackground(color);
				safe.setOpaque(true);
				
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
	
	/**
	 * Initializes a "HOME" space
	 * 
	 * @param color The color of the "HOME" space
	 * @param gridX The x-coordinate of the upper-left corner of the "HOME" space
	 * @param gridY The y-coordinate of the upper-left corner of the "HOME" space
	 */
	private void initializeHome(UUID[] safeStart, Color color, int gridX, int gridY)
	{
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
		
		
		//TerminalSpace home = new TerminalSpace(null, null, null, TerminalType.HOME);
		
		//Set "HOME" size and color
		int homeLength = Space.SIDE_LENGTH * 3;
		home.setPreferredSize(new Dimension(homeLength, homeLength));
		home.setBackground(color);
		home.setOpaque(true);
		
		//Set "HOME" location
		//FIXME: Padding
		GridBagConstraints c = new GridBagConstraints();
		int homeGridLength = 3;
		c.gridwidth = homeGridLength; //FIXME: make static?
		c.gridheight = homeGridLength; //FIXME: make static?
		c.gridx = gridX;
		c.gridy = gridY;
		
		home.addMouseListener(controller); //TODO: Can this be done in the Space class?
		add(home, c);	
	}
	
	/**
	 * Display each player's four tokens on the game board
	 * TODO: How to display four tokens in start and home
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

	@Override
	public void updated() 
	{
		displayTokens();
	}
}
