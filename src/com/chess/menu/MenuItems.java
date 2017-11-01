package com.chess.menu;



/**
 * @author Grant
 * MenuItems is the class offering menus and reply menu selecting.
 */
public class MenuItems {
	
//  initialize menu items.
	public static final int itemsCount = 4;
	public static final String[] items = new String[itemsCount];

	static {
		items[0] = "Initial ChessBoard automatically";
		items[1] = "Initial ChessBoard manually";
		items[2] = "Print out ChessBoard";
		items[3] = "Exit";
	}

}
