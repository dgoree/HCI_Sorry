package mvc.view;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;
import mvc.controller.Controller;
import mvc.model.GameSystem;

/**
 * GameFrame creates the window to display the user interface
 * 
 * @author ryanbrowning
 */
public class GameFrame extends JFrame
{
	private static final int MIN_WIDTH = 700;
	private static final int MIN_HEIGHT = 700;
	private final GameSystem gameSystem;
	
	/**
	 * Constructs the window
	 * @throws IOException 
	 */
	public GameFrame(final GameSystem gameSystem, final Controller controller) throws IOException
	{
		super();
		this.gameSystem = gameSystem;
		init(gameSystem, controller);
		setVisible(true);
	}
	
	/**
	 * Initializes the window size and layout
	 * and add the game board to the window
	 * @throws IOException 
	 */
	public void init(final GameSystem gameSystem, final Controller controller) throws IOException
	{
		setSize(MIN_WIDTH, MIN_HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null); //Centers window on screen
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout(0, 0));
		
		//Create game board and add to window
		GameBoard gameBoard = new GameBoard(gameSystem, controller);
		add(gameBoard, BorderLayout.CENTER);
	}

}
