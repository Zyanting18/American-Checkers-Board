package com.chess.board;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JButton;

public class CheckBoardBlock extends JButton {
	
	String bgColor;
	String position;
	
	public CheckBoardBlock(String color, String position) {
		super();
		this.bgColor = color;
		this.position = position;
	}


	public void paint(Graphics g) {
		// Set color of the board
		g.setColor(bgColor.equals("red")?Color.RED:Color.WHITE);
		g.fillRect(0, 0, getSize().width - 1, getSize().height - 1);
		super.paint(g);
	}

}
