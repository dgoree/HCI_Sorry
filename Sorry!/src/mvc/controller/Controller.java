package mvc.controller;

import mvc.view.*;
import spaces.Space;
import spaces.TerminalSpace;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import agents.Player;
import gameItems.Token;
import mvc.model.GameSystem;

public class Controller implements ActionListener, MouseListener
{
	private final GameSystem gameSystem;

	private Player currentPlayer;
	private Token currentPlayerTokens[];
	private UUID currentPlayerTokenIDs[];
	private ArrayList<UUID> currentPlayerMoves = new ArrayList<UUID>();
	private Token selectedToken;
	private ArrayList<UUID> selectedTokenMoves = new ArrayList<UUID>();
	private ArrayList<UUID> magentaSpaces = new ArrayList<UUID>();

	public Controller(final GameSystem gameSystem)
	{
		this.gameSystem = gameSystem;
	}

	/**
	 * Advances turn to next player and allows player to draw a card
	 */
	private void advanceTurn()
	{
		gameSystem.advanceTurn();

		//Update current player
		currentPlayer = gameSystem.getPlayers().get(gameSystem.getTurn());
		currentPlayerTokens = currentPlayer.getTokens();
		currentPlayerTokenIDs = new UUID[currentPlayerTokens.length];
		for(int i = 0; i < currentPlayerTokens.length; i++)
		{
			currentPlayerTokenIDs[i] = currentPlayerTokens[i].getSpaceID();
		}
		currentPlayerMoves.clear(); //No moves before card has been drawn
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equals(CardHolder.DRAW_CARD_COMMAND))
		{
			if(gameSystem.isGameInProgress())
			{
				//They clicked a button to draw a card.
				gameSystem.drawCard();

				int debug = gameSystem.getCardNum();
				if(debug == 0) {
					int stop = 0;
				}

				//Get all possible moves across each token for current player
				for(Token token : currentPlayerTokens)
				{
					ArrayList<UUID> tokenMoves = token.getMoves();
					for(UUID move : tokenMoves)
					{
						currentPlayerMoves.add(move);
					}
				}

				//If current player has no possible moves, alert player then advance turn
				if(currentPlayerMoves.isEmpty())
				{
					//Alert player
					gameSystem.setNoPossibleMoves(true);

					//FIXME: Does this need to be in the View? Or is it ok in the Controller?
					JOptionPane.showMessageDialog(null, "You have no possible moves.", "Forfeit Your Turn", JOptionPane.PLAIN_MESSAGE);

					//Advance turn
					advanceTurn();
				}	
			}
			else
			{
				//TODO: notify the user they must start a game?
			}
		}
		else if(e.getActionCommand().equals(GameFrame.NEW_GAME_COMMAND))
		{
			if(!gameSystem.isGameInProgress())
			{
				//Remove all token icons currently on game board
				for (Player player : gameSystem.getPlayers())
				{
					for (Token token : player.getTokens())
					{
						gameSystem.getSpace(token.getSpaceID()).setIcon(null);
						gameSystem.getSpace(token.getSpaceID()).setText(null);
					}
				}
				
				//Start new game
				gameSystem.setChangeNameMenu();
				gameSystem.newGame();

				//Initialize current player to first player
				currentPlayer = gameSystem.getPlayers().get(gameSystem.getTurn());
				currentPlayerTokens = currentPlayer.getTokens();
				currentPlayerTokenIDs = new UUID[currentPlayerTokens.length];
				for(int i = 0; i < currentPlayerTokens.length; i++)
				{
					currentPlayerTokenIDs[i] = currentPlayerTokens[i].getSpaceID();
				}
				currentPlayerMoves.clear(); //No moves before card has been drawn
			}
			else
			{
				//TODO confirm they want to restart the game.
				int newGame = JOptionPane.showConfirmDialog(null, "Are you sure you want to start a new game?", "New Game", JOptionPane.YES_NO_OPTION);
				if(newGame == JOptionPane.YES_OPTION)
				{
					//Remove all token icons currently on game board				
					for (Player player : gameSystem.getPlayers())
					{
						for (Token token : player.getTokens())
						{
							gameSystem.getSpace(token.getSpaceID()).setIcon(null);
							gameSystem.getSpace(token.getSpaceID()).setText(null);
						}
					}

					//Start new game
					gameSystem.setChangeNameMenu();
					gameSystem.newGame(); 

					//Initialize current player to first player
					currentPlayer = gameSystem.getPlayers().get(gameSystem.getTurn());
					currentPlayerTokens = currentPlayer.getTokens();
					currentPlayerTokenIDs = new UUID[currentPlayerTokens.length];
					for(int i = 0; i < currentPlayerTokens.length; i++)
					{
						currentPlayerTokenIDs[i] = currentPlayerTokens[i].getSpaceID();
					}
					currentPlayerMoves.clear(); //No moves before card has been drawn
				}
			}

		}
		else if(e.getActionCommand().equals(GameFrame.QUIT_GAME_COMMAND))
		{
			int quitGame = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit the game?", "Quit Game", JOptionPane.YES_NO_OPTION);
			if(quitGame == JOptionPane.YES_OPTION)
			{
				System.exit(0);
			}
		}
		else if(e.getActionCommand().equals(GameFrame.GENERAL_RULES))
		{
			String rules1 ="  To be the first player to get all four of your pawns " +
					"from your color START to your color HOME";
			String rules2 = "  The red player moves first.  Movement is clockwise around the "
					+ "board and the play passes clockwise."
					+ " Only a 1 card or a 2 card can move you out of START.  If it's your"
					+ " first turn and you do not draw a card that lets you "
					+ "start a pawn out, you forfeit (skip) your turn.  On all turns you "
					+ "draw a card and move accordingly if you can.";
			String rules3 = "  You may jump over your own or another player's "
					+ "pawn that's in your way, counting it as one space.  BUT if you land on a space"
					+ "that's already occupied by a pawn, bump that pawn back to its own color "
					+ "START space.";
			String rules4 =  "  If at any time you can move, you must move, even if "
					+ "it's to your disadvantage.";
			
			JOptionPane.showMessageDialog(null,  "<html><p style='width:200px;'><Strong>Object:</Strong>" + rules1 + "</p></html>" + "\n"
			+ "<html><p style='width:200px;'><Strong>Game Play:</Strong>"
			+ rules2 + "</p></html>" + "\n" + "<html><p style='width:200px;'><Strong>Jumping and Bumping:</Strong>"
			+ rules3 + "</p></html>" + "\n" + "<html><p style='width:200px;'><Strong>Note:</Strong>"
			+ rules4 + "</p></html>", "Game Play Information", JOptionPane.PLAIN_MESSAGE);
		}
		else if(e.getActionCommand().equals(GameFrame.THE_BOARD_COMMAND))
		{
			String home = "  You must bring all four of your pawns into HOME by "
					+ "exact count!  Once HOME, do not move that pawn again for the rest of the game.";
			String slide = "  Regardless of which card sent you there, any time you land by exact "
					+ "count on the triangle at the beginning of a slide that is not your own "
					+ "color, slide ahead to the end and bump any pawns in your way--including your own!--"
					+ " back to their own START spaces.  If you land on a slide of your own color, don't "
					+ "slide.  Just stay on the triangle.";
			String safety = "  Only you many enter your own color safety zone.  All other rules apply.  "
					+ "No pawn may enter its safety zone by a backward move; however a pawn may move backward out of "
					+ "its safety zone and on subsequent turns move back into the zone as cards permit.";
			JOptionPane.showMessageDialog(null,  "<html><p style='width:200px;'><Strong>Home:</Strong>" + home + "</p></html>" + "\n"
					+ "<html><p style='width:200px;'><Strong>Slides:</Strong>"
					+ slide + "</p></html>" + "\n" + "<html><p style='width:200px;'><Strong>Safety Zones:</Strong>"
					+ safety + "</p></html>", "Game Board Information", JOptionPane.PLAIN_MESSAGE);
		}
		else if(e.getActionCommand().equals(GameFrame.CARD_COMMAND))
		{
			String one = " Either start a pawn out OR move one pawn forward 1 space.";
			String two = " Either start a pawn out OR move one pawn forward 2 spaces.  Whichever you "
					+ "do--or even if you couldn't move--DRAW AGAIN and move accordingly.";
			String three = "  Move one pawn forward 3 spaces.";
			String four = "  Move one pawn backwards 4 spaces.";
			String five = "  Move one pawn forward 5 spaces.";
			String seven = "  Either move one pawn forward 7 spaces OR split the forward move between any two "
					+ "pawns.  You may not use 7 to start a pawn, and if you use part of the 7 to get a "
					+ "pawn HOME, you must be able to use the balance of the move for another pawn.";
			String eight = "  Move one pawn forward 8 spaces.";
			String ten = "  Either move one pawn forward 10 spaces OR move one pawn backwards 1 space.";
			String eleven = "  Move one pawn forward 11 spaces OR switch any one of your pawns with one of any opponent's."
					+ "  If your switch lands you on a triangle at the beginning of another player's slide, slide to the end!";
			String twelve = "  Move one pawn forward 12 spaces.";
			String sorry = "  Take one pawn from your START, place it on any space that is occupied by any opponent, "
					+ "and bump that opponent's pawn back to its START.  If there is no pawn on your START or no "
					+ "opponent's pawn on any space you can move to, you forfeit your move.";
			JOptionPane.showMessageDialog(null,  "<html><p style='width:200px;'><Strong>1:</Strong>" + one + "</p></html>" + "\n"
					+ "<html><p style='width:200px;'><Strong>2:</Strong>"
					+ two + "</p></html>" + "\n" + "<html><p style='width:200px;'><Strong>3:</Strong>"
					+ three + "</p></html>" + "\n" + "<html><p style='width:200px;'><Strong>4:</Strong>"
					+ four + "</p></html>"+ "\n"
					+ "<html><p style='width:200px;'><Strong>5:</Strong>"
					+ five + "</p></html>" + "\n" + "<html><p style='width:200px;'><Strong>7:</Strong>"
					+ seven + "</p></html>" + "\n" + "<html><p style='width:200px;'><Strong>8:</Strong>"
					+ eight + "</p></html>" + "\n"
					+ "<html><p style='width:200px;'><Strong>10:</Strong>"
					+ ten + "</p></html>" + "\n" + "<html><p style='width:200px;'><Strong>11:</Strong>"
					+ eleven + "</p></html>" + "\n" + "<html><p style='width:200px;'><Strong>12:</Strong>"
					+ twelve + "</p></html>" + "\n" + "<html><p style='width:200px;'><Strong>Sorry!:</Strong>"
					+ sorry + "</p></html>", "Game Play Information", JOptionPane.PLAIN_MESSAGE);
		}
	}

	/**
	 * Highlight possible moves when current player clicks on one of his own tokens.
	 * Move token when player clicks on a destination space.
	 * 
	 * @param e the MouseEvent
	 */
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		Space spaceClicked = (Space) e.getSource(); //Only added MouseListeners to spaces
		
		//Convert array to list
		if(currentPlayerTokenIDs == null)
			return;
		List<UUID> currentPlayerTokenIDsList = Arrays.asList(currentPlayerTokenIDs);

		//If player clicked a space occupied by own token (except home)
		if (currentPlayerTokenIDsList.contains(spaceClicked.getId()) && !spaceClicked.isHome())
		{
			//reset magenta spaces if necessary
			for(UUID id: magentaSpaces) {
				gameSystem.getSpace(id).setBackground((new JLabel()).getBackground());
				gameSystem.getSpace(id).setOpaque(false);
				gameSystem.getSpace(id).getParent().setComponentZOrder(gameSystem.getSpace(id), 0);
			}
			magentaSpaces.clear();

			//Get corresponding token
			for(Token token : currentPlayerTokens)
			{
				if (token.getSpaceID() == spaceClicked.getId())
				{
					selectedToken = token;
					break;
				}
			}

			//Show token's possible moves
			selectedTokenMoves = selectedToken.getMoves();
			if(selectedTokenMoves == null)
				return;
			for(UUID move : selectedTokenMoves)
			{
				gameSystem.getSpace(move).setBackground(Color.MAGENTA);
				gameSystem.getSpace(move).setOpaque(true);
				magentaSpaces.add(gameSystem.getSpace(move).getId());
			}
		}
		else
		{	
			//Check if user clicked a legal destination space for the selected token
			if (selectedTokenMoves != null && !selectedTokenMoves.isEmpty() && selectedTokenMoves.contains(spaceClicked.getId()))
			{				
				//Save current location of token
				UUID tokenPrevLocation = selectedToken.getSpaceID();

				//Move token to destination space
				gameSystem.moveToken(selectedToken, spaceClicked.getId());

				//Remove icon from token's previous location
				//Unless that location is start and there are still other tokens there
				if(!Arrays.asList(gameSystem.getStartIDs()).contains(tokenPrevLocation) || currentPlayer.numTokensInStart() == 0) {
					gameSystem.getSpace(tokenPrevLocation).setIcon(null);
				}

				//reset magenta spaces
				for(UUID id: magentaSpaces) {
					gameSystem.getSpace(id).setBackground((new JLabel()).getBackground());
					gameSystem.getSpace(id).setOpaque(false);
					gameSystem.getSpace(id).getParent().setComponentZOrder(gameSystem.getSpace(id), 0);
				}
				magentaSpaces.clear();
				selectedTokenMoves.clear();

				//End game or advance turn
				if(!gameSystem.isGameInProgress()) {
					JOptionPane.showMessageDialog(null, gameSystem.getPlayerName()+" won!", "Game Over", JOptionPane.PLAIN_MESSAGE);
				}
				else if(!gameSystem.isSecondSevenMove()) {
					advanceTurn();
				}
			}	
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

}