package mvc.controller;

import mvc.view.CardHolder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

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
		if(e.getSource() instanceof JButton)
		{
			if(((JButton)e.getSource()).getText().equals("<html><center>" + "Click to draw a card"+"</center></html>"))
			{
				//They clicked a button to draw a card.
				gameSystem.takeTurn();
			}
		}
		
	}

}
