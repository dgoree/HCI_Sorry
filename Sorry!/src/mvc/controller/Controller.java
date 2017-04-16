package mvc.controller;

import mvc.view.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JMenuItem;

import mvc.model.GameSystem;

public class Controller implements ActionListener
{
	private final GameSystem gameSystem;
	
	public Controller(final GameSystem gameSystem)
	{
		this.gameSystem = gameSystem;
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equals(CardHolder.DRAW_CARD_COMMAND))
		{
			if(gameSystem.isGameInProgress())
			{
				//They clicked a button to draw a card.
				gameSystem.takeTurn();
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
				gameSystem.playGame();
			}
			else
			{
				//TODO confirm they want to restart the game.
				gameSystem.playGame(); 
			}
			System.out.println("start a new game"); //debug print. TODO: remove
		}
		else if(e.getActionCommand().equals(GameFrame.QUIT_GAME_COMMAND))
		{
			//TODO quit game elegantly with posibility of starting a new one without closing the program?
			System.out.println("game ended"); //debug print. TODO: remove
			System.exit(0);
		}
		
	}

}
