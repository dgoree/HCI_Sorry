package mvc.view;

import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 * GameFrame creates the window to display the user interface
 * 
 * @author ryanbrowning
 */
public class GameFrame extends JFrame
{
	private static final int MIN_WIDTH = 700;
	private static final int MIN_HEIGHT = 700;
	
	/**
	 * Constructs the window
	 */
	public GameFrame()
	{
		super();
		init();
		setVisible(true);
	}
	
	/**
	 * Initializes the window size and layout
	 * and add the game board to the window
	 */
	public void init()
	{
		setSize(MIN_WIDTH, MIN_HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null); //Centers window on screen
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout(0, 0));
		
		//Create game board and add to window
		GameBoard gameBoard = new GameBoard();
		add(gameBoard, BorderLayout.CENTER);
	}

}
