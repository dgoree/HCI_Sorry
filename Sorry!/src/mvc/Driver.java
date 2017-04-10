package mvc;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;

import mvc.view.GameBoard;
import mvc.controller.Controller;
import mvc.model.GameSystem;

public class Driver 
{
	private static final int MIN_WIDTH = 700;
	private static final int MIN_HEIGHT = 700;
	
	public static void main(String[] args) throws IOException {
		
		//GameSystem gameSystem = new GameSystem();
		
		//Create window
		JFrame frame = new JFrame();
		frame.setSize(MIN_WIDTH, MIN_HEIGHT);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null); //Centers window on screen
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout(0, 0));
		
		GameSystem gameSystem = new GameSystem();
		Controller controller = new Controller(gameSystem);
				
		//Create game board and add to window
		GameBoard gameBoard = new GameBoard(gameSystem, controller);
		frame.add(gameBoard, BorderLayout.CENTER);
				
		frame.setVisible(true);
	}
}
