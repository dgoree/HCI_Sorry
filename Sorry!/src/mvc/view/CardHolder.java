package mvc.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Cursor;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import mvc.controller.Controller;
import mvc.model.GameSystem;
import mvc.model.Listener;

/**
 * TODO: Add description
 * 
 * @author mariawolinski
 */
public class CardHolder extends JLabel implements Listener
{
	public static final String DRAW_CARD_COMMAND = "draw card";
	
	private static final Color NAVY = new Color(0, 0, 128);
	private static final int BORDER_WEIGHT = 2;
	private JButton drawCard;
	private final GameSystem gameSystem;
	private ImageIcon image;
	private final Controller controller;
	
	/**
	 * Constructor
	 * @throws IOException 
	 */
	public CardHolder(GameSystem gameSystem, Controller controller) throws IOException
	{
		this.gameSystem = gameSystem;
		this.controller = controller;
		
		gameSystem.addListener(this);
		
		//Set border
		setBorder(new LineBorder(NAVY, BORDER_WEIGHT));
		
		this.setLayout(new GridLayout(1, 1));
		
		drawCard = new JButton("Click to draw" + "\n" +"a card");
		drawCard.setFont(new Font("Comic Sans MS", Font.BOLD, 25));
		drawCard.setText("<html><center>" +"Select New Game :)"+"</center></html>");
		drawCard.setBackground(new Color(153, 214, 255));
		drawCard.setOpaque(true);
		drawCard.setBorderPainted(false);
		drawCard.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		drawCard.setActionCommand(DRAW_CARD_COMMAND);
		drawCard.addActionListener(controller);
		this.add(drawCard);
	}
	
	public void updated()
	{		
		//don't update if a player needs to move a second token
		if(gameSystem.isSecondSevenMove()) {
			return;
		}
		
		//display the draw card button 
		if(!gameSystem.getShowCard())
		{
			this.removeAll();
			drawCard.setText("<html><center>" + gameSystem.getPlayerName() + ", click to draw a card"+"</center></html>");
			drawCard.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			int turn = gameSystem.getTurn();
			if(turn == 0)
			{
				drawCard.setBackground(Color.RED);
			}
			else if(turn == 1)
			{
				drawCard.setBackground(new Color(153, 214, 255));
			}
			else if(turn == 2)
			{
				drawCard.setBackground(Color.YELLOW);
			}
			else if(turn == 3)
			{
				drawCard.setBackground(Color.GREEN);
			}
			setBorder(new LineBorder(NAVY, BORDER_WEIGHT));
			this.add(drawCard);
		}
		
		//ask which card to display and display it
		else
		{
			this.remove(drawCard);
			
			int cardNum = gameSystem.getCardNum();
			this.remove(drawCard);
			this.setLayout(new GridLayout(1, 1));
			if(cardNum == 0)
			{
				image = new ImageIcon("images/Card_Sorry.jpg");
			}
			else
			{
				image = new ImageIcon("images/Card_" + cardNum +".jpg");
			}
			Image scaledImage = image.getImage().getScaledInstance(160, 240, Image.SCALE_SMOOTH); //FIXME: constants?
			image = new ImageIcon(scaledImage);
			this.setIcon(image);
			
			int turn = gameSystem.getTurn();
			if(turn == 0)
			{
				setBorder(new LineBorder(Color.RED, BORDER_WEIGHT));
			}
			else if(turn == 1)
			{
				setBorder(new LineBorder(Color.BLUE, BORDER_WEIGHT));
			}
			else if(turn == 2)
			{
				setBorder(new LineBorder(Color.YELLOW, BORDER_WEIGHT));
			}
			else if(turn == 3)
			{
				setBorder(new LineBorder(Color.GREEN, BORDER_WEIGHT));
			}
		}
		
	}

}
