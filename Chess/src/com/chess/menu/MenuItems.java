package com.chess.menu;

public class MenuItems
{
  public static final int itemsCount = 9;
  public static final String[] items = new String[9];
  
  static
  {
    items[0] = "Initial ChessBoard automatically";
    items[1] = "Initial ChessBoard manually";
    items[2] = "Print out ChessBoard";
    items[3] = "Exit";
    
    items[4] = "Save";
    items[5] = "Load";
    
    items[6] = "Play with Human";
    items[7] = "Play with black AI";
    items[8] = "Play with white AI";
  }
}
