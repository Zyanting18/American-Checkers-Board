package com.chess.board;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JButton;

public class CheckPiece extends JButton {

	String checkPlayer;// white or black
	String position;

	public CheckPiece(String player, String position) {
		super();
		this.checkPlayer = player;
		this.position = position;
		
		// This call causes the JButton not to paint
		// the background.
		// This allows us to paint a round background.
		setContentAreaFilled(false);
	}

	public void paintComponent(Graphics g) {
		if (getModel().isArmed()) {
			// When mouse pressed, change the color
			g.setColor(checkPlayer.equals("black") ? Color.BLACK:Color.WHITE); //getBackground().brighter() : getBackground().darker());
			g.fillOval(140, 140, getSize().width - 1, getSize().height - 1);
		} else {
			g.setColor(getForeground());
		}
		super.paintComponent(g);
	}

	// Paint the border of the button using a simple stroke.
	protected void paintBorder(Graphics g) {
		g.setColor(getForeground());
		g.drawOval(0, 0, getSize().width - 1, getSize().height - 1);
	}

}