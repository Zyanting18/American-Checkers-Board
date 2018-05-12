package com.chess.ai;

import com.chess.board.CheckBoardBlock;
import com.chess.board.CheckPiece;
import com.chess.board.GameStatus;
import com.chess.board.MainFrame;
import java.awt.Color;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;
import javax.swing.border.LineBorder;

public class AIRobot
{
  public static final String AI_STATUS_HUMAN = "human";
  public static final String AI_STATUS_BLACK = "black";
  public static final String AI_STATUS_WHITE = "white";
  private static HashMap<String, CheckPiece> tmpPiece = null;
  private static CheckPiece tp = null;
  
  public static HashMap<String, CheckPiece> getPlayerPieces(MainFrame m, HashMap<String, CheckPiece> ps, String flag)
  {
    HashMap<String, CheckPiece> tmp = new HashMap();
    for (int i = 0; i < ps.size(); i++)
    {
      CheckPiece tp = (CheckPiece)ps.get(m.order[i]);
      if (tp.checkPlayer.equals(flag)) {
        tmp.put(tp.position, tp);
      }
    }
    return tmp;
  }
  
  public static void robotMove(MainFrame m)
  {
    if ((m.status.getStatus().equals("EXEC")) && (m.activePlayer.equals(m.aiFlag)))
    {
      int level = 0;
      tmpPiece = m.piece;
      
      tmpPiece = getMovablePieces(m, tmpPiece);
      level = 4;
      if (tmpPiece.size() == 0) {
        return;
      }
      if (getPiecesCanCapture(m, tmpPiece).size() != 0)
      {
        level = 1;
        System.out.println("level:" + level);
        tmpPiece = getPiecesCanCapture(m, tmpPiece);
        System.out.println("level:" + level);
      }
      else if (getPiecesWillBeCaptured(m, getPlayerPieces(m, m.piece, m.aiFlag)).size() != 0)
      {
        level = 2;
        System.out.println("level:" + level);
        tmpPiece = getPiecesWillBeCaptured(m, getPlayerPieces(m, m.piece, m.aiFlag));
        System.out.println("level:" + level);
      }
      else if (getPiecesCanBeKing(m, tmpPiece).size() != 0)
      {
        level = 3;
        System.out.println("level:" + level);
        tmpPiece = getPiecesCanBeKing(m, tmpPiece);
        System.out.println("level:" + level);
      }
      Object[] pieces = tmpPiece.values().toArray();
      for (int i = 0; i < pieces.length; i++) {
        System.out.print(((CheckPiece)pieces[i]).position + " ");
      }
      if (pieces.length == 0) {
        return;
      }
      Random random = new Random();
      int index = random.nextInt(pieces.length);
      tp = (CheckPiece)pieces[index];
      
      Vector<CheckBoardBlock> avaiBlocks = m.getMovablePosition(tp);
      if (level == 4)
      {
        System.out.println("level:" + level);
        changePiecePosition(m, tp, ((CheckBoardBlock)avaiBlocks.get(random.nextInt(avaiBlocks.size() - 1) + 1)).position);
      }
      else if (level == 1)
      {
        System.out.println("level:" + level);
        
        Vector<CheckBoardBlock> capturableBlocks = new Vector();
        for (int i = 0; i < avaiBlocks.size(); i++)
        {
          int fv = Integer.parseInt(tp.position.substring(1));
          char th = ((CheckBoardBlock)avaiBlocks.get(i)).position.charAt(0);
          int tv = Integer.parseInt(((CheckBoardBlock)avaiBlocks.get(i)).position.substring(1));
          if (Math.abs(fv - tv) == 2) {
            capturableBlocks.add((CheckBoardBlock)avaiBlocks.get(i));
          }
        }
        changePiecePosition(m, tp, ((CheckBoardBlock)capturableBlocks.get(random.nextInt(capturableBlocks.size()))).position);
      }
      else if (level == 2)
      {
        System.out.println("level:" + level + "    " + tp.position);
        
        char h = tp.position.charAt(0);
        int v = Integer.parseInt(tp.position.substring(1));
        
        CheckPiece t1 = (CheckPiece)m.piece.get(Character.toString((char)(h + '\001')) + (v + 1));
        CheckPiece t2 = (CheckPiece)m.piece.get(Character.toString((char)(h - '\001')) + (v + 1));
        CheckPiece t3 = (CheckPiece)m.piece.get(Character.toString((char)(h + '\001')) + (v - 1));
        CheckPiece t4 = (CheckPiece)m.piece.get(Character.toString((char)(h - '\001')) + (v - 1));
        if (tp.checkPlayer.equals("white"))
        {
          if ((t1 != null) && (t1.checkPlayer.equals("black")) && (t4.checkPlayer.equals("empty")) && (getHelpPiece(m, tp, t4) != null)) {
            changePiecePosition(m, getHelpPiece(m, tp, t4), t4.position);
          } else if ((t2 != null) && (t2.checkPlayer.equals("black")) && (t3.checkPlayer.equals("empty")) && (getHelpPiece(m, tp, t3) != null)) {
            changePiecePosition(m, getHelpPiece(m, tp, t3), t3.position);
          } else if ((t3 != null) && (t3.checkPlayer.equals("black")) && (t3.getText() != null) && (t2.checkPlayer.equals("empty")) && (getHelpPiece(m, tp, t2) != null)) {
            changePiecePosition(m, getHelpPiece(m, tp, t2), t2.position);
          } else if ((t4 != null) && (t4.checkPlayer.equals("black")) && (t4.getText() != null) && (t1.checkPlayer.equals("empty")) && (getHelpPiece(m, tp, t1) != null)) {
            changePiecePosition(m, getHelpPiece(m, tp, t1), t1.position);
          }
        }
        else if (tp.checkPlayer.equals("black")) {
          if ((t1 != null) && (t1.checkPlayer.equals("white")) && (t1.getText() != null) && (t4.checkPlayer.equals("empty")) && (getHelpPiece(m, tp, t4) != null)) {
            changePiecePosition(m, getHelpPiece(m, tp, t4), t4.position);
          } else if ((t2 != null) && (t2.checkPlayer.equals("white")) && (t2.getText() != null) && (t3.checkPlayer.equals("empty")) && (getHelpPiece(m, tp, t3) != null)) {
            changePiecePosition(m, getHelpPiece(m, tp, t3), t3.position);
          } else if ((t3 != null) && (t3.checkPlayer.equals("white")) && (t2.checkPlayer.equals("empty")) && (getHelpPiece(m, tp, t2) != null)) {
            changePiecePosition(m, getHelpPiece(m, tp, t2), t2.position);
          } else if ((t4 != null) && (t4.checkPlayer.equals("white")) && (t1.checkPlayer.equals("empty")) && (getHelpPiece(m, tp, t1) != null)) {
            changePiecePosition(m, getHelpPiece(m, tp, t1), t1.position);
          }
        }
      }
      else if (level == 3)
      {
        System.out.println("level:" + level);
        changePiecePosition(m, tp, ((CheckBoardBlock)avaiBlocks.get(random.nextInt(avaiBlocks.size() - 1) + 1)).position);
      }
    }
  }
  
  public static HashMap<String, CheckPiece> getMovablePieces(MainFrame m, HashMap<String, CheckPiece> ps)
  {
    HashMap<String, CheckPiece> tmp = new HashMap();
    
    Object[] pieces = ps.values().toArray();
    for (int i = 0; i < pieces.length; i++)
    {
      CheckPiece tp = (CheckPiece)pieces[i];
      if ((tp.checkPlayer.equals(m.aiFlag)) && (m.getMovablePosition(tp).size() != 1)) {
        tmp.put(tp.position, tp);
      }
    }
    return tmp;
  }
  
  public static HashMap<String, CheckPiece> getPiecesCanCapture(MainFrame m, HashMap<String, CheckPiece> ps)
  {
    HashMap<String, CheckPiece> tmp = new HashMap();
    
    Object[] pieces = ps.values().toArray();
    for (int i = 0; i < pieces.length; i++)
    {
      CheckPiece tp = (CheckPiece)pieces[i];
      
      Vector<CheckBoardBlock> avaiBlocks = m.getMovablePosition(tp);
      for (int j = 0; j < avaiBlocks.size(); j++)
      {
        int a = Integer.parseInt(((CheckBoardBlock)avaiBlocks.get(j)).position.substring(1));
        int b = Integer.parseInt(tp.position.substring(1));
        if (Math.abs(a - b) == 2)
        {
          tmp.put(tp.position, tp);
          break;
        }
      }
    }
    return tmp;
  }
  
  public static HashMap<String, CheckPiece> getPiecesWillBeCaptured(MainFrame m, HashMap<String, CheckPiece> ps)
  {
    HashMap<String, CheckPiece> tmp = new HashMap();
    
    Object[] pieces = ps.values().toArray();
    for (int i = 0; i < pieces.length; i++)
    {
      CheckPiece tp = (CheckPiece)pieces[i];
      
      CheckPiece hp = getPieceCanBeHelp(m, tp);
      if (hp != null) {
        tmp.put(hp.position, hp);
      }
    }
    return tmp;
  }
  
  public static CheckPiece getPieceCanBeHelp(MainFrame m, CheckPiece tp)
  {
    char h = tp.position.charAt(0);
    int v = Integer.parseInt(tp.position.substring(1));
    
    CheckPiece t1 = (CheckPiece)m.piece.get(Character.toString((char)(h + '\001')) + (v + 1));
    CheckPiece t2 = (CheckPiece)m.piece.get(Character.toString((char)(h - '\001')) + (v + 1));
    CheckPiece t3 = (CheckPiece)m.piece.get(Character.toString((char)(h + '\001')) + (v - 1));
    CheckPiece t4 = (CheckPiece)m.piece.get(Character.toString((char)(h - '\001')) + (v - 1));
    if (tp.checkPlayer.equals("white"))
    {
      if ((t1 != null) && (t4 != null) && (t1.checkPlayer.equals("black")) && (t4.checkPlayer.equals("empty")) && (getHelpPiece(m, tp, t4) != null))
      {
        System.out.println(tp.checkPlayer + " " + tp.position + " is in danger1");
        return tp;
      }
      if ((t2 != null) && (t3 != null) && (t2.checkPlayer.equals("black")) && (t3.checkPlayer.equals("empty")) && (getHelpPiece(m, tp, t3) != null))
      {
        System.out.println(tp.checkPlayer + " " + tp.position + " is in danger2");
        return tp;
      }
      if ((t3 != null) && (t2 != null) && (t3.checkPlayer.equals("black")) && (t3.getText() != null) && (t2.checkPlayer.equals("empty")) && (getHelpPiece(m, tp, t2) != null))
      {
        System.out.println(tp.checkPlayer + " " + tp.position + " is in danger3");
        return tp;
      }
      if ((t4 != null) && (t1 != null) && (t4.checkPlayer.equals("black")) && (t4.getText() != null) && (t1.checkPlayer.equals("empty")) && (getHelpPiece(m, tp, t1) != null))
      {
        System.out.println(tp.checkPlayer + " " + tp.position + " is in danger4");
        return tp;
      }
    }
    else if (tp.checkPlayer.equals("black"))
    {
      if ((t1 != null) && (t4 != null) && (t1.checkPlayer.equals("white")) && (t1.getText() != null) && (t4.checkPlayer.equals("empty")) && (getHelpPiece(m, tp, t4) != null))
      {
        System.out.println(tp.checkPlayer + " " + tp.position + " is in danger5");
        return tp;
      }
      if ((t2 != null) && (t3 != null) && (t2.checkPlayer.equals("white")) && (t2.getText() != null) && (t3.checkPlayer.equals("empty")) && (getHelpPiece(m, tp, t3) != null))
      {
        System.out.println(tp.checkPlayer + " " + tp.position + " is in danger6");
        return tp;
      }
      if ((t3 != null) && (t2 != null) && (t3.checkPlayer.equals("white")) && (t2.checkPlayer.equals("empty")) && (getHelpPiece(m, tp, t2) != null))
      {
        System.out.println(tp.checkPlayer + " " + tp.position + " is in danger7");
        return tp;
      }
      if ((t4 != null) && (t1 != null) && (t4.checkPlayer.equals("white")) && (t1.checkPlayer.equals("empty")) && (getHelpPiece(m, tp, t1) != null))
      {
        System.out.println(tp.checkPlayer + " " + tp.position + " is in danger8");
        return tp;
      }
    }
    return null;
  }
  
  public static CheckPiece getHelpPiece(MainFrame m, CheckPiece p, CheckPiece helpP)
  {
    char ph = p.position.charAt(0);
    int pv = Integer.parseInt(p.position.substring(1));
    char hh = helpP.position.charAt(0);
    int hv = Integer.parseInt(helpP.position.substring(1));
    
    CheckPiece t1 = (CheckPiece)m.piece.get(Character.toString((char)(hh + '\001')) + (hv + 1));
    CheckPiece t2 = (CheckPiece)m.piece.get(Character.toString((char)(hh - '\001')) + (hv + 1));
    CheckPiece t3 = (CheckPiece)m.piece.get(Character.toString((char)(hh + '\001')) + (hv - 1));
    CheckPiece t4 = (CheckPiece)m.piece.get(Character.toString((char)(hh - '\001')) + (hv - 1));
    if ((p.checkPlayer.equals("white")) && (t1 != null) && (p.position.equals(t1.position)))
    {
      System.out.println("getHelpPiece1:" + t1.checkPlayer + "  " + t1.position);
      if ((t2 != null) && (t2.checkPlayer.equals("white")) && (t2.getText() != null)) {
        return t2;
      }
      if ((t3 != null) && (t3.checkPlayer.equals("white"))) {
        return t3;
      }
      if ((t4 != null) && (t4.checkPlayer.equals("white"))) {
        return t4;
      }
    }
    else if ((p.checkPlayer.equals("white")) && (t2 != null) && (p.position.equals(t2.position)))
    {
      System.out.println("getHelpPiece2:" + t2.checkPlayer + "  " + t2.position);
      if ((t1 != null) && (t1.checkPlayer.equals("white")) && (t1.getText() != null)) {
        return t1;
      }
      if ((t3 != null) && (t3.checkPlayer.equals("white"))) {
        return t3;
      }
      if ((t4 != null) && (t4.checkPlayer.equals("white"))) {
        return t4;
      }
    }
    else if ((p.checkPlayer.equals("white")) && (t3 != null) && (p.position.equals(t3.position)))
    {
      System.out.println("getHelpPiece3:" + t3.checkPlayer + "  " + t3.position);
      if ((t1 != null) && (t1.checkPlayer.equals("white")) && (t1.getText() != null)) {
        return t1;
      }
      if ((t2 != null) && (t2.checkPlayer.equals("white")) && (t2.getText() != null)) {
        return t2;
      }
      if ((t4 != null) && (t4.checkPlayer.equals("white"))) {
        return t4;
      }
    }
    else if ((p.checkPlayer.equals("white")) && (t4 != null) && (p.position.equals(t4.position)))
    {
      System.out.println("getHelpPiece4:" + t4.checkPlayer + "  " + t4.position);
      if ((t1 != null) && (t1.checkPlayer.equals("white")) && (t1.getText() != null)) {
        return t1;
      }
      if ((t2 != null) && (t2.checkPlayer.equals("white")) && (t2.getText() != null)) {
        return t2;
      }
      if ((t3 != null) && (t3.checkPlayer.equals("white"))) {
        return t3;
      }
    }
    else if ((p.checkPlayer.equals("black")) && (t1 != null) && (p.position.equals(t1.position)))
    {
      System.out.println("getHelpPiece5:" + t1.checkPlayer + "  " + t1.position);
      if ((t2 != null) && (t2.checkPlayer.equals("black")))
      {
        System.out.println("t2");return t2;
      }
      if ((t3 != null) && (t3.checkPlayer.equals("black")) && (t3.getText() != null))
      {
        System.out.println("t3");return t3;
      }
      if ((t4 != null) && (t4.checkPlayer.equals("black")) && (t4.getText() != null))
      {
        System.out.println("t4");return t4;
      }
    }
    else if ((p.checkPlayer.equals("black")) && (t2 != null) && (p.position.equals(t2.position)))
    {
      System.out.println("getHelpPiece6:" + t2.checkPlayer + "  " + t2.position);
      if ((t1 != null) && (t1.checkPlayer.equals("black")))
      {
        System.out.println("t1");return t1;
      }
      if ((t3 != null) && (t3.checkPlayer.equals("black")) && (t3.getText() != null))
      {
        System.out.println("t3");return t3;
      }
      if ((t4 != null) && (t4.checkPlayer.equals("black")) && (t4.getText() != null))
      {
        System.out.println("t4");return t4;
      }
    }
    else if ((p.checkPlayer.equals("black")) && (t3 != null) && (p.position.equals(t3.position)))
    {
      System.out.println("getHelpPiece7:" + t3.checkPlayer + "  " + t3.position);
      if ((t1 != null) && (t1.checkPlayer.equals("black")))
      {
        System.out.println("t1");return t1;
      }
      if ((t2 != null) && (t2.checkPlayer.equals("black")))
      {
        System.out.println("t2");return t2;
      }
      if ((t4 != null) && (t4.checkPlayer.equals("black")) && (t4.getText() != null))
      {
        System.out.println("t4");return t4;
      }
    }
    else if ((p.checkPlayer.equals("black")) && (t4 != null) && (p.position.equals(t4.position)))
    {
      System.out.println("getHelpPiece8:" + t4.checkPlayer + "  " + t4.position);
      if ((t1 != null) && (t1.checkPlayer.equals("black")))
      {
        System.out.println("t1");return t1;
      }
      if ((t2 != null) && (t2.checkPlayer.equals("black")))
      {
        System.out.println("t2");return t2;
      }
      if ((t3 != null) && (t3.checkPlayer.equals("black")) && (t3.getText() != null))
      {
        System.out.println("t3");return t3;
      }
    }
    else
    {
      return null;
    }
    return null;
  }
  
  public static HashMap<String, CheckPiece> getPiecesCanBeKing(MainFrame m, HashMap<String, CheckPiece> ps)
  {
    HashMap<String, CheckPiece> tmp = new HashMap();
    
    Object[] pieces = ps.values().toArray();
    for (int i = 0; i < pieces.length; i++)
    {
      CheckPiece t1 = null;
      CheckPiece t2 = null;
      
      CheckPiece tp = (CheckPiece)pieces[i];
      char h = tp.position.charAt(0);
      int v = Integer.parseInt(tp.position.substring(1));
      if ((tp.checkPlayer.equals("white")) && (tp.getText() == null) && (v == 7))
      {
        String tmp1 = Character.toString((char)(h + '\001')) + (v + 1);
        t1 = (CheckPiece)m.piece.get(tmp1);
        String tmp2 = Character.toString((char)(h - '\001')) + (v + 1);
        t2 = (CheckPiece)m.piece.get(tmp2);
      }
      else if ((tp.checkPlayer.equals("black")) && (tp.getText() == null) && (v == 2))
      {
        String tmp1 = Character.toString((char)(h + '\001')) + (v - 1);
        t1 = (CheckPiece)m.piece.get(tmp1);
        String tmp2 = Character.toString((char)(h - '\001')) + (v - 1);
        t2 = (CheckPiece)m.piece.get(tmp2);
      }
      if (((t1 != null) && (t1.checkPlayer.equals("empty"))) || ((t2 != null) && (t2.checkPlayer.equals("empty")))) {
        tmp.put(tp.position, tp);
      }
    }
    return tmp;
  }
  
  public static void changePiecePosition(MainFrame m, CheckPiece p1, String newP)
  {
    ((CheckBoardBlock)m.board.get(p1.position)).setBorderPainted(true);
    ((CheckBoardBlock)m.board.get(newP)).setBorderPainted(true);
    ((CheckBoardBlock)m.board.get(p1.position)).setBorder(new LineBorder(Color.ORANGE, 4));
    ((CheckBoardBlock)m.board.get(newP)).setBorder(new LineBorder(Color.ORANGE, 4));
    m.changePosition((CheckPiece)m.piece.get(p1.position), newP);
  }
}
