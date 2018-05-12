package com.chess.board;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.ButtonModel;
import javax.swing.JButton;

public class CheckPiece
  extends JButton
{
  public String checkPlayer;
  public String position;
  public volatile int draggedAtX;
  public volatile int draggedAtY;
  
  public CheckPiece(String player, String position)
  {
    this.checkPlayer = player;
    this.position = position;
    
    setContentAreaFilled(false);
  }
  
  public void paintComponent(Graphics g)
  {
    if (getModel().isArmed()) {
      g.setColor(getBackground());
    } else {
      g.setColor(getBackground());
    }
    g.fillOval(0, 0, getSize().width - 1, getSize().height - 1);
    super.paintComponent(g);
  }
  
  protected void paintBorder(Graphics g)
  {
    g.setColor(getForeground());
    g.drawOval(0, 0, getSize().width - 1, getSize().height - 1);
  }
}
