package com.chess.board;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JButton;

public class CheckBoardBlock
  extends JButton
{
  public String bgColor;
  public String position;
  
  public CheckBoardBlock(String color, String position)
  {
    this.bgColor = color;
    this.position = position;
  }
  
  public void paint(Graphics g)
  {
    setBackground(this.bgColor.equals("white") ? Color.WHITE : Color.RED);
    super.paint(g);
  }
}
