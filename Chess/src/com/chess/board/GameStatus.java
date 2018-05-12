package com.chess.board;

public class GameStatus
{
  public static final String GAME_STATUS_INIT = "INIT";
  public static final String GAME_STATUS_EDIT = "EDIT";
  public static final String GAME_STATUS_EXEC = "EXEC";
  public static final String GAME_STATUS_OVER = "OVER";
  private String status;
  
  public String getStatus()
  {
    return this.status;
  }
  
  public void setStatusInit()
  {
    this.status = "INIT";
  }
  
  public void setStatusEdit()
  {
    this.status = "EDIT";
  }
  
  public void setStatusExec()
  {
    this.status = "EXEC";
  }
  
  public void setStatusOver()
  {
    this.status = "OVER";
  }
  
  public GameStatus()
  {
    this.status = "INIT";
  }
  
  public static void main(String[] a) {}
}
