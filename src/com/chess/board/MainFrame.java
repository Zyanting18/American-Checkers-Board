package com.chess.board;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import com.chess.menu.MenuItems;

public class MainFrame extends JFrame implements ActionListener {

	JPanel boardBg;// Board background layer
	JLayeredPane lp;// JFrame layering object
	JPanel body;// Pieces layer
	JLabel t;// Message
	JButton switcher;
	JButton confirm;
	GridLayout gridLayout;// Board layout
	String order[] = { "A8", "B8", "C8", "D8", "E8", "F8", "G8", "H8", "A7",
			"B7", "C7", "D7", "E7", "F7", "G7", "H7", "A6", "B6", "C6", "D6",
			"E6", "F6", "G6", "H6", "A5", "B5", "C5", "D5", "E5", "F5", "G5",
			"H5", "A4", "B4", "C4", "D4", "E4", "F4", "G4", "H4", "A3", "B3",
			"C3", "D3", "E3", "F3", "G3", "H3", "A2", "B2", "C2", "D2", "E2",
			"F2", "G2", "H2", "A1", "B1", "C1", "D1", "E1", "F1", "G1", "H1" };// board position names

	CheckPiece blackPieces[] = new CheckPiece[12];// Black pieces collection
	CheckPiece whitePieces[] = new CheckPiece[12];// White pieces collection
	JButton spacePieces[] = new JButton[64];// No pieces collection

	Vector<CheckPiece> piece = new Vector<CheckPiece>();// black pieces on board temporarily
	Vector<JButton> space = new Vector<JButton>();// positions that no pieces on temporarily
	Vector<CheckBoardBlock> board = new Vector<CheckBoardBlock>();// board blocks

	public MainFrame(String arg0) {

		// Generate menu bar and menu items, and set listener for them
		MenuBar menuBar = new MenuBar();
		Menu menu = new Menu("start");
		MenuItem i1 = new MenuItem(MenuItems.items[0]);
		MenuItem i2 = new MenuItem(MenuItems.items[1]);
		MenuItem i3 = new MenuItem(MenuItems.items[2]);
		MenuItem i4 = new MenuItem(MenuItems.items[3]);
		i1.addActionListener(this);
		i2.addActionListener(this);
		i3.addActionListener(this);
		i4.addActionListener(this);
		menu.add(i1);
		menu.add(i2);
		menu.add(i3);
		menu.add(i4);
		menuBar.add(menu);
		this.setMenuBar(menuBar);

		// Initialize pieces collection
		initPieces();

		// Initialize board
		initialize();

		this.setSize(515, 615);
		this.setDefaultCloseOperation(MainFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	/**
	 * Initialize pieces collection
	 */
	public void initPieces() {
		for (int i = 0; i < 64; i++) {
			if (i < 12) {
				blackPieces[i] = new CheckPiece("black", "");
				blackPieces[i].setBackground(Color.BLACK);
				whitePieces[i] = new CheckPiece("white", "");
				whitePieces[i].setBackground(Color.WHITE);
			}
			spacePieces[i] = new JButton();
			spacePieces[i].setEnabled(false);
			spacePieces[i].setVisible(false);
		}
	}

	/**
	 * Initialize the board, all pieces are on initialized position
	 */
	public void initialize() {

		// Add control button
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		this.setLayout(fl);
		switcher = new JButton("Switch Player:black");
		switcher.setEnabled(false);
		switcher.addActionListener(this);
		confirm = new JButton("Confirm");
		confirm.setEnabled(false);
		confirm.addActionListener(this);
		this.add(switcher);
		this.add(confirm);
		switcher.setBounds(520, 200, 50, 20);
		confirm.setBounds(520, 400, 50, 20);

		// Add message upper from the board
		t = new JLabel();
		t.setVerticalAlignment(JLabel.TOP);
		this.add(t);
		t.setVisible(true);

		// Get layer object, which in order to enable layering of JFrame
		lp = this.getLayeredPane();

		// Define board by JPanel with a 8*8 GridLayout
		// Put red and white board
		boardBg = new JPanel();
		boardBg.setLayout(new GridLayout(8, 8));
		for (int i = 0; i < 8 * 8; i++) {
			// Put red board
			if ((i % 2 == 0 && (i / 8) % 2 == 1)
					|| (i % 2 == 1 && (i / 8) % 2 == 0)) {
				CheckBoardBlock block = new CheckBoardBlock("red", order[i]);
				block.addActionListener(this);
				board.add(block);
				block.setEnabled(false);
				boardBg.add(block);
			}
			// Put white board
			else {
				CheckBoardBlock block = new CheckBoardBlock("white", order[i]);
				block.addActionListener(this);
				board.add(block);
				block.setEnabled(false);
				boardBg.add(block);
			}
		}

		// Put pieces on the board, on another same JPanel with 8*8 GridLayout
		body = new JPanel();
		body.setLayout(new GridLayout(8, 8));

		// Put board panel and piece panel in different layer by the Integer
		// parameter
		lp.add(boardBg, new Integer(1));
		lp.add(body, new Integer(2));
		boardBg.setBounds(0, 40, 500, 500);
		boardBg.setVisible(true);
		body.setBounds(0, 40, 500, 500);
		body.setOpaque(false);// Make the upper layer panel transparent so that
								// lower layer can be seen
		body.setVisible(true);

	}

	// Remove all pieces from board and clear template data
	public void clearBoard() {
		body.removeAll();
		piece.clear();
		piece.removeAllElements();
		space.clear();
		space.removeAllElements();
		
		// Remove 'K' label of all pieces
		for (int i=0; i<blackPieces.length; i++) {
			blackPieces[i].setText(null);
		}
		for (int i=0; i<whitePieces.length; i++) {
			whitePieces[i].setText(null);
		}
	}

	/**
	 * Initialize all pieces to default position
	 */
	
	public void initCheckBoardAutomaticly() {
		this.clearBoard();
		for (int i = 0, w = 0, b = 0, s = 0; i < 8 * 8; i++) {

			// Disable board clickable
			CheckBoardBlock block = board.get(i);
			block.setEnabled(false);

			// Put black pieces and record the position
			if (i % 2 != 1 && i >= 8 && i < 16) {
				CheckPiece blackPiece = blackPieces[b];
				blackPiece.position = order[i];
				blackPiece.checkPlayer = "black";
				piece.add(blackPiece);
				body.add(blackPiece);
				b++;
			} else if (i % 2 != 0 && (i < 8 || (i > 16 && i < 24))) {
				CheckPiece blackPiece = blackPieces[b];
				blackPiece.position = order[i];
				blackPiece.checkPlayer = "black";
				piece.add(blackPiece);
				body.add(blackPiece);
				b++;
			}

			// Put white pieces and record the position
			else if (i % 2 != 0 && i > 48 && i < 56) {
				CheckPiece whitePiece = whitePieces[w];
				whitePiece.position = order[i];
				whitePiece.checkPlayer = "white";
				piece.add(whitePiece);
				body.add(whitePiece);
				w++;
			} else if (i % 2 != 1
					&& ((i >= 40 && i < 48) || (i >= 56 && i < 64))) {
				CheckPiece whitePiece = whitePieces[w];
				whitePiece.position = order[i];
				whitePiece.checkPlayer = "white";
				piece.add(whitePiece);
				body.add(whitePiece);
				w++;
			}

			// Put empty pieces on the board
			// Actually, empty pieces will not display on the board, they are
			// not existing
			// to chess players, just for calculation
			else {
				JButton spacePiece = spacePieces[s];
				body.add(spacePiece);
				space.add(spacePiece);
				spacePiece.setVisible(false);
				s++;
			}
		}
		t.setText("Chess Board has been initialized automatically");
	}
	
	/**
	 * Set the board clickable or not
	 * @param flag
	 */
	public void setBoardEnabled(boolean flag) {
		for (int i = 0; i < order.length; i++) {
			CheckBoardBlock block = board.get(i);
			block.setEnabled(flag);
		}
	}

	/**
	 * Remove all pieces from board and user able to set pieces position
	 * manually
	 */
	public void initChessBoardManually() {
		clearBoard();
		this.setBoardEnabled(true);
		switcher.setEnabled(true);
		confirm.setEnabled(true);
		t.setText("<html>Please choose position to put the pieces.</html>");
	}

	/**
	 * Invoked when user put one piece on the board when manually set the board
	 * 
	 * @param b
	 */
	public void putPieceOnBoard(CheckBoardBlock b, String player) {
		int blackCount = getBlackCountInVector();
		int whiteCount = getWhiteCountInVector();
		if (blackCount == 12 && whiteCount == 12) {
			t.setText("<html>black:" + getBlackCountInVector() + "  white:" + getWhiteCountInVector() + "<br>Cannot put any more pieces on</html>");
			return;
		}
		if (blackCount == 12 && player.equals("black")) {
			t.setText("<html>black:" + getBlackCountInVector() + "  white:" + getWhiteCountInVector() + "<br>Black pieces are maximum</html>");
			return;
		}
		if (whiteCount == 12 && player.equals("white")) {
			t.setText("<html>black:" + getBlackCountInVector() + "  white:" + getWhiteCountInVector() + "<br>White pieces are maximum</html>");
			return;
		}
		String position = b.position;
		CheckPiece tmpPiece;
		if (player.equals("black")) {
			tmpPiece = blackPieces[blackCount];

			// Set 'K' label
			if (position.equals("A1")||position.equals("B1")||position.equals("C1")||position.equals("D1")||
				position.equals("E1")||position.equals("F1")||position.equals("G1")||position.equals("H1")) {
				tmpPiece.setText("<html><font color=\"white\">K</font></html>");
			}
		} else {
			tmpPiece = whitePieces[whiteCount];

			// Set 'K' label
			if (position.equals("A8")||position.equals("B8")||position.equals("C8")||position.equals("D8")||
				position.equals("E8")||position.equals("F8")||position.equals("G8")||position.equals("H8")) {
				tmpPiece.setText("<html><font color=\"black\">K</font></html>");
			}
		}
		tmpPiece.position = position;
		piece.add(tmpPiece);
		body.add(tmpPiece);
		reFormatPieceLayer();

		t.setText("<html>black:" + getBlackCountInVector() + "  white:" + getWhiteCountInVector() + "</html>");
	}
	
	/**
	 * Refresh the pieces according to this.piece record
	 */
	public void reFormatPieceLayer() {
		body.removeAll();
		for (int i = 0; i < 8 * 8; i++) {
			boolean pFlag = false;
			CheckPiece tmpPiece = null;

			String position = order[i];
			for (int j = 0; j < piece.size(); j++) {
				if (position.equals(piece.get(j).position)) {
					tmpPiece = piece.get(j);
					pFlag = true;
				}
			}
			if (pFlag == true) {
				body.add(tmpPiece);
			} else {
				body.add(spacePieces[i]);
			}
		}
	}
	
	/**
	 * Get the black piece on the board
	 * @return
	 */
	public int getBlackCountInVector() {
		int count = 0;
		for (int i = 0; i < piece.size(); i++) {
			if (piece.get(i).checkPlayer.equals("black")) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Get the white piece on the board
	 * @return
	 */
	public int getWhiteCountInVector() {
		int count = 0;
		for (int i = 0; i < piece.size(); i++) {
			if (piece.get(i).checkPlayer.equals("white")) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Print piece for testing
	 */
	public void print() {
		for (int i = 0; i < piece.size(); i++) {
			System.out.println(piece.get(i).checkPlayer + ":"
					+ piece.get(i).position);
		}
		System.out.println();
		System.out.println("space:");
		for (int i = 0; i < space.size(); i++) {
			System.out.println(space.get(i));
		}

		System.out.println(body.countComponents());
	}


	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Initial ChessBoard automatically")) {

			this.initCheckBoardAutomaticly();

		} else if (e.getActionCommand().equals("Initial ChessBoard manually")) {

			this.initChessBoardManually();

		} else if (e.getActionCommand().equals("Print out ChessBoard")) {

			this.print();

		} else if (e.getActionCommand().equals("Exit")) {

			System.exit(0);

		} else if (e.getSource() instanceof CheckBoardBlock) {

			CheckBoardBlock b = (CheckBoardBlock) e.getSource();
			if (!b.bgColor.equals("white")) {
				String flag = switcher.getText();
				this.putPieceOnBoard(b, (flag.equals("Switch Player:black")) ? "black":"white");
			}

		} else if (e.getActionCommand().equals("Switch Player:black")) {

			switcher.setText("Switch Player:white");

		} else if (e.getActionCommand().equals("Switch Player:white")) {

			switcher.setText("Switch Player:black");

		} else if (e.getActionCommand().equals("Confirm")) {
			
			this.setBoardEnabled(false);
			switcher.setEnabled(false);
			confirm.setEnabled(false);

		} else {
			System.out.println("else:");
		}
	}

	// Test program
	public static void main(String[] args) {
		new MainFrame("Chess");
	}

}
