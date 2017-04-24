package mvc.controller;

import mvc.view.*;
import spaces.Space;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import agents.Player;
import gameItems.Token;
import mvc.model.GameSystem;

public class Controller implements ActionListener, MouseListener
{
	private final GameSystem gameSystem;
	
	private Player currentPlayer;
	private Token currentPlayerTokens[];
	private UUID currentPlayerTokenIDs[];
	private ArrayList<UUID> currentPlayerMoves;
	private Token selectedToken;
	
	public Controller(final GameSystem gameSystem)
	{
		this.gameSystem = gameSystem;
	}
	
	/**
	 * Advances turn to next player and allows player to draw a card
	 * FIXME: Buggy! Not correctly reverting deck to draw button. Skipping turns.
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
		currentPlayerMoves = new ArrayList<UUID>(); //No moves before card has been drawn
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
					System.out.println("No possible moves."); //debug print TODO: remove
					gameSystem.setNoPossibleMoves(true);
					JOptionPane.showMessageDialog(null, "You have no possible moves.", "Forfeit Your Turn", JOptionPane.PLAIN_MESSAGE);
					
					//Advance turn
					advanceTurn();
					
					System.out.println("Player " + gameSystem.getTurn() + "'s Turn (" + currentPlayer.getColor() + ")"); //debug print TODO: remove
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
				gameSystem.newGame();
				System.out.println("start a new game"); //debug print. TODO: remove
				
				//Initialize current player to first player
				currentPlayer = gameSystem.getPlayers().get(gameSystem.getTurn());
				currentPlayerTokens = currentPlayer.getTokens();
				currentPlayerTokenIDs = new UUID[currentPlayerTokens.length];
				for(int i = 0; i < currentPlayerTokens.length; i++)
				{
					currentPlayerTokenIDs[i] = currentPlayerTokens[i].getSpaceID();
				}
				currentPlayerMoves = new ArrayList<UUID>(); //No moves before card has been drawn
				
				System.out.println("Player " + gameSystem.getTurn() + "'s Turn (" + currentPlayer.getColor() + ")"); //debug print TODO: remove	
			}
			else
			{
				//TODO confirm they want to restart the game.
				gameSystem.newGame(); 
			}

		}
		else if(e.getActionCommand().equals(GameFrame.QUIT_GAME_COMMAND))
		{
			//TODO quit game elegantly with possibility of starting a new one without closing the program?
			System.out.println("game ended"); //debug print. TODO: remove
			System.exit(0);
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
			
		//If player clicked a space occupied by own token
		if (currentPlayerTokenIDsList.contains(spaceClicked.getId()))
		{				
			//Get corresponding token
			for(Token token : currentPlayerTokens)
			{
				if (token.getSpaceID() == spaceClicked.getId())
				{
					selectedToken = token;
				}
			}
			
			//Show token's possible moves
			//FIXME: Set color of spaces back to white
			ArrayList<UUID> selectedTokenMoves = selectedToken.getMoves();
			for(UUID move : selectedTokenMoves)
			{
				gameSystem.getSpace(move).setBackground(Color.MAGENTA); //FIXME: choose color
			}
		}
		else
		{			
			//Check if user clicked a legal destination space
			if (currentPlayerMoves.contains(spaceClicked.getId()))
			{				
				//Save current location of token
				UUID tokenPrevLocation = selectedToken.getSpaceID();
				
				//Move token to destination space
				gameSystem.moveToken(selectedToken, spaceClicked.getId());
				
				//Remove icon from token's previous location
				//FIXME: Removes icon from start if other pieces still in start
				gameSystem.getSpace(tokenPrevLocation).setIcon(null);
				
				//Advance turn
				advanceTurn();	
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
