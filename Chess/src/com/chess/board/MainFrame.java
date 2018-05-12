package com.chess.board;

import java.awt.event.ItemEvent;
import javax.swing.border.LineBorder;
import java.awt.event.MouseEvent;
import java.util.Vector;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.Color;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JMenuItem;
import com.chess.menu.MenuItems;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import java.util.HashMap;
import com.chess.ai.AIRobot;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import java.awt.event.ItemListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

public class MainFrame extends JFrame implements ActionListener, MouseMotionListener, MouseListener, ItemListener
{
    private JPanel boardBg;
    private JLayeredPane lp;
    private JPanel body;
    JLabel t;
    JButton switcher;
    JButton confirm;
    private GridLayout gridLayout;
    public GameStatus status;
    public String activePlayer;
    public String aiFlag;
    public AIRobot robot;
    public String[] order;
    private CheckPiece[] blackPieces;
    private CheckPiece[] whitePieces;
    private CheckPiece[] spacePieces;
    public HashMap<String, CheckPiece> piece;
    public HashMap<String, CheckBoardBlock> board;
    
    public MainFrame(final String arg0) {
        this.status = new GameStatus();
        this.activePlayer = new String();
        this.aiFlag = "human";
        this.robot = new AIRobot();
        this.order = new String[] { "A8", "B8", "C8", "D8", "E8", "F8", "G8", "H8", "A7", "B7", "C7", "D7", "E7", "F7", "G7", "H7", "A6", "B6", "C6", "D6", "E6", "F6", "G6", "H6", "A5", "B5", "C5", "D5", "E5", "F5", "G5", "H5", "A4", "B4", "C4", "D4", "E4", "F4", "G4", "H4", "A3", "B3", "C3", "D3", "E3", "F3", "G3", "H3", "A2", "B2", "C2", "D2", "E2", "F2", "G2", "H2", "A1", "B1", "C1", "D1", "E1", "F1", "G1", "H1" };
        this.blackPieces = new CheckPiece[12];
        this.whitePieces = new CheckPiece[12];
        this.spacePieces = new CheckPiece[64];
        this.piece = new HashMap<String, CheckPiece>();
        this.board = new HashMap<String, CheckBoardBlock>();
        final JMenuBar menuBar = new JMenuBar();
        final JMenu file = new JMenu("File");
        final JMenuItem f1 = new JMenuItem(MenuItems.items[4]);
        final JMenuItem f2 = new JMenuItem(MenuItems.items[5]);
        f1.addActionListener(this);
        f2.addActionListener(this);
        file.add(f1);
        file.add(f2);
        menuBar.add(file);
        final JMenu start = new JMenu("start");
        final JMenuItem i1 = new JMenuItem(MenuItems.items[0]);
        final JMenuItem i2 = new JMenuItem(MenuItems.items[1]);
        final JMenuItem i3 = new JMenuItem(MenuItems.items[2]);
        final JMenuItem i4 = new JMenuItem(MenuItems.items[3]);
        i1.addActionListener(this);
        i2.addActionListener(this);
        i3.addActionListener(this);
        i4.addActionListener(this);
        start.add(i1);
        start.add(i2);
        start.add(i3);
        start.add(i4);
        menuBar.add(start);
        final JMenu AI = new JMenu("AI");
        final JRadioButtonMenuItem playWithHuman = new JRadioButtonMenuItem(MenuItems.items[6]);
        playWithHuman.setSelected(true);
        final JRadioButtonMenuItem playWithBlack = new JRadioButtonMenuItem(MenuItems.items[7]);
        final JRadioButtonMenuItem playWithWhite = new JRadioButtonMenuItem(MenuItems.items[8]);
        final ButtonGroup aiItems = new ButtonGroup();
        aiItems.add(playWithHuman);
        aiItems.add(playWithBlack);
        aiItems.add(playWithWhite);
        playWithHuman.addItemListener(this);
        playWithBlack.addItemListener(this);
        playWithWhite.addItemListener(this);
        AI.add(playWithHuman);
        AI.add(playWithBlack);
        AI.add(playWithWhite);
        menuBar.add(AI);
        this.setJMenuBar(menuBar);
        this.initPieces();
        this.initialize();
        this.setSize(515, 615);
        this.setDefaultCloseOperation(3);
        this.setVisible(true);
    }
    
    public void initPieces() {
        for (int i = 0; i < 64; ++i) {
            if (i < 12) {
                (this.blackPieces[i] = new CheckPiece("black", "")).setBackground(Color.BLACK);
                this.blackPieces[i].addMouseMotionListener(this);
                this.blackPieces[i].addMouseListener(this);
                this.blackPieces[i].setText(null);
                (this.whitePieces[i] = new CheckPiece("white", "")).setBackground(Color.WHITE);
                this.whitePieces[i].addMouseMotionListener(this);
                this.whitePieces[i].addMouseListener(this);
                this.whitePieces[i].setText(null);
            }
            (this.spacePieces[i] = new CheckPiece("empty", "")).setEnabled(false);
            this.spacePieces[i].setVisible(false);
            this.spacePieces[i].setText(null);
        }
    }
    
    public void initialize() {
        final FlowLayout fl = new FlowLayout();
        fl.setAlignment(0);
        this.setLayout(fl);
        (this.switcher = new JButton("Switch Player:black")).setEnabled(false);
        this.switcher.addActionListener(this);
        (this.confirm = new JButton("Confirm")).setEnabled(false);
        this.confirm.addActionListener(this);
        this.add(this.switcher);
        this.add(this.confirm);
        this.switcher.setBounds(520, 200, 50, 20);
        this.confirm.setBounds(520, 400, 50, 20);
        (this.t = new JLabel()).setVerticalAlignment(1);
        this.add(this.t);
        this.t.setVisible(true);
        this.lp = this.getLayeredPane();
        (this.boardBg = new JPanel()).setLayout(new GridLayout(8, 8));
        for (int i = 0; i < 64; ++i) {
            if ((i % 2 == 0 && i / 8 % 2 == 1) || (i % 2 == 1 && i / 8 % 2 == 0)) {
                final CheckBoardBlock block = new CheckBoardBlock("red", this.order[i]);
                block.addActionListener(this);
                block.addMouseListener(this);
                this.board.put(this.order[i], block);
                block.setEnabled(false);
                block.setBorderPainted(false);
                this.boardBg.add(block);
            }
            else {
                final CheckBoardBlock block = new CheckBoardBlock("white", this.order[i]);
                block.addActionListener(this);
                block.addMouseListener(this);
                this.board.put(this.order[i], block);
                block.setEnabled(false);
                block.setBorderPainted(false);
                this.boardBg.add(block);
            }
        }
        (this.body = new JPanel()).setLayout(new GridLayout(8, 8));
        this.lp.add(this.boardBg, new Integer(1));
        this.lp.add(this.body, new Integer(2));
        this.boardBg.setBounds(0, 70, 500, 500);
        this.boardBg.setVisible(true);
        this.body.setBounds(0, 70, 500, 500);
        this.body.setOpaque(false);
        this.body.setVisible(true);
        this.status.setStatusInit();
    }
    
    public void clearBoard() {
        this.body.removeAll();
        this.piece.clear();
        for (int i = 0; i < this.blackPieces.length; ++i) {
            this.blackPieces[i].setText(null);
        }
        for (int i = 0; i < this.whitePieces.length; ++i) {
            this.whitePieces[i].setText(null);
        }
        this.body.repaint();
    }
    
    public void initChessBoardAutomaticly() {
        this.initPieces();
        this.clearBoard();
        for (int i = 0; i < this.board.size(); ++i) {
            final CheckBoardBlock b = this.board.get(this.order[i]);
            b.setBorderPainted(false);
        }
        int i = 0;
        int w = 0;
        int b2 = 0;
        int s = 0;
        while (i < 64) {
            final CheckBoardBlock block = this.board.get(this.order[i]);
            block.setEnabled(false);
            if (i % 2 != 1 && i >= 8 && i < 16) {
                final CheckPiece blackPiece = this.blackPieces[b2];
                blackPiece.position = this.order[i];
                this.piece.put(this.order[i], blackPiece);
                this.body.add(blackPiece);
                ++b2;
            }
            else if (i % 2 != 0 && (i < 8 || (i > 16 && i < 24))) {
                final CheckPiece blackPiece = this.blackPieces[b2];
                blackPiece.position = this.order[i];
                this.piece.put(this.order[i], blackPiece);
                this.body.add(blackPiece);
                ++b2;
            }
            else if (i % 2 != 0 && i > 48 && i < 56) {
                final CheckPiece whitePiece = this.whitePieces[w];
                whitePiece.position = this.order[i];
                this.piece.put(this.order[i], whitePiece);
                this.body.add(whitePiece);
                ++w;
            }
            else if (i % 2 != 1 && ((i >= 40 && i < 48) || (i >= 56 && i < 64))) {
                final CheckPiece whitePiece = this.whitePieces[w];
                whitePiece.position = this.order[i];
                this.piece.put(this.order[i], whitePiece);
                this.body.add(whitePiece);
                ++w;
            }
            else {
                final CheckPiece spacePiece = this.spacePieces[s];
                spacePiece.position = this.order[i];
                this.body.add(spacePiece);
                this.piece.put(this.order[i], spacePiece);
                spacePiece.setVisible(false);
                ++s;
            }
            ++i;
        }
        this.t.setText("Chess Board has been initialized automatically");
        this.startGame();
    }
    
    public void setBoardEnabled(final boolean flag) {
        for (int i = 0; i < this.order.length; ++i) {
            final CheckBoardBlock block = this.board.get(this.order[i]);
            block.setEnabled(flag);
        }
    }
    
    public void initChessBoardManually() {
        this.status.setStatusEdit();
        this.clearBoard();
        this.setBoardEnabled(true);
        for (int i = 0; i < 64; ++i) {
            final CheckBoardBlock block = this.board.get(this.order[i]);
            block.setBorderPainted(false);
        }
        this.switcher.setEnabled(true);
        this.confirm.setEnabled(true);
        this.t.setText("<html>Please choose position to put the pieces.<br>Right click to set/cancel King flag</html>");
    }
    
    public void putPieceOnBoard(final CheckBoardBlock b, final String player) {
        final int blackCount = this.getBlackCountInVector();
        final int whiteCount = this.getWhiteCountInVector();
        if (blackCount == 12 && whiteCount == 12) {
            this.t.setText("<html>black:" + this.getBlackCountInVector() + "  white:" + this.getWhiteCountInVector() + "<br>Cannot put any more pieces on</html>");
            return;
        }
        if (blackCount == 12 && player.equals("black")) {
            this.t.setText("<html>black:" + this.getBlackCountInVector() + "  white:" + this.getWhiteCountInVector() + "<br>Black pieces are maximum</html>");
            return;
        }
        if (whiteCount == 12 && player.equals("white")) {
            this.t.setText("<html>black:" + this.getBlackCountInVector() + "  white:" + this.getWhiteCountInVector() + "<br>White pieces are maximum</html>");
            return;
        }
        final String position = b.position;
        CheckPiece tmpPiece;
        if (player.equals("black")) {
            tmpPiece = this.blackPieces[blackCount];
            System.out.println(player);
            if (position.equals("A1") || position.equals("B1") || position.equals("C1") || position.equals("D1") || position.equals("E1") || position.equals("F1") || position.equals("G1") || position.equals("H1")) {
                tmpPiece.setText("<html><font color=\"white\">K</font></html>");
            }
        }
        else {
            tmpPiece = this.whitePieces[whiteCount];
            if (position.equals("A8") || position.equals("B8") || position.equals("C8") || position.equals("D8") || position.equals("E8") || position.equals("F8") || position.equals("G8") || position.equals("H8")) {
                tmpPiece.setText("<html><font color=\"black\">K</font></html>");
            }
        }
        tmpPiece.position = position;
        this.piece.put(position, tmpPiece);
        this.body.add(tmpPiece);
        this.reFormatPieceLayer();
        this.t.setText("<html>black:" + this.getBlackCountInVector() + "  white:" + this.getWhiteCountInVector() + "</html>");
    }
    
    public void reFormatPieceLayer() {
        this.body.removeAll();
        for (int i = 0; i < 64; ++i) {
            final CheckPiece tmpPiece = this.piece.get(this.order[i]);
            if (tmpPiece == null) {
                this.spacePieces[i].position = this.order[i];
                this.piece.put(this.order[i], this.spacePieces[i]);
                this.body.add(this.spacePieces[i]);
            }
            else {
                this.piece.put(this.order[i], tmpPiece);
                this.body.add(tmpPiece);
            }
        }
        this.body.repaint();
    }
    
    public int getBlackCountInVector() {
        int count = 0;
        final Object[] tmp = this.piece.values().toArray();
        for (int i = 0; i < tmp.length; ++i) {
            final CheckPiece p = (CheckPiece)tmp[i];
            if (p.checkPlayer.equals("black")) {
                ++count;
            }
        }
        return count;
    }
    
    public int getWhiteCountInVector() {
        int count = 0;
        final Object[] tmp = this.piece.values().toArray();
        for (int i = 0; i < tmp.length; ++i) {
            final CheckPiece p = (CheckPiece)tmp[i];
            if (p.checkPlayer.equals("white")) {
                ++count;
            }
        }
        return count;
    }
    
    public void print() {
        final Object[] tmp = this.piece.values().toArray();
        for (int i = 0; i < this.piece.size(); ++i) {
            final CheckPiece p = (CheckPiece)tmp[i];
            System.out.println(String.valueOf(i) + " " + p.checkPlayer + ":" + p.position + " " + p.isEnabled());
        }
        System.out.println();
        System.out.println(this.body.countComponents());
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getActionCommand().equals("Initial ChessBoard automatically")) {
            this.initPieces();
            this.initChessBoardAutomaticly();
        }
        else if (e.getActionCommand().equals("Initial ChessBoard manually")) {
            this.initPieces();
            this.initChessBoardManually();
        }
        else if (e.getActionCommand().equals("Print out ChessBoard")) {
            this.print();
        }
        else if (e.getActionCommand().equals("Exit")) {
            System.exit(0);
        }
        else if (e.getSource() instanceof CheckBoardBlock) {
            final CheckBoardBlock b = (CheckBoardBlock)e.getSource();
            if (!b.bgColor.equals("white")) {
                final String flag = this.switcher.getText();
                this.putPieceOnBoard(b, flag.equals("Switch Player:black") ? "black" : "white");
            }
        }
        else if (e.getActionCommand().equals("Switch Player:black")) {
            this.switcher.setText("Switch Player:white");
        }
        else if (e.getActionCommand().equals("Switch Player:white")) {
            this.switcher.setText("Switch Player:black");
        }
        else if (e.getActionCommand().equals("Confirm")) {
            this.setBoardEnabled(false);
            this.switcher.setEnabled(false);
            this.confirm.setEnabled(false);
            final String winner = this.win();
            if (winner != null && this.status.getStatus().equals("EXEC")) {
                this.t.setText("Winner is:" + winner);
                return;
            }
            this.startGame();
        }
        else if (e.getActionCommand().equals("Save")) {
            FileOutputStream out = null;
            String buffer = String.valueOf(this.t.getText().replaceAll("null", "")) + "\r\n";
            try {
                final File save = new File("save.txt");
                if (!save.exists()) {
                    save.createNewFile();
                }
                out = new FileOutputStream(save);
                for (int i = 0; i < this.piece.size(); ++i) {
                    final CheckPiece p = this.piece.get(this.order[i]);
                    buffer = String.valueOf(buffer) + p.checkPlayer + ":" + this.order[i] + ":" + p.getText() + "\r\n";
                }
                buffer = String.valueOf(buffer) + this.status.getStatus() + "\r\n";
                buffer = String.valueOf(buffer) + this.activePlayer;
                out.write(buffer.getBytes());
            }
            catch (Exception e2) {
                e2.printStackTrace();
                try {
                    out.close();
                }
                catch (IOException e3) {
                    e3.printStackTrace();
                }
                return;
            }
            finally {
                try {
                    out.close();
                }
                catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
            try {
                out.close();
            }
            catch (IOException e3) {
                e3.printStackTrace();
            }
        }
        else if (e.getActionCommand().equals("Load")) {
            final File f = new File("save.txt");
            BufferedReader input = null;
            try {
                input = new BufferedReader(new FileReader(f));
                String s = "";
                String s2 = "";
                while ((s = input.readLine()) != null) {
                    s2 = String.valueOf(s2) + s + "\r\n";
                }
                int blackCount = 0;
                int whiteCount = 0;
                int spaceCount = 0;
                final String[] ps = s2.split("\r\n");
                this.t.setText(ps[0]);
                for (int j = 1; j < ps.length - 2; ++j) {
                    System.out.println(ps[j]);
                    final String[] node = ps[j].split(":");
                    CheckPiece p2;
                    if (node[0].equals("black")) {
                        p2 = this.blackPieces[blackCount];
                        p2.setText(node[2].equals("null") ? null : "<html><font color=\"white\">K</font></html>");
                        ++blackCount;
                    }
                    else if (node[0].equals("white")) {
                        p2 = this.whitePieces[whiteCount];
                        p2.setText(node[2].equals("null") ? null : "<html><font color=\"black\">K</font></html>");
                        ++whiteCount;
                    }
                    else {
                        p2 = this.spacePieces[spaceCount];
                        ++spaceCount;
                    }
                    p2.position = node[1];
                    this.piece.put(node[1], p2);
                }
                this.reFormatPieceLayer();
                final String st = ps[ps.length - 2];
                if (st.equals("EXEC")) {
                    this.status.setStatusExec();
                    this.switcher.setEnabled(false);
                    this.confirm.setEnabled(false);
                    if (ps[0].startsWith("white")) {
                        this.disablePlayer("black");
                    }
                    else {
                        this.disablePlayer("white");
                    }
                }
                else if (st.equals("EDIT")) {
                    this.status.setStatusEdit();
                    this.switcher.setEnabled(true);
                    this.confirm.setEnabled(true);
                }
                this.activePlayer = ps[ps.length - 1];
            }
            catch (Exception e2) {
                e2.printStackTrace();
                try {
                    input.close();
                }
                catch (IOException e4) {
                    e4.printStackTrace();
                }
                return;
            }
            finally {
                try {
                    input.close();
                }
                catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
            try {
                input.close();
            }
            catch (IOException e4) {
                e4.printStackTrace();
            }
        }
        else {
            System.out.println("else:");
        }
    }
    
    public Vector<CheckBoardBlock> getReputablePosition() {
        final Vector<CheckBoardBlock> tmp = new Vector<CheckBoardBlock>();
        for (int i = 0; i < 64; ++i) {
            if (this.piece.get(this.order[i]).checkPlayer.equals("empty") && this.board.get(this.order[i]).bgColor.equals("red")) {
                this.board.get(this.order[i]).setBorder(null);
                tmp.add(this.board.get(this.order[i]));
            }
            else {
                this.board.get(this.order[i]).setEnabled(false);
            }
        }
        return tmp;
    }
    
    public Vector<CheckBoardBlock> getMovablePosition(final CheckPiece p) {
        final String player = p.checkPlayer;
        final String position = p.position;
        final char h = position.charAt(0);
        final int v = Integer.parseInt(position.substring(1));
        final Vector<CheckBoardBlock> tmp = new Vector<CheckBoardBlock>();
        tmp.add(this.board.get(position));
        if (player.equals("white")) {
            if (p.getText() == null) {
                final CheckBoardBlock br1 = this.board.get(String.valueOf(Character.toString((char)(h + '\u0001'))) + (v + 1));
                final CheckBoardBlock br2 = this.board.get(String.valueOf(Character.toString((char)(h + '\u0002'))) + (v + 2));
                if (br1 != null && this.piece.get(br1.position).checkPlayer.equals("empty")) {
                    tmp.add(this.board.get(br1.position));
                }
                else if (br2 != null && this.piece.get(br1.position).checkPlayer.equals("black") && this.piece.get(br2.position).checkPlayer.equals("empty")) {
                    tmp.add(this.board.get(br2.position));
                }
                final CheckBoardBlock bl1 = this.board.get(String.valueOf(Character.toString((char)(h - '\u0001'))) + (v + 1));
                final CheckBoardBlock bl2 = this.board.get(String.valueOf(Character.toString((char)(h - '\u0002'))) + (v + 2));
                if (bl1 != null && this.piece.get(bl1.position).checkPlayer.equals("empty")) {
                    tmp.add(this.board.get(bl1.position));
                }
                else if (bl2 != null && this.piece.get(bl1.position).checkPlayer.equals("black") && this.piece.get(bl2.position).checkPlayer.equals("empty")) {
                    tmp.add(this.board.get(bl2.position));
                }
            }
            else {
                final CheckBoardBlock bur1 = this.board.get(String.valueOf(Character.toString((char)(h + '\u0001'))) + (v + 1));
                final CheckBoardBlock bur2 = this.board.get(String.valueOf(Character.toString((char)(h + '\u0002'))) + (v + 2));
                if (bur1 != null && this.piece.get(bur1.position).checkPlayer.equals("empty")) {
                    tmp.add(this.board.get(bur1.position));
                }
                else if (bur2 != null && this.piece.get(bur1.position).checkPlayer.equals("black") && this.piece.get(bur2.position).checkPlayer.equals("empty")) {
                    tmp.add(this.board.get(bur2.position));
                }
                final CheckBoardBlock bul1 = this.board.get(String.valueOf(Character.toString((char)(h - '\u0001'))) + (v + 1));
                final CheckBoardBlock bul2 = this.board.get(String.valueOf(Character.toString((char)(h - '\u0002'))) + (v + 2));
                if (bul1 != null && this.piece.get(bul1.position).checkPlayer.equals("empty")) {
                    tmp.add(this.board.get(bul1.position));
                }
                else if (bul2 != null && this.piece.get(bul1.position).checkPlayer.equals("black") && this.piece.get(bul2.position).checkPlayer.equals("empty")) {
                    tmp.add(this.board.get(bul2.position));
                }
                final CheckBoardBlock bdr1 = this.board.get(String.valueOf(Character.toString((char)(h + '\u0001'))) + (v - 1));
                final CheckBoardBlock bdr2 = this.board.get(String.valueOf(Character.toString((char)(h + '\u0002'))) + (v - 2));
                if (bdr1 != null && this.piece.get(bdr1.position).checkPlayer.equals("empty")) {
                    tmp.add(this.board.get(bdr1.position));
                }
                else if (bdr2 != null && this.piece.get(bdr1.position).checkPlayer.equals("black") && this.piece.get(bdr2.position).checkPlayer.equals("empty")) {
                    tmp.add(this.board.get(bdr2.position));
                }
                final CheckBoardBlock bdl1 = this.board.get(String.valueOf(Character.toString((char)(h - '\u0001'))) + (v - 1));
                final CheckBoardBlock bdl2 = this.board.get(String.valueOf(Character.toString((char)(h - '\u0002'))) + (v - 2));
                if (bdl1 != null && this.piece.get(bdl1.position).checkPlayer.equals("empty")) {
                    tmp.add(this.board.get(bdl1.position));
                }
                else if (bdl2 != null && this.piece.get(bdl1.position).checkPlayer.equals("black") && this.piece.get(bdl2.position).checkPlayer.equals("empty")) {
                    tmp.add(this.board.get(bdl2.position));
                }
            }
        }
        else if (p.getText() == null) {
            final CheckBoardBlock br1 = this.board.get(String.valueOf(Character.toString((char)(h + '\u0001'))) + (v - 1));
            final CheckBoardBlock br2 = this.board.get(String.valueOf(Character.toString((char)(h + '\u0002'))) + (v - 2));
            if (br1 != null && this.piece.get(br1.position).checkPlayer.equals("empty")) {
                tmp.add(this.board.get(br1.position));
            }
            else if (br2 != null && this.piece.get(br1.position).checkPlayer.equals("white") && this.piece.get(br2.position).checkPlayer.equals("empty")) {
                tmp.add(this.board.get(br2.position));
            }
            final CheckBoardBlock bl1 = this.board.get(String.valueOf(Character.toString((char)(h - '\u0001'))) + (v - 1));
            final CheckBoardBlock bl2 = this.board.get(String.valueOf(Character.toString((char)(h - '\u0002'))) + (v - 2));
            if (bl1 != null && this.piece.get(bl1.position).checkPlayer.equals("empty")) {
                tmp.add(this.board.get(bl1.position));
            }
            else if (bl2 != null && this.piece.get(bl1.position).checkPlayer.equals("white") && this.piece.get(bl2.position).checkPlayer.equals("empty")) {
                tmp.add(this.board.get(bl2.position));
            }
        }
        else {
            final CheckBoardBlock bur1 = this.board.get(String.valueOf(Character.toString((char)(h + '\u0001'))) + (v + 1));
            final CheckBoardBlock bur2 = this.board.get(String.valueOf(Character.toString((char)(h + '\u0002'))) + (v + 2));
            if (bur1 != null && this.piece.get(bur1.position).checkPlayer.equals("empty")) {
                tmp.add(this.board.get(bur1.position));
            }
            else if (bur2 != null && this.piece.get(bur1.position).checkPlayer.equals("white") && this.piece.get(bur2.position).checkPlayer.equals("empty")) {
                tmp.add(this.board.get(bur2.position));
            }
            final CheckBoardBlock bul1 = this.board.get(String.valueOf(Character.toString((char)(h - '\u0001'))) + (v + 1));
            final CheckBoardBlock bul2 = this.board.get(String.valueOf(Character.toString((char)(h - '\u0002'))) + (v + 2));
            if (bul1 != null && this.piece.get(bul1.position).checkPlayer.equals("empty")) {
                tmp.add(this.board.get(bul1.position));
            }
            else if (bul2 != null && this.piece.get(bul1.position).checkPlayer.equals("white") && this.piece.get(bul2.position).checkPlayer.equals("empty")) {
                tmp.add(this.board.get(bul2.position));
            }
            final CheckBoardBlock bdr1 = this.board.get(String.valueOf(Character.toString((char)(h + '\u0001'))) + (v - 1));
            final CheckBoardBlock bdr2 = this.board.get(String.valueOf(Character.toString((char)(h + '\u0002'))) + (v - 2));
            if (bdr1 != null && this.piece.get(bdr1.position).checkPlayer.equals("empty")) {
                tmp.add(this.board.get(bdr1.position));
            }
            else if (bdr2 != null && this.piece.get(bdr1.position).checkPlayer.equals("white") && this.piece.get(bdr2.position).checkPlayer.equals("empty")) {
                tmp.add(this.board.get(bdr2.position));
            }
            final CheckBoardBlock bdl1 = this.board.get(String.valueOf(Character.toString((char)(h - '\u0001'))) + (v - 1));
            final CheckBoardBlock bdl2 = this.board.get(String.valueOf(Character.toString((char)(h - '\u0002'))) + (v - 2));
            if (bdl1 != null && this.piece.get(bdl1.position).checkPlayer.equals("empty")) {
                tmp.add(this.board.get(bdl1.position));
            }
            else if (bdl2 != null && this.piece.get(bdl1.position).checkPlayer.equals("white") && this.piece.get(bdl2.position).checkPlayer.equals("empty")) {
                tmp.add(this.board.get(bdl2.position));
            }
        }
        return tmp;
    }
    
    public static void main(final String[] args) {
        new MainFrame("Chess");
    }
    
    @Override
    public void mouseDragged(final MouseEvent e) {
        final CheckPiece p = (CheckPiece)e.getSource();
        final int newX = e.getX() - p.draggedAtX + p.getLocation().x;
        final int newY = e.getY() - p.draggedAtY + p.getLocation().y;
        p.setLocation(newX, newY);
    }
    
    @Override
    public void mouseMoved(final MouseEvent e) {
        final boolean b = e.getSource() instanceof CheckBoardBlock;
    }
    
    @Override
    public void mouseClicked(final MouseEvent arg0) {
        if (arg0.getSource() instanceof CheckPiece && this.status.getStatus().equals("EDIT") && arg0.getButton() == 3) {
            final CheckPiece p = (CheckPiece)arg0.getSource();
            if (p.checkPlayer.equals("black") && p.position.charAt(1) != '1') {
                if (p.getText() == null) {
                    p.setText("<html><font color=\"white\">K</font></html>");
                }
                else {
                    p.setText(null);
                }
            }
            else if (p.checkPlayer.equals("white") && p.position.charAt(1) != '8') {
                if (p.getText() == null) {
                    p.setText("<html><font color=\"black\">K</font></html>");
                }
                else {
                    p.setText(null);
                }
            }
        }
    }
    
    @Override
    public void mouseEntered(final MouseEvent arg0) {
        final boolean b = arg0.getSource() instanceof CheckBoardBlock;
    }
    
    @Override
    public void mouseExited(final MouseEvent arg0) {
        final boolean b = arg0.getSource() instanceof CheckBoardBlock;
    }
    
    @Override
    public void mousePressed(final MouseEvent arg0) {
        if (this.status.getStatus().equals("EXEC") && arg0.getSource() instanceof CheckPiece) {
            final CheckPiece p = (CheckPiece)arg0.getSource();
            final Vector<CheckBoardBlock> blocks = this.getMovablePosition(p);
            for (int i = 0; i < blocks.size(); ++i) {
                final CheckBoardBlock b = blocks.elementAt(i);
                b.setEnabled(true);
                b.setBorderPainted(true);
                if (p.position.equals(b.position)) {
                    b.setBorder(new LineBorder(Color.BLUE, 4));
                }
                else {
                    b.setBorder(new LineBorder(Color.GREEN, 4));
                }
            }
            p.draggedAtX = arg0.getX();
            p.draggedAtY = arg0.getY();
            this.body.repaint();
        }
        if (this.status.getStatus().equals("EDIT") && arg0.getSource() instanceof CheckPiece) {
            final CheckPiece p = (CheckPiece)arg0.getSource();
            final Vector<CheckBoardBlock> blocks = this.getReputablePosition();
            for (int i = 0; i < blocks.size(); ++i) {
                final CheckBoardBlock b = blocks.elementAt(i);
                b.setEnabled(true);
                b.setBorderPainted(true);
                b.setBorder(new LineBorder(Color.GREEN, 0));
            }
            p.draggedAtX = arg0.getX();
            p.draggedAtY = arg0.getY();
            this.body.repaint();
        }
    }
    
    @Override
    public void mouseReleased(final MouseEvent arg0) {
        if (arg0.getSource() instanceof CheckPiece) {
            final CheckPiece p = (CheckPiece)arg0.getSource();
            final CheckBoardBlock block = this.findMovedBlock(p.getLocation().x + arg0.getX(), p.getLocation().y + arg0.getY());
            if (block == null) {
                this.changePosition(p, this.board.get(p.position).position);
            }
            else if (block.isBorderPainted() && ((LineBorder)block.getBorder()).getLineColor().equals(Color.GREEN)) {
                this.changePosition(p, block.position);
            }
            else {
                this.changePosition(p, this.board.get(p.position).position);
            }
            for (int i = 0; i < this.board.size(); ++i) {
                final CheckBoardBlock b = this.board.get(this.order[i]);
                b.setBorder(null);
                b.setBorderPainted(false);
            }
            AIRobot.robotMove(this);
            if (this.isPeace()) {
                this.t.setText("This game is peace, restart a new game");
            }
            final String winner = this.win();
            if (winner != null) {
                this.t.setText("Winner is:" + winner);
            }
        }
    }
    
    public CheckBoardBlock findMovedBlock(final int x, final int y) {
        for (int i = 0; i < this.board.size(); ++i) {
            final CheckBoardBlock b = this.board.get(this.order[i]);
            if (x >= b.getX() && x < b.getX() + b.getWidth() && y >= b.getY() && y < b.getY() + b.getHeight()) {
                return b;
            }
        }
        return null;
    }
    
    public void changePosition(final CheckPiece p1, final String newP) {
        if (!p1.position.equals(newP)) {
            if (this.status.getStatus().equals("EXEC")) {
                this.clearCapturedPiece(p1, newP);
                this.exchangeActivePlayer();
            }
            final CheckPiece p2 = this.piece.get(newP);
            p2.position = p1.position;
            this.piece.put(p1.position, p2);
            p1.position = newP;
            this.piece.put(newP, p1);
            if (newP.charAt(1) == '8' && p1.checkPlayer.equals("white")) {
                this.piece.get(newP).setText("<html><font color=\"black\">K</font></html>");
            }
            if (newP.charAt(1) == '1' && p1.checkPlayer.equals("black")) {
                this.piece.get(newP).setText("<html><font color=\"white\">K</font></html>");
            }
        }
        if (this.status.getStatus().equals("EXEC")) {
            this.setBoardEnabled(false);
        }
        this.reFormatPieceLayer();
    }
    
    public void clearCapturedPiece(final CheckPiece p1, final String newP) {
        final char lh = p1.position.charAt(0);
        final int lv = Integer.parseInt(p1.position.substring(1));
        final char rh = newP.charAt(0);
        final int rv = Integer.parseInt(newP.substring(1));
        String midPosition;
        if (Math.abs(lv - rv) == 1) {
            midPosition = "no";
        }
        else {
            midPosition = String.valueOf(Character.toString((char)((lh + rh) / '\u0002'))) + (lv + rv) / 2;
            this.piece.get(midPosition).checkPlayer = "empty";
            this.piece.get(midPosition).setText(null);
            this.piece.get(midPosition).setVisible(false);
            this.reFormatPieceLayer();
        }
        System.out.println(String.valueOf(p1.position) + " " + newP + " insert:" + midPosition);
    }
    
    public void disablePlayer(final String player) {
        final Object[] o = this.piece.values().toArray();
        for (int i = 0; i < o.length; ++i) {
            final CheckPiece p = (CheckPiece)o[i];
            if (p.checkPlayer.equals(player)) {
                p.removeMouseListener(this);
                p.removeMouseMotionListener(this);
            }
        }
    }
    
    public void enablePlayer(final String player) {
        final Object[] o = this.piece.values().toArray();
        for (int i = 0; i < o.length; ++i) {
            final CheckPiece p = (CheckPiece)o[i];
            if (p.checkPlayer.equals(player)) {
                p.addMouseListener(this);
                p.addMouseMotionListener(this);
            }
        }
        this.activePlayer = player;
    }
    
    public void exchangeActivePlayer() {
        if (this.activePlayer.equals("black")) {
            this.disablePlayer("black");
            this.enablePlayer("white");
            this.t.setText("white turn");
        }
        else {
            this.disablePlayer("white");
            this.enablePlayer("black");
            this.t.setText("black turn");
        }
    }
    
    public boolean isPeace() {
        for (int i = 0; i < this.piece.size(); ++i) {
            final CheckPiece p = this.piece.get(this.order[i]);
            if (p.checkPlayer.equals("black") || p.checkPlayer.equals("white")) {
                final Vector<CheckBoardBlock> blocks = this.getMovablePosition(p);
                if (blocks.size() > 1) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public String win() {
        int blackCount = 0;
        int whiteCount = 0;
        for (int i = 0; i < this.piece.size(); ++i) {
            if (this.piece.get(this.order[i]).checkPlayer.equals("black")) {
                ++blackCount;
            }
            else if (this.piece.get(this.order[i]).checkPlayer.equals("white")) {
                ++whiteCount;
            }
        }
        if (blackCount == 0) {
            return "white";
        }
        if (whiteCount == 0) {
            return "black";
        }
        return null;
    }
    
    public void startGame() {
        this.status.setStatusExec();
        if (this.t.getText().startsWith("Winner")) {
            this.disablePlayer("black");
            this.disablePlayer("white");
            return;
        }
        this.disablePlayer("black");
        this.activePlayer = "white";
        if (this.activePlayer.equals("white") && this.aiFlag.equals("white")) {
            AIRobot.robotMove(this);
        }
        this.t.setText("white turn");
        this.setBoardEnabled(false);
    }
    
    @Override
    public void itemStateChanged(final ItemEvent ie) {
        final JRadioButtonMenuItem item = (JRadioButtonMenuItem)ie.getSource();
        if (item.getText().equals(MenuItems.items[6]) && item.isSelected()) {
            for (int i = 0; i < this.board.size(); ++i) {
                final CheckBoardBlock b = this.board.get(this.order[i]);
                b.setBorder(null);
            }
            this.aiFlag = "human";
            AIRobot.robotMove(this);
        }
        else if (item.getText().equals(MenuItems.items[7]) && item.isSelected()) {
            for (int i = 0; i < this.board.size(); ++i) {
                final CheckBoardBlock b = this.board.get(this.order[i]);
                b.setBorder(null);
            }
            this.aiFlag = "black";
            AIRobot.robotMove(this);
        }
        else if (item.getText().equals(MenuItems.items[8]) && item.isSelected()) {
            for (int i = 0; i < this.board.size(); ++i) {
                final CheckBoardBlock b = this.board.get(this.order[i]);
                b.setBorder(null);
            }
            this.aiFlag = "white";
            AIRobot.robotMove(this);
        }
    }
}