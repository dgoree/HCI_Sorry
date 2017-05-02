package mvc.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import mvc.controller.Controller;
import mvc.model.GameSystem;
import mvc.model.Listener;

/**
 * GameFrame creates the window to display the user interface
 * 
 * @author ryanbrowning
 */
public class GameFrame extends JFrame implements Listener
{
	public static final String NEW_GAME_COMMAND = "new game";
	public static final String QUIT_GAME_COMMAND = "quit game";
	//public static final String HELP_COMMAND = "help";
	public static final String GENERAL_RULES = "general";
	public static final String THE_BOARD_COMMAND = "board";
	public static final String CARD_COMMAND = "cards";
	static final int MIN_WIDTH = 700;
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
		gameSystem.addListener(this);
		
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
		
		//Create menu bar and add to window
		JMenuBar menuBar = new JMenuBar();
		JButton newGame = new JButton("New Game");
		JButton quitGame = new JButton("Quit Game");
		JToggleButton help = new JToggleButton("Help...");
		newGame.setActionCommand(NEW_GAME_COMMAND);
		quitGame.setActionCommand(QUIT_GAME_COMMAND);
		//help.setActionCommand(HELP_COMMAND);
		newGame.addActionListener(controller);
		quitGame.addActionListener(controller);
		//help.addActionListener(controller);
		
		JPopupMenu helpOptions = new JPopupMenu();
		
		JMenuItem general = new JMenuItem("General Game Play");
		general.setActionCommand(GENERAL_RULES);
		general.addActionListener(controller);
		helpOptions.add(general);
		
		JMenuItem board = new JMenuItem("The Board");
		board.setActionCommand(THE_BOARD_COMMAND);
		board.addActionListener(controller);
		helpOptions.add(board);
		
		JMenuItem cards = new JMenuItem("The Cards");
		cards.setActionCommand(CARD_COMMAND);
		cards.addActionListener(controller);
		helpOptions.add(cards);
		
		
		help.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev){
				JToggleButton b = help;
				if (b.isSelected()) {
					helpOptions.show(b,  0,  b.getBounds().height);
				} else {
					helpOptions.setVisible(false);
				}
			}
		});

		menuBar.add(newGame);
		menuBar.add(quitGame);
		menuBar.add(help);
		add(menuBar, BorderLayout.NORTH);
		
		//Create game board and add to window
		GameBoard gameBoard = new GameBoard(gameSystem, controller);
		add(gameBoard, BorderLayout.CENTER);
	}
	
	public void updated()
	{
		if (gameSystem.getChangeNameMenu())
		{
			JTextField  name1 = new JTextField(gameSystem.getPlayerName(0));
			JTextField  name2 = new JTextField(gameSystem.getPlayerName(1));
			JTextField  name3 = new JTextField(gameSystem.getPlayerName(2));
			JTextField  name4 = new JTextField(gameSystem.getPlayerName(3));
			
			JPanel panel = new JPanel(new GridLayout(0, 1));
			panel.add(new JLabel("Change Player Names?"));
			
			panel.add(new JLabel("Red Player:"));
			panel.add(name1);
			panel.add(new JLabel("Blue Player:"));
			panel.add(name2);
			panel.add(new JLabel("Yellow Player:"));
			panel.add(name3);
			panel.add(new JLabel("Green Player:"));
			panel.add(name4);
			
			JOptionPane pane = new JOptionPane();
			pane.add(panel);
			
			
			int result = JOptionPane.showConfirmDialog(null, panel, "Options!",
		            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		    if (result == JOptionPane.OK_OPTION) 
		    {
		    	//FIXME: I (Maria) broke MVC...
		    	gameSystem.setPlayerNames(name1.getText(), 0);
		    	gameSystem.setPlayerNames(name2.getText(), 1);
		    	gameSystem.setPlayerNames(name3.getText(), 2);
		    	gameSystem.setPlayerNames(name4.getText(), 3);
		    } 
		    else 
		    {
		    	System.out.println("Cancelled");
		    }
		}
	}
}
