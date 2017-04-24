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
		drawCard.setText("<html><center>" + "Click to draw a card"+"</center></html>");
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
		//ask whether to display a card or the button 
		boolean showCard = gameSystem.getShowCard();
		
		//if it's the button...  
		if(!showCard)
		{
			this.removeAll();
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
		}
		
	}

}
