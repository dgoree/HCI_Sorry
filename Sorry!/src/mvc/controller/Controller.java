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
				
				System.out.println("Player " + gameSystem.getTurn() + "'s Turn (" + currentPlayer.getColor() + ")");	
			}

		}
		else if(e.getActionCommand().equals(GameFrame.QUIT_GAME_COMMAND))
		{
			//TODO quit game elegantly with possibility of starting a new one without closing the program?
			System.out.println("game ended"); //debug print. TODO: remove
			System.exit(0);
		}

		else if(e.getActionCommand().equals(GameFrame.HELP_COMMAND))
		{
			String rules = "I'll come back and add in the actual "
					+ "rules... maybe a scroll bar if they're long enough... fun things like that.";
			
			JTextArea text = new JTextArea(rules);
			text.setLineWrap(true);
			text.setWrapStyleWord(true);
			JScrollPane pane = new JScrollPane(text);
			JOptionPane.showMessageDialog(null, "<html><p style='width:200px;'>" + rules + "</p></html>", "Game Play Information", JOptionPane.PLAIN_MESSAGE);
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
			if (!selectedTokenMoves.isEmpty() && selectedTokenMoves.contains(spaceClicked.getId()))
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