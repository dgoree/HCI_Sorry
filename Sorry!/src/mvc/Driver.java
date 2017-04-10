package mvc;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;

import mvc.view.GameBoard;
import mvc.controller.Controller;
import mvc.model.GameSystem;
import mvc.view.GameFrame;

public class Driver 
{
	private static final int MIN_WIDTH = 700;
	private static final int MIN_HEIGHT = 700;
	
	public static void main(String[] args) throws IOException 
	{
		
		GameSystem gameSystem = new GameSystem();
		Controller controller = new Controller(gameSystem);
		
		GameFrame gameFrame = new GameFrame(gameSystem, controller);
	}
}
