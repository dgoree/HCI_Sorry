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
			//TODO check to make sure a card can be drawn.
			//They clicked a button to draw a card.
			gameSystem.takeTurn();
		}
		else if(e.getActionCommand().equals(GameFrame.NEW_GAME_COMMAND))
		{
			//TODO start a new game
			System.out.println("start a new game"); //debug print. TODO: remove
		}
		else if(e.getActionCommand().equals(GameFrame.QUIT_GAME_COMMAND))
		{
			//TODO quit game
			System.out.println("game ended"); //debug print. TODO: remove
		}
		
	}

}
